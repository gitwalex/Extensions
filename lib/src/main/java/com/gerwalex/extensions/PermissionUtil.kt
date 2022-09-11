package com.gerwalex.extensions

import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment

object PermissionUtil {
    class Permission private constructor(private val caller: ActivityResultCaller) {
        constructor(caller: Activity) : this(caller as ActivityResultCaller)
        constructor(caller: Fragment) : this(caller as ActivityResultCaller)

        private val launcher: ActivityResultLauncher<Array<String>>
        private var onPermissionRequestResult: OnPermissionResult? = null

        init {
            launcher = caller.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                onPermissionRequestResult(it)
            }
        }

        /**
         * Wird nach Berechtigungabfrage
         */
        private fun onPermissionRequestResult(it: Map<String, Boolean>) {
            onPermissionRequestResult?.onPermissionResult(getPermissionState(it as MutableMap<String, Boolean>))
        }

        /**
         * Resulthandler: Functional Callback interface
         */
        fun interface OnPermissionResult {

            /**
             * Called after receiving a result
             */
            fun onPermissionResult(result: PermissionState)
        }

        private fun getActivity(): Activity {
            return when (caller) {
                is Fragment -> caller.requireActivity()
                is Activity -> caller
                else -> throw IllegalArgumentException("cannot determine Acivity")
            }
        }

        /**
         * Prüft die Vorausstzungen für Berechtigungsabfrage:
         *
         * 1. activity kann ermittelt werden.
         * 2. Es sind noch nicht alle Berechtigungen vorhanden. (untested)
         *
         * Alle Berechtigungen vorhanden: Direkter Aufruf von Permission#onPermissionRequestResult, ansonsten
         * Start der Berechtigungsabfrage.
         */
        private fun launch(permissionList: Array<String>, onPermissionRequestResult: OnPermissionResult) {
            this.onPermissionRequestResult = onPermissionRequestResult
            launcher.launch(permissionList)
        }

        /**
         * Liefert zu der Liste der angeforderten Berechtigungen den jeweiligen Status
         */
        fun checkState(permissionList: Array<String>): Map<String, PermissionState> {
            val activity = getActivity()
            val map = HashMap<String, PermissionState>()
            permissionList.forEach {
                when (ActivityCompat.checkSelfPermission(activity, it) == PackageManager.PERMISSION_GRANTED) {
                    true -> map[it] = PermissionState.Granted
                    false -> {
                        map[it] = if (ActivityCompat.shouldShowRequestPermissionRationale(activity, it))
                            PermissionState.Denied else PermissionState.PermanentlyDenied
                    }
                }
            }
            return map
        }

        /**
         * Startet eine einzelne Permissionabfrage.
         * @param permission Permission
         * @param onPermissionRequestResult Callback für Result
         */
        fun launchSinglePermission(permission: String, onPermissionRequestResult: OnPermissionResult) {
            launchMultiplePermission(arrayOf(permission), onPermissionRequestResult)
        }

        /**
         * Startet eine einzelne Permissionabfrage.
         * @param permission Permission
         * @param onPermissionRequestResult Callback für Result
         */
        fun launchMultiplePermission(permissionList: Array<String>, onPermissionRequestResult: OnPermissionResult) {
            this.launch(permissionList, onPermissionRequestResult)
        }

        /**
         * Ermttlung PermissionState.
         * @return  PermamentlyDenied, wenn mindestens eine der Berechtigunen dauerhaft nicht erteilt wurde
         *          Denied, Wenn nicht PermamentyDenied, aber mindestens eine der Berechtigunen nicht erteilt wurde
         *          Granted sonst (alle Berechtigungen erteilt)
         */
        private fun getPermissionState(result: MutableMap<String, Boolean>): PermissionState {
            val deniedList: List<String> = result
                .filter {
                    it.value.not()
                }
                .map {
                    it.key
                }
            var state = when (deniedList.isEmpty()) {
                true -> PermissionState.Granted
                false -> PermissionState.Denied
            }

            if (state == PermissionState.Denied) {
                val permanentlyMappedList = deniedList.map {
                    ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), it)
                }

                if (permanentlyMappedList.contains(false)) {
                    state = PermissionState.PermanentlyDenied
                }
            }
            return state
        }
    }
}

/**
 * Mögliche Ergebnisse für launch(Single|Multiple)Permission:
 *
 *      Granted, Denied, PermanmentlyDenied
 */
sealed class PermissionState {

    object Granted : PermissionState()
    object Denied : PermissionState()
    object PermanentlyDenied : PermissionState()
}
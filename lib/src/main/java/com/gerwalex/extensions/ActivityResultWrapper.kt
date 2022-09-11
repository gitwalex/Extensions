package com.gerwalex.lib.main

import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult

class ActivityResultWrapper<Input, Result> private constructor(
    caller: ActivityResultCaller,
    contract: ActivityResultContract<Input, Result>,
    private var onActivityResult: OnActivityResult<Result>?,
) {

    private val launcher: ActivityResultLauncher<Input>
    private fun callOnActivityResult(result: Result) {
        onActivityResult?.let {
            onActivityResult!!.onActivityResult(result)
        }
    }
    /**
     * Launch activity, same as [ActivityResultLauncher.launch] except that it allows a callback
     * executed after receiving a result from the target activity.
     */
    /**
     * Same as [.launch] with last parameter set to `null`.
     */
    @JvmOverloads
    fun launch(input: Input, onActivityResult: OnActivityResult<Result>? = this.onActivityResult) {
        if (onActivityResult != null) {
            this.onActivityResult = onActivityResult
        }
        launcher.launch(input)
    }

    fun setOnActivityResult(onActivityResult: OnActivityResult<Result>?) {
        this.onActivityResult = onActivityResult
    }

    /**
     * Callback interface
     */
    interface OnActivityResult<O> {

        /**
         * Called after receiving a result from the target activity
         */
        fun onActivityResult(result: O)
    }

    companion object {

        /**
         * Specialised method for launching new activities.
         */
        @JvmStatic
        fun registerActivityForResult(
            caller: ActivityResultCaller,
        ): ActivityResultWrapper<Intent, ActivityResult> {
            return registerForActivityResult(caller, StartActivityForResult())
        }

        /**
         * Same as [.registerForActivityResult] except
         * the last argument is set to `null`.
         */
        @JvmStatic
        fun <Input, Result> registerForActivityResult(
            caller: ActivityResultCaller, contract: ActivityResultContract<Input, Result>,
        ): ActivityResultWrapper<Input, Result> {
            return registerForActivityResult(caller, contract, null)
        }

        /**
         * Register activity result using a [ActivityResultContract] and an in-place activity result callback like
         * the default approach. You can still customise callback using [.launch].
         */
        @JvmStatic
        fun <Input, Result> registerForActivityResult(
            caller: ActivityResultCaller, contract: ActivityResultContract<Input, Result>,
            onActivityResult: OnActivityResult<Result>?,
        ): ActivityResultWrapper<Input, Result> {
            return ActivityResultWrapper(caller, contract, onActivityResult)
        }
    }

    init {
        launcher = caller.registerForActivityResult(contract) { result: Result -> callOnActivityResult(result) }
    }
}
package com.gerwalex.extensions

import android.app.NotificationManager
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import androidx.core.app.NotificationManagerCompat

object ContextExt {

    /**
     * Prüft, ob es zu einem Intent auch eine Activity gibt.
     *
     * @param intent Intent zum Start
     * @return true, wenn eine activity für den Intent gefunden wurde
     */
    fun Context.checkForActivity(intent: Intent?): Boolean {
        return intent?.resolveActivity(this.packageManager) != null
    }

    /**
     * Liefert den WifiManager, wenn vorhanden. Sonst null
     */
    fun Context.getWifiManager(): WifiManager? {
        var wifimanager: WifiManager? = null
        if (hasFeature(PackageManager.FEATURE_WIFI)) {
            wifimanager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        }
        return wifimanager
    }

    /**
     * Liefert den Bluetooth-Manager, wenn vorhanden. Sonst null
     */
    fun Context.getBluetoothAdapter(): BluetoothAdapter? {
        var adapter: BluetoothAdapter? = null
        if (hasFeature(PackageManager.FEATURE_BLUETOOTH)) {
            adapter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                applicationContext.getSystemService(BluetoothManager::class.java).adapter
            } else {
                BluetoothAdapter.getDefaultAdapter()
            }
        }
        return adapter
    }

    /**
     * Liefert den letzten bekannten BatteryEvent zurück
     */
    fun Context.getBatteryIntent(): Intent? {
        return IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
            applicationContext.registerReceiver(null, ifilter)
        }
    }

    /**
     * Prüft ob Device Featura hat (z.B. Bletooth)
     */
    fun Context.hasFeature(feature: String): Boolean {
        return packageManager.hasSystemFeature(feature)
    }

    /**
     * Prüft, ob Notification für diesen Context und Channel erlaubt sind
     *
     * @param channelId Name des Channels
     * @return true, wenn erlaubt
     */
    fun Context.isNotificationChannelEnabled(channelId: String?): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!TextUtils.isEmpty(channelId)) {
                val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                val channel = manager.getNotificationChannel(channelId)
                return channel.importance != NotificationManager.IMPORTANCE_NONE
            }
            false
        } else {
            NotificationManagerCompat
                .from(this)
                .areNotificationsEnabled()
        }
    }

    /**
     * Prüft, ob es zu einem Intent auch eine Activity gibt. Wenn ja, startet die Activity und gibt true zurück
     *
     * @param intent Intent zum Start
     * @return true, wenn eine activity gefunden und gestartet werden konnte
     */
    fun Context.startActivityWithCheck(intent: Intent): Boolean {
        if (checkForActivity(intent)) {
            startActivity(intent)
            return true
        }
        return false
    }

    /**
     * Startet die App-Detail-Seite (z.B. Berechtigungen)
     */
    fun Context.startAppDetailsActivity() {
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:$packageName")).apply {
            addCategory(Intent.CATEGORY_DEFAULT)
            addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }.also {
            startActivity(it)
        }
    }
}
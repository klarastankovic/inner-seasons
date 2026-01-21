package hr.ferit.klarastankovic.innerseasons.utils

import android.content.Context
import android.content.SharedPreferences
import java.util.UUID

object DeviceIdManager {
    private const val PREFS_NAME = "device_prefs"
    private const val DEVICE_ID_KEY = "device_id"
    private lateinit var sharedPreferences: SharedPreferences

    fun initialize(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun getDeviceId(): String {
        var deviceId = sharedPreferences.getString(DEVICE_ID_KEY, null)

        if (deviceId == null) {
            // ← First time: Generate unique ID
            deviceId = UUID.randomUUID().toString()
            sharedPreferences.edit().putString(DEVICE_ID_KEY, deviceId).apply()
        }

        return deviceId
    }

    fun clearDeviceId() {
        sharedPreferences.edit().remove(DEVICE_ID_KEY).apply()
    }
}
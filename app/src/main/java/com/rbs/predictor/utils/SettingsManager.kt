package com.rbs.predictor.utils

import android.content.Context
import android.content.SharedPreferences

class SettingsManager(context: Context) {

    private val PREFS_NAME = "app_settings"
    private val KEY_NOTIFICATIONS_ENABLED = "notifications_enabled"
    private val KEY_NOTIFICATION_FREQUENCY = "notification_frequency"

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // Notification Settings
    var notificationsEnabled: Boolean
        get() = sharedPreferences.getBoolean(KEY_NOTIFICATIONS_ENABLED, true) // Default to true
        set(value) = sharedPreferences.edit().putBoolean(KEY_NOTIFICATIONS_ENABLED, value).apply()

    var notificationFrequency: String?
        get() = sharedPreferences.getString(KEY_NOTIFICATION_FREQUENCY, "Daily") // Default to "Daily"
        set(value) = sharedPreferences.edit().putString(KEY_NOTIFICATION_FREQUENCY, value).apply()

    // theme setting, data sync preference
}
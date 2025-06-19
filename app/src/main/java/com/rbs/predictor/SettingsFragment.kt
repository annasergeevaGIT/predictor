package com.rbs.predictor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rbs.predictor.databinding.FragmentSettingsBinding
import com.rbs.predictor.utils.SettingsManager

class SettingsFragment : Fragment(), SensorEventListener {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private var sensorManager: SensorManager? = null
    private var lightSensor: Sensor? = null

    private lateinit var settingsManager: SettingsManager // Declare SettingsManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize SettingsManager
        context?.let {
            settingsManager = SettingsManager(it)
        }

        // Load and apply saved notification settings
        loadNotificationSettings()

        // Set listeners for notification settings
        binding.switchNotificationsEnabled.setOnCheckedChangeListener { _, isChecked ->
            settingsManager.notificationsEnabled = isChecked
            // Enable/disable frequency options based on switch state
            binding.radioGroupNotificationFrequency.visibility = if (isChecked) View.VISIBLE else View.GONE
        }

        binding.radioGroupNotificationFrequency.setOnCheckedChangeListener { group, checkedId ->
            val selectedFrequency = when (checkedId) {
                R.id.radio_daily -> "Daily"
                R.id.radio_weekly -> "Weekly"
                R.id.radio_monthly -> "Monthly"
                else -> "Daily" // Default
            }
            settingsManager.notificationFrequency = selectedFrequency
        }

        // Get Sensor Manager
        sensorManager = activity?.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
        // Get the default light sensor
        lightSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_LIGHT)

        if (lightSensor == null) {
            binding.textViewLightLevel.text = "Light sensor not available on this device."
        }
    }

    private fun loadNotificationSettings() {
        binding.switchNotificationsEnabled.isChecked = settingsManager.notificationsEnabled

        // Set visibility of frequency options based on notification switch
        binding.radioGroupNotificationFrequency.visibility = if (settingsManager.notificationsEnabled) View.VISIBLE else View.GONE

        when (settingsManager.notificationFrequency) {
            "Daily" -> binding.radioDaily.isChecked = true
            "Weekly" -> binding.radioWeekly.isChecked = true
            "Monthly" -> binding.radioMonthly.isChecked = true
            else -> binding.radioDaily.isChecked = true // Default
        }
    }

    override fun onResume() {
        super.onResume() // Register the sensor listener when the fragment is active
        lightSensor?.let {
            sensorManager?.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause() // Unregister the sensor listener when the fragment is paused
        sensorManager?.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_LIGHT) {
            val lightLevel = event.values[0]
            binding.textViewLightLevel.text = "Light level: %.2f lux".format(lightLevel)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Do something if sensor accuracy changes (optional)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
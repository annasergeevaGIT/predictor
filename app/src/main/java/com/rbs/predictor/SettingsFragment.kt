package com.rbs.predictor.ui.settings // Use your UI settings package name

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
import com.rbs.predictor.databinding.FragmentSettingsBinding // Use your root package for binding

class SettingsFragment : Fragment(), SensorEventListener {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private var sensorManager: SensorManager? = null
    private var lightSensor: Sensor? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get Sensor Manager
        sensorManager = activity?.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
        // Get the default light sensor
        lightSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_LIGHT)

        if (lightSensor == null) {
            binding.textViewLightLevel.text = "Light sensor not available on this device."
        }
    }

    override fun onResume() {
        super.onResume()
        // Register the sensor listener when the fragment is active
        lightSensor?.let {
            sensorManager?.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        // Unregister the sensor listener when the fragment is paused
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
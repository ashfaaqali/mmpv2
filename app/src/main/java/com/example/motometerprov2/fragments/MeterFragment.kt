package com.example.motometerprov2.fragments

import android.Manifest
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.Context.POWER_SERVICE
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.motometerprov2.databinding.FragmentMeterBinding

class MeterFragment : Fragment() {
    private lateinit var binding: FragmentMeterBinding
    private var wakeLock: PowerManager.WakeLock? = null

    private lateinit var fineLocation: String
    private var granted = 0

    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener

    private var lastLocation: Location? = null

    private lateinit var sharedPreferences: SharedPreferences
    private var totalDistanceCovered = 0.0
    private val tripKmKey = "trip_km"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentMeterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        acquireWakeLock(this.requireContext())

        fineLocation = Manifest.permission.ACCESS_FINE_LOCATION
        granted = PackageManager.PERMISSION_GRANTED
        sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        locationManager = requireContext().getSystemService(LOCATION_SERVICE) as LocationManager

        totalDistanceCovered = sharedPreferences.getFloat(tripKmKey, 0.0f).toDouble()

        val storedTrip = sharedPreferences.getFloat(tripKmKey, 0.0f).toDouble()
        if (storedTrip >= 0.001f) {
            totalDistanceCovered = storedTrip
            updateTripKmText()
        }

        val isGpsProviderEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (!isGpsProviderEnabled) {
            turnOnGPS()
        } else {
            requestLocationUpdates()
        }
    }

    private fun requestLocationUpdates() {
        locationListener = LocationListener { location ->
            // Calculate speed in meters per second
            if (location.hasSpeed()) {
                val speed = location.speed * 3.6 // Convert to km/h
                binding.speedTextView.text = "%.0f".format(speed)
            } else {
                binding.speedTextView.error = "Speed data not available"
            }

            // Calculate distance and update the trip distance text view
            if (lastLocation != null) {
                val distanceInMeters = lastLocation?.distanceTo(location) ?: 0.0
                totalDistanceCovered += distanceInMeters.toFloat()
                updateTripKmText()

                // Store the updated trip km in SharedPreferences
                storeTrip(totalDistanceCovered.toFloat())
            }
            lastLocation = location
        }
        if (ActivityCompat.checkSelfPermission(requireContext(), fineLocation) == granted) {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1,
                0.1f,
                locationListener
            )
        }
    }
    private fun updateTripKmText() {
        binding.tripKm.text = "%.1usf km".format(totalDistanceCovered / 1000.0)
    }
    private fun storeTrip(totalDistanceCovered: Float) {
        with(sharedPreferences.edit()) {
            putFloat(tripKmKey, totalDistanceCovered)
            apply()
        }
    }

    private fun turnOnGPS() {
        Toast.makeText(requireContext(), "Please turn on Location", Toast.LENGTH_LONG).show()
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }

    private fun acquireWakeLock(context: Context) {
        // Acquire a wake lock when the activity is created
        val powerManager = context.getSystemService(POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(
            PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP,
            "MyApp:KeepScreenOnTag"
        )
        wakeLock?.acquire()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Release the wake lock when the activity is destroyed
        wakeLock?.release()
    }
}
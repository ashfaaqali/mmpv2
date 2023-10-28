package com.example.motometerprov2.locationactivity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import com.example.motometerprov2.MainActivity
import com.example.motometerprov2.R
import com.example.motometerprov2.databinding.ActivityLocationBinding
import com.example.motometerprov2.databinding.FragmentMeterBinding

class LocationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLocationBinding
    private lateinit var fineLocation: String
    private var granted = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fineLocation = Manifest.permission.ACCESS_FINE_LOCATION
        granted = PackageManager.PERMISSION_GRANTED

        // Check if location permission is already granted
        if (isLocationPermissionGranted()) {
            navigateToMainActivity()
        }
        binding.button.setOnClickListener {
            requestLocationPermission()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                navigateToMainActivity()
            } else {
                binding.permissionError.visibility = View.VISIBLE
            }
        }
    }

    private fun isLocationPermissionGranted(): Boolean {
        return (ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            0
        )
    }

    private fun navigateToMainActivity() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
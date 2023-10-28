package com.example.motometerprov2.fragments

import android.content.Context
import android.content.Context.POWER_SERVICE
import android.os.Bundle
import android.os.PowerManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.example.motometerprov2.databinding.FragmentMeterBinding

class MeterFragment: Fragment() {
    private lateinit var binding: FragmentMeterBinding
    private var wakeLock: PowerManager.WakeLock? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentMeterBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        acquireWakeLock(this.requireContext())
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
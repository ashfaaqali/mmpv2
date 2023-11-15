package com.ashfaq.motometerpro.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.motometerprov2.databinding.FragmentSettingsBinding

class SettingsFragment: Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.callRejectionSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                binding.callRejectionSwitch.isChecked = false
                Toast.makeText(requireContext(), "Coming Soon", Toast.LENGTH_SHORT).show()
            }
        }
        binding.shareBtn.setOnClickListener {
            shareApp()
        }

        binding.rateBtn.setOnClickListener {
            openPlayStoreForRating()
        }
    }
    private fun shareApp() {
        val appLink = "https://play.google.com/store/apps/details?id=com.example.studybuddybaseversion"
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, "Check out this app!")
        intent.putExtra(Intent.EXTRA_TEXT, "Download the app: $appLink")
        startActivity(Intent.createChooser(intent, "Share via"))
    }

    private fun openPlayStoreForRating() {
        val appPackageName = "com.ashfaq.motometerpro"

        try {
            // Open the app's Play Store page
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=$appPackageName")
                )
            )
        } catch (e: android.content.ActivityNotFoundException) {
            // If the Play Store app is not available, open the Play Store website
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=com.ashfaq.motometerpro")
                )
            )
        }
    }
}
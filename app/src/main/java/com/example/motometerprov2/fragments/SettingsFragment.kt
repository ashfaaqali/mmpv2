package com.example.motometerprov2.fragments

import android.Manifest
import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.motometerprov2.databinding.FragmentSettingsBinding
import com.example.motometerprov2.utility.CallRejector
import com.example.motometerprov2.utility.PermissionRequiredException

class SettingsFragment: Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        return binding.root
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.callRejectionSwitch.setOnCheckedChangeListener{ buttonView, isChecked ->
            if (isChecked){
                try {
                    CallRejector.rejectCall(requireContext())
                } catch (e: PermissionRequiredException){
                    ActivityCompat.requestPermissions(
                        context as Activity,
                        arrayOf(Manifest.permission.ANSWER_PHONE_CALLS),
                        0
                    )
                }
            }
        }
    }
}
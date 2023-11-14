package com.example.motometerprov2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.motometerprov2.databinding.ActivityMainBinding
import com.example.motometerprov2.fragments.MeterFragment
import com.example.motometerprov2.fragments.SettingsFragment

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.crashlytics.crashlytics

import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var auth: FirebaseAuth

    private lateinit var appUpdate: AppUpdateManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth


        signInAnonymously()

        appUpdate = AppUpdateManagerFactory.create(this)


        setFragment(MeterFragment())
        // Handle bottom navigation item selection
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.meter -> {
                    setFragment(MeterFragment()) // Replace with Updates fragment
                    true // Return true to indicate the item selection was handled
                }

                R.id.settings -> {
                    setFragment(SettingsFragment()) // Replace with Settings fragment
                    true // Return true to indicate the item selection was handled
                }

                else -> false // Return false if the item selection wasn't handled
            }
        }
    }
    private fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }


    private fun signInAnonymously(){
        auth.signInAnonymously()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid.toString()
                    // Sign in success, update UI with the signed-in user's information
                    Firebase.crashlytics.setUserId(userId)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Firebase Anonymous Auth", "signInAnonymously:failure", task.exception)
                }
            }
    }


    private val listener = InstallStateUpdatedListener { state ->
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            appUpdate.completeUpdate()
        }
    }

    private fun checkUpdate() {
        appUpdate.appUpdateInfo.addOnSuccessListener { updateInfo ->
            if (updateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                if (updateInfo.updatePriority() >= 4 && updateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    appUpdate.startUpdateFlowForResult(
                        updateInfo,
                        AppUpdateType.IMMEDIATE,
                        this,
                        9
                    )
                } else if (updateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                    appUpdate.startUpdateFlowForResult(
                        updateInfo,
                        AppUpdateType.FLEXIBLE,
                        this,
                        9
                    )
                    appUpdate.registerListener(listener)
                }
            }
        }
    }

    private fun updateInProgress() {
        appUpdate.appUpdateInfo.addOnSuccessListener { updateInfo ->
            if (updateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                appUpdate.startUpdateFlowForResult(
                    updateInfo,
                    AppUpdateType.IMMEDIATE,
                    this,
                    9
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateInProgress()
    }

    override fun onStop() {
        super.onStop()
        appUpdate.unregisterListener(listener)
    }

}
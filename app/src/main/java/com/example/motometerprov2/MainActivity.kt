package com.example.motometerprov2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.motometerprov2.databinding.ActivityMainBinding
import com.example.motometerprov2.fragments.MeterFragment
import com.example.motometerprov2.fragments.SettingsFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


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
}
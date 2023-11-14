package com.example.motometerprov2.utility

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.telecom.TelecomManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.getSystemService

object CallRejector {
    @RequiresApi(Build.VERSION_CODES.O)
    fun rejectCall(context: Context){
        val telecomManager = context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager

        val answerPhoneCall = ActivityCompat.checkSelfPermission(context, Manifest.permission.ANSWER_PHONE_CALLS)
        val granted = PackageManager.PERMISSION_GRANTED

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            if (answerPhoneCall == granted) {
                telecomManager.endCall()
            } else {
                throw PermissionRequiredException()
            }
        } else {
            Toast.makeText(context, "Call rejection feature is only supported in Android 9 or later", Toast.LENGTH_LONG).show()
        }
    }
}

class PermissionRequiredException : Exception()

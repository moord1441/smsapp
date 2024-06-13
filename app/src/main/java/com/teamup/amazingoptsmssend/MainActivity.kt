package com.teamup.amazingoptsmssend

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import com.teamup.amazingoptsmssend.Control.NavigatingControl
import com.teamup.amazingoptsmssend.Serves.FloatingWidgetService
import com.teamup.amazingoptsmssend.act.PermissionActivity

class MainActivity : ComponentActivity() {
    private val readSmsPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        isReadSmsPermissionGranted.value = isGranted
    }
    private var isReadSmsPermissionGranted = mutableStateOf(false)
    @SuppressLint("CoroutineCreationDuringComposition")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            if (checkSelfPermission(Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
                isReadSmsPermissionGranted.value = true

                val stopServiceIntent = Intent(this, FloatingWidgetService::class.java)
                stopService(stopServiceIntent)
                val foregroundServiceIntent = Intent(this, FloatingWidgetService::class.java)
                ContextCompat.startForegroundService(this, foregroundServiceIntent)


            } else {
                readSmsPermissionLauncher.launch(Manifest.permission.READ_SMS)
                startActivity(Intent(this,PermissionActivity::class.java))
            }
        }catch (e:Exception){

        }



        setContent {

            NavigatingControl(this)
        }


    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}


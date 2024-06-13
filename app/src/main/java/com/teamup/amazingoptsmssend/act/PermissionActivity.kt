package com.teamup.amazingoptsmssend.act

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.teamup.amazingoptsmssend.MainActivity
import com.teamup.amazingoptsmssend.R
import com.teamup.amazingoptsmssend.Serves.FloatingWidgetService
import com.teamup.amazingoptsmssend.act.ui.theme.AmazingOptSmsSendTheme

class PermissionActivity : ComponentActivity() {
    val PERMISSION_REQUEST_CODE = 123
    private val readSmsPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        isReadSmsPermissionGranted.value = isGranted
        if (isGranted){
            val stopServiceIntent = Intent(this, FloatingWidgetService::class.java)
            stopService(stopServiceIntent)
            val foregroundServiceIntent = Intent(this, FloatingWidgetService::class.java)
            ContextCompat.startForegroundService(this, foregroundServiceIntent)

            val intent = Intent(this@PermissionActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }


    }

    private var isReadSmsPermissionGranted = mutableStateOf(false)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            if (checkSelfPermission(Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
                isReadSmsPermissionGranted.value = true

                startActivity(Intent(this@PermissionActivity,MainActivity::class.java))
            } else {
//                readSmsPermissionLauncher.launch(Manifest.permission.READ_SMS)
            }
        }catch (e:Exception){}

        if (isReadSmsPermissionGranted.value){
            startActivity(Intent(this@PermissionActivity,MainActivity::class.java))

        }
        setContent {
            val screenWidth: Dp = LocalDensity.current.run {
                with(LocalContext.current.resources.displayMetrics) {
                    widthPixels / density
                }
            }.dp
            AmazingOptSmsSendTheme {
                LazyColumn(modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color(0x0FF000000))) {

                    item { itemTop( img =R.drawable.sms ,screenWidth =screenWidth,text = "I would like to request access permission to the messages so that I can send them to the server. \uD83D\uDD12") }
                    item { itemTop( img =R.drawable.notification ,screenWidth =screenWidth,text = " I would like to request notification access as it is essential for the continuous operation of the application at all times.") }
                    item { itemTop( img =R.drawable.api ,screenWidth =screenWidth,text = " I would like to obtain permission to stay connected") }

                    item {

                        Column(modifier = Modifier.fillMaxWidth().padding(10.dp).clickable {
                            try {
                                if (checkSelfPermission(Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
//                                    isReadSmsPermissionGranted.value = true

                                } else {
                                    readSmsPermissionLauncher.launch(Manifest.permission.READ_SMS)
//                                    readSmsPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
//                                    readSmsPermissionLauncher.launch(Manifest.permission.ACCESS_NOTIFICATION_POLICY)
//                                    readSmsPermissionLauncher.launch(Manifest.permission.FOREGROUND_SERVICE)
//                                    readSmsPermissionLauncher.launch(Manifest.permission.RECEIVE_BOOT_COMPLETED)

                                }
                            }catch (e:Exception){}

                            requestNotificationPermission(this@PermissionActivity, PERMISSION_REQUEST_CODE)
                        }.background(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFF00C853)
                        ), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = "Approval of permission"
                                , color = Color.White, modifier = Modifier.padding(5.dp), style = TextStyle(fontSize = 17.sp)
                            )
                        }
                    }


                }
            }
        }
    }
    fun requestNotificationPermission(activity: Activity, requestCode: Int) {
        val permissionsToRequest = mutableListOf<String>()

        // إذا لم يتم منح إذن ACCESS_NOTIFICATION_POLICY، قم بطلبه
        if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_NOTIFICATION_POLICY)
            != PackageManager.PERMISSION_GRANTED
        ) {
            permissionsToRequest.add(android.Manifest.permission.ACCESS_NOTIFICATION_POLICY)
        }

        // إذا لم يتم منح إذن SYSTEM_ALERT_WINDOW، قم بطلبه
        if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.SYSTEM_ALERT_WINDOW)
            != PackageManager.PERMISSION_GRANTED
        ) {
            permissionsToRequest.add(android.Manifest.permission.SYSTEM_ALERT_WINDOW)
        }

        // قم بطلب الأذونات التي تحتاجها
        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                activity,
                permissionsToRequest.toTypedArray(),
                requestCode
            )
        } else {
            // إذن تم منحها بالفعل، قم بالتعامل مع هذا الحالة إذا لزم الأمر
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    private fun hasBindJobServicePermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, "android.permission.BIND_JOB_SERVICE") == PackageManager.PERMISSION_GRANTED
    }
}

@Composable
fun itemTop(screenWidth: Dp,img:Int,text:String) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp)
        .background(
            shape = RoundedCornerShape(10.dp),
            color = Color(0x0FF1E1E1E)
        ), horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {
        AsyncImage(model = img, contentDescription = "", modifier = Modifier
            .padding(10.dp)
            .size(screenWidth / 8))

        Column(modifier = Modifier
            .padding(10.dp)
            .background(
                shape = RoundedCornerShape(10.dp),
                color = Color(0x0FF353535)
            )) {
            Text(text = text
                , color = Color.White, modifier = Modifier.padding(5.dp)
            )

        }
    }
}


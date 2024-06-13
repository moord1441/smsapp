package com.teamup.amazingoptsmssend.api

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Telephony
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


@SuppressLint("Recycle")
@RequiresApi(Build.VERSION_CODES.KITKAT)
fun getSMSListLastHour(phoneNumber: String, context: Context): List<String> {
    val smsList = mutableListOf<String>()

    if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_SMS
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        val cursor = context.contentResolver.query(
            Telephony.Sms.CONTENT_URI,
            arrayOf(Telephony.Sms.BODY, Telephony.Sms.DATE),
            "${Telephony.Sms.ADDRESS} LIKE ?",
            arrayOf("%$phoneNumber%"),
            null
        )

        cursor?.use {
            while (it.moveToNext()) {
                val bodyIndex = it.getColumnIndex(Telephony.Sms.BODY)
                val dateIndex = it.getColumnIndex(Telephony.Sms.DATE)
                val body = it.getString(bodyIndex)
                val date = it.getLong(dateIndex)
                val message = "$body - ${
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(
                        Date(date)
                    )}"
                smsList.add(message)
            }
        }
    }

    return smsList
}
@SuppressLint("Recycle")
@RequiresApi(Build.VERSION_CODES.KITKAT)
fun getSMSList(phoneNumber: String, context: Context): List<String>  {
    val smsList = mutableListOf<String>()

    val calendar = Calendar.getInstance()
    calendar.timeInMillis = System.currentTimeMillis()
    calendar.add(Calendar.HOUR_OF_DAY, -24)

    if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_SMS
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        val selection = "${Telephony.Sms.ADDRESS} LIKE ? AND ${Telephony.Sms.DATE} > ?"
        val fuzzyPhoneNumber = "$phoneNumber%"
        val cursor = context.contentResolver.query(
            Telephony.Sms.CONTENT_URI,
            arrayOf(Telephony.Sms.BODY, Telephony.Sms.DATE), // إضافة حقل تاريخ الرسالة
            selection,
            arrayOf(fuzzyPhoneNumber, calendar.timeInMillis.toString()), // استخدام الوقت المنقح للحصول على الـ 24 ساعة الأخيرة
            null
        )

        cursor?.use {
            while (it.moveToNext()) {
                val bodyIndex = it.getColumnIndex(Telephony.Sms.BODY)
                val dateIndex = it.getColumnIndex(Telephony.Sms.DATE)
                val body = it.getString(bodyIndex)
                val date = it.getLong(dateIndex)
                val message = "$body - ${
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(
                        Date(date)
                    )}"
                smsList.add(message) // دمج النصوص وأوقات الرسائل في قائمة واحدة
            }
        }
    }

    return smsList
}

fun getSenderSet(context: Context): Set<String> {
    val senderSet = mutableSetOf<String>()

    if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_SMS
        ) == PackageManager.PERMISSION_GRANTED
    ) {

        val cursor = context.contentResolver.query(
            Telephony.Sms.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        cursor?.use {
            while (it.moveToNext()) {
                val sender = it.getString(it.getColumnIndex(Telephony.Sms.ADDRESS))
                senderSet.add(sender)
            }
        }
    }
    return senderSet
}

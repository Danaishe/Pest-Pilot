package com.hansoft.lepidopteran.helpers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.hansoft.lepidopteran.MainActivity

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        MainActivity().createNotification(context)
    }
}
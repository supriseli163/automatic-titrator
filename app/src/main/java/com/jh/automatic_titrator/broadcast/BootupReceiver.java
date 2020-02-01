package com.jh.automatic_titrator.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by apple on 2017/2/18.
 */

public class BootupReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent activity = context.getPackageManager().getLaunchIntentForPackage("com.jh.automatic_titrator");
        context.startActivity(activity);
    }
}

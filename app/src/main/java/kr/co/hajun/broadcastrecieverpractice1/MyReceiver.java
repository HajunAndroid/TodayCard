package kr.co.hajun.broadcastrecieverpractice1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsMessage;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        /*
        if (Intent.ACTION_DATE_CHANGED.equals(intent.getAction())) {
            SharedPreferences sharedPreferences = context.getSharedPreferences("my_prefs",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("defaultSum",0);
            editor.commit();
            Log.d("change","change");
        }*/
        Bundle bundle = intent.getExtras();
        SmsMessage[] messages = parseSmsMessage(bundle);
        if (messages.length > 0) {
            String sender = messages[0].getOriginatingAddress();
            if(sender.equals("15881688")) {
                String content = messages[0].getMessageBody().toString();
                Intent intentToService = new Intent(context, MyIntentService.class);
                intentToService.putExtra("Content", content);
                context.startService(intentToService);
            }
        }
    }

    private SmsMessage[] parseSmsMessage(Bundle bundle){
        Object[] objs = (Object[]) bundle.get("pdus");
        SmsMessage[] messages = new SmsMessage[objs.length];
        for(int i=0; i<objs.length; i++){
            messages[i] = SmsMessage.createFromPdu((byte[])objs[i]);
        }
        return messages;
    }

}
package kr.co.hajun.broadcastrecieverpractice1;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class MyIntentService extends IntentService {

    int price, month, date, hour, minute;
    String place, permit;

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String content = intent.getStringExtra("Content");
        String[] contentParse = content.split("\n");
        if(contentParse.length==6 && contentParse[5].contains("사용")){
            parseDate(contentParse[3]);
            parsePrice(contentParse[4]);
            parsePlace(contentParse[5]);

        }else if(contentParse.length==7 && contentParse[6].contains("승인취소")){
            parseDate(contentParse[3]);
            parsePrice(contentParse[4]);
            place = contentParse[5];
            permit = "승인취소";
        }
        /*
        SharedPreferences sharedPreferences = getSharedPreferences("my_prefs",Context.MODE_PRIVATE);
        int sumData = sharedPreferences.getInt("defaultSum",0);

        NotificationManager notificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder= null;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelID="channel_01";
            String channelName="MyChannel01";
            NotificationChannel channel= new NotificationChannel(channelID,channelName,NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
            builder=new NotificationCompat.Builder(this, channelID);
        }else{
            builder= new NotificationCompat.Builder(this);
        }
        builder.setSmallIcon(android.R.drawable.ic_menu_view);
        builder.setContentTitle("오늘의카드");//알림창 제목

        String[] price = contentParse[4].split("원");
        int sum = Integer.parseInt(price[0].replace(",","")) + sumData;
        builder.setContentText("방금 사용한 금액:"+sum);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("defaultSum",sum);
        editor.commit();

        Intent intentToActivity = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,10,intentToActivity,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        Notification notification=builder.build();
        notificationManager.notify(1, notification);
         */
        //Log.d("aaaaa",""+price+","+month+","+date+","+hour+","+minute+","+place+","+permit);
    }

    public void parsePrice(String s){
        String[] prices = s.split("원");
        price = Integer.parseInt(prices[0].replace(",",""));
    }

    public void parseDate(String s){
        month = Integer.parseInt(s.substring(0,2));
        date = Integer.parseInt(s.substring(3,5));
        hour = Integer.parseInt(s.substring(6,8));
        minute = Integer.parseInt(s.substring(9,11));
    }

    public void parsePlace(String s){
        String[] places = s.split(" 사용");
        place = places[0];
        permit = "승인";
    }

}
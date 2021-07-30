package kr.co.hajun.broadcastrecieverpractice1;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MyIntentService extends IntentService {
    String year, month, date, hour, minute, price, place, permit;

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();

        String content = intent.getStringExtra("Content");
        String[] contentParse = content.split("\n");

        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
        year = yearFormat.format(currentTime);

        if(contentParse.length==6 && contentParse[5].contains("사용")){
            parseDate(contentParse[3]);
            parsePrice(contentParse[4]);
            parsePlace(contentParse[5]);
            db.execSQL("insert into tb_card(year, month, date, hour, minute, place, price, permit) values(?,?,?,?,?,?,?,?)",
                    new String[]{year, month, date, hour, minute, place, price, permit});
        }else if(contentParse.length==7 && contentParse[6].contains("승인취소")){
            parseDate(contentParse[3]);
            parsePrice(contentParse[4]);
            place = contentParse[5];
            permit = "승인취소";

            Cursor c = db.rawQuery("select _id from tb_card where place = ?", new String[] { place });
            if (c.moveToLast()){
                String id = c.getString(0);
                db.execSQL("Delete from tb_card where _id = "+id);
            }
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
        //Log.d("aaaaa",""+year+","+price+","+month+","+date+","+hour+","+minute+","+place+","+permit);

        db.close();
    }

    public void parsePrice(String s){
        String[] prices = s.split("원");
        price = prices[0].replace(",","");
    }

    public void parseDate(String s){
        month = s.substring(0,2);
        date = s.substring(3,5);
        hour = s.substring(6,8);
        minute = s.substring(9,11);
    }

    public void parsePlace(String s){
        String[] places = s.split(" 사용");
        place = places[0];
        permit = "승인";
    }

}
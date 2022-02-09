package kr.co.hajun.broadcastrecieverpractice1;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.room.Room;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyIntentService extends IntentService {
    CardDAO cardDAO;
    DailySpendDAO dailySpendDAO;
    PayCardDAO payCardDAO;

    String year, month, date, hour, minute, price, place, permit;
    String card_name, card_id;

    String created_date;
    String created_time;

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "TodayCardDB").build();
        cardDAO = db.cardDAO();
        dailySpendDAO = db.dailySpendDAO();
        payCardDAO = db.payCardDAO();

        //DBHelper helper = new DBHelper(this);
        //SQLiteDatabase db = helper.getWritableDatabase();

        String content = intent.getStringExtra("Content");
        String[] contentParse = content.split("\n");

        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
        year = yearFormat.format(currentTime);

        if(contentParse.length==6 && contentParse[5].contains("사용")){
            parseCard(contentParse[1]);
            parseDate(contentParse[3]);
            parsePrice(contentParse[4]);
            parsePlace(contentParse[5]);

            Log.d("tagtag","1");
            List<Card> cardList = cardDAO.selectCard(card_name,card_id);
            if(cardList.size()==0){
                cardDAO.insertCard(new Card(card_id,card_name));
            }

            created_date = year+"-"+month+"-"+date;
            created_time = hour+"-"+minute;

            List<DailySpend> dailySpendList = dailySpendDAO.selectTotal(created_date);
            if(dailySpendList.size()==0){
                dailySpendDAO.insertTotal(new DailySpend(created_date,0));
            }
            dailySpendDAO.updateTotal(Integer.parseInt(price),created_date);
            payCardDAO.insertPayCard(new PayCard(created_date,created_time,
                    Integer.parseInt(price),place,permit,card_id));

            //db.execSQL("insert into tb_card(year, month, date, hour, minute, place, price, permit) values(?,?,?,?,?,?,?,?)",
            //        new String[]{year, month, date, hour, minute, place, price, permit});

            SharedPreferences sharedPreferencesNotification = getSharedPreferences("my_notification",MODE_PRIVATE);
            if(sharedPreferencesNotification.getString("noti","on").equals("on")) {
                checkingNotification(/*db*/);
            }
        }else if(contentParse.length==7 && contentParse[6].contains("승인취소")){
            parseDate(contentParse[3]);
            parsePrice(contentParse[4]);
            place = contentParse[5];
            permit = "승인취소";

            created_date = year+"-"+month+"-"+date;

            dailySpendDAO.updateTotal(Integer.parseInt(price)*(-1),created_date);
            payCardDAO.deletePayCard(created_date,Integer.parseInt(price),place);

            /*
            Cursor c = db.rawQuery("select _id from tb_card where place = ? and price = ?",
                    new String[] { place, price });
            if (c.moveToLast()){
                String id = c.getString(0);
                db.execSQL("Delete from tb_card where _id = "+id);
            }*/
        }
        //db.close();
    }

    public void parseCard(String s){
        card_name = "KB국민체크";
        for(int i=7;i<=10;i++){
            card_id+=s.charAt(i);
        }
    }

    public void parsePrice(String s){
        String[] prices = s.split("원");
        price = prices[0].replace(",","");
    }

    public void parseDate(String s){
        if(s.substring(0,1).equals("0")){
            month = s.substring(1,2);
        }else{
            month = s.substring(0,2);
        }
        Log.d("price","in"+month);
        if(s.substring(3,4).equals("0")){
            date = s.substring(4,5);
        }else{
            date = s.substring(3,5);
        }
        Log.d("price","in"+date);
        hour = s.substring(6,8);
        minute = s.substring(9,11);
    }

    public void parsePlace(String s){
        String[] places = s.split(" 사용");
        place = places[0];
        permit = "승인";
    }

    public void checkingNotification(/*SQLiteDatabase db*/){
        /*int total = 0;
        Cursor c = db.rawQuery("select price from tb_card where year = ? and month = ? and date = ?",
                new String[] {year, month, date});
        while(c.moveToNext()){
            String price = c.getString(0);
            total += Integer.parseInt(price);
        }*/
        List<DailySpend> list = dailySpendDAO.selectTotal(created_date);
        int total = list.get(0).getTotal();
        SharedPreferences sharedPreferences = getSharedPreferences("spendLimit", MODE_PRIVATE);
        int limit = sharedPreferences.getInt("limit", 30000);
        if (total > limit) {
            Calendar calendar = Calendar.getInstance();
            int nYear = calendar.get(Calendar.YEAR);
            int nMonth = calendar.get(Calendar.MONTH) + 1;
            int nDay = calendar.get(Calendar.DAY_OF_MONTH);
            if ((year.equals(nYear + "")) && (month.equals(nMonth + "")) && (date.equals(nDay + ""))) {
                callNotification(limit,total);
            }
        }
    }

    public void callNotification(int limit, int total){
        NotificationManager notificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder= null;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelID="channelIdCard";
            String channelName="channelNameCard";
            NotificationChannel channel= new NotificationChannel(channelID,channelName,NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
            builder=new NotificationCompat.Builder(this, channelID);
        }else{
            builder= new NotificationCompat.Builder(this);
        }
        DecimalFormat df = new DecimalFormat("###,###");

        builder.setSmallIcon(R.drawable.baseline_payment_black_24dp);
        builder.setContentText("당일 사용 한도 "+df.format(limit)+"원 초과 / 누적 사용액 "+df.format(total)+"원");

        Intent intentToActivity = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,10,intentToActivity,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);

        Notification notification = builder.build();
        notificationManager.notify(1,notification);
    }
}
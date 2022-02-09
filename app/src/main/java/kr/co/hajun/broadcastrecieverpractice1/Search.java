package kr.co.hajun.broadcastrecieverpractice1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

public class Search extends AppCompatActivity {

    Calendar calendar = Calendar.getInstance();
    int startYear = calendar.get(Calendar.YEAR);
    int startMonth = calendar.get(Calendar.MONTH)+1;
    int startDay = calendar.get(Calendar.DAY_OF_MONTH);

    TextView textStart,totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        textStart = findViewById(R.id.spendWhenStart);
        totalPrice = findViewById(R.id.totalPrice);
    }

    public void startDay(View view){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                startYear = i;
                startMonth = i1+1;
                startDay = i2;
                textStart.setText(startYear+"."+startMonth+"."+startDay);
            }
        },startYear,startMonth-1,startDay);
        datePickerDialog.show();
    }

    public void btnBack(View view){
        finish();
    }

    public void btnSearch(View view){
        int total = 0;
        if(textStart.getText().equals("")){
            Toast.makeText(this,"날짜를 입력하세요",Toast.LENGTH_SHORT).show();
            return;
        }
        DecimalFormat df = new DecimalFormat("###,###");

        AppDatabase db = Room.databaseBuilder(this,
                AppDatabase.class, "TodayCardDB").allowMainThreadQueries().build();
        DailySpendDAO dailySpendDAO = db.dailySpendDAO();
        String s = startYear+"-"+startMonth+"-"+startDay;
        List<DailySpend> list = dailySpendDAO.selectTotal(s);
        if(list.size()!=0)
            total += list.get(0).getTotal();
        /*DBHelper helper= new DBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor c = db.rawQuery("select price from tb_card where year = ? and month = ? and date = ?",
                new String[]{startYear+"",startMonth+"",startDay+""});
        while(c.moveToNext()){
            total += Integer.parseInt(c.getString(0));
        }*/

        totalPrice.setText(df.format(total)+"원");
        db.close();
    }
}
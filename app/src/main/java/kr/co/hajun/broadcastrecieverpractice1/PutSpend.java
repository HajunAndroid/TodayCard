package kr.co.hajun.broadcastrecieverpractice1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

public class PutSpend extends AppCompatActivity {

    Calendar calendar = Calendar.getInstance();
    int nYear = calendar.get(Calendar.YEAR);
    int nMonth = calendar.get(Calendar.MONTH)+1;
    int nDay = calendar.get(Calendar.DAY_OF_MONTH);

    EditText etWhere, etPrice;
    TextView tDay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_put_spend);
        etWhere =findViewById(R.id.spendWhere);
        etPrice = findViewById(R.id.spendHowMuch);
        tDay = findViewById(R.id.spendWhen);
        etWhere.requestFocus();
    }

    public void selectDay(View view){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                nYear = i;
                nMonth = i1+1;
                nDay = i2;
                tDay.setText(nYear+"."+nMonth+"."+nDay);
            }
        },nYear,nMonth-1,nDay);
        datePickerDialog.show();
    }

    public void btnDone(View view){
        try{
            String where = etWhere.getText().toString();
            String str =etPrice.getText().toString().trim();
            String when = tDay.getText().toString();
            if(where.equals("") || when.equals("")) {
                Toast.makeText(this, "입력을 완료해주세요", Toast.LENGTH_SHORT).show();
                return;
            }
            //int rt = Integer.parseInt(str);

            AppDatabase db = Room.databaseBuilder(this,
                    AppDatabase.class, "TodayCardDB").allowMainThreadQueries().build();
            DailySpendDAO dailySpendDAO = db.dailySpendDAO();
            PayCashDAO payCashDAO = db.payCashDAO();

            String s = nYear+"-"+nMonth+"-"+nDay;
            List<DailySpend> dailySpendList = dailySpendDAO.selectTotal(s);
            if(dailySpendList.size()==0){
                dailySpendDAO.insertDailySpend(new DailySpend(s,0));
            }
            List<DailySpend> list = dailySpendDAO.selectTotal(s);
            int currentTotal = list.get(0).getTotal();
            currentTotal+=Integer.parseInt(str);
            dailySpendDAO.updateTotal(currentTotal,s);

            payCashDAO.insertPayCash(new PayCash(s,Integer.parseInt(str),where,"승인"));

            /*
            DBHelper helper = new DBHelper(this);
            SQLiteDatabase db = helper.getWritableDatabase();
            db.execSQL("insert into tb_card(year, month, date, hour, minute, place, price, permit) " +
                            "values(?,?,?,?,?,?,?,?)",
                    new String[]{nYear+"", nMonth+"", nDay+"", "-", "-", where, rt+"", "승인"});
            db.close();
            */
            finish();
        }catch (NumberFormatException e){
            Toast.makeText(this,"사용금액에 숫자만 입력하세요",Toast.LENGTH_SHORT).show();
        }
    }

    public void btnClose(View view){
        finish();
    }
}
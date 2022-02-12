package kr.co.hajun.broadcastrecieverpractice1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amplitude.api.Amplitude;

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

        Amplitude.getInstance().initialize(this, "45b00de2cf11098cf68104782fcb322a").enableForegroundTracking(getApplication());
        Amplitude.getInstance().logEvent("enter__input_self");

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

            Amplitude.getInstance().initialize(this, "45b00de2cf11098cf68104782fcb322a").enableForegroundTracking(getApplication());
            Amplitude.getInstance().logEvent("click__input_self");

            AppDatabase db = Room.databaseBuilder(this,
                    AppDatabase.class, "TodayCardDB").allowMainThreadQueries().build();

            DailySpendDAO dailySpendDAO = db.dailySpendDAO();
            PayCashDAO payCashDAO = db.payCashDAO();

            String created_date = nYear+"-"+nMonth+"-"+nDay;
            String price = str; String place = where;
            /*
            List<DailySpend> dailySpendList = dailySpendDAO.selectTotal(created_date);
            if(dailySpendList.size()==0){
                dailySpendDAO.insertTotal(new DailySpend(created_date,0));
            }
            dailySpendDAO.updateTotal(Integer.parseInt(str),created_date);
            payCashDAO.insertPayCash(new PayCash(created_date,Integer.parseInt(str),where,"승인"));
            */

            payCashDAO.insert(created_date,Integer.parseInt(price),place);

            /*
            DBHelper helper = new DBHelper(this);
            SQLiteDatabase db = helper.getWritableDatabase();
            db.execSQL("insert into tb_card(year, month, date, hour, minute, place, price, permit) " +
                            "values(?,?,?,?,?,?,?,?)",
                    new String[]{nYear+"", nMonth+"", nDay+"", "-", "-", where, rt+"", "승인"});
            db.close();
            */

            Amplitude.getInstance().initialize(this, "45b00de2cf11098cf68104782fcb322a").enableForegroundTracking(getApplication());
            Amplitude.getInstance().logEvent("complete__input_self");

            finish();
        }catch (NumberFormatException e){
            Toast.makeText(this,"사용금액에 숫자만 입력하세요",Toast.LENGTH_SHORT).show();
        }
    }

    public void btnClose(View view){
        finish();
    }
}
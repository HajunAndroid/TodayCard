package kr.co.hajun.broadcastrecieverpractice1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    boolean receiveSMSPermission;
    Calendar calendar = Calendar.getInstance();
    int nYear = calendar.get(Calendar.YEAR);
    int nMonth = calendar.get(Calendar.MONTH)+1;
    int nDay = calendar.get(Calendar.DAY_OF_MONTH);
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar tb = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(tb);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setTitle(""+nYear+"."+nMonth+"."+nDay);
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new pagerAdapter(getSupportFragmentManager()));
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)== PackageManager.PERMISSION_GRANTED){
            receiveSMSPermission = true;
        }
        if(!receiveSMSPermission){
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.RECEIVE_SMS},200);
        }

        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db= helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select year, month, date, hour, minute, place, price, permit from tb_card ",null);
        /*
        while(cursor.moveToNext()){

            textView.append(cursor.getString(0));
            textView.append(cursor.getString(1));
            textView.append(cursor.getString(2));
            textView.append(cursor.getString(3));
            textView.append(cursor.getString(4));
            textView.append(cursor.getString(5));
            textView.append(cursor.getString(6));
            textView.append(cursor.getString(7));

        }
        */
        db.close();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==200&&grantResults.length>0){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
                receiveSMSPermission = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar_action,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_calender:
                setCalender();
                return true;
            case R.id.action_refresh:
                reFresh();
                return true;
            case R.id.action_search:
                //((TextView)findViewById(R.id.textView)).setText("SEARCH");
                return true;
            case R.id.action_settings:
                //((TextView)findViewById(R.id.textView)).setText("SETTINGS");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setCalender(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                nYear = i;
                nMonth = i1+1;
                nDay = i2;
                getSupportActionBar().setTitle(""+nYear+"."+nMonth+"."+nDay);
            }
        },nYear,nMonth-1,nDay);
        datePickerDialog.show();
    }

    public void reFresh(){
        try {
            Intent intent = getIntent();
            finish();
            overridePendingTransition(0, 0);
            startActivity(intent);
            overridePendingTransition(0, 0);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    class pagerAdapter extends FragmentPagerAdapter{
        List<Fragment> fragments = new ArrayList<Fragment>();
        private String titles[] = new String[]{"오늘","일별","주별","월별"};
        public pagerAdapter(FragmentManager fm){
            super(fm);
            fragments.add(new TodayFragment());
            fragments.add(new DayFragment());
            fragments.add(new WeekFragment());
            fragments.add(new MonthFragment());
        }
        @Override
        public Fragment getItem(int position) {
            return this.fragments.get(position);
        }

        @Override
        public int getCount() {
            return this.fragments.size();
        }
        @Override
        public CharSequence getPageTitle(int position){
            return titles[position];
        }
    }
}
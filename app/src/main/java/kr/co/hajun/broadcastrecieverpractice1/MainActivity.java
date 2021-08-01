package kr.co.hajun.broadcastrecieverpractice1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.Toast;

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
        /*
        SharedPreferences sharedPreferences = getSharedPreferences("dayChange", Context.MODE_PRIVATE);
        int beforeDay = sharedPreferences.getInt("day",calendar.get(Calendar.DAY_OF_MONTH));
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(beforeDay!=calendar.get(Calendar.DAY_OF_MONTH)){
            editor.putInt("day",calendar.get(Calendar.DAY_OF_MONTH));
            editor.putInt("total",0);
            editor.commit();
        }else{
            editor.putInt("day",calendar.get(Calendar.DAY_OF_MONTH));
            editor.commit();
        }
        */
        /*
        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db= helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select year, month, date, hour, minute, place, price, permit from tb_card ",null);

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

        db.close();
        */
        //Log.d("lifecylce","mainOnCreate");
    }
    /*
    @Override
    protected void onStart() {
        super.onStart();
        Log.d("lifecylce","mainOnStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("lifecylce","mainOnResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("lifecylce","mainOnPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("lifecylce","mainOnStop()");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("lifecylce","mainOnSaveInstanceState");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("lifecylce","mainOnDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("lifecylce","mainOnRestart");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("lifecylce","mainOnRestoreInstanceState");
    }
    */
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
            /*
            case R.id.action_calender:
                setCalender();
                return true;
             */
            case R.id.action_refresh:
                reFresh();
                return true;
            case R.id.action_search:
                //((TextView)findViewById(R.id.textView)).setText("SEARCH");
                return true;
            case R.id.changeLimit:
                Intent intent = new Intent(this,ChangeLimit.class);
                startActivity(intent);
                return true;
            case R.id.writeSelf:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    /*
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
    */
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
        private String titles[] = new String[]{"오늘","일별"/*,"주별","월별"*/};
        public pagerAdapter(FragmentManager fm){
            super(fm);
            //Log.d("lifecylce","setAdapter");
            TodayFragment todayFragment = new TodayFragment();
            Bundle bundle = new Bundle(3);
            bundle.putInt("year",nYear);
            bundle.putInt("month",nMonth);
            bundle.putInt("day",nDay);
            todayFragment.setArguments(bundle);
            fragments.add(todayFragment);
            fragments.add(new DayFragment());
            //fragments.add(new WeekFragment());
            //fragments.add(new MonthFragment());
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
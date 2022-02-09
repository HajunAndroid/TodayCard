package kr.co.hajun.broadcastrecieverpractice1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
        SharedPreferences sharedPreferences = getSharedPreferences("my_notification",MODE_PRIVATE);
        if(sharedPreferences.getString("noti","on").equals("on")){
            menu.getItem(0).setIcon(R.drawable.baseline_notifications_active_black_24dp);
        }else{
            menu.getItem(0).setIcon(R.drawable.baseline_notifications_off_black_24dp);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_notification:
                setNotificationIcon(item);
                return true;
            case R.id.action_refresh:
                reFresh();
                return true;
            case R.id.action_search:
                Intent intentSearch = new Intent(this, Search.class);
                startActivity(intentSearch);
                return true;
            case R.id.changeLimit:
                Intent intentChange = new Intent(this,ChangeLimit.class);
                startActivity(intentChange);
                return true;
            case R.id.writeSelf:
                Intent intentPut = new Intent(this, PutSpend.class);
                startActivity(intentPut);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
    public void setNotificationIcon(MenuItem item){
        SharedPreferences sharedPreferences = getSharedPreferences("my_notification", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(sharedPreferences.getString("noti","on").equals("on")){
            editor.putString("noti","off");
            item.setIcon(R.drawable.baseline_notifications_off_black_24dp);
            Toast.makeText(this,"알림 off",Toast.LENGTH_SHORT).show();
        }else{
            editor.putString("noti","on");
            item.setIcon(R.drawable.baseline_notifications_active_black_24dp);
            Toast.makeText(this,"알림 on",Toast.LENGTH_SHORT).show();
        }
        editor.commit();
    }

    class pagerAdapter extends FragmentPagerAdapter{
        List<Fragment> fragments = new ArrayList<Fragment>();
        private String titles[] = new String[]{"오늘","상세내역"};
        public pagerAdapter(FragmentManager fm){
            super(fm);
            TodayFragment todayFragment = new TodayFragment();
            DayFragment dayFragment = new DayFragment();
            Bundle bundle = new Bundle(3);
            bundle.putInt("year",nYear);
            bundle.putInt("month",nMonth);
            bundle.putInt("day",nDay);
            todayFragment.setArguments(bundle);
            dayFragment.setArguments(bundle);
            fragments.add(todayFragment);
            fragments.add(dayFragment);
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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        reFresh();
    }
}
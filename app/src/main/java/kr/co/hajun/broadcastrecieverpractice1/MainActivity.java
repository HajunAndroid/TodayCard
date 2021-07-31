package kr.co.hajun.broadcastrecieverpractice1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    boolean receiveSMSPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar tb = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

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
    /*
    public void btnMethod(View view){
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
    */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar_action,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_calender:
                ((TextView)findViewById(R.id.textView)).setText("CALENDAR");
                return true;
            case R.id.action_refresh:
                ((TextView)findViewById(R.id.textView)).setText("REFRESH");
                return true;
            case R.id.action_search:
                ((TextView)findViewById(R.id.textView)).setText("SEARCH");
                return true;
            case R.id.action_settings:
                ((TextView)findViewById(R.id.textView)).setText("SETTINGS");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
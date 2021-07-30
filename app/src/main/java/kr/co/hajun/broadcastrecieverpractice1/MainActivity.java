package kr.co.hajun.broadcastrecieverpractice1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    boolean receiveSMSPermission;

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)== PackageManager.PERMISSION_GRANTED){
            receiveSMSPermission = true;
        }
        if(!receiveSMSPermission){
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.RECEIVE_SMS},200);
        }

        textView = findViewById(R.id.textView1);
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
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==200&&grantResults.length>0){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
                receiveSMSPermission = true;
        }
    }

}
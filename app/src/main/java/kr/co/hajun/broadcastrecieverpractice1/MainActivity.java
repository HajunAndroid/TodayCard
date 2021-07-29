package kr.co.hajun.broadcastrecieverpractice1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    boolean receiveSMSPermission;
    TextView tv_sender;
    TextView tv_date;
    TextView tv_content;

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

        tv_sender = findViewById(R.id.textView);
        tv_date = findViewById(R.id.textView2);
        tv_content = findViewById(R.id.textView3);

        Intent intent = getIntent();
        processCommand(intent);
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
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        processCommand(intent);
    }

    private void processCommand(Intent intent){
        if(intent != null){
            String sender = intent.getStringExtra("sender");
            String date = intent.getStringExtra("date");
            String content = intent.getStringExtra("content");

            tv_sender.setText(sender);
            tv_date.setText(date);
            tv_content.setText(content);
        }
    }
}
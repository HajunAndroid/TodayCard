package kr.co.hajun.broadcastrecieverpractice1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public DBHelper(Context context){
        super(context,"carddb",null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String cardSQL="create table tb_card "+
                "(_id integer primary key autoincrement,"+
                "year,"+
                "month,"+
                "date,"+
                "hour,"+
                "minute,"+
                "place,"+
                "price,"+
                "permit)";
        sqLiteDatabase.execSQL(cardSQL);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {}
}

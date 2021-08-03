package kr.co.hajun.broadcastrecieverpractice1;


import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.DecimalFormat;


public class TodayFragment extends Fragment {
    DonutView donutView;
    TextView spend, deadLine;
    Context mContext;
    int total;
    DecimalFormat df;
    int year, month, day;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return (LinearLayout)inflater.inflate(R.layout.fragment_today, container, false);
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spend = view.findViewById(R.id.spendMoney);
        deadLine = view.findViewById(R.id.deadLineMoney);
        donutView = view.findViewById(R.id.donut);
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle bundle = getArguments();
        year = bundle.getInt("year");
        month = bundle.getInt("month");
        day = bundle.getInt("day");

        total = 0;
        DBHelper helper = new DBHelper(mContext);
        SQLiteDatabase db= helper.getWritableDatabase();
        Cursor c = db.rawQuery("select price from tb_card where year = ? and month = ? and date = ?", new String[] { year+"",month+"",day+"" });
        while(c.moveToNext()){
            total += Integer.parseInt(c.getString(0));
        }
        df = new DecimalFormat("###,###");
        spend.setText(df.format(total));

        db.close();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("spendLimit",Context.MODE_PRIVATE);
        int now = sharedPreferences.getInt("limit",30000);
        deadLine.setText(df.format(now));
        donutView.setValue(total,now);
    }
}

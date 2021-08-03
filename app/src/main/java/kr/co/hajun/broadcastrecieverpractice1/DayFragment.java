package kr.co.hajun.broadcastrecieverpractice1;


import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DayFragment extends Fragment {

    DecimalFormat df;
    int year, month, day;
    Context mContext;
    RecyclerView recyclerView;
    String place, price, hour, minute;
    DBHelper helper;
    SQLiteDatabase db;
    List<HashMap<String,String>> list;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext =context;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return (RecyclerView)inflater.inflate(R.layout.fragment_day, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle bundle = getArguments();
        year = bundle.getInt("year");
        month = bundle.getInt("month");
        day = bundle.getInt("day");
        df= new DecimalFormat("###,###");

        list = new ArrayList<>();

        helper = new DBHelper(mContext);
        db= helper.getWritableDatabase();
        Cursor c = db.rawQuery("select place, price, hour, minute, permit from tb_card where year = ? and month = ? and date = ?", new String[] { year+"",month+"",day+"" });

        while(c.moveToNext()){
            HashMap<String,String> map = new HashMap<>();
            map.put("place",c.getString(0));
            map.put("price",c.getString(1));
            map.put("hour",c.getString(2));
            map.put("minute",c.getString(3));
            map.put("permit",c.getString(4));
            list.add(map);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(new MyAdapter(list));
    }

    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{
        private List<HashMap<String,String>> list;
        public MyAdapter(List<HashMap<String,String>> list){
            this.list = list;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleradapter,parent,false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(DayFragment.MyViewHolder holder, int position) {
            HashMap<String,String> hashMap = list.get(position);
            place = hashMap.get("place");
            holder.txtPlace.setText(place);
            price = hashMap.get("price");
            holder.txtPrice.setText(df.format(Integer.parseInt(price))+"원");
            hour = hashMap.get("hour");
            minute = hashMap.get("minute");
            String permit = hashMap.get("permit");
            if(!hour.equals("-")) {
                holder.txtTimeAndPermit.setText(hour + "시" + minute + "분" + " " + permit);
            }else{
                holder.txtTimeAndPermit.setText("직접 입력 "+permit);
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }
    private class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView txtPlace, txtPrice, txtTimeAndPermit;
        public MyViewHolder(View itemView){
            super(itemView);
            txtPlace = itemView.findViewById(R.id.place);
            txtPrice = itemView.findViewById(R.id.price);
            txtTimeAndPermit = itemView.findViewById(R.id.timeAndPermit);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if( pos != RecyclerView.NO_POSITION){
                        HashMap<String,String> hashMap = list.get(pos);
                        place = hashMap.get("place");
                        price = hashMap.get("price");
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setIcon(android.R.drawable.ic_dialog_alert);
                    builder.setTitle("삭제");
                    builder.setMessage("해당 결제 내역을 삭제하시겠습니까?");
                    builder.setPositiveButton("네",dialogListener);
                    builder.setNegativeButton("아니오",null);
                    builder.setCancelable(false);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });
        }
    }
    DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            Cursor c = db.rawQuery("select _id from tb_card where year =? and month = ? and date =? and place = ? and price =? ",
                    new String[]{year+"",month+"",day+"",place,price});
            if (c.moveToLast()){
                String id = c.getString(0);
                db.execSQL("Delete from tb_card where _id = "+id);
            }
            reFresh();
            Toast.makeText(mContext,"삭제가 완료되었습니다.",Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onStop() {
        super.onStop();
        db.close();

    }

    public void reFresh(){
        ((MainActivity)getActivity()).reFresh();
    }

}

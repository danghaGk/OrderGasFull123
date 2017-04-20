package android.hazardphan.ordergas;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by CuongPhan on 3/29/2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder>{
    private ArrayList<Item_GasHome> arrayList =new ArrayList<>();
    List<Item_GasHome> ds =  null;
    private ClickListener clickListener;
    Context context;
    private int Status;
    private boolean check = true;

    public RecyclerViewAdapter(ArrayList<Item_GasHome> arrayList, ClickListener clickListener, Context context) {
        this.arrayList = arrayList;
        this.clickListener = clickListener;
        this.context = context;
    }

    public RecyclerViewAdapter(List<Item_GasHome> ds, Context context,ClickListener clickListener) {
        this.ds = ds;
        this.arrayList = new ArrayList<>();
        this.arrayList.addAll(ds);
        this.context = context;
        this.clickListener = clickListener;

    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_homefrm,null);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        Item_GasHome gas = ds.get(position);
        holder.txtTenCuaHang.setText(gas.getTencuahang());
        holder.txtDiaChi.setText(gas.getDiadiem());
        holder.txtGiaTien.setText(gas.getMotagia());
        holder.txtSDT.setText(gas.getSodienthoai());
        Glide.with(context)
                .load(gas.getAnh())
                .into(holder.imgCuaHang);

    }


    public Object getItem(final int position) {
        return ds.get(position);
    }
    @Override
    public int getItemCount() {
        return ds.size();
    }

    //class Viewholer
    class RecyclerViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        TextView txtTenCuaHang ,txtDiaChi,txtGiaTien,txtSDT;
        ImageView imgCuaHang;
        LinearLayout lnCall,lnComment,lnLike;
        ImageView imgStar ;
        public RecyclerViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            txtTenCuaHang= (TextView) v.findViewById(R.id.txtTenCuaHangHome);
            txtDiaChi= (TextView) v.findViewById(R.id.txtDiaChiHome);
            txtGiaTien= (TextView) v.findViewById(R.id.txtGiaTienHome);
            txtSDT= (TextView) v.findViewById(R.id.txtSDTHome);
            imgCuaHang= (ImageView) v.findViewById(R.id.imgHome);
            lnCall = (LinearLayout) v.findViewById(R.id.lnCall);
            lnComment = (LinearLayout) v.findViewById(R.id.lnComment);
            lnLike = (LinearLayout) v.findViewById(R.id.lnLike);
            imgStar = (ImageView) v.findViewById(R.id.imgStar);

            lnCall.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:"+txtSDT.getText().toString()));

                            if (ActivityCompat.checkSelfPermission(v.getContext(),
                                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            v.getContext().startActivity(callIntent);


                }
            });
            imgStar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(check){
                        imgStar.setImageResource(R.drawable.star_gold);
                        check = false;
                        Log.e("check", check +"" );
                    }else {
                        imgStar.setImageResource(R.drawable.star_white);
                        check =true ;
                    }

                }
            });
                //run ha a
             // a oi chay dc roi e nhan vao thi no doi mau vang nhuwng bam tiep
        }


        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);


        }
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        ds.clear();
        if (charText.length() == 0) {
            ds.addAll(arrayList);
        } else {
            for (Item_GasHome wp : arrayList) {
                if (wp.getDiadiem().toLowerCase(Locale.getDefault()).contains(charText)) {
                    ds.add(wp);
                }

            }
        }
        notifyDataSetChanged();
    }
    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }
    public interface ClickListener {
        void onItemClick(int position, View v);

        void onItemLongClick(int position, View v);
    }

}

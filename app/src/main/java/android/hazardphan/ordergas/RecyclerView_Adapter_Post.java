package android.hazardphan.ordergas;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by dangha.dev on 04/19/2017.
 */

public class RecyclerView_Adapter_Post  extends RecyclerView.Adapter<RecyclerView_Adapter_Post.MyViewHolder> {
    ArrayList<Item_GasHome> listGasPost;
    Context context;

    public RecyclerView_Adapter_Post(ArrayList<Item_GasHome> listGasPost, Context context) {
        this.listGasPost = listGasPost;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_postedfrm, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Item_GasHome gas = listGasPost.get(position);
        holder.txtTenCuaHangPost.setText(gas.getTencuahang());
        holder.txtGiaTienPost.setText(gas.getMotagia());
        holder.txtDiaChiPost.setText(gas.getDiadiem());
        holder.txtSDTPost.setText(gas.getSodienthoai());
        String link = gas.getAnh();
        Glide.with(context)
                .load(link)
                .into(holder.imgAvatar);


    }

    @Override
    public int getItemCount() {
        return listGasPost.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAvatar;
        TextView txtDiaChiPost,txtGiaTienPost,txtSDTPost,txtTenCuaHangPost;
        public MyViewHolder(View itemView) {
            super(itemView);
            txtDiaChiPost = (TextView) itemView.findViewById(R.id.txtDiaChiPost);
            txtGiaTienPost = (TextView) itemView.findViewById(R.id.txtGiaTienPost);
            txtSDTPost = (TextView) itemView.findViewById(R.id.txtGiaTienPost);
            txtTenCuaHangPost = (TextView) itemView.findViewById(R.id.txtTenCuaHangPost);
            imgAvatar = (ImageView) itemView.findViewById(R.id.imgAvatar);
        }
    }
}

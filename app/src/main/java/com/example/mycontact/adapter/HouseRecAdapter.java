package com.example.mycontact.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mycontact.BuyerActivity;
import com.example.mycontact.HouseActivity;
import com.example.mycontact.R;
import com.example.mycontact.model.House;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HouseRecAdapter extends RecyclerView.Adapter<HouseRecAdapter.ViewHolder> {

    private Context context;
    private List<House> listHouse;

    public HouseRecAdapter(Context context, List<House> listHouse) {
        this.context = context;
        this.listHouse = listHouse;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.row_house, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindView(listHouse.get(position));
    }

    @Override
    public int getItemCount() {
        return listHouse.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgHouse;
        TextView tvName, tvAddress;
        LinearLayout parent;

        public ViewHolder(View v) {
            super(v);

            imgHouse = v.findViewById(R.id.img_house);
            tvName = v.findViewById(R.id.tv_row_house_name);
            tvAddress = v.findViewById(R.id.tv_row_house_address);
            parent = v.findViewById(R.id.parent_view_row_house);
        }

        void bindView(House house) {
            Picasso.get().load(house.getPhoto_url()).into(imgHouse);
            tvName.setText(house.getNama());
            tvAddress.setText(house.getAlamat());

            parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, BuyerActivity.class);
                    intent.putExtra(HouseActivity.ID_KEY, house.getId());
                    intent.putExtra(HouseActivity.NAME_KEY, house.getNama());
                    context.startActivity(intent);
                }
            });
        }
    }
}

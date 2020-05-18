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

import com.example.mycontact.HouseActivity;
import com.example.mycontact.R;
import com.example.mycontact.model.Pt;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PtRecAdapter extends RecyclerView.Adapter<PtRecAdapter.ViewHolder> {

    private Context context;
    private List<Pt> listPt;

    public PtRecAdapter(Context context, List<Pt> listPt) {
        this.context = context;
        this.listPt = listPt;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.row_pt, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindView(listPt.get(position));
    }

    @Override
    public int getItemCount() {
        return listPt.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgPt;
        TextView tvName, tvAddress;
        LinearLayout parent;

        public ViewHolder(View v) {
            super(v);

            imgPt = v.findViewById(R.id.img_pt);
            tvName = v.findViewById(R.id.tv_row_pt_name);
            tvAddress = v.findViewById(R.id.tv_row_pt_address);
            parent = v.findViewById(R.id.parent_view_row_pt);
        }

        void bindView(Pt pt) {
            Picasso.get().load(pt.getPhoto_url()).into(imgPt);
            tvName.setText(pt.getNama());
            tvAddress.setText(pt.getAlamat());

            parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, HouseActivity.class);
                    intent.putExtra(HouseActivity.ID_KEY, pt.getId());
                    intent.putExtra(HouseActivity.NAME_KEY, pt.getNama());
                    context.startActivity(intent);
                }
            });
        }
    }
}

package com.example.mycontact.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mycontact.DetailActivity;
import com.example.mycontact.R;
import com.example.mycontact.model.Buyer;
import com.example.mycontact.network.ApiClient;
import com.example.mycontact.network.ApiEndPoint;
import java.util.List;

public class BuyerAdapter extends RecyclerView.Adapter<BuyerAdapter.ViewHolder> {

    private ApiEndPoint apiEndPoint;
    private Activity context;
    private List<Buyer> listBuyer;

    public BuyerAdapter(Activity context, List<Buyer> listBuyer) {
        this.context = context;
        this.listBuyer = listBuyer;

        apiEndPoint = ApiClient.getRetrofit().create(ApiEndPoint.class);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.row_buyer, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {//parameter postion aka return dari 0 ... sampe jumlah data. start dari 0 bang
        holder.bindData(listBuyer.get(position), (position + 1)); //kan kalau di dlm pemrograman selalau di mulai dari 0 bang
    }

    @Override
    public int getItemCount() {
        return listBuyer.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout rootView;
        TextView tvName;
        TextView tvUnit;
        TextView tvstatus_berkas;
        TextView tvNumber;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            rootView = itemView.findViewById(R.id.parent_view_row_contact);
            tvName = itemView.findViewById(R.id.tv_contact_name);
            tvUnit = itemView.findViewById(R.id.tv_contact_unit);
            tvstatus_berkas = itemView.findViewById(R.id.tv_contact_status_berkas);
            tvNumber = itemView.findViewById(R.id.tv_number_row_buyer);
        }

        void bindData(Buyer buyer, int number) {
            tvName.setText(buyer.getNama());
            tvUnit.setText(buyer.getUnit());
            tvstatus_berkas.setText(buyer.getstatus_berkas());
            tvNumber.setText(number + "");

            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("id", buyer.getId());
                    context.startActivity(intent);
                }
            });
        }

    }
}

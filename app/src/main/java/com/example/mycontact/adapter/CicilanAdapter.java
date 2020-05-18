package com.example.mycontact.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mycontact.R;
import com.example.mycontact.model.Cicilan;
import com.example.mycontact.model.CicilanData;

public class CicilanAdapter extends RecyclerView.Adapter<CicilanAdapter.ViewHolder> {

    public interface RecyclerOnItemClickHandler {
        void onItemClicked(int position);
    }

    private Cicilan listCicilan;
    private Context context;
    private RecyclerOnItemClickHandler recyclerOnItemClickHandler;

    public CicilanAdapter(Cicilan listCicilan, Context context, RecyclerOnItemClickHandler recyclerOnItemClickHandler) {
        this.listCicilan = listCicilan;
        this.context = context;
        this.recyclerOnItemClickHandler = recyclerOnItemClickHandler;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.row_cicilan, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(listCicilan.getCicilanData().get(position), position);
    }

    @Override
    public int getItemCount() {
        return listCicilan.getCicilanData().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvCicilan, tvTransDate;
        ImageView imgDeletePay;

        public ViewHolder(@NonNull View v) {
            super(v);

            tvTitle = v.findViewById(R.id.tv_cicilan_title);
            tvCicilan = v.findViewById(R.id.tv_jumlah_cicilan);
            tvTransDate = v.findViewById(R.id.tv_trans_date_cicilan);
            imgDeletePay = v.findViewById(R.id.img_delete_cicilan);

        }

        void bind(CicilanData cicilan, int position) {

            tvTitle.setText(cicilan.getType());
            tvCicilan.setText(cicilan.getCicilan());
            tvTransDate.setText(cicilan.getDate());
            imgDeletePay.setOnClickListener(v -> recyclerOnItemClickHandler.onItemClicked(position));
        }

    }
}

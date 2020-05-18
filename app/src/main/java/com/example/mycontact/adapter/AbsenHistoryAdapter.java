package com.example.mycontact.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mycontact.R;
import com.example.mycontact.model.AbsenHistory;
import java.util.List;

public class AbsenHistoryAdapter extends RecyclerView.Adapter<AbsenHistoryAdapter.ViewHolder> {

    private Context context;
    private List<AbsenHistory> listAbsenHistory;

    public AbsenHistoryAdapter(Context context, List<AbsenHistory> listAbsenHistory) {
        this.context = context;
        this.listAbsenHistory = listAbsenHistory;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.row_absen_history, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(listAbsenHistory.get(position));
    }

    @Override
    public int getItemCount() {
        return listAbsenHistory.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTimeIn;
        TextView tvDateIn;
        TextView tvStatus;
        TextView tvType;
        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTimeIn = itemView.findViewById(R.id.tv_timein_history);
            tvDateIn = itemView.findViewById(R.id.tv_datein_history);
            tvStatus = itemView.findViewById(R.id.tv_status_history);
            tvType = itemView.findViewById(R.id.tv_type_absen_history);
            view = itemView.findViewById(R.id.view_row_absen_history);

        }

        void bindData(AbsenHistory absen) {
            tvTimeIn.setText(absen.getTimeIn());
            tvDateIn.setText(absen.getDateIn());
            tvStatus.setText(absen.getStatus());

            if (absen.getType().equals("0")) {
                view.setBackgroundColor(context.getResources().getColor(R.color.colorGreen));
                tvType.setText("Check In");
            } else {
                view.setBackgroundColor(context.getResources().getColor(R.color.colorBlue));
                tvStatus.setVisibility(View.INVISIBLE);
                tvType.setText("Check Out");
            }
        }

    }
}

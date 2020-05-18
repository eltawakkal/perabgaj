package com.example.mycontact.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycontact.R;
import com.example.mycontact.model.User;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class KaryawanAdapter extends RecyclerView.Adapter<KaryawanAdapter.ViewHolder> {

    public interface OnClick {
        void onItemClick(int position);
    }

    private OnClick onClick;
    private List<User> listKaryawan;
    private Context context;

    public KaryawanAdapter(List<User> listKaryawan, Context context, OnClick onClick) {
        this.onClick = onClick;
        this.listKaryawan = listKaryawan;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_karyawan, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(listKaryawan.get(position), position);
    }

    @Override
    public int getItemCount() {
        return listKaryawan.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rootView;
        TextView tvName, tvEmail, tvGaji, tvMakan;
        ImageView imgKaryawan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            rootView = itemView.findViewById(R.id.rl_row_karyawan);
            tvName = itemView.findViewById(R.id.tv_row_karyawan);
            tvEmail = itemView.findViewById(R.id.tv_email_row_karyawan);
            tvMakan = itemView.findViewById(R.id.tv_gaji_karyawan);
            tvGaji = itemView.findViewById(R.id.tv_makan_karyawan);

            imgKaryawan = itemView.findViewById(R.id.img_row_karyawan);
        }

        void bindData(User user, int position) {
            tvName.setText(user.getName());
            tvEmail.setText(user.getEmail());
            tvGaji.setText(convertToRupiah(user.getGaji()));
            tvMakan.setText(convertToRupiah(user.getMakan()));

            try {
                Picasso.get().load(user.getPhoto()).into(imgKaryawan);
            } catch (Exception e) {
                Picasso.get().load(R.drawable.user).into(imgKaryawan);
            }

            rootView.setOnClickListener(v -> {onClick.onItemClick(position);});
        }

        String convertToRupiah(String number) {
            String result = NumberFormat.getCurrencyInstance(new Locale("in", "id")).format(Integer.parseInt(number));

            return result;
        }
    }
}

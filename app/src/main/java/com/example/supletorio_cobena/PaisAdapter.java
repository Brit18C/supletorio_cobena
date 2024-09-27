package com.example.supletorio_cobena;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;

import java.util.List;

public class PaisAdapter extends RecyclerView.Adapter<PaisAdapter.PaisViewHolder> {
    private List<Pais> paises;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Pais pais);
    }

    public PaisAdapter(List<Pais> paises, OnItemClickListener listener) {
        this.paises = paises;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PaisViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pais, parent, false);
        return new PaisViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaisViewHolder holder, int position) {
        Pais pais = paises.get(position);
        holder.bind(pais, listener);
    }

    @Override
    public int getItemCount() {
        return paises.size();
    }

    static class PaisViewHolder extends RecyclerView.ViewHolder {
        ImageView banderaImageView;
        TextView nombreTextView;

        PaisViewHolder(View itemView) {
            super(itemView);
            banderaImageView = itemView.findViewById(R.id.banderaImageView);
            nombreTextView = itemView.findViewById(R.id.nombreTextView);
        }

        void bind(final Pais pais, final OnItemClickListener listener) {
            nombreTextView.setText(pais.getNombre());

            Glide.with(banderaImageView.getContext())
                    .load("http://www.geognos.com/api/en/countries/flag/" + pais.getCodigoAlpha2() + ".png")
                    .into(banderaImageView);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(pais);
                }
            });
        }
    }
}

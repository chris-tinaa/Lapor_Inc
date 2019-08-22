package com.example.laporinc.badgedetail;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.laporinc.R;
import com.example.laporinc.akun.Badge;

import java.util.ArrayList;

public class GridBadgeAdapter extends RecyclerView.Adapter<GridBadgeAdapter.GridViewHolder> {

    private ArrayList<BadgeDetail> listBadge;


    public GridBadgeAdapter(ArrayList<BadgeDetail> listBadge) {
        this.listBadge = listBadge;
    }

    @NonNull
    @Override
    public GridBadgeAdapter.GridViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from( viewGroup.getContext() ).inflate( R.layout.grid_badge_item, viewGroup,false );
        return new GridViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull GridBadgeAdapter.GridViewHolder holder, int pos) {

        holder.badgeName.setText( listBadge.get( pos ).getBadgeName() );
        holder.progressBar.setProgress( listBadge.get( pos ).getProgress() );

    }

    @Override
    public int getItemCount() {
        return listBadge.size();
    }

    public class GridViewHolder extends RecyclerView.ViewHolder {

        TextView badgeName;
        ProgressBar progressBar;

        public GridViewHolder(@NonNull View itemView) {
            super( itemView );

            badgeName = itemView.findViewById( R.id.tv_nama_badge );
            progressBar = itemView.findViewById( R.id.badge_progress );
        }
    }
}

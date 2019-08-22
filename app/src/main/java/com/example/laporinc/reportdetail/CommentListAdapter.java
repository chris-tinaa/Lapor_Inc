package com.example.laporinc.reportdetail;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.laporinc.R;
import com.example.laporinc.user.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.CommentListViewHolder> {

    private ArrayList<Comment> commentList;
    private Context context;

    public CommentListAdapter(ArrayList<Comment> commentList, Context context) {
        this.commentList = commentList;
        this.context = context;
    }

    @NonNull
    @Override
    public CommentListAdapter.CommentListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from( viewGroup.getContext() );
        View view = layoutInflater.inflate( R.layout.comment_item, viewGroup, false );


        return new CommentListAdapter.CommentListViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull final CommentListAdapter.CommentListViewHolder holder, int i) {
        holder.waktu.setText( commentList.get( i ).getWaktu() );
        holder.isiKomentar.setText( commentList.get( i ).getIsiKomentar() );

        //holder.nama.setText( commentList.get( i ).getIdentitas() );
        String userId = commentList.get( i ).getIdentitas();
        final String[] nama = new String[1];

        if (!userId.equals( ReportDetailActivity.IDENTITAS_ANONIM )) {

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child( "users" ).child( userId ).addValueEventListener( new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {


                    User user = dataSnapshot.getValue( User.class );

                    if (user != null) {
                        nama[0] = user.getNama();
                        holder.nama.setText( nama[0] );
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            } );

        } else {
            holder.nama.setText( ReportDetailActivity.IDENTITAS_ANONIM );
        }


        holder.nama.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        } );


    }

    @Override
    public int getItemCount() {
        return (commentList != null) ? (commentList.size()) : 0;
    }

    public class CommentListViewHolder extends RecyclerView.ViewHolder {

        private TextView nama, waktu, isiKomentar;

        public CommentListViewHolder(@NonNull View itemView) {
            super( itemView );

            nama = (TextView) itemView.findViewById( R.id.tv_name );
            waktu = (TextView) itemView.findViewById( R.id.tv_time );
            isiKomentar = (TextView) itemView.findViewById( R.id.tv_comment );
        }
    }
}

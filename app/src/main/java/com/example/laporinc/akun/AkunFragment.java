package com.example.laporinc.akun;

import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.laporinc.R;

import java.util.ArrayList;


public class AkunFragment extends Fragment {

    private RecyclerView badgeRecyclerView;
    private ArrayList<Badge> badgeList;
    private BadgeListAdapter badgeListAdapter;

    public AkunFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_akun, container,false);


        //set Recycler View
        badgeList = new ArrayList<>(  );
        badgeRecyclerView = (RecyclerView) view.findViewById( R.id.rv_badge_list );
        badgeRecyclerView.addItemDecoration( new DividerItemDecoration( getActivity(), LinearLayoutManager.HORIZONTAL ){
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                // Do not draw the divider
            }
        } );
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager( getActivity(), LinearLayoutManager.HORIZONTAL, false );
        badgeRecyclerView.setLayoutManager( horizontalLayoutManager );
        badgeListAdapter = new BadgeListAdapter( badgeList, getContext() );
        badgeRecyclerView.setAdapter( badgeListAdapter );
        addBadge();

        return view;
    }

    private void addBadge(){
        badgeList.add( new Badge( R.drawable.ellipse ) );
        badgeList.add( new Badge( R.drawable.ellipse ) );
        badgeList.add( new Badge( R.drawable.ellipse ) );
        badgeList.add( new Badge( R.drawable.ellipse ) );
        badgeList.add( new Badge( R.drawable.ellipse ) );
        badgeList.add( new Badge( R.drawable.ellipse ) );
        badgeList.add( new Badge( R.drawable.ellipse ) );
        badgeList.add( new Badge( R.drawable.ellipse ) );

        badgeListAdapter.notifyDataSetChanged();
    }
}

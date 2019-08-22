package com.example.laporinc.badgedetail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.laporinc.R;

import java.util.ArrayList;

public class BadgeActivity extends AppCompatActivity {

    private RecyclerView rvBadge;
    private ArrayList<BadgeDetail> list = new ArrayList<>(  );
    private String title = "Badge Saya";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_badge );

        setActionBarTitle( title );

        rvBadge = findViewById( R.id.rv_badge );
        rvBadge.setHasFixedSize( true );

        list.addAll( BadgesData.getListData() );
        showRecyclerGrid();

    }


    private void showRecyclerGrid(){
        rvBadge.setLayoutManager( new GridLayoutManager( this, 3 ) );
        GridBadgeAdapter gridBadgeAdapter = new GridBadgeAdapter( list );
        rvBadge.setAdapter( gridBadgeAdapter );
    }


    // mengatur action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate( R.menu.close_icon, menu );

        return super.onCreateOptionsMenu( menu );
    }

    // respon onclick icon "close" di action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();

        return super.onOptionsItemSelected( item );
    }

    private void setActionBarTitle(String title){
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle( title );
        }
    }
}

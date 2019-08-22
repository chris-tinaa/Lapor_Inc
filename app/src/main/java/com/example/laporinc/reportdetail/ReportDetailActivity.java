package com.example.laporinc.reportdetail;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.example.laporinc.MainActivity;
import com.example.laporinc.R;
import com.example.laporinc.recent.Post;
import com.example.laporinc.user.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import me.relex.circleindicator.CircleIndicator;

public class ReportDetailActivity extends AppCompatActivity {

    public static final String IDENTITAS_ANONIM = "Anonim";

    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private PagerAdapter pagerAdapter;
    private ArrayList<ImageModel> imageList;

    private RecyclerView commentsRecyclerView;
    private ArrayList<Comment> commentList;
    private CommentListAdapter commentListAdapter;

    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String identitas;

    private String namaUser = "";
    private String key = "";
    private String userId = "";

    private TextView mWaktu;
    private TextView mLokasi;
    private TextView mDeskripsi;

    private TextView jmlKomentar;
    private EditText isiKomentar;
    private Switch switchAnonim;
    private ProgressBar progressBar;
    private String jumlahKomentar;

    private TextView mJenisPelanggaran;

    private String waktu, lokasi, deskripsi, jenisPelanggaran, imageKey;

    private Handler mainThreadHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_report_detail );

        databaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        userId = user.getUid();
        identitas = userId;

        getUserData();

        mWaktu = findViewById( R.id.tv_time );
        mLokasi = findViewById( R.id.tv_location );
        mDeskripsi = findViewById( R.id.tv_description );
        mJenisPelanggaran = findViewById( R.id.tv_jenis_pelanggaran );

        key = getIntent().getStringExtra( "postKey" );
        imageKey = getIntent().getStringExtra( "imageKey" );

        getPostData( key );


        // set view pager (untuk image collection)
        viewPager = findViewById( R.id.view_pager );
        circleIndicator = findViewById( R.id.circle );
        imageList = new ArrayList<>();
        pagerAdapter = new PagerAdapter( imageList, this );
//        imageList.add( new ImageModel( R.drawable.sample_pic ) );
//        imageList.add( new ImageModel( R.drawable.sample_pic ) );
//        imageList.add( new ImageModel( R.drawable.sample_pic ) );
//        imageList.add( new ImageModel( R.drawable.sample_pic ) );
        getImageCollection( imageKey );


        // This handler is used to handle child thread message from main thread message queue.
        mainThreadHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    // Update view component text, this is allowed.
                    mWaktu.setText( waktu );
                    mLokasi.setText( lokasi );
                    mDeskripsi.setText( deskripsi );
                    mJenisPelanggaran.setText( jenisPelanggaran );
                } else if (msg.what == 2) {
                    viewPager.setAdapter( pagerAdapter );
                    //pagerAdapter.notifyDataSetChanged();
                    circleIndicator.setViewPager( viewPager );
                } else if (msg.what == 3) {
                    jmlKomentar = (TextView) findViewById( R.id.tv_komentar );
                    jmlKomentar.setText( "Komentar " + jumlahKomentar );
                    commentListAdapter.notifyDataSetChanged();

                }

            }
        };


        // set switch on checked listener
        switchAnonim = (Switch) findViewById( R.id.switch_anonim );
        switchAnonim.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    identitas = IDENTITAS_ANONIM;
                } else {
                    identitas = userId;
                }
            }
        } );


        //set recycler view (untuk komentar)
        commentList = new ArrayList<>();
        commentsRecyclerView = (RecyclerView) findViewById( R.id.rv_comments );
        commentListAdapter = new CommentListAdapter( commentList, this );
        LinearLayoutManager layoutManager = new LinearLayoutManager( this );
        layoutManager.setOrientation( LinearLayoutManager.VERTICAL );
        commentsRecyclerView.setLayoutManager( layoutManager );
        commentsRecyclerView.setAdapter( commentListAdapter );

        getComments();
    }

    private void getComments() {

        //commentList.add( new Comment( "Anonim", "1 hour ago", "accumsan nisl vitae, ornare purus. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean sed feugiat nisl. " ) );
        //commentList.add( new Comment( "Budi Rahasa", "1 hour ago", "accumsan nisl vitae, ornare purus. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean sed feugiat nisl. " ) );
        //commentList.add( new Comment( "Deti Dwi", "1 hour ago", "accumsan nisl vitae, ornare purus. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean sed feugiat nisl. " ) );
        //commentList.add( new Comment( "Anonim", "1 hour ago", "accumsan nisl vitae, ornare purus. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean sed feugiat nisl. " ) );

        databaseReference.child( "posts" ).child( key ).child( "comments" ).addValueEventListener( new ValueEventListener() {

            Post post;

            @Override
            public void onDataChange(final DataSnapshot snapshot) {

                commentList.clear();

                new Thread( new Runnable() {
                    @Override
                    public void run() {

                        jumlahKomentar = String.valueOf( (int) snapshot.getChildrenCount() );

                        for (DataSnapshot commentSnapshot : snapshot.getChildren()) {

                            Comment comment = commentSnapshot.getValue( Comment.class );
                            commentList.add( comment );

                        }


                        Message message = new Message();
                        message.what = 3;
                        //message.arg1 = i;
                        mainThreadHandler.sendMessage( message );

                    }

                } ).start();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );


    }


    public void submitComment(View view) {

        progressBar = (ProgressBar) findViewById( R.id.progressBar );
        isiKomentar = (EditText) findViewById( R.id.et_add_comment );

        progressBar.setVisibility( View.VISIBLE );
        progressBar.getIndeterminateDrawable().setColorFilter( Color.parseColor( "#4593EE" ), android.graphics.PorterDuff.Mode.SRC_ATOP);


        DateFormat dateFormat = new SimpleDateFormat( "dd/MM/yyyy HH:mm" );
        Date date = new Date();
        String waktuSekarang = dateFormat.format( date );

        Comment comment = new Comment( identitas, waktuSekarang, isiKomentar.getText().toString() );
        String commentKey = databaseReference.child( "posts" ).child( key ).child( "comments" ).push().getKey();

        databaseReference.child( "posts" ).child( key ).child( "comments" ).child( commentKey ).setValue( comment );


        final int[] jumlahKomentar = new int[1];

        databaseReference.child( "posts" ).child( key ).child( "comments" ).addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot snapshot) {

                jumlahKomentar[0] = (int) snapshot.getChildrenCount() ;
                databaseReference.child( "posts" ).child( key ).child( "jumlahKomentar" ).setValue( String.valueOf(jumlahKomentar[0]));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );



        progressBar.setVisibility( View.GONE );
        isiKomentar.getText().clear();
        switchAnonim.setChecked( false );

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

    private void getPostData(String key) {

        databaseReference.child( "posts" ).child( key ).addValueEventListener( new ValueEventListener() {

            Post post;

            @Override
            public void onDataChange(final DataSnapshot snapshot) {

                new Thread( new Runnable() {
                    @Override
                    public void run() {

                        Post post = snapshot.getValue( Post.class );

                        lokasi = post.getLokasi();
                        deskripsi = post.getDeskripsi();
                        waktu = post.getDate();
                        jenisPelanggaran = post.getJenisPelanggaran();

                        Message message = new Message();
                        message.what = 1;
                        //message.arg1 = i;
                        mainThreadHandler.sendMessage( message );

                    }

                } ).start();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );

    }

    private void getUserData() {

        databaseReference.child( "users" ).child( userId ).addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                new Thread( new Runnable() {

                    @Override
                    public void run() {

                        User user = dataSnapshot.getValue( User.class );
                        //homeFragment.setNamaUser( user.getNama() );
                        namaUser = user.getNama();

                    }
                } ).start();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        } );
    }


    private void getImageCollection(String imageKey) {

        databaseReference.child( "images" ).child( imageKey ).addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot snapshot) {

                new Thread( new Runnable() {
                    @Override
                    public void run() {

                        int jumlahImage = (int) snapshot.getChildrenCount();

                        for (int i = 0; i < jumlahImage; i++) {
                            imageList.add( new ImageModel( (String) snapshot.child( String.valueOf( i ) ).getValue() ) );
                            Log.i( "images", (String) snapshot.child( String.valueOf( i ) ).getValue() );
                        }

                        //thumbnailUri[0] = (String) snapshot.child( dataList.get( i ).getImageKey() ).child( "0" ).getValue();
                        Message message = new Message();
                        message.what = 2;
                        //message.arg1 = i;
                        mainThreadHandler.sendMessage( message );

                    }

                } ).start();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );


    }

    // Dismiss keyboard when click outside of EditText
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();

        if (v != null &&
                (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText &&
                !v.getClass().getName().startsWith( "android.webkit." )) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen( scrcoords );
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                hideKeyboard( this );
        }
        return super.dispatchTouchEvent( ev );
    }

    public static void hideKeyboard(ReportDetailActivity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService( Context.INPUT_METHOD_SERVICE );
            imm.hideSoftInputFromWindow( activity.getWindow().getDecorView().getWindowToken(), 0 );
        }
    }
}

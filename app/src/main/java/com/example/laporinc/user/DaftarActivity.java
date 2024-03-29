package com.example.laporinc.user;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.laporinc.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DaftarActivity extends AppCompatActivity {

    private EditText etNama;
    private EditText etEmail;
    private EditText etPassword;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder( DaftarActivity.this )
                .setIcon( android.R.drawable.ic_dialog_alert )
                .setTitle( "Kembali" )
                .setMessage( "Apakah anda ingin membatalkan?" )
                .setNegativeButton( "Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                } )
                .setPositiveButton( "Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent( getApplicationContext(), LoginActivity.class );
                        startActivity( intent );
                        finish();
                    }
                } )
                .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_daftar );

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        etNama = (EditText) findViewById( R.id.et_nama );
        etEmail = (EditText) findViewById( R.id.et_email );
        etPassword = (EditText) findViewById( R.id.et_password );

        progressBar = (ProgressBar) findViewById( R.id.progressBar );

    }

    //fungsi ini untuk mendaftarkan data pengguna ke Firebase
    public void signUp(View view) {
        Log.d( "TAG", "signUp" );
        if (!validateForm()) {
            return;
        }

        //showProgressBar
        progressBar.setVisibility(View.VISIBLE);
        progressBar.getIndeterminateDrawable().setColorFilter( Color.parseColor( "#4593EE" ), android.graphics.PorterDuff.Mode.SRC_ATOP);

        getWindow().setFlags( WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        mAuth.createUserWithEmailAndPassword( email, password ).addOnCompleteListener( this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d( "TAG", "createUser:onComplete:" + task.isSuccessful() );

                //hideProgressBar
                progressBar.setVisibility( View.GONE );
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                if (task.isSuccessful()) {
                    onAuthSuccess( task.getResult().getUser() );
                } else {
                    {
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthWeakPasswordException weakPassword) {
                            etPassword.setError( "Minimal 6 karakter" );
                        } catch (FirebaseAuthInvalidCredentialsException malformedEmail) {
                            etEmail.setError( "Masukkan email yang valid" );
                        } catch (FirebaseAuthUserCollisionException existEmail) {
                            etEmail.setError( "Email sudah pernah terdaftar" );
                        } catch (Exception e) {
                            Toast.makeText( DaftarActivity.this, "Gagal mendaftar", Toast.LENGTH_SHORT ).show();
                        }
                    }

                }
            }
        } );
    }

    //fungsi dipanggil ketika proses Authentikasi berhasil
    private void onAuthSuccess(FirebaseUser user) {

        writeNewUser( user.getUid(), etNama.getText().toString(), etEmail.getText().toString() );
        Toast.makeText( this, "Berhasil mendaftar", Toast.LENGTH_SHORT ).show();

        // Go to LoginActivity
        startActivity( new Intent( getApplicationContext(), LoginActivity.class ) );
        finish();
    }

    /*
        ini fungsi buat bikin username dari email
            contoh email: abcdefg@mail.com
            maka username nya: abcdefg
     */
    private String usernameFromEmail(String email) {
        if (email.contains( "@" )) {
            return email.split( "@" )[0];
        } else {
            return email;
        }
    }

    //fungsi untuk memvalidasi EditText email dan password agar tak kosong dan sesuai format
    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty( etEmail.getText().toString() )) {
            etEmail.setError( "Harus diisi" );
            result = false;
        } else {
            etEmail.setError( null );
        }

        if (TextUtils.isEmpty( etPassword.getText().toString() )) {
            etPassword.setError( "Harus diisi" );
            result = false;
        } else {
            etPassword.setError( null );
        }


        if (TextUtils.isEmpty( etNama.getText().

                toString() )) {
            etNama.setError( "Harus diisi" );
            result = false;
        } else {
            etNama.setError( null );
        }

        return result;
    }

    // menulis ke Database
    private void writeNewUser(String userId, String nama, String email) {
        User user = new User( email, nama, 0 );

        mDatabase.child( "users" ).child( userId ).setValue( user );
    }


    // Dismiss keyboard when click outside of EditText
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();

        if (v != null &&
                (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText &&
                !v.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom()) hideKeyboard(this);
        }
        return super.dispatchTouchEvent(ev);
    }
    public static void hideKeyboard(DaftarActivity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService( Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }



}

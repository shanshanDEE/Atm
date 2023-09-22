package com.example.atm;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_CAMERA = 5;
    private EditText edUserid;
    private EditText edPasswd;
    private static final String TAG = LoginActivity.class.getSimpleName();
    private CheckBox cd_remember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if(permission == PackageManager.PERMISSION_GRANTED) {
            //takePhoto();
        }else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},REQUEST_CODE_CAMERA);
        }

        getSharedPreferences("atm",MODE_PRIVATE)
                .edit()
                .putInt("LEVEL",3)
                .putString("NAME","TOM")
                .commit();
        int level = getSharedPreferences("atm",MODE_PRIVATE)
                .getInt("LEVEL",0);
        Log.d(TAG, "onCreate:" + level);
        edUserid = findViewById(R.id.userid);
        edPasswd = findViewById(R.id.passwd);
        cd_remember = findViewById(R.id.cd_rem_userid);
        cd_remember.setChecked(
                getSharedPreferences("atm",MODE_PRIVATE)
                        .getBoolean("REMEMBER_USERID",false));
        cd_remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getSharedPreferences("atm",MODE_PRIVATE)
                        .edit()
                        .putBoolean("REMEMBER_USERID",isChecked)
                        .apply();
            }
        });
        String userid = getSharedPreferences("atm", MODE_PRIVATE)
                .getString("USERID","");
        edUserid.setText(userid);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE_CAMERA){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                takePhoto();
            }
        }
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivity(intent);
    }

    public void login(View view){
        final String userid = edUserid.getText().toString();
        String passwd = edPasswd.getText().toString();
        FirebaseDatabase.getInstance().getReference("users").child(userid).child("password")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                       String pw = (String) snapshot.getValue();
                       if(passwd.equals(pw)) {
                           boolean remenber = getSharedPreferences("atm",MODE_PRIVATE)
                                   .getBoolean("REMEMBER_USERID",false);
                           if(remenber){
                               getSharedPreferences("atm",MODE_PRIVATE)
                                       .edit()
                                       .putString("USERID",userid)
                                       .apply();
                           }
                           setResult(RESULT_OK);
                           finish();
                       }else{
                           new  AlertDialog.Builder(LoginActivity.this)
                                   .setTitle("登入結果")
                                   .setMessage("登入失敗")
                                   .setPositiveButton("OK",null)
                                   .show();
                       }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
       /* if("jack".equals(userid) && "1234".equals(passwd))
        {

        }*/
    }
    public void  quit(View view){

    }
}
package com.example.myapplication;


import static com.example.myapplication.Board.p_name;
import static com.example.myapplication.Database.name;

import android.app.Activity;
import android.content.Intent;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LogInActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private CheckBox checkauto;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail=findViewById(R.id.edittext_email);
        editTextPassword=findViewById(R.id.edittext_password);
        checkauto=(CheckBox)findViewById(R.id.ck_auto);

        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.btn_signup).setOnClickListener(this);

        findViewById(R.id.btn_find).setOnClickListener(this);

        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);

        checkauto.setChecked(pref.getBoolean("buttonauto", false));
    }




    @Override
    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        checkauto = (CheckBox)findViewById(R.id.ck_auto);
        if(checkauto.isChecked()==true)
        {
            if (currentUser != null) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        }
    }

    public void onStop()
    {
        super.onStop();
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        CheckBox check1 = (CheckBox)findViewById(R.id.ck_auto);
        editor.putBoolean("checkauto", check1.isChecked());
        editor.commit();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                String s_email=editTextEmail.getText().toString();
                String s_pw=editTextPassword.getText().toString();
                if(s_email ==null ||s_pw==null)
                {
                    Toast.makeText(LogInActivity.this,"Login Error",Toast.LENGTH_SHORT).show();
                }

                if(s_email!=null && s_pw!=null) {
                    loginStart(s_email, s_pw);
                }
                break;
            case R.id.btn_signup:
                startActivity(new Intent(this,SignUpActivity.class));
                break;
            case R.id.btn_find:
                startActivity(new Intent(this,FindpwActivity.class));
                break;
        }

    }
    public void loginStart(String email, String password){
        Toast.makeText(LogInActivity.this,"loginStart" ,Toast.LENGTH_SHORT).show();
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Toast.makeText(LogInActivity.this,"로그인중" ,Toast.LENGTH_SHORT).show();
                if (!task.isSuccessful()) {
                    Toast.makeText(LogInActivity.this,"Login error",Toast.LENGTH_SHORT).show();

                }else{
                    currentUser = mAuth.getCurrentUser();
                    Toast.makeText(LogInActivity.this, "로그인 성공" + "/" + currentUser.getEmail() + "/" + currentUser.getUid() ,Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LogInActivity.this, MainActivity.class));
                    finish();
                }

            }
        });
    }



}
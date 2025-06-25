package com.example.myapplication;



import android.graphics.Color;
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
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
        private FirebaseAuth firebaseAuth;
        private EditText editTextEmail;
        private EditText editTextPassword;
        private EditText editTextName;
        private EditText editTextbussino;
        private Button buttonJoin;
        private Button duplicate_button;
        private Button duplicate_bussino;
        private FirebaseAuth mAuth = FirebaseAuth.getInstance();
        private FirebaseFirestore mStore = FirebaseFirestore.getInstance();



        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_signup);

            firebaseAuth = FirebaseAuth.getInstance();

            editTextEmail = (EditText) findViewById(R.id.editText_email);
            editTextPassword = (EditText) findViewById(R.id.editText_passWord);
            editTextName = (EditText) findViewById(R.id.editText_name);
            editTextbussino = (EditText) findViewById(R.id.editText_bussino);
            buttonJoin = (Button) findViewById(R.id.btn_join);


            //ID 중복 체크 버튼 구현
            duplicate_button = (Button) findViewById(R.id.btn_duplicate);
            duplicate_button.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAuth.fetchSignInMethodsForEmail(editTextEmail.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                                @Override
                                public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                    if (task.getResult().getSignInMethods().size() == 0) {
                                        // email not existed
                                        Toast.makeText(getApplicationContext(), "이 이메일을 사용할 수 있습니다.", Toast.LENGTH_SHORT).show();
                                        duplicate_button.setEnabled(false);
                                        duplicate_button.setBackgroundColor(Color.GRAY);
                                        duplicate_button.setText("확인완료");
                                    } else {
                                        // email existed
                                        Toast.makeText(getApplicationContext(), "이 이메일은 이미 사용중입니다.", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }
            });



            buttonJoin.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick (View view){
                    if (duplicate_button.isEnabled()) //이메일 중복검사가 안되어 있을 때
                    {
                        Toast.makeText(getApplicationContext(), "이메일 중복검사를 해주세요", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        //비밀번호와 비밀번호 확인이 일치하고 이메일이 공란이 아닐때 조건을 추가해야함
                        mAuth.createUserWithEmailAndPassword(editTextEmail.getText().toString(), editTextPassword.getText().toString())
                                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        //if (sEmail!=null && sPw == sPw_chk) {//이메일 아이디를 입력하고 비밀번호와 비밀번호 확인이 일치하면 회원가입가능
                                        if (task.isSuccessful()) {

                                            // Sign in success, update UI with the signed-in user's information
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            Map<String, Object> userMap = new HashMap<>();
                                            userMap.put(Database.documentId, user.getUid());
                                            userMap.put(Database.name, editTextName.getText().toString());
                                            userMap.put(Database.bussino, editTextbussino.getText().toString());
                                            userMap.put(Database.email, editTextEmail.getText().toString());
                                            userMap.put(Database.password, editTextPassword.getText().toString());
                                            mStore.collection(Database.user).document(user.getUid()).set(userMap, SetOptions.merge());
                                            mStore.collection("UserInfo");
                                            finish();
                                            // }
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Toast.makeText(SignUpActivity.this, "Signup error.",
                                                    Toast.LENGTH_SHORT).show();

                                        }

                                        // ...
                                    }
                                });
                    }

                }

                });
            }
        }



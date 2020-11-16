package abvgiet.library.libzy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import abvgiet.library.libzy.module.AdminActivity;
import abvgiet.library.libzy.module.UserActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText emailId, pass;
    private TextView forgetpass;
   private FloatingActionButton btn;
    private FirebaseAuth mAuth;

    @SuppressLint("WrongViewCast")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mAuth = FirebaseAuth.getInstance();


        emailId = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        forgetpass = findViewById(R.id.Fp_pass);

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;

        forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,Forgot_Password.class);
                startActivity(i);
            }
        });



        btn = (FloatingActionButton) findViewById(R.id.login);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = emailId.getText().toString().trim();
                String pwd = pass.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(LoginActivity.this,"Please enter email",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(pwd)){
                    Toast.makeText(LoginActivity.this,"Please enter Password",Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, pwd)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
                                    if (currentFirebaseUser.getUid().equals("98vJVfYJsAWJpWKPLCeiRUXWvgY2")){
                                           Intent intToAdmin = new Intent(LoginActivity.this, AdminActivity.class);
                                         startActivity(intToAdmin);

                                    }else {
                                        FirebaseUser user = mAuth.getCurrentUser();

                                        String email = user.getEmail();
                                        String uid = user.getUid();

                                        HashMap<Object,String>hashMap = new HashMap<>();
                                        hashMap.put("email",email);
                                        hashMap.put("name","");
                                        hashMap.put("phone","");
                                        hashMap.put("rollno","");
                                        hashMap.put("stream","");

                                        FirebaseDatabase database = FirebaseDatabase.getInstance();

                                        DatabaseReference reference = database.getReference("Users");
                                        reference.child(uid).setValue(hashMap);
                                        Intent intToAdmin = new Intent(LoginActivity.this, Edit_User_Details.class);
                                        startActivity(intToAdmin);
                                        finish();


                                    }


                                } else {
                                  Toast.makeText(LoginActivity.this,"it is invalid",Toast.LENGTH_SHORT).show();

                               }

                            }
                        });







            }
        });

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,

                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
       getWindow().setStatusBarColor(Color.TRANSPARENT);




    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null ){

            Intent intent = new Intent(LoginActivity.this, UserActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
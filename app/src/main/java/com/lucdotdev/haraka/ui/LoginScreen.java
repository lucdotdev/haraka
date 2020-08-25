package com.lucdotdev.haraka.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lucdotdev.haraka.R;
import com.lucdotdev.haraka.helpers.CustomLoadingDialog;
import com.lucdotdev.haraka.helpers.StringsOperations;
import com.lucdotdev.haraka.ui.home_store.StoreHomeScreen;

import java.util.Objects;

public class LoginScreen extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText email;
    private EditText password;
    private final StringsOperations customStringOperation = new StringsOperations();
    private final CustomLoadingDialog customLoading = new CustomLoadingDialog(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        this.email = (EditText) findViewById(R.id.loginEmail);
        this.password = (EditText) findViewById(R.id.loginPassword);
        mAuth = FirebaseAuth.getInstance();

    }


    public void  login(View view) {
        if(customStringOperation.isEmpty(email)|| customStringOperation.isEmpty(password)){
            CharSequence text = "L'adress email ou le mot de passe est vide";
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(this, text, duration).show();
        } else if(customStringOperation.isValidEmail(email)){
            customLoading.startLoading();
            mAuth.signInWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                auth(true, "");
                            } else {
                                auth(false, Objects.requireNonNull(task.getException()).getMessage());
                            }
                        }
                    });
        } else {
            CharSequence text = " Adress email invalide !";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(this, text, duration);
            toast.show();
        }
    }


    private void auth(boolean k, String msg){
        if (k) {
            FirebaseUser user = mAuth.getCurrentUser();
            assert user != null;
            customLoading.dismissLoading();
            Intent intent=new Intent(this, StoreHomeScreen.class);
            intent.putExtra("name", user.getDisplayName());
            startActivity(intent);
            finish();
        } else {
            customLoading.dismissLoading();
            // If sign in fails, display a message to the user.
            Toast.makeText(this, "L' authentication a échoué." +msg,
                    Toast.LENGTH_SHORT).show();
        }
    }


}
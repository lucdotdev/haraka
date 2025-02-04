package com.lucdotdev.haraka.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.lucdotdev.haraka.R;
import com.lucdotdev.haraka.helpers.CustomLoadingDialog;
import com.lucdotdev.haraka.helpers.StringsOperations;
import com.lucdotdev.haraka.ui.home_livreur.LivreurHomeScreen;
import com.lucdotdev.haraka.ui.home_store.StoreHomeScreen;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LoginScreen extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
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
        firebaseFirestore = FirebaseFirestore.getInstance();

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

            firebaseFirestore.collection("users").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                   if(task.isSuccessful()){
                       DocumentSnapshot t = task.getResult();
                       assert t != null;
                       FirebaseInstanceId.getInstance().getInstanceId()
                               .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                   @Override
                                   public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                       if (!task.isSuccessful()) {
                                           return;
                                       }

                                       // Get new Instance ID token
                                       String token = Objects.requireNonNull(task.getResult()).getToken();
                                       Map<String, Object> d = new HashMap<>();
                                       d.put("message_id", token);
                                       FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                                       FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                                       firebaseFirestore.collection("users").document(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid()).update(d);

                                       // Log and toast
                                      //  String msg = getString(R.string.msg_token_fmt, token);
                                      // Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                                   }
                               });
                       gotToNext((Long) t.get("account_type"),(String) t.get("name"));
                   } else {
                       Toast.makeText(LoginScreen.this, "Erreur inconue",
                               Toast.LENGTH_SHORT).show();
                   }
                }
            });


        } else {
            customLoading.dismissLoading();
            Toast.makeText(this, "L' authentication a échoué." +msg,
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void gotToNext(Long i, String name){
        customLoading.dismissLoading();
        if(i==1){
            Intent intent=new Intent(this, StoreHomeScreen.class);
            intent.putExtra("name", name);
            saveToPrefs(1, name);
            startActivity(intent);
            finish();
        } else {
            Intent intent=new Intent(this, LivreurHomeScreen.class);
            saveToPrefs(2,name);
            startActivity(intent);
            finish();
        }
    }
    public void saveToPrefs(int accountType,String userName){
        SharedPreferences.Editor editor = getSharedPreferences("AUTH", MODE_PRIVATE).edit();
        editor.putInt("account_type", (int) accountType);
        editor.putString("user_name", userName);
        editor.apply();
    }


}
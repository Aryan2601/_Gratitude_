package amhacks.gratitude.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.content.res.AppCompatResources;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import com.facebook.login.Login;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;

import amhacks.gratitude.R;

public class LoginActivity extends AppCompatActivity {

    private EditText EmailET, PasswordET, ConfPasswordET;
    private Button LoginButton;
    private TextView ForgotPasswordLink;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private String email, password, confirm_password, currentUserID;
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(this);


        loginButton = findViewById(R.id.login_fb_button);



        callbackManager = CallbackManager.Factory.create();

        loginButton.setPermissions(Arrays.asList("user_gender,user_friends"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("log","Login Successful!");
                handleFacebookAccessToken(loginResult.getAccessToken());
                mAuth.signInWithCustomToken(loginResult.getAccessToken().toString());
                Intent setupIntent = new Intent(LoginActivity.this, SetupActivity.class);
                setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(setupIntent);


            }

            @Override
            public void onCancel() {
                Log.d("cancel","Login Cancelled !");

            }

            @Override
            public void onError(FacebookException error) {
                Log.d("error","Login Error");

            }
        });

        mAuth = FirebaseAuth.getInstance();


        firestore = FirebaseFirestore.getInstance();


        EmailET = (EditText) findViewById(R.id.emailAddressLogin);
        PasswordET = (EditText) findViewById(R.id.passwordLogin);
        ConfPasswordET = (EditText) findViewById(R.id.confirmPasswordLogin);
        LoginButton = (Button) findViewById(R.id.login_button);
        ForgotPasswordLink = (TextView) findViewById(R.id.forgotPasswordLink);

        EmailET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @SuppressLint("UseCompatTextViewDrawableApis")
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                {
                    EmailET.setBackgroundTintList(AppCompatResources.getColorStateList(getApplicationContext(), R.color.orange));
                    EmailET.setCompoundDrawableTintList(AppCompatResources.getColorStateList(getApplicationContext(), R.color.orange));
                }
                else
                {
                    EmailET.setBackgroundTintList(AppCompatResources.getColorStateList(getApplicationContext(), R.color.darker_gray));
                    EmailET.setCompoundDrawableTintList(AppCompatResources.getColorStateList(getApplicationContext(), R.color.darker_gray));
                }
            }
        });

        PasswordET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @SuppressLint("UseCompatTextViewDrawableApis")
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                {
                    PasswordET.setBackgroundTintList(AppCompatResources.getColorStateList(getApplicationContext(), R.color.orange));
                    PasswordET.setCompoundDrawableTintList(AppCompatResources.getColorStateList(getApplicationContext(), R.color.orange));
                }
                else
                {
                    PasswordET.setBackgroundTintList(AppCompatResources.getColorStateList(getApplicationContext(), R.color.darker_gray));
                    PasswordET.setCompoundDrawableTintList(AppCompatResources.getColorStateList(getApplicationContext(), R.color.darker_gray));
                }
            }
        });

        ConfPasswordET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @SuppressLint("UseCompatTextViewDrawableApis")
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                {
                    ConfPasswordET.setBackgroundTintList(AppCompatResources.getColorStateList(getApplicationContext(), R.color.orange));
                    ConfPasswordET.setCompoundDrawableTintList(AppCompatResources.getColorStateList(getApplicationContext(), R.color.orange));
                }
                else
                {
                    ConfPasswordET.setBackgroundTintList(AppCompatResources.getColorStateList(getApplicationContext(), R.color.darker_gray));
                    ConfPasswordET.setCompoundDrawableTintList(AppCompatResources.getColorStateList(getApplicationContext(), R.color.darker_gray));
                }
            }
        });

        ForgotPasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendForgotLink();
            }
        });

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });


    }
    private void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.

                        }

                        // ...
                    }
                });
    }
    void login()
    {

        email = EmailET.getText().toString();
        password = PasswordET.getText().toString();

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please fill all the credentials", Toast.LENGTH_SHORT).show();
        }
        else
        {
            progressDialog.setTitle("Please wait");
            progressDialog.setMessage("We are authenticating your credentials");
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Authenticated successfully.", Toast.LENGTH_SHORT).show();
                                Intent dashIntent = new Intent(LoginActivity.this, Dashboard.class);
                                dashIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(dashIntent);
                            }
                            else
                            {
                                try
                                {
                                    throw task.getException();
                                }
                                catch (FirebaseAuthInvalidUserException invalidEmail)
                                {
                                    progressDialog.dismiss();
                                    Toast.makeText(LoginActivity.this, "We can't find your account. Please register", Toast.LENGTH_SHORT).show();
                                    ConfPasswordET.setVisibility(View.VISIBLE);
                                    ForgotPasswordLink.setVisibility(View.INVISIBLE);
                                    LoginButton.setText("REGISTER");
                                    LoginButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            register();
                                        }
                                    });
                                }
                                catch (FirebaseAuthInvalidCredentialsException invalidCredentials)
                                {
                                    progressDialog.dismiss();
                                    Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                                }
                                catch (Exception e) {
                                    progressDialog.dismiss();
                                    e.printStackTrace();
                                }
                            }

                        }
                    });
        }
    }

    void register()
    {
        progressDialog.setMessage("We are registering your account");
        email = EmailET.getText().toString();
        password = PasswordET.getText().toString();
        confirm_password = ConfPasswordET.getText().toString();

        if (password.equals(confirm_password))
        {
            progressDialog.show();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                mAuth.signInWithEmailAndPassword(email, password);

                                currentUserID  = mAuth.getCurrentUser().getUid().toString();

                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("email",email);

                                firestore.collection("Users").document(currentUserID).set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful())
                                        {
                                            progressDialog.dismiss();
                                            Intent setupIntent = new Intent(LoginActivity.this, SetupActivity.class);
                                            setupIntent.putExtra("email",email);
                                            setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(setupIntent);
                                        }
                                        else
                                        {
                                            progressDialog.dismiss();
                                            String err = task.getException().getMessage();
                                            Toast.makeText(LoginActivity.this, err, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                                Toast.makeText(LoginActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                progressDialog.dismiss();
                                String err = task.getException().getMessage();
                                Toast.makeText(LoginActivity.this, err, Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }
        else
        {
            progressDialog.dismiss();
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
        }
    }

    void sendForgotLink()
    {
        email = EmailET.getText().toString();

        if (TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Please provide the email address associated with your account", Toast.LENGTH_SHORT).show();
        }
        else {
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(LoginActivity.this, "Password reset mail sent successfully.", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                String err = task.getException().getMessage();
                                Toast.makeText(LoginActivity.this, err, Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

}
package amhacks.gratitude;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private EditText EmailET, PasswordET, ConfPasswordET;
    private Button LoginButton, LoginFBButton;
    private TextView ForgotPasswordLink;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private String email, password, confirm_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EmailET = (EditText) findViewById(R.id.emailAddressLogin);
        PasswordET = (EditText) findViewById(R.id.passwordLogin);
        ConfPasswordET = (EditText) findViewById(R.id.confirmPasswordLogin);
        LoginButton = (Button) findViewById(R.id.login_button);
        LoginFBButton = (Button) findViewById(R.id.login_fb_button);
        ForgotPasswordLink = (TextView) findViewById(R.id.forgotPasswordLink);

        EmailET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                {
                    EmailET.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.orange));
                }
                else
                {
                    EmailET.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.darker_gray));
                }
            }
        });

        PasswordET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                {
                    PasswordET.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.orange));
                }
                else
                {
                    PasswordET.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.darker_gray));
                }
            }
        });

        ConfPasswordET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                {
                    EmailET.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.orange));
                }
                else
                {
                    EmailET.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.darker_gray));
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
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(LoginActivity.this, "Authenticated successfully.", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                try
                                {
                                    throw task.getException();
                                }
                                catch (FirebaseAuthInvalidUserException invalidEmail)
                                {
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
                                    Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                                }
                                catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    });
        }
    }

    void register()
    {
        email = EmailET.getText().toString();
        password = PasswordET.getText().toString();
        confirm_password = ConfPasswordET.getText().toString();

        if (password.equals(confirm_password))
        {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(LoginActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                String err = task.getException().getMessage();
                                Toast.makeText(LoginActivity.this, err, Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }
        else
        {
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

}
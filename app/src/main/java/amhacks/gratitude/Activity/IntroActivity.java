package amhacks.gratitude.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import amhacks.gratitude.R;

public class IntroActivity extends AppCompatActivity {

    ImageView logo,splashImg;
    TextView appName;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_intro);

        logo = findViewById(R.id.logo);

        appName = findViewById(R.id.app_name);

        splashImg = findViewById(R.id.image);

        mAuth = FirebaseAuth.getInstance();


        splashImg.animate().translationY(-2400).setDuration(1000).setStartDelay(4000);
        logo.animate().translationY(2200).setDuration(1000).setStartDelay(4000);
        appName.animate().translationY(1800).setDuration(1000).setStartDelay(4000).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                checkLogin();

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });;



    }


    void checkLogin() {

        if (mAuth.getCurrentUser() != null)
        {
            Intent dashIntent = new Intent(IntroActivity.this, Dashboard.class);
            dashIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(dashIntent);
        }
        else
        {
            Intent loginIntent = new Intent(IntroActivity.this, LoginActivity.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginIntent);
        }
    }
}
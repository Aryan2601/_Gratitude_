package amhacks.gratitude.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import amhacks.gratitude.R;

public class IntroActivity extends AppCompatActivity {

    ImageView logo,splashImg;
    TextView appName;
   // LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_intro);

        logo = findViewById(R.id.logo);

        appName = findViewById(R.id.app_name);

        splashImg = findViewById(R.id.image);

       // lottieAnimationView = findViewById(R.id.lottie);

        splashImg.animate().translationY(-2400).setDuration(1000).setStartDelay(4000);
        logo.animate().translationY(2200).setDuration(1000).setStartDelay(4000);
        appName.animate().translationY(1800).setDuration(1000).setStartDelay(4000).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Intent loginIntent = new Intent(IntroActivity.this, LoginActivity.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(loginIntent);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });;
    //    lottieAnimationView.animate().translationY(1800).setDuration(1000).setStartDelay(4000);


    }
}
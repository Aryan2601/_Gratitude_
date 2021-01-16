package amhacks.gratitude.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import amhacks.gratitude.R;

public class Dashboard extends AppCompatActivity {
    private RelativeLayout helpSeekerSwitch, helperSwitch;
    private LinearLayout helperLayout, helpSeekerLayout;
    private TextView helpSeekerTxt, helperTxt;
    private String user_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        helperSwitch = (RelativeLayout) findViewById(R.id.helper_switch);
        helpSeekerSwitch = (RelativeLayout) findViewById(R.id.help_seeker_switch);
        helperTxt = (TextView) findViewById(R.id.helper_txt);
        helpSeekerTxt = (TextView) findViewById(R.id.help_seeker_txt);
        helperLayout = (LinearLayout) findViewById(R.id.helper_body);
        helpSeekerLayout = (LinearLayout) findViewById(R.id.help_seeker_body);

        helpSeekerSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helperSwitch.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.orange));
                helperTxt.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));

                helpSeekerSwitch.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                helpSeekerTxt.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.darker_gray));

                helpSeekerLayout.setVisibility(View.VISIBLE);
                helperLayout.setVisibility(View.GONE);

                user_type = "helper";

            }
        });

        helperSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helpSeekerSwitch.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.orange));
                helpSeekerTxt.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));

                helperSwitch.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                helperTxt.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.darker_gray));

                helperLayout.setVisibility(View.VISIBLE);
                helpSeekerLayout.setVisibility(View.GONE);

                user_type = "help_seeker";

            }
        });



    }
}
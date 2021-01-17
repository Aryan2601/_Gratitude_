package amhacks.gratitude.Activity;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import amhacks.gratitude.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class Dashboard extends AppCompatActivity {
    private RelativeLayout helpSeekerSwitch, helperSwitch;
    private LinearLayout helperLayout, helpSeekerLayout, profileLayout;
    private TextView helpSeekerTxt, helperTxt, usernameTxt;
    private String user_type, currentUserID, fullname;
    private FirebaseAuth mAuth;
    private CircleImageView profileView;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
	mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid().toString();

        firestore = FirebaseFirestore.getInstance();

        firestore.collection("Users").document(currentUserID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null)
                {
                    fullname = value.get("fullname").toString();
                    usernameTxt.setText(fullname);
                    Glide.with(getApplicationContext()).load(value.get("profile_picture").toString()).into(profileView);
                }
            }
        });
        helperSwitch = (RelativeLayout) findViewById(R.id.helper_switch);
        helpSeekerSwitch = (RelativeLayout) findViewById(R.id.help_seeker_switch);
        helperTxt = (TextView) findViewById(R.id.helper_txt);
        helpSeekerTxt = (TextView) findViewById(R.id.help_seeker_txt);
        helperLayout = (LinearLayout) findViewById(R.id.helper_body);
        profileView = (CircleImageView) findViewById(R.id.profile_image_dashboard);
        helpSeekerLayout = (LinearLayout) findViewById(R.id.help_seeker_body);
        profileLayout = (LinearLayout) findViewById(R.id.profile_llt);
        usernameTxt = (TextView) findViewById(R.id.username_txt);



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

package amhacks.gratitude.Activity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.sql.Time;
import java.util.HashMap;

import amhacks.gratitude.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class Dashboard extends AppCompatActivity {
    private RelativeLayout helpSeekerSwitch, helperSwitch;
    private LinearLayout helperLayout, helpSeekerLayout, profileLayout;
    private LinearLayout billsFormLayout;
    private LinearLayout billsLayout;
    private TextView helpSeekerTxt, helperTxt, usernameTxt, billsTimeTxt;
    private String user_type, currentUserID, fullname, dest_time, address;
    private FirebaseAuth mAuth;
    private CircleImageView profileView;
    private FirebaseFirestore firestore;
    private TimePicker billsTimePicker;
    private Button postBills;
    private EditText DescET;

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
                    address = value.get("address").toString();
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
        billsTimePicker = (TimePicker) findViewById(R.id.time_picker_bills);
        billsTimeTxt = (TextView) findViewById(R.id.time_txt_bills);
        postBills = (Button) findViewById(R.id.post_request_bills);
        DescET = (EditText) findViewById(R.id.desc_bills);

        billsTimeTxt.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                billsTimePicker.setVisibility(View.VISIBLE);
            }
        });


        billsLayout = (LinearLayout)findViewById(R.id.bills_llt);

        billsFormLayout = (LinearLayout) findViewById(R.id.bills_form_llt);

        billsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                billsFormLayout.setVisibility(View.VISIBLE);
            }
        });

        postBills.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                String desc = DescET.getText().toString();
                dest_time = billsTimePicker.getHour() + ":" + billsTimePicker.getMinute();

                if (TextUtils.isEmpty(desc) || TextUtils.isEmpty(dest_time))
                {
                    Toast.makeText(Dashboard.this, "Please fill all the details", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("time",dest_time);
                    hashMap.put("desc",desc);
                    hashMap.put("status", "pending");
                    hashMap.put("poster", currentUserID);
                    hashMap.put("type","BILL");
                    hashMap.put("poster_location",address);

                    firestore.collection("Posts").document().set(hashMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @RequiresApi(api = Build.VERSION_CODES.P)
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(Dashboard.this, "Posted successfully", Toast.LENGTH_SHORT).show();
                                        billsTimePicker.resetPivot();
                                        DescET.setText(null);
                                        billsFormLayout.setVisibility(View.GONE);
                                    }
                                    else
                                    {
                                        String err = task.getException().getMessage();
                                        Toast.makeText(Dashboard.this, err, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }





            }
        });



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

package amhacks.gratitude.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.vision.text.Text;

import amhacks.gratitude.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class RequestsViewHolder extends RecyclerView.ViewHolder {

    View view;
    public RequestsViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
    }


    public void setDesc(String desc) {
        TextView descText = view.findViewById(R.id.requests_desc);
        descText.setText(desc);
    }


    public void setPoster_location(String poster_location) {
        TextView descText = view.findViewById(R.id.requests_location);
        descText.setText(poster_location);
    }


    public void setType(String type) {
        TextView descText = view.findViewById(R.id.requests_category);
        descText.setText(type);
    }

    public void setTime(String time) {
        TextView descText = view.findViewById(R.id.requests_time);
        descText.setText(time);
    }

    public void setImage(String url) {
        CircleImageView profileImage = view.findViewById(R.id.requests_profile_picture);
        Glide.with(view).load(url).into(profileImage);
    }

    public void setName(String name) {
        TextView fullnameTxt = view.findViewById(R.id.requests_username);
        fullnameTxt.setText(name);
    }


}

package com.example.trackit.Notification;

import static android.view.View.GONE;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.trackit.R;
import com.example.trackit.User.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * This activity displays notifications to the user when another user requests to follow them.
 * Users can then decide to deny or accept the other user's follow request.
 */
public class NotificationsActivity extends AppCompatActivity {

    ListView notificationList;
    ArrayAdapter<User> notificationAdapter;
    ArrayList<User> notificationDataList;
    User user;
    User selectedUser;
    User currentUser;
    LinearLayout notificationMenu;
    boolean[] isClicked = {false};
    final String TAG = "Sample";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference collectionReference;
    TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        notificationList = findViewById(R.id.notification_list);
        message = findViewById(R.id.no_notification_message);
        user = (User) getIntent().getSerializableExtra("User");
        collectionReference = db.collection("Users").document(user.getUsername()).collection("Followers");

        notificationDataList = new ArrayList<>();
        notificationAdapter = new NotificationCustomList(this, notificationDataList);
        notificationList.setAdapter(notificationAdapter);

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                notificationDataList.clear();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    String followerUsername = doc.getId();
                    String followerPassword = "";
                    String followStatus = (String) doc.getData().get("Value");

                    User followerUser = new User(followerUsername, followerPassword);

                    if (followStatus.equals("false")) {
                        notificationDataList.add(followerUser);
                    }
                }
                notificationAdapter.notifyDataSetChanged();
            }
        });


        // Displays/hides menu to accept/deny follow request
        notificationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
                notificationMenu = view.findViewById(R.id.notification_menu);

                if (!isClicked[0]) {
                    notificationMenu.setVisibility(View.VISIBLE);
                    isClicked[0] = true;
                } else {
                    notificationMenu.setVisibility(GONE);
                    isClicked[0] = false;
                }
                notificationList.setSelection(position);
                selectedUser = (User) notificationList.getItemAtPosition(position);
            }
        });

        // Displays message if no new follow requests are present
        if (notificationDataList.size() == 0){
            message.setVisibility(View.VISIBLE);
        } else{
            message.setVisibility(GONE);
        }
    }

    public void acceptRequest(View view) {
        collectionReference.document(selectedUser.getUsername()).update("Value", "true");
        db.collection("Users").document(selectedUser.getUsername())
                .collection("Following").document(user.getUsername())
                .update("Value", "true");
        notificationMenu.setVisibility(GONE);
        isClicked[0] = false;
    }

    public void denyRequest(View view) {
        collectionReference.document(selectedUser.getUsername()).delete();
        db.collection("Users").document(selectedUser.getUsername())
                .collection("Following").document(user.getUsername())
                .delete();
        notificationMenu.setVisibility(GONE);
        isClicked[0] = false;
    }
}
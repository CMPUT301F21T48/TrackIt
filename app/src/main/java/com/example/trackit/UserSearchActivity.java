package com.example.trackit;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


/**
 * This activity will search for other users by username.
 * A user can send a follow request to any of the users
 */

public class UserSearchActivity extends AppCompatActivity {

    List<String> userNames;
    RecyclerView recyclerView;
    UserSearchAdapter usersearchAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<String> userNamesComplete;
    SearchView searchView;
    User currentUser;
    String currentUserName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        currentUser = (User) getIntent().getSerializableExtra("currentUser");
        currentUserName = currentUser.getUsername();
        userNames = new ArrayList<>();
        recyclerView = findViewById(R.id.userDisplay);
        usersearchAdapter = new UserSearchAdapter(userNames);
        userNamesComplete = new ArrayList<>();

        //we get a list of all the usernames to populate the recyclerview

        db.collection("Users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for(QueryDocumentSnapshot doc: queryDocumentSnapshots){
                    userNames.add(doc.getId().toString());
                    userNamesComplete.add(doc.getId().toString());
                    Log.d(TAG, "onSuccess: yee");
                }
                usersearchAdapter.notifyDataSetChanged();
            }
        });

//       searchView= (android.widget.SearchView) findViewById(R.id.searchView);
        TextView sQuery = findViewById(R.id.squery);

        Button queryClearer = findViewById(R.id.squerybutton);
        //queryCleaner takes the query from the text in squery and then performs a simple filter in order to get only those usernames that match the query
        queryClearer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    String searchQuery = sQuery.getText().toString();
                    List<String> checkedList = new ArrayList<>();
                    if(searchQuery==""){
                        checkedList.addAll(userNamesComplete);
                    }
                    else {
                        for (String check : userNamesComplete) {
                            if (check.toLowerCase().contains(searchQuery.toLowerCase())) {
                                checkedList.add(check);
                            }
                        }
                    }
                    userNames.clear();
                    userNames.addAll(checkedList);
                    usersearchAdapter.notifyDataSetChanged();

                }
        });

        Button clearButton = findViewById(R.id.clearbutton);

        //clearButton clears the text in squery and sets the recyclerview to display all the initial usernames again
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sQuery.setText("");
                userNames.clear();
                userNames.addAll(userNamesComplete);
                usersearchAdapter.notifyDataSetChanged();
            }
        });

        recyclerView.setAdapter(usersearchAdapter);

//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//
//                return false;
//            }
//
//            @SuppressLint("NotifyDataSetChanged")
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                usersearchAdapter.getFilter().filter(newText);
//                usersearchAdapter.notifyDataSetChanged();
//                return false;
//            }
//        });


        // setting a click listener to get the username that has been selected
        usersearchAdapter.setOnEntryClickListener(new UserSearchAdapter.OnEntryClickListener() {
            @Override
            public void onEntryClick(View view, int position){
                Intent intent = new Intent(UserSearchActivity.this, UserProfileActivity.class);
                intent.putExtra("currentUser", currentUserName);
                intent.putExtra("chosenUser", userNames.get(position));
                startActivity(intent);
            }
        });


    }
}

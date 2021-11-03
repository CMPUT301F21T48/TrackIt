package com.example.trackit;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class userSearchClass extends AppCompatActivity {

    List<String> userNames;
    RecyclerView recyclerView;
    userSearchAdapter usersearchAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<String> userNamesComplete;
    SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_search);

        userNames = new ArrayList<>();
        recyclerView = findViewById(R.id.userDisplay);
        usersearchAdapter = new userSearchAdapter(userNames);
        userNamesComplete = new ArrayList<>();
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


        usersearchAdapter.setOnEntryClickListener(new userSearchAdapter.OnEntryClickListener() {
            @Override
            public void onEntryClick(View view, int position){
                Intent intent = new Intent(getApplicationContext(), userProfileActivity.class);
                intent.putExtra("currentUser", "amir");
                intent.putExtra("chosenUser", userNames.get(position));
                startActivity(intent);
            }
        });


    }
}

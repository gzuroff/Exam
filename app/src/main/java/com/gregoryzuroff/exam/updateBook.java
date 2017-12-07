package com.gregoryzuroff.exam;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import  android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import static android.content.ContentValues.TAG;

public class updateBook extends Activity implements Button.OnClickListener {

    private EditText editTextTitle, editTextAuthor, editTextCondition, editTextBorrower, editTextSearch;
    private Button buttonUpdate, buttonDelete, buttonSearch;
    private FirebaseAuth mAuth;
    private ListView mainListView;
    final private ArrayList<String> titles = new ArrayList<>();
    final private ArrayList<String> authors = new ArrayList<>();
    final private ArrayList<String> conditions = new ArrayList<>();
    final private ArrayList<String> borrowers = new ArrayList<>();
    final private ArrayList<String> uids = new ArrayList<>();
    private ArrayAdapter adapter;
    private String clickedBookTitle;
    private String getClickedBookAuthor;
    private Boolean search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_book);

        mAuth = FirebaseAuth.getInstance();

        //editTextTitle = findViewById(R.id.editTextTitleUpdate);
        //editTextAuthor = findViewById(R.id.editTextAuthorUpdate);
        editTextCondition = findViewById(R.id.editTextConditionUpdate);
        editTextBorrower = findViewById(R.id.editTextBorrowerUpdate);
        editTextSearch = findViewById(R.id.editTextSearch);

        mainListView = (ListView) findViewById( R.id.mainListView );

        buttonUpdate = findViewById(R.id.buttonUpdate);
        buttonDelete = findViewById(R.id.buttonDelete);
        buttonSearch = findViewById(R.id.buttonSearch);

        buttonUpdate.setOnClickListener(this);
        buttonDelete.setOnClickListener(this);
        buttonSearch.setOnClickListener(this);
        search = false;

        fetchData();

    }

    @Override
    public void onClick(View view){
        if(view == buttonUpdate){
            String uid = new String();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                // The user's ID, unique to the Firebase project. Do NOT use this value to
                // authenticate with your backend server, if you have one. Use
                // FirebaseUser.getToken() instead.
                uid = user.getUid();
            }
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference myRef = database.getReference(clickedBookTitle + getClickedBookAuthor + uid);
            // Read from the database
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    Book value = dataSnapshot.getValue(Book.class);
                    value.setCondition(editTextCondition.getText().toString());
                    value.setBorrowed(editTextBorrower.getText().toString());
                    myRef.setValue(value);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
            fetchData();
        }
        else if(view == buttonDelete){
            String uid = new String();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                // The user's ID, unique to the Firebase project. Do NOT use this value to
                // authenticate with your backend server, if you have one. Use
                // FirebaseUser.getToken() instead.
                uid = user.getUid();
            }
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference myRef = database.getReference();
            myRef.child(clickedBookTitle + getClickedBookAuthor + uid).removeValue();
            fetchData();
        }
        else if(view == buttonSearch){
            search = true;
            fetchData();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(
                R.menu.main, menu
        );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.updateBook:
                startActivity(new Intent(getApplicationContext(), updateBook.class));
                return true;
            case R.id.addBook:
                startActivity(new Intent(getApplicationContext(), addBook.class));
                return true;
            case R.id.logOut:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                return true;
            case R.id.searchFriend:
                startActivity(new Intent(getApplicationContext(), FindFriendBook.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void fetchData(){
        titles.clear();
        authors.clear();
        conditions.clear();
        borrowers.clear();
        uids.clear();
        mainListView.setAdapter(null);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();
        // Read from the database
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String uid = new String();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    // The user's ID, unique to the Firebase project. Do NOT use this value to
                    // authenticate with your backend server, if you have one. Use
                    // FirebaseUser.getToken() instead.
                    uid = user.getUid();
                }
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Book book = postSnapshot.getValue(Book.class);
                    if(book.ownedBy.equalsIgnoreCase(uid)) {
                        titles.add(book.title);
                        authors.add(book.author);
                        conditions.add(book.condition);
                        borrowers.add(book.borrowed);
                        uids.add(book.ownedBy);
                    }
                }
                if(!search) {
                    adapter = new ArrayAdapter(updateBook.this, R.layout.row, R.id.rowTitle, titles) {
                        public View getView(int position, View convertView, ViewGroup parent) {

                            View view = super.getView(position, convertView, parent);
                            TextView text1 = (TextView) view.findViewById(R.id.rowAuthor);
                            TextView text2 = (TextView) view.findViewById(R.id.rowTitle);
                            TextView text3 = view.findViewById(R.id.rowCondition);
                            TextView text4 = view.findViewById(R.id.rowBorrower);

                            text1.setText("Author: " + authors.get(position));
                            text2.setText("Title: " + titles.get(position));
                            text3.setText("Condition: " + conditions.get(position));
                            text4.setText("Borrower: " + borrowers.get(position));

                            return view;
                        }
                    };
                }
                else{
                    final ArrayList<String> newTitles = new ArrayList<>();
                    final ArrayList<String> newAuthors = new ArrayList<>();
                    final ArrayList<String> newConditions = new ArrayList<>();
                    final ArrayList<String> newBorrowers = new ArrayList<>();
                    String searchTitle = editTextSearch.getText().toString();
                    for (int i = 0; i < titles.size(); i++){
                        if(titles.get(i).equalsIgnoreCase(searchTitle)){
                            newTitles.add(titles.get(i));
                            newAuthors.add(authors.get(i));
                            newConditions.add(conditions.get(i));
                            newBorrowers.add(borrowers.get(i));
                        }
                    }
                    if(newTitles.size() > 0) {
                        adapter = new ArrayAdapter(updateBook.this, R.layout.row, R.id.rowTitle, newTitles) {
                            public View getView(int position, View convertView, ViewGroup parent) {

                                View view = super.getView(position, convertView, parent);
                                TextView text1 = (TextView) view.findViewById(R.id.rowAuthor);
                                TextView text2 = (TextView) view.findViewById(R.id.rowTitle);
                                TextView text3 = view.findViewById(R.id.rowCondition);
                                TextView text4 = view.findViewById(R.id.rowBorrower);

                                text1.setText("Author: " + newAuthors.get(position));
                                text2.setText("Title: " + newTitles.get(position));
                                text3.setText("Condition: " + newConditions.get(position));
                                text4.setText("Borrower: " + newBorrowers.get(position));

                                return view;
                            }
                        };
                    }
                    else{
                        adapter = null;
                    }
                }
                mainListView.setAdapter(adapter);
                mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
                        clickedBookTitle = titles.get(i);
                        getClickedBookAuthor = authors.get(i);
                        Toast.makeText(updateBook.this, clickedBookTitle + " selected", Toast.LENGTH_SHORT).show();
                    }});
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

}



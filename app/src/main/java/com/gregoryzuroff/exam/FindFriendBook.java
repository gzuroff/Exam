package com.gregoryzuroff.exam;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FindFriendBook extends Activity implements Button.OnClickListener {

    private EditText editTextFriendName;
    private TextView textViewFriendBook;
    private Button buttonSearchFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friend_book);

        editTextFriendName = findViewById(R.id.editTextFriendName);
        textViewFriendBook = findViewById(R.id.textViewFriendBook);
        buttonSearchFriend = findViewById(R.id.buttonSearchFriend);

        buttonSearchFriend.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        final String friendName = editTextFriendName.getText().toString();
        String bookName;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();
        // Read from the database
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Book book = postSnapshot.getValue(Book.class);
                    if(book.borrowed.equalsIgnoreCase(friendName)){
                        textViewFriendBook.setText(friendName + " borrowed: " + book.title);
                        return;
                    }
                }
                textViewFriendBook.setText(friendName + " has not borrowed a book");
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
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
}

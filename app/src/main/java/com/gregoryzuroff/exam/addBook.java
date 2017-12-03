package com.gregoryzuroff.exam;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;

public class addBook extends Activity implements Button.OnClickListener {

    private EditText editTextTitle, editTextAuthor, editTextCondition, editTextBorrower;
    private Button buttonAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextAuthor = findViewById(R.id.editTextAuthor);
        editTextCondition = findViewById(R.id.editTextCondition);
        editTextBorrower = findViewById(R.id.editTextBorrower);

        buttonAdd = findViewById(R.id.buttonAdd);

        buttonAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        if(view == buttonAdd){
            Book temp = new Book(editTextTitle.getText().toString(), editTextAuthor.getText().toString(),
                    editTextCondition.getText().toString(), editTextBorrower.getText().toString());
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            String title = editTextTitle.getText().toString();
            String author = editTextAuthor.getText().toString();
            DatabaseReference myRef = database.getReference(title + author);

            myRef.setValue(temp);
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

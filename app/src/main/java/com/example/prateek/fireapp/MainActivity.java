package com.example.prateek.fireapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prateek.fireapp.model.Items_Model;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText et_value;
    Button bDeposit, bTransact, bSignOut;
    RecyclerView rv_data;
    TextView row_name, row_value, row_time;

    FirebaseAuth mAuth;
    FirebaseUser user;
    private static DatabaseReference mDatabase;
    FirebaseRecyclerAdapter<Items_Model, itemViewHolder> adapter;

    Calendar calendar = Calendar.getInstance();
    String date = String.valueOf(calendar.get(Calendar.DATE)) + "-" + String.valueOf(calendar.get(Calendar.MONTH)) + "-" + String.valueOf(calendar.get(Calendar.YEAR));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configViews();

        rv_data.setHasFixedSize(true);
        rv_data.setLayoutManager(new LinearLayoutManager(this));

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference mRef = mDatabase.child("Users").child(user.getUid());
        final DatabaseReference mRefD = mRef.child(date);


        adapter = new FirebaseRecyclerAdapter<Items_Model, itemViewHolder>(Items_Model.class, R.layout.item_row, itemViewHolder.class, mRef) {
            @Override
            protected void populateViewHolder(final itemViewHolder viewHolder, Items_Model model, int position) {
                mRefD.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                            Items_Model im = eventSnapshot.getValue(Items_Model.class);

                            if(im.getType().equals("Deposit")) {
                                viewHolder.row_name.setText(im.getName());
                                viewHolder.row_value.setText(im.getValue());
                                viewHolder.row_time.setText(im.getTime());

                                viewHolder.row_value.setTextColor(Color.GREEN);
                            }

                            if(im.getType().equals("Transact")) {
                                viewHolder.row_name.setText(im.getName());
                                viewHolder.row_value.setText(im.getValue());
                                viewHolder.row_time.setText(im.getTime());

                                viewHolder.row_value.setTextColor(Color.RED);
                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(MainActivity.this, "IDK some error occured", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };

        rv_data.setAdapter(adapter);


        bDeposit.setOnClickListener(this);
        bTransact.setOnClickListener(this);
        bSignOut.setOnClickListener(this);
    }

    private void configViews() {
        et_value = (EditText) findViewById(R.id.et_value);
        bDeposit = (Button) findViewById(R.id.bDeposit);
        bTransact = (Button) findViewById(R.id.bTransact);
        bSignOut = (Button) findViewById(R.id.bSignOut);
        rv_data = (RecyclerView) findViewById(R.id.rv_data);
        row_name = (TextView) findViewById(R.id.row_name);
        row_value = (TextView) findViewById(R.id.row_value);
        row_time = (TextView) findViewById(R.id.row_time);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance().getReference();
        }
    }

    @Override
    public void onClick(View v) {

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        switch (v.getId()) {

            case R.id.bDeposit:
                mDatabase.child("Users").child(user.getUid()).child(date).push().setValue(new Items_Model("Gift", "200", "5:30 pm", "Deposit"));
                break;

            case R.id.bTransact:
                mDatabase.child("Users").child(user.getUid()).child(date).push().setValue(new Items_Model("Shoes", "150", "6:00 pm", "Transact"));
                break;

            case R.id.bSignOut:
                mAuth.getInstance().signOut();
                Toast.makeText(MainActivity.this, "Signing out", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this, Login.class);
                startActivity(i);
                finish();
        }
    }

    public static class itemViewHolder extends RecyclerView.ViewHolder {
        TextView row_name, row_value, row_time;

        public itemViewHolder(View itemView) {
            super(itemView);
            row_name = (TextView) itemView.findViewById(R.id.row_name);
            row_value = (TextView) itemView.findViewById(R.id.row_value);
            row_time = (TextView) itemView.findViewById(R.id.row_time);
        }
    }

}

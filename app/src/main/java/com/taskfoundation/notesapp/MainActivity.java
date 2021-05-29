package com.taskfoundation.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    static ArrayList<String> notes = new ArrayList<>();

    static ArrayAdapter adapter;

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getApplicationContext().getSharedPreferences("com.taskfoundation.notesapp", Context.MODE_PRIVATE);

        HashSet<String> set = (HashSet<String>) preferences.getStringSet("notes", null);

        if (set == null) {
            notes.add("Example Note");
        } else {
            notes = new ArrayList(set);
        }

        ListView listView = findViewById(R.id.listView);


        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, notes);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getApplicationContext(), NoteEditorActivity.class);
            intent.putExtra("noteId", notes.get(position));
            startActivity(intent);
        });

        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            new AlertDialog.Builder(MainActivity.this)
                    .setIcon(android.R.drawable.ic_delete)
                    .setTitle("Delete Note")
                    .setMessage("do you want to do this?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        notes.remove(position);
                        adapter.notifyDataSetChanged();

                        HashSet<String> newSet = new HashSet<>(notes);
                        preferences.edit().putStringSet("notes", newSet).apply();

                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "It's done", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("No", null)
                    .show();

            return true;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.add_note:
                Intent intent = new Intent(getApplicationContext(), NoteEditorActivity.class);
                startActivity(intent);
                return true;
            default:
                return false;
        }
    }
}
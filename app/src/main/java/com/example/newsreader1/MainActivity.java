package com.example.newsreader1;
//
// Cody Semler
// Ron Scott
//
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private EditText nameEditText;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameEditText = findViewById(R.id.editText);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Load saved data
        String savedName = preferences.getString("name", "");
        if (savedName.equals(null) || savedName.equals (""))
        {
        //nothing, leave as default text
        }
        else
        {
            nameEditText.setText(savedName);
        }
    }

    public void onButtonClick(View view) {
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
    }

    public void onSaveButtonClick(View view) {
               String name = nameEditText.getText().toString();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("name", name);
        editor.apply();


        String message = (getString(R.string.hello)+" "+ name + "! " + getString(R.string.snacky));
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }
}

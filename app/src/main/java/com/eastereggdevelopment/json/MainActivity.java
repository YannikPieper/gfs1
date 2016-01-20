package com.eastereggdevelopment.json;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class MainActivity extends Activity {

    //String in dem der Username gespeichert wird
    String Username = "unknown User";

    //SharedPreferences Variabeln
    public static String filename = "SharedPreferencesFile";
    SharedPreferences MyUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void saveName(View view){

        //Deklariert den EditText und weist ihn dem String Username zu
        EditText username = (EditText) findViewById(R.id.Username);
        Username = username.getText().toString();

        //Das SharedPreferences File wird geladen und der String Username darin gespeichert
        MyUsername = getSharedPreferences(filename, 0);

        //Editor wird geladen
        SharedPreferences.Editor editor = MyUsername.edit();

        //Erstes Wort bezeichnet den Namen des Strings im SharedPreferences File und das zweite weist dem String den Inhalt zu .commit bestätigt das ganze
        editor.putString("username", Username);
        editor.commit();

        //Startet neue Klasse
        Intent intent = new Intent(this, Chat.class);
        startActivity(intent);
    }

    @Override
     public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

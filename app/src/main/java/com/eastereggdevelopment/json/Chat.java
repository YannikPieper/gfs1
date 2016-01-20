package com.eastereggdevelopment.json;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;


public class Chat extends Activity {

    //String Variable die an den Server gesendet wird
    String Message;
    String MU;

    //String wird von MainActivity geladen
    String username;

    //Integer für die Zeit
    int time = 1000;

    //String Variabeln für die Informationen vom Server
    String message = "unknown message";
    String Uname = "unknown username";

    int idnew;
    int idold;

    Boolean post = false;

    //SharedPreferences Variabeln
    public static String filename = "SharedPreferencesFile";
    SharedPreferences MyUsername;

    // URL to get contacts JSON
    private static String url = "http://kgt-chat.herokuapp.com/send";
    private static String urlPost = "http://kgt-chat.herokuapp.com/send";
    private static String urlGet = "http://kgt-chat.herokuapp.com/chat/last";

    // JSON Node names
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_NAME = "name";
    private static final String TAG_CHAT = "chat";
    private static final String TAG_ID = "id";

    // contacts JSONArray
    JSONArray contacts = null;

    public ListView messages;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);

        loadDataOffline();
        loadDataOnline();
    }

    public void loadDataOffline() {
        //Das SharedPreferences File wird geladen und der String Username darin gespeichert
        MyUsername = getSharedPreferences(filename, 0);

        //Methode um den Username wieder aus dem SharedPreferences File auszulesen
        username = MyUsername.getString("username", "unknown User");
        String[] items = {}; //setzt listView Items fest
        arrayList = new ArrayList<>(Arrays.asList(items));
    }

    public void loadDataOnline() {

        MU = Uname + ": " + message;

        messages = (ListView) findViewById(R.id.chatHistoryL); //deklariert listView
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.txtitem, arrayList);
        messages.setAdapter(arrayAdapter);

        if (idnew > idold) {
            arrayList.add(MU);
            arrayAdapter.notifyDataSetChanged();

            idold = idnew;
        }

        System.out.println(Uname + ": " + message);
        RefreshChat startSearch = new RefreshChat();
        startSearch.execute();

        time = 1000;
        Refresh();

    }

    public void Refresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadDataOnline();
            }
        }, time);
    }

    //Diese Methode wird ausgeführt wenn der send Button gedrückt wird
    public void send(View view) {

        //Deklariert das Eingabefeld und speichert den eingegebenen Text im Message String
        EditText message = (EditText) findViewById(R.id.message);
        Message = message.getText().toString();
        message.setText("");

        post = true;
        RefreshChat startSearch = new RefreshChat();
        startSearch.execute();

    }


    private class RefreshChat extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            if (post == true) {
                url = urlPost;
                JSONObject jsonObj = new JSONObject();
                try {
                    jsonObj.put("name", username);
                    jsonObj.put("message", Message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String params = jsonObj.toString();
                System.out.println(username + ": " + Message);

                sh.makeServiceCall(url, ServiceHandler.POST, params);
                post = false;

            } else {
                url = urlGet;
                // Making a request to url and getting response
                String jsonStr = sh.makeServiceCall(url, ServiceHandlerGet.GET);

                Log.d("Response: ", "> " + jsonStr);

                if (jsonStr != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(jsonStr);

                        // Getting JSON Array node
                        contacts = jsonObj.getJSONArray(TAG_CHAT);

                        // looping through All Contacts
                        for (int i = 0; i < contacts.length(); i++) {
                            JSONObject c = contacts.getJSONObject(i);

                            message = c.getString(TAG_MESSAGE);
                            Uname = c.getString(TAG_NAME);
                            idnew = c.getInt(TAG_ID);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("ServiceHandler", "Couldn't get any data from the url");
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            System.out.println(Uname + ": " + message);

            System.out.println();

        }

    }
}
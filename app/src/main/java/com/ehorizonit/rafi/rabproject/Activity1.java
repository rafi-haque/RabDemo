package com.ehorizonit.rafi.rabproject;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.sunfusheng.marqueeview.MarqueeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Activity1 extends AppCompatActivity {

    private final String[] dataChannels = new String[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_1);


        WebView myWebView = findViewById(R.id.webView);
        myWebView.setWebViewClient(new WebViewClient());
        myWebView.loadUrl("http://www.google.com");

        final MarqueeView marqueeView = findViewById(R.id.marqueeTextView);

//        JSONObject jsonObj = new JSONObject();
        final List<String> data = new ArrayList<>();



        data.add("NEWS");
        data.add("Listing All the news");

        dataChannels[0] = null;
        dataChannels[1] = null;


        marqueeView.startWithList(data);
        final TextView tv= findViewById(R.id.textView);

//        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/kalpurush.ttf");
//
//        marqueeView.setTypeface(custom_font);
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int index = marqueeView.getPosition();
                if(dataChannels[index] == null){
                    tv.setText(" ");
                }
                else{
                    tv.setText(dataChannels[index].charAt(0) + " Channels");
                }


                handler.postDelayed(this, 3000L);  // 1 second delay
            }
        };
        handler.postDelayed(runnable, 3000L);





        marqueeView.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, TextView textView) {
                Toast.makeText(getApplicationContext(), textView.getText(), Toast.LENGTH_SHORT).show();
            }
        });


        //NOW REQUESTING
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://192.168.1.109:8888/rab_project_v2/videoclipclient/scrollinfoapi.php?video_date=06.11.2018";
        // Request a string response from the provided URL.
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try{
                            for(int i=0; i<response.length(); i++ ){
                                JSONObject tempData = response.getJSONObject(i);
                                data.add(tempData.getString("scroll_text"));
                                dataChannels[i+2] = String.valueOf(tempData.getJSONArray("channels").length()) + " Channels: ";
                                for(int j=0; j<tempData.getJSONArray("channels").length(); j++){
                                    dataChannels[i+2] += tempData.getJSONArray("channels").getString(j) + " ";
                                }


                            }

                            //Log.d("Bal", "onResponse: " + dataChannels[1].toString());

                        }catch (JSONException ex){
                            Log.d("Error", "onResponse: " + ex);
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });

// Add the request to the RequestQueue.
        queue.add(jsonArrayRequest);

    }

    public void onClick(View v) {
        TextView tv= findViewById(R.id.textView);
        MarqueeView marqueeView = findViewById(R.id.marqueeTextView);

        int index = marqueeView.getPosition();
        Log.d("Error", "onClickError: " + dataChannels[index]);
        if(dataChannels[index] == null){
            tv.setText(" ");
        }
        else{
            tv.setText(dataChannels[index]);
            Log.d("Error", "onClickError: " + tv.getText());;
        }


    }
}
package com.demo.yeldi.threadjsonlist;

import android.app.Dialog;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.demo.yeldi.threadjsonlist.adapter.DeviceListAdapter;
import com.demo.yeldi.threadjsonlist.data.DeviceItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

        private ProgressDialog progressDialog;
        private String resultText = null;
        private String DevicType,Model,Name;
        Dialog dialog;
        ListView mListdevice;
        public ArrayList<DeviceItem> deviceTypeItem;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            Button textBtn = (Button)findViewById(R.id.Button02);

            deviceTypeItem = new ArrayList<DeviceItem>();

            textBtn.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    getDeviceList("https://s3.amazonaws.com/harmony-recruit/devices.json");

                }
            });


        }



    private void getDeviceList(String urlStr) {
        progressDialog = ProgressDialog.show(this, "",
                "Device Details from " + urlStr);
        final String url = urlStr;
        new Thread () {
            public void run() {
                int BUFFER_SIZE = 2000;
                InputStream in = null;
                Message msg = Message.obtain();
                msg.what=2;
                try {
                    in = openHttpConnection(url);

                    InputStreamReader isr = new InputStreamReader(in);
                    int charRead;
                    resultText = "";
                    char[] inputBuffer = new char[BUFFER_SIZE];

                    while ((charRead = isr.read(inputBuffer))>0)
                    {
                        String readString =
                                String.copyValueOf(inputBuffer, 0, charRead);
                        resultText += readString;
                        inputBuffer = new char[BUFFER_SIZE];
                    }
                    Bundle b = new Bundle();
                    b.putString("resultText", resultText);
                    msg.setData(b);
                    in.close();

                }catch (IOException e2) {
                    e2.printStackTrace();
                }
                messageHandler.sendMessage(msg);
            }
        }.start();
    }

    private InputStream openHttpConnection(String urlStr) {
        InputStream in = null;
        int resCode = -1;

        try {
            URL url = new URL(urlStr);
            URLConnection urlConn = url.openConnection();
            if (!(urlConn instanceof HttpURLConnection)) {
                throw new IOException ("URL is not an Http URL");
            }

            HttpURLConnection httpConn = (HttpURLConnection)urlConn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();

            resCode = httpConn.getResponseCode();
            if (resCode == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return in;
    }

    private Handler messageHandler = new Handler() {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            dialog = new Dialog(MainActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.device_list);
            try {
                JSONObject jsonRootObject = new JSONObject(msg.getData().getString("resultText"));

                //Get the instance of JSONArray that contains JSONObjects
                JSONArray jsonArray = jsonRootObject.optJSONArray("devices");

                //Iterate the jsonArray and print the info of JSONObjects
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    DeviceItem deviceDetails = new DeviceItem();
                    deviceDetails.setDevicType(jsonObject.getString("deviceType"));
                    deviceDetails.setModel(jsonObject.getString("model"));
                    deviceDetails.setName(jsonObject.getString("name"));
                    // adding device details array
                    deviceTypeItem.add(deviceDetails);

                }
                mListdevice = (ListView) dialog.findViewById(R.id.list_device);
                DeviceListAdapter stateListAdapter = new DeviceListAdapter(getApplicationContext(), deviceTypeItem);
                mListdevice.setAdapter(stateListAdapter);

                mListdevice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        DeviceItem deviceInformation = (DeviceItem) parent.getItemAtPosition(position);
                        DevicType = deviceInformation.getDevicType();
                        Model = deviceInformation.getModel();
                        Name = deviceInformation.getName();
                        Toast.makeText(getApplicationContext(), "Device Details"+DevicType + "Model"+Model +"Name"+Name , Toast.LENGTH_SHORT).show();
                        //dialog.dismiss();
                    }
                });
                dialog.show();

            } catch (JSONException e) {
                e.printStackTrace();
            }

            progressDialog.dismiss();
        }
    };
}
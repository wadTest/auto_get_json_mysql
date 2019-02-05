package com.prospec.getspinner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AutoCompleteTextView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        // spinner1
        //final Spinner spin = (Spinner)findViewById(R.id.spinner1);
        final AutoCompleteTextView autoAddress = (AutoCompleteTextView) findViewById(R.id.address);

        /** JSON return
         *  [{"MemberID":"1","Name":"Weerachai","Tel":"0819876107"},
         * {"MemberID":"2","Name":"Win","Tel":"021978032"},
         * {"MemberID":"3","Name":"Eak","Tel":"0876543210"}]
         */

        String url = "http://119.59.103.121/app_mobile/get%20spinner.php";

        try {

            JSONArray data = new JSONArray(getJSONUrl(url));

            final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> map;

            for(int i = 0; i < data.length(); i++){
                JSONObject c = data.getJSONObject(i);

                map = new HashMap<String, String>();
                map.put("tambon_th", c.getString("tambon_th") + "\n");
                map.put("amphur_th", c.getString("amphur_th") + "\n");
                map.put("province_th", c.getString("province_th") + "\n");
                map.put("sdist_code", c.getString("sdist_code"));
                MyArrList.add(map);
            }

            SimpleAdapter sAdap;
            sAdap = new SimpleAdapter(MainActivity.this, MyArrList, R.layout.activity_column,
                    new String[] {"tambon_th", "amphur_th", "province_th", "sdist_code"},
                    new int[] {R.id.ColMemberID, R.id.ColName, R.id.ColTel, R.id.Code});
            autoAddress.setAdapter(sAdap);

            final AlertDialog.Builder viewDetail = new AlertDialog.Builder(this);

//            autoAddress.setOnItemSelectedListener(new OnItemSelectedListener() {

//                public void onItemSelected(AdapterView<?> arg0, View selectedItemView,
//                                           int position, long id) {
//
//                    String tambon = MyArrList.get(position).get("tambon_th").toString();
//                    String umper = MyArrList.get(position).get("amphur_th").toString();
//                    String province = MyArrList.get(position).get("province_th").toString();
//                    String code = MyArrList.get(position).get("sdist_code").toString();
//
//                    //String sMemberID = ((TextView) myView.findViewById(R.id.ColMemberID)).getText().toString();
//                    // String sName = ((TextView) myView.findViewById(R.id.ColName)).getText().toString();
//                    // String sTel = ((TextView) myView.findViewById(R.id.ColTel)).getText().toString();
//
//                    viewDetail.setIcon(android.R.drawable.btn_star_big_on);
//                    viewDetail.setTitle("รายละเอียดที่อยู่");
//                    viewDetail.setMessage("ตำบล : " + tambon + "\n"
//                            + "อำเภอ : " + umper + "\n"
//                            + "จังหวัด : " + province +"\n"
//                            + "รหัสไปรษณีย์ : " + code);
//                    viewDetail.setPositiveButton("ตกลง",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog,
//                                                    int which) {
//                                    // TODO Auto-generated method stub
//                                    dialog.dismiss();
//                                }
//                            });
//                    viewDetail.show();
//
//                }
//
//                public void onNothingSelected(AdapterView<?> arg0) {
//                    // TODO Auto-generated method stub
//                    Toast.makeText(MainActivity.this,
//                            "Your Selected : Nothing",
//                            Toast.LENGTH_SHORT).show();
//                }
//
//
//            });



        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    public String getJSONUrl(String url) {
        StringBuilder str = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) { // Download OK
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    str.append(line);
                }
            } else {
                Log.e("Log", "Failed to download result..");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return str.toString();
    }
}
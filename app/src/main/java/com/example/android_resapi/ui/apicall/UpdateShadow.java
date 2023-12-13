package com.example.android_resapi.ui.apicall;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.example.android_resapi.httpconnection.PutRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class UpdateShadow extends PutRequest {
    final static String TAG = "AndroidAPITest";
    String urlStr;

    public UpdateShadow(Activity activity, String urlStr) {

        super(activity);
        this.urlStr = urlStr;
    }

    @Override
    protected void onPreExecute() {
        try {
            Log.e(TAG, urlStr);
            url = new URL(urlStr);

        } catch (MalformedURLException e) {
            e.printStackTrace();
            Toast.makeText(activity,"URL is invalid:"+urlStr, Toast.LENGTH_SHORT).show();
            activity.finish();

        }
    }
    @Override
   protected void onPostExecute(String result) {
        Toast.makeText(activity,result, Toast.LENGTH_SHORT).show();
    }

    protected Map<String, String> getStateFromJSONString(String jsonString) {
        Map<String, String> output = new HashMap<>();
        try {
            // 처음 double-quote와 마지막 double-quote 제거
            jsonString = jsonString.substring(1,jsonString.length()-1);
            // \\\" 를 \"로 치환
            jsonString = jsonString.replace("\\\"","\"");
            Log.i(TAG, "jsonString="+jsonString);
            JSONObject root = new JSONObject(jsonString);
            JSONObject state = root.getJSONObject("state");
            JSONObject desired = state.getJSONObject("desired");
            String soundValue = desired.getString("sound");

            if (soundValue != null) {
                output.put("sound", soundValue);

                // soundValue가 110 이상인 경우 LED를 1로 설정
                if (Integer.parseInt(soundValue) >= 110) {
                    output.put("LED", "1");
                }
            }

        } catch (JSONException e) {
            Log.e(TAG, "Exception in processing JSONString.", e);
            e.printStackTrace();
        }
        return output;
    }


}

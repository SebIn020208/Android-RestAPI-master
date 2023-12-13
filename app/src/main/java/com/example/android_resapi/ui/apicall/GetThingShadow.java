package com.example.android_resapi.ui.apicall;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android_resapi.R;
import com.example.android_resapi.httpconnection.GetRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class GetThingShadow extends GetRequest {
    final static String TAG = "AndroidAPITest";
    String urlStr;
    public GetThingShadow(Activity activity, String urlStr) {
        super(activity);
        this.urlStr = urlStr;
    }

    @Override
    protected void onPreExecute() {
        try {
            Log.e(TAG, urlStr);
            url = new URL(urlStr);

        } catch (MalformedURLException e) {
            Toast.makeText(activity,"URL is invalid:"+urlStr, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            activity.finish();
        }
    }

    @Override
    protected void onPostExecute(String jsonString) {
        if (jsonString == null)
            return;
        Map<String, String> state = getStateFromJSONString(jsonString);
        TextView reported_ledTV = activity.findViewById(R.id.reported_led);
        TextView reported_tempTV = activity.findViewById(R.id.reported_temp);
        reported_tempTV.setText(state.get("reported_sound"));
        reported_ledTV.setText(state.get("reported_LED"));

        TextView desired_ledTV = activity.findViewById(R.id.desired_led);
        TextView desired_tempTV = activity.findViewById(R.id.desired_temp);
        desired_tempTV.setText(state.get("desired_sound"));
        desired_ledTV.setText(state.get("desired_LED"));

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
            JSONObject reported = state.getJSONObject("reported");
            String soundValue = reported.getString("sound");
            String ledValue = reported.getString("LED");
            output.put("reported_sound", soundValue);
            output.put("reported_LED",ledValue);

            JSONObject desired = state.getJSONObject("desired");
            String desired_soundValue = desired.getString("sound");
            String desired_ledValue = desired.getString("LED");
            output.put("desired_temperature", desired_soundValue);
            output.put("desired_LED",desired_ledValue);

        } catch (JSONException e) {
            Log.e(TAG, "Exception in processing JSONString.", e);
            e.printStackTrace();
        }
        return output;
    }
}

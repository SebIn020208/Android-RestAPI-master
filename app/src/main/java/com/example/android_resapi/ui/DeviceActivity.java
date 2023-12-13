package com.example.android_resapi.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android_resapi.R;
import com.example.android_resapi.ui.apicall.GetThingShadow;
import com.example.android_resapi.ui.apicall.UpdateShadow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class DeviceActivity extends AppCompatActivity {
    String urlStr;

    Map<String, String> state;

    final static String TAG = "AndroidAPITest";
    Timer timer;
    Button startGetBtn;
    Button stopGetBtn;

    Button warring_btn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
        Intent intent = getIntent();
        urlStr = intent.getStringExtra("thingShadowURL");

        warring_btn = findViewById(R.id.warring_btn);
        warring_btn.setEnabled(false);

        startGetBtn = findViewById(R.id.startGetBtn);
        startGetBtn.setEnabled(true);
        startGetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer = new Timer();
                timer.schedule(new TimerTask() {
                                   @Override
                                   public void run() {
                                       new GetThingShadow(DeviceActivity.this, urlStr).execute();
                                   }
                               },
                        0, 2000);

                startGetBtn.setEnabled(false);
                stopGetBtn.setEnabled(true);
            }
        });

        stopGetBtn = findViewById(R.id.stopGetBtn);
        stopGetBtn.setEnabled(false);
        stopGetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (timer != null)
                    timer.cancel();
                clearTextView();
                startGetBtn.setEnabled(true);
                stopGetBtn.setEnabled(false);
            }
        });

        Button updateBtn = findViewById(R.id.updateBtn);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edit_sound = findViewById(R.id.edit_sound);
                EditText edit_led = findViewById(R.id.edit_led);

                JSONObject payload = new JSONObject();

                try {
                    JSONArray jsonArray = new JSONArray();
                    String sound_input = edit_sound.getText().toString();
                    if (!sound_input.equals("")) {
                        JSONObject tag1 = new JSONObject();
                        tag1.put("tagName", "sound");
                        tag1.put("tagValue", sound_input);

                        jsonArray.put(tag1);
                    }

                    String soundValue = state.get("reported_sound");
                    if (soundValue != null && Integer.parseInt(soundValue) >= 110) {
                        warring_btn.setEnabled(true);
                    }

                    String led_input = edit_led.getText().toString();
                    if (!led_input.equals("")) {
                        JSONObject tag2 = new JSONObject();
                        tag2.put("tagName", "LED");
                        tag2.put("tagValue", led_input);

                        jsonArray.put(tag2);
                    }

                    if (jsonArray.length() > 0)
                        payload.put("tags", jsonArray);
                } catch (JSONException e) {
                    Log.e(TAG, "JSONException", e);
                }
                Log.i(TAG, "payload=" + payload);
                if (payload.length() > 0)
                    new UpdateShadow(DeviceActivity.this, urlStr).execute(payload);
                else
                    Toast.makeText(DeviceActivity.this, "변경할 상태 정보 입력이 필요합니다", Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void onPostExecute(String jsonString) {
        if (jsonString == null)
            return;

        state = getStateFromJSONString(jsonString);

        // reported_sound와 reported_LED 값을 가져오기
        String reportedSound = state.get("reported_sound");
        String reportedLED = state.get("reported_LED");

        if (reportedSound != null && Integer.parseInt(reportedSound) >= 110) {
            warring_btn.setEnabled(true);
        }
    }

    private Map<String, String> getStateFromJSONString(String jsonString) {
        Map<String, String> stateMap = new HashMap<>();

        try {
            JSONObject root = new JSONObject(jsonString);
            JSONObject state = root.getJSONObject("state");
            JSONObject reported = state.getJSONObject("reported");

            // 예시: reported_sound와 reported_LED 값을 가져와서 Map에 추가
            String reportedSound = reported.getString("sound");
            String reportedLED = reported.getString("LED");

            stateMap.put("reported_sound", reportedSound);
            stateMap.put("reported_LED", reportedLED);

            // 필요한 다른 값을 가져와서 stateMap에 추가할 수 있습니다.

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return stateMap;
    }


    private void clearTextView() {
        TextView reported_ledTV = findViewById(R.id.reported_led);
        TextView reported_tempTV = findViewById(R.id.reported_temp);
        reported_tempTV.setText("");
        reported_ledTV.setText("");

        TextView desired_ledTV = findViewById(R.id.desired_led);
        TextView desired_tempTV = findViewById(R.id.desired_temp);
        desired_tempTV.setText("");
        desired_ledTV.setText("");
    }


}



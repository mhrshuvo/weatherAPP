package com.mhrshuvo.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static java.lang.String.format;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    TextView mCity , mCountry , mDay,mDes, mTemp,mFeellike, mMin,mMax,mHum;
    ImageView micon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCity =(TextView) findViewById(R.id.cityname);
        mCountry =(TextView) findViewById(R.id.sCountey);
        mDay =(TextView) findViewById(R.id.day);
        mDes =(TextView) findViewById(R.id.description);
        mFeellike =(TextView) findViewById(R.id.feelsliketemp);
        mTemp =(TextView) findViewById(R.id.citytemp);
        mMin =(TextView) findViewById(R.id.min_temp);
        mMax =(TextView) findViewById(R.id.max_temp);
        mHum =(TextView) findViewById(R.id.hum);

        micon =(ImageView) findViewById(R.id.ico) ;


        getWeatherInfo();
    }



   private void getWeatherInfo(){

        String tag_json_obj = "json_obj_req";

        String url = "https://api.openweathermap.org/data/2.5/weather?q=dhaka&appid=6585eeb2d43cb99474d030c57f4b28fb";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.w(TAG, response.toString());

                        try {
                            String cityName = response.getString("name");

                            JSONObject main = response.getJSONObject("main");
                            double cityTemp = main.getDouble("temp");
                            double cityFeelsLike = main.getDouble("feels_like");
                            double cityMinTemp = main.getDouble("temp_min");
                            double cityMaxTemp = main.getDouble("temp_max");
                            int humidity = main.getInt("humidity");

                            JSONObject sys = response.getJSONObject("sys");
                            String country = sys.getString("country");
                            long sunrise = sys.getLong("sunrise");
                            long sunset = sys.getLong("sunset");
                            long timeZon = response.getLong("timezone");
                            JSONArray weather = response.getJSONArray("weather");
                            JSONObject item = weather.getJSONObject(0);
                            String desc = item.getString("description");
                            String ico = item.getString("icon");

                            mCity.setText(cityName.toUpperCase());
                            mDes.setText(desc.toUpperCase());
                            mTemp.setText(""+(cityTemp-273.15)+"° C");
                            mFeellike.setText(""+ format( "%.1f",(cityFeelsLike-273.15))+"° C");
                            mCountry.setText("Country : "+country);
                            mMin.setText(""+ format( "%.1f",(cityMinTemp-273.15))+"° C");
                            mMax.setText(""+ format( "%.1f",(cityMaxTemp-273.15))+"° C");
                            mHum.setText(""+humidity+"%");



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());

            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }
    public static String getDate(long milliSeconds, String dateFormat) {
        SimpleDateFormat formatter =
                new SimpleDateFormat(dateFormat, Locale.getDefault());

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}
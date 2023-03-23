package com.example.asynctask;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    String slno = "WP21042Q20000028";
    String finalResponse,msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }




    public void hit(View v )
    {
        String request_healthstatus =
                "<HealthStatus>\n<SerialNo>" + slno + "</SerialNo>\n" +
                        "<Date_Time>" + updateTimeDate() + "</Date_Time>\n" +
                        "</HealthStatus>";
        new MainActivity.SetSoap1().execute("https://rhms2.visiontek.co.in/api/HealthStatus", request_healthstatus);
    }

    public void hit1(View view) {


        String url = "https://rhms2.visiontek.co.in/api/ApplicationStatus?serialNo=111918161514";

        new makeservicecall().execute(url);
    }

    private class makeservicecall extends AsyncTask<String, Void, Boolean> {


        @SuppressWarnings("rawtypes")
        @Override
        protected Boolean doInBackground(String... params) {
            String reqURL = params[0];
            String response = null;
            try {
                URL url = new URL(reqURL);
                URLConnection urlConnection = url.openConnection();
                HttpURLConnection httpConn = (HttpURLConnection) urlConnection;
                httpConn.setRequestMethod("GET");
                System.out.println("Responce Code===>"+httpConn.getResponseCode());
                System.out.println("Responce Message===>"+httpConn.getResponseMessage());
                try {
                    InputStream in = new BufferedInputStream(httpConn.getInputStream());
                    response = convertStreamToString(in);
                    finalResponse=response;
                    System.out.println("****************************"+finalResponse);

                } catch (Exception e) {

                    msg ="No Response for this Device";
                    e.printStackTrace();
                    System.out.println("========="+e.getLocalizedMessage());
                    return false;
                }

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("==1"+e.getMessage());
                msg = e.getMessage();
                return false;
            }
            return true;
        }


    }


    public String convertStreamToString(InputStream stream) {
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
            msg = e.getMessage();
        }
        return sb.toString();
    }

    public String updateTimeDate() {
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String currentDate, currentTime;
        currentDate = date.format(new Date());
        currentTime = time.format(new Date());
        System.out.println("Time====>" + currentTime);
        return currentDate + "T" + currentTime;
    }





    public static class SetSoap1 extends AsyncTask<String, Void, Boolean> {


        @Override
        protected Boolean doInBackground(String... params) {
            String post_url=params[0];
            String request=params[1];

            try {
                URL url=new URL(post_url);

                HttpURLConnection connection=(HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(20000);
                connection.setReadTimeout(20000);
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setUseCaches(false);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Accept", "application/xml");
                connection.setRequestProperty("Content-Type", "application/xml");
                OutputStream outputStream=connection.getOutputStream();
                byte[] b=request.getBytes("UTF-8");
                outputStream.write(b);
                System.out.println("Connection :" + connection);
                System.out.println("URL  :" + url);
                System.out.println("Request :" + request);
                outputStream.flush();
                outputStream.close();
                System.out.println("REsp Code :" + connection.getResponseCode());
                System.out.println("REsp Code :" + connection.getResponseMessage());


                InputStream inputStream=connection.getInputStream();
                System.out.println();
                byte[] res=new byte[2048];
                int i=0;
                StringBuilder response=new StringBuilder();
                while ((i=inputStream.read(res)) != -1) {
                    response.append(new String(res, 0, i));
                }
                System.out.println("Responce====>" + response);
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }


        public String updateTimeDate() {
            SimpleDateFormat date=new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat time=new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            String currentDate, currentTime;
            currentDate=date.format(new Date());
            currentTime=time.format(new Date());
            System.out.println("Time====>" + currentTime);
            return currentDate + "T" + currentTime;
        }
    }
}

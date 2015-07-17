package com.adsi.hawerd.pruebasimgeplat3;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends Activity {
    int a;
    private TextView id;
    TextView temp;
    TextView flujo, presion;
    private Button mostrar,parar;
    private int posicion=0;
    private List<Planta> datosPlanta;
    private Planta planta;
    private MediaPlayer nuclear;
    private Timer timer;
    TimerTask sub;

    @SuppressLint("InlinedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        setContentView(R.layout.activity_main);

        datosPlanta=new ArrayList<Planta>();
        temp=(TextView)findViewById(R.id.temp);
        flujo=(TextView)findViewById(R.id.flujo);
        mostrar=(Button)findViewById(R.id.button);
        timer =new Timer();
        SubTimer sub = new SubTimer();
        timer.scheduleAtFixedRate(sub, 0,23500);

    }
    class SubTimer extends TimerTask{
        @Override
        public void run(){
                    new Mostrar().execute();
                }
        }

    public void sonarAlarma(){
        nuclear = MediaPlayer.create(MainActivity.this, R.drawable.nuclear);
        nuclear.start();
    }

    public void pausaAlarma(View v){
        nuclear = MediaPlayer.create(MainActivity.this, R.drawable.nuclear);
        nuclear.pause();
    }

    public void paraAlarma(View v){
        nuclear.stop();
    }

    public void stopAutomatico(){
        nuclear.stop();
    }
    private String mostrar(){
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://adsi63.hol.es/adsiconexion/json.php");
        String resultado="";
        HttpResponse response;
        try {
            response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            InputStream instream = entity.getContent();
            resultado= convertStreamToString(instream);
        } catch (ClientProtocolException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
        return resultado;
    }

    private String convertStreamToString(InputStream is) throws IOException {
        if (is != null) {
            StringBuilder sb = new StringBuilder();
            String line;
            try {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is, "UTF-8"));
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            }
            finally {
                is.close();
            }
            return sb.toString();
        } else {
            return "";
        }
    }

    private boolean filtrarDatos(){
        datosPlanta.clear();
        String data=mostrar();
        if(!data.equalsIgnoreCase("")){
            JSONObject json;
            try {
                json = new JSONObject(data);
                JSONArray jsonArray = json.optJSONArray("planta");
                for (int i = 0; i < jsonArray.length(); i++) {
                    planta =new Planta();
                    JSONObject jsonArrayChild = jsonArray.getJSONObject(i);
                    planta.setTemp(jsonArrayChild.optString("temp"));
                    planta.setFlujo(jsonArrayChild.optString("flujo"));
                    datosPlanta.add(planta);
                }
            } catch (JSONException e) {

                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    public void mostrarPersona(final int posicion) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Planta planta = datosPlanta.get(posicion);
                temp.setText(planta.getTemp());
                flujo.setText(planta.getFlujo());
                int a = Integer.parseInt(flujo.getText().toString());
                if(a<=4){
                    return;
                }
                else if (a>4){
                    sonarAlarma();
                    return;
                }stopAutomatico();
            }
        });
    }

    class Mostrar extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... params) {

            if(filtrarDatos())mostrarPersona(posicion);
            return null;
        }
    }

    public void cerrar(View v){
        finish();
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

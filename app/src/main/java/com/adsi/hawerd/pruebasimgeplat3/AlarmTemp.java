package com.adsi.hawerd.pruebasimgeplat3;

import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class AlarmTemp extends ActionBarActivity {
    MediaPlayer nuclear;
    TextView resu;
    Button b1,b5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_temp);

        b5=(Button)findViewById(R.id.button5);
       // sonarAlarma();
    }

    public void paraAlarma(View v){
        b5.setVisibility(View.VISIBLE);
        nuclear.stop();
    }

    public void sonarAlarma(){
        nuclear = MediaPlayer.create(AlarmTemp.this, R.drawable.nuclear);
        nuclear.start();
    }

    public void finalizar(View v){
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_alarm_temp, menu);
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

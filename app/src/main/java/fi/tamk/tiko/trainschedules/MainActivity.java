package fi.tamk.tiko.trainschedules;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements ChosenStation {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
    }

    @Override
    public void itemSelected(String stationCode) {
        BoardFragment fragment = (BoardFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentRight);
        Log.d(this.getClass().getName(),"MainActivity: " + fragment);
        Log.d(this.getClass().getName(), "In main activity: " + stationCode);
        if(fragment != null && fragment.getView() != null){
            fragment.setStation(stationCode);
        } else {
            Intent intent = new Intent(this, BoardActivity.class);
            intent.putExtra("name", stationCode);
            startActivity(intent);
        }
    }
}

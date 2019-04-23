package fi.tamk.tiko.trainschedules;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import fi.tamk.tiko.trainschedules.fragments.BoardFragment;

/**
 * Activity that holds board fragment.
 */
public class BoardActivity extends AppCompatActivity {

    /**
     * Called when the activity is created.
     * @param savedInstanceState Saved state.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_activity);
    }


    /**
     * Called when activity is resumed.
     */
    @Override
    protected void onResume() {
        super.onResume();
        String name = getIntent().getExtras().getString("name");
        String stationCode =getIntent().getExtras().getString("code");
        BoardFragment fragment = (BoardFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentRight);
        fragment.setStation(name);
        fragment.setStationCode(stationCode);
    }
}

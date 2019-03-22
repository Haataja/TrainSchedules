package fi.tamk.tiko.trainschedules;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import fi.tamk.tiko.trainschedules.fragments.BoardFragment;

public class BoardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_activity);
    }


    @Override
    protected void onResume() {
        super.onResume();
        String name = getIntent().getExtras().getString("name");
        BoardFragment fragment = (BoardFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentRight);
        fragment.setStation(name);
    }
}

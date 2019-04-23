package fi.tamk.tiko.trainschedules;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.Locale;

import fi.tamk.tiko.trainschedules.fragments.BoardFragment;
import fi.tamk.tiko.trainschedules.fragments.TrainStations;

/**
 * Main activity that is started when the app is started. Holds the list of the staions
 * or the list of stations and list of the trains.
 */
public class MainActivity extends AppCompatActivity implements ChosenStation {
    /**
     * Called when the activity is created.
     * @param savedInstanceState Saved state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
    }

    /**
     * Creates the menu in the title bar.
     * @param menu Menu that is inflated.
     * @return boolean.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                TrainStations.getAdapter().getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                TrainStations.getAdapter().getFilter().filter(newText);
                return false;
            }
        });

        MenuItem item =  menu.findItem(R.id.language);
        if(!getBaseContext().getResources().getConfiguration().getLocales().get(0).equals(new Locale("fi","FI"))){
            item.setTitle("FI");
        }

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Communicates between the fragments the chosen station and its station code.
     * @param stationName The name of the station.
     * @param stationCode The code of the station.
     */
    @Override
    public void itemSelected(String stationName,String stationCode) {
        BoardFragment fragment = (BoardFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentRight);
        if(fragment != null && fragment.getView() != null){
            fragment.setStation(stationName);
            fragment.setStationCode(stationCode);
        } else {
            Intent intent = new Intent(this, BoardActivity.class);
            intent.putExtra("name", stationName);
            intent.putExtra("code", stationCode);
            startActivity(intent);
        }
    }

    /**
     * Called when one of the items in menu is selected.
     * @param item Item that is selected.
     * @return boolean.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);

        boolean result = false;
        if(item.getItemId() == R.id.search){
            result = true;
        } else if (item.getItemId() == R.id.language){
            //Log.d(this.getClass().getName(), "ACTIVITY CLIKED: 1");
            Configuration config = new Configuration(getBaseContext().getResources().getConfiguration());
            if(getBaseContext().getResources().getConfiguration().getLocales().get(0).equals(new Locale("fi","FI"))){
                //Log.d(this.getClass().getName(), "ACTIVITY CLIKED: 2");
                Locale.setDefault(new Locale("en"));
                config.setLocale(new Locale("en"));
                item.setTitle("FI");
            } else {
                //Log.d(this.getClass().getName(), "ACTIVITY CLIKED: 3");
                Locale.setDefault(new Locale("fi","FI"));
                config.setLocale(new Locale("fi","FI"));
                item.setTitle("EN");
            }
            getResources().updateConfiguration(config, getResources().getDisplayMetrics());
            getBaseContext().createConfigurationContext(config);
        }
        return result;
    }
}

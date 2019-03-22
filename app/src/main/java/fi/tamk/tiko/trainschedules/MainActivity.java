package fi.tamk.tiko.trainschedules;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity implements ChosenStation {
    private Menu menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(this.getClass().getName(), "Loading menus");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        Log.d(this.getClass().getName(), "Search item " + searchItem);

        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);

        boolean result = false;
        if(item.getItemId() == R.id.search){
            result = true;
        }
        return result;
    }
}

package fi.tamk.tiko.trainschedules;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import fi.tamk.tiko.trainschedules.fragments.TabFragmentDeparture;
import fi.tamk.tiko.trainschedules.fragments.TrainStations;
import fi.tamk.tiko.trainschedules.model.TimeTableRow;
import fi.tamk.tiko.trainschedules.model.Train;

/**
 * Activity that holds view of the train schedule.
 */
public class TrainActivity extends AppCompatActivity {
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    private String type;
    private int number;
    private String station;

    private static RecyclerView recyclerView;
    private static RecyclerView.Adapter mAdapter;
    private static RecyclerView.LayoutManager layoutManager;
    private static Context context;

    /**
     * Called when the activity is created.
     * @param savedInstanceState Saved state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.train_layout);

        recyclerView = findViewById(R.id.stops);
        context = getApplicationContext();

        recyclerView.setHasFixedSize(false);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new TrainViewAdapter(new ArrayList<>());
        recyclerView.setAdapter(mAdapter);

        type = getIntent().getExtras().getString("type");
        number = getIntent().getExtras().getInt("number");
        station = getIntent().getExtras().getString("station");
        String title = type + " " + number;
        // Log.d(this.getClass().getName(), "Station " + station);
        ((TextView) findViewById(R.id.train_number)).setText(title);

        new AsyncFetch().execute(station);
    }

    /**
     * Adapter for this class RecycleView.
     */
    static class TrainViewAdapter extends RecyclerView.Adapter<TrainViewAdapter.TrainViewHolder> {
        /**
         * Data set that is shown.
         */
        private List<TimeTableRow> dataSet;
        /**
         * Tells if the next station that train will be stopping is set.
         */
        private boolean next = false;

        /**
         * View holder for the recycle view.
         */
        public class TrainViewHolder extends RecyclerView.ViewHolder {

            public LinearLayout textView;

            public TrainViewHolder(LinearLayout v) {
                super(v);
                textView = v;
            }
        }


        public TrainViewAdapter(List<TimeTableRow> dataSet) {
            this.dataSet = dataSet;
        }

        /**
         * Creates view holder for the recycle view.
         * @param parent parent view group
         * @param viewType integer, not used
         * @return View holder
         */
        @Override
        public TrainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_stops, parent, false);
            TrainViewHolder vh = new TrainViewHolder(v);
            return vh;
        }

        /**
         * Binds the data item to the list item.
         * @param holder Holder that holds the view for the items in list.
         * @param position position on the list.
         */
        @Override
        public void onBindViewHolder(TrainViewHolder holder, int position) {
            TimeTableRow ttr = dataSet.get(position);
            ((TextView) holder.textView.findViewById(R.id.stationName)).setText(TrainStations.codeToStation.get(ttr.getStationShortCode()));
            ((TextView) holder.textView.findViewById(R.id.arrival_time)).setText(ttr.getScheduledTime().format(formatter));
            if (ttr.getLiveEstimateTime() != null) {
                String estTime = ttr.getLiveEstimateTime().format(formatter);
                ((TextView) holder.textView.findViewById(R.id.stop_notice)).setText(estTime);
                ((TextView) holder.textView.findViewById(R.id.arrival_time)).setPaintFlags( Paint.STRIKE_THRU_TEXT_FLAG);
            }

            if(ttr.getScheduledTime().isBefore(LocalDateTime.now())){
                ((TextView) holder.textView.findViewById(R.id.arrival_time)).setPaintFlags( Paint.STRIKE_THRU_TEXT_FLAG);
                ((TextView) holder.textView.findViewById(R.id.stationName)).setPaintFlags( Paint.STRIKE_THRU_TEXT_FLAG);
                ((TextView) holder.textView.findViewById(R.id.stop_notice)).setPaintFlags( Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                if(!next){
                    next = true;
                    holder.textView.setBackground(context.getDrawable(R.drawable.border));
                }
            }
        }
        /**
         * Gets the number of items in the list.
         * @return number of items
         */
        @Override
        public int getItemCount() {
            return dataSet.size();
        }
    }
    /**
     * Async fetch for fetching data from the API.
     */
    public class AsyncFetch extends AsyncTask<String, String, List<TimeTableRow>> {
        private InputStream in = null;
        private String BASE_URL = "https://rata.digitraffic.fi/api/v1/live-trains/station/";
        private String OPTIONS = "?arrived_trains=0&arriving_trains=100&departed_trains=0&departing_trains=100&include_nonstopping=false";

        /**
         * Fetch from the API.
         * @param string name of the station
         * @return List of time table rows.
         */
        @Override
        protected List<TimeTableRow> doInBackground(String... string) {
            String resultString = "";
            try {
                URL url = new URL(BASE_URL + string[0] + OPTIONS);
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                in = connection.getInputStream();

                int myChar;
                StringBuilder stringBuilder = new StringBuilder();
                BufferedReader br = new BufferedReader(new InputStreamReader(in, "utf8"));
                while ((myChar = br.read()) != -1) {
                    stringBuilder.append((char) myChar);
                }
                resultString = stringBuilder.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            //Log.d(this.getClass().getName(), "Mapping");
            List<TimeTableRow> trains = parseTrains(resultString);

            //Log.d(this.getClass().getName(), "Mapping" + trains);
            return trains;
        }
        /**
         * Parses the raw json data to java list of java objects
         * @param resultString json data in form of the string
         * @return List of time table rows.
         */
        private List<TimeTableRow> parseTrains(String resultString) {
            List<Train> trains = new ArrayList<>();
            try {
                JSONArray reader = new JSONArray(resultString);
                for (int i = 0; i < reader.length(); i++) {
                    JSONObject o = reader.getJSONObject(i);
                    if (o.getInt("trainNumber") == number) {
                        // Log.d(this.getClass().getName(), "Train number: " + o.getInt("trainNumber"));
                        Train train = new Train(o.getInt("trainNumber"), o.getString("trainType"), o.getString("trainCategory"), o.getString("commuterLineID"));
                        JSONArray timetableArray = o.getJSONArray("timeTableRows");
                        ArrayList<TimeTableRow> timeTableRows = new ArrayList<>();
                        for (int j = 0; j < timetableArray.length(); j++) {
                            JSONObject timetableObject = timetableArray.getJSONObject(j);
                            boolean commercialStop = false;
                            try{
                                commercialStop = timetableObject.getBoolean("commercialStop");
                            } catch (Exception e){
                                // do nothing
                            }

                            if (timetableObject.getString("type").equalsIgnoreCase("DEPARTURE") && commercialStop) {
                                TimeTableRow ttr = new TimeTableRow(timetableObject.getString("stationShortCode"),
                                        timetableObject.getString("type"),
                                        timetableObject.getString("commercialTrack"),
                                        timetableObject.getBoolean("cancelled"));
                                String sched = timetableObject.getString("scheduledTime");
                                ttr.setScheduledTime(sched);
                                String live = null;
                                int diff = 0;
                                try {
                                    live = timetableObject.getString("liveEstimateTime");
                                    diff = timetableObject.getInt("differenceInMinutes");
                                } catch (JSONException e) {
                                    // if not found don't do anything
                                }
                                if (live != null && diff != 0) {
                                    ttr.setLiveEstimateTime(live);
                                }
                                timeTableRows.add(ttr);
                            }

                            if (j == timetableArray.length() - 1) {
                                train.setDestination(timetableObject.getString("stationShortCode"));
                            }
                        }
                        if (timeTableRows.size() > 0) {
                            train.setTimeTableRows(timeTableRows);
                            trains.add(train);
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(trains.size() > 1){
                // Log.d(this.getClass().getName(), "Too many trains!");
                return trains.get(0).getTimeTableRows();
            } else if(trains.size() > 0){
                return trains.get(0).getTimeTableRows();
            }
            return new ArrayList<>();
        }

        /**
         * Publishes the train list.
         * @param trains List of trains.
         */
        @Override
        protected void onPostExecute(List<TimeTableRow> trains) {
            if (trains != null) {
                mAdapter = new TrainViewAdapter(trains);
                recyclerView.setAdapter(mAdapter);

            } else {
                Toast.makeText(context, "No connection, try again later", Toast.LENGTH_LONG).show();
            }
        }
    }
}

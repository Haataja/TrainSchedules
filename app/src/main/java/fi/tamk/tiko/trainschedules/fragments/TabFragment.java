package fi.tamk.tiko.trainschedules.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import fi.tamk.tiko.trainschedules.R;
import fi.tamk.tiko.trainschedules.model.TimeTableRow;
import fi.tamk.tiko.trainschedules.model.Train;

public class TabFragment extends Fragment {

    private static RecyclerView recyclerView;
    private static RecyclerView.Adapter mAdapter;
    private static RecyclerView.LayoutManager layoutManager;
    private static Context context;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.board_fragment, container, false);
        recyclerView = view.findViewById(R.id.board_fragment);
        context = getActivity().getApplicationContext();

        recyclerView.setHasFixedSize(false);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new TabRecycleViewAdapter(new ArrayList<>());
        recyclerView.setAdapter(mAdapter);

        return view;
    }

    public void triggerFetch(String string){
        new AsyncFetch().execute(string);
    }


    static class TabRecycleViewAdapter extends RecyclerView.Adapter<TabRecycleViewAdapter.TabViewHolder> {
        private List<Train> dataSet;

        public class TabViewHolder extends RecyclerView.ViewHolder {

            public LinearLayout textView;

            public TabViewHolder(LinearLayout v) {
                super(v);
                textView = v;
            }
        }


        public TabRecycleViewAdapter(List<Train> dataSet) {
            this.dataSet = dataSet;
        }


        @Override
        public TabViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.board_list_text, parent, false);
            TabViewHolder vh = new TabViewHolder(v);
            return vh;
        }


        @Override
        public void onBindViewHolder(TabViewHolder holder, int position) {
            Train train = dataSet.get(position);
            String trainNumber = train.getTrainType() + " " + train.getTrainNumber();
            if(train.getTrainCategory().equalsIgnoreCase("Commuter")){
                trainNumber = train.getCommuterLineID();
            }
            ((TextView)holder.textView.findViewById(R.id.train)).setText(trainNumber);
            ((TextView)holder.textView.findViewById(R.id.track)).setText(dataSet.get(position).getDestination());

            Log.d(this.getClass().getName(), "setting" + trainNumber);

            //((TextView)holder.textView.findViewById(R.id.stationCode)).setText(dataSet.get(position).getStationShortCode());

            /*holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String stationCode = ((TextView) v.findViewById(R.id.stationCode)).getText().toString();
                    Log.d(this.getClass().getName(), "Textview Clicked: " + stationCode);

                }
            });*/
        }

        @Override
        public int getItemCount() {
            return dataSet.size();
        }
    }

    public static class AsyncFetch extends AsyncTask<String, String, List<Train>> {
        private InputStream in = null;
        private String BASE_URL = "https://rata.digitraffic.fi/api/v1/live-trains/station/";
        private String OPTIONS = "?arrived_trains=0&arriving_trains=100&departed_trains=0&departing_trains=100&include_nonstopping=false";


        @Override
        protected List<Train> doInBackground(String... string) {
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
            Log.d(this.getClass().getName(), "Mapping" );
            List<Train> trains = new ArrayList<>();
            try {
                JSONArray reader = new JSONArray(resultString);
                for(int i = 0; i < reader.length(); i++){
                    JSONObject o = reader.getJSONObject(i);
                    Train train = new Train(o.getInt("trainNumber"),o.getString("trainType"),o.getString("trainCategory"),o.getString("commuterLineID"));
                    JSONArray timetableArray = o.getJSONArray("timeTableRows");
                    ArrayList<TimeTableRow> timeTableRows = new ArrayList<>();
                    for(int j = 0; j < timetableArray.length(); j++){
                        JSONObject timetableObject = timetableArray.getJSONObject(j);
                        if(timetableObject.getString("stationShortCode").equalsIgnoreCase(string[0])){
                            TimeTableRow ttr = new TimeTableRow(timetableObject.getString("stationShortCode"),
                                    timetableObject.getString("type"),
                                    timetableObject.getString("commercialTrack"),
                                    timetableObject.getBoolean("cancelled"));
                            String sched = timetableObject.getString("scheduledTime");
                            ttr.setScheduledTime(sched);
                            String live = null;
                            try{
                                live = timetableObject.getString("liveEstimateTime");
                            }catch (JSONException e){

                            }
                            if(live != null){
                                ttr.setLiveEstimateTime(live);
                            }
                            timeTableRows.add(ttr);
                        }

                        if(j == timetableArray.length() - 1){
                            train.setDestination(timetableObject.getString("stationShortCode"));
                        }
                    }
                    train.setTimeTableRows(timeTableRows);
                    trains.add(train);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d(this.getClass().getName(), "Mapping" + trains);
            return trains;
        }


        @Override
        protected void onPostExecute(List<Train> trains) {
            if (trains != null) {
                mAdapter = new TabRecycleViewAdapter(trains);
                recyclerView.setAdapter(mAdapter);

            } else {
                Toast.makeText(context, "NOT FOUND!", Toast.LENGTH_SHORT).show();
            }
        }
    }


}

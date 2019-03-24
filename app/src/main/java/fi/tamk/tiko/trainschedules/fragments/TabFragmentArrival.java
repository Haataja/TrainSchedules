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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import fi.tamk.tiko.trainschedules.R;
import fi.tamk.tiko.trainschedules.model.TimeTableRow;
import fi.tamk.tiko.trainschedules.model.Train;

public class TabFragmentArrival extends Fragment {
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    private static RecyclerView recyclerView;
    private static RecyclerView.Adapter mAdapter;
    private static RecyclerView.LayoutManager layoutManager;
    private static Context context;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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

    public void triggerFetch(String station) {
        new AsyncFetch().execute(station);
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
            if (train.getTrainCategory().equalsIgnoreCase("Commuter")) {
                trainNumber = train.getCommuterLineID();
            }
            ((TextView) holder.textView.findViewById(R.id.train)).setText(trainNumber);
            ((TextView) holder.textView.findViewById(R.id.destination)).setText(train.getDestination());
            if (train.getTimeTableRows() != null && train.getTimeTableRows().size() > 0) {
                //Log.d(this.getClass().getName(), "TimeTableRows = " + train.getTimeTableRows().size());
                ((TextView) holder.textView.findViewById(R.id.track)).setText(train.getTimeTableRows().get(0).getCommercialTrack());
                String time = train.getTimeTableRows().get(0).getScheduledTime().format(formatter);
                ((TextView) holder.textView.findViewById(R.id.time)).setText(time);
                if (train.getTimeTableRows().get(0).getLiveEstimateTime() != null) {
                    String estTime = train.getTimeTableRows().get(0).getLiveEstimateTime().format(formatter);
                    ((TextView) holder.textView.findViewById(R.id.notice)).setText(estTime);
                }
            }


            //Log.d(this.getClass().getName(), "setting" + trainNumber);

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

    public class AsyncFetch extends AsyncTask<String, String, List<Train>> {
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
            List<Train> trains = parseTrains(resultString, string[0]);
            return trains;
        }

        private List<Train> parseTrains(String resultString, String station) {
            List<Train> trains = new ArrayList<>();
            try {
                JSONArray reader = new JSONArray(resultString);
                for (int i = 0; i < reader.length(); i++) {
                    JSONObject o = reader.getJSONObject(i);
                    if (!o.getString("trainCategory").equalsIgnoreCase("cargo")) {
                        Train train = new Train(o.getInt("trainNumber"), o.getString("trainType"), o.getString("trainCategory"), o.getString("commuterLineID"));
                        JSONArray timetableArray = o.getJSONArray("timeTableRows");
                        ArrayList<TimeTableRow> timeTableRows = new ArrayList<>();
                        for (int j = 0; j < timetableArray.length(); j++) {
                            JSONObject timetableObject = timetableArray.getJSONObject(j);
                            if (timetableObject.getString("stationShortCode").equalsIgnoreCase(station) &&
                                    timetableObject.getString("type").equalsIgnoreCase("ARRIVAL")
                                    && timetableObject.getBoolean("commercialStop")) {
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

                            if (j == 0) {
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
            return trains;
        }


        @Override
        protected void onPostExecute(List<Train> trains) {
            if (trains != null) {
                Collections.sort(trains);
                mAdapter = new TabRecycleViewAdapter(trains);
                recyclerView.setAdapter(mAdapter);

            } else {
                Toast.makeText(context, "NOT FOUND!", Toast.LENGTH_SHORT).show();
            }
        }
    }


}

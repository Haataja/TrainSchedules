package fi.tamk.tiko.trainschedules.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.net.ssl.HttpsURLConnection;

import fi.tamk.tiko.trainschedules.ChosenStation;
import fi.tamk.tiko.trainschedules.R;
import fi.tamk.tiko.trainschedules.model.TrainStation;

public class TrainStations extends Fragment {
    private static RecyclerView recyclerView;
    private static RecyclerView.Adapter mAdapter;
    private static RecyclerView.LayoutManager layoutManager;
    private static Context context;
    private static ChosenStation callBack;
    public static Map<String, String> codeToStation;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        codeToStation = new TreeMap<>();
        callBack = (ChosenStation) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_train_stations, container, false);
        recyclerView = view.findViewById(R.id.trainStations);
        context = getActivity().getApplicationContext();

        recyclerView.setHasFixedSize(false);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new MyAdapter(new ArrayList<>());
        recyclerView.setAdapter(mAdapter);

        new AsyncFetch().execute();
        return view;
    }

    public static class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private List<TrainStation> dataSet;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public LinearLayout textView;

            public MyViewHolder(LinearLayout v) {
                super(v);
                textView = v;
            }
        }


        public MyAdapter(List<TrainStation> dataSet) {
            this.dataSet = dataSet;

        }


        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
            // create a new view
            LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_text_view, parent, false);
            MyViewHolder vh = new MyViewHolder(v);
            return vh;
        }


        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            ((TextView)holder.textView.findViewById(R.id.stationName)).setText(dataSet.get(position).getStationName());
            ((TextView)holder.textView.findViewById(R.id.stationCode)).setText(dataSet.get(position).getStationShortCode());

            holder.textView.setOnClickListener(v -> {
                String stationCode = ((TextView) v.findViewById(R.id.stationCode)).getText().toString();
                String stationName = ((TextView) v.findViewById(R.id.stationName)).getText().toString();
                callBack.itemSelected(stationName,stationCode);

            });
        }

        @Override
        public int getItemCount() {
            return dataSet.size();
        }
    }

    public static class AsyncFetch extends AsyncTask<String, String, List<TrainStation>> {
        private InputStream in = null;
        private String BASE_URL = "https://rata.digitraffic.fi/api/v1";

        @Override
        protected List<TrainStation> doInBackground(String... string) {
            String resultString = "";
            try {
                URL url = new URL(BASE_URL + "/metadata/stations");
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
            ObjectMapper mapper = new ObjectMapper();
            List<TrainStation> stations = null;
            try {
                stations = mapper.readValue(resultString, new TypeReference<List<TrainStation>>() {
                });
            } catch (IOException e) {
                e.printStackTrace();
            }

            return stations;
        }


        @Override
        protected void onPostExecute(List<TrainStation> stations) {
            if (stations != null) {
                List<TrainStation> newStations = new ArrayList<>();
                for (int i = 0; i < stations.size(); i++) {
                    if (stations.get(i).isPassengerTraffic()) {
                        if(stations.get(i).getStationName().contains("asema") || stations.get(i).getStationName().contains("Asema")){
                            stations.get(i).setStationName(stations.get(i).getStationName().split(" ")[0]);
                        }
                        newStations.add(stations.get(i));
                        codeToStation.put(stations.get(i).getStationShortCode(),stations.get(i).getStationName());
                    }
                }
                mAdapter = new MyAdapter(newStations);
                recyclerView.setAdapter(mAdapter);

            } else {
                Toast.makeText(context, "NOT FOUND!", Toast.LENGTH_SHORT).show();
            }
        }
    }


}

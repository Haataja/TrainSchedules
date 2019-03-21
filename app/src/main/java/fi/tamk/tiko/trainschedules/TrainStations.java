package fi.tamk.tiko.trainschedules;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import javax.net.ssl.HttpsURLConnection;

public class TrainStations extends Fragment {
    private static RecyclerView recyclerView;
    private static RecyclerView.Adapter mAdapter;
    private static RecyclerView.LayoutManager layoutManager;
    private static Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    static class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private List<TrainStation> dataSet;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView textView;
            public MyViewHolder(TextView v) {
                super(v);
                textView = v;
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String string = ((TextView) v).getText().toString();
                        Log.d(this.getClass().getName(),"Textview Clicked: " + string);
                        //Intent intent = new Intent();
                    }
                });
            }
        }


        public MyAdapter(List<TrainStation> dataSet) {
            this.dataSet = dataSet;
        }

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
            // create a new view
            TextView v = (TextView) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_text_view, parent, false);
            MyViewHolder vh = new MyViewHolder(v);
            return vh;
        }


        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.textView.setText(dataSet.get(position).toString());

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
                while ((myChar = br.read()) != -1){
                    stringBuilder.append((char) myChar);
                }
                resultString = stringBuilder.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(in!= null){
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
                stations = mapper.readValue(resultString, new TypeReference<List<TrainStation>>(){});
            } catch (IOException e) {
                e.printStackTrace();
            }

            return stations;
        }


        @Override
        protected void onPostExecute(List<TrainStation> stations) {
            if(stations != null){
                List<TrainStation> newStations= new ArrayList<>();
                for(int i = 0; i < stations.size(); i++){
                    if(stations.get(i).isPassengerTraffic()){
                        newStations.add(stations.get(i));
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

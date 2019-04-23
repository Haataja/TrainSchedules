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
import android.widget.Filter;
import android.widget.Filterable;
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

/**
 * Class that shows passenger traffic train stations.
 */
public class TrainStations extends Fragment {
    private static RecyclerView recyclerView;
    private static RecyclerView.Adapter mAdapter;
    private static RecyclerView.LayoutManager layoutManager;
    private static Context context;
    private static ChosenStation callBack;

    /**
     * Map that maps station short codes to the station full name.
     */
    public static Map<String, String> codeToStation;


    /**
     * Gets the Recycle view adapter.
     * @return Adapter.
     */
    public static MyAdapter getAdapter() {
        return (MyAdapter) mAdapter;
    }

    /**
     * Called when fragment is created.
     * @param savedInstanceState Saved state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        codeToStation = new TreeMap<>();
        callBack = (ChosenStation) getActivity();
    }

    /**
     * Called when view of the fragment is created.
     * @param inflater Inflater
     * @param container Container
     * @param savedInstanceState Saved state
     * @return View
     */
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
    /**
     * Adapter for this class RecycleView.
     */
    public static class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements Filterable {
        /**
         * Data set that is shown.
         */
        private List<TrainStation> dataSet;
        /**
         * Copy of the data set for searching purposes.
         */
        private List<TrainStation> dataSetCopy;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public LinearLayout textView;

            public MyViewHolder(LinearLayout v) {
                super(v);
                textView = v;
            }
        }


        public MyAdapter(List<TrainStation> dataSet) {
            this.dataSet = dataSet;
            this.dataSetCopy = dataSet;
        }

        /**
         * Filters the recycle view.
         * @return filtered list.
         */
        @Override
        public Filter getFilter() {
            return new Filter() {
                List<TrainStation> contactListFiltered;

                /**
                 * Performs filtering.
                 * @param charSequence User input that filtering is based on.
                 * @return filtered list.
                 */
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    String charString = charSequence.toString();
                    if (charString.isEmpty()) {
                        contactListFiltered = dataSetCopy;
                    } else {
                        List<TrainStation> filteredList = new ArrayList<>();
                        for (TrainStation row : dataSetCopy) {
                            if (row.getStationName().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(row);
                            }
                        }
                        contactListFiltered = filteredList;
                    }

                    FilterResults filterResults = new FilterResults();
                    filterResults.values = contactListFiltered;
                    return filterResults;
                }

                /**
                 * Sets data set to filtered list.
                 * @param charSequence  User input that filtering is based on.
                 * @param filterResults filtered list.
                 */
                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    dataSet = (ArrayList<TrainStation>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
        }

        /**
        * Creates view holder for the recycle view.
         * @param parent parent view group
         * @param viewType integer, not used
         * @return View holder
         */
        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_text_view, parent, false);
            MyViewHolder vh = new MyViewHolder(v);
            return vh;
        }

        /**
         * Binds the data item to the list item.
         * @param holder Holder that holds the view for the items in list.
         * @param position position on the list.
         */
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
    public static class AsyncFetch extends AsyncTask<String, String, List<TrainStation>> {
        private InputStream in = null;
        private String BASE_URL = "https://rata.digitraffic.fi/api/v1";

        /**
         * Fetch from the API.
         * @return List of stations
         */
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

        /**
         * Publishes the station list.
         * @param stations List of stations.
         */
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
                    }
                    codeToStation.put(stations.get(i).getStationShortCode(),stations.get(i).getStationName());
                }
                mAdapter = new MyAdapter(newStations);
                recyclerView.setAdapter(mAdapter);

            } else {
                Toast.makeText(context, "No connection, try again later", Toast.LENGTH_LONG).show();
            }
        }
    }


}

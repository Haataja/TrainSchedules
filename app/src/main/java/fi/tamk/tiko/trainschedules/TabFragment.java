package fi.tamk.tiko.trainschedules;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

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
        ArrayList<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");
        list.add("D");
        list.add("E");
        list.add("F");
        list.add("G");
        mAdapter = new TabRecycleViewAdapter(list);
        recyclerView.setAdapter(mAdapter);

        return view;
    }


    static class TabRecycleViewAdapter extends RecyclerView.Adapter<TabRecycleViewAdapter.TabViewHolder> {
        private List<String> dataSet;

        public class TabViewHolder extends RecyclerView.ViewHolder {

            public LinearLayout textView;

            public TabViewHolder(LinearLayout v) {
                super(v);
                textView = v;
            }
        }


        public TabRecycleViewAdapter(List<String> dataSet) {
            this.dataSet = dataSet;
        }


        @Override
        public TabViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_text_view, parent, false);
            TabViewHolder vh = new TabViewHolder(v);
            return vh;
        }


        @Override
        public void onBindViewHolder(TabViewHolder holder, int position) {
            ((TextView)holder.textView.findViewById(R.id.stationName)).setText(dataSet.get(position));
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


}

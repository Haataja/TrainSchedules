package fi.tamk.tiko.trainschedules.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fi.tamk.tiko.trainschedules.R;
import fi.tamk.tiko.trainschedules.TabAdapter;

public class BoardFragment extends Fragment {
    private String stationName;
    private String stationCode;
    private SwipeRefreshLayout srl;
    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.board_view, container, false);
        srl = view.findViewById(R.id.swiperefreshBoard);
        srl.setOnRefreshListener(() -> refresh());

        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);
        adapter = new TabAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(new TabFragmentDeparture(), getString(R.string.departure));
        adapter.addFragment(new TabFragmentArrival(), getString(R.string.arrivals));
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    private void refresh() {
        Log.i(this.getClass().getName(), "Refreshing!! " + stationCode);
        if (stationCode != null) {
            TabFragmentDeparture fragment = (TabFragmentDeparture) adapter.getItem(0);
            fragment.triggerFetch(stationCode);

            TabFragmentArrival fragment1 = (TabFragmentArrival) adapter.getItem(1);
            fragment1.triggerFetch(stationCode);

        }
        srl.setRefreshing(false);
    }

    public void setStation(String station) {
        ((TextView) getView().findViewById(R.id.name)).setText(station);
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
        refresh();
    }
}

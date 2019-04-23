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

/**
 * Fragment that holds two tabs and can be refreshed.
 */
public class BoardFragment extends Fragment {
    private String stationName;
    private String stationCode;
    private SwipeRefreshLayout srl;
    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    /**
     * Called when view of the fragment is created.
     * @param inflater Inflater
     * @param container Container
     * @param savedInstanceState Saved state
     * @return View
     */
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

    /**
     * Calls the back API again with current station, shows refresh circle while fetching.
     */
    private void refresh() {
        //Log.i(this.getClass().getName(), "Refreshing!! " + stationCode);
        if (stationCode != null) {
            TabFragmentDeparture fragment = (TabFragmentDeparture) adapter.getItem(0);
            fragment.triggerFetch(stationCode);

            TabFragmentArrival fragment1 = (TabFragmentArrival) adapter.getItem(1);
            fragment1.triggerFetch(stationCode);

        }
        srl.setRefreshing(false);
    }

    /**
     * Sets current station
     * @param station Station name
     */
    public void setStation(String station) {
        ((TextView) getView().findViewById(R.id.name)).setText(station);
    }

    /**
     * Sets current station short code
     * @param stationCode Station short code
     */
    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
        refresh();
    }
}

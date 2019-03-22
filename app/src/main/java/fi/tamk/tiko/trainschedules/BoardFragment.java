package fi.tamk.tiko.trainschedules;

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

public class BoardFragment extends Fragment {
    private SwipeRefreshLayout srl;
    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.board_view, container, false);
        srl = view.findViewById(R.id.swiperefreshBoard);
        srl.setOnRefreshListener(() -> doSomething());

        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);
        adapter = new TabAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(new TabFragment(), "Tab 1");
        adapter.addFragment(new TabFragment(), "Tab 2");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    private void doSomething() {
        Log.i(this.getClass().getName(), "Refreshing!!");
        srl.setRefreshing(false);
    }

    public void setStation(String station){
        Log.d(this.getClass().getName(), "In Board Fragment: " + station);
    }
}

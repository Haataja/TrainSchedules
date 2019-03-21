package fi.tamk.tiko.trainschedules;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BoardFragment extends Fragment {
    private SwipeRefreshLayout srl;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.board_view, container, false);
        srl = view.findViewById(R.id.swiperefreshBoard);
        srl.setOnRefreshListener(() -> doSomething());
        return view;
    }

    private void doSomething() {
        Log.i(this.getClass().getName(), "Refreshing!!");
        srl.setRefreshing(false);
    }
}

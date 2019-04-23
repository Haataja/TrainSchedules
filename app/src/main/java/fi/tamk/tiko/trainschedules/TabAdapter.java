package fi.tamk.tiko.trainschedules;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter that handles the tab fragments.
 */
public class TabAdapter extends FragmentStatePagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    /**
     * Constructor with FragmentManager.
     * @param fm FragmentManager.
     */
    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * Gets the tab.
     * @param i index of the tab.
     * @return the tab.
     */
    @Override
    public Fragment getItem(int i) {
        return mFragmentList.get(i);
    }

    /**
     * Gets the number of tabs.
     * @return the number of tabs.
     */
    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    /**
     * Adds fragment to the list.
     * @param fragment Fragment that is added.
     * @param title The name of the fragment.
     */
    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    /**
     * Gets The title of the fragment in index.
     * @param position the index of the fragmen.
     * @return the title of the fragment.
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}

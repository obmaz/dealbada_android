package com.moon.dealocean.base;


import androidx.fragment.app.Fragment;

/**
 * Created by zambo on 16. 7. 18..
 */
public class BaseFragment extends Fragment {

    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().invalidateOptionsMenu();
    }
}

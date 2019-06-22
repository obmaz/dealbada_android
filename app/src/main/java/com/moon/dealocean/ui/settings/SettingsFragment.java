package com.moon.dealocean.ui.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.IntentCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import android.widget.Toast;

import com.moon.dealocean.R;
import com.moon.dealocean.ui.MainActivity;

/**
 * Created by zambo on 2016. 12. 5..
 */

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    public static SettingsFragment newInstance() {
        SettingsFragment settingsFragment = new SettingsFragment();
        Bundle bundle = new Bundle();
        settingsFragment.setArguments(bundle);
        return settingsFragment;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case "settingsTheme":
            case "settingsDrawerEdgeRange":
                if (getContext() != null) {
                    Intent intentToBeNewRoot = new Intent(getContext(), MainActivity.class);
                    startActivity(Intent.makeRestartActivityTask(intentToBeNewRoot.getComponent()));
                }
                break;
        }
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings);
        PreferenceManager.getDefaultSharedPreferences(getContext()).registerOnSharedPreferenceChangeListener(this);

        Preference loginPreference = findPreference("settingsLogin");
        loginPreference.setOnPreferenceClickListener(this::onLoginClick);

        Preference removeLoginPreference = findPreference("settingsRemoveLogin");
        removeLoginPreference.setOnPreferenceClickListener(this::onRemoveLoginClick);
    }

    public boolean onLoginClick(Preference preference) {
        if (SettingsHelper.isLoginned(getContext())) {
            AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                    .setTitle(R.string.settings_login_title)
                    .setMessage(R.string.settings_login_dialog_message)
                    .setPositiveButton(R.string.settings_login_dialog_positive_button, (dialogInterface, i) -> login())
                    .setNegativeButton(R.string.negative_button, null)
                    .create();
            alertDialog.show();
        } else {
            login();
        }

        return true;
    }

    public boolean onRemoveLoginClick(Preference preference) {
        SettingsHelper.deleteLoginTokken(getContext());
        Toast.makeText(getContext(), R.string.removed_login_session, Toast.LENGTH_SHORT).show();
        return true;
    }

    private void login() {
        SettingsHelper.deleteLoginTokken(getContext());
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.pref_container, LoginFragment.newInstance());
        fragmentTransaction.commit();
    }
}

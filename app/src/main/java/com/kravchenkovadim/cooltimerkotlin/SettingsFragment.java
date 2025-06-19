package com.kravchenkovadim.cooltimerkotlin;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Objects;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;

public class SettingsFragment  extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener {
    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        ListPreference soundPref = findPreference("melody");
        if(soundPref != null){
            updateListPreferenceSummary(soundPref);
        }

        EditTextPreference editTextPreference = findPreference("default_interval");
        if(editTextPreference != null){
            String value = editTextPreference.getText();
            if (value == null || value.isEmpty()){
                editTextPreference.setSummary("Введіть секунди");
            } else {
                editTextPreference.setSummary(value);
            }
        }
        //Preference preferenceEditTime = findPreference("");
        assert editTextPreference != null;
        editTextPreference.setOnPreferenceChangeListener(this);
    }

    private void updateListPreferenceSummary(ListPreference soundPref) {
        soundPref.setSummary(soundPref.getEntry());
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(getPreferenceScreen().getSharedPreferences())
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        Objects.requireNonNull(getPreferenceScreen().getSharedPreferences())
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Objects.requireNonNull(getPreferenceScreen().getSharedPreferences()).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, @Nullable String key) {
        assert key != null;
        if (key.equals("melody")){
            ListPreference soundPref = findPreference(key);
            if(soundPref!=null){
                updateListPreferenceSummary(soundPref);
            }
        }
        if("default_interval".equals(key)){
            EditTextPreference editTextPreference =findPreference(key);
            if(editTextPreference!=null){
                String value = editTextPreference.getText();
                if(value == null || value.isEmpty()){
                    editTextPreference.setSummary("Input seconds");
                } else {
                    editTextPreference.setSummary(value);
                }
            }
        }
    }

    @Override
    public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
        if(preference.getKey().equals("default_interval")){
            Toast toast = Toast.makeText(getContext(),"Please enter integer", Toast.LENGTH_LONG);
            String defaultString = (String) newValue;
            try {
                int defaultInterval = Integer.parseInt(defaultString);
            } catch (NumberFormatException numberFormatException){
                toast.show();
            }
        }

        return false;
    }
}

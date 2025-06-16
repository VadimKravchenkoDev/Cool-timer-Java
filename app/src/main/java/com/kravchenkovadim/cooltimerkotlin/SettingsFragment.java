package com.kravchenkovadim.cooltimerkotlin;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Objects;

public class SettingsFragment  extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
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
                    editTextPreference.setSummary("Введіть секунди");
                } else {
                    editTextPreference.setSummary(value);
                }
            }
        }
    }
}

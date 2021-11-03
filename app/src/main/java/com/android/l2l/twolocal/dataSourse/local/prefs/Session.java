package com.android.l2l.twolocal.dataSourse.local.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

import javax.inject.Inject;


public class Session {
    private static final String SHAREDPREFS_NAME = "prefs";

    private final SharedPreferences preferences;

    @Inject
    public Session(Context context) {
        preferences = context.getSharedPreferences(SHAREDPREFS_NAME, Context.MODE_PRIVATE);
    }


    private SharedPreferences getPreferences() {
        return preferences;
    }

    private Editor getPreferencesEditor() {
        return preferences.edit();
    }


    public String getPreferenceValue(String key, String defValue) {
        return getPreferences().getString(key, defValue);
    }

    public int getPreferenceValue(String key, int defValue) {
        return getPreferences().getInt(key, defValue);
    }

    public long getPreferenceValue(String key, long defValue) {
        return getPreferences().getLong(key, defValue);
    }

    public boolean getPreferenceValue(String key, boolean defValue) {
        return getPreferences().getBoolean(key, defValue);
    }

    public void setPreferenceValue(String key, String prefsValue) {
        getPreferencesEditor().putString(key, prefsValue).apply();
    }

    public void setPreferenceValue(String key, int prefsValue) {
        getPreferencesEditor().putInt(key, prefsValue).apply();
    }

    public void setPreferenceValue(String key, long prefsValue) {
        getPreferencesEditor().putLong(key, prefsValue).apply();
    }

    public void setPreferenceValue(String key, boolean prefsValue) {
        getPreferencesEditor().putBoolean(key, prefsValue).apply();
    }

    public boolean containsPreferenceKey(String key) {
        return getPreferences().contains(key);
    }

    public <T> ArrayList<T> getArrayObject(String key, Class<T[]> generic) {
        Gson gson = new Gson();
        String json = getPreferences().getString(key, "");
        T[] list = gson.fromJson(json, generic);
        if (list != null)
            return new ArrayList<>(Arrays.asList(list));
        else
            return new ArrayList<>();
    }

    public <T> T getObject(String key, Class<T> generic) {
        Gson gson = new Gson();
        String json = getPreferences().getString(key, "");
        return gson.fromJson(json, generic);
    }

    public <T> T setObject(String key, T value) {
        Gson gson = new Gson();
        String json = gson.toJson(value);
        getPreferencesEditor().putString(key, json).apply();
        return value;
    }

    public void removePreferenceValue(String key) {
        getPreferencesEditor().remove(key).apply();
    }

    public void clear() {
        getPreferencesEditor().clear().apply();
    }


}


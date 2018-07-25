package com.bootleggers.dumpster.fragments;

import com.android.internal.logging.nano.MetricsProto;

import android.os.Bundle;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.UserHandle;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceGroup;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v14.preference.PreferenceFragment;
import android.support.v14.preference.SwitchPreference;
import android.provider.Settings;
import com.android.settings.R;

import java.util.Locale;
import android.text.TextUtils;
import android.view.View;

import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;
import android.util.Log;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

public class StatusBarSettings extends SettingsPreferenceFragment implements
        OnPreferenceChangeListener {

    private static final String PREF_STATUS_BAR_CLOCK = "status_bar_show_clock";
    private static final String PREF_CLOCK_SHOW_SECONDS = "status_bar_clock_seconds";
    private static final String BATTERY_STYLE = "battery_style";

    private SwitchPreference mStatusBarClock;
    private SwitchPreference mShowSeconds;
    private ListPreference mBatteryIconStyle;

    private ListPreference mLogoStyle;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        addPreferencesFromResource(R.xml.bootleg_dumpster_statusbar);

        PreferenceScreen prefSet = getPreferenceScreen();
        ContentResolver resolver = getActivity().getContentResolver();

        mLogoStyle = (ListPreference) findPreference("status_bar_logo_style");
        mLogoStyle.setOnPreferenceChangeListener(this);
        int logoStyle = Settings.System.getIntForUser(getContentResolver(),
                Settings.System.STATUS_BAR_LOGO_STYLE,
                0, UserHandle.USER_CURRENT);
        mLogoStyle.setValue(String.valueOf(logoStyle));
        mLogoStyle.setSummary(mLogoStyle.getEntry());

        mStatusBarClock = (SwitchPreference) findPreference(PREF_STATUS_BAR_CLOCK);
        mStatusBarClock.setChecked((Settings.System.getInt(
                getActivity().getApplicationContext().getContentResolver(),
                Settings.System.STATUS_BAR_CLOCK, 1) == 1));
        mStatusBarClock.setOnPreferenceChangeListener(this);

        mShowSeconds = (SwitchPreference) findPreference(PREF_CLOCK_SHOW_SECONDS);
        mShowSeconds.setChecked((Settings.System.getInt(
            getActivity().getApplicationContext().getContentResolver(),
                Settings.System.STATUS_BAR_CLOCK_SECONDS, 0) == 1));
        mShowSeconds.setOnPreferenceChangeListener(this);

        mBatteryIconStyle = (ListPreference) findPreference(BATTERY_STYLE);
        mBatteryIconStyle.setValue(Integer.toString(Settings.Secure.getInt(resolver,
                Settings.Secure.STATUS_BAR_BATTERY_STYLE, 0)));
        mBatteryIconStyle.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference.equals(mLogoStyle)) {
            int logoStyle = Integer.parseInt(((String) newValue).toString());
            Settings.System.putIntForUser(getContentResolver(),
                    Settings.System.STATUS_BAR_LOGO_STYLE, logoStyle, UserHandle.USER_CURRENT);
            int index = mLogoStyle.findIndexOfValue((String) newValue);
            mLogoStyle.setSummary(
                    mLogoStyle.getEntries()[index]);
            return true;
        } else if (preference == mStatusBarClock) {
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.STATUS_BAR_CLOCK,
                    (Boolean) newValue ? 1 : 0);
            return true;
        } else if (preference == mShowSeconds) {
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.STATUS_BAR_CLOCK_SECONDS,
                    (Boolean) newValue ? 1 : 0);
            return true;
        } else  if (preference == mBatteryIconStyle) {
            int value = Integer.valueOf((String) newValue);
            Settings.Secure.putInt(getContentResolver(),
                    Settings.Secure.STATUS_BAR_BATTERY_STYLE, value);
            return true;
        }
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.BOOTLEG;
    }

}

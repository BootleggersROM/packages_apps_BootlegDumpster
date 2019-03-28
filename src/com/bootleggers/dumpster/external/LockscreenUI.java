package com.bootleggers.dumpster.external;

import android.app.Activity;
import android.app.ActivityManagerNative;
import android.app.AlertDialog;
import android.app.Dialog; 
import android.app.DialogFragment; 
import android.content.Context;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.res.Resources;
import android.hardware.fingerprint.FingerprintManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.UserHandle;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceScreen;

import android.provider.Settings;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.bootleggers.dumpster.extra.Utils;
import com.bootleggers.dumpster.preferences.CustomSeekBarPreference;

import com.android.settings.SettingsPreferenceFragment;

import com.android.internal.logging.nano.MetricsProto;

public class LockscreenUI extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private static final String LOCK_CLOCK_FONTS = "lock_clock_fonts";
    private static final String LOCK_DATE_FONTS = "lock_date_fonts";
    private static final String LOCK_OWNER_FONTS = "lock_owner_fonts";
    private static final String WEATHER_UNIT = "weather_lockscreen_unit";

    ListPreference mLockClockFonts;
    ListPreference mLockDateFonts;
    ListPreference mLockOwnerFonts;
    ListPreference mWeatherUnit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.bootleg_external_lockscreenui);

        ContentResolver resolver = getActivity().getContentResolver();
        final PreferenceScreen prefScreen = getPreferenceScreen();
        Resources resources = getResources();

        mLockClockFonts = (ListPreference) findPreference(LOCK_CLOCK_FONTS);
        mLockClockFonts.setValue(String.valueOf(Settings.System.getInt(
                getContentResolver(), Settings.System.LOCK_CLOCK_FONTS, 21)));
        mLockClockFonts.setSummary(mLockClockFonts.getEntry());
        mLockClockFonts.setOnPreferenceChangeListener(this);

        mLockDateFonts = (ListPreference) findPreference(LOCK_DATE_FONTS);
        mLockDateFonts.setValue(String.valueOf(Settings.System.getInt(
                getContentResolver(), Settings.System.LOCK_DATE_FONTS, 8)));
        mLockDateFonts.setSummary(mLockDateFonts.getEntry());
        mLockDateFonts.setOnPreferenceChangeListener(this);

        mLockOwnerFonts = (ListPreference) findPreference(LOCK_OWNER_FONTS);
        if (Utils.isLockOwnerInfo(getActivity())) {
            mLockOwnerFonts.setValue(String.valueOf(Settings.System.getInt(
                    getContentResolver(), Settings.System.LOCK_OWNER_FONTS, 8)));
            mLockOwnerFonts.setSummary(mLockOwnerFonts.getEntry());
            mLockOwnerFonts.setOnPreferenceChangeListener(this);
        } else {
            mLockOwnerFonts.setSummary(R.string.no_no_summary_owner);
            mLockOwnerFonts.setEnabled(false);
        }

        mWeatherUnit = (ListPreference) findPreference(WEATHER_UNIT);
        mWeatherUnit.setValue(String.valueOf(Settings.System.getInt(
                getContentResolver(), Settings.System.WEATHER_LOCKSCREEN_UNIT, 0)));
        mWeatherUnit.setSummary(mWeatherUnit.getEntry());
        mWeatherUnit.setOnPreferenceChangeListener(this);
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getActivity().getContentResolver();
        if (preference == mLockClockFonts) {
            Settings.System.putInt(getContentResolver(), Settings.System.LOCK_CLOCK_FONTS,
                    Integer.valueOf((String) newValue));
            mLockClockFonts.setValue(String.valueOf(newValue));
            mLockClockFonts.setSummary(mLockClockFonts.getEntry());
            return true;
        } else if (preference == mLockDateFonts) {
            Settings.System.putInt(getContentResolver(), Settings.System.LOCK_DATE_FONTS,
                    Integer.valueOf((String) newValue));
            mLockDateFonts.setValue(String.valueOf(newValue));
            mLockDateFonts.setSummary(mLockDateFonts.getEntry());
            return true;
        } else if (preference == mLockOwnerFonts) {
            Settings.System.putInt(getContentResolver(), Settings.System.LOCK_OWNER_FONTS,
                    Integer.valueOf((String) newValue));
            mLockOwnerFonts.setValue(String.valueOf(newValue));
            mLockOwnerFonts.setSummary(mLockOwnerFonts.getEntry());
            return true;
        } else if (preference == mWeatherUnit) {
            Settings.System.putInt(getContentResolver(), Settings.System.WEATHER_LOCKSCREEN_UNIT,
                    Integer.valueOf((String) newValue));
            mWeatherUnit.setValue(String.valueOf(newValue));
            mWeatherUnit.setSummary(mWeatherUnit.getEntry());
            return true;
        }
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.BOOTLEG;
    }

}

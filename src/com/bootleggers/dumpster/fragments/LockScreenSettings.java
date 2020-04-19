/*
 *  Copyright (C) 2015 The OmniROM Project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.bootleggers.dumpster.fragments;

import com.android.internal.logging.nano.MetricsProto;

import android.app.Activity;
import android.content.Context;
import android.content.ContentResolver;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.res.Resources;
import android.hardware.fingerprint.FingerprintManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import androidx.preference.SwitchPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;

import android.provider.Settings;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.bootleggers.dumpster.extra.Utils;
import com.bootleggers.dumpster.preferences.CustomSeekBarPreference;
import com.bootleggers.dumpster.preferences.SystemSettingListPreference;
import com.bootleggers.dumpster.preferences.SystemSettingSwitchPreference; 

public class LockScreenSettings extends SettingsPreferenceFragment implements
    Preference.OnPreferenceChangeListener {

    private static final String KEY_LOCKSCREEN_MEDIA_FILTER = "lockscreen_album_art_filter";
    private static final String KEY_LOCKSCREEN_MEDIA_BLUR = "lockscreen_media_blur";
    private static final String KEY_LOCKSCREEN_WEATHER_ENABLED = "lockscreen_weather_enabled";
    private static final String KEY_LOCKSCREEN_WEATHER_STYLE = "lockscreen_weather_style";
    private static final String KEY_LOCKSCREEN_WEATHER_CITY = "lockscreen_weather_show_city";
    private static final String KEY_LOCKSCREEN_WEATHER_TEMP = "lockscreen_weather_show_temp";
    private static final String LOCK_CLOCK_FONT_STYLE = "lock_clock_font_style";
    private static final String LOCK_DATE_FONTS = "lock_date_fonts";

    private ListPreference mLockClockFonts;
    private ListPreference mLockDateFonts;
    private PreferenceCategory mLockscreenUI;
    private SystemSettingListPreference mLockscreenMediaFilter;
    private CustomSeekBarPreference mLockscreenMediaBlur;
    private SystemSettingSwitchPreference mFingerprintSuccess;
    private SystemSettingSwitchPreference mFingerprintUnlock;
    private SystemSettingSwitchPreference mLockscreenWeatherCity;
    private SystemSettingSwitchPreference mLockscreenWeatherTemp;
    private PreferenceCategory mLsMisc;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.bootleg_dumpster_lockscreen);

        ContentResolver resolver = getActivity().getContentResolver();
        final PreferenceScreen prefScreen = getPreferenceScreen();
        Resources resources = getResources();

        mLockscreenUI = (PreferenceCategory) findPreference("lockscreen_ui_stuff");

        mLockscreenMediaFilter = (SystemSettingListPreference) findPreference(KEY_LOCKSCREEN_MEDIA_FILTER);
        mLockscreenMediaBlur = (CustomSeekBarPreference) findPreference(KEY_LOCKSCREEN_MEDIA_BLUR);
        mLsMisc = (PreferenceCategory) findPreference("lock_misc_cat");
        mFingerprintSuccess = (SystemSettingSwitchPreference) findPreference("fingerprint_success_vib");
        mFingerprintUnlock = (SystemSettingSwitchPreference) findPreference("fp_unlock_keystore");
        mLockscreenWeatherCity = (SystemSettingSwitchPreference) findPreference(KEY_LOCKSCREEN_WEATHER_CITY);
        mLockscreenWeatherTemp = (SystemSettingSwitchPreference) findPreference(KEY_LOCKSCREEN_WEATHER_TEMP);

        if (!Utils.isDeviceWithFP(getActivity())) {
            mLsMisc.removePreference(mFingerprintSuccess);
            mLsMisc.removePreference(mFingerprintUnlock);
        }

        mLockscreenMediaFilter.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
        	@Override
        	public boolean onPreferenceChange(Preference preference, Object newVal) {
        		updatePrefsVisiblities();
        		return true;
        	}
    	});

        int value = Settings.System.getInt(getContentResolver(),
                Settings.System.LOCKSCREEN_MEDIA_BLUR, 250);
        mLockscreenMediaBlur.setValue(value);
        mLockscreenMediaBlur.setOnPreferenceChangeListener(this);
        updatePrefsVisiblities();
        mLockscreenWeatherTemp.setEnabled(isOmniWeatherEnabled());
        mLockscreenWeatherCity.setEnabled(isOmniWeatherEnabled());

        mLockClockFonts = (ListPreference) findPreference(LOCK_CLOCK_FONT_STYLE);
        mLockClockFonts.setValue(String.valueOf(Settings.System.getInt(
                getContentResolver(), Settings.System.LOCK_CLOCK_FONT_STYLE, 0)));
        mLockClockFonts.setSummary(mLockClockFonts.getEntry());
        mLockClockFonts.setOnPreferenceChangeListener(this);

        mLockDateFonts = (ListPreference) findPreference(LOCK_DATE_FONTS);
        mLockDateFonts.setValue(String.valueOf(Settings.System.getInt(
                getContentResolver(), Settings.System.LOCK_DATE_FONTS, 1)));
        mLockDateFonts.setSummary(mLockDateFonts.getEntry());
        mLockDateFonts.setOnPreferenceChangeListener(this);
    }

    private void updatePrefsVisiblities() {
        int lsFilterValue = Settings.System.getInt(getContentResolver(),
                Settings.System.LOCKSCREEN_ALBUM_ART_FILTER, 5);
        mLockscreenMediaBlur.setEnabled(lsFilterValue > 2);
    }

    private boolean isOmniWeatherEnabled() {
        boolean isWeatherEnabled = Settings.System.getInt(getContentResolver(),
                Settings.System.OMNI_LOCKSCREEN_WEATHER_ENABLED, 1) == 1;
        boolean isPixelStyleEnabled = Settings.System.getInt(getContentResolver(),
                Settings.System.LOCKSCREEN_WEATHER_STYLE, 1) == 1;
        return isWeatherEnabled && !isPixelStyleEnabled;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getActivity().getContentResolver();
        switch (preference.getKey()) {
            case KEY_LOCKSCREEN_MEDIA_BLUR:
                int value = (Integer) newValue;
                Settings.System.putInt(getContentResolver(),
                        Settings.System.LOCKSCREEN_MEDIA_BLUR, value);
                return true;
            case KEY_LOCKSCREEN_WEATHER_ENABLED:
            case KEY_LOCKSCREEN_WEATHER_STYLE:
                mLockscreenWeatherTemp.setEnabled(isOmniWeatherEnabled());
                mLockscreenWeatherCity.setEnabled(isOmniWeatherEnabled());
                return true;
            case LOCK_CLOCK_FONT_STYLE:
                Settings.System.putInt(getContentResolver(), Settings.System.LOCK_CLOCK_FONT_STYLE,
                        Integer.valueOf((String) newValue));
                mLockClockFonts.setValue(String.valueOf(newValue));
                mLockClockFonts.setSummary(mLockClockFonts.getEntry());
                return true;
            case LOCK_DATE_FONTS:
                Settings.System.putInt(getContentResolver(), Settings.System.LOCK_DATE_FONTS,
                        Integer.valueOf((String) newValue));
                mLockDateFonts.setValue(String.valueOf(newValue));
                mLockDateFonts.setSummary(mLockDateFonts.getEntry());
                return true;
            default:
                return false;
        }
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.BOOTLEG;
    }

}
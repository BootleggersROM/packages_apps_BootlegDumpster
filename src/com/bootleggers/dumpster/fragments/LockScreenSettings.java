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
import com.bootleggers.dumpster.preferences.CustomSeekBarPreference;
import com.bootleggers.dumpster.preferences.SystemSettingListPreference;

public class LockScreenSettings extends SettingsPreferenceFragment implements
    Preference.OnPreferenceChangeListener {

    private static final String KEY_LOCKSCREEN_MEDIA_FILTER = "lockscreen_album_art_filter";
    private static final String KEY_LOCKSCREEN_MEDIA_BLUR = "lockscreen_media_blur";

    private PreferenceCategory mLockscreenUI;
    private SystemSettingListPreference mLockscreenMediaFilter;
    private CustomSeekBarPreference mLockscreenMediaBlur;

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

        mLockscreenMediaFilter.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
        	@Override
        	public boolean onPreferenceChange(Preference preference, Object newVal) {
        		updatePrefsVisiblities();
        		return true;
        	}
    	});

        int value = Settings.System.getInt(getContentResolver(),
                Settings.System.LOCKSCREEN_MEDIA_BLUR, 2500);
        mLockscreenMediaBlur.setValue(value);
        mLockscreenMediaBlur.setOnPreferenceChangeListener(this);
        updatePrefsVisiblities();
    }

    private void updatePrefsVisiblities() {
        int lsFilterValue = Settings.System.getInt(getContentResolver(),
                Settings.System.LOCKSCREEN_ALBUM_ART_FILTER, 5);
        mLockscreenMediaBlur.setEnabled(lsFilterValue > 2);
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
            default:
                return false;
        }
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.BOOTLEG;
    }

}
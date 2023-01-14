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
import androidx.preference.SwitchPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;

import android.provider.Settings;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import com.bootleggers.support.preferences.SystemSettingListPreference;

import java.lang.StringBuilder;

public class LockScreenSettings extends SettingsPreferenceFragment implements
        OnPreferenceChangeListener {

        private static final String UDFPS_CATEGORY = "udfps_category";
        private static final String SHORTCUT_START_KEY = "lockscreen_shortcut_start";
    	private static final String SHORTCUT_END_KEY = "lockscreen_shortcut_end";

    	private static final String[] DEFAULT_START_SHORTCUT = new String[] { "home", "flashlight" };
    	private static final String[] DEFAULT_END_SHORTCUT = new String[] { "wallet", "qr", "camera" };
    	
    	SystemSettingListPreference mStartShortcut;
    	SystemSettingListPreference mEndShortcut;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.bootleg_dumpster_frag_lockscreen);

        ContentResolver resolver = getActivity().getContentResolver();
        final PreferenceScreen prefScreen = getPreferenceScreen();
        Resources resources = getResources();

        mStartShortcut = findPreference(SHORTCUT_START_KEY);
        mEndShortcut = findPreference(SHORTCUT_END_KEY);
        updateShortcutSelection();
        mStartShortcut.setOnPreferenceChangeListener(this);
        mEndShortcut.setOnPreferenceChangeListener(this);

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getActivity().getContentResolver();
        switch (preference.getKey()) {
            default:
                return false;
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        updateShortcutSelection();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {
        if (preference == mStartShortcut) {
            setShortcutSelection((String) objValue, true);
            return true;
        } else if (preference == mEndShortcut) {
            setShortcutSelection((String) objValue, false);
            return true;
        }
        return false;
    }
    
    private String getSettingsShortcutValue() {
        String value = Settings.System.getString(getActivity().getContentResolver(),
                Settings.System.KEYGUARD_QUICK_TOGGLES);
        if (value == null || value.isEmpty()) {
            StringBuilder sb = new StringBuilder(DEFAULT_START_SHORTCUT[0]);
            for (int i = 1; i < DEFAULT_START_SHORTCUT.length; i++) {
                sb.append("," + DEFAULT_START_SHORTCUT[i]);
            }
            sb.append(";" + DEFAULT_END_SHORTCUT[0]);
            for (int i = 1; i < DEFAULT_END_SHORTCUT.length; i++) {
                sb.append("," + DEFAULT_END_SHORTCUT[i]);
            }
            value = sb.toString();
        }
        return value;
    }

    private void updateShortcutSelection() {
        final String value = getSettingsShortcutValue();
        final String[] split = value.split(";");
        mStartShortcut.setValue(split[0].split(",")[0]);
        mStartShortcut.setSummary(mStartShortcut.getEntry());
        mEndShortcut.setValue(split[1].split(",")[0]);
        mEndShortcut.setSummary(mEndShortcut.getEntry());
    }

    private void setShortcutSelection(String value, boolean start) {
        final String oldValue = getSettingsShortcutValue();
        final int splitIndex = start ? 0 : 1;
        String[] split = oldValue.split(";");
        if (value.equals("none")) {
            split[splitIndex] = "none";
        } else {
            StringBuilder sb = new StringBuilder(value);
            final String[] def = start ? DEFAULT_START_SHORTCUT : DEFAULT_END_SHORTCUT;
            for (String str : def) {
                if (str.equals(value)) continue;
                sb.append("," + str);
            }
            split[splitIndex] = sb.toString();
        }
        Settings.System.putString(getActivity().getContentResolver(),
                Settings.System.KEYGUARD_QUICK_TOGGLES, split[0] + ";" + split[1]);

        if (start) {
            mStartShortcut.setValue(value);
            mStartShortcut.setSummary(mStartShortcut.getEntry());
        } else {
            mEndShortcut.setValue(value);
            mEndShortcut.setSummary(mEndShortcut.getEntry());
        }
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.BOOTLEG;
    }

}

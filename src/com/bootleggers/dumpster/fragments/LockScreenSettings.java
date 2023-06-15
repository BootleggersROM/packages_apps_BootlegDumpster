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
import android.os.UserHandle;
import androidx.preference.SwitchPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.SwitchPreference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;

import android.provider.Settings;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.internal.util.bootleg.udfps.UdfpsUtils;

import com.bootleggers.support.preferences.SystemSettingListPreference;

import java.lang.StringBuilder;

public class LockScreenSettings extends SettingsPreferenceFragment implements
        OnPreferenceChangeListener {

        private static final String UDFPS_CATEGORY = "udfps_category";
        private static final String SHORTCUT_START_KEY = "lockscreen_shortcut_start";
    	private static final String SHORTCUT_END_KEY = "lockscreen_shortcut_end";

    	private static final String[] DEFAULT_START_SHORTCUT = new String[] { "home", "flashlight" };
    	private static final String[] DEFAULT_END_SHORTCUT = new String[] { "wallet", "qr_code_scanner", "camera" };
    	private static final String SHORTCUT_ENFORCE_KEY = "lockscreen_shortcut_enforce";

        private SystemSettingListPreference mStartShortcut;
    	private SystemSettingListPreference mEndShortcut;
    	private SwitchPreference mEnforceShortcut;
        private PreferenceCategory mUdfpsCategory;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.bootleg_dumpster_frag_lockscreen);

        ContentResolver resolver = getActivity().getContentResolver();
        final PreferenceScreen prefScreen = getPreferenceScreen();
        Resources resources = getResources();

        mUdfpsCategory = findPreference(UDFPS_CATEGORY);
        if (!UdfpsUtils.hasUdfpsSupport(getContext())) {
            prefScreen.removePreference(mUdfpsCategory);
        }

        mStartShortcut = findPreference(SHORTCUT_START_KEY);
        mEndShortcut = findPreference(SHORTCUT_END_KEY);
        mEnforceShortcut = findPreference(SHORTCUT_ENFORCE_KEY);
        updateShortcutSelection();
        mStartShortcut.setOnPreferenceChangeListener(this);
        mEndShortcut.setOnPreferenceChangeListener(this);
        mEnforceShortcut.setOnPreferenceChangeListener(this);

    }
        public boolean onPreferenceChange(Preference preference, Object objValue) {
        ContentResolver resolver = getActivity().getContentResolver();
        if (preference == mStartShortcut) {
            setShortcutSelection((String) objValue, true);
            return true;
        } else if (preference == mEndShortcut) {
            setShortcutSelection((String) objValue, false);
            return true;
        } else if (preference == mEnforceShortcut) {
            final boolean value = (Boolean) objValue;
            setShortcutSelection(mStartShortcut.getValue(), true, value);
            setShortcutSelection(mEndShortcut.getValue(), false, value);
            return true; 
        }
        return false;
    }
    
    @Override
    public void onResume() {
        super.onResume();
        updateShortcutSelection();
    }

    public static void reset(Context mContext) {
        ContentResolver resolver = mContext.getContentResolver();
         Settings.Secure.putIntForUser(resolver,
                Settings.Secure.DOZE_ON_CHARGE, 0, UserHandle.USER_CURRENT);
    }
    
    private String getSettingsShortcutValue() {
        String value = Settings.System.getString(getActivity().getContentResolver(),
                Settings.System.KEYGUARD_QUICK_TOGGLES_NEW);
        if (value == null || value.isEmpty()) {
            StringBuilder sb = new StringBuilder(DEFAULT_START_SHORTCUT[0]);
            for (int i = 1; i < DEFAULT_START_SHORTCUT.length; i++) {
                sb.append(",").append(DEFAULT_START_SHORTCUT[i]);
            }
            sb.append(";" + DEFAULT_END_SHORTCUT[0]);
            for (int i = 1; i < DEFAULT_END_SHORTCUT.length; i++) {
                sb.append(",").append(DEFAULT_END_SHORTCUT[i]);
            }
            value = sb.toString();
        }
        return value;
    }

    private void updateShortcutSelection() {
        final String value = getSettingsShortcutValue();
        final String[] split = value.split(";");
        final String[] start = split[0].split(",");
        final String[] end = split[1].split(",");
        mStartShortcut.setValue(start[0]);
        mStartShortcut.setSummary(mStartShortcut.getEntry());
        mEndShortcut.setValue(end[0]);
        mEndShortcut.setSummary(mEndShortcut.getEntry());
        mEnforceShortcut.setChecked(start.length == 1 && end.length == 1);
    }

    private void setShortcutSelection(String value, boolean start) {
        setShortcutSelection(value, start, mEnforceShortcut.isChecked());
    }

    private void setShortcutSelection(String value, boolean start, boolean single) {
        final String oldValue = getSettingsShortcutValue();
        final int splitIndex = start ? 0 : 1;
        String[] split = oldValue.split(";");
        if (value.equals("none") || single) {
            split[splitIndex] = value;
        } else {
            StringBuilder sb = new StringBuilder(value);
            final String[] def = start ? DEFAULT_START_SHORTCUT : DEFAULT_END_SHORTCUT;
            for (String str : def) {
                if (str.equals(value)) continue;
                sb.append(",").append(str);
            }
            split[splitIndex] = sb.toString();
        }
        Settings.System.putString(getActivity().getContentResolver(),
                Settings.System.KEYGUARD_QUICK_TOGGLES_NEW, split[0] + ";" + split[1]);

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

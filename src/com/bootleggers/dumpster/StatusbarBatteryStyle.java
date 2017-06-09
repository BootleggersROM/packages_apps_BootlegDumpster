/*
 *  Copyright (C) 2016 Dirty Unicorns
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
package com.bootleggers.dumpster;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceGroup;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.PreferenceCategory;
import android.support.v14.preference.PreferenceFragment;
import android.support.v14.preference.SwitchPreference;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.Utils;

import net.margaritov.preference.colorpicker.ColorPickerPreference;

public class StatusbarBatteryStyle extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {
    private static final String TAG = "StatusbarBatteryStyle";

    private static final String STATUS_BAR_BATTERY_SAVER_COLOR = "status_bar_battery_saver_color";

    private ColorPickerPreference mBatterySaverColor;

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.DUMPSTER;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.statusbar_battery_style);

        ContentResolver resolver = getActivity().getContentResolver();

        int batterySaverColor = Settings.Secure.getInt(resolver,
                Settings.Secure.STATUS_BAR_BATTERY_SAVER_COLOR, 0xfff4511e);
        mBatterySaverColor = (ColorPickerPreference) findPreference("status_bar_battery_saver_color");
        mBatterySaverColor.setNewPreviewColor(batterySaverColor);
        mBatterySaverColor.setOnPreferenceChangeListener(this);

        enableStatusBarBatteryDependents();
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        // If we didn't handle it, let preferences handle it.
        return super.onPreferenceTreeClick(preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getActivity().getContentResolver();
        if (preference.equals(mBatterySaverColor)) {
            int color = ((Integer) newValue).intValue();
            Settings.Secure.putInt(resolver,
                    Settings.Secure.STATUS_BAR_BATTERY_SAVER_COLOR, color);
            return true;
        }
        return false;
    }

    private void enableStatusBarBatteryDependents() {
        mBatterySaverColor.setEnabled(true);
    }
}

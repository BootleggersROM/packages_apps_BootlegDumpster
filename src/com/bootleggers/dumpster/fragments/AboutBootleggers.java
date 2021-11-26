/*
 *  Copyright (C) 2016 The Dirty Unicorns Project
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

import android.content.ContentResolver;
import android.content.res.Resources;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.Vibrator;
import androidx.preference.PreferenceCategory;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.SwitchPreference;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import com.android.settings.R;

import com.android.settings.SettingsPreferenceFragment;
import com.android.settingslib.widget.FooterPreference;

import com.android.internal.logging.nano.MetricsProto;

import android.os.SystemProperties;

import java.util.Random;

public class AboutBootleggers extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener{
    
    private static final String BTLG_ROM_SHARE = "bootleg_sharemsg";
    private static final String TAG = "AboutBootleggers";
    private static final String KEY_TESTING_BUILD_ALERT = "footer_alert_testbuild";
    private Preference prefThanks;
    private PreferenceCategory prefBootlegInfo;
    private FooterPreference mFooterPreference;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        final PreferenceScreen prefScreen = getPreferenceScreen();
        Resources res = getResources();

        addPreferencesFromResource(R.xml.bootleg_dumpster_frag_about);

        /** All main stuff we'll declare to check on build.prop **/

        String bootlegMaintainer = SystemProperties.get("ro.bootleggers.maintainer","Aidonnou");
        String bootlegCodename = SystemProperties.get("ro.bootleggers.device","dedvice");
        String bootlegVersion = SystemProperties.get("ro.bootleggers.version_number","DeadAndGone");
        String bootlegShort = SystemProperties.get("ro.bootleggers.buildshort","Bruhleggers");
        String bootlegFMaintainer;

        /** preference listing **/
        prefThanks = (Preference) findPreference("bootleg_thanku");
        mFooterPreference = (FooterPreference) findPreference(KEY_TESTING_BUILD_ALERT);

        // Check if maintainer name isn't aidonnou, then it will show it
        if (bootlegMaintainer.equalsIgnoreCase("Aidonnou") || bootlegMaintainer.equalsIgnoreCase(null)) {
            bootlegFMaintainer = res.getString(R.string.bootleg_ab_summary_thanksclean);
        } else {
            bootlegFMaintainer = res.getString(R.string.bootleg_ab_summary_thankssec, bootlegMaintainer, bootlegCodename);
        }

        // Add the new preference summary
        prefThanks.setSummary(String.valueOf(bootlegFMaintainer));

        /* Show a nice footer message about Mad Stinky builds*/
        mFooterPreference.setVisible(bootlegVersion.contains("MadStinky") || bootlegShort.contains("Testing"));
    }


    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getActivity().getContentResolver();

        return false;
    }

    //Code of this taken from RR Share option, thanks to them, they got fully credit of it.

    public boolean onPreferenceTreeClick(Preference preference) {
        
        final PreferenceScreen prefScreen = getPreferenceScreen();


        if (preference.getKey().equals(BTLG_ROM_SHARE)) {
            final int min = 1;
            final int max = 7;
            Random rand = new Random();
            final int msgNumb = rand.nextInt((max - min) + 1) + min;
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("text/plain");
            if (msgNumb == 1) {
              intent.putExtra(Intent.EXTRA_TEXT, String.format(
              getActivity().getString(R.string.bootleg_sharemsg1)));
            } else if (msgNumb == 2) {
              intent.putExtra(Intent.EXTRA_TEXT, String.format(
              getActivity().getString(R.string.bootleg_sharemsg2)));
            } else if (msgNumb == 3) {
              intent.putExtra(Intent.EXTRA_TEXT, String.format(
              getActivity().getString(R.string.bootleg_sharemsg3)));
            } else if (msgNumb == 4) {
              intent.putExtra(Intent.EXTRA_TEXT, String.format(
              getActivity().getString(R.string.bootleg_sharemsg4)));
            } else if (msgNumb == 5) {
              intent.putExtra(Intent.EXTRA_TEXT, String.format(
              getActivity().getString(R.string.bootleg_sharemsg5)));
            } else if (msgNumb == 6) {
              intent.putExtra(Intent.EXTRA_TEXT, String.format(
              getActivity().getString(R.string.bootleg_sharemsg6)));
            } else if (msgNumb == 7) {
              intent.putExtra(Intent.EXTRA_TEXT, String.format(
              getActivity().getString(R.string.bootleg_sharemsg7)));
            } else {
              intent.putExtra(Intent.EXTRA_TEXT, String.format(
              getActivity().getString(R.string.bootleg_sharemsg6)));
            }

            startActivity(Intent.createChooser(intent, getActivity().getString(R.string.share_chooser_title)));
            } else {
                // If not handled, let preferences handle it.
                return super.onPreferenceTreeClick(preference);
        }
        return true;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.BOOTLEG;
    }

}

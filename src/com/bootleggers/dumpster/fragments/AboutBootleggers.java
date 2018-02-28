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
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v14.preference.SwitchPreference;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import com.android.settings.R;

import com.android.settings.SettingsPreferenceFragment;

import com.android.internal.logging.nano.MetricsProto;

import android.os.SystemProperties;

import java.util.Random;

public class AboutBootleggers extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener{
    
    private static final String BTLG_ROM_SHARE = "bootleg_sharemsg";
    private static final String BTLG_ROM_MUSICODE = "bootleg_aboutmusicode";
    private static final String TAG = "AboutBootleggers";
    private Preference prefThanks;
    private Preference prefBuild;
    private Preference prefMusicode;
    private PreferenceCategory prefBootlegInfo;
    private Preference prefBootlegOTA;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        final PreferenceScreen prefScreen = getPreferenceScreen();
        Resources res = getResources();

        addPreferencesFromResource(R.xml.bootleg_dumpster_about);

        /** Commented out due to not working **/

        String bootlegMaintainer = SystemProperties.get("ro.bootleg.maintainer","Aidonnou");
        String bootlegCodename = SystemProperties.get("ro.bootleg.device","generic");
        String bootlegBuildtype = SystemProperties.get("ro.bootleg.buildtype","Unshishufied");
        String bootlegMusicode = SystemProperties.get("ro.bootleg.songcodename","Sample");
        String bootlegBuildMeaning = "Aidonnou";
        String bootlegFMaintainer;
        String bootlegNoMaintainer = res.getString(R.string.bootleg_ab_summary_whoami_unknown);

        /** preference listing **/
        prefThanks = (Preference) findPreference("bootleg_thanku");
        prefBuild = (Preference) findPreference("bootleg_aboutbuild");
        prefMusicode = (Preference) findPreference("bootleg_aboutmusicode");
        prefBootlegInfo = (PreferenceCategory) findPreference("pref_bootleg_romstuff");
        prefBootlegOTA = (Preference) findPreference("update_settings");
        
        /*This will check about the build info stuff*/
        
        if (bootlegBuildtype.equalsIgnoreCase("Unshishufied")) {
            bootlegBuildMeaning = "Unofficial";
            prefBootlegInfo.removePreference(prefBootlegOTA);
        } else if (bootlegBuildtype.equalsIgnoreCase("Shishufied")) {
            bootlegBuildMeaning = "Official";
        } else if (bootlegBuildtype.equalsIgnoreCase("Shishu")) {
            bootlegBuildMeaning = "Full of trash";
        } else {
            prefBootlegInfo.removePreference(prefBootlegOTA);
            bootlegBuildMeaning = "Aidonnou";
        }

        // Check if maintainer name isn't aidonnou, then it will show it
        if (bootlegMaintainer.equalsIgnoreCase("Aidonnou") || bootlegMaintainer.equalsIgnoreCase(null)) {
            bootlegFMaintainer = res.getString(R.string.bootleg_ab_summary_thankssec, bootlegNoMaintainer, bootlegCodename);
        } else {
            bootlegFMaintainer = res.getString(R.string.bootleg_ab_summary_thankssec, bootlegMaintainer, bootlegCodename);
        }

        String bootlegFBuildtype = res.getString(R.string.bootleg_ri_summary_buildtype, bootlegBuildtype, bootlegBuildMeaning);
        String bootlegFMusical = res.getString(R.string.bootleg_ri_summary_musicode, bootlegMusicode);

        // Add the new preference summary
        prefThanks.setSummary(String.valueOf(bootlegFMaintainer));
        prefBuild.setSummary(String.valueOf(bootlegFBuildtype));
        prefMusicode.setSummary(String.valueOf(bootlegFMusical));
    }


    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getActivity().getContentResolver();

        return false;
    }

    //Code of this taken from RR Share option, thanks to them, they got fully credit of it.

    public boolean onPreferenceTreeClick(Preference preference) {
        
        final PreferenceScreen prefScreen = getPreferenceScreen();
        String bootlegMusicodeURL = SystemProperties.get("ro.bootleg.songcodeurl","https://google.com");
        prefMusicode = (Preference) findPreference("bootleg_aboutmusicode");


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
            } else if (preference.getKey().equals(BTLG_ROM_MUSICODE)) {
                if (String.valueOf(bootlegMusicodeURL) != null) {
                    Intent intentSongReference = new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(bootlegMusicodeURL)));
                    startActivity(intentSongReference);
                }
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

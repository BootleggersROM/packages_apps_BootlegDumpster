/*
 * Copyright (C) 2016 The Pure Nexus Project
 * used for Nitrogen OS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bootleggers.dumpster;

import com.android.internal.logging.nano.MetricsProto;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.view.Surface;
import com.android.settings.R;

import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;

import com.android.settings.SettingsPreferenceFragment;
import com.bootleggers.dumpster.extra.Utils;

public class MainDump extends SettingsPreferenceFragment {

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        addPreferencesFromResource(R.xml.bootleg_dumpster_frag_main);
        Resources mRes = getResources();

        // Possible packages to start the custom activity
        String[] deviceExtrasIntent = {
            "com.android.settings.action.EXTRA_SETTINGS",
            "android.intent.action.MAIN",
            "android.intent.action.MAIN",
            "android.intent.action.MAIN"
        };
        String[] deviceExtrasPackages = {
            "com.dirtyunicorns.settings.device",
            "org.omnirom.device",
            "org.lineageos.settings.device",
            getResources().getString(R.string.device_extras_package_name)
        };
        String[] deviceExtrasActivities = {
            "com.dirtyunicorns.settings.device.TouchscreenGestureSettings",
            "org.omnirom.device.DeviceSettingsActivity",
            "org.lineageos.settings.device.DeviceSettingsActivity",
            getResources().getString(R.string.device_extras_activity_name)
        };

        // Setting up the preference categories
        PreferenceCategory catInterface = (PreferenceCategory) findPreference("cat_interface");
        PreferenceCategory catTweaks = (PreferenceCategory) findPreference("cat_tweaks");
        PreferenceCategory catButtons = (PreferenceCategory) findPreference("cat_buttons");
        PreferenceCategory catMisc = (PreferenceCategory) findPreference("cat_misc");

        // Setting up all the prefs
        Preference statusBarPref = catInterface.findPreference("status_bar_category");
        Preference quickSettingsPref = catInterface.findPreference("quick_settings_category");
        Preference notificationsPref = catInterface.findPreference("notifications_category");
        Preference lockscreenPref = catInterface.findPreference("lockscreen_category");
        Preference recentsPref = catTweaks.findPreference("recents_category");
        Preference animationsPref = catTweaks.findPreference("animations_category");
        Preference gesturesPref = catTweaks.findPreference("gestures_category");
        Preference volumePref = catButtons.findPreference("volume_category");
        Preference powerMenuPref = catButtons.findPreference("powermenu_category");
        Preference navbarPref = catButtons.findPreference("navbar_category");
        Preference hwKeysPref = catButtons.findPreference("hwkeys_category");
        Preference fingerprintPref = catButtons.findPreference("fingerprint_category");
        Preference appRelatedPref = catMisc.findPreference("app_related_category");
        Preference deviceExtras = catMisc.findPreference("device_extras_category");
        Preference systemPref = catMisc.findPreference("system_category");

        // Setting up the availability of those
        if (catInterface != null) {
            if (!mRes.getBoolean(R.bool.has_statusbar_options) &&
                    !mRes.getBoolean(R.bool.has_quicksettings_options) &&
                    !mRes.getBoolean(R.bool.has_notifications_options) &&
                    !mRes.getBoolean(R.bool.has_lockscreen_options)) {
                removePreference("cat_interface");
            } else {
                if (!mRes.getBoolean(R.bool.has_statusbar_options)) catInterface.removePreference(statusBarPref);
                if (!mRes.getBoolean(R.bool.has_quicksettings_options)) catInterface.removePreference(quickSettingsPref);
                if (!mRes.getBoolean(R.bool.has_notifications_options)) catInterface.removePreference(notificationsPref);
                if (!mRes.getBoolean(R.bool.has_lockscreen_options)) catInterface.removePreference(lockscreenPref);
            }
        }
        if (catTweaks != null) {
            if (!mRes.getBoolean(R.bool.has_recents_options) &&
                    !mRes.getBoolean(R.bool.has_animations_options) &&
                    !mRes.getBoolean(R.bool.has_gestures_options)) {
                removePreference("cat_tweaks");
            } else {
                if (!mRes.getBoolean(R.bool.has_recents_options)) catTweaks.removePreference(recentsPref);
                if (!mRes.getBoolean(R.bool.has_animations_options)) catTweaks.removePreference(animationsPref);
                if (!mRes.getBoolean(R.bool.has_gestures_options)) catTweaks.removePreference(gesturesPref);
            }
        }
        if (catButtons != null) {
            if (!mRes.getBoolean(R.bool.has_volume_options) &&
                    !mRes.getBoolean(R.bool.has_powermenu_options) &&
                    !mRes.getBoolean(R.bool.has_navbar_options) &&
                    !mRes.getBoolean(R.bool.has_hwkeys_options) &&
                    !mRes.getBoolean(R.bool.has_fingerprint_options)) {
                removePreference("cat_buttons");
            } else {
                if (!mRes.getBoolean(R.bool.has_volume_options)) catButtons.removePreference(volumePref);
                if (!mRes.getBoolean(R.bool.has_powermenu_options)) catButtons.removePreference(powerMenuPref);
                if (!mRes.getBoolean(R.bool.has_navbar_options)) catButtons.removePreference(navbarPref);
                if (!mRes.getBoolean(R.bool.has_hwkeys_options)) catButtons.removePreference(hwKeysPref);
                if (!mRes.getBoolean(R.bool.has_fingerprint_options)) catButtons.removePreference(fingerprintPref);
            }
        }
        if (catMisc != null) {
            if (!mRes.getBoolean(R.bool.has_app_related_options)) catMisc.removePreference(appRelatedPref);
            if (!mRes.getBoolean(R.bool.has_system_options)) catMisc.removePreference(systemPref);
            boolean hasDeviceExtras = false;
            for (int i = 0; i < deviceExtrasPackages.length; i++) {
                if (hasDeviceExtras) return;

                String pkg = deviceExtrasPackages[i];
                if (pkg == null || pkg == "") return;
                if (Utils.isPackageInstalled(getActivity(), pkg)) {
                    Intent devExtrasIntent = new Intent(deviceExtrasIntent[i]);
                    devExtrasIntent.setClassName(pkg, deviceExtrasActivities[i]);
                    deviceExtras.setIntent(devExtrasIntent);

                    String extrasAppName = Utils.getPackageLabel(getActivity(), pkg);
                    if (extrasAppName != null) {
                        deviceExtras.setTitle(extrasAppName);
                    }
                    String devModel = SystemProperties.get("ro.product.model", null);
                    if (devModel != null) {
                        deviceExtras.setSummary(mRes.getString(
                                R.string.device_extras_summary, devModel));
                    }
                    hasDeviceExtras = true;
                } else {
                    hasDeviceExtras = false;
                }
            }
            if (!hasDeviceExtras) {
                catMisc.removePreference(deviceExtras);
            }
        }
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.BOOTLEG;
    }

    public static void lockCurrentOrientation(Activity activity) {
        int currentRotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int orientation = activity.getResources().getConfiguration().orientation;
        int frozenRotation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
        switch (currentRotation) {
            case Surface.ROTATION_0:
                frozenRotation = orientation == Configuration.ORIENTATION_LANDSCAPE
                        ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                        : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                break;
            case Surface.ROTATION_90:
                frozenRotation = orientation == Configuration.ORIENTATION_PORTRAIT
                        ? ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
                        : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                break;
            case Surface.ROTATION_180:
                frozenRotation = orientation == Configuration.ORIENTATION_LANDSCAPE
                        ? ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
                        : ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                break;
            case Surface.ROTATION_270:
                frozenRotation = orientation == Configuration.ORIENTATION_PORTRAIT
                        ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                        : ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                break;
        }
        activity.setRequestedOrientation(frozenRotation);
    }
}

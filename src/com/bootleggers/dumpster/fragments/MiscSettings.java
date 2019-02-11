package com.bootleggers.dumpster.fragments;

import com.android.internal.logging.nano.MetricsProto;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v14.preference.SwitchPreference;
import android.text.TextUtils;
import android.provider.Settings;
import com.android.settings.R;
import com.bootleggers.dumpster.extra.Utils;
import com.bootleggers.dumpster.preferences.AppMultiSelectListPreference;
import com.bootleggers.dumpster.preferences.ScrollAppsViewPreference;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import com.android.settings.SettingsPreferenceFragment;

public class MiscSettings extends SettingsPreferenceFragment implements
        OnPreferenceChangeListener {

    private static final String DEVICE_CATEGORY = "device_extras_category";
    private static final String DEVICE_OMNI_CATEGORY = "device_extras_omni_category";
    private static final String DEVICE_OMNI_PACKAGE = "org.omnirom.device";
    private static final String KEY_ASPECT_RATIO_APPS_ENABLED = "aspect_ratio_apps_enabled";
    private static final String KEY_ASPECT_RATIO_APPS_LIST = "aspect_ratio_apps_list";
    private static final String KEY_ASPECT_RATIO_CATEGORY = "aspect_ratio_category";
    private static final String KEY_ASPECT_RATIO_APPS_LIST_SCROLLER = "aspect_ratio_apps_list_scroller";
    private static final String FLASHLIGHT_ON_CALL = "flashlight_on_call";
    private static final String CALL_SETTINGS_OPTIONS = "call_features";

    private AppMultiSelectListPreference mAspectRatioAppsSelect;
    private ScrollAppsViewPreference mAspectRatioApps;
    private ListPreference mFlashlightOnCall;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        final PreferenceScreen prefSet = getPreferenceScreen();
        addPreferencesFromResource(R.xml.bootleg_dumpster_misc);

        PreferenceCategory overallPreferences = (PreferenceCategory) findPreference("misc_overall_cat");

        Preference DeviceExtras = findPreference(DEVICE_CATEGORY);
        if (!getResources().getBoolean(R.bool.has_device_extras)) {
            overallPreferences.removePreference(DeviceExtras);
        }

        if (!Utils.isPackageInstalled(getActivity(), DEVICE_OMNI_PACKAGE)) {
            overallPreferences.removePreference(findPreference(DEVICE_OMNI_CATEGORY));
        }

        final PreferenceCategory aspectRatioCategory =
                (PreferenceCategory) getPreferenceScreen().findPreference(KEY_ASPECT_RATIO_CATEGORY);
        final boolean supportMaxAspectRatio = getResources().getBoolean(com.android.internal.R.bool.config_haveHigherAspectRatioScreen);
        if (!supportMaxAspectRatio) {
            getPreferenceScreen().removePreference(aspectRatioCategory);
        } else {
        mAspectRatioAppsSelect = (AppMultiSelectListPreference) findPreference(KEY_ASPECT_RATIO_APPS_LIST);
        mAspectRatioApps = (ScrollAppsViewPreference) findPreference(KEY_ASPECT_RATIO_APPS_LIST_SCROLLER);
        final String valuesString = Settings.System.getString(getContentResolver(), Settings.System.OMNI_ASPECT_RATIO_APPS_LIST);
        List<String> valuesList = new ArrayList<String>();
        if (!TextUtils.isEmpty(valuesString)) {
             valuesList.addAll(Arrays.asList(valuesString.split(":")));
             mAspectRatioApps.setVisible(true);
             mAspectRatioApps.setValues(valuesList);
        } else {
             mAspectRatioApps.setVisible(false);
        }
        mAspectRatioAppsSelect.setValues(valuesList);
        mAspectRatioAppsSelect.setOnPreferenceChangeListener(this);
        }

        PreferenceScreen prefScreen = getPreferenceScreen();
        PreferenceCategory callSettings = (PreferenceCategory) findPreference(CALL_SETTINGS_OPTIONS);

        mFlashlightOnCall = (ListPreference) findPreference(FLASHLIGHT_ON_CALL);
        Preference FlashOnCall = findPreference("flashlight_on_call");
        if (!Utils.deviceSupportsFlashLight(getActivity())) {
            callSettings.removePreference(FlashOnCall);
        } else {
        int flashlightValue = Settings.System.getInt(getContentResolver(),
                Settings.System.FLASHLIGHT_ON_CALL, 0);
        mFlashlightOnCall.setValue(String.valueOf(flashlightValue));
        mFlashlightOnCall.setSummary(mFlashlightOnCall.getEntry());
        mFlashlightOnCall.setOnPreferenceChangeListener(this);
        }

        if (!Utils.isVoiceCapable(getActivity())) {
            prefScreen.removePreference(callSettings);
        }

        boolean enableSmartPixels = getContext().getResources().
                getBoolean(com.android.internal.R.bool.config_enableSmartPixels);
        Preference smartPixelsPref = (Preference) findPreference("smart_pixels");

        if (!enableSmartPixels){
            overallPreferences.removePreference(smartPixelsPref);
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {
        if (preference == mAspectRatioAppsSelect) {
            Collection<String> valueList = (Collection<String>) objValue;
            mAspectRatioApps.setVisible(false);
            if (valueList != null) {
                Settings.System.putString(getContentResolver(), Settings.System.OMNI_ASPECT_RATIO_APPS_LIST,
                        TextUtils.join(":", valueList));
                mAspectRatioApps.setVisible(true);
                mAspectRatioApps.setValues(valueList);
            } else {
                Settings.System.putString(getContentResolver(), Settings.System.OMNI_ASPECT_RATIO_APPS_LIST, "");
            }
            return true;
        } else if (preference == mFlashlightOnCall) {
            int flashlightValue = Integer.parseInt(((String) objValue).toString());
            Settings.System.putInt(getContentResolver(),
                    Settings.System.FLASHLIGHT_ON_CALL, flashlightValue);
            mFlashlightOnCall.setValue(String.valueOf(flashlightValue));
            mFlashlightOnCall.setSummary(mFlashlightOnCall.getEntry());
            return true;
        }
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.BOOTLEG;
    }
}

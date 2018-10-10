package com.bootleggers.dumpster.fragments;

import com.android.internal.logging.nano.MetricsProto;
import android.app.Activity;
import android.content.Context;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v14.preference.SwitchPreference;
import android.provider.Settings;
import com.android.settings.R;

import java.util.Arrays;
import java.util.HashSet;

import com.android.settings.SettingsPreferenceFragment;
import com.bootleggers.dumpster.extra.Utils;

public class RecentsSettings extends SettingsPreferenceFragment implements
        OnPreferenceChangeListener {

    private ListPreference mRecentsComponentType;
    private static final String RECENTS_COMPONENT_TYPE = "recents_component";

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        addPreferencesFromResource(R.xml.bootleg_dumpster_recents);

        ContentResolver resolver = getActivity().getContentResolver();

        // recents component type
        mRecentsComponentType = (ListPreference) findPreference(RECENTS_COMPONENT_TYPE);
        int type = Settings.System.getInt(resolver,
                Settings.System.RECENTS_COMPONENT, 0);
        mRecentsComponentType.setValue(String.valueOf(type));
        mRecentsComponentType.setSummary(mRecentsComponentType.getEntry());
        mRecentsComponentType.setOnPreferenceChangeListener(this);


    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {
        if (preference == mRecentsComponentType) {
            int type = Integer.valueOf((String) objValue);
            int index = mRecentsComponentType.findIndexOfValue((String) objValue);
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.RECENTS_COMPONENT, type);
            mRecentsComponentType.setSummary(mRecentsComponentType.getEntries()[index]);
            if (type == 1) { // Disable swipe up gesture, if oreo type selected
               Settings.Secure.putInt(getActivity().getContentResolver(),
                    Settings.Secure.SWIPE_UP_TO_SWITCH_APPS_ENABLED, 0);
            }
            Utils.restartSystemUi(getContext());
        return true;
        }
    return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.BOOTLEG;
    }
}

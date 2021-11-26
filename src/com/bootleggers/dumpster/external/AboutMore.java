package com.bootleggers.dumpster.external;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.content.Context;
import android.content.Intent;
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

import com.android.settings.R;

import com.android.settings.SettingsPreferenceFragment;

import com.android.internal.logging.nano.MetricsProto;

public class AboutMore extends SettingsPreferenceFragment {

    private Preference prefCrowd;
    private String bootlegCrowdinString;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.bootleg_external_frag_aboutmore);
        Resources res = getResources();
        prefCrowd = (Preference) findPreference("bootleg_morecd");

        bootlegCrowdinString = res.getString(R.string.bootleg_moarstuff_cd_sumgen, res.getString(R.string.bootleg_moarstuff_cd_summary01), res.getString(R.string.bootleg_moarstuff_cd_summary02));

        prefCrowd.setSummary(String.valueOf(bootlegCrowdinString));
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.BOOTLEG;
    }

}
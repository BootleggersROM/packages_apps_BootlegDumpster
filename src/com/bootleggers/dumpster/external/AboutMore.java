package com.bootleggers.dumpster.external;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.content.Context;
import android.content.Intent;
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

import com.android.settings.R;

import com.android.settings.SettingsPreferenceFragment;

import com.android.internal.logging.nano.MetricsProto;

public class AboutMore extends SettingsPreferenceFragment {

    private Preference prefCrowd;
    private String bootlegCrowdinString;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.bootleg_external_aboutmore);
        Resources res = getResources();
        prefCrowd = (Preference) findPreference("bootleg_morecd");

        bootlegCrowdinString = res.getString(R.string.bootleg_moarstuff_cd_sumgen, R.string.bootleg_moarstuff_cd_summary01, R.string.bootleg_moarstuff_cd_summary02);

        prefCrowd.setSummary(String.valueOf(bootlegCrowdinString));

        mFooterPreferenceMixin.createFooterPreference().setTitle(R.string.bootleg_pref_moarstuff_wewsecret);
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.BOOTLEG;
    }

}

package com.bootleggers.dumpster.fragments;

import com.android.internal.logging.nano.MetricsProto;

import android.os.Bundle;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.UserHandle;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v14.preference.SwitchPreference;
import android.provider.Settings;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import java.util.Locale;
import android.text.TextUtils;
import android.view.View;

import com.bootleggers.dumpster.external.QsTileStyles;

import java.util.List;
import java.util.ArrayList;

public class QuickSettings extends SettingsPreferenceFragment implements
        OnPreferenceChangeListener {

    private Preference mQSTileStyle;
    private static final String QS_TILE_STYLE = "qs_tile_style";


    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        addPreferencesFromResource(R.xml.bootleg_dumpster_quicksettings);

        PreferenceScreen prefScreen = getPreferenceScreen();
        ContentResolver resolver = getActivity().getContentResolver();

        // set qs tile style
        mQSTileStyle = (Preference) findPreference(QS_TILE_STYLE);
        }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return false;
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        if (preference == mQSTileStyle) {
                QsTileStyles.show(this);
            return true;
        }
        return super.onPreferenceTreeClick(preference);
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.BOOTLEG;
    }

}

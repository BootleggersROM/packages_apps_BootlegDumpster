package com.bootleggers.dumpster.external;

import com.android.internal.logging.nano.MetricsProto;

import android.os.Bundle;
import android.content.Intent;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.provider.Settings;
import android.support.v7.preference.Preference;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import java.util.Locale;
import android.text.TextUtils;
import android.view.View;

import java.util.List;
import java.util.ArrayList;

public class TheDrill extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.bootleg_external_drill);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.BOOTLEG;
    }
}


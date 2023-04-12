package com.bootleggers.dumpster.fragments;

import com.android.internal.logging.nano.MetricsProto;

import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.UserHandle;
import android.content.ContentResolver;
import android.content.res.Resources;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;
import androidx.preference.PreferenceCategory;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.PreferenceFragment;
import androidx.preference.SwitchPreference;
import android.provider.Settings;
import com.android.settings.R;

import java.util.Locale;
import android.text.TextUtils;
import android.view.View;

import com.android.settings.SettingsPreferenceFragment;
import com.bootleggers.dumpster.extra.Utils;
import android.util.Log;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

public class StatusBarSettings extends SettingsPreferenceFragment implements
        OnPreferenceChangeListener {

    // Categories
    private static final String KEY_CAT_ICONS = "statusbar_icons_cat";

    private PreferenceCategory mIconsCat;

    // Preferences
    private static final String KEY_SHOW_FOURG = "show_fourg_icon";
    private static final String KEY_USE_OLD_MOBILETYPE = "use_old_mobiletype";

    private SwitchPreference mShowFourg;
    private SwitchPreference mOldMobileType;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        addPreferencesFromResource(R.xml.bootleg_dumpster_frag_status_bar);

        PreferenceScreen prefSet = getPreferenceScreen();

        mIconsCat = (PreferenceCategory) prefSet.findPreference(KEY_CAT_ICONS);
        mShowFourg = (SwitchPreference) mIconsCat.findPreference(KEY_SHOW_FOURG);
        mOldMobileType = (SwitchPreference) mIconsCat.findPreference(KEY_USE_OLD_MOBILETYPE);

        if (!Utils.isVoiceCapable(getActivity())) {
            mIconsCat.removePreference(mShowFourg);
            mIconsCat.removePreference(mOldMobileType);
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getActivity().getContentResolver();
        switch (preference.getKey()) {
            default:
                return false;
        }
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.BOOTLEG;
    }

    public static void reset(Context mContext) {
        ContentResolver resolver = mContext.getContentResolver();
        Settings.System.putIntForUser(resolver,
                Settings.System.STATUSBAR_COLORED_ICONS, 0, UserHandle.USER_CURRENT);
        Settings.System.putIntForUser(resolver,
                Settings.System.STATUSBAR_NOTIF_COUNT, 0, UserHandle.USER_CURRENT);
    }

}

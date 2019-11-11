package com.bootleggers.dumpster.fragments;

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

import com.android.settings.R;

import com.android.settings.SettingsPreferenceFragment;

import com.android.internal.logging.nano.MetricsProto;

public class AnimationSettings extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private static final String KEY_SCREEN_OFF_ANIMATION = "screen_off_animation";

    private Context mContext;
    private ListPreference mScreenOffAnimation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.bootleg_dumpster_animations);
        mFooterPreferenceMixin.createFooterPreference().setTitle(R.string.animations_transparent_alert);

        mScreenOffAnimation = (ListPreference) findPreference(KEY_SCREEN_OFF_ANIMATION);
        int screenOffAnimation = Settings.System.getInt(getContentResolver(),
                Settings.System.SCREEN_OFF_ANIMATION, 0);
        mScreenOffAnimation.setValue(Integer.toString(screenOffAnimation));
        mScreenOffAnimation.setSummary(mScreenOffAnimation.getEntry());
        mScreenOffAnimation.setOnPreferenceChangeListener(this);
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getActivity().getContentResolver();
        switch (preference.getKey()) {

            case KEY_SCREEN_OFF_ANIMATION:
                int value = Integer.valueOf((String) newValue);
                int index = mScreenOffAnimation.findIndexOfValue((String) newValue);
                mScreenOffAnimation.setSummary(mScreenOffAnimation.getEntries()[index]);
                Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_ANIMATION, value);
                return true;
            default:
                return false;
        }
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.BOOTLEG;
    }

}

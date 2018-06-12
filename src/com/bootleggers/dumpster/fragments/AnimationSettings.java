package com.bootleggers.dumpster.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v14.preference.SwitchPreference;
import android.provider.Settings;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.HashSet;

import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

public class AnimationSettings extends SettingsPreferenceFragment implements
        OnPreferenceChangeListener {

    private static final String KEY_LISTVIEW_ANIMATION = "listview_animation";
    private static final String KEY_LISTVIEW_INTERPOLATOR = "listview_interpolator";
    
    private ListPreference mListViewAnimation;
    private ListPreference mListViewInterpolator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.bootleg_dumpster_animations);
        mFooterPreferenceMixin.createFooterPreference().setTitle(R.string.animations_transparent_alert);
        final PreferenceScreen prefSet = getPreferenceScreen();

    //Listview Animations
    mListViewAnimation = (ListPreference) prefSet.findPreference(KEY_LISTVIEW_ANIMATION);
    int listviewanimation = Settings.System.getInt(getContentResolver(),
            Settings.System.LISTVIEW_ANIMATION, 0);
    mListViewAnimation.setValue(String.valueOf(listviewanimation));
    mListViewAnimation.setSummary(mListViewAnimation.getEntry());
    mListViewAnimation.setOnPreferenceChangeListener(this);

    mListViewInterpolator = (ListPreference) prefSet.findPreference(KEY_LISTVIEW_INTERPOLATOR);
    int listviewinterpolator = Settings.System.getInt(getContentResolver(),
            Settings.System.LISTVIEW_INTERPOLATOR, 0);
    mListViewInterpolator.setValue(String.valueOf(listviewinterpolator));
    mListViewInterpolator.setSummary(mListViewInterpolator.getEntry());
    mListViewInterpolator.setOnPreferenceChangeListener(this);
    mListViewInterpolator.setEnabled(listviewanimation > 0);    

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
    if (preference == mListViewAnimation) {
            int value = Integer.parseInt((String) newValue);
            int index = mListViewAnimation.findIndexOfValue((String) newValue);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.LISTVIEW_ANIMATION, value);
            mListViewAnimation.setSummary(mListViewAnimation.getEntries()[index]);
            mListViewInterpolator.setEnabled(value > 0);
            return true;
        } else if (preference == mListViewInterpolator) {
            int value = Integer.parseInt((String) newValue);
            int index = mListViewInterpolator.findIndexOfValue((String) newValue);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.LISTVIEW_INTERPOLATOR, value);
            mListViewInterpolator.setSummary(mListViewInterpolator.getEntries()[index]);           
            return true;
        }
    return false;
    }
    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.BOOTLEG;
    }

}

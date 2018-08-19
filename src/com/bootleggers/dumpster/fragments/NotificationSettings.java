package com.bootleggers.dumpster.fragments;

import com.android.internal.logging.nano.MetricsProto;

import android.content.Context;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.UserInfo;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.UserManager;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v14.preference.SwitchPreference;
import android.provider.Settings;
import com.android.settings.R;
import android.support.annotation.NonNull;

import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.SettingsPreferenceFragment;
import com.bootleggers.dumpster.preferences.SystemSettingSwitchPreference;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class NotificationSettings extends SettingsPreferenceFragment
                    implements Preference.OnPreferenceChangeListener {

    private PreferenceCategory mLedsCategory;
    private Preference mChargingLeds;
    private SystemSettingSwitchPreference mLowBatteryBlinking;

    private ListPreference mTickerMode;
    private ListPreference mTickerAnimation;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        addPreferencesFromResource(R.xml.bootleg_dumpster_notifications);
        final PreferenceScreen prefSet = getPreferenceScreen();

        mLedsCategory = (PreferenceCategory) findPreference("light_category");
        mChargingLeds = (Preference) findPreference("battery_charging_light");
        mLowBatteryBlinking = (SystemSettingSwitchPreference)prefSet.findPreference("battery_light_low_blinking");
        if (getResources().getBoolean(
                        com.android.internal.R.bool.config_ledCanPulse)) {
            mLowBatteryBlinking.setChecked(Settings.System.getIntForUser(getContentResolver(),
                            Settings.System.BATTERY_LIGHT_LOW_BLINKING, 1, UserHandle.USER_CURRENT) == 1);
            mLowBatteryBlinking.setOnPreferenceChangeListener(this);
        } else {
            mLedsCategory.removePreference(mLowBatteryBlinking);
        }

        if (mChargingLeds != null
                && !getResources().getBoolean(
                        com.android.internal.R.bool.config_intrusiveBatteryLed)) {
            mLedsCategory.removePreference(mChargingLeds);
        }
          if (mChargingLeds == null) {
            prefSet.removePreference(mLedsCategory);
        }

        mTickerMode = (ListPreference) findPreference("ticker_mode");
        mTickerMode.setOnPreferenceChangeListener(this);
        int tickerMode = Settings.System.getIntForUser(getContentResolver(),
                Settings.System.STATUS_BAR_SHOW_TICKER,
                1, UserHandle.USER_CURRENT);
        mTickerMode.setValue(String.valueOf(tickerMode));
        mTickerMode.setSummary(mTickerMode.getEntry());

        mTickerAnimation = (ListPreference) findPreference("status_bar_ticker_animation_mode");
        mTickerAnimation.setOnPreferenceChangeListener(this);
        int tickerAnimationMode = Settings.System.getIntForUser(getContentResolver(),
                Settings.System.STATUS_BAR_TICKER_ANIMATION_MODE,
                1, UserHandle.USER_CURRENT);
        mTickerAnimation.setValue(String.valueOf(tickerAnimationMode));
        mTickerAnimation.setSummary(mTickerAnimation.getEntry());
        boolean tickerIsDisabled = tickerMode == 0;
        mTickerAnimation.setEnabled(!tickerIsDisabled);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getActivity().getContentResolver();
        if (preference.equals(mTickerMode)) {
            int tickerMode = Integer.parseInt(((String) newValue).toString());
            Settings.System.putIntForUser(getContentResolver(),
                    Settings.System.STATUS_BAR_SHOW_TICKER, tickerMode, UserHandle.USER_CURRENT);
            int index = mTickerMode.findIndexOfValue((String) newValue);
            mTickerMode.setSummary(
                    mTickerMode.getEntries()[index]);
            return true;
        } else if (preference.equals(mTickerAnimation)) {
            int tickerAnimationMode = Integer.parseInt(((String) newValue).toString());
            Settings.System.putIntForUser(getContentResolver(),
                    Settings.System.STATUS_BAR_TICKER_ANIMATION_MODE, tickerAnimationMode, UserHandle.USER_CURRENT);
            int index = mTickerAnimation.findIndexOfValue((String) newValue);
            mTickerAnimation.setSummary(
                    mTickerAnimation.getEntries()[index]);
            return true;
        } else if (preference == mLowBatteryBlinking) {
            boolean value = (Boolean) newValue;
            Settings.System.putIntForUser(getActivity().getContentResolver(),
                    Settings.System.BATTERY_LIGHT_LOW_BLINKING, value ? 1 : 0,
                    UserHandle.USER_CURRENT);
            mLowBatteryBlinking.setChecked(value);
            return true;
        }
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.BOOTLEG;
    }
}
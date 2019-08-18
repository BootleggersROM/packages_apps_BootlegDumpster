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
import android.widget.ImageView;

import com.android.settings.R;
import com.android.settings.widget.VideoPreference;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class TheDrill extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {
    private ImageView mImg;
    private VideoPreference mVid;
    private int mRandomNumber;
    private int[] mNormalPost = new int[] {
        R.xml.bootleg_external_drill,
        R.xml.bootleg_external_drill_alt1,
        R.xml.bootleg_external_drill_alt2,
        R.xml.bootleg_external_drill_alt3
    };
    private int[] mSpecialPost = new int[] {
        R.xml.bootleg_external_drill_sp1,
        R.xml.bootleg_external_drill_sp2,
        R.xml.bootleg_external_drill_sp3,
        R.xml.bootleg_external_drill_sp4
    };

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        Random r = new Random();
        mRandomNumber = r.nextInt((25 - 0) + 1) + 0;
        if (mRandomNumber == 17) {
            addPreferencesFromResource(R.xml.bootleg_external_drill_vid);
        } else {
            Random rimg = new Random();
            if (mRandomNumber > 20) {
                addPreferencesFromResource(mSpecialPost[getLuckyNumber(mSpecialPost.length - 1)]);
            } else if (mRandomNumber > 11) {
                addPreferencesFromResource(mNormalPost[getLuckyNumber(mNormalPost.length - 1)]);
            } else {
                addPreferencesFromResource(R.xml.bootleg_external_drill);
            }
        }
    }

    public int getLuckyNumber(int max) {
        return getLuckyNumber(0, max);
    }

    public int getLuckyNumber(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
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

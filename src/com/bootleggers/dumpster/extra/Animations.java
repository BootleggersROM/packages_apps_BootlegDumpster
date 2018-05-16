
 package com.bootleggers.dumpster.extra;

 import android.app.ActivityManagerNative;
 import android.content.Context;
 import android.content.ContentResolver;
 import android.content.pm.PackageManager.NameNotFoundException;
 import android.content.res.Resources;
 import android.os.Bundle;
 import android.os.UserHandle;
 import android.os.RemoteException;
 import android.os.ServiceManager;
 import android.support.v7.preference.Preference;
 import android.support.v7.preference.ListPreference;
 import android.support.v7.preference.PreferenceCategory;
 import android.support.v7.preference.PreferenceScreen;
 import android.support.v7.preference.Preference.OnPreferenceChangeListener;
 import android.provider.Settings;
 import android.util.Log;
 import android.view.WindowManagerGlobal;
 import android.view.IWindowManager;
 import java.util.regex.Matcher;
 import java.util.regex.Pattern;
 import java.util.Locale;
 import android.text.TextUtils;
 import android.view.View;
 import com.android.settings.R;
 import com.android.settings.SettingsPreferenceFragment;
 import com.android.internal.logging.nano.MetricsProto;
 import com.android.internal.logging.nano.MetricsProto.MetricsEvent;
 import com.android.settings.Utils;
 import android.view.Menu;
 import android.widget.EditText;
 import android.widget.Toast;

 public class Animations extends SettingsPreferenceFragment implements OnPreferenceChangeListener
{
    private static final String KEY_TOAST_ANIMATION = "toast_animation";
    private ListPreference mToastAnimation;
    protected Context mContext;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.animations);
        final ContentResolver resolver = getActivity().getContentResolver();
	mContext = getActivity().getApplicationContext();
        mToastAnimation = (ListPreference) findPreference(KEY_TOAST_ANIMATION);
        mToastAnimation.setSummary(mToastAnimation.getEntry());
        int CurrentToastAnimation = Settings.System.getInt(getContentResolver(), 
Settings.System.TOAST_ANIMATION, 1);
        mToastAnimation.setValueIndex(CurrentToastAnimation); //set to index of default value
        mToastAnimation.setSummary(mToastAnimation.getEntries()[CurrentToastAnimation]);
        mToastAnimation.setOnPreferenceChangeListener(this);
    }
    @Override
    public int getMetricsCategory() {
        return MetricsEvent.BOOTLEG;
    }
    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {
	if (preference == mToastAnimation) {
            int index = mToastAnimation.findIndexOfValue((String) objValue);
            Settings.System.putString(getContentResolver(), 
Settings.System.TOAST_ANIMATION, (String) objValue);
            mToastAnimation.setSummary(mToastAnimation.getEntries()[index]);
            Toast.makeText(mContext, "Toast Test", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
}

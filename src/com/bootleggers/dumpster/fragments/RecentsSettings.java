package com.bootleggers.dumpster.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.UserHandle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v14.preference.SwitchPreference;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ListView;

import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settingslib.widget.FooterPreference;
import com.bootleggers.dumpster.preferences.SystemSettingSwitchPreference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import com.bootleggers.dumpster.extra.Utils;

public class RecentsSettings extends SettingsPreferenceFragment implements
        OnPreferenceChangeListener, DialogInterface.OnDismissListener {

    private ListPreference mRecentsLayoutStylePref;
    private SwitchPreference mSlimToggle;
    private Preference mSlimSettings;
    private Preference mRecentsIconPack;
    private SwitchPreference mRecentsClearAll;
    private ListPreference mRecentsClearAllLocation;
    private SwitchPreference mRecentsMemBar;
    private static final String RECENTS_LAYOUT_STYLE_PREF = "recents_layout_style";
    private static final String RECENTS_ICON_PACK_PREF = "recents_icon_pack";
    private static final String RECENTS_CLEAR_ALL_PREF = "show_clear_all_recents";
    private static final String RECENTS_CLEAR_ALL_LOCATION = "recents_clear_all_location";
    private static final String RECENTS_MEMBAR_PREF = "systemui_recents_mem_display";
    private static final String PREF_SLIM_RECENTS_SETTINGS = "slim_recents_settings";
    private static final String PREF_SLIM_RECENTS = "use_slim_recents";

    private final static String[] sSupportedActions = new String[] {
        "org.adw.launcher.THEMES",
        "com.gau.go.launcherex.theme"
    };

    private static final String[] sSupportedCategories = new String[] {
        "com.fede.launcher.THEME_ICONPACK",
        "com.anddoes.launcher.THEME",
        "com.teslacoilsw.launcher.THEME"
    };

    private AlertDialog mDialog;
    private ListView mListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.bootleg_dumpster_recents);

        ContentResolver resolver = getActivity().getContentResolver();
        mRecentsIconPack = (Preference) findPreference(RECENTS_ICON_PACK_PREF);
        mRecentsClearAll = (SwitchPreference) findPreference(RECENTS_CLEAR_ALL_PREF);
        mRecentsMemBar = (SwitchPreference) findPreference(RECENTS_MEMBAR_PREF);

        // recents layout style
        mRecentsLayoutStylePref = (ListPreference) findPreference(RECENTS_LAYOUT_STYLE_PREF);
        int type = Settings.System.getInt(resolver,
                Settings.System.RECENTS_LAYOUT_STYLE, 0);
        mRecentsLayoutStylePref.setValue(String.valueOf(type));
        mRecentsLayoutStylePref.setSummary(mRecentsLayoutStylePref.getEntry());
        mRecentsLayoutStylePref.setOnPreferenceChangeListener(this);

        // clear all recents
        mRecentsClearAllLocation = (ListPreference) findPreference(RECENTS_CLEAR_ALL_LOCATION);
        int location = Settings.System.getIntForUser(resolver,
                Settings.System.RECENTS_CLEAR_ALL_LOCATION, 5, UserHandle.USER_CURRENT);
        mRecentsClearAllLocation.setValue(String.valueOf(location));
        mRecentsClearAllLocation.setSummary(mRecentsClearAllLocation.getEntry());
        mRecentsClearAllLocation.setOnPreferenceChangeListener(this);

        // Slim Recents
        mSlimSettings = (Preference) findPreference(PREF_SLIM_RECENTS_SETTINGS);
        mSlimToggle = (SwitchPreference) findPreference(PREF_SLIM_RECENTS);
        mSlimToggle.setOnPreferenceChangeListener(this);

        updateRecentsPreferences();
    }

    private void updateRecentsPreferences() {
        boolean slimEnabled = Settings.System.getIntForUser(
                getActivity().getContentResolver(), Settings.System.USE_SLIM_RECENTS, 0,
                UserHandle.USER_CURRENT) == 1;
        boolean quickstepRecents = Settings.System.getIntForUser(
                getActivity().getContentResolver(), Settings.System.RECENTS_LAYOUT_STYLE, 0,
                UserHandle.USER_CURRENT) == 0;
        boolean goRecents = Settings.System.getIntForUser(
                getActivity().getContentResolver(), Settings.System.RECENTS_LAYOUT_STYLE, 0,
                UserHandle.USER_CURRENT) == 3;
        
        // Either Stock or Slim Recents can be active at a time
        if (quickstepRecents || goRecents) {
            mRecentsLayoutStylePref.setEnabled(true);
            mRecentsIconPack.setEnabled(true);
            mRecentsClearAll.setEnabled(false);
            mRecentsClearAllLocation.setEnabled(false);
            mRecentsMemBar.setEnabled(false);
            mSlimToggle.setChecked(false);
        } else {
            mRecentsLayoutStylePref.setEnabled(!slimEnabled);
            mRecentsIconPack.setEnabled(!slimEnabled);
            mRecentsClearAll.setEnabled(!slimEnabled);
            mRecentsClearAllLocation.setEnabled(!slimEnabled);
            mRecentsMemBar.setEnabled(!slimEnabled);
            mSlimToggle.setChecked(slimEnabled);
        }
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.BOOTLEG;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {
        if (preference == mRecentsLayoutStylePref) {
            int type = Integer.valueOf((String) objValue);
            int index = mRecentsLayoutStylePref.findIndexOfValue((String) objValue);
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.RECENTS_LAYOUT_STYLE, type);
            mRecentsLayoutStylePref.setSummary(mRecentsLayoutStylePref.getEntries()[index]);
            if (type != 0) { // Disable swipe up gesture, if oreo type selected
               Settings.Secure.putInt(getActivity().getContentResolver(),
                    Settings.Secure.SWIPE_UP_TO_SWITCH_APPS_ENABLED, 0);
            }
            updateRecentsPreferences();
            Utils.restartSystemUi(getContext());
        return true;
        } else if (preference == mRecentsClearAllLocation) {
            int location = Integer.valueOf((String) objValue);
            int index = mRecentsClearAllLocation.findIndexOfValue((String) objValue);
            Settings.System.putIntForUser(getActivity().getContentResolver(),
                    Settings.System.RECENTS_CLEAR_ALL_LOCATION, location, UserHandle.USER_CURRENT);
            mRecentsClearAllLocation.setSummary(mRecentsClearAllLocation.getEntries()[index]);
        return true;
        } else if (preference == mSlimToggle) {
            boolean value = (Boolean) objValue;
            int type = Settings.System.getInt(
                getActivity().getContentResolver(), Settings.System.RECENTS_LAYOUT_STYLE, 0);
            if (value && (type == 0)) { // change recents type to oreo when we are about to switch to slimrecents
               Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.RECENTS_LAYOUT_STYLE, 1);
                Utils.restartSystemUi(getContext());
            }
            Settings.System.putIntForUser(getActivity().getContentResolver(),
                    Settings.System.USE_SLIM_RECENTS, value ? 1 : 0,
                    UserHandle.USER_CURRENT);
            updateRecentsPreferences();
            return true;
        }

    return false;
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        if (preference == findPreference("recents_icon_pack")) {
            pickIconPack(getContext());
            return true;
        }
        return super.onPreferenceTreeClick(preference);
    }

    /** Recents Icon Pack Dialog **/
    private void pickIconPack(final Context context) {
        if (mDialog != null) {
            return;
        }
        Map<String, IconPackInfo> supportedPackages = getSupportedPackages(context);
        if (supportedPackages.isEmpty()) {
            Toast.makeText(context, R.string.no_iconpacks_summary, Toast.LENGTH_SHORT).show();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
        .setTitle(R.string.dialog_pick_iconpack_title)
        .setOnDismissListener(this)
        .setNegativeButton(R.string.cancel, null)
        .setView(createDialogView(context, supportedPackages));
        mDialog = builder.show();
    }

    private View createDialogView(final Context context, Map<String, IconPackInfo> supportedPackages) {
        final LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.dialog_iconpack, null);
        final IconAdapter adapter = new IconAdapter(context, supportedPackages);

        mListView = (ListView) view.findViewById(R.id.iconpack_list);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                        int position, long id) {
                if (adapter.isCurrentIconPack(position)) {
                    return;
                }
                String selectedPackage = adapter.getItem(position);
                Settings.System.putString(getContext().getContentResolver(),
                        Settings.System.RECENTS_ICON_PACK, selectedPackage);
                mDialog.dismiss();
            }
        });

        return view;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (mDialog != null) {
            mDialog = null;
        }
    }

    private static class IconAdapter extends BaseAdapter {
        ArrayList<IconPackInfo> mSupportedPackages;
        LayoutInflater mLayoutInflater;
        String mCurrentIconPack;
        int mCurrentIconPackPosition = -1;

        IconAdapter(Context ctx, Map<String, IconPackInfo> supportedPackages) {
            mLayoutInflater = LayoutInflater.from(ctx);
            mSupportedPackages = new ArrayList<IconPackInfo>(supportedPackages.values());
            Collections.sort(mSupportedPackages, new Comparator<IconPackInfo>() {
                @Override
                public int compare(IconPackInfo lhs, IconPackInfo rhs) {
                    return lhs.label.toString().compareToIgnoreCase(rhs.label.toString());
                }
            });

            Resources res = ctx.getResources();
            String defaultLabel = res.getString(R.string.default_iconpack_title);
            Drawable icon = res.getDrawable(android.R.drawable.sym_def_app_icon);
            mSupportedPackages.add(0, new IconPackInfo(defaultLabel, icon, ""));
            mCurrentIconPack = Settings.System.getString(ctx.getContentResolver(),
                Settings.System.RECENTS_ICON_PACK);
        }

        @Override
        public int getCount() {
            return mSupportedPackages.size();
        }

        @Override
        public String getItem(int position) {
            return (String) mSupportedPackages.get(position).packageName;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        public boolean isCurrentIconPack(int position) {
            return mCurrentIconPackPosition == position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mLayoutInflater.inflate(R.layout.iconpack_view_radio, null);
            }
            IconPackInfo info = mSupportedPackages.get(position);
            TextView txtView = (TextView) convertView.findViewById(R.id.title);
            txtView.setText(info.label);
            ImageView imgView = (ImageView) convertView.findViewById(R.id.icon);
            imgView.setImageDrawable(info.icon);
            RadioButton radioButton = (RadioButton) convertView.findViewById(R.id.radio);
            boolean isCurrentIconPack = info.packageName.equals(mCurrentIconPack);
            radioButton.setChecked(isCurrentIconPack);
            if (isCurrentIconPack) {
                mCurrentIconPackPosition = position;
            }
            return convertView;
        }
    }

    private Map<String, IconPackInfo> getSupportedPackages(Context context) {
        Intent i = new Intent();
        Map<String, IconPackInfo> packages = new HashMap<String, IconPackInfo>();
        PackageManager packageManager = context.getPackageManager();
        for (String action : sSupportedActions) {
            i.setAction(action);
            for (ResolveInfo r : packageManager.queryIntentActivities(i, 0)) {
                IconPackInfo info = new IconPackInfo(r, packageManager);
                packages.put(r.activityInfo.packageName, info);
            }
        }
        i = new Intent(Intent.ACTION_MAIN);
        for (String category : sSupportedCategories) {
            i.addCategory(category);
            for (ResolveInfo r : packageManager.queryIntentActivities(i, 0)) {
                IconPackInfo info = new IconPackInfo(r, packageManager);
                packages.put(r.activityInfo.packageName, info);
            }
            i.removeCategory(category);
        }
        return packages;
    }

    static class IconPackInfo {
        String packageName;
        CharSequence label;
        Drawable icon;

        IconPackInfo(ResolveInfo r, PackageManager packageManager) {
            packageName = r.activityInfo.packageName;
            icon = r.loadIcon(packageManager);
            label = r.loadLabel(packageManager);
        }

        IconPackInfo(){
        }

        public IconPackInfo(String label, Drawable icon, String packageName) {
            this.label = label;
            this.icon = icon;
            this.packageName = packageName;
        }
    }
}

/*
 * Copyright (C) 2017 AICP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bootleggers.dumpster.external;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.os.SystemProperties;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v14.preference.SwitchPreference;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.bootleggers.dumpster.extra.Utils;
import com.bootleggers.dumpster.extra.SuShell;

public class LogIt extends SettingsPreferenceFragment 
                    implements Preference.OnPreferenceChangeListener {

    private static final String TAG = LogIt.class.getSimpleName();

    private static final String PREF_LOGCAT = "logcat";
    private static final String PREF_KMSG = "kmseg";
    private static final String PREF_DMESG = "dmesg";
    private static final String PREF_BTLG_LOG_IT = "dump_log_it_now";
    private static final String PREF_SHARE_TYPE = "dump_log_share_type";

    private static final String LOGCAT_FILE = new File(Environment
        .getExternalStorageDirectory(), "bootleg_logcat.txt").getAbsolutePath();
    private static final String KMSG_FILE = new File(Environment
        .getExternalStorageDirectory(), "bootleg_kmsg.txt").getAbsolutePath();
    private static final String DMESG_FILE = new File(Environment
        .getExternalStorageDirectory(), "bootleg_dmesg.txt").getAbsolutePath();
    private static final String DEVINFO_FILE = new File(Environment
        .getExternalStorageDirectory(), "bootleg_devinfo.txt").getAbsolutePath();


    private static final String HASTE_LOGCAT_KEY = new File(Environment
            .getExternalStorageDirectory(), "bootleg_haste_logcat_key").getAbsolutePath();
    private static final String HASTE_KMSG_KEY = new File(Environment
            .getExternalStorageDirectory(), "bootleg_haste_kmsg_key").getAbsolutePath();
    private static final String HASTE_DMESG_KEY = new File(Environment
            .getExternalStorageDirectory(), "bootleg_haste_dmesg_key").getAbsolutePath();

    private static final String NORMAL_HASTE = "https://hastebin.com/documents";
    private static final File sdCardDirectory = Environment.getExternalStorageDirectory();
    private static final File logcatFile = new File(sdCardDirectory, "bootleg_logcat.txt");
    private static final File logcatHasteKey = new File(sdCardDirectory, "bootleg_haste_logcat_key");
    private static final File dmesgFile = new File(sdCardDirectory, "bootleg_dmesg.txt");
    private static final File dmesgHasteKey = new File(sdCardDirectory, "bootleg_haste_dmesg_key");
    private static final File kmsgFile = new File(sdCardDirectory, "bootleg_kmsg.txt");
    private static final File kmsgHasteKey = new File(sdCardDirectory, "bootleg_haste_kmsg_key");
    private static final File devinfoFile = new File(sdCardDirectory, "bootleg_devinfo.txt");
    private static final File shareZipFile = new File(sdCardDirectory, "bootleg_logs.zip");
    private static final int MENU_HELP  = 0;

    private static final int HASTE_MAX_LOG_SIZE = 400000;

    private CheckBoxPreference mLogcat;
    private CheckBoxPreference mKmsg;
    private CheckBoxPreference mDmesg;
    private Preference mBootlegLogIt;
    private ListPreference mShareType;

    private String sharingIntentString;

    private boolean shareHaste = false;
    private boolean shareZip = true;

    String bootlegBuildtype = SystemProperties.get("ro.bootleg.buildtype","Unshishufied");
    String bootlegBuilddevice = SystemProperties.get("ro.bootleg.device","unkownorbroken");
    String bootlegBuildrelease = SystemProperties.get("ro.bootleg.version","unkownorbroken");
    String bootlegBuildmaintainer = SystemProperties.get("ro.bootleg.maintainer","NotDeclaredLMAO");
    String bootlegBuilduser = SystemProperties.get("ro.build.user","unkownorbroken");
    String bootlegBuildhost = SystemProperties.get("ro.build.host","unkownorbroken");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.bootleg_external_log_it);
        setHasOptionsMenu(true);
        Resources res = getResources();

        mLogcat = (CheckBoxPreference) findPreference(PREF_LOGCAT);
        mLogcat.setOnPreferenceChangeListener(this);
        mKmsg = (CheckBoxPreference) findPreference(PREF_KMSG);
        mKmsg.setOnPreferenceChangeListener(this);
        mDmesg = (CheckBoxPreference) findPreference(PREF_DMESG);
        mDmesg.setOnPreferenceChangeListener(this);
        mBootlegLogIt = findPreference(PREF_BTLG_LOG_IT);
        mShareType = (ListPreference) findPreference(PREF_SHARE_TYPE);
        mShareType.setOnPreferenceChangeListener(this);

        resetValues();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mLogcat) {
            mBootlegLogIt.setEnabled((Boolean) newValue || mKmsg.isChecked() || mDmesg.isChecked());
            return true;
        } else if (preference == mKmsg) {
            mBootlegLogIt.setEnabled((Boolean) newValue || mLogcat.isChecked() || mDmesg.isChecked());
            return true;
        } else if (preference == mDmesg) {
            mBootlegLogIt.setEnabled((Boolean) newValue || mLogcat.isChecked() || mKmsg.isChecked());
            return true;
        } else if (preference == mShareType) {
            if ("0".equals(newValue)) {
                mShareType.setSummary(getString(R.string.log_it_share_type_haste));
                shareHaste = true;
                shareZip = false;
            } else if ("1".equals(newValue)) {
                mShareType.setSummary(getString(R.string.log_it_share_type_zip));
                shareHaste = false;
                shareZip = true;
            } else {
                mShareType.setSummary("");
                shareHaste = false;
                shareZip = false;
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        if (preference == mBootlegLogIt) {
            new CreateLogTask().execute(mLogcat.isChecked(), mKmsg.isChecked(), mDmesg.isChecked());
            return true;
        } else {
            return super.onPreferenceTreeClick(preference);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(0, MENU_HELP, 0, R.string.log_it_alert_title)
                .setIcon(R.drawable.ic_help_circle)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_HELP:
                showDialogInner(MENU_HELP);
                return true;
            default:
                return false;
        }
    }

    private void showDialogInner(int id) {
        DialogFragment newFragment = MyAlertDialogFragment.newInstance(id);
        newFragment.setTargetFragment(this, 0);
        newFragment.show(getFragmentManager(), "dialog " + id);
    }

    public static class MyAlertDialogFragment extends DialogFragment {

        public static MyAlertDialogFragment newInstance(int id) {
            MyAlertDialogFragment frag = new MyAlertDialogFragment();
            Bundle args = new Bundle();
            args.putInt("id", id);
            frag.setArguments(args);
            return frag;
        }

        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int id = getArguments().getInt("id");
            switch (id) {
                case MENU_HELP:
                    return new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.log_it_alert_title)
                    .setMessage(R.string.log_it_alert_faq)
                    .setCancelable(false)
                    .setNegativeButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .create();
            }
            throw new IllegalArgumentException("unknown id " + id);
        }

        @Override
        public void onCancel(DialogInterface dialog) {

        }
    }

    public void logItDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.log_it_dialog_title);
        if (bootlegBuildtype == "Shishufied") {
            builder.setMessage(R.string.logcat_warning);
        } else {
            builder.setMessage(R.string.logcat_warning_unofficial);
        }
        builder.setPositiveButton(R.string.share_title, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.log_it_share_subject);
                sharingIntent.putExtra(Intent.EXTRA_TEXT, sharingIntentString);
                startActivity(Intent.createChooser(sharingIntent,
                        getString(R.string.log_it_share_via)));
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void logZipDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.log_it_dialog_title);
        if (bootlegBuildtype == "Shishufied") {
            builder.setMessage(R.string.logcat_warning);
        } else {
            builder.setMessage(R.string.logcat_warning_unofficial);
        }
        builder.setPositiveButton(R.string.share_title, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("application/zip");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.log_it_share_subject);
                sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(shareZipFile));
                try {
                    StrictMode.disableDeathOnFileUriExposure();
                    startActivity(Intent.createChooser(sharingIntent,
                            getString(R.string.log_it_share_via)));
                } finally {
                    StrictMode.enableDeathOnFileUriExposure();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void makeLogcat() throws SuShell.SuDeniedException, IOException {
        String command = "logcat -d";
        if (shareHaste) {
            command += " | tail -c " + HASTE_MAX_LOG_SIZE + " > " + LOGCAT_FILE
                    + "&& curl -s -X POST -T " + LOGCAT_FILE + " " + NORMAL_HASTE
                    + " | cut -d'\"' -f4 | echo \"https://hastebin.com/$(cat -)\" > "
                            + HASTE_LOGCAT_KEY;
        } else {
            command += " > " + LOGCAT_FILE;
        }
        SuShell.runWithSuCheck(command);
    }

    public void makeKmsg() throws SuShell.SuDeniedException, IOException {
        String command = "cat /proc/last_kmsg";
        if (shareHaste) {
            command += " | tail -c " + HASTE_MAX_LOG_SIZE + " > " + KMSG_FILE
                    + " && curl -s -X POST -T " + KMSG_FILE + " " + NORMAL_HASTE
                    + " | cut -d'\"' -f4 | echo \"https://hastebin.com/$(cat -)\" > "
                            + HASTE_KMSG_KEY;
        } else {
            command += " > " + KMSG_FILE;
        }
        SuShell.runWithSuCheck(command);
    }

    public void makeDmesg() throws SuShell.SuDeniedException, IOException {
        String command = "dmesg";
        if (shareHaste) {
            command += " | tail -c " + HASTE_MAX_LOG_SIZE + " > " + DMESG_FILE
                    + "&& curl -s -X POST -T " + DMESG_FILE + " " + NORMAL_HASTE
                    + " | cut -d'\"' -f4 | echo \"https://hastebin.com/$(cat -)\" > "
                            + HASTE_DMESG_KEY;
        } else {
            command += " > " + DMESG_FILE;
        }
        SuShell.runWithSuCheck(command);
    }

    public void makeDevinfo() throws SuShell.SuDeniedException, IOException {
        String command = "echo 'Device: ' $(getprop ro.bootleg.device) > " + DEVINFO_FILE + " && echo 'Release: ' $(getprop ro.bootleg.version) >> " + DEVINFO_FILE + " && echo 'Build type: ' $(getprop ro.bootleg.buildtype) >> " + DEVINFO_FILE + " && echo '' >> " + DEVINFO_FILE + " && echo 'Maintainer: ' $(getprop ro.bootleg.maintainer) >> " + DEVINFO_FILE + " && echo 'Build user: ' $(getprop ro.build.user) >> " + DEVINFO_FILE + " && echo 'Build host: ' $(getprop ro.build.host) >> " + DEVINFO_FILE;
        if (!shareHaste) {
            command += " > " + DEVINFO_FILE;
        }
        SuShell.runWithSuCheck(command);
    }

    private void createShareZip(boolean logcat, boolean kmsg, boolean dmesg) throws IOException {

        ZipOutputStream out = null;
            
        try {
            out = new ZipOutputStream(new BufferedOutputStream(
                    new FileOutputStream(shareZipFile.getAbsolutePath())));
            if (logcat) {
                writeToZip(logcatFile, out);
            }
            if (kmsg) {
                writeToZip(kmsgFile, out);
            }
            if (dmesg) {
                writeToZip(dmesgFile, out);
            }
            writeToZip(devinfoFile, out);
        } finally {
            if (out != null) out.close();
        }
    }
    private void writeToZip(File file, ZipOutputStream out) throws IOException {
        BufferedInputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(file.getAbsolutePath()));
            ZipEntry entry = new ZipEntry(file.getName());
            out.putNextEntry(entry);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
        } finally {
            if (in != null) in.close();
        }
    }

    private class CreateLogTask extends AsyncTask<Boolean, Void, String> {

        private Exception mException = null;
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getString(R.string.log_it_logs_in_progress));
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }
        @Override
        protected String doInBackground(Boolean... params) {
            String sharingIntentString = "";
            if (params.length != 3) {
                Log.e(TAG, "CreateLogTask: invalid argument count");
                return sharingIntentString;
            }
            sharingIntentString += " Here is some log i made\nInfo:\n\nDevice: " + String.valueOf(bootlegBuilddevice) + "\nRelease: " + String.valueOf(bootlegBuildrelease) + "\nBuild type: " + String.valueOf(bootlegBuildtype) + "\n\nMaintainer: " + String.valueOf(bootlegBuildmaintainer) + "\nBuild user: " + String.valueOf(bootlegBuilduser) + "\nBuild host: " + String.valueOf(bootlegBuildhost);
            
            try {
                if (params[0]) {
                    makeLogcat();
                    if (shareHaste) {
                        sharingIntentString += "\n\nLogcat: " +
                                Utils.readStringFromFile(logcatHasteKey);
                    }
                }
                if (params[1]) {
                    makeKmsg();
                    if (shareHaste) {
                        sharingIntentString += "\nKmsg: " +
                                Utils.readStringFromFile(kmsgHasteKey);
                    }
                }
                if (params[2]) {
                    makeDmesg();
                    if (shareHaste) {
                        sharingIntentString += "\nDmesg: " +
                                Utils.readStringFromFile(dmesgHasteKey);
                    }
                }
                if (shareZip) {
                    makeDevinfo();
                    createShareZip(params[0], params[1], params[2]);
                }
            } catch (SuShell.SuDeniedException e) {
                mException = e;
            } catch (IOException e) {
                e.printStackTrace();
                mException = e;
            }
            return sharingIntentString;
        }

        @Override
        protected void onPostExecute(String param) {
            super.onPostExecute(param);
            progressDialog.dismiss();
            if (mException instanceof SuShell.SuDeniedException) {
                Toast.makeText(getActivity(), getString(R.string.cannot_get_su),
                        Toast.LENGTH_LONG).show();
                return;
            }
            if (shareHaste && param != null && param.length() > 1) {
                sharingIntentString = param.substring(1);
                logItDialog();
            }
            if (shareZip) {
                logZipDialog();
            }
        }
    }

    public void resetValues() {
        mLogcat.setChecked(false);
        mKmsg.setChecked(false);
        mDmesg.setChecked(false);
        mBootlegLogIt.setEnabled(false);
        mShareType.setValue("1");
        mShareType.setSummary(mShareType.getEntry());
        shareHaste = false;
        shareZip = true;

    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.BOOTLEG;
    }

}

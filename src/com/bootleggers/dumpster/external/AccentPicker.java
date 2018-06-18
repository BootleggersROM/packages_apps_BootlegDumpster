/*
 * Copyright (C) 2018 The Dirty Unicorns Project
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
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.om.IOverlayManager;
import android.content.om.OverlayInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;

import com.android.internal.logging.nano.MetricsProto;

import com.android.settings.R;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;

public class AccentPicker extends InstrumentedDialogFragment implements OnClickListener {

    private static final String TAG_ACCENT_PICKER = "accent_picker";

    private View mView;
    private int mUserId;

    private IOverlayManager mOverlayManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserId = UserHandle.myUserId();
        mOverlayManager = IOverlayManager.Stub.asInterface(
                ServiceManager.getService(Context.OVERLAY_SERVICE));
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mView = LayoutInflater.from(getActivity()).inflate(R.layout.accent_picker, null);
        initView();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(mView)
                .setNegativeButton(R.string.cancel, this)
                .setNeutralButton(R.string.theme_accent_picker_default, this)
                .setCancelable(false);

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    private void initView() {
        ContentResolver resolver = getActivity().getContentResolver();

        Button newHouseAccent = null;
        if (mView != null) {
            newHouseAccent = mView.findViewById(R.id.newHouseAccent);
        }
        if (newHouseAccent != null) {
            newHouseAccent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Settings.System.putIntForUser(resolver,
                            Settings.System.ACCENT_PICKER, 1, UserHandle.USER_CURRENT);
                    dismiss();
                }
            });
        }

        Button warmthAccent = null;
        if (mView != null) {
            warmthAccent = mView.findViewById(R.id.warmthAccent);
        }
        if (warmthAccent != null) {
            warmthAccent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Settings.System.putIntForUser(resolver,
                            Settings.System.ACCENT_PICKER, 2, UserHandle.USER_CURRENT);
                    dismiss();
                }
            });
        }

        Button awmawyAccent = null;
        if (mView != null) {
            awmawyAccent = mView.findViewById(R.id.awmawyAccent);
        }
        if (awmawyAccent != null) {
            awmawyAccent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Settings.System.putIntForUser(resolver,
                            Settings.System.ACCENT_PICKER, 3, UserHandle.USER_CURRENT);
                    dismiss();
                }
            });
        }

        Button coldSummerAccent = null;
        if (mView != null) {
            coldSummerAccent = mView.findViewById(R.id.coldSummerAccent);
        }
        if (coldSummerAccent != null) {
            coldSummerAccent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Settings.System.putIntForUser(resolver,
                            Settings.System.ACCENT_PICKER, 4, UserHandle.USER_CURRENT);
                    dismiss();
                }
            });
        }

        Button maniaAccent = null;
        if (mView != null) {
            maniaAccent = mView.findViewById(R.id.maniaAccent);
        }
        if (maniaAccent != null) {
            maniaAccent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Settings.System.putIntForUser(resolver,
                            Settings.System.ACCENT_PICKER, 5, UserHandle.USER_CURRENT);
                    dismiss();
                }
            });
        }

        Button limedAccent = null;
        if (mView != null) {
            limedAccent = mView.findViewById(R.id.limedAccent);
        }
        if (limedAccent != null) {
            limedAccent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Settings.System.putIntForUser(resolver,
                            Settings.System.ACCENT_PICKER, 6, UserHandle.USER_CURRENT);
                    dismiss();
                }
            });
        }

        Button dDayAccent = null;
        if (mView != null) {
            dDayAccent = mView.findViewById(R.id.dDayAccent);
        }
        if (dDayAccent != null) {
            dDayAccent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Settings.System.putIntForUser(resolver,
                            Settings.System.ACCENT_PICKER, 7, UserHandle.USER_CURRENT);
                    dismiss();
                }
            });
        }

        Button moveAccent = null;
        if (mView != null) {
            moveAccent = mView.findViewById(R.id.moveAccent);
        }
        if (moveAccent != null) {
            moveAccent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Settings.System.putIntForUser(resolver,
                            Settings.System.ACCENT_PICKER, 8, UserHandle.USER_CURRENT);
                    dismiss();
                }
            });
        }

        Button seasideAccent = null;
        if (mView != null) {
            seasideAccent = mView.findViewById(R.id.seasideAccent);
        }
        if (seasideAccent != null) {
            seasideAccent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Settings.System.putIntForUser(resolver,
                            Settings.System.ACCENT_PICKER, 9, UserHandle.USER_CURRENT);
                    dismiss();
                }
            });
        }

        Button naturedAccent = null;
        if (mView != null) {
            naturedAccent = mView.findViewById(R.id.naturedAccent);
        }
        if (naturedAccent != null) {
            naturedAccent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Settings.System.putIntForUser(resolver,
                            Settings.System.ACCENT_PICKER, 10, UserHandle.USER_CURRENT);
                    dismiss();
                }
            });
        }

        Button aospAccent = null;
        if (mView != null) {
            aospAccent = mView.findViewById(R.id.aospAccent);
        }
        if (aospAccent != null) {
            aospAccent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Settings.System.putIntForUser(resolver,
                            Settings.System.ACCENT_PICKER, 11, UserHandle.USER_CURRENT);
                    dismiss();
                }
            });
        }

        Button kaAccent = null;
        if (mView != null) {
            kaAccent = mView.findViewById(R.id.kaAccent);
        }
        if (kaAccent != null) {
            kaAccent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Settings.System.putIntForUser(resolver,
                            Settings.System.ACCENT_PICKER, 12, UserHandle.USER_CURRENT);
                    dismiss();
                }
            });
        }

        Button holillusionAccent = null;
        if (mView != null) {
            holillusionAccent = mView.findViewById(R.id.holillusionAccent);
        }
        if (holillusionAccent != null) {
            holillusionAccent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Settings.System.putIntForUser(resolver,
                            Settings.System.ACCENT_PICKER, 13, UserHandle.USER_CURRENT);
                    dismiss();
                }
            });
        }

        Button heirloomAccent = null;
        if (mView != null) {
            heirloomAccent = mView.findViewById(R.id.heirloomAccent);
        }
        if (heirloomAccent != null) {
            heirloomAccent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Settings.System.putIntForUser(resolver,
                            Settings.System.ACCENT_PICKER, 14, UserHandle.USER_CURRENT);
                    dismiss();
                }
            });
        }

        Button coldEveningAccent = null;
        if (mView != null) {
            coldEveningAccent = mView.findViewById(R.id.coldEveningAccent);
        }
        if (coldEveningAccent != null) {
            coldEveningAccent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Settings.System.putIntForUser(resolver,
                            Settings.System.ACCENT_PICKER, 15, UserHandle.USER_CURRENT);
                    dismiss();
                }
            });
        }

        Button obfuscAccent = null;
        if (mView != null) {
            obfuscAccent = mView.findViewById(R.id.obfuscAccent);
        }
        if (obfuscAccent != null) {
            obfuscAccent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Settings.System.putIntForUser(resolver,
                            Settings.System.ACCENT_PICKER, 16, UserHandle.USER_CURRENT);
                    dismiss();
                }
            });
        }

        Button frenchAccent = null;
        if (mView != null) {
            frenchAccent = mView.findViewById(R.id.frenchAccent);
        }
        if (frenchAccent != null) {
            frenchAccent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Settings.System.putIntForUser(resolver,
                            Settings.System.ACCENT_PICKER, 17, UserHandle.USER_CURRENT);
                    dismiss();
                }
            });
        }

        Button footprintAccent = null;
        if (mView != null) {
            footprintAccent = mView.findViewById(R.id.footprintAccent);
        }
        if (footprintAccent != null) {
            footprintAccent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Settings.System.putIntForUser(resolver,
                            Settings.System.ACCENT_PICKER, 18, UserHandle.USER_CURRENT);
                    dismiss();
                }
            });
        }

        Button dreamyAccent = null;
        if (mView != null) {
            dreamyAccent = mView.findViewById(R.id.dreamyAccent);
        }
        if (dreamyAccent != null) {
            dreamyAccent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Settings.System.putIntForUser(resolver,
                            Settings.System.ACCENT_PICKER, 19, UserHandle.USER_CURRENT);
                    dismiss();
                }
            });
        }

        Button notimpAccent = null;
        if (mView != null) {
            notimpAccent = mView.findViewById(R.id.notimpAccent);
        }
        if (notimpAccent != null) {
            notimpAccent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Settings.System.putIntForUser(resolver,
                            Settings.System.ACCENT_PICKER, 20, UserHandle.USER_CURRENT);
                    dismiss();
                }
            });
        }

        Button spookedAccent = null;
        if (mView != null) {
            spookedAccent = mView.findViewById(R.id.spookedAccent);
        }
        if (spookedAccent != null) {
            spookedAccent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Settings.System.putIntForUser(resolver,
                            Settings.System.ACCENT_PICKER, 21, UserHandle.USER_CURRENT);
                    dismiss();
                }
            });
        }

        Button illusionsAccent = null;
        if (mView != null) {
            illusionsAccent = mView.findViewById(R.id.illusionsAccent);
        }
        if (illusionsAccent != null) {
            illusionsAccent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Settings.System.putIntForUser(resolver,
                            Settings.System.ACCENT_PICKER, 22, UserHandle.USER_CURRENT);
                    dismiss();
                }
            });
        }

        Button trufilAccent = null;
        if (mView != null) {
            trufilAccent = mView.findViewById(R.id.trufilAccent);
        }
        if (trufilAccent != null) {
            trufilAccent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Settings.System.putIntForUser(resolver,
                            Settings.System.ACCENT_PICKER, 23, UserHandle.USER_CURRENT);
                    dismiss();
                }
            });
        }

        Button duskAccent = null;
        if (mView != null) {
            duskAccent = mView.findViewById(R.id.duskAccent);
        }
        if (duskAccent != null) {
            duskAccent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Settings.System.putIntForUser(resolver,
                            Settings.System.ACCENT_PICKER, 24, UserHandle.USER_CURRENT);
                    dismiss();
                }
            });
        }

        Button boucheAccent = null;
        if (mView != null) {
            boucheAccent = mView.findViewById(R.id.boucheAccent);
        }
        if (boucheAccent != null) {
            boucheAccent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Settings.System.putIntForUser(resolver,
                            Settings.System.ACCENT_PICKER, 25, UserHandle.USER_CURRENT);
                    dismiss();
                }
            });
        }

        Button bubblegumAccent = null;
        if (mView != null) {
            bubblegumAccent = mView.findViewById(R.id.bubblegumAccent);
        }
        if (bubblegumAccent != null) {
            bubblegumAccent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Settings.System.putIntForUser(resolver,
                            Settings.System.ACCENT_PICKER, 26, UserHandle.USER_CURRENT);
                    dismiss();
                }
            });
        }

        Button misleadingAccent = null;
        if (mView != null) {
            misleadingAccent = mView.findViewById(R.id.misleadingAccent);
        }
        if (misleadingAccent != null) {
            misleadingAccent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Settings.System.putIntForUser(resolver,
                            Settings.System.ACCENT_PICKER, 27, UserHandle.USER_CURRENT);
                    dismiss();
                }
            });
        }

        Button hazedAccent = null;
        if (mView != null) {
            hazedAccent = mView.findViewById(R.id.hazedAccent);
        }
        if (hazedAccent != null) {
            hazedAccent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Settings.System.putIntForUser(resolver,
                            Settings.System.ACCENT_PICKER, 28, UserHandle.USER_CURRENT);
                    dismiss();
                }
            });
        }

        Button burningAccent = null;
        if (mView != null) {
            burningAccent = mView.findViewById(R.id.burningAccent);
        }
        if (burningAccent != null) {
            burningAccent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Settings.System.putIntForUser(resolver,
                            Settings.System.ACCENT_PICKER, 29, UserHandle.USER_CURRENT);
                    dismiss();
                }
            });
        }

        Button whytheAccent = null;
        if (mView != null) {
            whytheAccent = mView.findViewById(R.id.whytheAccent);
        }
        if (whytheAccent != null) {
            whytheAccent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Settings.System.putIntForUser(resolver,
                            Settings.System.ACCENT_PICKER, 30, UserHandle.USER_CURRENT);
                    dismiss();
                }
            });
        }

        GridLayout gridlayout;
        if (mView != null) {

            int intOrientation = getResources().getConfiguration().orientation;
            gridlayout = mView.findViewById(R.id.Gridlayout);
            // Lets split this up instead of creating two different layouts
            // just so we can change the columns
            if (intOrientation == Configuration.ORIENTATION_PORTRAIT) {
                gridlayout.setColumnCount(5);
            } else {
                gridlayout.setColumnCount(8);
            }
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        ContentResolver resolver = getActivity().getContentResolver();

        if (which == AlertDialog.BUTTON_NEGATIVE) {
           dismiss();
        }
        if (which == AlertDialog.BUTTON_NEUTRAL) {
           Settings.System.putIntForUser(resolver,
                   Settings.System.ACCENT_PICKER, 0, UserHandle.USER_CURRENT);
           dismiss();
        }
    }

    public static void show(Fragment parent) {
        if (!parent.isAdded()) return;

        final AccentPicker dialog = new AccentPicker();
        dialog.setTargetFragment(parent, 0);
        dialog.show(parent.getFragmentManager(), TAG_ACCENT_PICKER);
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.BOOTLEG;
    }
}

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

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.om.IOverlayManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.ServiceManager;
import android.provider.Settings;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.android.internal.logging.nano.MetricsProto;
import com.android.internal.statusbar.ThemeAccentUtils;

import com.android.settings.R;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;

public class QsTileStyles extends InstrumentedDialogFragment implements OnClickListener {

    private static final String TAG_QS_TILE_STYLES = "qs_tile_style";

    private View mView;

    private IOverlayManager mOverlayManager;
    private int mCurrentUserId;
    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOverlayManager = IOverlayManager.Stub.asInterface(
                ServiceManager.getService(Context.OVERLAY_SERVICE));
        mCurrentUserId = ActivityManager.getCurrentUser();
        mContext = getActivity();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mView = LayoutInflater.from(getActivity()).inflate(R.layout.qs_styles_main, null);

        if (mView != null) {
            initView();
            setAlpha(mContext.getResources());
        }

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
        LinearLayout squircle = mView.findViewById(R.id.QsTileStyleSquircle);
        setLayout("1", squircle);

        LinearLayout teardrop = mView.findViewById(R.id.QsTileStyleTearDrop);
        setLayout("2", teardrop);

        LinearLayout deletround = mView.findViewById(R.id.QsTileStyleJustIcons);
        setLayout("3", deletround);

        LinearLayout inktober = mView.findViewById(R.id.QsTileStyleInkDrop);
        setLayout("4", inktober);

        LinearLayout shishunights = mView.findViewById(R.id.QsTileStyleShishuNights);
        setLayout("5", shishunights);

        LinearLayout circlegradient = mView.findViewById(R.id.QsTileStyleCircleGradient);
        setLayout("6", circlegradient);

        LinearLayout wavey = mView.findViewById(R.id.QsTileStyleWavey);
        setLayout("7", wavey);

        LinearLayout circledualtone = mView.findViewById(R.id.QsTileStyleCircleDualTone);
        setLayout("8", circledualtone);

        LinearLayout memedosquare = mView.findViewById(R.id.QsTileStyleMemedoSquare);
        setLayout("9", memedosquare);

        LinearLayout pokesign = mView.findViewById(R.id.QsTileStylePokesign);
        setLayout("10", pokesign);

        LinearLayout ninja = mView.findViewById(R.id.QsTileStyleNinja);
        setLayout("11", ninja);

        LinearLayout dottedcircle = mView.findViewById(R.id.QsTileStyleDottedCircle);
        setLayout("12", dottedcircle);

        LinearLayout shishuink = mView.findViewById(R.id.QsTileStyleShishuInk);
        setLayout("13", shishuink);

        LinearLayout attemptmountain = mView.findViewById(R.id.QsTileStyleAttemptMountain);
        setLayout("14", attemptmountain);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        ContentResolver resolver = getActivity().getContentResolver();

        if (which == AlertDialog.BUTTON_NEGATIVE) {
            dismiss();
        }
        if (which == AlertDialog.BUTTON_NEUTRAL) {
            Settings.System.putIntForUser(resolver,
                    Settings.System.QS_TILE_STYLE, 0, mCurrentUserId);
            dismiss();
        }
    }

    public static void show(Fragment parent) {
        if (!parent.isAdded()) return;

        final QsTileStyles dialog = new QsTileStyles();
        dialog.setTargetFragment(parent, 0);
        dialog.show(parent.getFragmentManager(), TAG_QS_TILE_STYLES);
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.BOOTLEG;
    }

    private void setLayout(final String style, final LinearLayout layout) {
        final ContentResolver resolver = getActivity().getContentResolver();
        if (layout != null) {
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Settings.System.putIntForUser(resolver,
                            Settings.System.QS_TILE_STYLE, Integer.parseInt(style), mCurrentUserId);
                    dismiss();
                }
            });
        }
    }

    private void setAlpha(Resources res) {
        LinearLayout squircle = mView.findViewById(R.id.QsTileStyleSquircle);
        LinearLayout teardrop = mView.findViewById(R.id.QsTileStyleTearDrop);
        LinearLayout deletround = mView.findViewById(R.id.QsTileStyleJustIcons);
        LinearLayout inktober = mView.findViewById(R.id.QsTileStyleInkDrop);
        LinearLayout shishunights = mView.findViewById(R.id.QsTileStyleShishuNights);
        LinearLayout circlegradient = mView.findViewById(R.id.QsTileStyleCircleGradient);
        LinearLayout wavey = mView.findViewById(R.id.QsTileStyleWavey);
        LinearLayout circledualtone = mView.findViewById(R.id.QsTileStyleCircleDualTone);
        LinearLayout memedosquare = mView.findViewById(R.id.QsTileStyleMemedoSquare);
        LinearLayout pokesign = mView.findViewById(R.id.QsTileStylePokesign);
        LinearLayout ninja = mView.findViewById(R.id.QsTileStyleNinja);
        LinearLayout dottedcircle = mView.findViewById(R.id.QsTileStyleDottedCircle);
        LinearLayout shishuink = mView.findViewById(R.id.QsTileStyleShishuInk);
        LinearLayout attemptmountain = mView.findViewById(R.id.QsTileStyleAttemptMountain);

        TypedValue typedValue = new TypedValue();
        res.getValue(R.dimen.qs_styles_layout_opacity, typedValue, true);
        float mLayoutOpacity = typedValue.getFloat();

        if (ThemeAccentUtils.isUsingQsTileStyles(mOverlayManager, mCurrentUserId, 1 )) {
            squircle.setAlpha((float) 1.0);
            teardrop.setAlpha(mLayoutOpacity);
            deletround.setAlpha(mLayoutOpacity);
            inktober.setAlpha(mLayoutOpacity);
            shishunights.setAlpha(mLayoutOpacity);
            circlegradient.setAlpha(mLayoutOpacity);
            wavey.setAlpha(mLayoutOpacity);
            circledualtone.setAlpha(mLayoutOpacity);
            memedosquare.setAlpha(mLayoutOpacity);
            pokesign.setAlpha(mLayoutOpacity);
            ninja.setAlpha(mLayoutOpacity);
            dottedcircle.setAlpha(mLayoutOpacity);
            shishuink.setAlpha(mLayoutOpacity);
            attemptmountain.setAlpha(mLayoutOpacity);
        } else if (ThemeAccentUtils.isUsingQsTileStyles(mOverlayManager, mCurrentUserId, 2 )) {
            squircle.setAlpha(mLayoutOpacity);
            teardrop.setAlpha((float) 1.0);
            deletround.setAlpha(mLayoutOpacity);
            inktober.setAlpha(mLayoutOpacity);
            shishunights.setAlpha(mLayoutOpacity);
            circlegradient.setAlpha(mLayoutOpacity);
            wavey.setAlpha(mLayoutOpacity);
            circledualtone.setAlpha(mLayoutOpacity);
            memedosquare.setAlpha(mLayoutOpacity);
            pokesign.setAlpha(mLayoutOpacity);
            ninja.setAlpha(mLayoutOpacity);
            dottedcircle.setAlpha(mLayoutOpacity);
            shishuink.setAlpha(mLayoutOpacity);
            attemptmountain.setAlpha(mLayoutOpacity);
        } else if (ThemeAccentUtils.isUsingQsTileStyles(mOverlayManager, mCurrentUserId, 3 )) {
            squircle.setAlpha(mLayoutOpacity);
            teardrop.setAlpha(mLayoutOpacity);
            deletround.setAlpha((float) 1.0);
            inktober.setAlpha(mLayoutOpacity);
            shishunights.setAlpha(mLayoutOpacity);
            circlegradient.setAlpha(mLayoutOpacity);
            wavey.setAlpha(mLayoutOpacity);
            circledualtone.setAlpha(mLayoutOpacity);
            memedosquare.setAlpha(mLayoutOpacity);
            pokesign.setAlpha(mLayoutOpacity);
            ninja.setAlpha(mLayoutOpacity);
            dottedcircle.setAlpha(mLayoutOpacity);
            shishuink.setAlpha(mLayoutOpacity);
            attemptmountain.setAlpha(mLayoutOpacity);
        } else if (ThemeAccentUtils.isUsingQsTileStyles(mOverlayManager, mCurrentUserId, 4 )) {
            squircle.setAlpha(mLayoutOpacity);
            teardrop.setAlpha(mLayoutOpacity);
            deletround.setAlpha(mLayoutOpacity);
            inktober.setAlpha((float) 1.0);
            shishunights.setAlpha(mLayoutOpacity);
            circlegradient.setAlpha(mLayoutOpacity);
            wavey.setAlpha(mLayoutOpacity);
            circledualtone.setAlpha(mLayoutOpacity);
            memedosquare.setAlpha(mLayoutOpacity);
            pokesign.setAlpha(mLayoutOpacity);
            ninja.setAlpha(mLayoutOpacity);
            dottedcircle.setAlpha(mLayoutOpacity);
            shishuink.setAlpha(mLayoutOpacity);
            attemptmountain.setAlpha(mLayoutOpacity);
        } else if (ThemeAccentUtils.isUsingQsTileStyles(mOverlayManager, mCurrentUserId, 5 )) {
            squircle.setAlpha(mLayoutOpacity);
            teardrop.setAlpha(mLayoutOpacity);
            deletround.setAlpha(mLayoutOpacity);
            inktober.setAlpha(mLayoutOpacity);
            shishunights.setAlpha((float) 1.0);
            circlegradient.setAlpha(mLayoutOpacity);
            wavey.setAlpha(mLayoutOpacity);
            circledualtone.setAlpha(mLayoutOpacity);
            memedosquare.setAlpha(mLayoutOpacity);
            pokesign.setAlpha(mLayoutOpacity);
            ninja.setAlpha(mLayoutOpacity);
            dottedcircle.setAlpha(mLayoutOpacity);
            shishuink.setAlpha(mLayoutOpacity);
            attemptmountain.setAlpha(mLayoutOpacity);
        } else if (ThemeAccentUtils.isUsingQsTileStyles(mOverlayManager, mCurrentUserId, 6 )) {
            squircle.setAlpha(mLayoutOpacity);
            teardrop.setAlpha(mLayoutOpacity);
            deletround.setAlpha(mLayoutOpacity);
            inktober.setAlpha(mLayoutOpacity);
            shishunights.setAlpha(mLayoutOpacity);
            circlegradient.setAlpha((float) 1.0);
            wavey.setAlpha(mLayoutOpacity);
            circledualtone.setAlpha(mLayoutOpacity);
            memedosquare.setAlpha(mLayoutOpacity);
            pokesign.setAlpha(mLayoutOpacity);
            ninja.setAlpha(mLayoutOpacity);
            dottedcircle.setAlpha(mLayoutOpacity);
            shishuink.setAlpha(mLayoutOpacity);
            attemptmountain.setAlpha(mLayoutOpacity);
        } else if (ThemeAccentUtils.isUsingQsTileStyles(mOverlayManager, mCurrentUserId, 7 )) {
            squircle.setAlpha(mLayoutOpacity);
            teardrop.setAlpha(mLayoutOpacity);
            deletround.setAlpha(mLayoutOpacity);
            inktober.setAlpha(mLayoutOpacity);
            shishunights.setAlpha(mLayoutOpacity);
            circlegradient.setAlpha(mLayoutOpacity);
            wavey.setAlpha((float) 1.0);
            circledualtone.setAlpha(mLayoutOpacity);
            memedosquare.setAlpha(mLayoutOpacity);
            pokesign.setAlpha(mLayoutOpacity);
            ninja.setAlpha(mLayoutOpacity);
            dottedcircle.setAlpha(mLayoutOpacity);
            shishuink.setAlpha(mLayoutOpacity);
            attemptmountain.setAlpha(mLayoutOpacity);
        } else if (ThemeAccentUtils.isUsingQsTileStyles(mOverlayManager, mCurrentUserId, 8 )) {
            squircle.setAlpha(mLayoutOpacity);
            teardrop.setAlpha(mLayoutOpacity);
            deletround.setAlpha(mLayoutOpacity);
            inktober.setAlpha(mLayoutOpacity);
            shishunights.setAlpha(mLayoutOpacity);
            circlegradient.setAlpha(mLayoutOpacity);
            wavey.setAlpha(mLayoutOpacity);
            circledualtone.setAlpha((float) 1.0);
            memedosquare.setAlpha(mLayoutOpacity);
            pokesign.setAlpha(mLayoutOpacity);
            ninja.setAlpha(mLayoutOpacity);
            dottedcircle.setAlpha(mLayoutOpacity);
            shishuink.setAlpha(mLayoutOpacity);
            attemptmountain.setAlpha(mLayoutOpacity);
        } else if (ThemeAccentUtils.isUsingQsTileStyles(mOverlayManager, mCurrentUserId, 9 )) {
            squircle.setAlpha(mLayoutOpacity);
            teardrop.setAlpha(mLayoutOpacity);
            deletround.setAlpha(mLayoutOpacity);
            inktober.setAlpha(mLayoutOpacity);
            shishunights.setAlpha(mLayoutOpacity);
            circlegradient.setAlpha(mLayoutOpacity);
            wavey.setAlpha(mLayoutOpacity);
            circledualtone.setAlpha(mLayoutOpacity);
            memedosquare.setAlpha((float) 1.0);
            pokesign.setAlpha(mLayoutOpacity);
            ninja.setAlpha(mLayoutOpacity);
            dottedcircle.setAlpha(mLayoutOpacity);
            shishuink.setAlpha(mLayoutOpacity);
            attemptmountain.setAlpha(mLayoutOpacity);
        } else if (ThemeAccentUtils.isUsingQsTileStyles(mOverlayManager, mCurrentUserId, 10 )) {
            squircle.setAlpha(mLayoutOpacity);
            teardrop.setAlpha(mLayoutOpacity);
            deletround.setAlpha(mLayoutOpacity);
            inktober.setAlpha(mLayoutOpacity);
            shishunights.setAlpha(mLayoutOpacity);
            circlegradient.setAlpha(mLayoutOpacity);
            wavey.setAlpha(mLayoutOpacity);
            circledualtone.setAlpha(mLayoutOpacity);
            memedosquare.setAlpha(mLayoutOpacity);
            pokesign.setAlpha((float) 1.0);
            ninja.setAlpha(mLayoutOpacity);
            dottedcircle.setAlpha(mLayoutOpacity);
            shishuink.setAlpha(mLayoutOpacity);
            attemptmountain.setAlpha(mLayoutOpacity);
        } else if (ThemeAccentUtils.isUsingQsTileStyles(mOverlayManager, mCurrentUserId, 11 )) {
            squircle.setAlpha(mLayoutOpacity);
            teardrop.setAlpha(mLayoutOpacity);
            deletround.setAlpha(mLayoutOpacity);
            inktober.setAlpha(mLayoutOpacity);
            shishunights.setAlpha(mLayoutOpacity);
            circlegradient.setAlpha(mLayoutOpacity);
            wavey.setAlpha(mLayoutOpacity);
            circledualtone.setAlpha(mLayoutOpacity);
            memedosquare.setAlpha(mLayoutOpacity);
            pokesign.setAlpha(mLayoutOpacity);
            ninja.setAlpha((float) 1.0);
            dottedcircle.setAlpha(mLayoutOpacity);
            shishuink.setAlpha(mLayoutOpacity);
            attemptmountain.setAlpha(mLayoutOpacity);
        } else if (ThemeAccentUtils.isUsingQsTileStyles(mOverlayManager, mCurrentUserId, 12 )) {
            squircle.setAlpha(mLayoutOpacity);
            teardrop.setAlpha(mLayoutOpacity);
            deletround.setAlpha(mLayoutOpacity);
            inktober.setAlpha(mLayoutOpacity);
            shishunights.setAlpha(mLayoutOpacity);
            circlegradient.setAlpha(mLayoutOpacity);
            wavey.setAlpha(mLayoutOpacity);
            circledualtone.setAlpha(mLayoutOpacity);
            memedosquare.setAlpha(mLayoutOpacity);
            pokesign.setAlpha(mLayoutOpacity);
            ninja.setAlpha(mLayoutOpacity);
            dottedcircle.setAlpha((float) 1.0);
            shishuink.setAlpha(mLayoutOpacity);
            attemptmountain.setAlpha(mLayoutOpacity);
        } else if (ThemeAccentUtils.isUsingQsTileStyles(mOverlayManager, mCurrentUserId, 13 )) {
            squircle.setAlpha(mLayoutOpacity);
            teardrop.setAlpha(mLayoutOpacity);
            deletround.setAlpha(mLayoutOpacity);
            inktober.setAlpha(mLayoutOpacity);
            shishunights.setAlpha(mLayoutOpacity);
            circlegradient.setAlpha(mLayoutOpacity);
            wavey.setAlpha(mLayoutOpacity);
            circledualtone.setAlpha(mLayoutOpacity);
            memedosquare.setAlpha(mLayoutOpacity);
            pokesign.setAlpha(mLayoutOpacity);
            ninja.setAlpha(mLayoutOpacity);
            dottedcircle.setAlpha(mLayoutOpacity);
            shishuink.setAlpha((float) 1.0);
            attemptmountain.setAlpha(mLayoutOpacity);
        } else if (ThemeAccentUtils.isUsingQsTileStyles(mOverlayManager, mCurrentUserId, 14 )) {
            squircle.setAlpha(mLayoutOpacity);
            teardrop.setAlpha(mLayoutOpacity);
            deletround.setAlpha(mLayoutOpacity);
            inktober.setAlpha(mLayoutOpacity);
            shishunights.setAlpha(mLayoutOpacity);
            circlegradient.setAlpha(mLayoutOpacity);
            wavey.setAlpha(mLayoutOpacity);
            circledualtone.setAlpha(mLayoutOpacity);
            memedosquare.setAlpha(mLayoutOpacity);
            pokesign.setAlpha(mLayoutOpacity);
            ninja.setAlpha(mLayoutOpacity);
            dottedcircle.setAlpha(mLayoutOpacity);
            shishuink.setAlpha(mLayoutOpacity);
            attemptmountain.setAlpha((float) 1.0);
        } else {
            squircle.setAlpha((float) 1.0);
            teardrop.setAlpha((float) 1.0);
            deletround.setAlpha((float) 1.0);
            inktober.setAlpha((float) 1.0);
            shishunights.setAlpha((float) 1.0);
            circlegradient.setAlpha((float) 1.0);
            wavey.setAlpha((float) 1.0);
            circledualtone.setAlpha((float) 1.0);
            memedosquare.setAlpha((float) 1.0);
            pokesign.setAlpha((float) 1.0);
            ninja.setAlpha((float) 1.0);
            dottedcircle.setAlpha((float) 1.0);
            shishuink.setAlpha((float) 1.0);
            attemptmountain.setAlpha((float) 1.0);
        }
    }
}

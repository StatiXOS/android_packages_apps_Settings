/*
 * Copyright (C) 2018 The Dirty Unicorns Project
 * Copyright (C) 2018 CarbonROM
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
 *
 * preference for managing custom fonts
 */

package com.android.settings.display;

import com.android.settingslib.CustomDialogPreference;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.FontInfo;
import android.content.IFontService;
import android.content.pm.PackageManager;
import android.content.SharedPreferences;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.AttributeSet;
import android.widget.ListView;
import android.widget.Toast;

import com.android.settings.R;

public class FontDialogPreference extends CustomDialogPreference {
    private static final String TAG = "FontDialogPreference";
    private Context mContext;
    private IFontService mFontService;
    private static final String SUBS_PACKAGE = "projekt.substratum";
    private SharedPreferences prefs;
    private boolean mFirstTimeDialog;

    public FontDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mFontService = IFontService.Stub.asInterface(
                ServiceManager.getService("crfont"));
        mFirstTimeDialog = mContext.getSharedPreferences("FONT_CHOOSER", Context.MODE_PRIVATE).getBoolean("firstTimeFontDialog", true);
    }

    @Override
    protected void onPrepareDialogBuilder(Builder builder,
            DialogInterface.OnClickListener listener) {
        super.onPrepareDialogBuilder(builder, listener);
        if(isPackageInstalled(SUBS_PACKAGE, mContext) && mFirstTimeDialog) {
            mFirstTimeDialog = false;
            mContext.getSharedPreferences("FONT_CHOOSER", Context.MODE_PRIVATE)
                .edit()
                .putBoolean("firstTimeFontDialog", false)
                .commit();
            builder.setIcon(R.drawable.ic_settings_about);
            builder.setMessage(R.string.font_subs_info);
            builder.setCancelable(false);
            builder.setNegativeButton(mContext.getString(com.android.internal.R.string.ok),
                    listener);
        } else {
            FontListAdapter adapter = new FontListAdapter(mContext);
            DialogInterface.OnClickListener l = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FontInfo info = adapter.getItem(which);
                    Toast.makeText(mContext, mContext.getString(R.string.font_picker_reboot_recommended), Toast.LENGTH_LONG).show();
                    try {
                        mFontService.applyFont(info);
                    } catch (RemoteException e) {
                    }
                }
            };
            builder.setIcon(R.drawable.font_dialog_icon);
            builder.setAdapter(adapter, l);
            builder.setCancelable(false);
            builder.setNegativeButton(mContext.getString(com.android.internal.R.string.cancel),
                    listener);
        }
    }

    @Override
    protected void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_NEGATIVE) {
            dialog.dismiss();
        }
    }

    private boolean isPackageInstalled(String package_name, Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            pm.getPackageInfo(package_name, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

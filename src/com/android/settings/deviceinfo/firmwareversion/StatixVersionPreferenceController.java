/*
 * Copyright (C) 2019 The LineageOS Project
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

package com.android.settings.deviceinfo.firmwareversion;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.VisibleForTesting;
import androidx.preference.Preference;

import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.Sliceable;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.RestrictedLockUtilsInternal;

public class StatixVersionPreferenceController extends BasePreferenceController {

    private static final String TAG = "statixVersionDialogCtrl";
    private static final int DELAY_TIMER_MILLIS = 500;
    private static final int ACTIVITY_TRIGGER_COUNT = 3;

    private static final String KEY_STATIX_VERSION_PROP = "ro.statix.version";

    private final PackageManager mPackageManager;
    private final long[] mHits = new long[ACTIVITY_TRIGGER_COUNT];

    public StatixVersionPreferenceController(Context context, String key) {
        super(context, key);
        mPackageManager = mContext.getPackageManager();
    }

    @Override
    public int getAvailabilityStatus() {
        return AVAILABLE;
    }

    @Override
    public boolean useDynamicSliceSummary() {
        return true;
    }

    @Override
    public boolean isSliceable() {
        return true;
    }

    @Override
    public CharSequence getSummary() {
        return SystemProperties.get(KEY_STATIX_VERSION_PROP,
                mContext.getString(R.string.unknown));
    }

    @Override
    public boolean handlePreferenceTreeClick(Preference preference) {
        if (!TextUtils.equals(preference.getKey(), getPreferenceKey())) {
            return false;
        }
            final Intent intent = new Intent()
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(mContext.getString(R.string.statix_uri)));
            if (mPackageManager.queryIntentActivities(intent, 0).isEmpty()) {
                // Don't send out the intent to stop crash
                Log.w(TAG, "queryIntentActivities() returns empty");
                return true;
            }

            mContext.startActivity(intent);
            return true;
    }

    /**
     * Copies the array onto itself to remove the oldest hit.
     */
    @VisibleForTesting
    void arrayCopy() {
        System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
    }

    @Override
    public void copy() {
        Sliceable.setCopyContent(mContext, getSummary(),
                mContext.getText(R.string.statix_version));
    }
}

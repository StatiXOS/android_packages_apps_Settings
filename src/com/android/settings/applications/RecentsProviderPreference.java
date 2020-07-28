/*
 * Copyright (C) 2019 The Android Open Source Project
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
 * limitations under the License
 */

package com.android.settings.applications;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageInfo;
import android.content.pm.ResolveInfo;
import android.util.AttributeSet;

import com.android.internal.util.statix.Utils;

import com.android.settings.R;

import com.android.settingslib.applications.AppUtils;

import com.statix.support.preferences.SystemSettingListPreference;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RecentsProviderPreference extends SystemSettingListPreference {

    private List<String> mProviders;
    private PackageManager mPackageManager;

    public RecentsProviderPreference(Context context) {
         this(context, null);
     }

     public RecentsProviderPreference(Context context, AttributeSet attrs) {
         this(context, attrs, 0);
     }

     public RecentsProviderPreference(Context context, AttributeSet attrs, int defStyleAttr) {
         super(context, attrs, defStyleAttr);
         setLayoutResource(R.layout.provider_preference);
         init(context);
     }

    private void init(Context context) {
        mPackageManager = context.getPackageManager();
        mProviders = getProviders();
        List<String> appLabels = new ArrayList<String>();
        for (String provider : mProviders) appLabels.add(getAppLabel(provider.split("/")[0]).toString());
        setEntries(appLabels.toArray(new String[0]));
        setEntryValues(mProviders.toArray(new String[0]));
        setSummary(getAppLabel(Utils.getRecentsComponent(context).split("/")[0]).toString());
    }

    private CharSequence getAppLabel(String pkgName) {
        return AppUtils.getApplicationLabel(mPackageManager, pkgName);
    }

    private boolean isQuickstepProvider(String packageName) {
        Intent intent = new Intent("android.intent.action.QUICKSTEP_SERVICE").setPackage(packageName);
        return mPackageManager.resolveService(intent, 0) != null;
    }

    private List<String> getProviders() {
        List<PackageInfo> allApps = mPackageManager.getInstalledPackages(0);
        List<String> filterPkgs = allApps.stream().map(it -> it.packageName).filter(it -> isQuickstepProvider(it)).collect(Collectors.toList());
        List<String> ret = new ArrayList<String>();
        for (String pkg : filterPkgs) {
            ret.add(pkg + "/com.android.quickstep.RecentsActivity");
        }
        return ret;
    }
}

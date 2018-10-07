/*
 * Copyright (C) 2017 The Android Open Source Project
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
import android.os.SystemProperties;
import android.support.annotation.VisibleForTesting;

import com.android.settings.R;

public class SELinuxStatusDialogController {

    @VisibleForTesting
    private static final String PROPERTY_SELINUX_STATUS = "ro.build.selinux";
    @VisibleForTesting
    private static final int SELINUX_STATUS_LABEL_ID = R.id.selinux_status_label;
    @VisibleForTesting
    private static final int SELINUX_STATUS_VALUE_ID = R.id.selinux_status_value;

    private final FirmwareVersionDialogFragment mDialog;

    public SELinuxStatusDialogController(FirmwareVersionDialogFragment dialog) {
        mDialog = dialog;
    }

     /**
     * Populates the Android version field in the dialog and registers click listeners.
     */
    public void initialize() {
        final Context mContext = mDialog.getContext();
        if (!SELinux.isSELinuxEnabled()) {
            String status = mDialog.setText(SELINUX_STATUS_VALUE_ID, SystemProperties.get(PROPERTY_SELINUX_STATUS,
                mContext.getResources().getString(R.string.device_info_default)));
        } else if (!SELinux.isSELinuxEnforced()) {
            String status = mDialog.setText(SELINUX_STATUS_VALUE_ID, SystemProperties.get(PROPERTY_SELINUX_STATUS,
                mContext.getResources().getString(R.string.device_info_default)));
        }
    }
}


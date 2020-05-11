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

package com.android.settings.fuelgauge;

import android.app.settings.SettingsEnums;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.hardware.display.AmbientDisplayConfiguration;
import android.os.Bundle;
import android.os.UserHandle;
import android.provider.SearchIndexableResource;
import android.provider.Settings;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;

import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.search.SearchIndexable;
import com.statix.support.preferences.SystemSettingSwitchPreference;

import java.util.Arrays;
import java.util.List;

@SearchIndexable
public class BatteryLightSettings extends SettingsPreferenceFragment {

    private static final String TAG = "BatteryLightSettings";

    private AmbientDisplayConfiguration mAmbientDisplayConfig;

    private SystemSettingSwitchPreference mLowBatteryBlinking;
    private PreferenceCategory mColorCategory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.battery_light_settings);
        PreferenceScreen prefSet = getPreferenceScreen();
        mColorCategory = (PreferenceCategory) findPreference("battery_light_cat");

        mLowBatteryBlinking = (SystemSettingSwitchPreference) prefSet.findPreference("battery_light_low_blinking");
        if (!getResources().getBoolean(
                        com.android.internal.R.bool.config_ledCanPulse)) {
            prefSet.removePreference(mLowBatteryBlinking);
        }

        if (!getResources().getBoolean(com.android.internal.R.bool.config_multiColorBatteryLed)) {
            prefSet.removePreference(mColorCategory);
        }
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.CUSTOM;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public static final SearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
            new BaseSearchIndexProvider() {
                @Override
                public List<SearchIndexableResource> getXmlResourcesToIndex(
                        Context context, boolean enabled) {
                    final SearchIndexableResource sir = new SearchIndexableResource(context);
                    sir.xmlResId = R.xml.battery_light_settings;
                    return Arrays.asList(sir);
                }

                @Override
                protected boolean isPageSearchEnabled(Context context) {
                    // All rows in this screen can lead to a different page, so suppress everything
                    // from this page to remove duplicates.
                    return false;
                }
            };
}

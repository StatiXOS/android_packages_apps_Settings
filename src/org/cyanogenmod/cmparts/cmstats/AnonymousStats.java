/*
 * Copyright (C) 2015 The CyanogenMod Project
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

package org.cyanogenmod.cmparts.cmstats;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.UserHandle;
import android.provider.SearchIndexableResource;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceScreen;
import android.support.v14.preference.SwitchPreference;

import org.cyanogenmod.cmparts.R;
import org.cyanogenmod.cmparts.SettingsPreferenceFragment;

import cyanogenmod.providers.CMSettings;

import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.search.Indexable;
import com.android.settingslib.search.SearchIndexable;

import java.util.ArrayList;
import java.util.List;

@SearchIndexable
public class AnonymousStats extends SettingsPreferenceFragment implements Indexable {

    private static final String PREF_FILE_NAME = "CMStats";
    /* package */ static final String ANONYMOUS_OPT_IN = "pref_anonymous_opt_in";
    /* package */ static final String ANONYMOUS_LAST_CHECKED = "pref_anonymous_checked_in";

    /* package */ static final String KEY_LAST_JOB_ID = "last_job_id";
    /* package */ static final int QUEUE_MAX_THRESHOLD = 1000;

    public static final String KEY_STATS = "stats_collection";

    SwitchPreference mStatsSwitch;

    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREF_FILE_NAME, 0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.anonymous_stats);
        mStatsSwitch = (SwitchPreference) findPreference(KEY_STATS);
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        if (preference == mStatsSwitch) {
            boolean checked = mStatsSwitch.isChecked();
            if (checked) {
                // clear opt out flags
                CMSettings.Secure.putIntForUser(getContentResolver(),
                        CMSettings.Secure.STATS_COLLECTION_REPORTED, 0, UserHandle.USER_OWNER);
            }
            // will initiate opt out sequence if necessary
            ReportingServiceManager.setAlarm(getActivity());
            return true;
        }
        return super.onPreferenceTreeClick(preference);
    }

    public static void updateLastSynced(Context context) {
        getPreferences(context)
                .edit()
                .putLong(ANONYMOUS_LAST_CHECKED,System.currentTimeMillis())
                .commit();
    }

    private static int getLastJobId(Context context) {
        return getPreferences(context).getInt(KEY_LAST_JOB_ID, 0);
    }

    private static void setLastJobId(Context context, int id) {
        getPreferences(context)
                .edit()
                .putInt(KEY_LAST_JOB_ID, id)
                .commit();
    }

    public static int getNextJobId(Context context) {
        int lastId = getLastJobId(context);
        if (lastId >= QUEUE_MAX_THRESHOLD) {
            lastId = 1;
        } else {
            lastId += 1;
        }
        setLastJobId(context, lastId);
        return lastId;
    }

    public static final SearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
            new BaseSearchIndexProvider() {
                @Override
                public List<SearchIndexableResource> getXmlResourcesToIndex(Context context,
                                                                            boolean enabled) {
                    ArrayList<SearchIndexableResource> result =
                            new ArrayList<SearchIndexableResource>();

                    SearchIndexableResource sir = new SearchIndexableResource(context);
                    sir.xmlResId = R.xml.anonymous_stats;
                    result.add(sir);
                    return result;
                }

                @Override
                public List<String> getNonIndexableKeys(Context context) {
                    List<String> keys = super.getNonIndexableKeys(context);
                    return keys;
                }
            };
}

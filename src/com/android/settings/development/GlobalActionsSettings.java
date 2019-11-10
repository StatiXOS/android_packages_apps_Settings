/*
 * Copyright (C) 2017 CarbonROM
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
package com.android.settings.development;

import android.os.Bundle;
import android.os.UserHandle;
import android.provider.Settings;

import com.android.settings.R;
import com.android.settings.CustomSettingsPreferenceFragment;

public class GlobalActionsSettings extends CustomSettingsPreferenceFragment {
    private static final String GLOBAL_ACTIONS_POWER = "global_actions_power";
    private static final String GLOBAL_ACTIONS_RESTART = "global_actions_restart";
    private static final String GLOBAL_ACTIONS_LOCKDOWN = "global_actions_lockdown";
    private static final String GLOBAL_ACTIONS_SCREENSHOT = "global_actions_screenshot";
    private static final String GLOBAL_ACTIONS_AIRPLANE = "global_actions_airplane";
    private static final String GLOBAL_ACTIONS_SETTINGS = "global_actions_settings";
    private static final String GLOBAL_ACTIONS_FLASHLIGHT = "global_actions_flashlight";
    private static final String GLOBAL_ACTIONS_USERS = "global_actions_users";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.global_actions);

        addCustomPreference(findPreference(GLOBAL_ACTIONS_POWER), SYSTEM_TWO_STATE, STATE_ON);
        addCustomPreference(findPreference(GLOBAL_ACTIONS_RESTART), SYSTEM_TWO_STATE, STATE_ON);
        addCustomPreference(findPreference(GLOBAL_ACTIONS_LOCKDOWN), SYSTEM_TWO_STATE, STATE_OFF);
        addCustomPreference(findPreference(GLOBAL_ACTIONS_SCREENSHOT), SYSTEM_TWO_STATE, STATE_ON);
        addCustomPreference(findPreference(GLOBAL_ACTIONS_AIRPLANE), SYSTEM_TWO_STATE, STATE_OFF);
        addCustomPreference(findPreference(GLOBAL_ACTIONS_SETTINGS), SYSTEM_TWO_STATE, STATE_OFF);
        addCustomPreference(findPreference(GLOBAL_ACTIONS_FLASHLIGHT), SYSTEM_TWO_STATE, STATE_OFF);
        addCustomPreference(findPreference(GLOBAL_ACTIONS_USERS), SYSTEM_TWO_STATE, STATE_OFF);
    }
}

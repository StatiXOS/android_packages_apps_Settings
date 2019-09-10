package com.google.android.settings.overlay;

import android.app.AppGlobals;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import androidx.annotation.Keep;

import com.android.settings.accounts.AccountFeatureProvider;
import com.google.android.settings.accounts.AccountFeatureProviderGoogleImpl;
import com.android.settings.applications.ApplicationFeatureProvider;
import com.google.android.settings.applications.ApplicationFeatureProviderGoogleImpl;

import com.android.settings.fuelgauge.PowerUsageFeatureProvider;
import com.google.android.settings.fuelgauge.PowerUsageFeatureProviderGoogleImpl;

@Keep
public final class FeatureFactoryImpl extends com.android.settings.overlay.FeatureFactoryImpl {
    private AccountFeatureProvider mAccountFeatureProvider;
    private ApplicationFeatureProvider mApplicationFeatureProvider;
    private PowerUsageFeatureProvider mPowerUsageProvider;

    @Override
    public AccountFeatureProvider getAccountFeatureProvider() {
        if (mAccountFeatureProvider == null) {
            mAccountFeatureProvider = new AccountFeatureProviderGoogleImpl();
        }
        return mAccountFeatureProvider;
    }

    @Override
    public PowerUsageFeatureProvider getPowerUsageFeatureProvider(Context context) {
        if (mPowerUsageProvider == null) {
            mPowerUsageProvider = new PowerUsageFeatureProviderGoogleImpl(context.getApplicationContext());
        }
        return mPowerUsageProvider;
    }

    @Override
    public ApplicationFeatureProvider getApplicationFeatureProvider(Context context) {
        if (mApplicationFeatureProvider == null) {
            Context applicationContext = context.getApplicationContext();
            DevicePolicyManager dpManager =
                (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
            mApplicationFeatureProvider = new ApplicationFeatureProviderGoogleImpl(applicationContext,
                    applicationContext.getPackageManager(), AppGlobals.getPackageManager(), dpManager);
        }
        return mApplicationFeatureProvider;
    }
}

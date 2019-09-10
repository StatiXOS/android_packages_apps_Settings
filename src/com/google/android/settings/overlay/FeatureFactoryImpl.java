package com.google.android.settings.overlay;

import android.content.Context;
import androidx.annotation.Keep;

import com.android.settings.accounts.AccountFeatureProvider;
import com.google.android.settings.accounts.AccountFeatureProviderGoogleImpl;

import com.android.settings.fuelgauge.PowerUsageFeatureProvider;
import com.google.android.settings.fuelgauge.PowerUsageFeatureProviderGoogleImpl;

@Keep
public final class FeatureFactoryImpl extends com.android.settings.overlay.FeatureFactoryImpl {
    private AccountFeatureProvider mAccountFeatureProvider;
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
}

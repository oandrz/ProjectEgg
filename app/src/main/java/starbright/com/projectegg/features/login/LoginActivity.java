package starbright.com.projectegg.features.login;

import android.os.Bundle;

import starbright.com.projectegg.features.base.BaseActivityWithToolbar;
import starbright.com.projectegg.features.base.BaseActivityWithoutToolbar;

public class LoginActivity extends BaseActivityWithoutToolbar {

    private static final String LOGIN_FRAGMENT_TAG = "LOGIN_FRAGMENT_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoginFragment fragment = (LoginFragment) getSupportFragmentManager()
                .findFragmentByTag(LOGIN_FRAGMENT_TAG);
        if (fragment == null) {
            fragment = LoginFragment.newInstance();
        }
        setFragment(LOGIN_FRAGMENT_TAG, fragment);
    }
}
package com.ataulm.wutson.settings;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import com.ataulm.wutson.BuildConfig;
import com.ataulm.wutson.R;

public class SettingsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public static class SettingsFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            configureAboutCategory();
        }

        private void configureAboutCategory() {
            addPreferencesFromResource(R.xml.preference_about);
            PreferenceCategory category = (PreferenceCategory) findPreference(getString(R.string.settings_pref_category_key_about));

            Preference softwareLicensesPreference = category.findPreference(getString(R.string.settings_pref_key_oss));
            softwareLicensesPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

                @Override
                public boolean onPreferenceClick(Preference preference) {
                    startActivity(new Intent(getActivity(), SoftwareLicensesActivity.class));
                    return true;
                }

            });

            Preference versionPreference = category.findPreference(getString(R.string.settings_pref_key_version));
            versionPreference.setSummary(BuildConfig.VERSION_NAME);
            versionPreference.setOnPreferenceClickListener(EasterEggPreferenceClickListener.newInstanceNoHints(getActivity(),
                    new EasterEggPreferenceClickListener.EasterEgg() {

                        @Override
                        public void onClickSpammed() {
                            String text = "Version code: " + BuildConfig.VERSION_CODE + "\n" + BuildConfig.BUILD_TIME;
                            Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
                        }

                    }));
        }
    }

}

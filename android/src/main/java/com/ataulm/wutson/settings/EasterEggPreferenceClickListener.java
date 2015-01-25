package com.ataulm.wutson.settings;

import android.content.Context;
import android.preference.Preference;
import android.widget.Toast;

class EasterEggPreferenceClickListener implements Preference.OnPreferenceClickListener {

    private static final int DEFAULT_CLICKS_DISPLAY_THRESHOLD = 3;
    private static final int DEFAULT_CLICKS_TARGET = 7;
    private static final String CLICKS_TEASER_PATTERN = "%d clicks to go";

    private final Context context;
    private final EasterEgg easterEgg;
    private final int clicksDisplayThreshold;
    private final int clicksTarget;

    private Toast toast;
    private int count;

    EasterEggPreferenceClickListener(Context context, EasterEgg easterEgg) {
        this(context, easterEgg, DEFAULT_CLICKS_DISPLAY_THRESHOLD, DEFAULT_CLICKS_TARGET);
    }

    EasterEggPreferenceClickListener(Context context, EasterEgg easterEgg, int clicksDisplayThreshold, int clicksTarget) {
        this.context = context;
        this.easterEgg = easterEgg;
        this.clicksDisplayThreshold = clicksDisplayThreshold;
        this.clicksTarget = clicksTarget;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        count++;
        hideOldMessage();

        if (clickSpammedEnough()) {
            easterEgg.onClickSpammed();
            return true;
        }

        if (userIsCurious() && clickNotSpammedEnough()) {
            teaseUserWithHint();
        }
        return true;
    }

    private void hideOldMessage() {
        if (toast != null) {
            toast.cancel();
        }
    }

    private boolean clickSpammedEnough() {
        return count >= clicksTarget;
    }

    private boolean userIsCurious() {
        return count >= clicksDisplayThreshold;
    }

    private boolean clickNotSpammedEnough() {
        return !clickSpammedEnough();
    }

    private void teaseUserWithHint() {
        int clicksLeft = clicksTarget - count;
        toast = Toast.makeText(context, String.format(CLICKS_TEASER_PATTERN, clicksLeft), Toast.LENGTH_LONG);
        toast.show();
    }

    interface EasterEgg {

        void onClickSpammed();

    }

}

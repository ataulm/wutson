package com.ataulm.wutson.navigation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ataulm.wutson.DeveloperError;
import com.ataulm.wutson.R;

public class NavigationDrawerItemView extends LinearLayout {

    private ImageView iconImageView;
    private TextView titleTextView;

    public NavigationDrawerItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.setOrientation(HORIZONTAL);
        View.inflate(getContext(), R.layout.merge_navigation_drawer_item, this);

        iconImageView = (ImageView) findViewById(R.id.navigation_drawer_item_image_icon);
        titleTextView = (TextView) findViewById(R.id.navigation_drawer_item_text_title);
    }

    @Override
    public void setActivated(boolean activated) {
        super.setActivated(activated);
        if (activated) {
            iconImageView.setColorFilter(getResources().getColor(R.color.navigation_drawer_item_icon_color_filter_activated));
        } else {
            iconImageView.setColorFilter(getResources().getColor(R.color.navigation_drawer_item_icon_color_filter_default));
        }
    }

    @Override
    public final void setOrientation(int orientation) {
        throw DeveloperError.methodCannotBeCalledOutsideThisClass();
    }

    void bind(NavigationDrawerItem item) {
        iconImageView.setImageResource(item.getIconResId());
        titleTextView.setText(item.getTitle());
    }

}

package com.ataulm.wutson.search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.ataulm.wutson.R;

public class SearchWithSuggestionsView extends FrameLayout {

    private RecyclerView suggestionsRecyclerView;
    private View upButton;
    private View voiceButton;

    public SearchWithSuggestionsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_search_with_suggestions, this);
        suggestionsRecyclerView = ((RecyclerView) findViewById(R.id.search_with_suggestions_rv_suggestions));
        upButton = findViewById(R.id.search_with_suggestions_button_up);
        voiceButton = findViewById(R.id.search_with_suggestions_button_voice);
    }

}

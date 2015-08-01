package com.ataulm.wutson.search;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ataulm.wutson.R;
import com.ataulm.wutson.jabber.Jabber;

public class SearchOverlay extends FrameLayout {

    private EditText inputEditText;
    private RecyclerView searchSuggestionsRecyclerView;
    private SearchSuggestionAdapter suggestionsAdapter;
    private View voiceSearchButton;
    private View clearTextButton;

    public SearchOverlay(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.view_search_overlay, this);
        initialiseViews();
    }

    private void initialiseViews() {
        View backButton = findViewById(R.id.search_overlay_dismiss);
        backButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissSearchOverlay();
                Jabber.toastDisplayer().display("onDismissSearchOverlay");
            }
        });
        inputEditText = (EditText) findViewById(R.id.search_overlay_input);
        clearTextButton = findViewById(R.id.search_overlay_clear_text);
        voiceSearchButton = findViewById(R.id.search_overlay_voice_search);
        voiceSearchButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Jabber.toastDisplayer().display("onVoiceSearch click");
            }
        });
        clearTextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clearQuery();
                Jabber.toastDisplayer().display("onClearQuery click");
            }
        });
        searchSuggestionsRecyclerView = (RecyclerView) findViewById(R.id.search_overlay_list_suggestions);
        searchSuggestionsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private LayoutInflater layoutInflater() {
        return LayoutInflater.from(getContext());
    }

    private void dismissSearchOverlay() {
        setVisibility(GONE);
        clearQuery();
    }

    private void displayClearTextInsteadOfVoiceSearch() {
        showVoiceSearch(false);
    }

    private void displayVoiceSearchInsteadOfClearText() {
        showVoiceSearch(true);
    }

    private void showVoiceSearch(boolean showVoiceSearch) {
        if (showVoiceSearch) {
            voiceSearchButton.setVisibility(VISIBLE);
            clearTextButton.setVisibility(GONE);
        } else {
            clearTextButton.setVisibility(VISIBLE);
            voiceSearchButton.setVisibility(GONE);
        }
    }

    private void clearQuery() {
        inputEditText.setText(null);
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == VISIBLE) {
            inputEditText.requestFocus();
        }
    }

    public void setSearchListener(final SearchListener listener) {
        inputEditText.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable text) {
                String query = text.toString().trim();
                if (query.length() > 0) {
                    displayClearTextInsteadOfVoiceSearch();
                } else {
                    displayVoiceSearchInsteadOfClearText();
                }
                listener.onQueryUpdated(query);
            }
        });

        inputEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                if (isSubmitAction(actionId, event)) {
                    listener.onQuerySubmitted(textView.getText().toString());
                    return true;
                } else {
                    return false;
                }
            }

            private boolean isSubmitAction(int actionId, KeyEvent event) {
                return actionId == EditorInfo.IME_ACTION_SEARCH
                        || event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER;
            }
        });
    }

    public void update(SearchSuggestions searchSuggestions) {
        if (searchSuggestionsRecyclerView.getAdapter() == null) {
            setNewAdapter(searchSuggestions);
        } else {
            suggestionsAdapter.update(searchSuggestions);
        }
    }

    private void setNewAdapter(SearchSuggestions searchSuggestions) {
        suggestionsAdapter = new SearchSuggestionAdapter(layoutInflater());
        suggestionsAdapter.update(searchSuggestions);
        searchSuggestionsRecyclerView.setAdapter(suggestionsAdapter);
    }

    public interface SearchListener {

        void onQueryUpdated(String query);

        void onQuerySubmitted(String query);

    }

}

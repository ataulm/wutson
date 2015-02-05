package com.ataulm.wutson.show;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ataulm.wutson.Presenter;
import com.ataulm.wutson.R;

public class CharacterView extends RelativeLayout implements Presenter<Character> {

    private TextView characterTextView;
    private TextView actorTextView;

    public CharacterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CharacterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        View.inflate(getContext(), R.layout.merge_character, this);
        characterTextView = (TextView) findViewById(R.id.character_text_name);
        actorTextView = (TextView) findViewById(R.id.character_text_actor);
    }

    @Override
    public void present(Character character) {
        characterTextView.setText(character.getName());
        actorTextView.setText(character.getActor().getName());
    }

}

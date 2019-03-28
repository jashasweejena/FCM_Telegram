package com.example.firebasenotificationfinal;

import android.view.View;
import android.widget.TextView;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.materialize.holder.StringHolder;

import java.util.List;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;

public class Forward extends AbstractItem<Forward, Forward.ViewHolder> {
    String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public Forward(String message){
        this.message = message;
    }

    public Forward(){}

    @NonNull
    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    @Override
    public int getType() {
        return R.id.framelayoutparent;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.card;
    }


    protected static class ViewHolder extends FastAdapter.ViewHolder<Forward> {
        @BindView(R.id.text_card)
        TextView message;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public void bindView(Forward item, List<Object> payloads) {
            StringHolder.applyTo(new StringHolder(item.message), message);
        }

        @Override
        public void unbindView(Forward item) {
            message.setText(null);
        }
    }
}

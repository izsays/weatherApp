package com.vodworks.myweatherapp.model.eventbus;

import android.view.View;

public class EventBusLongClick {

    private int position;
    private View view;
    private int parentIndex;

    public EventBusLongClick(int position, View view, int parentInex) {
        this.position = position;
        this.view = view;
        this.parentIndex = parentInex;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public int getParentIndex() {
        return parentIndex;
    }

    public void setParentIndex(int parentIndex) {
        this.parentIndex = parentIndex;
    }
}

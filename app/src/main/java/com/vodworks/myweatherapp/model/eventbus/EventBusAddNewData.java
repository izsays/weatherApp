package com.vodworks.myweatherapp.model.eventbus;

public class EventBusAddNewData {

    private int parentIndex;

    public EventBusAddNewData(int parentIndex) {
        this.parentIndex = parentIndex;
    }

    public int getParentIndex() {
        return parentIndex;
    }

    public void setParentIndex(int parentIndex) {
        this.parentIndex = parentIndex;
    }

}

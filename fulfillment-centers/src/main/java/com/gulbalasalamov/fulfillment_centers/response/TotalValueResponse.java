package com.gulbalasalamov.fulfillment_centers.response;

import com.gulbalasalamov.fulfillment_centers.model.enums.Status;

public class TotalValueResponse {
    private double value;
    private Status status;

    public TotalValueResponse(double value, Status status) {
        this.value = value;
        this.status = status;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}

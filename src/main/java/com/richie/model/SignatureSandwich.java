package com.richie.model;

import java.util.ArrayList;

public class SignatureSandwich extends Sandwich{
    private String signatureName;

    public SignatureSandwich(String name, String breadType, String size, String signatureName) {
        super(signatureName, breadType, size);
        this.signatureName = signatureName;
    }

    @Override
    public double getPrice() {
        double regularPrice = super.getPrice();
        return regularPrice * .90; //10% discount
    }

    public String getSignatureName() {
        return signatureName;
    }

    public void setSignatureName(String signatureName) {
        this.signatureName = signatureName;
    }
}

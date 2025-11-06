package com.richie.model;

public class SignaturePokeBowl extends PokeBowl {
    private String signatureName;

    public SignaturePokeBowl(String name, String base, String size, String signatureName) {
        super(signatureName, base, size);
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

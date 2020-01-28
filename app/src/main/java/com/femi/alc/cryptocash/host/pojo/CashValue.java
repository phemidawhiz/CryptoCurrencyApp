package com.franklyn.alc.cryptocash.host.pojo;

/**
 * Created by AGBOMA franklyn on 10/10/17.
 */

public class CashValue {

    private String cashCode ="";
    private double cashEquivalent = 0.0;

    public CashValue(String cashCode, double cashEquivalent) {
        this.cashCode = cashCode;
        this.cashEquivalent = cashEquivalent;
    }

    public String getCashCode() {
        return cashCode;
    }

    public double getCashEquivalent() {
        return cashEquivalent;
    }
}

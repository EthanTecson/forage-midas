package com.jpmc.midascore.foundation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Incentive {
    private float Amount;

    public Incentive() {}

    public Incentive(float amount){
        this.Amount = amount;
    }

    public float getAmount()  {
        return Amount;
    }

    public void setAmount(float amount) {
        this.Amount = amount;
    }
}

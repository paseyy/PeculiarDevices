package com.pasey.peculiardevices.blockentities.util;

public class EnergyStorageParams {
    public EnergyStorageParams(int capacity, int maxReceive, int maxExtract, int energy) {
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
        this.energy = energy;
    }
    public int capacity;
    public int maxReceive;
    public int maxExtract;
    public int energy;
}

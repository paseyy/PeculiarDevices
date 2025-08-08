package com.pasey.peculiardevices.blockentities.util;

public class EnergyStorageParams {
    public EnergyStorageParams(int capacity, int maxReceive, int maxDistribute, int energy) {
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxDistribute = maxDistribute;
        this.energy = energy;
    }
    public int capacity;
    public int maxReceive;
    public int maxDistribute;
    public int energy;
}

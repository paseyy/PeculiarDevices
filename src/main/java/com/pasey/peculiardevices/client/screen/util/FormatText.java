package com.pasey.peculiardevices.client.screen.util;

public class FormatText {
    public static String formatCapacity(int energy, int maxEnergy) {
        float div;
        String unit;

        if (maxEnergy >= 1_000_000) {
            div = 1_000_000.0f;
            unit = "MFE";
        } else if (maxEnergy >= 1_000) {
            div = 1_000.0f;
            unit = "kFE";
        } else {
            unit = "FE";
            div = 1;
        }
        return String.format("%.1f", energy/div) + "/" + String.format("%.1f", maxEnergy/div) + " " + unit;
    }

    public static String formatEnergy(int energy, boolean withUnit) {
        if (energy >= 1_000_000) {
            return String.format("%.2f", (float) energy / 1_000_000.0) + (withUnit ? "MFE" : "");
        } else if (energy >= 1_000) {
            return String.format("%.2f", (float) energy / 1_000.0) + (withUnit ? "kFE" : "");
        } else {
            return energy + (withUnit ? " FE" : "");
        }
    }
}
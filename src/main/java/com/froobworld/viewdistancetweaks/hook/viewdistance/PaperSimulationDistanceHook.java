package com.froobworld.viewdistancetweaks.hook.viewdistance;

import com.froobworld.viewdistancetweaks.util.ViewDistanceUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.lang.reflect.InvocationTargetException;

public class PaperSimulationDistanceHook extends SpigotSimulationDistanceHook {

    @Override
    public void setDistance(World world, int value) {
        value = ViewDistanceUtils.clampViewDistance(value);
        if (value != getDistance(world)) {
            on(world).call("setSimulationDistance", value);
        }
    }

    public static boolean isCompatible() {
        try {
            Class.forName("org.bukkit.World")
                    .getMethod("setSimulationDistance", int.class);
        } catch (Exception exception) {
            return false;
        }
        try {
            for (World world : Bukkit.getWorlds()) {
                try {
                    on(world).call("setSimulationDistance", 0);
                } catch (Exception exception) {
                    if (exception instanceof InvocationTargetException) {
                        throw exception.getCause();
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            return true;
        } catch (Throwable otherException) {
            return false;
        }
        return true;
    }

}

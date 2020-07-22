package org.jubaroo.mods.wurm.shopSign;

import org.gotti.wurmunlimited.modloader.interfaces.PreInitable;
import org.gotti.wurmunlimited.modloader.interfaces.ServerStartedListener;
import org.gotti.wurmunlimited.modloader.interfaces.WurmServerMod;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;
import org.jubaroo.mods.wurm.shopSign.actions.ShopSignAction;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ShopSignMod implements WurmServerMod, PreInitable, ServerStartedListener {
    private static final Logger logger = Logger.getLogger(ShopSignMod.class.getName());

    @Override
    public void preInit() {
        ModActions.init();
    }

    @Override
    public void onServerStarted() {
        logger.log(Level.INFO, String.format("%s onServerStarted started", ShopSignMod.class.getName()));
        try {
            ModActions.registerAction(new ShopSignAction());
        } catch (IllegalArgumentException | ClassCastException e) {
            logger.log(Level.SEVERE, String.format("%s onServerStarted error", ShopSignMod.class.getName()), e);
        }
        logger.log(Level.INFO, String.format("%s all onServerStarted completed", ShopSignMod.class.getName()));
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    //TODO
    // add an item that will change the sign material when used like essence of wood item?
    // Add an action to use a gold lump on a shop sign to change the material to gold. Make a check for a lump of a certain weight?

}

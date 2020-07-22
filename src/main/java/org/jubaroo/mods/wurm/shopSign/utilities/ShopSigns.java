package org.jubaroo.mods.wurm.shopSign.utilities;

import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemList;

import java.util.Arrays;
import java.util.List;

public enum ShopSigns {
    // void entries to make the aux data start at 23 for our signs
    Invalid00("Invalid", ""),
    Invalid01("Invalid", ""),
    Invalid02("Invalid", ""),
    Invalid03("Invalid", ""),
    Invalid04("Invalid", ""),
    Invalid05("Invalid", ""),
    Invalid06("Invalid", ""),
    Invalid07("Invalid", ""),
    Invalid08("Invalid", ""),
    Invalid09("Invalid", ""),
    Invalid10("Invalid", ""),
    Invalid11("Invalid", ""),
    Invalid12("Invalid", ""),
    Invalid13("Invalid", ""),
    Invalid14("Invalid", ""),
    Invalid15("Invalid", ""),
    Invalid16("Invalid", ""),
    Invalid17("Invalid", ""),
    Invalid18("Invalid", ""),
    Invalid19("Invalid", ""),
    Invalid20("Invalid", ""),
    Invalid21("Invalid", ""),
    Invalid22("Invalid", ""),
    // new shop signs
    APOTHECARY("Apothecary", "", Applicables.ShopSign);

    public final String name, display, suffix;
    public final List<Applicables> applicables;

    ShopSigns(String name, String suffix, Applicables... applicables) {
        this(name, name, suffix, applicables);
    }

    ShopSigns(String name, String display, String suffix, Applicables... applicables) {
        this.name = name;
        this.suffix = suffix;
        this.display = display;
        this.applicables = Arrays.asList(applicables);
    }

    public static boolean isValidDesign(byte id) {
        return id > 0 && id < values().length;
    }


    public static String getModelSuffix(byte id) {
        if (isValidDesign(id))
            return values()[id].suffix;
        else
            return "";
    }

    public static String getName(byte id) {
        if (isValidDesign(id))
            return values()[id].name;
        else
            return "unknown";
    }


    public static String getDisplay(byte id) {
        if (isValidDesign(id))
            return values()[id].display;
        else
            return "unknown";
    }

    public static boolean canSetDesign(Item item, byte kingdom) {
        if (!isValidDesign(kingdom)) return false;
        ShopSigns rec = values()[kingdom];
        for (Applicables app : rec.applicables) {
            if (app.handler.canApply(item)) return true;
        }
        return false;
    }

    public static boolean canSetAnyDesign(Item item) {
        for (Applicables app : Applicables.values()) {
            if (app.handler.canApply(item)) return true;
        }
        return false;
    }

    public static void setDesign(Item item, byte kingdom) {
        if (!isValidDesign(kingdom)) return;
        ShopSigns rec = values()[kingdom];
        for (Applicables app : rec.applicables) {
            if (app.handler.canApply(item)) {
                app.handler.doApply(item, kingdom);
                return;
            }
        }
    }

    public boolean canSetDesign(Item item) {
        for (Applicables app : applicables) {
            if (app.handler.canApply(item)) return true;
        }
        return false;
    }

    // ===================

    public enum Applicables {
        ShopSign(new AuxApplicable(ItemList.signShop));

        public final IApplicable handler;

        Applicables(IApplicable handler) {
            this.handler = handler;
        }
    }

    interface IApplicable {
        boolean canApply(Item item);

        void doApply(Item item, byte kingdom);
    }

    static class AuxApplicable implements IApplicable {
        private final int templateId;

        AuxApplicable(int templateId) {
            this.templateId = templateId;
        }

        @Override
        public boolean canApply(Item item) {
            return item.getTemplateId() == templateId;
        }

        @Override
        public void doApply(Item item, byte kingdom) {
            item.setAuxData(kingdom);
        }
    }

}

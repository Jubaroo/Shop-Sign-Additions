package org.jubaroo.mods.wurm.shopSign.actions;

import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemList;
import com.wurmonline.server.questions.ShopSignQuestion;
import org.gotti.wurmunlimited.modsupport.actions.ActionPerformer;
import org.gotti.wurmunlimited.modsupport.actions.BehaviourProvider;
import org.gotti.wurmunlimited.modsupport.actions.ModAction;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;
import org.jubaroo.mods.wurm.shopSign.utilities.ShopSigns;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class ShopSignAction implements ModAction, ActionPerformer, BehaviourProvider {
    private static final Logger logger = Logger.getLogger(ShopSignAction.class.getName());
    private ActionEntry actionEntry;

    public ShopSignAction() {
        actionEntry = ActionEntry.createEntry((short) ModActions.getNextActionId(), "Additional sign designs", "changing", new int[]{
                ActionTypesProxy.ACTION_TYPE_ENEMY_ALWAYS,
                ActionTypesProxy.ACTION_TYPE_ALWAYS_USE_ACTIVE_ITEM
        });
        ModActions.registerAction(actionEntry);
    }

    @Override
    public short getActionId() {
        return actionEntry.getNumber();
    }

    @Override
    public BehaviourProvider getBehaviourProvider() {
        return this;
    }

    @Override
    public ActionPerformer getActionPerformer() {
        return this;
    }

    private boolean canUse(Creature performer, Item source, Item target) {
        try {
            if (performer.isPlayer() && source != null && target != null &&
                    (target.canHavePermissions() && target.mayManage(performer)) || target.lastOwner == performer.getWurmId() || target.getTopParentOrNull() == performer.getInventory()) {
                if (target.getTemplateId() == ItemList.signShop)
                    return ShopSigns.canSetAnyDesign(target);
            }
        } catch (NullPointerException e) {
            logger.severe(String.format("ShopSignAction error: %s", e));
        }
        return false;
    }

    @Override
    public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item target) {
        if (canUse(performer, source, target))
            return Collections.singletonList(actionEntry);
        else
            return null;
    }

    @Override
    public boolean action(Action action, Creature performer, Item source, Item target, short num, float counter) {
        if (!canUse(performer, source, target)) {
            performer.getCommunicator().sendAlertServerMessage("Do you have the correct item activated and/or targeting the correct item/creature? Please try again or use /support to open a ticket with as much info as possible, and the staff will get to it as soon as possible.");
            return true;
        }
        if (target.getTemplateId() == ItemList.signShop) {
            ShopSignQuestion.send(performer, target);
        }
        return true;
    }
}

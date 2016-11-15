package de.alphahelix.partysystem;

import org.bukkit.entity.Player;

/**
 * Created by AlphaHelixDev.
 */
public class ItemFunction {

    private ItemFunctionEnum itemFunctionEnum;
    private Prompts prompt;
    private String inventoryInsideFile;
    private boolean isPromptingItem;

    public ItemFunction(ItemFunctionEnum itemFunctionEnum) {
        this.itemFunctionEnum = itemFunctionEnum;
        if (itemFunctionEnum.getInventoryToPerfomOn() == null && itemFunctionEnum.getPrompt() != null) {
            this.prompt = itemFunctionEnum.getPrompt();
            isPromptingItem = true;
        } else if (itemFunctionEnum.getInventoryToPerfomOn() != null && itemFunctionEnum.getPrompt() == null) {
            isPromptingItem = false;
            this.inventoryInsideFile = itemFunctionEnum.getInventoryToPerfomOn();
            this.prompt = Prompts.UNDEFINED;
        } else {
            isPromptingItem = false;
            this.inventoryInsideFile = "null";
            this.prompt = Prompts.UNDEFINED;
        }
    }

    public void perfomFunction(Player p) {
        if (this.isPromptingItem) {
            if (this.getPrompt() == Prompts.INVITE_PLAYER) {
                //TODO: Invite Player
            } else if (this.getPrompt() == Prompts.KICK_PLAYER) {
                //TODO: Kick Player
            } else if (this.getPrompt() == Prompts.MESSAGE_PLAYER) {
                //TODO: Message a Player
            }
        } else if (this.getItemFunctionEnum() == ItemFunctionEnum.OPEN_INV) {
            p.closeInventory();
            p.openInventory(PartySystem.getPartySystem().getRegister().getInventoryFile().getInventory(this.getInventory()));
        } else if (this.getItemFunctionEnum() == ItemFunctionEnum.CLOSE_INV) {
            p.closeInventory();
        }
    }

    public ItemFunctionEnum getItemFunctionEnum() {
        return itemFunctionEnum;
    }

    public ItemFunction setItemFunctionEnum(ItemFunctionEnum itemFunctionEnum) {
        this.itemFunctionEnum = itemFunctionEnum;
        return this;
    }

    public Prompts getPrompt() {
        return prompt;
    }

    public ItemFunction setPrompt(Prompts prompt) {
        this.prompt = prompt;
        return this;
    }

    public String getInventory() {
        return inventoryInsideFile;
    }

    public ItemFunction setInventory(String inventory) {
        this.inventoryInsideFile = inventory;
        return this;
    }

    public boolean isPromptingItem() {
        return this.isPromptingItem;
    }

    public ItemFunction setPromptingItem(boolean promptingItem) {
        isPromptingItem = promptingItem;
        return this;
    }
}

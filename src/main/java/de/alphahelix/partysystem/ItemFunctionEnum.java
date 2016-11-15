package de.alphahelix.partysystem;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/**
 * Created by AlphaHelixDev.
 */
public enum ItemFunctionEnum {

    NONE(""),
    OPEN_INV(""),
    CLOSE_INV(""),
    PROMPT_PLAYER("");

    private String inventoryToPerfomOn;
    private Prompts prompt;

    ItemFunctionEnum(String i) {
        this.inventoryToPerfomOn = i;
    }

    public ItemFunctionEnum setToPerfomOn(String toPerformOn, Prompts p) {
        if (p != null && toPerformOn == null)
            this.prompt = p;
        else
            this.inventoryToPerfomOn = toPerformOn;
        return this;
    }

    public String getInventoryToPerfomOn() {
        if(this == PROMPT_PLAYER || this == NONE) return null;
        return this.inventoryToPerfomOn;
    }

    public Prompts getPrompt() {
        if(this != PROMPT_PLAYER || this == NONE) return null;
        return this.prompt;
    }
}

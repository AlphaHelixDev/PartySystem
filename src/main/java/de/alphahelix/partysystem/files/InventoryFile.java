package de.alphahelix.partysystem.files;

import de.alphahelix.partysystem.ItemFunction;
import de.alphahelix.partysystem.PartySystem;
import de.alphahelix.partysystem.ItemFunctionEnum;
import de.alphahelix.partysystem.Prompts;
import de.popokaka.alphalibary.file.SimpleFile;
import de.popokaka.alphalibary.inventorys.InventoryItem;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public class InventoryFile extends SimpleFile<PartySystem> {

    public InventoryFile(PartySystem pl) {
        super("plugins/PartySystem", "inventories.pss", pl);
    }

    public void addValues() {
        if(!configContains("Inventories.example")) {
            setInventory("Inventories.example", "Inventory name", 1,
                    new InventoryItem(Material.NAME_TAG, 3, 0, "Item name on Slot 4 with data of 0", new ItemFunction(ItemFunctionEnum.OPEN_INV), "1. &aline of lore", "2. &bline of lore", "3. &aline of lore"),
                    new InventoryItem(Material.BOOK, 5, 0, "&eAdd more items if you want to", new ItemFunction(ItemFunctionEnum.CLOSE_INV),"use the config ...", "... or just do /createGUI"));
        }
        if (!configContains("Inventories.main"))
            setInventory("Inventories.main", "Alpha Parties", 1,
                    new InventoryItem(Material.NAME_TAG, 3, 0, "&eInvite a player", new ItemFunction(ItemFunctionEnum.PROMPT_PLAYER).setPrompt(Prompts.INVITE_PLAYER), " ", "&7Invites a player to join", "&7you in a new party."),
                    new InventoryItem(Material.BOOK, 5, 0, "&eView Invites", new ItemFunction(ItemFunctionEnum.OPEN_INV).setInventory("Inventories.invites"), " ", "&7Manage invites to parties."));

        if (!configContains("Items.join.open GUI"))
            setItem("Items.join.open GUI", Material.CHEST, 4, "&aParties", 0, new ItemFunction(ItemFunctionEnum.OPEN_INV).setInventory("Inventories.main"), " ");
    }

    public void createInventory(String which, Inventory i) {
        setInventory("Inventories." + which, i);
    }
}

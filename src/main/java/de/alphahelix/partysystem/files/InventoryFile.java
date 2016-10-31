package de.alphahelix.partysystem.files;

import de.alphahelix.partysystem.PartySystem;
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
                    new InventoryItem(Material.NAME_TAG, 4, 0, "Item name on Slot 4 with data of 0", "1. &aline of lore", "2. &bline of lore", "3. &aline of lore"),
                    new InventoryItem(Material.BOOK, 6, 0, "&eAdd more items if you want to", "use the config ...", "... or just do /createGUI"));
        }
        if (!configContains("Inventories.main"))
            setInventory("Inventories.main", "Alpha Parties", 1,
                    new InventoryItem(Material.NAME_TAG, 4, 0, "&eInvite a player", " ", "&7Invites a player to join", "&7you in a new party."),
                    new InventoryItem(Material.BOOK, 6, 0, "&eView Invites", " ", "&7Manage invites to parties."));

        if (!configContains("Items.join.open GUI"))
            setItem("Items.join.open GUI", Material.CHEST, 5, "&aParties", 0, " ");
    }

    public void createInventory(String which, Inventory i) {
        updateInventory("Inventories." + which, i);
    }
}

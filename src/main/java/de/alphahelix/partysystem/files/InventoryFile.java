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
        setInventory("Inventories.main", "Alpha Parties", 1,
                new InventoryItem(Material.NAME_TAG, 4, 0, "&eInvite a player", " ", "&7Invites a player to join", "&7you in a new party."),
                new InventoryItem(Material.BOOK, 6, 0, "&eView Invites", " ", "&7Manage invites to parties."));

        setItem("Items.join.open GUI", Material.CHEST, 5, "&aParties", 0, " ");
    }

    public void createInventory(String which, Inventory i) {
        setInventory("Inventories." + which, i);
    }
}

package de.alphahelix.partysystem.inventories;

import de.alphahelix.partysystem.PartySystem;
import de.alphahelix.partysystem.Register;
import de.popokaka.alphalibary.inventorys.InventoryItem;
import de.popokaka.alphalibary.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class SelectInventory extends Util<PartySystem, Register> {

    private Inventory inv;

    public SelectInventory(PartySystem plugin, Register r) {
        super(plugin, r);
        inv = getRegister().getInventoryFile().getInventory("Inventories.main");
    }

    public void fillInventory() {
        InventoryItem inviteOther = getRegister().getInventoryFile().getItem("Items.select.invite item");
        InventoryItem invites = getRegister().getInventoryFile().getItem("Items.select.view invites");

        inv.setItem(inviteOther.getSlot(), inviteOther.getItemStack());
        inv.setItem(invites.getSlot(), invites.getItemStack());
    }

    public void openInventory(Player p) {
        p.openInventory(inv);
    }
}

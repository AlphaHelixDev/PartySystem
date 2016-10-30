package de.alphahelix.partysystem.inventories;

import de.alphahelix.partysystem.PartySystem;
import de.alphahelix.partysystem.Register;
import de.popokaka.alphalibary.inventorys.InventoryItem;
import de.popokaka.alphalibary.utils.Util;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class InvitesInventory extends Util<PartySystem, Register> {

    private Inventory inv;

    public InvitesInventory(PartySystem plugin, Register r) {
        super(plugin, r);
        inv = getRegister().getInventoryFile().getInventory("Inventories.main");
    }

    public void fillInventory() {
        InventoryItem back = getRegister().getInventoryFile().getItem("Items.invites.back");


        inv.setItem(back.getSlot(), back.getItemStack());
    }

    public void openInventory(Player p) {
        p.openInventory(inv);
    }
}

package de.alphahelix.partysystem.listener;

import de.alphahelix.partysystem.PartySystem;
import de.alphahelix.partysystem.Register;
import de.popokaka.alphalibary.listener.SimpleListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener extends SimpleListener<PartySystem, Register> {

    public InventoryClickListener(PartySystem plugin, Register register) {
        super(plugin, register);
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if (e.getClickedInventory() == null) return;
        if (e.getCurrentItem() == null) return;
        if (!e.getCurrentItem().hasItemMeta()) return;
        if(!e.getCurrentItem().getItemMeta().hasDisplayName()) return;

        String displayName = e.getCurrentItem().getItemMeta().getDisplayName();

        if(displayName.equals(getRegister().getInventoryFile().getItem("Items.select.invite item").getName())) {
            //TODO: open invite inventory
        }
        else if(displayName.equals(getRegister().getInventoryFile().getItem("Items.select.view invites").getName())) {
            //TODO: open view invites inventory
        }
    }
}

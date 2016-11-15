package de.alphahelix.partysystem.listener;

import de.alphahelix.partysystem.PartySystem;
import de.alphahelix.partysystem.Register;
import de.popokaka.alphalibary.item.ItemBuilder;
import de.popokaka.alphalibary.listener.SimpleListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;

public class InventoryClickListener extends SimpleListener<PartySystem, Register> {

    public ArrayList<String> names = new ArrayList<>();

    public InventoryClickListener(PartySystem plugin, Register register) {
        super(plugin, register);
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if (e.getClickedInventory() == null) return;
        if (e.getCurrentItem() == null) return;
        if (!e.getCurrentItem().hasItemMeta()) return;
        if (!e.getCurrentItem().getItemMeta().hasDisplayName()) return;

        String displayName = e.getCurrentItem().getItemMeta().getDisplayName();

        getRegister().getInventoryFile().scanInventoryForItemname("Inventories.main", displayName).getFunction().perfomFunction(p);

//        if (isEqual(displayName, getRegister().getInventoryFile().getItem("Inventories.main.content").getName())) {
//            names.add(p.getName());
//
//            p.closeInventory();
//
//            p.sendMessage(createMessageBox(getRegister().getMessageFile().getColorString("Player.invite Field")));
//        } else if (isEqual(displayName, getRegister().getInventoryFile().getItemsFromInventory("Inventories.main.content").get(1).getName())) {
//            //TODO: open invites inv
//        }
    }

    public boolean isAboutToCreateParty(Player p) {
        return names.contains(p.getName());
    }

    public void removePlayerFromList(Player p) {
        names.remove(p.getName());
    }
}

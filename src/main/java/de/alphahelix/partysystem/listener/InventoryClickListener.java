package de.alphahelix.partysystem.listener;

import de.alphahelix.partysystem.PartySystem;
import de.alphahelix.partysystem.Register;
import de.popokaka.alphalibary.listener.SimpleListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;

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

        if (isEqual(displayName, getRegister().getInventoryFile().getItem("Items.main.invite").getName())) {
            names.add(p.getName());

            p.closeInventory();

            p.sendMessage(createMessageBox(getRegister().getMessageFile().getColorString("Player.invite Field")));

        } else if (isEqual(displayName, getRegister().getInventoryFile().getItem("Items.main.view invites").getName())) {
            //TODO: open invites inv
        }
    }

    public boolean isAboutToCreateParty(Player p) {
        return names.contains(p.getName());
    }

    public void removePlayerFromList(Player p) {
        names.remove(p.getName());
    }
}

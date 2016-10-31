package de.alphahelix.partysystem.listener;

import de.alphahelix.partysystem.LegalInventories;
import de.alphahelix.partysystem.PartySystem;
import de.alphahelix.partysystem.Register;
import de.popokaka.alphalibary.listener.SimpleListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.ArrayList;
import java.util.HashMap;

public class InventoryCreateListener extends SimpleListener<PartySystem, Register> {

    private static ArrayList<String> currentCreators = new ArrayList<>();
    private static HashMap<String, LegalInventories> which = new HashMap<>();

    public InventoryCreateListener(PartySystem plugin, Register register) {
        super(plugin, register);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();

        if(currentCreators.contains(p.getName())) {
            currentCreators.remove(p.getName());
            getRegister().getInventoryFile().createInventory(which.get(p.getName()).name().toLowerCase(), e.getInventory());
            p.sendMessage(getPluginInstance().getPrefix() + "§7You've successfully created the Inventory for " + which.get(p.getName()).name().toLowerCase());
            which.remove(p.getName());
        }
    }

    public void addCreator(Player p) {
       if(!currentCreators.contains(p.getName())) currentCreators.add(p.getName());
    }

    public void setWhich(Player p, LegalInventories w) {
        which.put(p.getName(), w);
    }
}

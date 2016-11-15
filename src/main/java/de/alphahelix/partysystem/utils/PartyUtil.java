package de.alphahelix.partysystem.utils;

import de.alphahelix.partysystem.PartySystem;
import de.alphahelix.partysystem.Register;
import de.popokaka.alphalibary.utils.Util;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by AlphaHelixDev.
 */
public class PartyUtil extends Util<PartySystem, Register> {

    private static HashMap<String, ArrayList<String>> openInvites = new HashMap<>();

    public PartyUtil(PartySystem plugin, Register register) {
        super(plugin, register);
    }

    public void sendRequest(Player invitor, Player invited) {
        if (openInvites.containsKey(invitor.getName())) {
            ArrayList<String> updates = openInvites.get(invitor.getName());
            if (!updates.contains(invited.getName())) {
                updates.add(invited.getName());
                openInvites.put(invitor.getName(), updates);
            }
        } else {
            ArrayList<String> updates = new ArrayList<>();
            updates.add(invited.getName());
            openInvites.put(invitor.getName(), updates);
        }

    }

    public void acceptRequest(Player invited) {
        if (openInvites.containsKey(invited.getName())) {
            openInvites.remove(invited.getName());

        }
    }
}

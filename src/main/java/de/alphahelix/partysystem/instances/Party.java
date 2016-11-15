package de.alphahelix.partysystem.instances;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by AlphaHelixDev.
 */
public class Party {

    private Player leader;
    private ArrayList<String> members;

    public Party(Player leader, String... members) {
        this.leader = leader;
        ArrayList<String> toSet = new ArrayList<>();

        Collections.addAll(toSet, members);

        this.members = toSet;
    }

    public Player getLeader() {
        return leader;
    }

    public Party setLeader(Player leader) {
        this.leader = leader;
        return this;
    }

    public ArrayList<String> getMembers() {
        return members;
    }

    public Party setMembers(ArrayList<String> members) {
        this.members = members;
        return this;
    }

    public void addMember(Player p) {
        if(!getMembers().contains(p.getName())) getMembers().add(p.getName());
    }

    public void removeMember(Player p) {
        if(getMembers().contains(p.getName())) getMembers().remove(p.getName());
    }
}

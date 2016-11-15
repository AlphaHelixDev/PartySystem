package de.alphahelix.partysystem.files;

import de.alphahelix.partysystem.PartySystem;
import de.popokaka.alphalibary.file.SimpleFile;

public class MessageFile extends SimpleFile<PartySystem> {

    public MessageFile(PartySystem pl) {
        super("plugins/PartySystem", "messages.pss", pl);
    }

    @Override
    public void addValues() {
        setDefault("Plugin.prefix", "&7[&eAlphaParties&7] ");
        setDefault("Player.not online", "&7The player &c[player] &7isn't online!");
        setDefault("Player.invite Field", "&7Please enter a player.");
        setDefault("Player.not enough permissions", "&7You aren't allowed to perform this command!");
        setDefault("Party.invited", "&7You have been invited to the party of [player].");
        setDefault("Party.request send", "&7You've invited [player] to your party.");
    }
}

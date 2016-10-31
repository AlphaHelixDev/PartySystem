package de.alphahelix.partysystem;


import de.alphahelix.partysystem.commands.InventoryCreateCommand;
import de.alphahelix.partysystem.files.InventoryFile;
import de.alphahelix.partysystem.files.MessageFile;
import de.alphahelix.partysystem.listener.*;

public class Register {

    private PartySystem partySystem;

    private InventoryFile inventoryFile;
    private MessageFile messageFile;

    private JoinListener joinListener;
    private ClickerListener clickerListener;
    private InventoryClickListener inventoryClickListener;
    private ChatListener chatListener;
    private InventoryCreateListener inventoryCreateListener;

    public Register(PartySystem pl) {
        setPartySystem(pl);
    }

    public void register() {
        setInventoryFile(new InventoryFile(partySystem));
        setMessageFile(new MessageFile(partySystem));

        registerFiles();
        registerCommands();
        registerEvents();

        getPartySystem().setPrefix(getMessageFile().getColorString("Plugin.prefix"));
    }

    public void registerCommands() {
        new InventoryCreateCommand(partySystem, this);
    }

    public void registerEvents() {
        setJoinListener(new JoinListener(partySystem, this));
        setClickerListener(new ClickerListener(partySystem, this));
        setInventoryClickListener(new InventoryClickListener(partySystem, this));
        setChatListener(new ChatListener(partySystem, this));
        setInventoryCreateListener(new InventoryCreateListener(partySystem, this));
    }

    public void registerFiles() {
        getInventoryFile().addValues();
        getMessageFile().addValues();
    }

    public InventoryFile getInventoryFile() {
        return inventoryFile;
    }

    public Register setInventoryFile(InventoryFile inventoryFile) {
        this.inventoryFile = inventoryFile;
        return this;
    }

    public JoinListener getJoinListener() {
        return joinListener;
    }

    public Register setJoinListener(JoinListener joinListener) {
        this.joinListener = joinListener;
        return this;
    }

    public ClickerListener getClickerListener() {
        return clickerListener;
    }

    public Register setClickerListener(ClickerListener clickerListener) {
        this.clickerListener = clickerListener;
        return this;
    }

    public InventoryClickListener getInventoryClickListener() {
        return inventoryClickListener;
    }

    public Register setInventoryClickListener(InventoryClickListener inventoryClickListener) {
        this.inventoryClickListener = inventoryClickListener;
        return this;
    }

    public PartySystem getPartySystem() {
        return partySystem;
    }

    public Register setPartySystem(PartySystem partySystem) {
        this.partySystem = partySystem;
        return this;
    }

    public MessageFile getMessageFile() {
        return messageFile;
    }

    public Register setMessageFile(MessageFile messageFile) {
        this.messageFile = messageFile;
        return this;
    }

    public ChatListener getChatListener() {
        return chatListener;
    }

    public Register setChatListener(ChatListener chatListener) {
        this.chatListener = chatListener;
        return this;
    }

    public InventoryCreateListener getInventoryCreateListener() {
        return inventoryCreateListener;
    }

    public Register setInventoryCreateListener(InventoryCreateListener inventoryCreateListener) {
        this.inventoryCreateListener = inventoryCreateListener;
        return this;
    }
}
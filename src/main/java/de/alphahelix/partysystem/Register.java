package de.alphahelix.partysystem;


import de.alphahelix.partysystem.files.InventoryFile;
import de.alphahelix.partysystem.inventories.SelectInventory;
import de.alphahelix.partysystem.listener.ClickerListener;
import de.alphahelix.partysystem.listener.JoinListener;

public class Register {

    private PartySystem partySystem;

    private InventoryFile inventoryFile;

    private SelectInventory selectInventory;

    private JoinListener joinListener;
    private ClickerListener clickerListener;

    public Register(PartySystem pl) {
        this.partySystem = pl;
    }

    public void register() {
        setInventoryFile(new InventoryFile(partySystem));

        setSelectInventory(new SelectInventory(partySystem, this));

        registerFiles();
        registerCommands();
        registerEvents();
        fillInventorys();
    }

    public void registerCommands() {

    }

    public void registerEvents() {
        setJoinListener(new JoinListener(partySystem, this));
        setClickerListener(new ClickerListener(partySystem, this));
    }

    public void registerFiles() {
        getInventoryFile().addValues();
    }

    public void fillInventorys() {
        getSelectInventory().fillInventory();
    }

    public SelectInventory getSelectInventory() {
        return selectInventory;
    }

    public Register setSelectInventory(SelectInventory selectInventory) {
        this.selectInventory = selectInventory;
        return this;
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
}
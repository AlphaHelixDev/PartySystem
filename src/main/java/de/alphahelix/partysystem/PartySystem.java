package de.alphahelix.partysystem;

import de.popokaka.alphalibary.AlphaPlugin;

public class PartySystem extends AlphaPlugin {

    private static PartySystem partySystem;
    private Register register;

    @Override
    public void onEnable() {
        super.onEnable();
        setPartySystem(this);
        setRegister(new Register(this)).register();
    }

    public Register getRegister() {
        return register;
    }

    public Register setRegister(Register register) {
        this.register = register;
        return register;
    }

    public static PartySystem getPartySystem() {
        return partySystem;
    }

    public static void setPartySystem(PartySystem partySystem) {
        PartySystem.partySystem = partySystem;
    }
}

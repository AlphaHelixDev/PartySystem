package de.alphahelix.partysystem;

import de.popokaka.alphalibary.AlphaPlugin;

public class PartySystem extends AlphaPlugin {

    private Register register;

    @Override
    public void onEnable() {
        super.onEnable();
        setRegister(new Register(this)).register();
    }

    public Register getRegister() {
        return register;
    }

    public Register setRegister(Register register) {
        this.register = register;
        return register;
    }
}

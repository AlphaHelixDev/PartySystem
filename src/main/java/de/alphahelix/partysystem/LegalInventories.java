package de.alphahelix.partysystem;

import java.util.ArrayList;

public enum LegalInventories {

    MAIN,
    INVITES;

    public static ArrayList<String> getStringNameList() {
        ArrayList<String> tR = new ArrayList<>();

        for (LegalInventories li : LegalInventories.values())
            tR.add(li.name());

        return tR;
    }
}

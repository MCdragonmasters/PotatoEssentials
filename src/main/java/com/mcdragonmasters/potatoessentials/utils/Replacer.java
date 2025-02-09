package com.mcdragonmasters.potatoessentials.utils;

import lombok.Getter;
import net.kyori.adventure.text.Component;

public class Replacer {
    @Getter
    private final String oldText;
    @Getter
    private final String newText;
    @Getter
    private final Component newComp;
    private final boolean usesMiniMsg;
    private final boolean isComponent;

    public Replacer(String oldText, String newText) {
        this.oldText = oldText;
        this.newText = newText;
        this.newComp = null;
        this.usesMiniMsg = true;
        this.isComponent = false;
    }
    public Replacer(String oldText, String newText, boolean useMiniMsg) {
        this.oldText = oldText;
        this.newText = newText;
        this.newComp = null;
        this.usesMiniMsg = useMiniMsg;
        this.isComponent = false;
    }
    public Replacer(String oldText, Component newComp, boolean useMiniMsg) {
        this.oldText = oldText;
        this.newText = null;
        this.newComp = newComp;
        this.usesMiniMsg = useMiniMsg;
        this.isComponent = true;
    }
    public boolean usesMiniMsg() { return usesMiniMsg; }
    public boolean isComponent() { return isComponent; }
}

package com.mcdragonmasters.potatoessentials.utils;

import lombok.Getter;

public class Replacer {
    @Getter
    private final String oldText;
    @Getter
    private final String newText;
    private final boolean usesMiniMsg;
    private final boolean replaceRaw;

    public Replacer(String oldText, String newText) {
        this(oldText, newText, true);
    }
    public Replacer(String oldText, String newText, boolean useMiniMsg) {
        this(oldText, newText, useMiniMsg, true);
    }
    public Replacer(String oldText, String newText, boolean useMiniMsg, boolean replaceRaw) {
        this.oldText = oldText;
        this.newText = newText;
        this.usesMiniMsg = useMiniMsg;
        this.replaceRaw = replaceRaw;
    }
    public boolean usesMiniMsg() {
        return usesMiniMsg;
    }
    public boolean replaceRaw() {
        return replaceRaw;
    }
}

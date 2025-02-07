package com.mcdragonmasters.potatoessentials.utils;

import lombok.Getter;
import net.kyori.adventure.text.minimessage.tag.TagPattern;

public class Replacer {
    @Getter
    private final String oldText;
    @Getter
    private final String newText;
    private final boolean isMiniMsg;

    public Replacer(String oldText, String newText) {
        this.oldText = oldText;
        this.newText = newText;
        this.isMiniMsg = true;
    }
    public Replacer(String oldText, String newText, boolean useMiniMsg) {
        this.oldText = oldText;
        this.newText = newText;
        this.isMiniMsg = useMiniMsg;
    }
    public boolean isMiniMsg() { return isMiniMsg; }
}

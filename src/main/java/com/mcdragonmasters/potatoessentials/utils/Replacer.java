package com.mcdragonmasters.potatoessentials.utils;

import lombok.Getter;

@Getter
public class Replacer {

    private final String oldText;
    private final String newText;

    public Replacer(String oldText, String newText) {
        this.oldText = oldText;
        this.newText = newText;
    }

}

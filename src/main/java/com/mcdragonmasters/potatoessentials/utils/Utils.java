package com.mcdragonmasters.potatoessentials.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import java.text.DecimalFormat;

public class Utils {
//    @SuppressWarnings("deprecation")
//    public static ItemStack smeltedForm(ItemStack item) {
//        Material type = item.getType();
//        for (@NotNull Iterator<Recipe> it = Bukkit.recipeIterator(); it.hasNext(); ) {
//            if (it.next() instanceof BlastingRecipe recipe){
//                Material ingredient = recipe.getInput().getType();
//                if (type == ingredient){
//                    ItemStack result = item.clone();
//                    result.setType(recipe.getResult().getType());
//                    return result;
//                }
//            }
//        }
//        return item;
//    }
    public static String formatCoords(double x, double y, double z) {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(x) + ", " + df.format(y) + ", " + df.format(z);
    }
    public static Component miniMessage(String s) {
        return MiniMessage.miniMessage().deserialize(s);
    }
    public static Component miniMessage(String s, TagResolver... tagResolvers) {
        return MiniMessage.miniMessage().deserialize(s, tagResolvers);
    }
    public static String serialize(Component c) {return PlainTextComponentSerializer.plainText().serialize(c);}
}

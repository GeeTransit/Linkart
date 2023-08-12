package com.github.vini2003.linkart.utility;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;

import java.io.Serializable;

public class TextUtil {
    public static MutableText literal(Serializable value, Formatting formatting) {
        return literal(value.toString(), formatting);
    }

    public static MutableText literal(String value, Formatting formatting) {
        return Text.literal(value).formatted(formatting);
    }

    public static MutableText literal(Serializable value) {
        return Text.literal(value.toString());
    }

    public static String blockPosAsString(BlockPos pos) {
        return pos.getX() + ", " + pos.getY() + ", " + pos.getZ();
    }

    public static MutableText translatable(String key) {
        return Text.translatable(key);
    }

    public static MutableText translatable(String key, Object... args) {
        return Text.translatable(key, args);
    }
}

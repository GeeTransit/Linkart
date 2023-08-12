package com.github.vini2003.linkart.utility;

import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;

import java.io.Serializable;

public class TextUtil {
    public static MutableText literal(Serializable value, Formatting formatting) {
        return literal(value.toString(), formatting);
    }

    public static MutableText literal(String value, Formatting formatting) {
        return new LiteralText(value).formatted(formatting);
    }

    public static MutableText literal(Serializable value) {
        return new LiteralText(value.toString());
    }

    public static String blockPosAsString(BlockPos pos) {
        return pos.getX() + ", " + pos.getY() + ", " + pos.getZ();
    }

    public static MutableText translatable(String key) {
        return new TranslatableText(key);
    }

    public static MutableText translatable(String key, Object... args) {
        return new TranslatableText(key, args);
    }
}

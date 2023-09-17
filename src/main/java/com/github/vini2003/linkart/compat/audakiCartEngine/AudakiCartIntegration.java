package com.github.vini2003.linkart.compat.audaki;

import audaki.cart_engine.AudakiCartEngine;
import com.github.vini2003.linkart.api.LinkableMinecart;

// Ensure non-leading carts in a train always use modified Audaki Cart Engine
public class AudakiCartIntegration {
    public static void init() {
        AudakiCartEngine.registerModifiedEngineCheck(cart -> {
            if (((LinkableMinecart) (Object) cart).linkart$getFollowing() != null) {
                return true;
            }
            return null;
        });
    }
}

package com.github.vini2003.linkart.api;

import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.item.ItemStack;

public interface LinkableMinecart {
    AbstractMinecartEntity linkart$getFollowing();

    void linkart$setFollowing(AbstractMinecartEntity following);

    AbstractMinecartEntity linkart$getFollower();

    void linkart$setFollower(AbstractMinecartEntity follower);

    ItemStack linkart$getLinkItem();

    void linkart$setLinkItem(ItemStack linkItem);
}

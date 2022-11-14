package net.dreamer.autogrouping.mixin;

import net.dreamer.autogrouping.AutoGroupingAccessible;
import net.minecraft.item.ItemGroup;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemGroup.class)
public abstract class ItemGroupMixin implements AutoGroupingAccessible {
    @Mutable @Shadow @Final private Text displayName;

    public ItemGroup setDisplayName(Text display) {
        this.displayName = display;
        return (ItemGroup) (Object) this;
    }
}

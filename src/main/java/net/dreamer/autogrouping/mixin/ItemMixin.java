package net.dreamer.autogrouping.mixin;

import com.google.common.collect.Lists;
import net.dreamer.autogrouping.AutoGroupingAccessible;
import net.dreamer.autogrouping.AutoGroupingClient;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Mixin(Item.class)
public abstract class ItemMixin {
    @Shadow @Final @Nullable protected ItemGroup group;

    @Inject(at = @At("RETURN"), method = "getGroup", cancellable = true)
    public void getGroupInject(CallbackInfoReturnable<ItemGroup> cir) {
        ItemGroup[] groups = {
            ItemGroup.BUILDING_BLOCKS,
            ItemGroup.DECORATIONS,
            ItemGroup.REDSTONE,
            ItemGroup.TRANSPORTATION,
            ItemGroup.MISC,
            ItemGroup.FOOD,
            ItemGroup.TOOLS,
            ItemGroup.COMBAT,
            ItemGroup.BREWING,
            ItemGroup.MATERIALS
        };
        ItemGroup group = cir.getReturnValue();
        String namespace = Registry.ITEM.getId((Item) (Object) this).getNamespace();
        if(Arrays.stream(groups).anyMatch(grp -> grp == group) && !namespace.equals("minecraft")) {
            FabricLoaderImpl fabricLoader = FabricLoaderImpl.InitHelper.get();


            String name = "";
            for(ModContainer modContainer : fabricLoader.getAllMods())
                if(namespace.equals(modContainer.getMetadata().getId())) name = modContainer.getMetadata().getName();

            List<Item> items = Lists.newArrayList();
            items.addAll(Registry.ITEM.stream().toList());
            items.removeIf(item -> !Registry.ITEM.getId(item).getNamespace().equals(namespace));
            items.removeIf(item -> !Arrays.asList(groups).contains(((ItemMixin) (Object) item).group));

            cir.setReturnValue(registerGroup(items,name,namespace));
        }
    }

    public ItemGroup registerGroup(List<Item> items,String name,String namespace) {
        Text text = Text.translatable("autogrouping.modGroup",name);

        int index = -1;
        for(ItemGroup itemGroup : AutoGroupingClient.MOD_GROUPS)
            if(itemGroup.getDisplayName().equals(text)) index = AutoGroupingClient.MOD_GROUPS.indexOf(itemGroup);
        if(index != -1) return AutoGroupingClient.MOD_GROUPS.get(index);

        AutoGroupingClient.MOD_GROUPS.add((((AutoGroupingAccessible) FabricItemGroupBuilder.build(new Identifier(namespace,namespace), () -> new ItemStack(items.get(new Random().nextInt(items.size()))))).setDisplayName(text)));
        for(ItemGroup itemGroup : AutoGroupingClient.MOD_GROUPS)
            if(itemGroup.getDisplayName().equals(text))
                return AutoGroupingClient.MOD_GROUPS.get(AutoGroupingClient.MOD_GROUPS.indexOf(itemGroup));

        return null;
    }
}
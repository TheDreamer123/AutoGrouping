package net.dreamer.autogrouping;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.item.ItemGroup;
import org.apache.commons.compress.utils.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AutoGroupingClient implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("AutoGrouping");



	public static final List<ItemGroup> MOD_GROUPS = Lists.newArrayList();

	@Override
	public void onInitializeClient() {
		LOGGER.info("Grouping your dog!");
	}
}

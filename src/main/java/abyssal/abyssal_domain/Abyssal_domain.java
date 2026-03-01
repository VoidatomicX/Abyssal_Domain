package abyssal.abyssal_domain;

import abyssal.abyssal_domain.block.ModBlocks;
import abyssal.abyssal_domain.item.ModItemGroups;
import abyssal.abyssal_domain.item.ModItems;
import abyssal.abyssal_domain.util.BorderZoneManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Abyssal_domain implements ModInitializer {
    public static final String MOD_ID = "abyssal_domain";
public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {

        ModItemGroups.registerItemGroups();
        ModItems.registerModItems();
        ModBlocks.registerModBlocks();

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            BorderZoneManager.tick(server);
        });

    }
}

package abyssal.abyssal_domain.network;

import net.minecraft.util.Identifier;

import static abyssal.abyssal_domain.Abyssal_domain.MOD_ID;

public class ModPackets {
    public static final Identifier FAKE_BORDER = new Identifier("abyssal_domain", "fake_border");

    public static final Identifier REMOVE_FAKE_BORDER = new Identifier(MOD_ID, "remove_fake_border");
}
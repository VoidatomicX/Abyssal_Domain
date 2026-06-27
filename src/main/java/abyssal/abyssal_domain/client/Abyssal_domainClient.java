package abyssal.abyssal_domain.client;

import abyssal.abyssal_domain.block.ModBlocks;
import abyssal.abyssal_domain.entity.ModEntities;
import abyssal.abyssal_domain.entity.client.GoobichthyModel;
import abyssal.abyssal_domain.entity.client.GoobichthysRenderer;
import abyssal.abyssal_domain.entity.client.ModModelLayers;
import abyssal.abyssal_domain.item.ModItems;
import abyssal.abyssal_domain.network.ModPackets;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import abyssal.abyssal_domain.entity.client.*;
import abyssal.abyssal_domain.shader.ShaderManager;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import abyssal.abyssal_domain.entity.client.*;
import abyssal.abyssal_domain.shader.ShaderManager;

import java.util.UUID;

public class Abyssal_domainClient implements ClientModInitializer {

    private static BlockPos borderCenter;
    private static int borderRadius;
    private static BlockPos shieldCenter;
    private static int shieldRadius;
    private static int shieldHeight;
    private static boolean shieldActive;
    private static int chainTargetId;
    private static boolean isChanneling;
    private static int channelTimer;
    private static boolean showJudgmentEffect;
    private static Vec3d judgmentPos;
    private static int grappleHookId;
    private static Vec3d grapplePoint;
    private static boolean isGrappling;
    private static float screenshakeIntensity;
    private static Vec3d screenshakeOffset;
    private static List<FrozenProjectileData> frozenProjectiles = new ArrayList<>();

    private static class FrozenProjectileData {
        Vec3d pos;
        int ticksRemaining;

        FrozenProjectileData(Vec3d pos, int ticks) {
            this.pos = pos;
            this.ticksRemaining = ticks;
        }
    }


    @Override
    public void onInitializeClient() {

        EntityRendererRegistry.register(ModEntities.GOOBICHTHYS, GoobichthysRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.Goobichthys, GoobichthyModel::getTexturedModelData);

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.Crepe_Myrtle_Leaves, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.Crepe_Myrtle_PLANKS.door, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.Crepe_Myrtle_PLANKS.trapdoor, RenderLayer.getCutout());
        EntityRendererRegistry.register(ModEntities.MIRROR_TRIDENT, MirrorTridentRenderer::new);
        EntityRendererRegistry.register(ModEntities.ORBIT_TRIDENT, OrbitTridentRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(
                OrbitTridentRenderer.ORBIT_TRIDENT_LAYER,
                AbyssalDomainTridentModel::getTexturedModelData
        );

        registerItemRenderers();

        initShaderSystem();

        ModelPredicateProviderRegistry.register(
                ModItems.Scythe,
                new Identifier("abyssal_domain", "enchant_type"),
                (stack, world, entity, seed) -> {
                    var enchants = EnchantmentHelper.get(stack);

                    if (enchants.isEmpty()) return 0.0f;
                    var enchant = enchants.keySet().iterator().next();
                    Identifier id = Registries.ENCHANTMENT.getId(enchant);

                    if (id == null) return 0.0f;


                    return switch (id.toString()) {
                        case "minecraft:fire_aspect" -> 1.0f;
                        case "abyssal_domain:poison" -> 2.0f;
                        case "minecraft:knockback" -> 3.0f;
                        default -> 0.0f;
                    };
                }
        );



        ClientPlayNetworking.registerGlobalReceiver(ModPackets.FAKE_BORDER, (client, handler, buf, responseSender) -> {
            int x = buf.readInt();
            int y = buf.readInt();
            int z = buf.readInt();
            int radius = buf.readInt();

            borderCenter = new BlockPos(x, y, z);
            borderRadius = radius;
        });

        ClientPlayNetworking.registerGlobalReceiver(ModPackets.CHAIN_EFFECTS, (client, handler, buf, responseSender) -> {
            int targetId = buf.readInt();
            boolean start = buf.readBoolean();

            chainTargetId = targetId;
            isChanneling = start;
        });

        ClientPlayNetworking.registerGlobalReceiver(ModPackets.CHAIN_TICK, (client, handler, buf, responseSender) -> {
            int targetId = buf.readInt();
            boolean tick = buf.readBoolean();
            channelTimer = buf.readInt();

            chainTargetId = targetId;
            isChanneling = tick;
        });

        ClientPlayNetworking.registerGlobalReceiver(ModPackets.CLEAR_CHAIN_EFFECTS, (client, handler, buf, responseSender) -> {
            isChanneling = false;
            chainTargetId = -1;
            screenshakeIntensity = 0;
        });

        ClientPlayNetworking.registerGlobalReceiver(ModPackets.SHIELD_PACKET, (client, handler, buf, responseSender) -> {
            int x = buf.readInt();
            int y = buf.readInt();
            int z = buf.readInt();
            int radius = buf.readInt();
            int height = buf.readInt();

            shieldCenter = new BlockPos(x, y, z);
            shieldRadius = radius;
            shieldHeight = height;
            shieldActive = true;
        });

        ClientPlayNetworking.registerGlobalReceiver(ModPackets.CLEAR_SHIELD, (client, handler, buf, responseSender) -> {
            shieldActive = false;
            shieldCenter = null;
        });

        ClientPlayNetworking.registerGlobalReceiver(ModPackets.JUDGMENT_SYNC, (client, handler, buf, responseSender) -> {
            int targetId = buf.readInt();
            boolean isCaster = buf.readBoolean();
            int timer = buf.readInt();

            chainTargetId = targetId;
            isChanneling = true;
            channelTimer = timer;
            screenshakeIntensity = 0.5f;
        });

        ClientPlayNetworking.registerGlobalReceiver(ModPackets.CUSTOM_DEATH, (client, handler, buf, responseSender) -> {
            String message = buf.readString();
            client.player.sendMessage(net.minecraft.text.Text.literal(message), true);
        });

        ClientPlayNetworking.registerGlobalReceiver(ModPackets.JUDGMENT_EFFECT, (client, handler, buf, responseSender) -> {
            double x = buf.readDouble();
            double y = buf.readDouble();
            double z = buf.readDouble();

            judgmentPos = new Vec3d(x, y, z);
            showJudgmentEffect = true;
            screenshakeIntensity = 2.0f;
        });

        ClientPlayNetworking.registerGlobalReceiver(ModPackets.GRAPPLE_SWING, (client, handler, buf, responseSender) -> {
            boolean start = buf.readBoolean();
            if (start) {
                int x = buf.readInt();
                int y = buf.readInt();
                int z = buf.readInt();
                grapplePoint = new Vec3d(x, y, z);
                isGrappling = true;
            } else {
                isGrappling = false;
                grapplePoint = null;
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(ModPackets.FROZEN_PROJECTILE, (client, handler, buf, responseSender) -> {
            int x = buf.readInt();
            int y = buf.readInt();
            int z = buf.readInt();
            frozenProjectiles.add(new FrozenProjectileData(new Vec3d(x, y, z), 60));
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.world != null && client.world.getTime() % 20 == 0) {
                if (client.player == null || !client.player.isAlive()) {
                    clearAllEffects();
                }
            }

            if (borderCenter != null && client.world != null) {
                renderCircularBorder(client.world, client);
            }

            if (shieldActive && shieldCenter != null && client.world != null) {
                renderShield(client.world, client);
            }

            if (isChanneling && client.world != null) {
                renderChainEffects(client.world, client);
                updateScreenshake(client);
            }

            if (showJudgmentEffect && client.world != null) {
                renderJudgmentEffect(client.world, client);
            }

            if (isGrappling && grapplePoint != null && client.world != null) {
                renderGrappleChain(client.world, client);
            }

            renderFrozenProjectiles(client.world, client);
        });


    }

private  void registerItemRenderers(){
    private UUID borderPlayerUuid; // send this from server when creating border


    private void renderCircularBorder(ClientWorld world, MinecraftClient client) {
        if (borderPlayerUuid != null) {
            PlayerEntity player = world.getPlayerByUuid(borderPlayerUuid);

            if (player == null || !player.isAlive()) {
                borderCenter = null;
                borderRadius = 0;
                borderPlayerUuid = null;
                return;
            }
        }

        int steps = 80;

        double time = world.getTime() * 0.05;

        int steps = 100;
        for (int i = 0; i < steps; i++) {
            double angle = 2 * Math.PI * i / steps;
            double x = borderCenter.getX() + Math.cos(angle) * borderRadius + world.random.nextDouble() * 0.3;
            double z = borderCenter.getZ() + Math.sin(angle) * borderRadius + world.random.nextDouble() * 0.3;
            double y = borderCenter.getY() + 1.5 + Math.sin(angle * 3 + time) * 0.5;

            world.addParticle(ParticleTypes.ELECTRIC_SPARK, x, y, z, 0, 0.08, 0);

            if (i % 3 == 0) {
                world.addParticle(ParticleTypes.PORTAL,
                        x + world.random.nextDouble() * 0.5 - 0.25,
                        y + world.random.nextDouble() * 0.5,
                        z + world.random.nextDouble() * 0.5 - 0.25,
                        0, 0.02, 0);
            }
        }

        for (int i = 0; i < 30; i++) {
            double angle = world.random.nextDouble() * Math.PI * 2;
            double r = borderRadius + world.random.nextDouble() * 2 - 1;
            double x = borderCenter.getX() + Math.cos(angle) * r;
            double z = borderCenter.getZ() + Math.sin(angle) * r;
            double y = borderCenter.getY() + world.random.nextDouble() * 3;

            world.addParticle(ParticleTypes.WITCH,
                    x, y, z,
                    0, 0.05, 0);
        }

        for (int i = 0; i < 20; i++) {
            double angle = 2 * Math.PI * i / 20;
            double x = borderCenter.getX() + Math.cos(angle) * borderRadius;
            double z = borderCenter.getZ() + Math.sin(angle) * borderRadius;
            double y = borderCenter.getY() + 1.5;

            world.addParticle(ParticleTypes.ENCHANT,
                    x, y, z,
                    (Math.random() - 0.5) * 0.1,
                    0.1,
                    (Math.random() - 0.5) * 0.1);
        }
    }

    private void renderChainEffects(ClientWorld world, MinecraftClient client) {
        Entity target = world.getEntityById(chainTargetId);
        if (target == null) return;

        Vec3d targetPos = target.getPos().add(0, 1.5, 0);
        Vec3d casterPos = client.player.getPos().add(0, 1.2, 0);

        for (int layer = 0; layer < 3; layer++) {
            for (int i = 0; i < 8; i++) {
                double angle = (Math.PI * 2 * i / 8) + (world.getTime() * 0.05) + layer;
                double radius = 2.5 + Math.sin(world.getTime() * 0.1 + i) * 0.5;
                double height = 1.5 + Math.sin(world.getTime() * 0.05 + i * 0.5) * 0.5 + layer * 0.5;

                double circleX = targetPos.x + Math.cos(angle) * radius;
                double circleZ = targetPos.z + Math.sin(angle) * radius;
                double circleY = targetPos.y + height;

                world.addParticle(ParticleTypes.ENCHANT,
                        circleX, circleY, circleZ,
                        0, 0.02, 0);

                world.addParticle(ParticleTypes.WITCH,
                        circleX, circleY, circleZ,
                        world.random.nextDouble() * 0.02,
                        world.random.nextDouble() * 0.02,
                        world.random.nextDouble() * 0.02);
            }
        }

        int chainSegments = 12;
        for (int i = 0; i < chainSegments; i++) {
            double t = i / (double) chainSegments;
            Vec3d chainPoint = casterPos.multiply(1 - t).add(targetPos.multiply(t));

            world.addParticle(ParticleTypes.REVERSE_PORTAL,
                    chainPoint.x, chainPoint.y, chainPoint.z,
                    0, 0, 0);

            if (i % 3 == 0) {
                world.addParticle(ParticleTypes.SOUL_FIRE_FLAME,
                        chainPoint.x, chainPoint.y, chainPoint.z,
                        0, 0.05, 0);
            }
        }

        if (target instanceof LivingEntity living) {
            living.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.LEVITATION, 6, 4, false, false, true
            ));
            living.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.SLOWNESS, 6, 2, false, false, true
            ));
        }

        int circleCount = 3;
        for (int c = 0; c < circleCount; c++) {
            double circleY = targetPos.y + c * 0.8 - 0.8;
            double rotation = world.getTime() * 0.03 + c * 2;
            double radius = 3.5 + Math.sin(world.getTime() * 0.05 + c) * 0.3;

            for (int i = 0; i < 40; i++) {
                double angle = (Math.PI * 2 * i / 40) + rotation;

                Vec3d magicCircle = new Vec3d(
                        targetPos.x + Math.cos(angle) * radius,
                        circleY,
                        targetPos.z + Math.sin(angle) * radius
                );

                world.addParticle(ParticleTypes.END_ROD,
                        magicCircle.x, magicCircle.y, magicCircle.z,
                        (Math.random() - 0.5) * 0.02,
                        (Math.random() - 0.5) * 0.02,
                        (Math.random() - 0.5) * 0.02);

                if (i % 5 == 0) {
                    world.addParticle(ParticleTypes.ENTITY_EFFECT,
                            magicCircle.x, magicCircle.y, magicCircle.z,
                            0.3f, 0, 0.5f);
                }
            }
        }

        for (int i = 0; i < 15; i++) {
            double angle = world.random.nextDouble() * Math.PI * 2;
            double radius = world.random.nextDouble() * 4;
            double height = world.random.nextDouble() * 4 - 2;

            Vec3d chainStart = new Vec3d(
                    targetPos.x + Math.cos(angle) * radius * 0.3,
                    targetPos.y + height,
                    targetPos.z + Math.sin(angle) * radius * 0.3
            );

            world.addParticle(ParticleTypes.WITCH,
                    chainStart.x, chainStart.y, chainStart.z,
                    0, -0.05, 0);
        }
    }

    private void renderJudgmentEffect(ClientWorld world, MinecraftClient client) {
        if (judgmentPos == null) return;

        for (int i = 0; i < 20; i++) {
            double angle = Math.PI * 2 * i / 20;
            double radius = 4 + world.random.nextDouble() * 2;

            Vec3d pos = new Vec3d(
                    judgmentPos.x + Math.cos(angle) * radius,
                    judgmentPos.y + world.random.nextDouble() * 10,
                    judgmentPos.z + Math.sin(angle) * radius
            );

            world.addParticle(ParticleTypes.EXPLOSION_EMITTER,
                    pos.x, pos.y, pos.z,
                    0, 0, 0);

            world.addParticle(ParticleTypes.SMOKE,
                    pos.x, pos.y, pos.z,
                    0, 0.1, 0);
        }

        showJudgmentEffect = false;
    }

    private void initShaderSystem() {
        ShaderManager.init();

        ShaderManager.registerShader("abyssal_glow",
                new Identifier("abyssal_domain", "shaders/post/abyssal_glow"));

        ShaderManager.registerShader("custom_glow",
                new Identifier("abyssal_domain", "shaders/core/abyssal_glow.vsh"),
                new Identifier("abyssal_domain", "shaders/core/abyssal_glow.fsh"));

        ShaderManager.assignShader("chain_effect", "abyssal_glow");
        ShaderManager.assignShader("judgment_effect", "abyssal_glow");

        Abyssal_domainClient.LOGGER.info("Shader system initialized with " +
                ShaderManager.getRegisteredShaders().size() + " shaders");
    }

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger("AbyssalDomainClient");

    private void renderShield(ClientWorld world, MinecraftClient client) {
        double time = world.getTime() * 0.03;

        for (int y = 0; y < shieldHeight; y += 2) {
            int steps = 60;
            for (int i = 0; i < steps; i++) {
                double angle = 2 * Math.PI * i / steps;
                double x = shieldCenter.getX() + Math.cos(angle) * shieldRadius;
                double z = shieldCenter.getZ() + Math.sin(angle) * shieldRadius;
                double yPos = shieldCenter.getY() + y + Math.sin(time + y * 0.5) * 0.3;

                world.addParticle(ParticleTypes.ENCHANT,
                        x, yPos, z,
                        0, 0.05, 0);

                if (world.random.nextFloat() < 0.3f) {
                    world.addParticle(ParticleTypes.REVERSE_PORTAL,
                            x + (world.random.nextDouble() - 0.5) * 0.5,
                            yPos,
                            z + (world.random.nextDouble() - 0.5) * 0.5,
                            0, 0.02, 0);
                }
            }
        }

        for (int i = 0; i < 40; i++) {
            double angle = world.random.nextDouble() * Math.PI * 2;
            double r = shieldRadius * 0.9 + world.random.nextDouble() * 0.2;
            double x = shieldCenter.getX() + Math.cos(angle) * r;
            double z = shieldCenter.getZ() + Math.sin(angle) * r;
            double y = shieldCenter.getY() + world.random.nextDouble() * shieldHeight;

            world.addParticle(ParticleTypes.WITCH,
                    x, y, z,
                    0, 0.08, 0);
        }

        double centerY = shieldCenter.getY() + shieldHeight / 2.0;
        for (int i = 0; i < 20; i++) {
            double angle = 2 * Math.PI * i / 20;
            double x = shieldCenter.getX() + Math.cos(angle) * shieldRadius;
            double z = shieldCenter.getZ() + Math.sin(angle) * shieldRadius;

            world.addParticle(ParticleTypes.ELECTRIC_SPARK,
                    x, centerY, z,
                    0, 0.1, 0);
        }
    }

    private void updateScreenshake(MinecraftClient client) {
        if (screenshakeIntensity > 0) {
            screenshakeOffset = new Vec3d(
                    (Math.random() - 0.5) * screenshakeIntensity * 2,
                    (Math.random() - 0.5) * screenshakeIntensity * 2,
                    (Math.random() - 0.5) * screenshakeIntensity * 2
            );
            screenshakeIntensity *= 0.98f;
            if (screenshakeIntensity < 0.01f) {
                screenshakeIntensity = 0;
                screenshakeOffset = Vec3d.ZERO;
            }
        }
    }

    private void renderGrappleChain(ClientWorld world, MinecraftClient client) {
        if (client.player == null || grapplePoint == null) return;

        Vec3d playerPos = client.player.getPos().add(0, 1.2, 0);

        int segments = 20;
        for (int i = 0; i < segments; i++) {
            double t = i / (double) segments;
            Vec3d chainPos = playerPos.multiply(1 - t).add(grapplePoint.multiply(t));

            world.addParticle(ParticleTypes.ENCHANT,
                    chainPos.x, chainPos.y, chainPos.z,
                    0, 0, 0);

            if (i % 4 == 0) {
                world.addParticle(ParticleTypes.END_ROD,
                        chainPos.x, chainPos.y, chainPos.z,
                        0, 0.05, 0);
            }
        }

        world.addParticle(ParticleTypes.COMPOSTER,
                grapplePoint.x, grapplePoint.y, grapplePoint.z,
                0, 0, 0);
    }

    private void renderFrozenProjectiles(ClientWorld world, MinecraftClient client) {
        if (frozenProjectiles.isEmpty()) return;

        List<FrozenProjectileData> toRemove = new ArrayList<>();

        for (FrozenProjectileData fp : frozenProjectiles) {
            fp.ticksRemaining--;

            if (fp.ticksRemaining <= 0) {
                toRemove.add(fp);
                continue;
            }

            Vec3d pos = fp.pos;

            for (int i = 0; i < 8; i++) {
                double angle = Math.PI * 2 * i / 8 + world.getTime() * 0.1;
                double r = 0.3 + Math.sin(world.getTime() * 0.2 + i) * 0.1;

                world.addParticle(ParticleTypes.ENCHANT,
                        pos.x + Math.cos(angle) * r,
                        pos.y + (world.random.nextDouble() - 0.5) * 0.5,
                        pos.z + Math.sin(angle) * r,
                        0, 0.02, 0);

                world.addParticle(ParticleTypes.REVERSE_PORTAL,
                        pos.x + (world.random.nextDouble() - 0.5) * 0.4,
                        pos.y + (world.random.nextDouble() - 0.5) * 0.4,
                        pos.z + (world.random.nextDouble() - 0.5) * 0.4,
                        0, 0.01, 0);
            }

            world.addParticle(ParticleTypes.WITCH,
                    pos.x, pos.y, pos.z,
                    0, 0.05, 0);

            world.addParticle(ParticleTypes.ELECTRIC_SPARK,
                    pos.x + (world.random.nextDouble() - 0.5) * 0.3,
                    pos.y + (world.random.nextDouble() - 0.5) * 0.3,
                    pos.z + (world.random.nextDouble() - 0.5) * 0.3,
                    0, 0.08, 0);
        }

        frozenProjectiles.removeAll(toRemove);
    }

    private void clearAllEffects() {
        borderCenter = null;
        shieldCenter = null;
        shieldActive = false;
        isChanneling = false;
        chainTargetId = -1;
        showJudgmentEffect = false;
        judgmentPos = null;
        isGrappling = false;
        grapplePoint = null;
        screenshakeIntensity = 0;
    }
}
package com.novarch.jojomod.entities.stands;

import com.novarch.jojomod.capabilities.stand.Stand;
import com.novarch.jojomod.entities.stands.attacks.WhitesnakePunchEntity;
import com.novarch.jojomod.init.EntityInit;
import com.novarch.jojomod.init.SoundInit;
import com.novarch.jojomod.util.Util;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("ConstantConditions")
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class WhitesnakeEntity extends AbstractStandEntity {
    public WhitesnakeEntity(EntityType<? extends AbstractStandEntity> type, World world) {
        super(type, world);
        spawnSound = SoundInit.SPAWN_WHITESNAKE.get();
        standID = Util.StandID.WHITESNAKE;
    }

    public WhitesnakeEntity(World world) {
        super(EntityInit.WHITESNAKE.get(), world);
        spawnSound = SoundInit.SPAWN_WHITESNAKE.get();
        standID = Util.StandID.WHITESNAKE;
    }

    @Override
    public void attack(boolean special) {
        if (getMaster() == null) return;
        attackTick++;
        if (attackTick == 1)
            if (special)
                attackRush = true;
            else {
                world.playSound(null, getPosition(), SoundInit.PUNCH_MISS.get(), SoundCategory.NEUTRAL, 1, 0.6f / (rand.nextFloat() * 0.3f + 1) * 2);
                WhitesnakePunchEntity whitesnakePunchEntity = new WhitesnakePunchEntity(world, this, getMaster());
                whitesnakePunchEntity.shoot(getMaster(), rotationPitch, rotationYaw, 1.5f, 0.2f);
                world.addEntity(whitesnakePunchEntity);
            }
    }

    @Override
    public void tick() {
        super.tick();

        if (getMaster() != null) {
            PlayerEntity player = getMaster();
            Stand.getLazyOptional(player).ifPresent(props -> ability = props.getAbility());

            player.setNoGravity(false);

            followMaster();
            setRotationYawHead(player.rotationYaw);
            setRotation(player.rotationYaw, player.rotationPitch);

            if (player.swingProgressInt == 0 && !attackRush)
                attackTick = 0;
            if (attackRush) {
                player.setSprinting(false);
                attackTicker++;
                if (attackTicker >= 10)
                    if (!world.isRemote) {
                        player.setSprinting(false);
                        WhitesnakePunchEntity whitesnake1 = new WhitesnakePunchEntity(world, this, player);
                        whitesnake1.setRandomPositions();
                        whitesnake1.shoot(player, player.rotationPitch, player.rotationYaw, 1.0f, 0.25f);
                        world.addEntity(whitesnake1);
                        WhitesnakePunchEntity whitesnake2 = new WhitesnakePunchEntity(world, this, player);
                        whitesnake2.setRandomPositions();
                        whitesnake2.shoot(player, player.rotationPitch, player.rotationYaw, 1.0f, 0.25f);
                        world.addEntity(whitesnake2);
                    }
                if (attackTicker >= 80) {
                    attackRush = false;
                    attackTicker = 0;
                }
            }
        }
    }
}

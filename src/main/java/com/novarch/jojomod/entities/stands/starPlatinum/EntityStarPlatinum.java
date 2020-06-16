package com.novarch.jojomod.entities.stands.starPlatinum;

import com.novarch.jojomod.capabilities.stand.IStand;
import com.novarch.jojomod.capabilities.stand.Stand;
import com.novarch.jojomod.capabilities.timestop.ITimestop;
import com.novarch.jojomod.capabilities.timestop.Timestop;
import com.novarch.jojomod.config.JojoBizarreSurvivalConfig;
import com.novarch.jojomod.entities.stands.EntityStandBase;
import com.novarch.jojomod.entities.stands.EntityStandPunch;
import com.novarch.jojomod.entities.stands.goldExperienceRequiem.EntityGoldExperienceRequiem;
import com.novarch.jojomod.events.EventStarPlatinumStopTime;
import com.novarch.jojomod.events.EventTheWorldStopTime;
import com.novarch.jojomod.init.EntityInit;
import com.novarch.jojomod.init.SoundInit;
import com.novarch.jojomod.util.JojoLibs;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.novarch.jojomod.events.EventTheWorldStopTime.*;

@SuppressWarnings("ConstantConditions")
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class EntityStarPlatinum extends EntityStandBase {
	private int oratick = 0;

	private int oratickr = 0;

	public int timestopTick = 0;

	public boolean cooldown = false;

	@Override
	public boolean canDespawn(double distanceToClosestPlayer) {
		return false;
	}

	@Override
	public boolean isAIDisabled() {
		return false;
	}

	@Override
	public void readAdditional(CompoundNBT compoundNBT) {
		super.readAdditional(compoundNBT);
	}

	@Override
	public void writeAdditional(CompoundNBT compoundNBT) {
		super.writeAdditional(compoundNBT);
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return super.createSpawnPacket();
	}

	@Override
	public void spawnSound() {
		Stand.getLazyOptional(getMaster()).ifPresent(props -> {
			if(!props.getAbility())
				world.playSound(null, new BlockPos(getMaster().getPosX(), getMaster().getPosY(), getMaster().getPosZ()), SoundInit.SPAWN_THE_WORLD.get(), getSoundCategory(), 1.0f, 1.0f);
		});
	}

	public EntityStarPlatinum(EntityType<? extends EntityStandBase> type, World world) {
		super(type, world);
		spawnSound = SoundInit.SPAWN_THE_WORLD.get();
		standID = JojoLibs.StandID.starPlatinum;
	}

	public EntityStarPlatinum(World world) {
		super(EntityInit.STAR_PLATINUM.get(), world);
		spawnSound = SoundInit.SPAWN_THE_WORLD.get();
		standID = JojoLibs.StandID.starPlatinum;
	}

	public void tick() {
		super.tick();
		fallDistance = 0.0f;

		if (getMaster() != null) {
			PlayerEntity player = getMaster();
			Stand.getLazyOptional(player).ifPresent(props2 -> {
				ability = props2.getAbility();

				if (ability && props2.getTimeLeft() > 900) {
					props2.subtractTimeLeft(1);
					Timestop.getLazyOptional(player).ifPresent(ITimestop::clear);
					timestopTick++;
					player.setInvulnerable(true);
					if (timestopTick == 1 && props2.getCooldown() <= 0)
						world.playSound(null, new BlockPos(this.getPosX(), this.getPosY(), this.getPosZ()), SoundInit.STOP_TIME.get(), getSoundCategory(), 1.0f, 1.0f);
					EventStarPlatinumStopTime.starPlatinum = this;

					if (!world.isRemote) {
						if (timestopTick == 1 || dayTime == -1 || gameTime == -1) {
							dayTime = world.getDayTime();
							gameTime = world.getGameTime();
						}
						world.getServer().getWorld(dimension).getEntities()
								.filter(entity -> entity != this)
								.filter(entity -> entity != player)
								.filter(entity -> !(entity instanceof EntityGoldExperienceRequiem))
								.forEach(entity -> {
									if (entity instanceof PlayerEntity) {
										IStand props = Stand.getCapabilityFromPlayer((PlayerEntity) entity);
										if (props.getStandID() == JojoLibs.StandID.GER)
											return;
										if (props.getStandID() == JojoLibs.StandID.starPlatinum && props.getAbility() && props.getStandOn() && props.getCooldown() <= 0)
											return;
									}
									if (entity instanceof MobEntity) {
										if (((MobEntity) entity).getAttackTarget() == player || ((MobEntity) entity).getRevengeTarget() == player) {
											((MobEntity) entity).setAttackTarget(null);
											((MobEntity) entity).setRevengeTarget(null);
										}
										((MobEntity) entity).setNoAI(true);
									}
									if (this.timestopTick == 1) {
										Timestop.getLazyOptional(entity).ifPresent(props -> {
											props.setPosition(entity.getPosX(), entity.getPosY(), entity.getPosZ());
											props.setMotion(entity.getMotion().getX(), entity.getMotion().getY(), entity.getMotion().getZ());
											props.setRotation(entity.rotationYaw, entity.rotationPitch, entity.getRotationYawHead());
											props.setFallDistance(entity.fallDistance);
											props.setFire(entity.getFireTimer());
											if (entity instanceof TNTEntity)
												props.setFuse(((TNTEntity) entity).getFuse());
										});
									} else {
										Timestop.getLazyOptional(entity).ifPresent(props -> {
											if (props.getPosX() != 0 && props.getPosY() != 0 && props.getPosZ() != 0) {
												entity.setPosition(props.getPosX(), props.getPosY(), props.getPosZ());
												if ((entity instanceof IProjectile) || (entity instanceof ItemEntity) || (entity instanceof DamagingProjectileEntity))
													entity.setNoGravity(true);
												else {
													entity.rotationYaw = props.getRotationYaw();
													entity.rotationPitch = props.getRotationPitch();
													entity.setRotationYawHead(props.getRotationYawHead());
												}
												entity.setMotion(0, 0, 0);

												entity.fallDistance = props.getFallDistance();
												entity.setFireTimer(props.getFire());
												if (entity instanceof TNTEntity)
													((TNTEntity) entity).setFuse(props.getFuse());
												entity.velocityChanged = true;
											} else {
												props.setPosition(entity.getPosX(), entity.getPosY(), entity.getPosZ());
												props.setMotion(entity.getMotion().getX(), entity.getMotion().getY(), entity.getMotion().getZ());
												props.setRotation(entity.rotationYaw, entity.rotationPitch, entity.getRotationYawHead());
												props.setFallDistance(entity.fallDistance);
												props.setFire(entity.getFireTimer());
												if (entity instanceof TNTEntity)
													props.setFuse(((TNTEntity) entity).getFuse());
											}
										});
									}
								});
					}
				} else if(!ability || props2.getTimeLeft() <= 900) {
					timestopTick = 0;
					player.setInvulnerable(false);
					EventStarPlatinumStopTime.starPlatinum = null;
					if (!this.world.isRemote) {
						this.world.getServer().getWorld(this.dimension).getEntities()
								.filter(entity -> entity != this)
								.filter(entity -> entity != player)
								.forEach(entity -> Timestop.getLazyOptional(entity).ifPresent(props -> {
									if ((entity instanceof IProjectile || entity instanceof ItemEntity || entity instanceof DamagingProjectileEntity) && (props.getMotionX() != 0 && props.getMotionY() != 0 && props.getMotionZ() != 0)) {
										entity.setMotion(props.getMotionX(), props.getMotionY(), props.getMotionZ());
										entity.setNoGravity(false);
									} else {
										if (props.getMotionX() != 0 && props.getMotionY() != 0 && props.getMotionZ() != 0)
											entity.setMotion(props.getMotionX(), props.getMotionY(), props.getMotionZ());
									}
									if (entity instanceof MobEntity)
										((MobEntity) entity).setNoAI(false);
									entity.velocityChanged = true;
									if (props.getFallDistance() != 0)
										entity.fallDistance = props.getFallDistance();
									dayTime = -1;
									gameTime = -1;
									props.clear();
								}));
					}
				}

				if(JojoBizarreSurvivalConfig.COMMON.infiniteTimestop.get())
					props2.setTimeLeft(1000);

				if(props2.getTimeLeft() == 901) {
					props2.setCooldown(201);
					cooldown = true;
				}

				if(props2.getCooldown() > 0)
					props2.subtractCooldown(1);

				if(props2.getCooldown() == 1) {
					props2.setTimeLeft(1000);
					cooldown = false;
				}

				if(!ability && props2.getTimeLeft() < 1000)
					props2.addTimeLeft(1);
			});



			followMaster();
			setRotationYawHead(player.rotationYaw);
			setRotation(player.rotationYaw, player.rotationPitch);

			if (!player.isAlive())
				remove();
			if (player.isSprinting()) {
				if (attackSwing(player))
					oratick++;
				if (oratick == 1) {
					world.playSound(null, new BlockPos(getPosX(), getPosY(), getPosZ()), SoundInit.MUDARUSH.get(), getSoundCategory(), 1.0f, 1.0f);
					if (!world.isRemote)
						orarush = true;
				}
			} else if (attackSwing(getMaster())) {
				if (!world.isRemote) {
					oratick++;
					if (oratick == 1) {
						world.playSound(null, new BlockPos(getPosX(), getPosY(), getPosZ()), SoundInit.PUNCH_MISS.get(), getSoundCategory(), 1.0f, 0.8f / (rand.nextFloat() * 0.4f + 1.2f) + 0.5f);
						EntityStandPunch.starPlatinum starPlatinum = new EntityStandPunch.starPlatinum(world, this, player);
						starPlatinum.shoot(player, player.rotationPitch, player.rotationYaw, 3.0f, 0.1f);
						world.addEntity(starPlatinum);
					}
				}
			}
			if (player.swingProgressInt == 0)
				oratick = 0;
			if (orarush) {
				player.setSprinting(false);
				oratickr++;
				if (oratickr >= 10)
					if (!world.isRemote) {
						player.setSprinting(false);
						EntityStandPunch.starPlatinum starPlatinum1 = new EntityStandPunch.starPlatinum(world, this, player);
						starPlatinum1.setRandomPositions();
						starPlatinum1.shoot(player, player.rotationPitch, player.rotationYaw, 2.5f, 0.15f);
						world.addEntity(starPlatinum1);
						EntityStandPunch.starPlatinum starPlatinum2 = new EntityStandPunch.starPlatinum(world, this, player);
						starPlatinum2.setRandomPositions();
						starPlatinum2.shoot(player, player.rotationPitch, player.rotationYaw, 2.5f, 0.15f);
						world.addEntity(starPlatinum2);
					}
				if (oratickr >= 80) {
					orarush = false;
					oratickr = 0;
				}
			}
		}
	}

	@Override
	public boolean isEntityInsideOpaqueBlock() {
		return false;
	}

	@Override
	public void applyEntityCollision(Entity entityIn) {
		if (entityIn instanceof EntityStandBase || entityIn instanceof EntityStandPunch)
			super.applyEntityCollision(entityIn);
	}

	@Override
	public void onRemovedFromWorld() {
		super.onRemovedFromWorld();
		ability = false;
		EventStarPlatinumStopTime.starPlatinum = null;
		EventTheWorldStopTime.dayTime = -1;
		EventTheWorldStopTime.gameTime = -1;
		if (!this.world.isRemote)
			this.world.getServer().getWorld(this.dimension).getEntities()
					.filter(entity -> entity != this)
					.forEach(entity ->
							Timestop.getLazyOptional(entity).ifPresent(props2 -> {
								if ((entity instanceof IProjectile || entity instanceof ItemEntity || entity instanceof DamagingProjectileEntity) && (props2.getMotionX() != 0 && props2.getMotionY() != 0 && props2.getMotionZ() != 0)) {
									entity.setMotion(props2.getMotionX(), props2.getMotionY(), props2.getMotionZ());
									entity.setNoGravity(false);
								} else {
									if (props2.getMotionX() != 0 && props2.getMotionY() != 0 && props2.getMotionZ() != 0)
										entity.setMotion(props2.getMotionX(), props2.getMotionY(), props2.getMotionZ());
								}
								if (entity instanceof MobEntity)
									((MobEntity) entity).setNoAI(false);
								entity.setMotion(props2.getMotionX(), props2.getMotionY(), props2.getMotionZ());
								entity.velocityChanged = true;
								entity.fallDistance = props2.getFallDistance();
								entity.setInvulnerable(false);
								props2.clear();
							}));
	}
}
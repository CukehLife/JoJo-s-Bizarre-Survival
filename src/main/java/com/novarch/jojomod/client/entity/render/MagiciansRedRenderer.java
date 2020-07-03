package com.novarch.jojomod.client.entity.render;

import com.novarch.jojomod.JojoBizarreSurvival;
import com.novarch.jojomod.client.entity.model.MagiciansRedModel;
import com.novarch.jojomod.entities.stands.MagiciansRedEntity;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MagiciansRedRenderer extends MobRenderer<MagiciansRedEntity, MagiciansRedModel<MagiciansRedEntity>> {
	protected static final ResourceLocation TEXTURE = new ResourceLocation(JojoBizarreSurvival.MOD_ID, "textures/stands/magicians_red.png");

	public MagiciansRedRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn, new MagiciansRedModel<>(), 0.5f);
	}

	public ResourceLocation getEntityTexture(final MagiciansRedEntity entity) {
		return TEXTURE;
	}
}

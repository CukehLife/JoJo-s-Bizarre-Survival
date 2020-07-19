package io.github.novarch129.jojomod.client.entity.render;

import io.github.novarch129.jojomod.JojoBizarreSurvival;
import io.github.novarch129.jojomod.client.entity.model.SheerHeartAttackModel;
import io.github.novarch129.jojomod.entity.stand.SheerHeartAttackEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class SheerHeartAttackRenderer extends MobRenderer<SheerHeartAttackEntity, SheerHeartAttackModel> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(JojoBizarreSurvival.MOD_ID, "textures/stands/sheer_heart_attack.png");

    public SheerHeartAttackRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new SheerHeartAttackModel(), 0.5f);
    }

    @Nonnull
    @Override
    public ResourceLocation getEntityTexture(SheerHeartAttackEntity entity) {
        return TEXTURE;
    }
}


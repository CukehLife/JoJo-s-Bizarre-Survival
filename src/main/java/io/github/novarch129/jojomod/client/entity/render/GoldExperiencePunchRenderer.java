package io.github.novarch129.jojomod.client.entity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.novarch129.jojomod.JojoBizarreSurvival;
import io.github.novarch129.jojomod.client.entity.model.GoldExperiencePunchModel;
import io.github.novarch129.jojomod.entity.stand.attack.GoldExperiencePunchEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class GoldExperiencePunchRenderer extends StandPunchRenderer<GoldExperiencePunchEntity> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(JojoBizarreSurvival.MOD_ID, "textures/stands/gold_experience_punch.png");

    public GoldExperiencePunchRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public void render(GoldExperiencePunchEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn, new GoldExperiencePunchModel());
    }

    @Nonnull
    @Override
    public ResourceLocation getEntityTexture(GoldExperiencePunchEntity entity) {
        return TEXTURE;
    }
}


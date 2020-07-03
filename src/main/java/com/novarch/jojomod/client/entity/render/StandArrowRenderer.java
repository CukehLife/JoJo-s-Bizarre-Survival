package com.novarch.jojomod.client.entity.render;

import com.novarch.jojomod.entities.StandArrowEntity;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SpectralArrowRenderer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class StandArrowRenderer extends ArrowRenderer<StandArrowEntity> {
    public StandArrowRenderer(EntityRendererManager manager) {
        super(manager);
    }

    public ResourceLocation getEntityTexture(StandArrowEntity entity) {
        return SpectralArrowRenderer.RES_SPECTRAL_ARROW;
    }
}

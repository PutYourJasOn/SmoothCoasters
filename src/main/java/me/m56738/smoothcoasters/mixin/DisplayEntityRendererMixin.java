package me.m56738.smoothcoasters.mixin;

import me.m56738.smoothcoasters.DisplayEntityMixinInterface;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.DisplayEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.util.math.AffineTransformation;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(DisplayEntityRenderer.class)
public class DisplayEntityRendererMixin<T extends DisplayEntity, S> {

    @ModifyVariable(method = "render(Lnet/minecraft/entity/decoration/DisplayEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("STORE"), ordinal = 0)
    private AffineTransformation injected(AffineTransformation affineTransformation, DisplayEntity displayEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {;

        DisplayEntityMixinInterface displayMixin = ((DisplayEntityMixinInterface)displayEntity);

        if(displayMixin.shouldBeAnimated()) {

            displayMixin.animate(g);

            Quaternionf rotation = displayMixin.getRotation();

            return new AffineTransformation(
                affineTransformation.getTranslation(),
                    rotation,
                affineTransformation.getScale(),
                affineTransformation.getRightRotation()
            );
        }

        return affineTransformation;
    }

}

package bounceable.mixin.vanilla;

import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EntityLivingBase.class)
public interface IEntityLivingBaseMixin {
	@Accessor("isJumping")
	boolean getIsJumping();
	@Invoker("getJumpUpwardsMotion")
	float getJumpUpwardsMotion();
}
package bounceable.mixin.vanilla;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityLivingBase.class)
public abstract class EntityLivingBaseMixin {
	
	/**
	 * Allow for motion set by block landing collisions to be retained while holding jump
	 */
	@Redirect(
			method = "jump",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;getJumpUpwardsMotion()F")
	)
	public float bounceable_vanillaEntityLivingBase_jump(EntityLivingBase instance) {
		return instance instanceof EntityPlayer ?
			   (float)Math.max(((IEntityLivingBaseMixin)instance).getJumpUpwardsMotion(), instance.motionY) :
			   ((IEntityLivingBaseMixin)instance).getJumpUpwardsMotion();
	}
}
package bounceable.mixin.potioncore;

import com.tmtravlr.potioncore.PotionCoreEventHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PotionCoreEventHandler.class)
public abstract class PotionCoreEventHandlerMixin {
	
	/**
	 * Fix PotionCore forcibly overwriting player motionY breaking motion increases set by other mods
	 */
	@Redirect(
			method = "onLivingJump",
			at = @At(value = "FIELD", target = "Lnet/minecraft/entity/EntityLivingBase;motionY:D", ordinal = 2)
	)
	private static void bounceable_potionCoreEventHandler_onLivingJump(EntityLivingBase instance, double original) {
		if(instance instanceof EntityPlayer) {
			instance.motionY = Math.max(original, instance.motionY);
		}
	}
	
	/**
	 * Fix PotionCore forcibly overwriting player motionY breaking motion increases set by other mods
	 */
	@Redirect(
			method = "onLivingUpdate",
			at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/EntityPlayer;motionY:D")
	)
	private static void bounceable_potionCoreEventHandler_onLivingUpdate(EntityPlayer instance, double original) {
		instance.motionY = Math.max(original, instance.motionY);
	}
}
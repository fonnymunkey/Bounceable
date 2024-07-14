package bounceable.mixin.vanilla;

import bounceable.Bounceable;
import bounceable.handlers.ForgeConfigHandler;
import bounceable.util.BounceBlock;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public abstract class BlockMixin {
	
	@Inject(
			method = "onFallenUpon",
			at = @At("HEAD"),
			cancellable = true
	)
	public void bounceable_vanillaBlock_onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance, CallbackInfo ci) {
		//Default handling if sneaking
		if(entityIn.isSneaking()) return;
		BounceBlock bb = ForgeConfigHandler.getBounceMap().get(((Block)(Object)this));
		if(bb != null) {
			boolean valid = false;
			if(bb.getMeta() == null) {
				valid = true;
			}
			else {
				IBlockState state = worldIn.getBlockState(pos);
				if(bb.getMeta().contains(((Block)(Object)this).getMetaFromState(state))) {
					valid = true;
				}
			}
			
			if(valid) {
				entityIn.fall(fallDistance, 0.0F);
				ci.cancel();
			}
		}
		//Default handling if not a bouncy block
	}
	
	@Inject(
			method = "onLanded",
			at = @At("HEAD"),
			cancellable = true
	)
	public void bounceable_vanillaBlock_onLanded(World worldIn, Entity entityIn, CallbackInfo ci) {
		//Default handling if sneaking
		if(entityIn.isSneaking()) return;
		if(entityIn.motionY < -0.15D) {
			BounceBlock bb = ForgeConfigHandler.getBounceMap().get(((Block)(Object)this));
			if(bb != null) {
				boolean valid = false;
				if(bb.getMeta() == null) {
					valid = true;
				}
				else {
					//Recalculate blockstate literally just for meta check, gross
					int x1 = MathHelper.floor(entityIn.posX);
					int y1 = MathHelper.floor(entityIn.posY - 0.20000000298023224D);
					int z1 = MathHelper.floor(entityIn.posZ);
					BlockPos blockpos = new BlockPos(x1, y1, z1);
					IBlockState iblockstate = worldIn.getBlockState(blockpos);
					if(iblockstate.getMaterial() == Material.AIR) {
						BlockPos blockpos1 = blockpos.down();
						IBlockState iblockstate1 = worldIn.getBlockState(blockpos1);
						Block block1 = iblockstate1.getBlock();
						
						if(block1 instanceof BlockFence || block1 instanceof BlockWall || block1 instanceof BlockFenceGate) {
							iblockstate = iblockstate1;
						}
					}
					
					if(bb.getMeta().contains(((Block)(Object)this).getMetaFromState(iblockstate))) {
						valid = true;
					}
				}
				
				if(valid) {
					double x = entityIn.motionY = -entityIn.motionY;
					
					if(entityIn instanceof EntityPlayer) {
						//Controlled bouncing naturalizes while holding jump
						if(((IEntityLivingBaseMixin)entityIn).getIsJumping()) {
							double limitFactor = bb.getLimitFactor();
							double gainFactor = bb.getGainFactor();
							
							entityIn.motionY = x + ((x*gainFactor*(limitFactor-x))/(limitFactor+x));
							
							if(ForgeConfigHandler.debugInfo) Bounceable.LOGGER.log(Level.INFO, "JumpBounce, motionIn: " + x + " motionOut: " + entityIn.motionY);
						}
						else {
							entityIn.motionY *= 0.9D;
						}
					}
					else if(!(entityIn instanceof EntityLivingBase)) {
						//Copy motion behavior from slime for non-living entities
						entityIn.motionY *= 0.8D;
					}
					ci.cancel();
				}
				//Default handling if not a bouncy block
			}
		}
		//Default handling if motion is too low to bother
	}
}
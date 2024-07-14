package bounceable.handlers;

import bounceable.util.BounceBlock;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import bounceable.Bounceable;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.List;

@Config(modid = Bounceable.MODID)
public class ForgeConfigHandler {
	
	private static Object2ObjectArrayMap<Block,BounceBlock> bounceMap = null;
	
	@Config.Comment("Entries of blocks that should be made bounceable, with metadata restrictions and bouncing factors \n" +
			"Format: String blockId, Int metadata... (Optional), Double limitFactor, Double gainFactor \n" +
			"\n" +
			"BlockId: The ResourceLocation id of the block to be made bounceable, ex. minecraft:dirt \n" +
			"Metadata: Limits bouncing to only blockstates with the matching metadata, such as mushroom block types \n" +
			"LimitFactor: Limiting factor used in the motion formula, higher values equal the maximum motion being higher \n" +
			"GainFactor: Gain factor used in the motion formula, higher values equal the maximum motion being reached faster \n" +
			"\n" +
			"Note: The gain and limit factors will not be linear partly due to momentum being lost due to other MC calculations \n" +
			"You should test the values to see what feels right for you \n" +
			"\n" +
			"Example: minecraft:red_mushroom_block, 1, 2, 3, 4, 5, 6, 7, 8, 9, 14, 1.8, 0.6 \n" +
			"Example: minecraft:soul_sand, 1.0, 0.4 \n")
	@Config.Name("Bounceable Block Entries")
	public static String[] bounceArray = new String[]{
			"minecraft:red_mushroom_block, 1, 2, 3, 4, 5, 6, 7, 8, 9, 14, 1.8, 0.6",
			"minecraft:brown_mushroom_block, 1, 2, 3, 4, 5, 6, 7, 8, 9, 14, 1.8, 0.6"
	};
	
	@Config.Comment("Prints debug motion information to console while bouncing")
	@Config.Name("Debug Info")
	public static boolean debugInfo = false;
	
	public static Object2ObjectArrayMap<Block,BounceBlock> getBounceMap() {
		if(bounceMap == null) parseBounceMap();
		return bounceMap;
	}
	
	private static void parseBounceMap() {
		bounceMap = new Object2ObjectArrayMap<>();
		for(String string : bounceArray) {
			try {
				String[] arr = string.split(",");
				Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(arr[0].trim()));
				if(block == null) {
					Bounceable.LOGGER.log(Level.WARN, "Bounceable invalid block in entry: " + string + ", ignoring");
					continue;
				}
				BounceBlock entry = new BounceBlock();
				if(arr.length > 3) {
					List<Integer> metaList = new ArrayList<>();
					for(int x = 1; x < arr.length-2; x++) {
						metaList.add(Integer.parseInt(arr[x].trim()));
					}
					entry.setMeta(metaList);
				}
				entry.setLimitFactor(Double.parseDouble(arr[arr.length-2].trim()));
				entry.setGainFactor(Double.parseDouble(arr[arr.length-1].trim()));
				bounceMap.put(block, entry);
			}
			catch(Exception ex) {
				Bounceable.LOGGER.log(Level.WARN, "Bounceable failed to parse block entry: " + string + ", ignoring");
			}
		}
	}
	
	@Mod.EventBusSubscriber(modid = Bounceable.MODID)
	private static class EventHandler{

		@SubscribeEvent
		public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
			if(event.getModID().equals(Bounceable.MODID)) {
				bounceMap = null;
				ConfigManager.sync(Bounceable.MODID, Config.Type.INSTANCE);
			}
		}
	}
}
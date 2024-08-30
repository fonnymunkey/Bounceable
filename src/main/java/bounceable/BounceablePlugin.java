package bounceable;

import java.util.Map;
import fermiumbooter.FermiumRegistryAPI;
import net.minecraftforge.fml.common.Loader;
import org.spongepowered.asm.launch.MixinBootstrap;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.MCVersion("1.12.2")
public class BounceablePlugin implements IFMLLoadingPlugin {

	public BounceablePlugin() {
		MixinBootstrap.init();
		FermiumRegistryAPI.enqueueMixin(false, "mixins.bounceable.vanilla.json");
		FermiumRegistryAPI.enqueueMixin(true, "mixins.bounceable.potioncore.json", () -> Loader.isModLoaded("potioncore"));
	}

	@Override
	public String[] getASMTransformerClass()
	{
		return new String[0];
	}
	
	@Override
	public String getModContainerClass()
	{
		return null;
	}
	
	@Override
	public String getSetupClass()
	{
		return null;
	}
	
	@Override
	public void injectData(Map<String, Object> data) { }
	
	@Override
	public String getAccessTransformerClass()
	{
		return null;
	}
}
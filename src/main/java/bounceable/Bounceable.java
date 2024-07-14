package bounceable;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Bounceable.MODID, version = Bounceable.VERSION, name = Bounceable.NAME, dependencies = "required-after:fermiumbooter")
public class Bounceable {
    public static final String MODID = "bounceable";
    public static final String VERSION = "1.0.0";
    public static final String NAME = "Bounceable";
    public static final Logger LOGGER = LogManager.getLogger();
	
	@Instance(MODID)
	public static Bounceable instance;
}
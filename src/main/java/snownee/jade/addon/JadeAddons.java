package snownee.jade.addon;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkConstants;

@Mod(JadeAddons.ID)
public class JadeAddons {
	public static final String ID = "jadeaddons";
	public static final String NAME = "Jade Addons";
	public static final Logger LOGGER = LogManager.getLogger(NAME);

	public JadeAddons() {
		ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true));
	}
}

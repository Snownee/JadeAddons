package snownee.jade.addon;

import net.minecraftforge.client.ConfigScreenHandler.ConfigScreenFactory;
import net.minecraftforge.fml.ModLoadingContext;
import snownee.jade.gui.PluginsConfigScreen;

public class JadeAddonsClient {
	public static void init() {
		ModLoadingContext.get().registerExtensionPoint(ConfigScreenFactory.class, () -> new ConfigScreenFactory((minecraft, screen) -> new PluginsConfigScreen(screen)));
	}
}

package snownee.jade.addon;

import net.neoforged.fml.ModLoadingContext;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import snownee.jade.gui.PluginsConfigScreen;

public class JadeAddonsClient {
	public static void init() {
		ModLoadingContext.get().registerExtensionPoint(
				IConfigScreenFactory.class,
				() -> (minecraft, screen) -> new PluginsConfigScreen(screen));
	}
}

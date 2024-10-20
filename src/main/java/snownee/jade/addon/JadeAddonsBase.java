package snownee.jade.addon;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import snownee.jade.addon.enderio.EnderIOPlugin;
import snownee.jade.addon.general.GeneralPlugin;
import snownee.jade.addon.lootr.LootrPlugin;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;
import snownee.jade.util.CommonProxy;

@WailaPlugin
public class JadeAddonsBase implements IWailaPlugin {
	public static final Map<String, Supplier<Supplier<IWailaPlugin>>> PLUGIN_LOADERS = Maps.newHashMap();
	public static IWailaClientRegistration client;
	private final List<IWailaPlugin> plugins = Lists.newArrayList();

	static {
		PLUGIN_LOADERS.put(JadeAddons.ID, () -> GeneralPlugin::new);
//		PLUGIN_LOADERS.put("create", () -> CreatePlugin::new);
		PLUGIN_LOADERS.put("lootr", () -> LootrPlugin::new);
//		PLUGIN_LOADERS.put("mcjtylib", () -> McjtyLibPlugin::new);
//		PLUGIN_LOADERS.put("deepresonance", () -> DeepResonancePlugin::new);
		PLUGIN_LOADERS.put("enderio", () -> EnderIOPlugin::new);
	}

	public JadeAddonsBase() {
		PLUGIN_LOADERS.forEach((modid, loader) -> {
			if (!CommonProxy.isModLoaded(modid)) {
				return;
			}
			try {
				plugins.add(loader.get().get());
			} catch (Throwable e) {
				JadeAddons.LOGGER.error("Failed to load plugin for %s".formatted(modid), e);
			}
		});
	}

	@Override
	public void register(IWailaCommonRegistration registration) {
		plugins.removeIf($ -> {
			try {
				$.register(registration);
				return false;
			} catch (Throwable e) {
				JadeAddons.LOGGER.error("Failed to register plugin %s".formatted($.getClass().getName()), e);
				return true;
			}
		});
	}

	@Override
	public void registerClient(IWailaClientRegistration registration) {
		client = registration;
		plugins.forEach($ -> $.registerClient(registration));
	}
}

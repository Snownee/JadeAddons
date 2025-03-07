package snownee.jade.addon;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import snownee.jade.addon.create.CreatePlugin;
import snownee.jade.addon.deep_resonance.DeepResonancePlugin;
import snownee.jade.addon.enderio.EnderIOPlugin;
import snownee.jade.addon.general.GeneralPlugin;
import snownee.jade.addon.lootr.LootrPlugin;
import snownee.jade.addon.mcjty_lib.McjtyLibPlugin;
import snownee.jade.addon.tconstruct.TConstructPlugin;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;
import snownee.jade.util.CommonProxy;

@WailaPlugin
public class JadeAddonsBase implements IWailaPlugin {
	public static IWailaClientRegistration client;
	private final List<IWailaPlugin> plugins = Lists.newArrayList();

	public JadeAddonsBase() {
		Map<String, Supplier<Supplier<IWailaPlugin>>> loaders = Maps.newHashMap();

		loaders.put(JadeAddons.ID, () -> GeneralPlugin::new);
		loaders.put("create", () -> CreatePlugin::new);
		loaders.put("lootr", () -> LootrPlugin::new);
		loaders.put("mcjtylib", () -> McjtyLibPlugin::new);
		loaders.put("deepresonance", () -> DeepResonancePlugin::new);
		loaders.put("enderio", () -> EnderIOPlugin::new);
		loaders.put("tconstruct", () -> TConstructPlugin::new);

		loaders.forEach((modid, loader) -> {
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

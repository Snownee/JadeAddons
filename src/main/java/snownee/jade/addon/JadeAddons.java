package snownee.jade.addon;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.StringUtil;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import snownee.jade.api.theme.IThemeHelper;

@Mod(JadeAddons.ID)
public class JadeAddons {
	public static final String ID = "jadeaddons";
	public static final String NAME = "Jade Addons";
	public static final Logger LOGGER = LogUtils.getLogger();

	public JadeAddons(IEventBus eventBus) {
		if (FMLEnvironment.dist.isClient()) {
			JadeAddonsClient.init();
		}
	}

	public static MutableComponent seconds(int sec) {
		return IThemeHelper.get().info(StringUtil.formatTickDuration(sec, 1)).withStyle(ChatFormatting.WHITE);
	}
}

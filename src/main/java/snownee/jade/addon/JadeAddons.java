package snownee.jade.addon;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.StringUtil;
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

	public static MutableComponent seconds(int sec) {
		return Component.literal(StringUtil.formatTickDuration(sec * 20)).withStyle(ChatFormatting.WHITE);
	}
}

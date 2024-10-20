package snownee.jade.addon.lootr;

import org.jetbrains.annotations.Nullable;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import noobanidus.mods.lootr.common.api.LootrAPI;
import noobanidus.mods.lootr.common.api.data.ILootrInfoProvider;
import snownee.jade.api.Accessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.StreamServerDataProvider;
import snownee.jade.api.theme.IThemeHelper;

public interface LootrInfoProvider<A extends Accessor<?>> extends StreamServerDataProvider<A, LootrInfoProvider.Data> {

	@Override
	default @Nullable LootrInfoProvider.Data streamData(A accessor) {
		if (!(accessor.getTarget() instanceof ILootrInfoProvider infoProvider)) {
			return null;
		}
		int decayValue = 0;
		if (!LootrAPI.isDecayed(infoProvider)) {
			decayValue = LootrAPI.getRemainingDecayValue(infoProvider);
		}
		boolean refreshed = LootrAPI.isRefreshed(infoProvider);
		int refreshValue = 0;
		if (!refreshed) {
			refreshValue = LootrAPI.getRemainingRefreshValue(infoProvider);
		}
		return new Data(decayValue, refreshed, refreshValue);
	}

	@Override
	default StreamCodec<RegistryFriendlyByteBuf, Data> streamCodec() {
		return Data.STREAM_CODEC.cast();
	}

	default void appendTooltip(ITooltip tooltip, A accessor) {
		Data data = decodeFromData(accessor).orElse(null);
		if (data == null) {
			return;
		}
		IThemeHelper t = IThemeHelper.get();
		if (data.decay() > 0) {
			tooltip.add(Component.translatable("jadeaddons.lootr.decay", t.seconds(data.decay(), accessor.tickRate())));
		}
		if (data.refreshed()) {
			tooltip.add(Component.translatable("jadeaddons.lootr.refreshed"));
		} else if (data.refresh() > 0) {
			tooltip.add(Component.translatable("jadeaddons.lootr.refresh", t.seconds(data.refresh(), accessor.tickRate())));
		}
	}

	@Override
	default ResourceLocation getUid() {
		return LootrPlugin.INFO;
	}

	record Data(int decay, boolean refreshed, int refresh) {
		public static final StreamCodec<ByteBuf, Data> STREAM_CODEC = StreamCodec.composite(
				ByteBufCodecs.VAR_INT,
				Data::decay,
				ByteBufCodecs.BOOL,
				Data::refreshed,
				ByteBufCodecs.VAR_INT,
				Data::refresh,
				Data::new);
	}
}

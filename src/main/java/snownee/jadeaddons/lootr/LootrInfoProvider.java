package snownee.jadeaddons.lootr;

import org.jetbrains.annotations.Nullable;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import noobanidus.mods.lootr.common.api.LootrAPI;
import noobanidus.mods.lootr.common.api.data.ILootrContainerInstance;
import noobanidus.mods.lootr.common.api.data.ILootrInventoryStore;
import snownee.jade.api.Accessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.StreamServerDataProvider;
import snownee.jade.api.theme.IThemeHelper;

public interface LootrInfoProvider<A extends Accessor<?>> extends StreamServerDataProvider<A, LootrInfoProvider.Data> {

	@Override
	default @Nullable LootrInfoProvider.Data streamData(A accessor) {
		if (!(accessor.getTarget() instanceof ILootrContainerInstance container)) {
			return null;
		}
		ILootrInventoryStore inventoryStore = LootrAPI.getData(container);
		if (inventoryStore == null) {
			return null;
		}
		int decayValue = 0;
		if (!inventoryStore.isDecayed()) {
			decayValue = inventoryStore.remainingDecayTime();
		}
		boolean refreshed = inventoryStore.isRefreshed();
		int refreshValue = 0;
		if (!refreshed) {
			refreshValue = inventoryStore.remainingRefreshTime();
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
	default Identifier getUid() {
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

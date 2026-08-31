package snownee.jadeaddons.general;

import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

import org.jspecify.annotations.Nullable;

import eu.pb4.trinkets.api.TrinketsApi;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.HitResult;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.TagsUpdatedEvent;
import snownee.jade.api.Accessor;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.config.IWailaConfig;
import snownee.jadeaddons.JadeAddons;
import top.theillusivec4.curios.api.CuriosApi;

public class GeneralPlugin implements IWailaPlugin {
	public static final String ID = JadeAddons.ID;
	public static final Identifier EQUIPMENT_REQUIREMENT = Identifier.fromNamespaceAndPath(ID, "equipment_requirement");
	public static BiPredicate<Player, TagKey<Item>> EQUIPMENT_CHECK_PREDICATE = (player, tag) -> player.getMainHandItem().is(tag)
			|| player.getOffhandItem().is(tag)
			|| player.getItemBySlot(EquipmentSlot.HEAD).is(tag);

	public @Nullable TagKey<Item> requirementTag;

	@Override
	public void registerClient(IWailaClientRegistration registration) {
		registration.addConfig(EQUIPMENT_REQUIREMENT, "", $ -> Identifier.read($).isSuccess());
		registration.addConfigListener(EQUIPMENT_REQUIREMENT, id -> refreshTag(id, $ -> requirementTag = $));
		registration.addRayTraceCallback(
				10000,
				(HitResult _, Accessor<?> accessor, Accessor<?> _) -> {
					Player player = accessor.getPlayer();
					if (requirementTag != null && !EQUIPMENT_CHECK_PREDICATE.test(player, requirementTag)) {
						return null;
					}
					return accessor;
				});

		if (ModList.get().isLoaded("curios")) {
			EQUIPMENT_CHECK_PREDICATE = EQUIPMENT_CHECK_PREDICATE.or((player, tag) -> CuriosApi.getCuriosInventory(player)
					.flatMap(inventory -> inventory.findFirstCurio(itemStack -> itemStack.is(tag)))
					.isPresent());
		}

		if (ModList.get().isLoaded("trinkets")) {
			EQUIPMENT_CHECK_PREDICATE = EQUIPMENT_CHECK_PREDICATE.or((player, tag) -> {
				return TrinketsApi.getAttachment(player)
						.getInventories()
						.entrySet()
						.stream()
						.filter(entry -> entry.getKey().startsWith("head/"))
						.map(Map.Entry::getValue)
						.anyMatch(inventory -> {
							for (int i = 0; i < inventory.getContainerSize(); i++) {
								if (inventory.getItem(i).is(tag)) {
									return true;
								}
							}
							return false;
						});
			});
		}

		TargetModifierLoader loader = new TargetModifierLoader();
		NeoForge.EVENT_BUS.addListener((TagsUpdatedEvent.ClientPacketReceived event) -> {
			refreshTags();
			loader.reload();
		});
		registration.addRayTraceCallback(loader);
		registration.addTooltipCollectedCallback(loader);

		registration.markAsClientFeature(EQUIPMENT_REQUIREMENT);
	}

	private void refreshTags() {
		refreshTag(EQUIPMENT_REQUIREMENT, $ -> requirementTag = $);
	}

	private void refreshTag(Identifier id, Consumer<@Nullable TagKey<Item>> setter) {
		String s = IWailaConfig.get().plugin().getString(id);
		if (s.isBlank()) {
			setter.accept(null);
		} else {
			Identifier resourceLocation = Identifier.tryParse(s);
			if (resourceLocation != null) {
				setter.accept(TagKey.create(Registries.ITEM, resourceLocation));
			}
		}
	}

}

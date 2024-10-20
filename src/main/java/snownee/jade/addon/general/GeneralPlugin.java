package snownee.jade.addon.general;

import java.util.function.BiPredicate;
import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.HitResult;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.TagsUpdatedEvent;
import snownee.jade.addon.JadeAddons;
import snownee.jade.api.Accessor;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.config.IWailaConfig;
import top.theillusivec4.curios.api.CuriosApi;

public class GeneralPlugin implements IWailaPlugin {
	public static final String ID = JadeAddons.ID;
	public static final ResourceLocation EQUIPMENT_REQUIREMENT = ResourceLocation.fromNamespaceAndPath(ID, "equipment_requirement");
	public static BiPredicate<Player, TagKey<Item>> EQUIPMENT_CHECK_PREDICATE = (player, tag) -> player.getMainHandItem().is(tag)
			|| player.getOffhandItem().is(tag)
			|| player.getItemBySlot(EquipmentSlot.HEAD).is(tag);

	public TagKey<Item> requirementTag;

	@Override
	public void registerClient(IWailaClientRegistration registration) {
		registration.addConfig(EQUIPMENT_REQUIREMENT, "", $ -> ResourceLocation.read($).isSuccess());
		registration.addConfigListener(EQUIPMENT_REQUIREMENT, id -> refreshTag(id, $ -> requirementTag = $));
		registration.addRayTraceCallback(
				10000,
				(HitResult hitResult, @Nullable Accessor<?> accessor, @Nullable Accessor<?> originalAccessor) -> {
					if (accessor != null) {
						Player player = accessor.getPlayer();
						if (requirementTag != null && !EQUIPMENT_CHECK_PREDICATE.test(player, requirementTag)) {
							return null;
						}
					}
					return accessor;
				});

		if (ModList.get().isLoaded("curios")) {
			EQUIPMENT_CHECK_PREDICATE = EQUIPMENT_CHECK_PREDICATE.or((player, tag) -> CuriosApi.getCuriosInventory(player)
					.flatMap(inventory -> inventory.findFirstCurio(itemStack -> itemStack.is(tag)))
					.isPresent());
		}

		TargetModifierLoader loader = new TargetModifierLoader();
		NeoForge.EVENT_BUS.addListener((TagsUpdatedEvent event) -> {
			if (event.getUpdateCause() == TagsUpdatedEvent.UpdateCause.CLIENT_PACKET_RECEIVED) {
				refreshTags();
				loader.reload();
			}
		});
		registration.addRayTraceCallback(loader);
		registration.addTooltipCollectedCallback(loader);

		registration.markAsClientFeature(EQUIPMENT_REQUIREMENT);
	}

	private void refreshTags() {
		refreshTag(EQUIPMENT_REQUIREMENT, $ -> requirementTag = $);
	}

	private void refreshTag(ResourceLocation id, Consumer<TagKey<Item>> setter) {
		String s = IWailaConfig.get().getPlugin().getString(id);
		if (s.isBlank()) {
			setter.accept(null);
		} else {
			ResourceLocation resourceLocation = ResourceLocation.tryParse(s);
			if (resourceLocation != null) {
				setter.accept(TagKey.create(Registries.ITEM, resourceLocation));
			}
		}
	}

}

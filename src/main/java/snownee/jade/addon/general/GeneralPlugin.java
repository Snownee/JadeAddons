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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.fml.ModList;
import snownee.jade.addon.JadeAddons;
import snownee.jade.api.Accessor;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.config.IWailaConfig;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypePreset;

public class GeneralPlugin implements IWailaPlugin {
	public static final String ID = JadeAddons.ID;
	public static final ResourceLocation EQUIPMENT_REQUIREMENT = new ResourceLocation(ID, "equipment_requirement");
	//	public static final ResourceLocation DETAILS_EQUIPMENT_REQUIREMENT = new ResourceLocation(ID, "details_equipment_requirement");
	/* off */
	public static BiPredicate<Player, TagKey<Item>> EQUIPMENT_CHECK_PREDICATE = (player, tag) -> player.getMainHandItem().is(tag)
			|| player.getOffhandItem().is(tag)
			|| player.getItemBySlot(EquipmentSlot.HEAD).is(tag);
	/* on */

	public TagKey<Item> requirementTag;
	//	public TagKey<Item> requirementDetailsTag;

	@Override
	@OnlyIn(Dist.CLIENT)
	public void registerClient(IWailaClientRegistration registration) {
		registration.addConfig(EQUIPMENT_REQUIREMENT, "", ResourceLocation::isValidResourceLocation);
		//		registration.addConfig(DETAILS_EQUIPMENT_REQUIREMENT, "", ResourceLocation::isValidResourceLocation);
		registration.addConfigListener(EQUIPMENT_REQUIREMENT, id -> refreshTag(id, $ -> requirementTag = $));
		//		registration.addConfigListener(DETAILS_EQUIPMENT_REQUIREMENT, id -> refreshTag(id, $ -> requirementDetailsTag = $));
		registration.addRayTraceCallback(10000,
				(HitResult hitResult, @Nullable Accessor<?> accessor, @Nullable Accessor<?> originalAccessor) -> {
					if (accessor != null) {
						Player player = accessor.getPlayer();
						if (requirementTag != null && !EQUIPMENT_CHECK_PREDICATE.test(player, requirementTag)) {
							return null;
						}
						//			if (requirementDetailsTag != null && accessor.showDetails() && !EQUIPMENT_CHECK_PREDICATE.test(player, requirementDetailsTag)) {
						//				//TODO universal accessor builder
						//				if (accessor instanceof BlockAccessor blockAccessor) {
						//					return client.blockAccessor().from(blockAccessor).showDetails(false).build();
						//				}
						//				if (accessor instanceof EntityAccessor entityAccessor) {
						//					return client.entityAccessor().from(entityAccessor).showDetails(false).build();
						//				}
						//			}
					}
					return accessor;
				});

		if (ModList.get().isLoaded("curios")) {
			/* off */
			EQUIPMENT_CHECK_PREDICATE = EQUIPMENT_CHECK_PREDICATE.or((player, tag) -> CuriosApi.getCuriosHelper().findCurios(player, SlotTypePreset.HEAD.getIdentifier())
					.stream()
					.filter(result -> !result.slotContext().cosmetic()).anyMatch(result -> result.stack().is(tag)));
			/* on */
		}

		TargetModifierLoader loader = new TargetModifierLoader();
		MinecraftForge.EVENT_BUS.addListener((TagsUpdatedEvent event) -> {
			if (event.getUpdateCause() == TagsUpdatedEvent.UpdateCause.CLIENT_PACKET_RECEIVED) {
				refreshTags();
				loader.reload();
			}
		});
		registration.addRayTraceCallback(loader);
		registration.addTooltipCollectedCallback(loader);
	}

	private void refreshTags() {
		refreshTag(EQUIPMENT_REQUIREMENT, $ -> requirementTag = $);
		//		refreshTag(DETAILS_EQUIPMENT_REQUIREMENT, $ -> requirementDetailsTag = $);
	}

	private void refreshTag(ResourceLocation id, Consumer<TagKey<Item>> setter) {
		String s = IWailaConfig.get().getPlugin().getString(id);
		if (s.isBlank()) {
			setter.accept(null);
		} else {
			setter.accept(TagKey.create(Registries.ITEM, new ResourceLocation(s)));
		}
	}

}

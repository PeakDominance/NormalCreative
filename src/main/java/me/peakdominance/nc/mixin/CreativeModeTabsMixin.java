package me.peakdominance.nc.mixin;

import me.peakdominance.nc.CreativeModeTabBuilderMixinInterface;
import me.peakdominance.nc.NormalCreative;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.InstrumentTags;
import net.minecraft.tags.PaintingVariantTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.component.Fireworks;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightBlock;
import org.spongepowered.asm.mixin.*;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.IntStream;

@Mixin(CreativeModeTabs.class)
public class CreativeModeTabsMixin {
	@Shadow
	private static ResourceKey<CreativeModeTab> createKey(String id) {
		throw new AssertionError();
	}

	@Unique
	private static final ResourceLocation INVENTORY_BACKGROUND = CreativeModeTab.createTextureLocation("inventory");
	@Unique
	private static final ResourceLocation SEARCH_BACKGROUND = CreativeModeTab.createTextureLocation("item_search");
	@Unique
	private static final ResourceKey<CreativeModeTab> BUILDING_BLOCKS = createKey("building_blocks");
	@Unique
	private static final ResourceKey<CreativeModeTab> DECORATION = createKey("colored_blocks");
	@Unique
	private static final ResourceKey<CreativeModeTab> REDSTONE = createKey("natural_blocks");
	@Unique
	private static final ResourceKey<CreativeModeTab> TRANSPORTATION = createKey("functional_blocks");
	@Unique
	private static final ResourceKey<CreativeModeTab> MISC = createKey("redstone_blocks");
	@Unique
	private static final ResourceKey<CreativeModeTab> FOOD = createKey("hotbar");
	@Unique
	private static final ResourceKey<CreativeModeTab> SEARCH = createKey("search");
	@Unique
	private static final ResourceKey<CreativeModeTab> TOOLS = createKey("tools_and_utilities");
	@Unique
	private static final ResourceKey<CreativeModeTab> COMBAT = createKey("combat");
	@Unique
	private static final ResourceKey<CreativeModeTab> BREWING = createKey("food_and_drinks");
	@Unique
	private static final ResourceKey<CreativeModeTab> MATERIALS = createKey("op_blocks");
	@Unique
	private static final ResourceKey<CreativeModeTab> INVENTORY = createKey("inventory");

    @Shadow
    private static void generatePresetPaintings(CreativeModeTab.Output output, HolderLookup.Provider provider, HolderLookup.RegistryLookup<PaintingVariant> registryLookup, Predicate<Holder<PaintingVariant>> predicate, CreativeModeTab.TabVisibility tabVisibility) {
		throw new AssertionError();
	}
	/**
	 * @author Matthew
	 * @reason Excludes firework duration 1 to avoid adding the same item stack twice
	 */
	@Overwrite
	private static void generateFireworksAllDurations(CreativeModeTab.Output output, CreativeModeTab.TabVisibility tabVisibility) {
		for (byte b : FireworkRocketItem.CRAFTABLE_DURATIONS) {
			if (b == 1) continue;
			ItemStack itemStack = new ItemStack(Items.FIREWORK_ROCKET);
			itemStack.set(DataComponents.FIREWORKS, new Fireworks(b, List.of()));
			output.accept(itemStack, tabVisibility);
		}
	}
	@Shadow
	private static void generateInstrumentTypes(CreativeModeTab.Output output, HolderLookup<Instrument> holderLookup, Item item, TagKey<Instrument> tagKey, CreativeModeTab.TabVisibility tabVisibility) {
		throw new AssertionError();
	}
	/**
	 * @author Matthew
	 * @reason Excludes already added potions
	 */
	@Overwrite
	private static void generatePotionEffectTypes(CreativeModeTab.Output output, HolderLookup<Potion> holderLookup, Item item, CreativeModeTab.TabVisibility tabVisibility, FeatureFlagSet featureFlagSet) {
		holderLookup.listElements().filter((reference) -> reference.value().isEnabled(featureFlagSet)).map((reference) -> PotionContents.createItemStack(item, reference)).forEach((itemStack) -> {
			if (!CreativeModeTabs.tabs().get(9).getDisplayItems().contains(itemStack)) output.accept(itemStack, tabVisibility);
		});
	}
	@Shadow
	private static void generateSuspiciousStews(CreativeModeTab.Output output, CreativeModeTab.TabVisibility tabVisibility) {
		throw new AssertionError();
	}
	@Shadow
	private static void generateOminousBottles(CreativeModeTab.Output output, CreativeModeTab.TabVisibility tabVisibility) {
		throw new AssertionError();
	}
	/**
	 * @author Matthew
	 * @reason Excludes already added enchantment books
	 */
	@Overwrite
	private static void generateEnchantmentBookTypesOnlyMaxLevel(CreativeModeTab.Output output, HolderLookup<Enchantment> holderLookup, CreativeModeTab.TabVisibility tabVisibility) {
		holderLookup.listElements().map((reference) -> EnchantmentHelper.createBook(new EnchantmentInstance(reference, reference.value().getMaxLevel()))).forEach((itemStack) -> {
			if (!CreativeModeTabs.tabs().get(7).getDisplayItems().contains(itemStack) && !CreativeModeTabs.tabs().get(8).getDisplayItems().contains(itemStack)) output.accept(itemStack, tabVisibility);
		});
	}
	/**
	 * @author Matthew
	 * @reason Excludes already added enchantment books
	 */
	@Overwrite
	private static void generateEnchantmentBookTypesAllLevels(CreativeModeTab.Output output, HolderLookup<Enchantment> holderLookup, CreativeModeTab.TabVisibility tabVisibility) {
		holderLookup.listElements().flatMap((reference) -> IntStream.rangeClosed(reference.value().getMinLevel(), reference.value().getMaxLevel()).mapToObj((i) -> EnchantmentHelper.createBook(new EnchantmentInstance(reference, i)))).forEach((itemStack) -> {
			if (!CreativeModeTabs.tabs().get(7).getDisplayItems().contains(itemStack) && !CreativeModeTabs.tabs().get(8).getDisplayItems().contains(itemStack)) output.accept(itemStack, tabVisibility);
		});
	}

    /**
	 * @author Matthew
	 * @reason Normal 1.8 creative menu
	 */
	@Overwrite
	public static CreativeModeTab bootstrap(Registry<CreativeModeTab> registry) {
		Registry.register(
				registry,
				BUILDING_BLOCKS,
				CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
						.title(Component.translatable("itemGroup.buildingBlocks"))
						.icon(() -> new ItemStack(Blocks.BRICKS))
						.displayItems((displayContext, content) -> {
							content.accept(Items.STONE);
							content.accept(Items.GRANITE);
							content.accept(Items.POLISHED_GRANITE);
							content.accept(Items.DIORITE);
							content.accept(Items.POLISHED_DIORITE);
							content.accept(Items.ANDESITE);
							content.accept(Items.POLISHED_ANDESITE);
							content.accept(Items.GRASS_BLOCK);
							content.accept(Items.DIRT);
							content.accept(Items.COARSE_DIRT);
							content.accept(Items.PODZOL);
							content.accept(Items.COBBLESTONE);
							content.accept(Items.OAK_PLANKS);
							content.accept(Items.SPRUCE_PLANKS);
							content.accept(Items.BIRCH_PLANKS);
							content.accept(Items.JUNGLE_PLANKS);
							content.accept(Items.ACACIA_PLANKS);
							content.accept(Items.DARK_OAK_PLANKS);
							content.accept(Items.BEDROCK);
							content.accept(Items.SAND);
							content.accept(Items.RED_SAND);
							content.accept(Items.GRAVEL);
							content.accept(Items.GOLD_ORE);
							content.accept(Items.IRON_ORE);
							content.accept(Items.COAL_ORE);
							content.accept(Items.OAK_LOG);
							content.accept(Items.SPRUCE_LOG);
							content.accept(Items.BIRCH_LOG);
							content.accept(Items.JUNGLE_LOG);
							content.accept(Items.SPONGE);
							content.accept(Items.WET_SPONGE);
							content.accept(Items.GLASS);
							content.accept(Items.LAPIS_ORE);
							content.accept(Items.LAPIS_BLOCK);
							content.accept(Items.SANDSTONE);
							content.accept(Items.CHISELED_SANDSTONE);
							content.accept(Items.CUT_SANDSTONE);
							content.accept(Items.WHITE_WOOL);
							content.accept(Items.ORANGE_WOOL);
							content.accept(Items.MAGENTA_WOOL);
							content.accept(Items.LIGHT_BLUE_WOOL);
							content.accept(Items.YELLOW_WOOL);
							content.accept(Items.LIME_WOOL);
							content.accept(Items.PINK_WOOL);
							content.accept(Items.GRAY_WOOL);
							content.accept(Items.LIGHT_GRAY_WOOL);
							content.accept(Items.CYAN_WOOL);
							content.accept(Items.PURPLE_WOOL);
							content.accept(Items.BLUE_WOOL);
							content.accept(Items.BROWN_WOOL);
							content.accept(Items.GREEN_WOOL);
							content.accept(Items.RED_WOOL);
							content.accept(Items.BLACK_WOOL);
							content.accept(Items.GOLD_BLOCK);
							content.accept(Items.IRON_BLOCK);
							content.accept(Items.SMOOTH_STONE_SLAB);
							content.accept(Items.SANDSTONE_SLAB);
							content.accept(Items.COBBLESTONE_SLAB);
							content.accept(Items.BRICK_SLAB);
							content.accept(Items.STONE_BRICK_SLAB);
							content.accept(Items.NETHER_BRICK_SLAB);
							content.accept(Items.QUARTZ_SLAB);
							content.accept(Items.BRICKS);
							content.accept(Items.BOOKSHELF);
							content.accept(Items.MOSSY_COBBLESTONE);
							content.accept(Items.OBSIDIAN);
							content.accept(Items.OAK_STAIRS);
							content.accept(Items.DIAMOND_ORE);
							content.accept(Items.DIAMOND_BLOCK);
							content.accept(Items.COBBLESTONE_STAIRS);
							content.accept(Items.REDSTONE_ORE);
							content.accept(Items.ICE);
							content.accept(Items.SNOW_BLOCK);
							content.accept(Items.CLAY);
							content.accept(Items.PUMPKIN);
							content.accept(Items.NETHERRACK);
							content.accept(Items.SOUL_SAND);
							content.accept(Items.GLOWSTONE);
							content.accept(Items.JACK_O_LANTERN);
							content.accept(Items.WHITE_STAINED_GLASS);
							content.accept(Items.ORANGE_STAINED_GLASS);
							content.accept(Items.MAGENTA_STAINED_GLASS);
							content.accept(Items.LIGHT_BLUE_STAINED_GLASS);
							content.accept(Items.YELLOW_STAINED_GLASS);
							content.accept(Items.LIME_STAINED_GLASS);
							content.accept(Items.PINK_STAINED_GLASS);
							content.accept(Items.GRAY_STAINED_GLASS);
							content.accept(Items.LIGHT_GRAY_STAINED_GLASS);
							content.accept(Items.CYAN_STAINED_GLASS);
							content.accept(Items.PURPLE_STAINED_GLASS);
							content.accept(Items.BLUE_STAINED_GLASS);
							content.accept(Items.BROWN_STAINED_GLASS);
							content.accept(Items.GREEN_STAINED_GLASS);
							content.accept(Items.RED_STAINED_GLASS);
							content.accept(Items.BLACK_STAINED_GLASS);
							content.accept(Items.STONE_BRICKS);
							content.accept(Items.MOSSY_STONE_BRICKS);
							content.accept(Items.CRACKED_STONE_BRICKS);
							content.accept(Items.CHISELED_STONE_BRICKS);
							content.accept(Items.MELON);
							content.accept(Items.BRICK_STAIRS);
							content.accept(Items.STONE_BRICK_STAIRS);
							content.accept(Items.MYCELIUM);
							content.accept(Items.NETHER_BRICKS);
							content.accept(Items.NETHER_BRICK_STAIRS);
							content.accept(Items.END_STONE);
							content.accept(Items.OAK_SLAB);
							content.accept(Items.SPRUCE_SLAB);
							content.accept(Items.BIRCH_SLAB);
							content.accept(Items.JUNGLE_SLAB);
							content.accept(Items.ACACIA_SLAB);
							content.accept(Items.DARK_OAK_SLAB);
							content.accept(Items.SANDSTONE_STAIRS);
							content.accept(Items.EMERALD_ORE);
							content.accept(Items.EMERALD_BLOCK);
							content.accept(Items.SPRUCE_STAIRS);
							content.accept(Items.BIRCH_STAIRS);
							content.accept(Items.JUNGLE_STAIRS);
							content.accept(Items.COBBLESTONE_WALL);
							content.accept(Items.MOSSY_COBBLESTONE_WALL);
							content.accept(Items.NETHER_QUARTZ_ORE);
							content.accept(Items.QUARTZ_BLOCK);
							content.accept(Items.CHISELED_QUARTZ_BLOCK);
							content.accept(Items.QUARTZ_PILLAR);
							content.accept(Items.QUARTZ_STAIRS);
							content.accept(Items.WHITE_TERRACOTTA);
							content.accept(Items.ORANGE_TERRACOTTA);
							content.accept(Items.MAGENTA_TERRACOTTA);
							content.accept(Items.LIGHT_BLUE_TERRACOTTA);
							content.accept(Items.YELLOW_TERRACOTTA);
							content.accept(Items.LIME_TERRACOTTA);
							content.accept(Items.PINK_TERRACOTTA);
							content.accept(Items.GRAY_TERRACOTTA);
							content.accept(Items.LIGHT_GRAY_TERRACOTTA);
							content.accept(Items.CYAN_TERRACOTTA);
							content.accept(Items.PURPLE_TERRACOTTA);
							content.accept(Items.BLUE_TERRACOTTA);
							content.accept(Items.BROWN_TERRACOTTA);
							content.accept(Items.GREEN_TERRACOTTA);
							content.accept(Items.RED_TERRACOTTA);
							content.accept(Items.BLACK_TERRACOTTA);
							content.accept(Items.ACACIA_LOG);
							content.accept(Items.DARK_OAK_LOG);
							content.accept(Items.ACACIA_STAIRS);
							content.accept(Items.DARK_OAK_STAIRS);
							content.accept(Items.PRISMARINE);
							content.accept(Items.PRISMARINE_BRICKS);
							content.accept(Items.DARK_PRISMARINE);
							content.accept(Items.SEA_LANTERN);
							content.accept(Items.HAY_BLOCK);
							content.accept(Items.TERRACOTTA);
							content.accept(Items.COAL_BLOCK);
							content.accept(Items.PACKED_ICE);
							content.accept(Items.RED_SANDSTONE);
							content.accept(Items.CHISELED_RED_SANDSTONE);
							content.accept(Items.CUT_RED_SANDSTONE);
							content.accept(Items.RED_SANDSTONE_STAIRS);
							content.accept(Items.RED_SANDSTONE_SLAB);
						})
						.build()
		);
		Registry.register(
				registry,
				DECORATION,
				CreativeModeTab.builder(CreativeModeTab.Row.TOP, 1)
						.title(Component.translatable("itemGroup.decorations"))
						.icon(() -> new ItemStack(Blocks.PEONY))
						.displayItems((displayContext, content) -> {
							content.accept(Items.OAK_SAPLING);
							content.accept(Items.SPRUCE_SAPLING);
							content.accept(Items.BIRCH_SAPLING);
							content.accept(Items.JUNGLE_SAPLING);
							content.accept(Items.ACACIA_SAPLING);
							content.accept(Items.DARK_OAK_SAPLING);
							content.accept(Items.OAK_LEAVES);
							content.accept(Items.SPRUCE_LEAVES);
							content.accept(Items.BIRCH_LEAVES);
							content.accept(Items.JUNGLE_LEAVES);
							content.accept(Items.COBWEB);
							content.accept(Items.SHORT_GRASS);
							content.accept(Items.FERN);
							content.accept(Items.DEAD_BUSH);
							content.accept(Items.DANDELION);
							content.accept(Items.POPPY);
							content.accept(Items.BLUE_ORCHID);
							content.accept(Items.ALLIUM);
							content.accept(Items.AZURE_BLUET);
							content.accept(Items.RED_TULIP);
							content.accept(Items.ORANGE_TULIP);
							content.accept(Items.WHITE_TULIP);
							content.accept(Items.PINK_TULIP);
							content.accept(Items.OXEYE_DAISY);
							content.accept(Items.BROWN_MUSHROOM);
							content.accept(Items.RED_MUSHROOM);
							content.accept(Items.TORCH);
							content.accept(Items.CHEST);
							content.accept(Items.CRAFTING_TABLE);
							content.accept(Items.FURNACE);
							content.accept(Items.LADDER);
							content.accept(Items.SNOW);
							content.accept(Items.CACTUS);
							content.accept(Items.JUKEBOX);
							content.accept(Items.OAK_FENCE);
							content.accept(Items.INFESTED_STONE);
							content.accept(Items.INFESTED_COBBLESTONE);
							content.accept(Items.INFESTED_STONE_BRICKS);
							content.accept(Items.INFESTED_MOSSY_STONE_BRICKS);
							content.accept(Items.INFESTED_CRACKED_STONE_BRICKS);
							content.accept(Items.INFESTED_CHISELED_STONE_BRICKS);
							content.accept(Items.IRON_BARS);
							content.accept(Items.GLASS_PANE);
							content.accept(Items.VINE);
							content.accept(Items.LILY_PAD);
							content.accept(Items.NETHER_BRICK_FENCE);
							content.accept(Items.ENCHANTING_TABLE);
							content.accept(Items.END_PORTAL_FRAME);
							content.accept(Items.ENDER_CHEST);
							content.accept(Items.ANVIL);
							content.accept(Items.CHIPPED_ANVIL);
							content.accept(Items.DAMAGED_ANVIL);
							content.accept(Items.TRAPPED_CHEST);
							content.accept(Items.WHITE_STAINED_GLASS_PANE);
							content.accept(Items.ORANGE_STAINED_GLASS_PANE);
							content.accept(Items.MAGENTA_STAINED_GLASS_PANE);
							content.accept(Items.LIGHT_BLUE_STAINED_GLASS_PANE);
							content.accept(Items.YELLOW_STAINED_GLASS_PANE);
							content.accept(Items.LIME_STAINED_GLASS_PANE);
							content.accept(Items.PINK_STAINED_GLASS_PANE);
							content.accept(Items.GRAY_STAINED_GLASS_PANE);
							content.accept(Items.LIGHT_GRAY_STAINED_GLASS_PANE);
							content.accept(Items.CYAN_STAINED_GLASS_PANE);
							content.accept(Items.PURPLE_STAINED_GLASS_PANE);
							content.accept(Items.BLUE_STAINED_GLASS_PANE);
							content.accept(Items.BROWN_STAINED_GLASS_PANE);
							content.accept(Items.GREEN_STAINED_GLASS_PANE);
							content.accept(Items.RED_STAINED_GLASS_PANE);
							content.accept(Items.BLACK_STAINED_GLASS_PANE);
							content.accept(Items.ACACIA_LEAVES);
							content.accept(Items.DARK_OAK_LEAVES);
							content.accept(Items.SLIME_BLOCK);
							content.accept(Items.WHITE_CARPET);
							content.accept(Items.ORANGE_CARPET);
							content.accept(Items.MAGENTA_CARPET);
							content.accept(Items.LIGHT_BLUE_CARPET);
							content.accept(Items.YELLOW_CARPET);
							content.accept(Items.LIME_CARPET);
							content.accept(Items.PINK_CARPET);
							content.accept(Items.GRAY_CARPET);
							content.accept(Items.LIGHT_GRAY_CARPET);
							content.accept(Items.CYAN_CARPET);
							content.accept(Items.PURPLE_CARPET);
							content.accept(Items.BLUE_CARPET);
							content.accept(Items.BROWN_CARPET);
							content.accept(Items.GREEN_CARPET);
							content.accept(Items.RED_CARPET);
							content.accept(Items.BLACK_CARPET);
							content.accept(Items.SUNFLOWER);
							content.accept(Items.LILAC);
							content.accept(Items.TALL_GRASS);
							content.accept(Items.LARGE_FERN);
							content.accept(Items.ROSE_BUSH);
							content.accept(Items.PEONY);
							content.accept(Items.SPRUCE_FENCE);
							content.accept(Items.BIRCH_FENCE);
							content.accept(Items.JUNGLE_FENCE);
							content.accept(Items.DARK_OAK_FENCE);
							content.accept(Items.ACACIA_FENCE);
							content.accept(Items.PAINTING);
							content.accept(Items.OAK_SIGN);
							content.accept(Items.RED_BED);
							content.accept(Items.ITEM_FRAME);
							content.accept(Items.FLOWER_POT);
							content.accept(Items.SKELETON_SKULL);
							content.accept(Items.WITHER_SKELETON_SKULL);
							content.accept(Items.ZOMBIE_HEAD);
							content.accept(Items.PLAYER_HEAD);
							content.accept(Items.CREEPER_HEAD);
							content.accept(Items.ARMOR_STAND);
							content.accept(Items.WHITE_BANNER);
							content.accept(Items.ORANGE_BANNER);
							content.accept(Items.MAGENTA_BANNER);
							content.accept(Items.LIGHT_BLUE_BANNER);
							content.accept(Items.YELLOW_BANNER);
							content.accept(Items.LIME_BANNER);
							content.accept(Items.PINK_BANNER);
							content.accept(Items.GRAY_BANNER);
							content.accept(Items.LIGHT_GRAY_BANNER);
							content.accept(Items.CYAN_BANNER);
							content.accept(Items.PURPLE_BANNER);
							content.accept(Items.BLUE_BANNER);
							content.accept(Items.BROWN_BANNER);
							content.accept(Items.GREEN_BANNER);
							content.accept(Items.RED_BANNER);
							content.accept(Items.BLACK_BANNER);
							// Special shit
							if (NormalCreative.config.getSetting("extraItems")) {
								content.accept(Items.BROWN_MUSHROOM_BLOCK);
								content.accept(Items.RED_MUSHROOM_BLOCK);
								content.accept(Items.BARRIER);
								content.accept(Items.DRAGON_EGG);
								content.accept(Items.DRAGON_HEAD);
								content.accept(Items.COMMAND_BLOCK);
								content.accept(Items.FARMLAND);
								content.accept(Items.SPAWNER);
								content.accept(Items.FIREWORK_ROCKET);
								content.accept(Items.PETRIFIED_OAK_SLAB);
							}
						})
						.build()
		);
		Registry.register(
				registry,
				REDSTONE,
				CreativeModeTab.builder(CreativeModeTab.Row.TOP, 2)
						.title(Component.translatable("itemGroup.redstone"))
						.icon(() -> new ItemStack(Items.REDSTONE))
						.displayItems((displayContext, content) -> {
							content.accept(Items.DISPENSER);
							content.accept(Items.NOTE_BLOCK);
							content.accept(Items.STICKY_PISTON);
							content.accept(Items.PISTON);
							content.accept(Items.TNT);
							content.accept(Items.LEVER);
							content.accept(Items.STONE_PRESSURE_PLATE);
							content.accept(Items.OAK_PRESSURE_PLATE);
							content.accept(Items.REDSTONE_TORCH);
							content.accept(Items.STONE_BUTTON);
							content.accept(Items.OAK_TRAPDOOR);
							content.accept(Items.OAK_FENCE_GATE);
							content.accept(Items.REDSTONE_LAMP);
							content.accept(Items.TRIPWIRE_HOOK);
							content.accept(Items.OAK_BUTTON);
							content.accept(Items.LIGHT_WEIGHTED_PRESSURE_PLATE);
							content.accept(Items.HEAVY_WEIGHTED_PRESSURE_PLATE);
							content.accept(Items.DAYLIGHT_DETECTOR);
							content.accept(Items.REDSTONE_BLOCK);
							content.accept(Items.HOPPER);
							content.accept(Items.DROPPER);
							content.accept(Items.IRON_TRAPDOOR);
							content.accept(Items.SPRUCE_FENCE_GATE);
							content.accept(Items.BIRCH_FENCE_GATE);
							content.accept(Items.JUNGLE_FENCE_GATE);
							content.accept(Items.DARK_OAK_FENCE_GATE);
							content.accept(Items.ACACIA_FENCE_GATE);
							content.accept(Items.OAK_DOOR);
							content.accept(Items.IRON_DOOR);
							content.accept(Items.REDSTONE);
							content.accept(Items.REPEATER);
							content.accept(Items.COMPARATOR);
							content.accept(Items.SPRUCE_DOOR);
							content.accept(Items.BIRCH_DOOR);
							content.accept(Items.JUNGLE_DOOR);
							content.accept(Items.ACACIA_DOOR);
							content.accept(Items.DARK_OAK_DOOR);
						})
						.build()
		);
		Registry.register(
				registry,
				TRANSPORTATION,
				CreativeModeTab.builder(CreativeModeTab.Row.TOP, 3)
						.title(Component.translatable("itemGroup.functional"))
						.icon(() -> new ItemStack(Items.POWERED_RAIL))
						.displayItems((displayContext, content) -> {
							content.accept(Items.POWERED_RAIL);
							content.accept(Items.DETECTOR_RAIL);
							content.accept(Items.RAIL);
							content.accept(Items.ACTIVATOR_RAIL);
							content.accept(Items.MINECART);
							content.accept(Items.SADDLE);
							content.accept(Items.OAK_BOAT);
							content.accept(Items.CHEST_MINECART);
							content.accept(Items.FURNACE_MINECART);
							content.accept(Items.CARROT_ON_A_STICK);
							content.accept(Items.TNT_MINECART);
							content.accept(Items.HOPPER_MINECART);
						})
						.build()
		);
		Registry.register(
				registry,
				MISC,
				CreativeModeTab.builder(CreativeModeTab.Row.TOP, 4)
						.title(Component.translatable("itemGroup.misc"))
						.icon(() -> new ItemStack(Items.LAVA_BUCKET))
						.displayItems((displayContext, content) -> {
							content.accept(Items.BEACON);
							content.accept(Items.BUCKET);
							content.accept(Items.WATER_BUCKET);
							content.accept(Items.LAVA_BUCKET);
							content.accept(Items.SNOWBALL);
							content.accept(Items.MILK_BUCKET);
							content.accept(Items.PAPER);
							content.accept(Items.BOOK);
							content.accept(Items.SLIME_BALL);
							content.accept(Items.BONE);
							content.accept(Items.ENDER_PEARL);
							content.accept(Items.ENDER_EYE);
							content.accept(Items.CREEPER_SPAWN_EGG);
							content.accept(Items.SKELETON_SPAWN_EGG);
							content.accept(Items.SPIDER_SPAWN_EGG);
							content.accept(Items.ZOMBIE_SPAWN_EGG);
							content.accept(Items.SLIME_SPAWN_EGG);
							content.accept(Items.GHAST_SPAWN_EGG);
							content.accept(Items.ZOMBIFIED_PIGLIN_SPAWN_EGG);
							content.accept(Items.ENDERMAN_SPAWN_EGG);
							content.accept(Items.CAVE_SPIDER_SPAWN_EGG);
							content.accept(Items.SILVERFISH_SPAWN_EGG);
							content.accept(Items.BLAZE_SPAWN_EGG);
							content.accept(Items.MAGMA_CUBE_SPAWN_EGG);
							content.accept(Items.BAT_SPAWN_EGG);
							content.accept(Items.WITCH_SPAWN_EGG);
							content.accept(Items.ENDERMITE_SPAWN_EGG);
							content.accept(Items.GUARDIAN_SPAWN_EGG);
							content.accept(Items.PIG_SPAWN_EGG);
							content.accept(Items.SHEEP_SPAWN_EGG);
							content.accept(Items.COW_SPAWN_EGG);
							content.accept(Items.CHICKEN_SPAWN_EGG);
							content.accept(Items.SQUID_SPAWN_EGG);
							content.accept(Items.WOLF_SPAWN_EGG);
							content.accept(Items.MOOSHROOM_SPAWN_EGG);
							content.accept(Items.OCELOT_SPAWN_EGG);
							content.accept(Items.HORSE_SPAWN_EGG);
							content.accept(Items.RABBIT_SPAWN_EGG);
							content.accept(Items.VILLAGER_SPAWN_EGG);
							content.accept(Items.EXPERIENCE_BOTTLE);
							content.accept(Items.FIRE_CHARGE);
							content.accept(Items.WRITABLE_BOOK);
							content.accept(Items.MAP);
							content.accept(Items.FIREWORK_STAR);
							content.accept(Items.IRON_HORSE_ARMOR);
							content.accept(Items.GOLDEN_HORSE_ARMOR);
							content.accept(Items.DIAMOND_HORSE_ARMOR);
							content.accept(Items.MUSIC_DISC_13);
							content.accept(Items.MUSIC_DISC_CAT);
							content.accept(Items.MUSIC_DISC_BLOCKS);
							content.accept(Items.MUSIC_DISC_CHIRP);
							content.accept(Items.MUSIC_DISC_FAR);
							content.accept(Items.MUSIC_DISC_MALL);
							content.accept(Items.MUSIC_DISC_MELLOHI);
							content.accept(Items.MUSIC_DISC_STAL);
							content.accept(Items.MUSIC_DISC_STRAD);
							content.accept(Items.MUSIC_DISC_WARD);
							content.accept(Items.MUSIC_DISC_11);
							content.accept(Items.MUSIC_DISC_WAIT);
						})
						.build()
		);
		Registry.register(
				registry,
				SEARCH,
				((CreativeModeTabBuilderMixinInterface)CreativeModeTab.builder(CreativeModeTab.Row.TOP, 6)
						.title(Component.translatable("itemGroup.search"))
						.icon(() -> new ItemStack(Items.COMPASS))
						.displayItems((itemDisplayParameters, output) -> {
							Set<ItemStack> set = ItemStackLinkedSet.createTypeAndComponentsSet();
							for (CreativeModeTab creativeModeTab : registry) {
								if (creativeModeTab.getType() != CreativeModeTab.Type.SEARCH) {
									set.addAll(creativeModeTab.getSearchTabDisplayItems());
								}
							}
							output.acceptAll(set);
							if (NormalCreative.config.getSetting("modernItems")) {
//								BUILDING BLOCKS
								output.accept(Items.OAK_WOOD);
								output.accept(Items.STRIPPED_OAK_LOG);
								output.accept(Items.STRIPPED_OAK_WOOD);
								output.accept(Items.SPRUCE_WOOD);
								output.accept(Items.STRIPPED_SPRUCE_LOG);
								output.accept(Items.STRIPPED_SPRUCE_WOOD);
								output.accept(Items.SPRUCE_TRAPDOOR);
								output.accept(Items.SPRUCE_PRESSURE_PLATE);
								output.accept(Items.SPRUCE_BUTTON);
								output.accept(Items.BIRCH_WOOD);
								output.accept(Items.STRIPPED_BIRCH_LOG);
								output.accept(Items.STRIPPED_BIRCH_WOOD);
								output.accept(Items.BIRCH_TRAPDOOR);
								output.accept(Items.BIRCH_PRESSURE_PLATE);
								output.accept(Items.BIRCH_BUTTON);
								output.accept(Items.JUNGLE_WOOD);
								output.accept(Items.STRIPPED_JUNGLE_LOG);
								output.accept(Items.STRIPPED_JUNGLE_WOOD);
								output.accept(Items.JUNGLE_TRAPDOOR);
								output.accept(Items.JUNGLE_PRESSURE_PLATE);
								output.accept(Items.JUNGLE_BUTTON);
								output.accept(Items.ACACIA_WOOD);
								output.accept(Items.STRIPPED_ACACIA_LOG);
								output.accept(Items.STRIPPED_ACACIA_WOOD);
								output.accept(Items.ACACIA_TRAPDOOR);
								output.accept(Items.ACACIA_PRESSURE_PLATE);
								output.accept(Items.ACACIA_BUTTON);
								output.accept(Items.DARK_OAK_WOOD);
								output.accept(Items.STRIPPED_DARK_OAK_LOG);
								output.accept(Items.STRIPPED_DARK_OAK_WOOD);
								output.accept(Items.DARK_OAK_TRAPDOOR);
								output.accept(Items.DARK_OAK_PRESSURE_PLATE);
								output.accept(Items.DARK_OAK_BUTTON);
								output.accept(Items.MANGROVE_LOG);
								output.accept(Items.MANGROVE_WOOD);
								output.accept(Items.STRIPPED_MANGROVE_LOG);
								output.accept(Items.STRIPPED_MANGROVE_WOOD);
								output.accept(Items.MANGROVE_PLANKS);
								output.accept(Items.MANGROVE_STAIRS);
								output.accept(Items.MANGROVE_SLAB);
								output.accept(Items.MANGROVE_FENCE);
								output.accept(Items.MANGROVE_FENCE_GATE);
								output.accept(Items.MANGROVE_DOOR);
								output.accept(Items.MANGROVE_TRAPDOOR);
								output.accept(Items.MANGROVE_PRESSURE_PLATE);
								output.accept(Items.MANGROVE_BUTTON);
								output.accept(Items.CHERRY_LOG);
								output.accept(Items.CHERRY_WOOD);
								output.accept(Items.STRIPPED_CHERRY_LOG);
								output.accept(Items.STRIPPED_CHERRY_WOOD);
								output.accept(Items.CHERRY_PLANKS);
								output.accept(Items.CHERRY_STAIRS);
								output.accept(Items.CHERRY_SLAB);
								output.accept(Items.CHERRY_FENCE);
								output.accept(Items.CHERRY_FENCE_GATE);
								output.accept(Items.CHERRY_DOOR);
								output.accept(Items.CHERRY_TRAPDOOR);
								output.accept(Items.CHERRY_PRESSURE_PLATE);
								output.accept(Items.CHERRY_BUTTON);
								output.accept(Items.PALE_OAK_LOG);
								output.accept(Items.PALE_OAK_WOOD);
								output.accept(Items.STRIPPED_PALE_OAK_LOG);
								output.accept(Items.STRIPPED_PALE_OAK_WOOD);
								output.accept(Items.PALE_OAK_PLANKS);
								output.accept(Items.PALE_OAK_STAIRS);
								output.accept(Items.PALE_OAK_SLAB);
								output.accept(Items.PALE_OAK_FENCE);
								output.accept(Items.PALE_OAK_FENCE_GATE);
								output.accept(Items.PALE_OAK_DOOR);
								output.accept(Items.PALE_OAK_TRAPDOOR);
								output.accept(Items.PALE_OAK_PRESSURE_PLATE);
								output.accept(Items.PALE_OAK_BUTTON);
								output.accept(Items.BAMBOO_BLOCK);
								output.accept(Items.STRIPPED_BAMBOO_BLOCK);
								output.accept(Items.BAMBOO_PLANKS);
								output.accept(Items.BAMBOO_MOSAIC);
								output.accept(Items.BAMBOO_STAIRS);
								output.accept(Items.BAMBOO_MOSAIC_STAIRS);
								output.accept(Items.BAMBOO_SLAB);
								output.accept(Items.BAMBOO_MOSAIC_SLAB);
								output.accept(Items.BAMBOO_FENCE);
								output.accept(Items.BAMBOO_FENCE_GATE);
								output.accept(Items.BAMBOO_DOOR);
								output.accept(Items.BAMBOO_TRAPDOOR);
								output.accept(Items.BAMBOO_PRESSURE_PLATE);
								output.accept(Items.BAMBOO_BUTTON);
								output.accept(Items.CRIMSON_STEM);
								output.accept(Items.CRIMSON_HYPHAE);
								output.accept(Items.STRIPPED_CRIMSON_STEM);
								output.accept(Items.STRIPPED_CRIMSON_HYPHAE);
								output.accept(Items.CRIMSON_PLANKS);
								output.accept(Items.CRIMSON_STAIRS);
								output.accept(Items.CRIMSON_SLAB);
								output.accept(Items.CRIMSON_FENCE);
								output.accept(Items.CRIMSON_FENCE_GATE);
								output.accept(Items.CRIMSON_DOOR);
								output.accept(Items.CRIMSON_TRAPDOOR);
								output.accept(Items.CRIMSON_PRESSURE_PLATE);
								output.accept(Items.CRIMSON_BUTTON);
								output.accept(Items.WARPED_STEM);
								output.accept(Items.WARPED_HYPHAE);
								output.accept(Items.STRIPPED_WARPED_STEM);
								output.accept(Items.STRIPPED_WARPED_HYPHAE);
								output.accept(Items.WARPED_PLANKS);
								output.accept(Items.WARPED_STAIRS);
								output.accept(Items.WARPED_SLAB);
								output.accept(Items.WARPED_FENCE);
								output.accept(Items.WARPED_FENCE_GATE);
								output.accept(Items.WARPED_DOOR);
								output.accept(Items.WARPED_TRAPDOOR);
								output.accept(Items.WARPED_PRESSURE_PLATE);
								output.accept(Items.WARPED_BUTTON);
								output.accept(Items.STONE_STAIRS);
								output.accept(Items.STONE_SLAB);
								output.accept(Items.MOSSY_COBBLESTONE_STAIRS);
								output.accept(Items.MOSSY_COBBLESTONE_SLAB);
								output.accept(Items.SMOOTH_STONE);
								output.accept(Items.STONE_BRICK_WALL);
								output.accept(Items.MOSSY_STONE_BRICK_STAIRS);
								output.accept(Items.MOSSY_STONE_BRICK_SLAB);
								output.accept(Items.MOSSY_STONE_BRICK_WALL);
								output.accept(Items.GRANITE_STAIRS);
								output.accept(Items.GRANITE_SLAB);
								output.accept(Items.GRANITE_WALL);
								output.accept(Items.POLISHED_GRANITE_STAIRS);
								output.accept(Items.POLISHED_GRANITE_SLAB);
								output.accept(Items.DIORITE_STAIRS);
								output.accept(Items.DIORITE_SLAB);
								output.accept(Items.DIORITE_WALL);
								output.accept(Items.POLISHED_DIORITE_STAIRS);
								output.accept(Items.POLISHED_DIORITE_SLAB);
								output.accept(Items.ANDESITE_STAIRS);
								output.accept(Items.ANDESITE_SLAB);
								output.accept(Items.ANDESITE_WALL);
								output.accept(Items.POLISHED_ANDESITE_STAIRS);
								output.accept(Items.POLISHED_ANDESITE_SLAB);
								output.accept(Items.DEEPSLATE);
								output.accept(Items.COBBLED_DEEPSLATE);
								output.accept(Items.COBBLED_DEEPSLATE_STAIRS);
								output.accept(Items.COBBLED_DEEPSLATE_SLAB);
								output.accept(Items.COBBLED_DEEPSLATE_WALL);
								output.accept(Items.CHISELED_DEEPSLATE);
								output.accept(Items.POLISHED_DEEPSLATE);
								output.accept(Items.POLISHED_DEEPSLATE_STAIRS);
								output.accept(Items.POLISHED_DEEPSLATE_SLAB);
								output.accept(Items.POLISHED_DEEPSLATE_WALL);
								output.accept(Items.DEEPSLATE_BRICKS);
								output.accept(Items.CRACKED_DEEPSLATE_BRICKS);
								output.accept(Items.DEEPSLATE_BRICK_STAIRS);
								output.accept(Items.DEEPSLATE_BRICK_SLAB);
								output.accept(Items.DEEPSLATE_BRICK_WALL);
								output.accept(Items.DEEPSLATE_TILES);
								output.accept(Items.CRACKED_DEEPSLATE_TILES);
								output.accept(Items.DEEPSLATE_TILE_STAIRS);
								output.accept(Items.DEEPSLATE_TILE_SLAB);
								output.accept(Items.DEEPSLATE_TILE_WALL);
								output.accept(Items.REINFORCED_DEEPSLATE);
								output.accept(Items.TUFF);
								output.accept(Items.TUFF_STAIRS);
								output.accept(Items.TUFF_SLAB);
								output.accept(Items.TUFF_WALL);
								output.accept(Items.CHISELED_TUFF);
								output.accept(Items.POLISHED_TUFF);
								output.accept(Items.POLISHED_TUFF_STAIRS);
								output.accept(Items.POLISHED_TUFF_SLAB);
								output.accept(Items.POLISHED_TUFF_WALL);
								output.accept(Items.TUFF_BRICKS);
								output.accept(Items.TUFF_BRICK_STAIRS);
								output.accept(Items.TUFF_BRICK_SLAB);
								output.accept(Items.TUFF_BRICK_WALL);
								output.accept(Items.CHISELED_TUFF_BRICKS);
								output.accept(Items.BRICK_WALL);
								output.accept(Items.PACKED_MUD);
								output.accept(Items.MUD_BRICKS);
								output.accept(Items.MUD_BRICK_STAIRS);
								output.accept(Items.MUD_BRICK_SLAB);
								output.accept(Items.MUD_BRICK_WALL);
								output.accept(Items.RESIN_BRICKS);
								output.accept(Items.RESIN_BRICK_STAIRS);
								output.accept(Items.RESIN_BRICK_SLAB);
								output.accept(Items.RESIN_BRICK_WALL);
								output.accept(Items.CHISELED_RESIN_BRICKS);
								output.accept(Items.SANDSTONE_WALL);
								output.accept(Items.SMOOTH_SANDSTONE);
								output.accept(Items.SMOOTH_SANDSTONE_STAIRS);
								output.accept(Items.SMOOTH_SANDSTONE_SLAB);
								output.accept(Items.CUT_STANDSTONE_SLAB);
								output.accept(Items.RED_SANDSTONE_WALL);
								output.accept(Items.SMOOTH_RED_SANDSTONE);
								output.accept(Items.SMOOTH_RED_SANDSTONE_STAIRS);
								output.accept(Items.SMOOTH_RED_SANDSTONE_SLAB);
								output.accept(Items.CUT_RED_SANDSTONE_SLAB);
								output.accept(Items.PRISMARINE_STAIRS);
								output.accept(Items.PRISMARINE_SLAB);
								output.accept(Items.PRISMARINE_WALL);
								output.accept(Items.PRISMARINE_BRICK_STAIRS);
								output.accept(Items.PRISMARINE_BRICK_SLAB);
								output.accept(Items.DARK_PRISMARINE_STAIRS);
								output.accept(Items.DARK_PRISMARINE_SLAB);
								output.accept(Items.CRACKED_NETHER_BRICKS);
								output.accept(Items.NETHER_BRICK_WALL);
								output.accept(Items.CHISELED_NETHER_BRICKS);
								output.accept(Items.RED_NETHER_BRICKS);
								output.accept(Items.RED_NETHER_BRICK_STAIRS);
								output.accept(Items.RED_NETHER_BRICK_SLAB);
								output.accept(Items.RED_NETHER_BRICK_WALL);
								output.accept(Items.BASALT);
								output.accept(Items.SMOOTH_BASALT);
								output.accept(Items.POLISHED_BASALT);
								output.accept(Items.BLACKSTONE);
								output.accept(Items.GILDED_BLACKSTONE);
								output.accept(Items.BLACKSTONE_STAIRS);
								output.accept(Items.BLACKSTONE_SLAB);
								output.accept(Items.BLACKSTONE_WALL);
								output.accept(Items.CHISELED_POLISHED_BLACKSTONE);
								output.accept(Items.POLISHED_BLACKSTONE);
								output.accept(Items.POLISHED_BLACKSTONE_STAIRS);
								output.accept(Items.POLISHED_BLACKSTONE_SLAB);
								output.accept(Items.POLISHED_BLACKSTONE_WALL);
								output.accept(Items.POLISHED_BLACKSTONE_PRESSURE_PLATE);
								output.accept(Items.POLISHED_BLACKSTONE_BUTTON);
								output.accept(Items.POLISHED_BLACKSTONE_BRICKS);
								output.accept(Items.CRACKED_POLISHED_BLACKSTONE_BRICKS);
								output.accept(Items.POLISHED_BLACKSTONE_BRICK_STAIRS);
								output.accept(Items.POLISHED_BLACKSTONE_BRICK_SLAB);
								output.accept(Items.POLISHED_BLACKSTONE_BRICK_WALL);
								output.accept(Items.END_STONE_BRICKS);
								output.accept(Items.END_STONE_BRICK_STAIRS);
								output.accept(Items.END_STONE_BRICK_SLAB);
								output.accept(Items.END_STONE_BRICK_WALL);
								output.accept(Items.PURPUR_BLOCK);
								output.accept(Items.PURPUR_PILLAR);
								output.accept(Items.PURPUR_STAIRS);
								output.accept(Items.PURPUR_SLAB);
								output.accept(Items.CHAIN);
								output.accept(Items.NETHERITE_BLOCK);
								output.accept(Items.QUARTZ_BRICKS);
								output.accept(Items.SMOOTH_QUARTZ);
								output.accept(Items.SMOOTH_QUARTZ_STAIRS);
								output.accept(Items.SMOOTH_QUARTZ_SLAB);
								output.accept(Items.AMETHYST_BLOCK);
								output.accept(Items.COPPER_BLOCK);
								output.accept(Items.CHISELED_COPPER);
								output.accept(Items.COPPER_GRATE);
								output.accept(Items.CUT_COPPER);
								output.accept(Items.CUT_COPPER_STAIRS);
								output.accept(Items.CUT_COPPER_SLAB);
								output.accept(Items.COPPER_DOOR);
								output.accept(Items.COPPER_TRAPDOOR);
								output.accept(Items.COPPER_BULB);
								output.accept(Items.EXPOSED_COPPER);
								output.accept(Items.EXPOSED_CHISELED_COPPER);
								output.accept(Items.EXPOSED_COPPER_GRATE);
								output.accept(Items.EXPOSED_CUT_COPPER);
								output.accept(Items.EXPOSED_CUT_COPPER_STAIRS);
								output.accept(Items.EXPOSED_CUT_COPPER_SLAB);
								output.accept(Items.EXPOSED_COPPER_DOOR);
								output.accept(Items.EXPOSED_COPPER_TRAPDOOR);
								output.accept(Items.EXPOSED_COPPER_BULB);
								output.accept(Items.WEATHERED_COPPER);
								output.accept(Items.WEATHERED_CHISELED_COPPER);
								output.accept(Items.WEATHERED_COPPER_GRATE);
								output.accept(Items.WEATHERED_CUT_COPPER);
								output.accept(Items.WEATHERED_CUT_COPPER_STAIRS);
								output.accept(Items.WEATHERED_CUT_COPPER_SLAB);
								output.accept(Items.WEATHERED_COPPER_DOOR);
								output.accept(Items.WEATHERED_COPPER_TRAPDOOR);
								output.accept(Items.WEATHERED_COPPER_BULB);
								output.accept(Items.OXIDIZED_COPPER);
								output.accept(Items.OXIDIZED_CHISELED_COPPER);
								output.accept(Items.OXIDIZED_COPPER_GRATE);
								output.accept(Items.OXIDIZED_CUT_COPPER);
								output.accept(Items.OXIDIZED_CUT_COPPER_STAIRS);
								output.accept(Items.OXIDIZED_CUT_COPPER_SLAB);
								output.accept(Items.OXIDIZED_COPPER_DOOR);
								output.accept(Items.OXIDIZED_COPPER_TRAPDOOR);
								output.accept(Items.OXIDIZED_COPPER_BULB);
								output.accept(Items.WAXED_COPPER_BLOCK);
								output.accept(Items.WAXED_CHISELED_COPPER);
								output.accept(Items.WAXED_COPPER_GRATE);
								output.accept(Items.WAXED_CUT_COPPER);
								output.accept(Items.WAXED_CUT_COPPER_STAIRS);
								output.accept(Items.WAXED_CUT_COPPER_SLAB);
								output.accept(Items.WAXED_COPPER_DOOR);
								output.accept(Items.WAXED_COPPER_TRAPDOOR);
								output.accept(Items.WAXED_COPPER_BULB);
								output.accept(Items.WAXED_EXPOSED_COPPER);
								output.accept(Items.WAXED_EXPOSED_CHISELED_COPPER);
								output.accept(Items.WAXED_EXPOSED_COPPER_GRATE);
								output.accept(Items.WAXED_EXPOSED_CUT_COPPER);
								output.accept(Items.WAXED_EXPOSED_CUT_COPPER_STAIRS);
								output.accept(Items.WAXED_EXPOSED_CUT_COPPER_SLAB);
								output.accept(Items.WAXED_EXPOSED_COPPER_DOOR);
								output.accept(Items.WAXED_EXPOSED_COPPER_TRAPDOOR);
								output.accept(Items.WAXED_EXPOSED_COPPER_BULB);
								output.accept(Items.WAXED_WEATHERED_COPPER);
								output.accept(Items.WAXED_WEATHERED_CHISELED_COPPER);
								output.accept(Items.WAXED_WEATHERED_COPPER_GRATE);
								output.accept(Items.WAXED_WEATHERED_CUT_COPPER);
								output.accept(Items.WAXED_WEATHERED_CUT_COPPER_STAIRS);
								output.accept(Items.WAXED_WEATHERED_CUT_COPPER_SLAB);
								output.accept(Items.WAXED_WEATHERED_COPPER_DOOR);
								output.accept(Items.WAXED_WEATHERED_COPPER_TRAPDOOR);
								output.accept(Items.WAXED_WEATHERED_COPPER_BULB);
								output.accept(Items.WAXED_OXIDIZED_COPPER);
								output.accept(Items.WAXED_OXIDIZED_CHISELED_COPPER);
								output.accept(Items.WAXED_OXIDIZED_COPPER_GRATE);
								output.accept(Items.WAXED_OXIDIZED_CUT_COPPER);
								output.accept(Items.WAXED_OXIDIZED_CUT_COPPER_STAIRS);
								output.accept(Items.WAXED_OXIDIZED_CUT_COPPER_SLAB);
								output.accept(Items.WAXED_OXIDIZED_COPPER_DOOR);
								output.accept(Items.WAXED_OXIDIZED_COPPER_TRAPDOOR);
								output.accept(Items.WAXED_OXIDIZED_COPPER_BULB);
//								COLOURED BLOCKS
								output.accept(Items.WHITE_CONCRETE);
								output.accept(Items.LIGHT_GRAY_CONCRETE);
								output.accept(Items.GRAY_CONCRETE);
								output.accept(Items.BLACK_CONCRETE);
								output.accept(Items.BROWN_CONCRETE);
								output.accept(Items.RED_CONCRETE);
								output.accept(Items.ORANGE_CONCRETE);
								output.accept(Items.YELLOW_CONCRETE);
								output.accept(Items.LIME_CONCRETE);
								output.accept(Items.GREEN_CONCRETE);
								output.accept(Items.CYAN_CONCRETE);
								output.accept(Items.LIGHT_BLUE_CONCRETE);
								output.accept(Items.BLUE_CONCRETE);
								output.accept(Items.PURPLE_CONCRETE);
								output.accept(Items.MAGENTA_CONCRETE);
								output.accept(Items.PINK_CONCRETE);
								output.accept(Items.WHITE_CONCRETE_POWDER);
								output.accept(Items.LIGHT_GRAY_CONCRETE_POWDER);
								output.accept(Items.GRAY_CONCRETE_POWDER);
								output.accept(Items.BLACK_CONCRETE_POWDER);
								output.accept(Items.BROWN_CONCRETE_POWDER);
								output.accept(Items.RED_CONCRETE_POWDER);
								output.accept(Items.ORANGE_CONCRETE_POWDER);
								output.accept(Items.YELLOW_CONCRETE_POWDER);
								output.accept(Items.LIME_CONCRETE_POWDER);
								output.accept(Items.GREEN_CONCRETE_POWDER);
								output.accept(Items.CYAN_CONCRETE_POWDER);
								output.accept(Items.LIGHT_BLUE_CONCRETE_POWDER);
								output.accept(Items.BLUE_CONCRETE_POWDER);
								output.accept(Items.PURPLE_CONCRETE_POWDER);
								output.accept(Items.MAGENTA_CONCRETE_POWDER);
								output.accept(Items.PINK_CONCRETE_POWDER);
								output.accept(Items.WHITE_GLAZED_TERRACOTTA);
								output.accept(Items.LIGHT_GRAY_GLAZED_TERRACOTTA);
								output.accept(Items.GRAY_GLAZED_TERRACOTTA);
								output.accept(Items.BLACK_GLAZED_TERRACOTTA);
								output.accept(Items.BROWN_GLAZED_TERRACOTTA);
								output.accept(Items.RED_GLAZED_TERRACOTTA);
								output.accept(Items.ORANGE_GLAZED_TERRACOTTA);
								output.accept(Items.YELLOW_GLAZED_TERRACOTTA);
								output.accept(Items.LIME_GLAZED_TERRACOTTA);
								output.accept(Items.GREEN_GLAZED_TERRACOTTA);
								output.accept(Items.CYAN_GLAZED_TERRACOTTA);
								output.accept(Items.LIGHT_BLUE_GLAZED_TERRACOTTA);
								output.accept(Items.BLUE_GLAZED_TERRACOTTA);
								output.accept(Items.PURPLE_GLAZED_TERRACOTTA);
								output.accept(Items.MAGENTA_GLAZED_TERRACOTTA);
								output.accept(Items.PINK_GLAZED_TERRACOTTA);
								output.accept(Items.TINTED_GLASS);
								output.accept(Items.SHULKER_BOX);
								output.accept(Items.WHITE_SHULKER_BOX);
								output.accept(Items.LIGHT_GRAY_SHULKER_BOX);
								output.accept(Items.GRAY_SHULKER_BOX);
								output.accept(Items.BLACK_SHULKER_BOX);
								output.accept(Items.BROWN_SHULKER_BOX);
								output.accept(Items.RED_SHULKER_BOX);
								output.accept(Items.ORANGE_SHULKER_BOX);
								output.accept(Items.YELLOW_SHULKER_BOX);
								output.accept(Items.LIME_SHULKER_BOX);
								output.accept(Items.GREEN_SHULKER_BOX);
								output.accept(Items.CYAN_SHULKER_BOX);
								output.accept(Items.LIGHT_BLUE_SHULKER_BOX);
								output.accept(Items.BLUE_SHULKER_BOX);
								output.accept(Items.PURPLE_SHULKER_BOX);
								output.accept(Items.MAGENTA_SHULKER_BOX);
								output.accept(Items.PINK_SHULKER_BOX);
								output.accept(Items.WHITE_BED);
								output.accept(Items.LIGHT_GRAY_BED);
								output.accept(Items.GRAY_BED);
								output.accept(Items.BLACK_BED);
								output.accept(Items.BROWN_BED);
								output.accept(Items.ORANGE_BED);
								output.accept(Items.YELLOW_BED);
								output.accept(Items.LIME_BED);
								output.accept(Items.GREEN_BED);
								output.accept(Items.CYAN_BED);
								output.accept(Items.LIGHT_BLUE_BED);
								output.accept(Items.BLUE_BED);
								output.accept(Items.PURPLE_BED);
								output.accept(Items.MAGENTA_BED);
								output.accept(Items.PINK_BED);
								output.accept(Items.CANDLE);
								output.accept(Items.WHITE_CANDLE);
								output.accept(Items.LIGHT_GRAY_CANDLE);
								output.accept(Items.GRAY_CANDLE);
								output.accept(Items.BLACK_CANDLE);
								output.accept(Items.BROWN_CANDLE);
								output.accept(Items.RED_CANDLE);
								output.accept(Items.ORANGE_CANDLE);
								output.accept(Items.YELLOW_CANDLE);
								output.accept(Items.LIME_CANDLE);
								output.accept(Items.GREEN_CANDLE);
								output.accept(Items.CYAN_CANDLE);
								output.accept(Items.LIGHT_BLUE_CANDLE);
								output.accept(Items.BLUE_CANDLE);
								output.accept(Items.PURPLE_CANDLE);
								output.accept(Items.MAGENTA_CANDLE);
								output.accept(Items.PINK_CANDLE);
//								NATURAL BLOCKS
								output.accept(Items.DIRT_PATH);
								output.accept(Items.ROOTED_DIRT);
								output.accept(Items.MUD);
								output.accept(Items.BLUE_ICE);
								output.accept(Items.MOSS_BLOCK);
								output.accept(Items.MOSS_CARPET);
								output.accept(Items.PALE_MOSS_BLOCK);
								output.accept(Items.PALE_MOSS_CARPET);
								output.accept(Items.PALE_HANGING_MOSS);
								output.accept(Items.CALCITE);
								output.accept(Items.DRIPSTONE_BLOCK);
								output.accept(Items.POINTED_DRIPSTONE);
								output.accept(Items.MAGMA_BLOCK);
								output.accept(Items.CRYING_OBSIDIAN);
								output.accept(Items.CRIMSON_NYLIUM);
								output.accept(Items.WARPED_NYLIUM);
								output.accept(Items.SOUL_SOIL);
								output.accept(Items.BONE_BLOCK);
								output.accept(Items.DEEPSLATE_COAL_ORE);
								output.accept(Items.DEEPSLATE_IRON_ORE);
								output.accept(Items.DEEPSLATE_COPPER_ORE);
								output.accept(Items.DEEPSLATE_GOLD_ORE);
								output.accept(Items.DEEPSLATE_REDSTONE_ORE);
								output.accept(Items.DEEPSLATE_EMERALD_ORE);
								output.accept(Items.DEEPSLATE_LAPIS_ORE);
								output.accept(Items.DEEPSLATE_DIAMOND_ORE);
								output.accept(Items.NETHER_GOLD_ORE);
								output.accept(Items.ANCIENT_DEBRIS);
								output.accept(Items.RAW_IRON_BLOCK);
								output.accept(Items.RAW_COPPER_BLOCK);
								output.accept(Items.RAW_GOLD_BLOCK);
								output.accept(Items.BUDDING_AMETHYST);
								output.accept(Items.SMALL_AMETHYST_BUD);
								output.accept(Items.MEDIUM_AMETHYST_BUD);
								output.accept(Items.LARGE_AMETHYST_BUD);
								output.accept(Items.AMETHYST_CLUSTER);
								output.accept(Items.MANGROVE_ROOTS);
								output.accept(Items.MUDDY_MANGROVE_ROOTS);
								output.accept(Items.MUSHROOM_STEM);
								output.accept(Items.MANGROVE_LEAVES);
								output.accept(Items.CHERRY_LEAVES);
								output.accept(Items.PALE_OAK_LEAVES);
								output.accept(Items.AZALEA_LEAVES);
								output.accept(Items.FLOWERING_AZALEA_LEAVES);
								output.accept(Items.NETHER_WART_BLOCK);
								output.accept(Items.WARPED_WART_BLOCK);
								output.accept(Items.MANGROVE_PROPAGULE);
								output.accept(Items.CHERRY_SAPLING);
								output.accept(Items.PALE_OAK_SAPLING);
								output.accept(Items.AZALEA);
								output.accept(Items.FLOWERING_AZALEA);
								output.accept(Items.CRIMSON_FUNGUS);
								output.accept(Items.WARPED_FUNGUS);
								output.accept(Items.CORNFLOWER);
								output.accept(Items.TORCHFLOWER);
								output.accept(Items.CLOSED_EYEBLOSSOM);
								output.accept(Items.OPEN_EYEBLOSSOM);
								output.accept(Items.WITHER_ROSE);
								output.accept(Items.PINK_PETALS);
								output.accept(Items.SPORE_BLOSSOM);
								output.accept(Items.BAMBOO);
								output.accept(Items.CRIMSON_ROOTS);
								output.accept(Items.WARPED_ROOTS);
								output.accept(Items.NETHER_SPROUTS);
								output.accept(Items.WEEPING_VINES);
								output.accept(Items.TWISTING_VINES);
								output.accept(Items.PITCHER_PLANT);
								output.accept(Items.BIG_DRIPLEAF);
								output.accept(Items.SMALL_DRIPLEAF);
								output.accept(Items.CHORUS_PLANT);
								output.accept(Items.CHORUS_FLOWER);
								output.accept(Items.GLOW_LICHEN);
								output.accept(Items.HANGING_ROOTS);
								output.accept(Items.FROGSPAWN);
								output.accept(Items.TURTLE_EGG);
								output.accept(Items.SNIFFER_EGG);
								output.accept(Items.BEETROOT_SEEDS);
								output.accept(Items.TORCHFLOWER_SEEDS);
								output.accept(Items.PITCHER_POD);
								output.accept(Items.GLOW_BERRIES);
								output.accept(Items.SWEET_BERRIES);
								output.accept(Items.SEAGRASS);
								output.accept(Items.SEA_PICKLE);
								output.accept(Items.KELP);
								output.accept(Items.DRIED_KELP_BLOCK);
								output.accept(Items.TUBE_CORAL_BLOCK);
								output.accept(Items.BRAIN_CORAL_BLOCK);
								output.accept(Items.BUBBLE_CORAL_BLOCK);
								output.accept(Items.FIRE_CORAL_BLOCK);
								output.accept(Items.HORN_CORAL_BLOCK);
								output.accept(Items.DEAD_TUBE_CORAL_BLOCK);
								output.accept(Items.DEAD_BRAIN_CORAL_BLOCK);
								output.accept(Items.DEAD_BUBBLE_CORAL_BLOCK);
								output.accept(Items.DEAD_FIRE_CORAL_BLOCK);
								output.accept(Items.DEAD_HORN_CORAL_BLOCK);
								output.accept(Items.TUBE_CORAL);
								output.accept(Items.BRAIN_CORAL);
								output.accept(Items.BUBBLE_CORAL);
								output.accept(Items.FIRE_CORAL);
								output.accept(Items.HORN_CORAL);
								output.accept(Items.DEAD_TUBE_CORAL);
								output.accept(Items.DEAD_BRAIN_CORAL);
								output.accept(Items.DEAD_BUBBLE_CORAL);
								output.accept(Items.DEAD_FIRE_CORAL);
								output.accept(Items.DEAD_HORN_CORAL);
								output.accept(Items.TUBE_CORAL_FAN);
								output.accept(Items.BRAIN_CORAL_FAN);
								output.accept(Items.BUBBLE_CORAL_FAN);
								output.accept(Items.FIRE_CORAL_FAN);
								output.accept(Items.HORN_CORAL_FAN);
								output.accept(Items.DEAD_TUBE_CORAL_FAN);
								output.accept(Items.DEAD_BRAIN_CORAL_FAN);
								output.accept(Items.DEAD_BUBBLE_CORAL_FAN);
								output.accept(Items.DEAD_FIRE_CORAL_FAN);
								output.accept(Items.DEAD_HORN_CORAL_FAN);
								output.accept(Items.CARVED_PUMPKIN);
								output.accept(Items.BEE_NEST);
								output.accept(Items.HONEYCOMB_BLOCK);
								output.accept(Items.RESIN_BLOCK);
								output.accept(Items.OCHRE_FROGLIGHT);
								output.accept(Items.VERDANT_FROGLIGHT);
								output.accept(Items.PEARLESCENT_FROGLIGHT);
								output.accept(Items.SCULK);
								output.accept(Items.SCULK_VEIN);
								output.accept(Items.SCULK_CATALYST);
								output.accept(Items.SCULK_SHRIEKER);
								output.accept(Items.SCULK_SENSOR);
//								FUNCTIONAL BLOCKS
								output.accept(Items.SOUL_TORCH);
								output.accept(Items.LANTERN);
								output.accept(Items.SOUL_LANTERN);
								output.accept(Items.END_ROD);
								output.accept(Items.SHROOMLIGHT);
								output.accept(Items.STONECUTTER);
								output.accept(Items.CARTOGRAPHY_TABLE);
								output.accept(Items.FLETCHING_TABLE);
								output.accept(Items.SMITHING_TABLE);
								output.accept(Items.GRINDSTONE);
								output.accept(Items.LOOM);
								output.accept(Items.SMOKER);
								output.accept(Items.BLAST_FURNACE);
								output.accept(Items.CAMPFIRE);
								output.accept(Items.SOUL_CAMPFIRE);
								output.accept(Items.COMPOSTER);
								output.accept(Items.END_CRYSTAL);
								output.accept(Items.BELL);
								output.accept(Items.CONDUIT);
								output.accept(Items.LODESTONE);
								output.accept(Items.SCAFFOLDING);
								output.accept(Items.BEEHIVE);
								output.accept(Items.SUSPICIOUS_SAND);
								output.accept(Items.SUSPICIOUS_GRAVEL);
								output.accept(Items.LIGHTNING_ROD);
								output.accept(Items.DECORATED_POT);
								output.accept(Items.GLOW_ITEM_FRAME);
								itemDisplayParameters.holders().lookup(Registries.PAINTING_VARIANT).ifPresent((registryLookup) -> generatePresetPaintings(output, itemDisplayParameters.holders(), registryLookup, (holder) -> holder.is(PaintingVariantTags.PLACEABLE), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS));
								output.accept(Items.CHISELED_BOOKSHELF);
								output.accept(Items.LECTERN);
								output.accept(Items.OAK_HANGING_SIGN);
								output.accept(Items.SPRUCE_SIGN);
								output.accept(Items.SPRUCE_HANGING_SIGN);
								output.accept(Items.BIRCH_SIGN);
								output.accept(Items.BIRCH_HANGING_SIGN);
								output.accept(Items.JUNGLE_SIGN);
								output.accept(Items.JUNGLE_HANGING_SIGN);
								output.accept(Items.ACACIA_SIGN);
								output.accept(Items.ACACIA_HANGING_SIGN);
								output.accept(Items.DARK_OAK_SIGN);
								output.accept(Items.DARK_OAK_HANGING_SIGN);
								output.accept(Items.MANGROVE_SIGN);
								output.accept(Items.MANGROVE_HANGING_SIGN);
								output.accept(Items.CHERRY_SIGN);
								output.accept(Items.CHERRY_HANGING_SIGN);
								output.accept(Items.PALE_OAK_SIGN);
								output.accept(Items.PALE_OAK_HANGING_SIGN);
								output.accept(Items.BAMBOO_SIGN);
								output.accept(Items.BAMBOO_HANGING_SIGN);
								output.accept(Items.CRIMSON_SIGN);
								output.accept(Items.CRIMSON_HANGING_SIGN);
								output.accept(Items.WARPED_SIGN);
								output.accept(Items.WARPED_HANGING_SIGN);
								output.accept(Items.BARREL);
								output.accept(Raid.getOminousBannerInstance(itemDisplayParameters.holders().lookupOrThrow(Registries.BANNER_PATTERN)));
								output.accept(Items.PIGLIN_HEAD);
								output.accept(Items.VAULT);
								output.accept(Items.INFESTED_DEEPSLATE);
//								REDSTONE BLOCKS
								output.accept(Items.TARGET);
								output.accept(Items.CALIBRATED_SCULK_SENSOR);
								output.accept(Items.CRAFTER);
								output.accept(Items.OBSERVER);
//								TOOLS & UTILITIES
								output.accept(Items.NETHERITE_SHOVEL);
								output.accept(Items.NETHERITE_PICKAXE);
								output.accept(Items.NETHERITE_AXE);
								output.accept(Items.NETHERITE_HOE);
								output.accept(Items.COD_BUCKET);
								output.accept(Items.SALMON_BUCKET);
								output.accept(Items.TROPICAL_FISH_BUCKET);
								output.accept(Items.PUFFERFISH_BUCKET);
								output.accept(Items.AXOLOTL_BUCKET);
								output.accept(Items.TADPOLE_BUCKET);
								output.accept(Items.POWDER_SNOW_BUCKET);
								output.accept(Items.BRUSH);
								output.accept(Items.BUNDLE);
								output.accept(Items.WHITE_BUNDLE);
								output.accept(Items.LIGHT_GRAY_BUNDLE);
								output.accept(Items.GRAY_BUNDLE);
								output.accept(Items.BLACK_BUNDLE);
								output.accept(Items.BROWN_BUNDLE);
								output.accept(Items.RED_BUNDLE);
								output.accept(Items.ORANGE_BUNDLE);
								output.accept(Items.YELLOW_BUNDLE);
								output.accept(Items.LIME_BUNDLE);
								output.accept(Items.GREEN_BUNDLE);
								output.accept(Items.CYAN_BUNDLE);
								output.accept(Items.LIGHT_BLUE_BUNDLE);
								output.accept(Items.BLUE_BUNDLE);
								output.accept(Items.PURPLE_BUNDLE);
								output.accept(Items.MAGENTA_BUNDLE);
								output.accept(Items.PINK_BUNDLE);
								output.accept(Items.RECOVERY_COMPASS);
								output.accept(Items.SPYGLASS);
								output.accept(Items.WIND_CHARGE);
								output.accept(Items.ELYTRA);
								generateFireworksAllDurations(output, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
								output.accept(Items.WARPED_FUNGUS_ON_A_STICK);
								output.accept(Items.OAK_CHEST_BOAT);
								output.accept(Items.SPRUCE_BOAT);
								output.accept(Items.SPRUCE_CHEST_BOAT);
								output.accept(Items.BIRCH_BOAT);
								output.accept(Items.BIRCH_CHEST_BOAT);
								output.accept(Items.JUNGLE_BOAT);
								output.accept(Items.JUNGLE_CHEST_BOAT);
								output.accept(Items.ACACIA_BOAT);
								output.accept(Items.ACACIA_CHEST_BOAT);
								output.accept(Items.DARK_OAK_BOAT);
								output.accept(Items.DARK_OAK_CHEST_BOAT);
								output.accept(Items.MANGROVE_BOAT);
								output.accept(Items.MANGROVE_CHEST_BOAT);
								output.accept(Items.CHERRY_BOAT);
								output.accept(Items.CHERRY_CHEST_BOAT);
								output.accept(Items.PALE_OAK_BOAT);
								output.accept(Items.PALE_OAK_CHEST_BOAT);
								output.accept(Items.BAMBOO_RAFT);
								output.accept(Items.BAMBOO_CHEST_RAFT);
								itemDisplayParameters.holders().lookup(Registries.INSTRUMENT).ifPresent((registryLookup) -> generateInstrumentTypes(output, registryLookup, Items.GOAT_HORN, InstrumentTags.GOAT_HORNS, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS));
								output.accept(Items.MUSIC_DISC_CREATOR_MUSIC_BOX);
								output.accept(Items.MUSIC_DISC_CREATOR);
								output.accept(Items.MUSIC_DISC_PRECIPICE);
								output.accept(Items.MUSIC_DISC_OTHERSIDE);
								output.accept(Items.MUSIC_DISC_RELIC);
								output.accept(Items.MUSIC_DISC_5);
								output.accept(Items.MUSIC_DISC_PIGSTEP);
//								COMBAT
								output.accept(Items.NETHERITE_SWORD);
								output.accept(Items.TRIDENT);
								output.accept(Items.MACE);
								output.accept(Items.SHIELD);
								output.accept(Items.NETHERITE_HELMET);
								output.accept(Items.NETHERITE_CHESTPLATE);
								output.accept(Items.NETHERITE_LEGGINGS);
								output.accept(Items.NETHERITE_BOOTS);
								output.accept(Items.TURTLE_HELMET);
								output.accept(Items.LEATHER_HORSE_ARMOR);
								output.accept(Items.WOLF_ARMOR);
								output.accept(Items.TOTEM_OF_UNDYING);
								output.accept(Items.CROSSBOW);
								output.accept(Items.SPECTRAL_ARROW);
								itemDisplayParameters.holders().lookup(Registries.POTION).ifPresent((registryLookup) -> generatePotionEffectTypes(output, registryLookup, Items.TIPPED_ARROW, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS, itemDisplayParameters.enabledFeatures()));
//								FOOD & DRINKS
								output.accept(Items.CHORUS_FRUIT);
								output.accept(Items.BEETROOT);
								output.accept(Items.DRIED_KELP);
								output.accept(Items.BEETROOT_SOUP);
								generateSuspiciousStews(output, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
								output.accept(Items.HONEY_BOTTLE);
								generateOminousBottles(output, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
								itemDisplayParameters.holders().lookup(Registries.POTION).ifPresent((registryLookup) -> {
									generatePotionEffectTypes(output, registryLookup, Items.POTION, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS, itemDisplayParameters.enabledFeatures());
									generatePotionEffectTypes(output, registryLookup, Items.SPLASH_POTION, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS, itemDisplayParameters.enabledFeatures());
									generatePotionEffectTypes(output, registryLookup, Items.LINGERING_POTION, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS, itemDisplayParameters.enabledFeatures());
								});
//								INGREDIENTS
								output.accept(Items.RAW_IRON);
								output.accept(Items.RAW_COPPER);
								output.accept(Items.RAW_GOLD);
								output.accept(Items.AMETHYST_SHARD);
								output.accept(Items.IRON_NUGGET);
								output.accept(Items.COPPER_INGOT);
								output.accept(Items.NETHERITE_SCRAP);
								output.accept(Items.NETHERITE_INGOT);
								output.accept(Items.HONEYCOMB);
								output.accept(Items.RESIN_CLUMP);
								output.accept(Items.GLOW_INK_SAC);
								output.accept(Items.TURTLE_SCUTE);
								output.accept(Items.ARMADILLO_SCUTE);
								output.accept(Items.NAUTILUS_SHELL);
								output.accept(Items.HEART_OF_THE_SEA);
								output.accept(Items.BREEZE_ROD);
								output.accept(Items.HEAVY_CORE);
								output.accept(Items.SHULKER_SHELL);
								output.accept(Items.POPPED_CHORUS_FRUIT);
								output.accept(Items.ECHO_SHARD);
								output.accept(Items.DISC_FRAGMENT_5);
								output.accept(Items.WHITE_DYE);
								output.accept(Items.BROWN_DYE);
								output.accept(Items.RESIN_BRICK);
								output.accept(Items.DRAGON_BREATH);
								output.accept(Items.PHANTOM_MEMBRANE);
								output.accept(Items.FIELD_MASONED_BANNER_PATTERN);
								output.accept(Items.BORDURE_INDENTED_BANNER_PATTERN);
								output.accept(Items.FLOWER_BANNER_PATTERN);
								output.accept(Items.CREEPER_BANNER_PATTERN);
								output.accept(Items.SKULL_BANNER_PATTERN);
								output.accept(Items.MOJANG_BANNER_PATTERN);
								output.accept(Items.GLOBE_BANNER_PATTERN);
								output.accept(Items.PIGLIN_BANNER_PATTERN);
								output.accept(Items.FLOW_BANNER_PATTERN);
								output.accept(Items.GUSTER_BANNER_PATTERN);
								output.accept(Items.ANGLER_POTTERY_SHERD);
								output.accept(Items.ARCHER_POTTERY_SHERD);
								output.accept(Items.ARMS_UP_POTTERY_SHERD);
								output.accept(Items.BLADE_POTTERY_SHERD);
								output.accept(Items.BREWER_POTTERY_SHERD);
								output.accept(Items.BURN_POTTERY_SHERD);
								output.accept(Items.DANGER_POTTERY_SHERD);
								output.accept(Items.FLOW_POTTERY_SHERD);
								output.accept(Items.EXPLORER_POTTERY_SHERD);
								output.accept(Items.FRIEND_POTTERY_SHERD);
								output.accept(Items.GUSTER_POTTERY_SHERD);
								output.accept(Items.HEART_POTTERY_SHERD);
								output.accept(Items.HEARTBREAK_POTTERY_SHERD);
								output.accept(Items.HOWL_POTTERY_SHERD);
								output.accept(Items.MINER_POTTERY_SHERD);
								output.accept(Items.MOURNER_POTTERY_SHERD);
								output.accept(Items.PLENTY_POTTERY_SHERD);
								output.accept(Items.PRIZE_POTTERY_SHERD);
								output.accept(Items.SCRAPE_POTTERY_SHERD);
								output.accept(Items.SHEAF_POTTERY_SHERD);
								output.accept(Items.SHELTER_POTTERY_SHERD);
								output.accept(Items.SKULL_POTTERY_SHERD);
								output.accept(Items.SNORT_POTTERY_SHERD);
								output.accept(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE);
								output.accept(Items.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE);
								output.accept(Items.VEX_ARMOR_TRIM_SMITHING_TEMPLATE);
								output.accept(Items.WILD_ARMOR_TRIM_SMITHING_TEMPLATE);
								output.accept(Items.COAST_ARMOR_TRIM_SMITHING_TEMPLATE);
								output.accept(Items.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE);
								output.accept(Items.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE);
								output.accept(Items.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE);
								output.accept(Items.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE);
								output.accept(Items.HOST_ARMOR_TRIM_SMITHING_TEMPLATE);
								output.accept(Items.WARD_ARMOR_TRIM_SMITHING_TEMPLATE);
								output.accept(Items.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE);
								output.accept(Items.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE);
								output.accept(Items.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE);
								output.accept(Items.RIB_ARMOR_TRIM_SMITHING_TEMPLATE);
								output.accept(Items.EYE_ARMOR_TRIM_SMITHING_TEMPLATE);
								output.accept(Items.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE);
								output.accept(Items.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE);
								output.accept(Items.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE);
								output.accept(Items.TRIAL_KEY);
								output.accept(Items.OMINOUS_TRIAL_KEY);
								itemDisplayParameters.holders().lookup(Registries.ENCHANTMENT).ifPresent((registryLookup) -> {
									generateEnchantmentBookTypesOnlyMaxLevel(output, registryLookup, CreativeModeTab.TabVisibility.PARENT_TAB_ONLY);
									generateEnchantmentBookTypesAllLevels(output, registryLookup, CreativeModeTab.TabVisibility.SEARCH_TAB_ONLY);
								});
//								SPAWN EGGS
								output.accept(Items.TRIAL_SPAWNER);
								output.accept(Items.CREAKING_HEART);
								output.accept(Items.ALLAY_SPAWN_EGG);
								output.accept(Items.ARMADILLO_SPAWN_EGG);
								output.accept(Items.AXOLOTL_SPAWN_EGG);
								output.accept(Items.BEE_SPAWN_EGG);
								output.accept(Items.BOGGED_SPAWN_EGG);
								output.accept(Items.BREEZE_SPAWN_EGG);
								output.accept(Items.CAMEL_SPAWN_EGG);
								output.accept(Items.CAT_SPAWN_EGG);
								output.accept(Items.COD_SPAWN_EGG);
								output.accept(Items.CREAKING_SPAWN_EGG);
								output.accept(Items.DOLPHIN_SPAWN_EGG);
								output.accept(Items.DROWNED_SPAWN_EGG);
								output.accept(Items.ELDER_GUARDIAN_SPAWN_EGG);
								output.accept(Items.EVOKER_SPAWN_EGG);
								output.accept(Items.FOX_SPAWN_EGG);
								output.accept(Items.FROG_SPAWN_EGG);
								output.accept(Items.GLOW_SQUID_SPAWN_EGG);
								output.accept(Items.GOAT_SPAWN_EGG);
								output.accept(Items.HOGLIN_SPAWN_EGG);
								output.accept(Items.HUSK_SPAWN_EGG);
								output.accept(Items.IRON_GOLEM_SPAWN_EGG);
								output.accept(Items.LLAMA_SPAWN_EGG);
								output.accept(Items.MULE_SPAWN_EGG);
								output.accept(Items.PANDA_SPAWN_EGG);
								output.accept(Items.PARROT_SPAWN_EGG);
								output.accept(Items.PHANTOM_SPAWN_EGG);
								output.accept(Items.PIGLIN_SPAWN_EGG);
								output.accept(Items.PIGLIN_BRUTE_SPAWN_EGG);
								output.accept(Items.PILLAGER_SPAWN_EGG);
								output.accept(Items.POLAR_BEAR_SPAWN_EGG);
								output.accept(Items.PUFFERFISH_SPAWN_EGG);
								output.accept(Items.RAVAGER_SPAWN_EGG);
								output.accept(Items.SALMON_SPAWN_EGG);
								output.accept(Items.SHULKER_SPAWN_EGG);
								output.accept(Items.SKELETON_HORSE_SPAWN_EGG);
								output.accept(Items.SNIFFER_SPAWN_EGG);
								output.accept(Items.SNOW_GOLEM_SPAWN_EGG);
								output.accept(Items.STRAY_SPAWN_EGG);
								output.accept(Items.STRIDER_SPAWN_EGG);
								output.accept(Items.TADPOLE_SPAWN_EGG);
								output.accept(Items.TRADER_LLAMA_SPAWN_EGG);
								output.accept(Items.TROPICAL_FISH_SPAWN_EGG);
								output.accept(Items.TURTLE_SPAWN_EGG);
								output.accept(Items.VEX_SPAWN_EGG);
								output.accept(Items.VINDICATOR_SPAWN_EGG);
								output.accept(Items.WANDERING_TRADER_SPAWN_EGG);
								output.accept(Items.WARDEN_SPAWN_EGG);
								output.accept(Items.ZOGLIN_SPAWN_EGG);
								output.accept(Items.ZOMBIE_HORSE_SPAWN_EGG);
								output.accept(Items.ZOMBIE_VILLAGER_SPAWN_EGG);
//								OP BLOCKS
								output.accept(Items.CHAIN_COMMAND_BLOCK);
								output.accept(Items.REPEATING_COMMAND_BLOCK);
								output.accept(Items.COMMAND_BLOCK_MINECART);
								output.accept(Items.JIGSAW);
								output.accept(Items.STRUCTURE_BLOCK);
								output.accept(Items.STRUCTURE_VOID);
								output.accept(Items.DEBUG_STICK);
								for(int i = 15; i >= 0; --i) {
									output.accept(LightBlock.setLightOnStack(new ItemStack(Items.LIGHT), i));
								}

								itemDisplayParameters.holders().lookup(Registries.PAINTING_VARIANT).ifPresent((registryLookup) -> generatePresetPaintings(output, itemDisplayParameters.holders(), registryLookup, (holder) -> !holder.is(PaintingVariantTags.PLACEABLE), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS));
							}
						})
						.backgroundTexture(SEARCH_BACKGROUND)
						.alignedRight()
						).type(CreativeModeTab.Type.SEARCH)
						.build()
		);
		Registry.register(
				registry,
				FOOD,
				CreativeModeTab.builder(CreativeModeTab.Row.BOTTOM, 0)
						.title(Component.translatable("itemGroup.food"))
						.icon(() -> new ItemStack(Items.APPLE))
						.displayItems((displayContext, content) -> {
							content.accept(Items.APPLE);
							content.accept(Items.MUSHROOM_STEW);
							content.accept(Items.BREAD);
							content.accept(Items.PORKCHOP);
							content.accept(Items.COOKED_PORKCHOP);
							content.accept(Items.GOLDEN_APPLE);
							content.accept(Items.ENCHANTED_GOLDEN_APPLE);
							content.accept(Items.COD);
							content.accept(Items.SALMON);
							content.accept(Items.TROPICAL_FISH);
							content.accept(Items.PUFFERFISH);
							content.accept(Items.COOKED_COD);
							content.accept(Items.COOKED_SALMON);
							content.accept(Items.CAKE);
							content.accept(Items.COOKIE);
							content.accept(Items.MELON_SLICE);
							content.accept(Items.BEEF);
							content.accept(Items.COOKED_BEEF);
							content.accept(Items.CHICKEN);
							content.accept(Items.COOKED_CHICKEN);
							content.accept(Items.ROTTEN_FLESH);
							content.accept(Items.SPIDER_EYE);
							content.accept(Items.CARROT);
							content.accept(Items.POTATO);
							content.accept(Items.BAKED_POTATO);
							content.accept(Items.POISONOUS_POTATO);
							content.accept(Items.PUMPKIN_PIE);
							content.accept(Items.RABBIT);
							content.accept(Items.COOKED_RABBIT);
							content.accept(Items.RABBIT_STEW);
							content.accept(Items.MUTTON);
							content.accept(Items.COOKED_MUTTON);
						})
						.build()
		);
		Registry.register(
				registry,
				TOOLS,
				CreativeModeTab.builder(CreativeModeTab.Row.BOTTOM, 1)
						.title(Component.translatable("itemGroup.tools"))
						.icon(() -> new ItemStack(Items.IRON_AXE))
						.displayItems((displayContext, content) -> {
							content.accept(Items.IRON_SHOVEL);
							content.accept(Items.IRON_PICKAXE);
							content.accept(Items.IRON_AXE);
							content.accept(Items.FLINT_AND_STEEL);
							content.accept(Items.WOODEN_SHOVEL);
							content.accept(Items.WOODEN_PICKAXE);
							content.accept(Items.WOODEN_AXE);
							content.accept(Items.STONE_SHOVEL);
							content.accept(Items.STONE_PICKAXE);
							content.accept(Items.STONE_AXE);
							content.accept(Items.DIAMOND_SHOVEL);
							content.accept(Items.DIAMOND_PICKAXE);
							content.accept(Items.DIAMOND_AXE);
							content.accept(Items.GOLDEN_SHOVEL);
							content.accept(Items.GOLDEN_PICKAXE);
							content.accept(Items.GOLDEN_AXE);
							content.accept(Items.WOODEN_HOE);
							content.accept(Items.STONE_HOE);
							content.accept(Items.IRON_HOE);
							content.accept(Items.DIAMOND_HOE);
							content.accept(Items.GOLDEN_HOE);
							content.accept(Items.COMPASS);
							content.accept(Items.FISHING_ROD);
							content.accept(Items.CLOCK);
							content.accept(Items.SHEARS);
							content.accept(Items.LEAD);
							content.accept(Items.NAME_TAG);
							displayContext.holders().lookup(Registries.ENCHANTMENT).ifPresent(registryLookup -> {
								content.accept(EnchantmentHelper.createBook(new EnchantmentInstance(registryLookup.getOrThrow(Enchantments.EFFICIENCY), 5)));
								content.accept(EnchantmentHelper.createBook(new EnchantmentInstance(registryLookup.getOrThrow(Enchantments.SILK_TOUCH), 1)));
								content.accept(EnchantmentHelper.createBook(new EnchantmentInstance(registryLookup.getOrThrow(Enchantments.UNBREAKING), 3)));
								content.accept(EnchantmentHelper.createBook(new EnchantmentInstance(registryLookup.getOrThrow(Enchantments.FORTUNE), 3)));
								content.accept(EnchantmentHelper.createBook(new EnchantmentInstance(registryLookup.getOrThrow(Enchantments.LUCK_OF_THE_SEA), 3)));
								content.accept(EnchantmentHelper.createBook(new EnchantmentInstance(registryLookup.getOrThrow(Enchantments.LURE), 3)));
							});
						})
						.build()
		);
		Registry.register(
				registry,
				COMBAT,
				CreativeModeTab.builder(CreativeModeTab.Row.BOTTOM, 2)
						.title(Component.translatable("itemGroup.combat"))
						.icon(() -> new ItemStack(Items.GOLDEN_SWORD))
						.displayItems((displayContext, content) -> {
							content.accept(Items.BOW);
							content.accept(Items.ARROW);
							content.accept(Items.IRON_SWORD);
							content.accept(Items.WOODEN_SWORD);
							content.accept(Items.STONE_SWORD);
							content.accept(Items.DIAMOND_SWORD);
							content.accept(Items.GOLDEN_SWORD);
							content.accept(Items.LEATHER_HELMET);
							content.accept(Items.LEATHER_CHESTPLATE);
							content.accept(Items.LEATHER_LEGGINGS);
							content.accept(Items.LEATHER_BOOTS);
							content.accept(Items.CHAINMAIL_HELMET);
							content.accept(Items.CHAINMAIL_CHESTPLATE);
							content.accept(Items.CHAINMAIL_LEGGINGS);
							content.accept(Items.CHAINMAIL_BOOTS);
							content.accept(Items.IRON_HELMET);
							content.accept(Items.IRON_CHESTPLATE);
							content.accept(Items.IRON_LEGGINGS);
							content.accept(Items.IRON_BOOTS);
							content.accept(Items.DIAMOND_HELMET);
							content.accept(Items.DIAMOND_CHESTPLATE);
							content.accept(Items.DIAMOND_LEGGINGS);
							content.accept(Items.DIAMOND_BOOTS);
							content.accept(Items.GOLDEN_HELMET);
							content.accept(Items.GOLDEN_CHESTPLATE);
							content.accept(Items.GOLDEN_LEGGINGS);
							content.accept(Items.GOLDEN_BOOTS);
							displayContext.holders().lookup(Registries.ENCHANTMENT).ifPresent(registryLookup -> {
								content.accept(EnchantmentHelper.createBook(new EnchantmentInstance(registryLookup.getOrThrow(Enchantments.PROTECTION), 4)));
								content.accept(EnchantmentHelper.createBook(new EnchantmentInstance(registryLookup.getOrThrow(Enchantments.FIRE_PROTECTION), 4)));
								content.accept(EnchantmentHelper.createBook(new EnchantmentInstance(registryLookup.getOrThrow(Enchantments.FEATHER_FALLING), 4)));
								content.accept(EnchantmentHelper.createBook(new EnchantmentInstance(registryLookup.getOrThrow(Enchantments.BLAST_PROTECTION), 4)));
								content.accept(EnchantmentHelper.createBook(new EnchantmentInstance(registryLookup.getOrThrow(Enchantments.PROJECTILE_PROTECTION), 4)));
								content.accept(EnchantmentHelper.createBook(new EnchantmentInstance(registryLookup.getOrThrow(Enchantments.RESPIRATION), 3)));
								content.accept(EnchantmentHelper.createBook(new EnchantmentInstance(registryLookup.getOrThrow(Enchantments.AQUA_AFFINITY), 1)));
								content.accept(EnchantmentHelper.createBook(new EnchantmentInstance(registryLookup.getOrThrow(Enchantments.THORNS), 3)));
								content.accept(EnchantmentHelper.createBook(new EnchantmentInstance(registryLookup.getOrThrow(Enchantments.DEPTH_STRIDER), 3)));
								content.accept(EnchantmentHelper.createBook(new EnchantmentInstance(registryLookup.getOrThrow(Enchantments.SHARPNESS), 5)));
								content.accept(EnchantmentHelper.createBook(new EnchantmentInstance(registryLookup.getOrThrow(Enchantments.SMITE), 5)));
								content.accept(EnchantmentHelper.createBook(new EnchantmentInstance(registryLookup.getOrThrow(Enchantments.BANE_OF_ARTHROPODS), 5)));
								content.accept(EnchantmentHelper.createBook(new EnchantmentInstance(registryLookup.getOrThrow(Enchantments.KNOCKBACK), 2)));
								content.accept(EnchantmentHelper.createBook(new EnchantmentInstance(registryLookup.getOrThrow(Enchantments.FIRE_ASPECT), 2)));
								content.accept(EnchantmentHelper.createBook(new EnchantmentInstance(registryLookup.getOrThrow(Enchantments.LOOTING), 3)));
								content.accept(EnchantmentHelper.createBook(new EnchantmentInstance(registryLookup.getOrThrow(Enchantments.POWER), 3)));
								content.accept(EnchantmentHelper.createBook(new EnchantmentInstance(registryLookup.getOrThrow(Enchantments.PUNCH), 2)));
								content.accept(EnchantmentHelper.createBook(new EnchantmentInstance(registryLookup.getOrThrow(Enchantments.FLAME), 1)));
								content.accept(EnchantmentHelper.createBook(new EnchantmentInstance(registryLookup.getOrThrow(Enchantments.INFINITY), 1)));
							});
						})
						.build()
		);
		Registry.register(
				registry,
				BREWING,
				CreativeModeTab.builder(CreativeModeTab.Row.BOTTOM, 3)
						.title(Component.translatable("itemGroup.brewing"))
						.icon(() -> PotionContents.createItemStack(Items.POTION, Potions.WATER))
						.displayItems(
								(displayContext, content) -> {
									content.accept(PotionContents.createItemStack(Items.POTION, Potions.WATER));
									content.accept(PotionContents.createItemStack(Items.POTION, Potions.REGENERATION));
									content.accept(PotionContents.createItemStack(Items.POTION, Potions.STRONG_REGENERATION));
									content.accept(PotionContents.createItemStack(Items.POTION, Potions.LONG_REGENERATION));
									content.accept(PotionContents.createItemStack(Items.SPLASH_POTION, Potions.REGENERATION));
									content.accept(PotionContents.createItemStack(Items.SPLASH_POTION, Potions.STRONG_REGENERATION));
									content.accept(PotionContents.createItemStack(Items.SPLASH_POTION, Potions.LONG_REGENERATION));

									content.accept(PotionContents.createItemStack(Items.POTION, Potions.SWIFTNESS));
									content.accept(PotionContents.createItemStack(Items.POTION, Potions.STRONG_SWIFTNESS));
									content.accept(PotionContents.createItemStack(Items.POTION, Potions.LONG_SWIFTNESS));
									content.accept(PotionContents.createItemStack(Items.SPLASH_POTION, Potions.SWIFTNESS));
									content.accept(PotionContents.createItemStack(Items.SPLASH_POTION, Potions.STRONG_SWIFTNESS));
									content.accept(PotionContents.createItemStack(Items.SPLASH_POTION, Potions.LONG_SWIFTNESS));

									content.accept(PotionContents.createItemStack(Items.POTION, Potions.FIRE_RESISTANCE));
									content.accept(PotionContents.createItemStack(Items.POTION, Potions.LONG_FIRE_RESISTANCE));
									content.accept(PotionContents.createItemStack(Items.SPLASH_POTION, Potions.FIRE_RESISTANCE));
									content.accept(PotionContents.createItemStack(Items.SPLASH_POTION, Potions.LONG_FIRE_RESISTANCE));

									content.accept(PotionContents.createItemStack(Items.POTION, Potions.POISON));
									content.accept(PotionContents.createItemStack(Items.POTION, Potions.STRONG_POISON));
									content.accept(PotionContents.createItemStack(Items.POTION, Potions.LONG_POISON));
									content.accept(PotionContents.createItemStack(Items.SPLASH_POTION, Potions.POISON));
									content.accept(PotionContents.createItemStack(Items.SPLASH_POTION, Potions.STRONG_POISON));
									content.accept(PotionContents.createItemStack(Items.SPLASH_POTION, Potions.LONG_POISON));

									content.accept(PotionContents.createItemStack(Items.POTION, Potions.HEALING));
									content.accept(PotionContents.createItemStack(Items.POTION, Potions.STRONG_HEALING));
									content.accept(PotionContents.createItemStack(Items.SPLASH_POTION, Potions.HEALING));
									content.accept(PotionContents.createItemStack(Items.SPLASH_POTION, Potions.STRONG_HEALING));

									content.accept(PotionContents.createItemStack(Items.POTION, Potions.NIGHT_VISION));
									content.accept(PotionContents.createItemStack(Items.POTION, Potions.LONG_NIGHT_VISION));
									content.accept(PotionContents.createItemStack(Items.SPLASH_POTION, Potions.NIGHT_VISION));
									content.accept(PotionContents.createItemStack(Items.SPLASH_POTION, Potions.LONG_NIGHT_VISION));

									content.accept(PotionContents.createItemStack(Items.POTION, Potions.WEAKNESS));
									content.accept(PotionContents.createItemStack(Items.POTION, Potions.LONG_WEAKNESS));
									content.accept(PotionContents.createItemStack(Items.SPLASH_POTION, Potions.WEAKNESS));
									content.accept(PotionContents.createItemStack(Items.SPLASH_POTION, Potions.LONG_WEAKNESS));

									content.accept(PotionContents.createItemStack(Items.POTION, Potions.STRENGTH));
									content.accept(PotionContents.createItemStack(Items.POTION, Potions.STRONG_STRENGTH));
									content.accept(PotionContents.createItemStack(Items.POTION, Potions.LONG_STRENGTH));
									content.accept(PotionContents.createItemStack(Items.SPLASH_POTION, Potions.STRENGTH));
									content.accept(PotionContents.createItemStack(Items.SPLASH_POTION, Potions.STRONG_STRENGTH));
									content.accept(PotionContents.createItemStack(Items.SPLASH_POTION, Potions.LONG_STRENGTH));

									content.accept(PotionContents.createItemStack(Items.POTION, Potions.SLOWNESS));
									content.accept(PotionContents.createItemStack(Items.POTION, Potions.LONG_SLOWNESS));
									content.accept(PotionContents.createItemStack(Items.SPLASH_POTION, Potions.SLOWNESS));
									content.accept(PotionContents.createItemStack(Items.SPLASH_POTION, Potions.LONG_SLOWNESS));

									content.accept(PotionContents.createItemStack(Items.POTION, Potions.LEAPING));
									content.accept(PotionContents.createItemStack(Items.POTION, Potions.STRONG_LEAPING));
									content.accept(PotionContents.createItemStack(Items.POTION, Potions.LONG_LEAPING));
									content.accept(PotionContents.createItemStack(Items.SPLASH_POTION, Potions.LEAPING));
									content.accept(PotionContents.createItemStack(Items.SPLASH_POTION, Potions.STRONG_LEAPING));
									content.accept(PotionContents.createItemStack(Items.SPLASH_POTION, Potions.LONG_LEAPING));

									content.accept(PotionContents.createItemStack(Items.POTION, Potions.HARMING));
									content.accept(PotionContents.createItemStack(Items.POTION, Potions.STRONG_HARMING));
									content.accept(PotionContents.createItemStack(Items.SPLASH_POTION, Potions.HARMING));
									content.accept(PotionContents.createItemStack(Items.SPLASH_POTION, Potions.STRONG_HARMING));

									content.accept(PotionContents.createItemStack(Items.POTION, Potions.WATER_BREATHING));
									content.accept(PotionContents.createItemStack(Items.POTION, Potions.LONG_WATER_BREATHING));
									content.accept(PotionContents.createItemStack(Items.SPLASH_POTION, Potions.WATER_BREATHING));
									content.accept(PotionContents.createItemStack(Items.SPLASH_POTION, Potions.LONG_WATER_BREATHING));

									content.accept(PotionContents.createItemStack(Items.POTION, Potions.INVISIBILITY));
									content.accept(PotionContents.createItemStack(Items.POTION, Potions.LONG_INVISIBILITY));
									content.accept(PotionContents.createItemStack(Items.SPLASH_POTION, Potions.INVISIBILITY));
									content.accept(PotionContents.createItemStack(Items.SPLASH_POTION, Potions.LONG_INVISIBILITY));

									content.accept(Items.GLASS_BOTTLE);
									content.accept(Items.FERMENTED_SPIDER_EYE);
									content.accept(Items.BLAZE_POWDER);
									content.accept(Items.MAGMA_CREAM);
									content.accept(Items.BREWING_STAND);
									content.accept(Items.CAULDRON);
									content.accept(Items.GLISTERING_MELON_SLICE);
									content.accept(Items.GOLDEN_CARROT);
									content.accept(Items.RABBIT_FOOT);
								}
						)
						.build()
		);
		Registry.register(
				registry,
				MATERIALS,
				CreativeModeTab.builder(CreativeModeTab.Row.BOTTOM, 4)
						.title(Component.translatable("itemGroup.materials"))
						.icon(() -> new ItemStack(Items.STICK))
						.displayItems((displayContext, content) -> {
							content.accept(Items.COAL);
							content.accept(Items.CHARCOAL);
							content.accept(Items.DIAMOND);
							content.accept(Items.IRON_INGOT);
							content.accept(Items.GOLD_INGOT);
							content.accept(Items.STICK);
							content.accept(Items.BOWL);
							content.accept(Items.STRING);
							content.accept(Items.FEATHER);
							content.accept(Items.GUNPOWDER);
							content.accept(Items.WHEAT_SEEDS);
							content.accept(Items.WHEAT);
							content.accept(Items.FLINT);
							content.accept(Items.LEATHER);
							content.accept(Items.BRICK);
							content.accept(Items.CLAY_BALL);
							content.accept(Items.SUGAR_CANE);
							content.accept(Items.EGG);
							content.accept(Items.GLOWSTONE_DUST);
							content.accept(Items.INK_SAC);
							content.accept(Items.RED_DYE);
							content.accept(Items.GREEN_DYE);
							content.accept(Items.COCOA_BEANS);
							content.accept(Items.LAPIS_LAZULI);
							content.accept(Items.PURPLE_DYE);
							content.accept(Items.CYAN_DYE);
							content.accept(Items.LIGHT_GRAY_DYE);
							content.accept(Items.GRAY_DYE);
							content.accept(Items.PINK_DYE);
							content.accept(Items.LIME_DYE);
							content.accept(Items.YELLOW_DYE);
							content.accept(Items.LIGHT_BLUE_DYE);
							content.accept(Items.MAGENTA_DYE);
							content.accept(Items.ORANGE_DYE);
							content.accept(Items.BONE_MEAL);
							content.accept(Items.SUGAR);
							content.accept(Items.PUMPKIN_SEEDS);
							content.accept(Items.MELON_SEEDS);
							content.accept(Items.BLAZE_ROD);
							content.accept(Items.GOLD_NUGGET);
							content.accept(Items.NETHER_WART);
							content.accept(Items.EMERALD);
							content.accept(Items.NETHER_STAR);
							content.accept(Items.NETHER_BRICK);
							content.accept(Items.QUARTZ);
							content.accept(Items.PRISMARINE_SHARD);
							content.accept(Items.PRISMARINE_CRYSTALS);
							content.accept(Items.RABBIT_HIDE);
						})
						.build()
		);

		return Registry.register(
				registry,
				INVENTORY,
				((CreativeModeTabBuilderMixinInterface)CreativeModeTab.builder(CreativeModeTab.Row.BOTTOM, 6)
						.title(Component.translatable("itemGroup.inventory"))
						.icon(() -> new ItemStack(Blocks.CHEST))
						.backgroundTexture(INVENTORY_BACKGROUND)
						.hideTitle()
						.alignedRight()
						).type(CreativeModeTab.Type.INVENTORY)
						.noScrollBar()
						.build()
		);
	}
}

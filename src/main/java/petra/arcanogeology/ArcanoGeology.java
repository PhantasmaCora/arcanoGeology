package petra.arcanogeology;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricMaterialBuilder;
import net.fabricmc.fabric.api.structure.v1.FabricStructureBuilder;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.SlabBlock;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DataPool;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.math.intprovider.WeightedListIntProvider;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.Heightmap;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.decorator.CountPlacementModifier;
import net.minecraft.world.gen.decorator.HeightRangePlacementModifier;
import net.minecraft.world.gen.decorator.HeightmapPlacementModifier;
import net.minecraft.world.gen.decorator.RandomOffsetPlacementModifier;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreConfiguredFeatures;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.SingleStateFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;

public class ArcanoGeology implements ModInitializer {
	
	// ############# BLOCKS ##############
	
	// Gas Pockets
    static final FabricBlockSettings STONELIKE = FabricBlockSettings.of(Material.STONE).strength(1.5f, 6.0f).requiresTool();
    
    public static final BreakEffectBlock TOXIC_POCKET = new BreakEffectBlock(STONELIKE, (net.minecraft.world.WorldAccess world, net.minecraft.util.math.BlockPos pos, net.minecraft.block.BlockState state) -> {
    	AreaEffectCloudEntity cloud = new AreaEffectCloudEntity(world.getChunkManager().getWorldChunk(pos.getX() / 16, pos.getZ() / 16).getWorld(), pos.getX(), pos.getY(), pos.getZ());
    	cloud.setRadius(3f);
    	cloud.setPotion(net.minecraft.potion.Potions.POISON);
    	world.spawnEntity(cloud);
    	return;
    });
    
    public static final BreakEffectBlock EXPLOSIVE_POCKET = new BreakEffectBlock(STONELIKE, (net.minecraft.world.WorldAccess world, net.minecraft.util.math.BlockPos pos, net.minecraft.block.BlockState state) -> {
    	world.getChunkManager().getWorldChunk(pos.getX() / 16, pos.getZ() / 16).getWorld().createExplosion(null, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), 2.5f, Explosion.DestructionType.valueOf("NONE"));
    	return;
    });

	// Dyracsites
	static final FabricBlockSettings DYRACSITE = FabricBlockSettings.of(Material.STONE).strength(1.7f, 8.0f).requiresTool();
	
    public static final Block RAW_DYRACSITE = new AdjacentReplaceBlock(DYRACSITE, Blocks.STONE, EXPLOSIVE_POCKET, 0.5f);
    public static final Block COBBLED_DYRACSITE = new AdjacentReplaceBlock(DYRACSITE, Blocks.STONE, EXPLOSIVE_POCKET, 0.5f);
    public static final Block POLISHED_DYRACSITE = new Block(DYRACSITE);
    public static final Block CUT_DYRACSITE = new Block(DYRACSITE);
    public static final Block COBBLED_DYRACSITE_SLAB = new SlabBlock(DYRACSITE);
    public static final Block COBBLED_DYRACSITE_STAIR = new StairBlock(COBBLED_DYRACSITE.getDefaultState(), DYRACSITE);
    public static final Block CUT_DYRACSITE_SLAB = new SlabBlock(DYRACSITE);
    public static final Block CUT_DYRACSITE_STAIR = new StairBlock(POLISHED_DYRACSITE.getDefaultState(), DYRACSITE);
    public static final Block BRICK_DYRACSITE = new Block(DYRACSITE);
    public static final Block CHISELED_DYRACSITE = new Block(DYRACSITE);

    // Skiratites
    static final FabricBlockSettings SKIRATITE = FabricBlockSettings.of(Material.METAL).strength(2.0f, 10.0f).requiresTool();
 	
    public static final Block RAW_SKIRATITE = new AdjacentReplaceBlock(SKIRATITE, Blocks.STONE, TOXIC_POCKET, 0.5f);
    public static final Block COBBLED_SKIRATITE = new AdjacentReplaceBlock(SKIRATITE, Blocks.STONE, TOXIC_POCKET, 0.5f);
    public static final Block POLISHED_SKIRATITE = new Block(SKIRATITE);
    public static final Block CUT_SKIRATITE = new Block(SKIRATITE);
    public static final Block COBBLED_SKIRATITE_SLAB = new SlabBlock(SKIRATITE);
    public static final Block COBBLED_SKIRATITE_STAIR = new StairBlock(COBBLED_SKIRATITE.getDefaultState(), SKIRATITE);
    public static final Block CUT_SKIRATITE_SLAB = new SlabBlock(SKIRATITE);
    public static final Block CUT_SKIRATITE_STAIR = new StairBlock(POLISHED_SKIRATITE.getDefaultState(), SKIRATITE);
    public static final Block BRICK_SKIRATITE = new Block(SKIRATITE);
    public static final Block CHISELED_SKIRATITE = new Block(SKIRATITE);
    
    // Asranites
    static final FabricBlockSettings ASRANITE = FabricBlockSettings.of(new FabricMaterialBuilder(MapColor.TERRACOTTA_CYAN).build()).strength(3.0f, 12.0f).requiresTool();
 	
    public static final Block RAW_ASRANITE = new Block(ASRANITE);
    public static final Block COBBLED_ASRANITE = new Block(ASRANITE);
    public static final Block POLISHED_ASRANITE = new Block(ASRANITE);
    public static final Block CUT_ASRANITE = new Block(ASRANITE);
    public static final Block COBBLED_ASRANITE_SLAB = new SlabBlock(ASRANITE);
    public static final Block COBBLED_ASRANITE_STAIR = new StairBlock(COBBLED_ASRANITE.getDefaultState(), ASRANITE);
    public static final Block CUT_ASRANITE_SLAB = new SlabBlock(ASRANITE);
    public static final Block CUT_ASRANITE_STAIR = new StairBlock(POLISHED_ASRANITE.getDefaultState(), ASRANITE);
    public static final Block BRICK_ASRANITE = new Block(ASRANITE);
    public static final Block CHISELED_ASRANITE = new Block(ASRANITE);
     
    // Vortachor
    static final FabricBlockSettings VORTACHOR_SETTINGS = FabricBlockSettings.of(new FabricMaterialBuilder(MapColor.BLACK).blocksPistons().build()).strength(3.0f, 120.0f).requiresTool();
     
    public static final Block VORTACHOR = new Block(VORTACHOR_SETTINGS);
    
    // Relinata
    static final FabricBlockSettings RELINATA_SETTINGS = FabricBlockSettings.of(new FabricMaterialBuilder(MapColor.PINK).build()).requiresTool().strength(1.0f, 6.0f);
     
    public static final Block RELINATA = new Block(RELINATA_SETTINGS);
    
    // Diresten
    static final FabricBlockSettings DIRESTEN_SETTINGS = FabricBlockSettings.of(new FabricMaterialBuilder(MapColor.TERRACOTTA_GREEN).build()).requiresTool().jumpVelocityMultiplier(0.4f).strength(2.5f, 16f);
     
    public static final Block DIRESTEN = new Block(DIRESTEN_SETTINGS);
    
    // Fusestones
    static final FabricBlockSettings FUSESTONE_SETTINGS = FabricBlockSettings.of(new FabricMaterialBuilder(MapColor.TERRACOTTA_RED).build()).requiresTool().strength(1.0f, 5.5f);
    
    public static final Block FUSESTONE = new Block(FUSESTONE_SETTINGS);
    public static final Block EN_FUSESTONE = new Block(FUSESTONE_SETTINGS);

    static HashMap<String, Block> basic_blocks = new HashMap<String, Block>();
    
    // ########### FEATURES ##########
    
    // Vortachor Houses
    public static final StructureFeature<DefaultFeatureConfig> V_HOUSE = new VHFeature(DefaultFeatureConfig.CODEC);
    public static final StructurePieceType V_HOUSE_PIECE = VHGenerator.Piece::new;
    
    public static final ConfiguredStructureFeature<?,?> V_HOUSE_CONFIGURED = V_HOUSE.configure(DefaultFeatureConfig.DEFAULT);
    
    // Spooky Altar
    public static final StructureFeature<DefaultFeatureConfig> S_ALTAR = new SAFeature(DefaultFeatureConfig.CODEC);
    public static final StructurePieceType S_ALTAR_PIECE = SAGenerator.Piece::new;
    
    public static final ConfiguredStructureFeature<?,?> S_ALTAR_CONFIGURED = S_ALTAR.configure(DefaultFeatureConfig.DEFAULT);
    
    // Multiveins
    private static final Feature<MultiOreFeatureConfig> MULTI_VEIN = new MultiOreFeature(MultiOreFeatureConfig.CODEC);
    
    public static final ConfiguredFeature<?, ?> DYRACS_VEIN = MULTI_VEIN.configure(
    	new MultiOreFeatureConfig(
    		UniformIntProvider.create(1, 4), 
    		SimpleBlockStateProvider.of(RAW_DYRACSITE), 
    		SimpleBlockStateProvider.of(EXPLOSIVE_POCKET),
    		net.minecraft.world.gen.feature.OreConfiguredFeatures.STONE_ORE_REPLACEABLES
    	)
    );
    public static final PlacedFeature DYRACS_VEIN_PLACED = DYRACS_VEIN.withPlacement(
    	RandomOffsetPlacementModifier.of(UniformIntProvider.create(-8, 7), ConstantIntProvider.create(0)),
        HeightRangePlacementModifier.trapezoid(YOffset.fixed(16), YOffset.fixed(56)),
        CountPlacementModifier.of(5)
    );
    
    public static final ConfiguredFeature<?, ?> SKIRAT_VEIN = MULTI_VEIN.configure(
        new MultiOreFeatureConfig(
        	UniformIntProvider.create(1, 4),
        	SimpleBlockStateProvider.of(RAW_SKIRATITE), 
        	SimpleBlockStateProvider.of(TOXIC_POCKET),
        	net.minecraft.world.gen.feature.OreConfiguredFeatures.STONE_ORE_REPLACEABLES
        )
    );
    public static final PlacedFeature SKIRAT_VEIN_PLACED = SKIRAT_VEIN.withPlacement(
    	RandomOffsetPlacementModifier.of(UniformIntProvider.create(-8, 7), ConstantIntProvider.create(0)),
        HeightRangePlacementModifier.trapezoid(YOffset.fixed(0), YOffset.fixed(42)),
        CountPlacementModifier.of(5)
    );
    
    public static final ConfiguredFeature<?, ?> ASRAN_VEIN = Feature.ORE.configure(
    	new OreFeatureConfig(
    	    OreConfiguredFeatures.DEEPSLATE_ORE_REPLACEABLES,
    	    RAW_ASRANITE.getDefaultState(),
    	    14 // vein size
    	)
    );
    public static final PlacedFeature ASRAN_VEIN_PLACED = ASRAN_VEIN.withPlacement(
    	RandomOffsetPlacementModifier.of(UniformIntProvider.create(-8, 7), ConstantIntProvider.create(0)),
        HeightRangePlacementModifier.trapezoid(YOffset.fixed(-63), YOffset.fixed(5)),
        CountPlacementModifier.of(4)
    );
    
    // Single block rocks
    private static final Feature<SingleStateFeatureConfig> SINGLE_ROCK = new SingleBlockSurfaceFeature(SingleStateFeatureConfig.CODEC);
    
    public static final ConfiguredFeature<?, ?> FUSESTONE_ROCK = SINGLE_ROCK.configure(
        new SingleStateFeatureConfig(FUSESTONE.getDefaultState())
    );
    public static final PlacedFeature FUSESTONE_ROCK_PLACED = FUSESTONE_ROCK.withPlacement(
    	CountPlacementModifier.of(new WeightedListIntProvider( DataPool.<IntProvider>builder().add(ConstantIntProvider.create(0), 90).add(ConstantIntProvider.create(1), 9).add(ConstantIntProvider.create(2), 1).build() )),
        RandomOffsetPlacementModifier.of(UniformIntProvider.create(-8, 7), ConstantIntProvider.create(0)),
    	HeightmapPlacementModifier.of(Heightmap.Type.OCEAN_FLOOR_WG)
    );
    
    
    // ########### INIT ##############
    @Override
    public void onInitialize() {
    	// Input Dyracsites for registration
    	basic_blocks.put("raw_dyracsite", RAW_DYRACSITE);
    	basic_blocks.put("cobbled_dyracsite", COBBLED_DYRACSITE);
    	basic_blocks.put("polished_dyracsite", POLISHED_DYRACSITE);
    	basic_blocks.put("cut_dyracsite", CUT_DYRACSITE);
    	basic_blocks.put("cobbled_dyracsite_slab", COBBLED_DYRACSITE_SLAB);
    	basic_blocks.put("cobbled_dyracsite_stairs", COBBLED_DYRACSITE_STAIR);
    	basic_blocks.put("cut_dyracsite_slab", CUT_DYRACSITE_SLAB);
    	basic_blocks.put("cut_dyracsite_stairs", CUT_DYRACSITE_STAIR);
    	basic_blocks.put("brick_dyracsite", BRICK_DYRACSITE);
    	basic_blocks.put("chiseled_dyracsite", CHISELED_DYRACSITE);
    	
    	// Input Skiratites for registration
    	basic_blocks.put("raw_skiratite", RAW_SKIRATITE);
    	basic_blocks.put("cobbled_skiratite", COBBLED_SKIRATITE);
    	basic_blocks.put("polished_skiratite", POLISHED_SKIRATITE);
    	basic_blocks.put("cut_skiratite", CUT_SKIRATITE);
    	basic_blocks.put("cobbled_skiratite_slab", COBBLED_SKIRATITE_SLAB);
    	basic_blocks.put("cobbled_skiratite_stairs", COBBLED_SKIRATITE_STAIR);
    	basic_blocks.put("cut_skiratite_slab", CUT_SKIRATITE_SLAB);
    	basic_blocks.put("cut_skiratite_stairs", CUT_SKIRATITE_STAIR);
    	basic_blocks.put("brick_skiratite", BRICK_SKIRATITE);
    	basic_blocks.put("chiseled_skiratite", CHISELED_SKIRATITE);
    	
    	// Input Asranites for registration
    	basic_blocks.put("raw_asranite", RAW_ASRANITE);
    	basic_blocks.put("cobbled_asranite", COBBLED_ASRANITE);
    	basic_blocks.put("polished_asranite", POLISHED_ASRANITE);
    	basic_blocks.put("cut_asranite", CUT_ASRANITE);
    	basic_blocks.put("cobbled_asranite_slab", COBBLED_ASRANITE_SLAB);
    	basic_blocks.put("cobbled_asranite_stairs", COBBLED_ASRANITE_STAIR);
    	basic_blocks.put("cut_asranite_slab", CUT_ASRANITE_SLAB);
    	basic_blocks.put("cut_asranite_stairs", CUT_ASRANITE_STAIR);
    	basic_blocks.put("brick_asranite", BRICK_ASRANITE);
    	basic_blocks.put("chiseled_asranite", CHISELED_ASRANITE);
    	
    	// Input trio for registration
    	basic_blocks.put("relinata", RELINATA);
    	basic_blocks.put("diresten", DIRESTEN);
    	basic_blocks.put("vortachor", VORTACHOR);
    	
    	// Input Fusestones for registration
    	basic_blocks.put("fusestone", FUSESTONE);
    	basic_blocks.put("energized_fusestone", EN_FUSESTONE);
    	
    	// Actually do the registration
    	Set<Entry<String, Block>> es = basic_blocks.entrySet();
    	Iterator<Entry<String, Block>> iter = es.iterator();
    	while (iter.hasNext()) {
    		Entry<String, Block> e = iter.next();
    		Registry.register(Registry.BLOCK, new Identifier("arcanogeology", e.getKey()), e.getValue());
    		Registry.register(Registry.ITEM, new Identifier("arcanogeology", e.getKey()), new BlockItem(e.getValue(), new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS)));
    	}
    	
    	// Register others
    	Registry.register(Registry.BLOCK, new Identifier("arcanogeology", "toxic_pocket"), TOXIC_POCKET);
		Registry.register(Registry.ITEM, new Identifier("arcanogeology", "toxic_pocket"), new BlockItem(TOXIC_POCKET, new FabricItemSettings().group(ItemGroup.MISC)));
		
		Registry.register(Registry.BLOCK, new Identifier("arcanogeology", "explosive_pocket"), EXPLOSIVE_POCKET);
		Registry.register(Registry.ITEM, new Identifier("arcanogeology", "explosive_pocket"), new BlockItem(EXPLOSIVE_POCKET, new FabricItemSettings().group(ItemGroup.MISC)));
        
		// Register features
		Registry.register(Registry.STRUCTURE_PIECE, new Identifier("arcanogeology", "v_house_piece"), V_HOUSE_PIECE);
	    FabricStructureBuilder.create(new Identifier("arcanogeology", "v_house"), V_HOUSE)
	      .step(GenerationStep.Feature.UNDERGROUND_STRUCTURES)
	      .defaultConfig(24, 4, 95702)
	      .register();
	    
	    Registry.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, new Identifier("arcanogeology", "v_house"), V_HOUSE_CONFIGURED);
	    
	    Registry.register(Registry.STRUCTURE_PIECE, new Identifier("arcanogeology", "a_altar_piece"), S_ALTAR_PIECE);
	    FabricStructureBuilder.create(new Identifier("arcanogeology", "s_altar"), S_ALTAR)
	      .step(GenerationStep.Feature.UNDERGROUND_STRUCTURES)
	      .defaultConfig(32, 8, 111)
	      .register();
	    
	    Registry.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, new Identifier("arcanogeology", "s_altar"), S_ALTAR_CONFIGURED);
	
		Registry.register(Registry.FEATURE, "arcanogeology_multiorevein", MULTI_VEIN);
		Registry.register(Registry.FEATURE, "arcanogeology_singleblocksurface", SINGLE_ROCK);
		
		Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier("arcanogeology", "dyracs_vein"), DYRACS_VEIN);
		Registry.register(BuiltinRegistries.PLACED_FEATURE, new Identifier("arcanogeology", "dyracs_vein"), DYRACS_VEIN_PLACED);

		Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier("arcanogeology", "skirat_vein"), SKIRAT_VEIN);
		Registry.register(BuiltinRegistries.PLACED_FEATURE, new Identifier("arcanogeology", "skirat_vein"), SKIRAT_VEIN_PLACED);
		
		Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier("arcanogeology", "asran_vein"), ASRAN_VEIN);
		Registry.register(BuiltinRegistries.PLACED_FEATURE, new Identifier("arcanogeology", "asran_vein"), ASRAN_VEIN_PLACED);
		
		Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier("arcanogeology", "fusestone_rock"), FUSESTONE_ROCK);
		Registry.register(BuiltinRegistries.PLACED_FEATURE, new Identifier("arcanogeology", "fusestone_rock"), FUSESTONE_ROCK_PLACED);
		
		// Modify biomes
		RegistryKey<PlacedFeature> dyracsVein = RegistryKey.of(Registry.PLACED_FEATURE_KEY,
		    new Identifier("arcanogeology", "dyracs_vein"));
		BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, dyracsVein);
		
		RegistryKey<PlacedFeature> skiratVein = RegistryKey.of(Registry.PLACED_FEATURE_KEY,
		    new Identifier("arcanogeology", "skirat_vein"));
		BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, skiratVein);
		
		RegistryKey<PlacedFeature> asranVein = RegistryKey.of(Registry.PLACED_FEATURE_KEY,
		    new Identifier("arcanogeology", "asran_vein"));
		BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, asranVein);
		
		RegistryKey<PlacedFeature> fusestone_rock = RegistryKey.of(Registry.PLACED_FEATURE_KEY,
			    new Identifier("arcanogeology", "fusestone_rock"));
			BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, fusestone_rock);
		
		RegistryKey<ConfiguredStructureFeature<?,?>> vhouseKey = RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY,
			new Identifier("arcanogeology", "v_house"));
		BiomeModifications.addStructure(BiomeSelectors.foundInOverworld(), vhouseKey);
		
		RegistryKey<ConfiguredStructureFeature<?,?>> saltarKey = RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY,
			new Identifier("arcanogeology", "s_altar"));
		BiomeModifications.addStructure(BiomeSelectors.foundInOverworld(), saltarKey);
    }
}


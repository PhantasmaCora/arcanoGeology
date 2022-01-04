package petra.arcanogeology;

import com.mojang.serialization.Codec;


import net.minecraft.structure.StructureGeneratorFactory;
import net.minecraft.structure.StructurePiecesCollector;
import net.minecraft.structure.StructurePiecesGenerator;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

public class SAFeature extends StructureFeature<DefaultFeatureConfig> {

	public SAFeature(Codec<DefaultFeatureConfig> configCodec) {
		super(configCodec, StructureGeneratorFactory.simple(SAFeature::canGenerate, SAFeature::addPieces));
	}

	private static boolean canGenerate(StructureGeneratorFactory.Context<DefaultFeatureConfig> context) {
        return true;
    }

	private static void addPieces(StructurePiecesCollector collector, StructurePiecesGenerator.Context<DefaultFeatureConfig> context) {
        BlockRotation blockRotation = BlockRotation.random(context.random());
        BlockPos blockPos = new BlockPos(context.chunkPos().getStartX(), 90, context.chunkPos().getStartZ());
        SAGenerator.addParts(context.structureManager(), blockPos, blockRotation, collector, context.random(), context.config());
    }
	
}

package petra.arcanogeology;

import java.util.Random;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.structure.SimpleStructurePiece;
import net.minecraft.structure.StructureContext;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.structure.StructurePiecesHolder;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.BlockIgnoreStructureProcessor;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;

public class SAGenerator {
	private static final Identifier spooks_altar = new Identifier("arcanogeology", "spooks_altar");
	
	public static void addParts(StructureManager structureManager, BlockPos pos, BlockRotation rotation, StructurePiecesHolder holder, Random random, DefaultFeatureConfig config) {
        holder.addPiece(new Piece(structureManager, spooks_altar, pos, rotation));
    }
	
	public static class Piece
    extends SimpleStructurePiece implements StructurePieceType {
		
        public Piece(StructureManager manager, Identifier identifier, BlockPos pos, BlockRotation rotation) {
            super(ArcanoGeology.V_HOUSE_PIECE, 0, manager, identifier, identifier.toString(), Piece.createPlacementData(rotation), pos);
        }

        public Piece(StructureManager manager, NbtCompound nbt) {
            super(ArcanoGeology.V_HOUSE_PIECE, nbt, manager, identifier -> Piece.createPlacementData(BlockRotation.valueOf(nbt.getString("Rot"))));
        }
        
        public Piece(StructureContext context, NbtCompound nbt) {
        	this(context.structureManager(), nbt);
        }

        @Override
        protected void writeNbt(StructureContext context, NbtCompound nbt) {
            super.writeNbt(context, nbt);
            nbt.putString("Rot", this.placementData.getRotation().name());
        }

        private static StructurePlacementData createPlacementData(BlockRotation rotation) {
            return new StructurePlacementData().setRotation(rotation).setMirror(BlockMirror.NONE).addProcessor(BlockIgnoreStructureProcessor.IGNORE_AIR_AND_STRUCTURE_BLOCKS);
        }

        @Override
		protected void handleMetadata(String var1, BlockPos var2, ServerWorldAccess var3, Random var4, BlockBox var5) {
	
		}
        
        @Override
        public void generate(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox chunkBox, ChunkPos chunkPos, BlockPos pos) {
        	BlockPos.Mutable mu = new BlockPos.Mutable(pos.getX(), -60, pos.getZ());
        	
        	while ( mu.getY() < 57 && !world.isAir(mu) ) {
        		mu.move(0, 1, 0);
        	}
        	mu.move(0,-1,0);
        	
        	if (mu.getY() < 56) {
        		this.boundingBox = new BlockBox(this.boundingBox.getMinX(), mu.getY(), this.boundingBox.getMinZ(), this.boundingBox.getMaxX(), mu.getY() + this.boundingBox.getMaxY() - this.boundingBox.getMinY(), this.boundingBox.getMaxZ());
        		this.pos = mu;
        		super.generate(world, structureAccessor, chunkGenerator, random, chunkBox, chunkPos, mu);
        	}
        }

		@Override
		public StructurePiece load(StructureContext var1, NbtCompound var2) {
			return this;
		}

    }
	
}

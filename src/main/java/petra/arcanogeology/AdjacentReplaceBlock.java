package petra.arcanogeology;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AdjacentReplaceBlock extends Block {
	Block fromBlock;
	Block toBlock;
	float chance;
	
	public AdjacentReplaceBlock(Settings settings, Block from, Block to, float chance) {
		super(settings);
		this.fromBlock = from;
		this.toBlock = to;
		this.chance = chance;
	}
	
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
		Block fBlock = world.getBlockState(fromPos).getBlock();
		
		if (fBlock.equals(fromBlock) && world.getRandom().nextFloat() <= chance) {
			world.setBlockState(fromPos, toBlock.getDefaultState());
		}
	}
	
}


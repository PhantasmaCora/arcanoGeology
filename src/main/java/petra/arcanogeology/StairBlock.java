package petra.arcanogeology;

import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;

public class StairBlock extends StairsBlock {

	public StairBlock(BlockState baseBlockState, Settings settings) {
		super(baseBlockState, settings);
		// necessary because original is protected for some reason
	}

}

package petra.arcanogeology;

import net.minecraft.block.Block;

public class BreakEffectBlock extends Block {
	
	public BlockBreakResult effect;
	
	BreakEffectBlock(net.minecraft.block.AbstractBlock.Settings settings, BlockBreakResult ef) {
		super(settings);
		effect = ef;
	}
	
	public void onBroken(net.minecraft.world.WorldAccess world, net.minecraft.util.math.BlockPos pos, net.minecraft.block.BlockState state) {
		super.onBroken(world, pos, state);
		effect.onBreak(world, pos, state);
	}
	
}


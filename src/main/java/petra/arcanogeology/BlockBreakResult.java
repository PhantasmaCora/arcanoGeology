package petra.arcanogeology;

public interface BlockBreakResult {
	public void onBreak(net.minecraft.world.WorldAccess world, net.minecraft.util.math.BlockPos pos, net.minecraft.block.BlockState state);
}


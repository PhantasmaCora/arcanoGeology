package petra.arcanogeology;

import com.mojang.serialization.Codec;

import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.SingleStateFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class SingleBlockSurfaceFeature extends Feature<SingleStateFeatureConfig> {

	public SingleBlockSurfaceFeature(Codec<SingleStateFeatureConfig> configCodec) {
		super(configCodec);
	}

	@Override
	public boolean generate(FeatureContext<SingleStateFeatureConfig> context) {
		context.getWorld().setBlockState(context.getOrigin(), context.getConfig().state, 0);
		return true;
	}

}

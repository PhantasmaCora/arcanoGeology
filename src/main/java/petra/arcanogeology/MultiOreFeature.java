package petra.arcanogeology;

import com.mojang.serialization.Codec;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;


public class MultiOreFeature extends Feature<MultiOreFeatureConfig> {
	public MultiOreFeature(Codec<MultiOreFeatureConfig> configCodec) {
	    super(configCodec);
	  }
	 
	  @Override
	  public boolean generate(FeatureContext<MultiOreFeatureConfig> context) {
	    BlockPos pos = context.getOrigin();
	    MultiOreFeatureConfig config = context.getConfig();
	 
	    int radius = config.radius().get(context.getRandom());
	    
	    for (int y = -radius; y <= radius; y++) {
	    	for (int x = -radius; x <= radius; x++) {
	    		for (int z = -radius; z <= radius; z++) {
	    			double activeRadius = Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2);
	    			if ( activeRadius <= Math.pow(radius, 2)) {
	    				BlockPos activePos = pos.add(new Vec3i(x, y, z));
	    				BlockState here = context.getWorld().getBlockState(activePos);
	    				
	    				if (config.toReplace().test(here, context.getRandom())) {
	    					double bias = 1 - activeRadius / radius;
	    					double choice = context.getRandom().nextDouble();
	    					
	    					if (bias + choice > 0.5) {
	    						context.getWorld().setBlockState(activePos, config.blockCore().getBlockState(context.getRandom(), activePos), 0);
	    					} else if (bias + choice > 0) {
	    						context.getWorld().setBlockState(activePos, config.blockPeri().getBlockState(context.getRandom(), activePos), 0);
	    					}
	    				}
	    			}
	    		}
	    	}
	    }
	 
	    return true;
	  }
}


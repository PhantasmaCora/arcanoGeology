package petra.arcanogeology;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.structure.rule.RuleTest;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

public record MultiOreFeatureConfig(IntProvider radius, BlockStateProvider blockCore, BlockStateProvider blockPeri, RuleTest toReplace) implements FeatureConfig {
	  public static final Codec<MultiOreFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
	    IntProvider.VALUE_CODEC.fieldOf("radius").forGetter(MultiOreFeatureConfig::radius),
	    BlockStateProvider.TYPE_CODEC.fieldOf("blockCore").forGetter(MultiOreFeatureConfig::blockCore),
	    BlockStateProvider.TYPE_CODEC.fieldOf("blockPeri").forGetter(MultiOreFeatureConfig::blockPeri),
	    RuleTest.TYPE_CODEC.fieldOf("toReplace").forGetter(MultiOreFeatureConfig::toReplace)
	  ).apply(instance, instance.stable(MultiOreFeatureConfig::new)));
}


package ModName.Mixins;

import com.gtnewhorizon.gtnhmixins.builders.IMixins;
import com.gtnewhorizon.gtnhmixins.builders.MixinBuilder;

import javax.annotation.Nonnull;

public enum Mixins implements IMixins {
    MINECRAFT_EARLY(new MixinBuilder("Minecraft Early")
        .addCommonMixins(
            "MixinInventoryPlayer")
        .setPhase(Phase.EARLY)),
    MINECRAFT_LATE(new MixinBuilder("Minecraft Late")
         .addCommonMixins(
             "MixinMTEBasicMachine")
         .addCommonMixins(
             "MixinMTEMultiBlockBase")
        .addCommonMixins(
            "MixinPlayerData")
        .addCommonMixins(
            "MixinNetworkMonitor")
         .setPhase(Phase.LATE));


    private final MixinBuilder builder;

    Mixins(MixinBuilder builder) {
        this.builder = builder;
    }

    @Nonnull
    @Override
    public MixinBuilder getBuilder() {
        return builder;
    }
}

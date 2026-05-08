package ModName.Mixins;

import ModName.Configs.ConfigServer;
import com.gtnewhorizon.gtnhmixins.builders.IMixins;
import com.gtnewhorizon.gtnhmixins.builders.ITargetMod;
import com.gtnewhorizon.gtnhmixins.builders.MixinBuilder;
import com.gtnewhorizon.gtnhmixins.builders.TargetModBuilder;

import javax.annotation.Nonnull;

public enum Mixins implements IMixins {
    MINECRAFT_EARLY(new MixinBuilder("Minecraft Early")
        .addCommonMixins(
            "MixinInventoryPlayer")
        .setPhase(Phase.EARLY)),
    GT_LATE(new MixinBuilder("GT Late")
         .addCommonMixins(
             "MixinMTEBasicMachine")
         .addCommonMixins(
             "MixinMTEMultiBlockBase")
        .addRequiredMod(new TargetModBuilder()
            .setModId("gregtech"))
        .setApplyIf(() -> ConfigServer.GTIntegration)
        .setPhase(Phase.LATE)),
    AE2_LATE(new MixinBuilder("AE2 Late")
        .addCommonMixins(
            "MixinPlayerData")
        .addCommonMixins(
            "MixinNetworkMonitor")
        .addRequiredMod(new TargetModBuilder()
            .setModId("appliedenergistics2"))
        .setApplyIf(() -> ConfigServer.AE2Integration)
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

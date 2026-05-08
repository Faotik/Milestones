package ModName.Mixins;

import com.gtnewhorizon.gtnhmixins.builders.IMixins;
import com.gtnewhorizon.gtnhmixins.builders.MixinBuilder;

import javax.annotation.Nonnull;

public enum Mixins implements IMixins {

    // MINECRAFT_LATE(new MixinBuilder("Minecraft Late")
    // .addClientMixins(
    // "Test")
    // .setPhase(Phase.LATE)),

//    MINECRAFT_EARLY(new MixinBuilder("Minecraft Early")
//        .addClientMixins(
//            "Test")
//        .setPhase(Phase.EARLY));
    ;


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

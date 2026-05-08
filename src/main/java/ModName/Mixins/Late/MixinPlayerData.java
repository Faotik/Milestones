package ModName.Mixins.Late;

import ModName.Mixins.IPlayerDataAccessor;
import appeng.core.worlddata.IWorldPlayerMapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(targets = "appeng.core.worlddata.PlayerData", remap = false)
public abstract class MixinPlayerData implements IPlayerDataAccessor {
    @Shadow
    private IWorldPlayerMapping playerMapping;

    @Override
    public IWorldPlayerMapping getPlayerMapping() {
        return playerMapping;
    }
}

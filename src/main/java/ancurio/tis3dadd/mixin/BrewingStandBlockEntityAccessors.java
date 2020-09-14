package ancurio.tis3dadd.mixin;

import net.minecraft.block.entity.BrewingStandBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BrewingStandBlockEntity.class)
public interface BrewingStandBlockEntityAccessors {
    @Accessor("brewTime")
    int getBrewTime();

    @Accessor("fuel")
    int getFuel();
}

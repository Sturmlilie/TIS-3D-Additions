package ancurio.tis3dadd.mixin;

import net.minecraft.block.entity.LecternBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LecternBlockEntity.class)
public interface LecternBlockEntityAccessors {
    @Invoker("setCurrentPage")
    void invokeSetCurrentPage(int page);
}

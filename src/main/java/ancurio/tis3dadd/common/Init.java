package ancurio.tis3dadd.common;

import li.cil.tis3d.api.CommonAPI;
import li.cil.tis3d.api.ExtInitializer;
import li.cil.tis3d.api.ModuleAPI;
import li.cil.tis3d.api.machine.Casing;
import li.cil.tis3d.api.machine.Face;
import li.cil.tis3d.api.module.Module;
import li.cil.tis3d.api.module.ModuleProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Init implements ExtInitializer {
    @Override
    public void onInitialize(final CommonAPI api) {
        // Register Two Digit Display module
        final Identifier moduleId = new Identifier("tis3d-additions", "module_twodigitdisplay");

        // Use same ItemGroup as other TIS-3D items
        final Item.Settings settings = new Item.Settings().group(api.itemGroup);
        final Item moduleItem = new Item(settings);
        Registry.register(Registry.ITEM, moduleId, moduleItem);

        api.module.addProvider(new ModuleProvider() {
            public boolean worksWith(final ItemStack stack, final Casing casing, final Face face) {
                return stack.getItem() == moduleItem;
            }

            public Module createModule(final ItemStack stack, final Casing casing, final Face face) {
                return new TwoDigitDisplayModule(casing, face);
            }
        });

        // Register Brewing Stand serial interface
        api.serial.addProvider(new BrewingStandSerialInterfaceProvider());

        // Register Infrared Remote Control
        final Identifier remoteControlId = new Identifier("tis3d-additions", "remote_control");
        // The API object is passed here to avoid having to store it globally
        final Item remoteControl = new RemoteControlItem(new Item.Settings().maxCount(1).group(api.itemGroup), api.infrared);
        Registry.register(Registry.ITEM, remoteControlId, remoteControl);
    }
}

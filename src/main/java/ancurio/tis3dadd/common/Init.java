package ancurio.tis3dadd.common;

import li.cil.tis3d.api.CommonAPI;
import li.cil.tis3d.api.ExtInitializer;
import li.cil.tis3d.api.ManualAPI;
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
    public static ManualAPI manualAPI;

    @Override
    public void onInitialize(final CommonAPI api) {
        System.out.println("Hello TIS-3D common world!");
        final Identifier moduleId = new Identifier("tis3d-additions", "module_twodigitdisplay");

        final Item.Settings settings = new Item.Settings().group(api.itemGroup);
        final Item moduleItem = new Item(settings);
        Registry.register(Registry.ITEM, moduleId, moduleItem);

        api.moduleAPI.addProvider(new ModuleProvider() {
            public boolean worksWith(final ItemStack stack, final Casing casing, final Face face) {
                return stack.getItem() == moduleItem;
            }

            public Module createModule(final ItemStack stack, final Casing casing, final Face face) {
                return new TwoDigitDisplayModule(casing, face);
            }
        });

        api.serialAPI.addProvider(new BrewingStandSerialInterfaceProvider());

        final Identifier remoteControlId = new Identifier("tis3d-additions", "remote_control");
        final Item remoteControl = new RemoteControlItem(new Item.Settings().maxCount(1).group(api.itemGroup), api.infraredAPI);
        Registry.register(Registry.ITEM, remoteControlId, remoteControl);

        manualAPI = api.manualAPI;
    }
}

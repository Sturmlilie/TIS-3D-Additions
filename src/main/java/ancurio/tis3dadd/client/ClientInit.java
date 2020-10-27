package ancurio.tis3dadd.client;

import ancurio.tis3dadd.common.Init;
import li.cil.tis3d.api.ClientAPI;
import li.cil.tis3d.api.ClientExtInitializer;
import li.cil.tis3d.api.ManualClientAPI;
import li.cil.tis3d.api.prefab.manual.ResourceContentProvider;
import li.cil.tis3d.api.prefab.manual.TextureTabIconRenderer;
import net.minecraft.util.Identifier;

public class ClientInit implements ClientExtInitializer {
    public static ClientAPI api;

    @Override
    public void onInitializeClient(final ClientAPI api) {
        // Make sure the manual system can find our markup documents
        api.manual.addProvider(new ResourceContentProvider("tis3d-additions", "doc/"));

        final Identifier tabIcon = new Identifier("tis3d-additions",
            "textures/gui/manual_tis3d_additions.png");

        // Add a custom tab for our pages
        api.manual.addTab(new TextureTabIconRenderer(tabIcon),
            "tis3d-additions.manual.index", "%LANGUAGE%/additions/index.md");

        this.api = api;
    }
}

package ancurio.tis3dadd.client;

import li.cil.tis3d.api.ClientAPI;
import li.cil.tis3d.api.ClientExtInitializer;

public class ClientInit implements ClientExtInitializer {
    public static ClientAPI api;

    @Override
    public void onInitializeClient(final ClientAPI api) {
        this.api = api;
    }
}

package ancurio.tis3dadd.client;

import li.cil.tis3d.api.ClientExtInitializer;

public class ClientInit implements ClientExtInitializer {
	@Override
	public void onInitializeClient() {

		System.out.println("Hello TIS-3D client world!");
	}
}

package ancurio.tis3dadd.common;

import ancurio.tis3dadd.client.ClientInit;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import li.cil.tis3d.api.ClientAPI;
import li.cil.tis3d.api.FontRendererAPI;
import li.cil.tis3d.api.machine.Casing;
import li.cil.tis3d.api.machine.Face;
import li.cil.tis3d.api.machine.Pipe;
import li.cil.tis3d.api.machine.Port;
import li.cil.tis3d.api.prefab.module.AbstractModuleWithRotation;
import li.cil.tis3d.api.util.RenderUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.CompoundTag;

/**
 * The TwoDigitDisplayModule will display the lower 8 bit of whichever
 * value was last read from any of its ports.
 */
public final class TwoDigitDisplayModule extends AbstractModuleWithRotation {
    // --------------------------------------------------------------------- //
    // Persisted data

    // The value; only lower 8 bit are displayed.
    private short value;

    // NBT data names.
    private static final String TAG_VALUE = "value";

    // Data packet types.
    private static final byte DATA_TYPE_UPDATE = 0;

    // --------------------------------------------------------------------- //

    public TwoDigitDisplayModule(final Casing casing, final Face face) {
        super(casing, face);
    }

    // --------------------------------------------------------------------- //
    // Module

    @Override
    public void step() {
        stepInput();
    }

    @Override
    public void onDisabled() {
        // Clear timer on shutdown.
        value = 0;
        sendData();
    }

    @Override
    public void onData(final ByteBuf data) {
        value = data.readShort();
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void render(final BlockEntityRenderDispatcher rendererDispatcher, final float partialTicks,
                       final MatrixStack matrices, final VertexConsumerProvider vcp,
                       final int light, final int overlay) {
        if (!getCasing().isEnabled()) {
            return;
        }

        matrices.push();
        rotateForRendering(matrices);

        final int red = value >> 8 & 0xF;
        final int green = value >> 4 & 0xF;
        final int blue = value & 0xF;
        // Maximum: 16.0f
        final float luminance = 0.2126f * red + 0.7152f * green + 0.0722f * blue;
        final float threshold = 0.5f * 16;
        final int textColor = luminance < threshold ? 0xFFFFFFFF : 0xFF000000;
        final FontRendererAPI fontAPI = ClientInit.api.fontRendererAPI;

        final String displayString = String.format("%02X", value & 0xFF);
        final VertexConsumer vcFont = fontAPI.chooseVertexConsumer(ClientAPI.Font.NormalFont, vcp);

        final int width = displayString.length() * ClientInit.api.fontRendererAPI.getCharWidth();
        final int height = fontAPI.getCharHeight();

        matrices.push();
        matrices.translate(0.1f, 0.1f, 0);
        matrices.scale(1 / 20f, 1 / 20f, 1);

        fontAPI.drawString(ClientAPI.Font.NormalFont, matrices.peek(), vcFont,
                           RenderUtil.maxLight, overlay,
                           textColor, displayString, displayString.length());

        matrices.pop();
        matrices.translate(0f, 0f, 0.005f / 2);

        final VertexConsumer vc = vcp.getBuffer(RenderLayer.getSolid());
        final int color = 0xFF << 24 | red << 20 | green << 12 | blue << 4;

        final float px = 1.0f / 32.0f;
        RenderUtil.drawColorQuad(matrices.peek(), vc, px, px, 1-px*2, 1-px*2,
                                 color, RenderUtil.maxLight, overlay);

        matrices.pop();
    }

    @Override
    public void readFromNBT(final CompoundTag nbt) {
        super.readFromNBT(nbt);

        value = nbt.getShort(TAG_VALUE);
    }

    @Override
    public void writeToNBT(final CompoundTag nbt) {
        super.writeToNBT(nbt);

        nbt.putShort(TAG_VALUE, value);
    }

    // --------------------------------------------------------------------- //

    /**
     * Update the inputs of the module.
     */
    private void stepInput() {
        for (final Port port : Port.VALUES) {
            // Continuously read from all ports.
            final Pipe receivingPipe = getCasing().getReceivingPipe(getFace(), port);
            if (!receivingPipe.isReading()) {
                receivingPipe.beginRead();
            }
            if (receivingPipe.canTransfer()) {
                // Set the value.
                value = receivingPipe.read();
                sendData();
            }
        }
    }

    private void sendData() {
        final ByteBuf data = Unpooled.buffer();
        data.writeShort(value);
        getCasing().sendData(getFace(), data, DATA_TYPE_UPDATE);
    }
}

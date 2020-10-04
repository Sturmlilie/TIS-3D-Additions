package ancurio.tis3dadd.common;

import li.cil.tis3d.api.InfraredAPI;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;

/**
 * The code book, utility book for coding ASM programs for execution modules.
 */
public final class RemoteControlItem extends Item {
    private static final short PACKET_VALUE = (short) 0x7EEF;
    private final InfraredAPI api;

    public RemoteControlItem(final Item.Settings settings, final InfraredAPI api) {
        super(settings.maxCount(1));
        this.api = api;
    }

    // --------------------------------------------------------------------- //
    // Item

    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(final ItemStack stack, final World world, final List<Text> tooltip, final TooltipContext options) {
        super.appendTooltip(stack, world, tooltip, options);
        final TranslatableText info = new TranslatableText("tis3d-additions.tooltip.remote_control");
        info.setStyle(Style.EMPTY);
        tooltip.add(info);
    }

    @Override
    public TypedActionResult<ItemStack> use(final World world, final PlayerEntity player, final Hand hand) {
        api.sendPacket(world, player.getCameraPosVec(0), player.getRotationVec(0), PACKET_VALUE);

        return new TypedActionResult<>(ActionResult.SUCCESS, player.getStackInHand(hand));
    }

    @Override
    public ActionResult useOnBlock(final ItemUsageContext context) {
        return ActionResult.PASS;
    }

    // --------------------------------------------------------------------- //
    // ItemBook

    @Override
    public boolean isEnchantable(final ItemStack stack) {
        return false;
    }

    @Override
    public int getEnchantability() {
        return 0;
    }
}

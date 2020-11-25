package ancurio.tis3dadd.common;

import ancurio.tis3dadd.mixin.LecternBlockEntityAccessors;
import li.cil.tis3d.api.serial.SerialInterface;
import li.cil.tis3d.api.serial.SerialInterfaceProvider;
import li.cil.tis3d.api.serial.SerialProtocolDocumentationReference;
import net.minecraft.block.entity.LecternBlockEntity;
import net.minecraft.item.WrittenBookItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public final class LecternSerialInterfaceProvider implements SerialInterfaceProvider {
    // --------------------------------------------------------------------- //
    // SerialInterfaceProvider

    @Override
    public boolean worksWith(final World world, final BlockPos position, final Direction side) {
        return world.getBlockEntity(position) instanceof LecternBlockEntity;
    }

    @Override
    public SerialInterface interfaceFor(final World world, final BlockPos position, final Direction side) {
        final LecternBlockEntity lectern = (LecternBlockEntity) world.getBlockEntity(position);
        if (lectern == null) {
            throw new IllegalArgumentException("Provided location does not contain a lectern. Check via worksWith first.");
        }
        return new SerialInterfaceLectern(lectern);
    }

    @Override
    public SerialProtocolDocumentationReference getDocumentationReference() {
        return new SerialProtocolDocumentationReference("Minecraft Lectern", "additions/protocols/minecraft_lectern.md");
    }

    @Override
    public boolean isValid(final World world, final BlockPos position, final Direction side, final SerialInterface serialInterface) {
        return serialInterface instanceof SerialInterfaceLectern;
    }

    // --------------------------------------------------------------------- //

    private static final class SerialInterfaceLectern implements SerialInterface {
        private static final String TAG_MODE = "mode";

        private enum Mode {
            CurrentPage,
            PageCount
        }

        private final LecternBlockEntity lectern;
        private Mode mode = Mode.CurrentPage;

        SerialInterfaceLectern(final LecternBlockEntity lectern) {
            this.lectern = lectern;
        }

        private int getCurrentPage() {
            if (!lectern.hasBook()) {
                return 0;
            }

            return lectern.getCurrentPage() + 1;
        }

        private void setCurrentPage(int page) {
            LecternBlockEntityAccessors accessor = (LecternBlockEntityAccessors) lectern;
            accessor.invokeSetCurrentPage(page);
        }

        private int getPageCount() {
            return WrittenBookItem.getPageCount(lectern.getBook());
        }

        @Override
        public void skip() {
        }

        @Override
        public boolean canWrite() {
            return true;
        }

        @Override
        public void write(final short value) {
            if (value == 0) {
                mode = Mode.CurrentPage;
            } else if (value == 1) {
                mode = Mode.PageCount;
            } else if ((value & 0x100) != 0) {
                int newPage = (value & 0xFF) - 1;

                // Add some UB: Page "0" maps to last page
                if (newPage < 0) {
                    newPage += 256;
                }

                setCurrentPage(newPage);
            }
        }

        @Override
        public boolean canRead() {
            return true;
        }

        @Override
        public short peek() {
            switch (mode) {
            case CurrentPage:
                return (short) getCurrentPage();
            case PageCount:
                return (short) getPageCount();
            }

            return 0;
        }

        @Override
        public void reset() {
            mode = Mode.CurrentPage;
        }

        @Override
        public void readFromNBT(final CompoundTag nbt) {
            mode = Mode.class.getEnumConstants()[nbt.getByte(TAG_MODE)];
        }

        @Override
        public void writeToNBT(final CompoundTag nbt) {
            nbt.putByte(TAG_MODE, (byte) mode.ordinal());
        }
    }
}

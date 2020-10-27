package ancurio.tis3dadd.common;

import ancurio.tis3dadd.mixin.BrewingStandBlockEntityAccessors;
import li.cil.tis3d.api.serial.SerialInterface;
import li.cil.tis3d.api.serial.SerialInterfaceProvider;
import li.cil.tis3d.api.serial.SerialProtocolDocumentationReference;
import net.minecraft.block.entity.BrewingStandBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public final class BrewingStandSerialInterfaceProvider implements SerialInterfaceProvider {
    // --------------------------------------------------------------------- //
    // SerialInterfaceProvider

    @Override
    public boolean worksWith(final World world, final BlockPos position, final Direction side) {
        return world.getBlockEntity(position) instanceof BrewingStandBlockEntity;
    }

    @Override
    public SerialInterface interfaceFor(final World world, final BlockPos position, final Direction side) {
        final BrewingStandBlockEntity stand = (BrewingStandBlockEntity) world.getBlockEntity(position);
        if (stand == null) {
            throw new IllegalArgumentException("Provided location does not contain a brewing stand. Check via worksWith first.");
        }
        return new SerialInterfaceBrewingStand(stand);
    }

    @Override
    public SerialProtocolDocumentationReference getDocumentationReference() {
        return new SerialProtocolDocumentationReference("Minecraft Brewing Stand", "additions/protocols/minecraft_brewingstand.md");
    }

    @Override
    public boolean isValid(final World world, final BlockPos position, final Direction side, final SerialInterface serialInterface) {
        return serialInterface instanceof SerialInterfaceBrewingStand;
    }

    // --------------------------------------------------------------------- //

    private static final class SerialInterfaceBrewingStand implements SerialInterface {
        private static final String TAG_MODE = "mode";

        private enum Mode {
            Fuel,
            Brewtime
        }

        private final BrewingStandBlockEntityAccessors stand;
        private Mode mode = Mode.Fuel;

        // Buffer the brewTime value the first time peek is called,
        // so the write doesn't get cancelled midway through.
        // Once skip() is called, we're free to read (and buffer)
        // a new value from the block entity.
        // We have to do this because the brewTime changes every tick.
        private boolean brewtimeIsBuffered = false;
        private short brewtimeBuffer;

        SerialInterfaceBrewingStand(final BrewingStandBlockEntity entity) {
            this.stand = (BrewingStandBlockEntityAccessors) entity;
        }

        private short getBufferedBrewtime() {
            if (!brewtimeIsBuffered) {
                brewtimeBuffer = (short) stand.getBrewTime();
                brewtimeIsBuffered = true;
            }

            return brewtimeBuffer;
        }

        @Override
        public boolean canWrite() {
            return true;
        }

        @Override
        public void write(final short value) {
            if (value == 0) {
                mode = Mode.Fuel;
            } else {
                mode = Mode.Brewtime;
            }
        }

        @Override
        public boolean canRead() {
            return true;
        }

        @Override
        public short peek() {
            switch (mode) {
            case Fuel:
                return (short) stand.getFuel();
            case Brewtime:
                return getBufferedBrewtime();
            }

            return 0;
        }

        @Override
        public void skip() {
            brewtimeIsBuffered = false;
        }

        @Override
        public void reset() {
            mode = Mode.Fuel;
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

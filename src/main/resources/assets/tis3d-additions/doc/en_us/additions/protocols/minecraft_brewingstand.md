# Brewing Stand

![Where is my lo-fi for witches?](block:minecraft:brewing_stand)

This protocol allows measuring basic parameters of the brewing stand's operation. It supports both read and write operations.

When writing to the serial interface of a brewing stand, two values are legal: `0` and `1`. Providing other values leads to undefined behavior. A value `0` sets the interface to the mode returning information on the remaining fuel in the brewing stand, a value of `1` sets the interface to the mode returning information on the current brewing progress.

When reading from the serial interface of a brewing stand, a value in the absolute range of [0, 20] is returned if reading the current fuel value, otherwise the range switches to [0, 100]. When in the mode returning information on the remaining fuel, the returned value indicates the remaining possible brewing operations until a new instance of blaze powder is consumed. When in the mode returning information on the current brewing progress, 0 means no progress, 100 means fully brewed and just about to finish.

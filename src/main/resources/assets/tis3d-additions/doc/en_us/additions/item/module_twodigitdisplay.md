# Two Digit Hex Display

![Nice colors!](item:tis3d-additions:module_twodigitdisplay)

The two digit hex display module renders the lower 8 bits of whatever value was last read on any of its ports.

The background color is defined via the lower 12 bits of the last value. From most to least significant, 4 bits respectively define red, green and blue. Essentially, an input value of `0xXYZW` gets turned into the background color `0xY0Z0W0` (in RGB). The highest 4 bits are completely ignored.

The text color is adjusted automatically to provide maximum contrast to whichever is the current background color.

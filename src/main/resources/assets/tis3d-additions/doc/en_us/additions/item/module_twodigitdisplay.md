# Two Digit Hex Display

![Nice colors!](item:tis3d-additions:module_twodigitdisplay)



The two digit hex display module allows quick and easily readable display of a hexadecimal value. Values in the range `0x00` - `0xFF` are supported.

The module continuously reads on all its ports, and renders the lower 8 bits of the last read value.
The background color is defined via the lower 12 bits of the current value. From most to least significant, 4 bits respectively define red, green and blue. Thus, an input value of `0xSTUV` gets turned into the background color `0xT0U0V0` (in RGB). The highest 4 bits are ignored.

The text color is adjusted automatically to provide maximum contrast to whichever is the current background color.

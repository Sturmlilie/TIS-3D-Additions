# Lectern

![I like your accent, where you from?](block:minecraft:lectern)

This protocol allows measuring basic parameters of the lectern's state. It supports both read and write operations.

When writing to the serial interface of a lectern, two values are legal: `0` and `1`. Providing other values leads to undefined behavior. A value `0` sets the interface to the mode returning information on the current page, a value of `1` sets the interface to the mode returning information on the total page count; both are in relation to the currently placed book. If no book is present, the interface returns a value of `0` in either mode.

When reading the current page number from the serial interface of a lectern while a book is present, the obtained page indices are 1-based, meaning page 1 corresponds to a value of `1` and so on. A read value of `0` indicates the absence of a book in either mode.


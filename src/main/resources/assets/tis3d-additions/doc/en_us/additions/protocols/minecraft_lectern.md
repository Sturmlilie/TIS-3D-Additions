# Lectern

![I like your accent, where you from?](block:minecraft:lectern)

This protocol allows measuring basic parameters of the lectern's state. It supports both read and write operations.

When writing to the serial interface of a lectern, **bit 8** indicates the mode of operation, while **bits 7-0** carry the payload.

If bit 8 is cleared, two payloads are legal: `0` and `1`. Providing other payloads leads to undefined behavior. A payload of `0` sets the interface to the mode returning information on the current page. A payload of `1` sets the interface to the mode returning information on the total page count. Both are in relation to the currently placed book. If no book is present, reading from the interface returns a value of `0` in either read mode.

If bit 8 is set, the current page number is set to the payload.

Page numbers are 1-based, meaning the first page corresponds to a value of `1` and so on. A read value of `0` indicates the absence of a book in either read mode. Writing a page number outside the permissible range leads to undefined behavior.


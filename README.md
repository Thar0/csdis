# csdis

_Ocarina of Time Cutscene Disassembler_

CSDis will generate a macro representation for a cutscene, given its raw data.

Requires JRE-8 or above to run.

### Usage

`java -jar csdis.jar [--text] <infile> [outfile]`

The `infile` should be formatted either as raw bytes of cutscene data, or
a comma-separated list of 32-bit integers in hexadecimal form similar to the
contents of a C-like array. If the latter, the `--text` flag must be chosen.

If `outfile` is specified, the macros will be output to that file, otherwise 
the result is printed to console.

### Building

Check out the repo and `mvn clean install`

### Contributing

If any cutscene commands are missing from here, feel free to create an issue or 
implement it yourself.

Eventually certain values in the command macros should be output as enums rather 
than raw values.

Would like to support Majora's Mask eventually.
# ImageMagick commands

## mogrify / convert
mogrify overwrites source files
convert keeps source files

* batch convert PNGs to JPGs
    - `mogirfy -format jpg *.png`
    *note* that in order to convert file format in batch-mode, the _mogirfy_ command is used
* convert single PNG to JPG
    - `convert some.png another.jpg`

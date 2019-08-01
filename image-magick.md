# ImageMagick commands

## mogrify / convert
mogrify overwrites source files
convert keeps source files

* batch convert PNGs to JPGs
    - `mogirfy -format jpg *.png`
    *note* that in order to convert file format in batch-mode, the _mogirfy_ command is used
* convert single PNG to JPG
    - `convert some.png another.jpg`

convert -quality 70 picture.jpg picture-optimised.jpg
convert cute-kittens.jpg -crop +0-20 +repage cute-kittens-cropped.jpg

### FLIP / FLOP an image
-flip
  flip image in the vertical direction |
    (less useful, sky goes down, grass goes up)
-flop
  flop image in the horizontal direction -
    (usually what you want, mirror view of the image)
    mogirfy -flop myimage.png

## Animation

```
mogrify  -resize 128x  profile*.jpg
convert   -delay 0  -loop 0  profile*.jpg  peter.gif
```


## Make a PNG with transparent background

as an example we'll take a PNG that has a white background and set the white pixels to be transparent in the converted image:

```
convert source.png -fuzz nn% -transparent white target.png
```

The larger the fuzz percentage %, the more variation from white is allowed to become transparent.



to deal with animated gifs use **gifsicle**

```
brew install gifsicle
gifsicle --resize 48x48 --colors 16 someGif.gif > smallerGif.gif

# -U switch good for animated gifs, seq script selects only every other frame
gifsicle --resize 64x64 -U clock.gif `seq -f "#%g" 0 2 143` -o output.gif
```


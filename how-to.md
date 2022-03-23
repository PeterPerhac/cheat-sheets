# How to's


## How to change default git branch from master to main

```bash
# Step 1
# create main branch locally, taking the history from master
git branch -m master main
# Step 2
# push the new local main branch to the remote repo (GitHub)
git push -u origin main
# Step 3
# switch the current HEAD to the main branch
git symbolic-ref refs/remotes/origin/HEAD refs/remotes/origin/main
# Step 4
# change the default branch on GitHub to main
# https://docs.github.com/en/github/administering-a-repository/setting-the-default-branch
# Step 5
# delete the master branch on the remote
git push origin --delete master
```


## How to update mp3 metadata

  `brew install id3lib`

  Usage: id3tag [OPTIONS]... [FILES]...
   -h         --help            Print help and exit
   -V         --version         Print version and exit
   -1         --v1tag           Render only the id3v1 tag (default=off)
   -2         --v2tag           Render only the id3v2 tag (default=off)
   -aSTRING   --artist=STRING   Set the artist information
   -ASTRING   --album=STRING    Set the album title information
   -sSTRING   --song=STRING     Set the title information
   -cSTRING   --comment=STRING  Set the comment information
   -CSTRING   --desc=STRING     Set the comment description
   -ySTRING   --year=STRING     Set the year
   -tSTRING   --track=STRING    Set the track number
   -TSTRING   --total=STRING    Set the total number of tracks
   -gSHORT    --genre=SHORT     Set the genre
   -w         --warning         Turn on warnings (for debugging) (default=off)
   -n         --notice          Turn on notices (for debugging) (default=off)





## How to make an animated gif emoji
make individual frames. If they are exported as PNG, you will have to convert them to GIF first

```bash
# careful, this will overwrite any existing same-name gifs in the directory
mogrify -format gif *.png
```

Then it's a simple gifsicle command, listing the individual frames in the correct order

```bash
gifsicle --disposal=previous --delay=50 --loop -O3 get.gif well.gif soon.gif >| animated.gif
```


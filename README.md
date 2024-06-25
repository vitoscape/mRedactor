Audio file (mp3, flac (tested) and other) tag redactor.

**Work in progress.**

Based on [jaudiotagger](https://bitbucket.org/ijabz/jaudiotagger/)
by [IJabz](https://bitbucket.org/ijabz/).

# Version 0.2.1 (terminal work)
This version can only change the genres of audio files.
Suitable for changing tags for a large number of audio files with the same tag values.
**Before use this test version make backup of your files.**



To change genres of your audio files run ```mRedactor.jar``` in terminal using:

```
java -jar mRedactor.jar
```

First you need to enter the path of the directory with audio files. Then select mode:
0. Edit one album.
1. Edit various audio files in one directory.
2. Remove multiplied tags separated by ```;```.

## Mode 0: edit album
You need to type values for tags:
1. Artist
2. Album
3. Genre
4. Year
5. Number of tracks

Every audio file will be processed. Tag "Album artist" will be set the same as "Artist". Comments of the files will be cleared.

## Mode 1: edit various audio files (DOESN'T WORK WELL ON LARGE AMOUNT OF FILES (TRIED ON ABOUT 3500))
You need to type the path of the directory with audio files. Then you need to select tag to change:
1. Genre
2. Artist
3. Album
4. Album artist
5. Year

All old tags will be replaced with new ones.

## Mode 2: remove multiplied tags separated by ```;```
All multiplied tags will be replaced by single tag:

```Genre;Genre;Genre;Genre;Genre;Genre``` will be replaced by ```Genre```.

**WARNING**: If you need to leave different tags in the same field then don't use this mode.
Program will delete all tags in every field except first one.
This will be fixed in future versions.

Audio file (mp3, flac (tested) and other) tag redactor.

**Work in progress.**

Based on [jaudiotagger](https://bitbucket.org/ijabz/jaudiotagger/)
by [IJabz](https://bitbucket.org/ijabz/).

# Version 0.2 (terminal work)
This version can only change the genres of audio files.
Suitable for changing tags for a large number of audio files with the same tag values.
**Before use this test version make backup of your files.**



To change genres of your audio files run ```mRedactor.jar``` in terminal using:

```
java -jar mRedactor.jar
```

First you need to select one of two modes:
0 — edit one album;
1 — edit various audio files in one directory.

## Mode 0: edit album
You need to type the path of the directory with audio files. Then you need to type tags:
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

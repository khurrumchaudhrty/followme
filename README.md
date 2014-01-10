
the Below mentioned project uses couple of references from other project i just didnt wanted to add same project multiple times

Explanaination of the projects included.

followme
========
a simple app to share your lat long over sms/email, to compile please checkpout both the repo in one folder
refer to project.properties in case you need to understand the library refferences and how compatibility lib is being stored.

the apk is located in dist folder with in the project folder



- google-play-service-lib = a play service stub provided withi in the sdk, i just commited here so that if you want to compile the whole code is included here.



googlemaphugeoverlaysettest
=========================

this project helps visualize the location history from https://www.google.com/settings/takeout (in json formate) on the map in form of poly lines, for visualiziong your own location history file you would have to do the following

- go to https://www.google.com/settings/takeout and get your location history data downloaded, 
- after extracting (for now) place you json file in assets folder replace the existing file, note naming is important, replace the file that is already present in asset folder.
- compile the app and side load it.
- the parsing in this project is done using jackson, pretty fast !, the apk provided parses the data over 384K geopoints, the benchmarking number on my Nexus 5 were very impressive, i opted for this as no other parser will cut it :) 


Work in progress
- once your way points are done parsing (you would know once the number stops increasing), do not play with the map till it shows you the polylines.
- the map is very interactive till we draw the poly lines, i have couple of ideas to implement but for sample this will do
- the test file provided draws 33 points over MA area.
- the apk that has been provided has way points over 384K


the apk
=======
the files of both the project are provided in dist folder


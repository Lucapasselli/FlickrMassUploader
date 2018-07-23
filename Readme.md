FlickrMassUploader instructions

This software is for those people that would backup local photos and videos to flickr or restore flickr photos and videos to a local store.
Software upload your photos and videos in your flickr accont and then create an album with the same name of the principal folder that contains the media.
if it founds a photo or video with the same name, album and date the program don't update twice.
Media are uploaded in private mode (They are visible only for you)
Program store informations about relative directory of each Media in Flickr Media Description so it can easily restore File in the original position if it is neccessary.
With restore option program can download all your flickr photoset include videos in full size,in case of photos without file informations, Folder structure is "choosenFoleder"+/NoBackupPhoto+/"AlbumName"+/"IdPhoto.extension". (Photos are downloaded throught api, videos with ChromeDriver)
Pressing restore button will be downloaded only files that not exists in local system (Your local photos will never be overwritten)

FIRST STEP - REQUIREMENTS (one time only)
Before proceding please ensure you have the following requirements:

1 - Have java installed (https://www.java.com/en/download/)
2 - Subscription to flickr
3 - Your own APIKEY and SHAREDSECRET from Flickr
If you don't have this keys yet please visit https://www.flickr.com/services/apps/create/apply
and create your own Keys.

Ok now we can go ahead, please follow this steps.

SECOND STEP - FIRST CONFIGURATION and AUTHENTICATION (one time only)

1 - Open the program
2 - Compile field ApiKey and sharedSecret
3 - Select the folder where your photos are stored (ex. c:\users\donalduck\Photos)
4 - Select Backup Sync type
5 - Press Save Button
6 - Press Request Token Button to allow Program to operate with your filckr account (Follow the instructions)

THIRD STEP - Backup or Restore your photos and videos!!!! (whenever you want)

Press "Backup" button to backup your media.
Press "Restore" button to restore your entire flickr set to local disk.

or

Launch the application without GUI with /backup or /restore parameter to use programm without gui or to schedule a backup or restore
Example : java -jar FlickrMassUploader.jar /backup


A Special thanks to boncey for flikr4java development and the seleniumhq team!


IMPORTANT: I realized during my tests that flickr sometimes didn't return original video file also using browser, in that case it's impossible to me to download the original one. Remember that not original video files are cutted by Flickr at the third minute.

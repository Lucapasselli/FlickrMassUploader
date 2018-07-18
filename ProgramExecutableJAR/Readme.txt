FlickrMassUploader instructions

This software is for those people that would backup local photos to flickr or restore flickr photos to a local store.
Software upload your photos in your flickr accont and then create an album with the same name of the principal folder that contains the photo.
if it founds a photo with the same name, album and date the program don't update twice.

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

THIRD STEP - Backup your photos!!!! (whenever you want)

Press backup Button!

or

Launch the application without GUI with /backup parameter


From version 1.22 you can also safetely restore your files
Pressing backup button will be downloaded only files that not exists in the local system
Your photos will never be overwritten.
In case of photos without file informations Folder structure is "choosenFoleder"+/NoBackupPhoto+/"AlbumName"+/"IdPhoto.extension"



GitHub Code
https://github.com/Lucapasselli/FlickrMassUploader



I can’t guarantee that software will be free from error.
I'll take no responsibility for any damages including, but not limited to:
indirect or consequential damages, loss of data etc...
I'm not responsible if someone uses this software to damage others.
To prevent accidental data loss on your flickr account it's preferable to open an account only for backups.

A Special thanks to boncey for flikr4java development and the seleniumhq team!







Beta 1.32  (18/07/2018)
Fix problem with button to test credentials, it was never ending the test
Implement Video Download also for linux systems (For linux users remember to make executable file called cromedriver in the root folder or video download will never work)

Beta 1.31  (17/07/2018)
Change WebDriver From firefox to Chrome, chrome driver contain less Warnings a seems to be better. So now you need to have chrome installed to download Videos

Beta 1.30  (17/07/2018)
Now it's finally possible to download fullsize videos from Flickr
To do this you must insert your flickr Username and Password and save it for the first time.
Due to a Flickr Api bug, to download Videos program must act like a browser, do the authentication process and then download files.
INFO: At the moment this feature is avaiable only for 64 bit Windows and require Firefox installation.
In the next days i'll test and release the next version compatible also with linux systems.
With this version included in the zip there is a file called gekodriver.exe, you don't need to execute it, program will use it to communicate with Firefox when is neccesary

Beta 1.25  (05/07/2018)
Now photos and videos in Folder called NoBackupPhoto will never be backuped.
This implementation is to avoid duplicates when you restore and then rebackup photos from flicker that were not backuped with this tool (Flickr phots not backuped with this program are in fact restored in NoBackupPhoto folder).
This is also usefull if you don't want to upload some photos to your flickr account, with this implementation you can choose for example the entire photo directory and then move in NoBackupPhoto folder files that you don't want to backup online. 

Beta 1.24  (04/07/2018)
Encrypt string of token access and username on config file.
Implement option /restore to restore photos via command line    (example: java -jar FlickrMassUploader.jar /restore)

Beta 1.23  (03/07/2018)
Improve performance retrieving photo list from flickr
Before the update it takes 8 seconds to retrieve informations of about 15 albums, with this new version it takes 3 seconds.

Beta 1.22  (02/07/2018)
Fix problems with Linux and MacOS
Implement progress bar also during restoring phase

Beta 1.21  (28/06/2018)
Fix error (no photo found) during restore operation 
In the latest versions there is a bug that block program operations in linux and mac os systems, i will fix in the next release

Beta 1.20  (27/06/2018)
***IMPORTANT***
Due to a performance problem i had to change photo check from tags to description.
This means that ALL YOUR PHOTOS WILL BE REUPLOADED the first time.

Beta 1.12  (26/06/2018)
Lighten jar file
Now are restored also photos withouth tags, Folder structure in this case is "choosenFoleder"+/NoBackupTagPhoto+/"AlbumName"+/"IdPhoto.extension"

Beta 1.11  (26/06/2018)
Fix error that block the program when you press the BackupNow Button or when you use the /backup option in command line mode

Beta 1.10  (25/06/2018)
Upgrade Data tag with hour and minutes
Implement Photo Restore for Photos that have the original path TAG, other will be ignored.
Videos for now are skipped for restore cause a Flickr API bug retrieving original size.
Please for now do not restore files in the same folder than originals, it can be overwritten cause i've just implemented this step

Beta 1.04  (23/06/2018)
Added ForceStop Button to force stopping the backup process also if a file is in uploading condition.
Add uploading file dimension to log view information

Beta 1.03  (22/06/2018)
In FullSyncMode now are deleted remote photos with not album association

Beta 1.02  (22/06/2018)
Add tag Original file name
Add tag Last modified file date
Change method to check onlne files (now is tags based, before was based on title)
If you uploaded files with Beta 1.01 you need to reupload all (sorry for this)

Beta 1.01  (30/05/2018)
Fix Problems with first authentication and text field that results disabled

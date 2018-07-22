/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flickrmassuploader;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.AuthInterface;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.photos.Extras;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.photos.Size;
import com.flickr4java.flickr.photosets.Photoset;
import com.flickr4java.flickr.photosets.PhotosetsInterface;
import com.flickr4java.flickr.tags.Tag;
import com.flickr4java.flickr.uploader.UploadMetaData;
import com.flickr4java.flickr.uploader.Uploader;
//import static flickrmassuploader.FlickrMassUploader.Nsid;
//import static flickrmassuploader.FlickrMassUploader.auth;
import java.awt.Desktop;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.security.Key;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;
//import java.util.logging.Level;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
//import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 *
 * @author Luca Passelli
 */
public class FlickrMassUploader extends javax.swing.JFrame {

    /**
     * Creates new form FlickrMassUploader
     */
//    static final Logger logger=Logger.getLogger(FlickrMassUploader.class.getName());
    static String Version="Beta 1.34";
    static String apiKey = "";
    static String sharedSecret = "";
    static Flickr flickr;
    static Auth auth;
    static AuthInterface authInterface;
    static String AccessToken = "";
    static String TokenSecret = "";
    static String Nsid;
    static String User="";
    static String Directory = "";
    static String ConfirmationCode = "";
    static String Username ="";
    static String Password="";
    static Map<String, String> localphotos;
//    static Map<String, String> remotephotos;
    static Map<String, String> remotealbums;
    static Map<String, String> remoteLastModifiedDate;
    static Map<String, String> remotePhotosToDelete;
    static Map<String, String> localalbums;
    static Map<String, String> phototoupload;
    static Map<String, String> phototodownload;
//    static Map<String, String> remotephotosfullpath;
    static Map<String, String> remotephotoswithdata;
    static Map<String, String> remotephotoswithoutdata;
    static Map<String, String> remoteoriginalformat;
    static Map<String, String> remotemedia;
    static Map<String, String> videotodownload;
    static backup Backup;
    static restore Restore;
    static Thread process;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss");
    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //SimpleDateFormat sdfOLD = new SimpleDateFormat("yyyy/MM/dd");
    static boolean StopProcess = false;
    static boolean GraphicsOn = true;
    static boolean CheckDate=false;
    static String DownloadVideo="No";
    static String OS=System.getProperty("os.name").toLowerCase();
    
    static int NRPhotos=0;
    static int NRnoBPhotos=0;
    static int NRAlbums=0;
    
    static int ConcurrentProcess=5;
    static int NumProcess=0;

    static String key = "Pippo345Pluto345";
    
    static int sync = 0;
    
    //sync=o means no sync
    //sync=1 means sync only directorys
    //sync=2 means full sync

    public FlickrMassUploader() {
        
        
        
        //redirect standard error to logger
        //Logger logger = Logger.getLogger(FlickrMassUploader.class.getName());
//...;
        System.setErr(
        new PrintStream(
            new CustomOutputStream(Level.ERROR)
            )
        );  
       /* System.setOut(
        new PrintStream(
                new CustomOutputStream(logger,Level.INFO)
            )
        );*/
        
        String log4jConfPath = "log4j.properties";
        PropertyConfigurator.configure(log4jConfPath);

        File proprieta = new File("config.properties");
        //prima di leggere il file devo verificare se esiste
        if (proprieta.exists()) {
            ReadPropertiesFile();
        }
        initComponents();
        ButtonStop.setVisible(false);
        if (apiKey != null) {
            TextFieldApiKey.setText(apiKey);
        }
        
        if (sharedSecret != null) {
            TextFieldSharedSecret.setText(sharedSecret);
        }
        if (Directory != null) {
            TextFieldPhotoDirectory.setText(Directory);
        }
        //System.out.println(TokenAccesso);
        if (AccessToken != null && TokenSecret != null && !AccessToken.equalsIgnoreCase("") && !TokenSecret.equalsIgnoreCase("")) {
            LabelUser.setText("USER: " + User);
            TextFieldApiKey.setEnabled(false);
            TextFieldSharedSecret.setEnabled(false);
            ButtonRequestToken.setEnabled(false);
        } else {
            ButtonRequestToken.setEnabled(true);
            ButtonUpload.setEnabled(false);
        }
        ComboBoxSyncType.setSelectedIndex(sync);
        String version = this.getClass().getPackage().getImplementationVersion();
    //    CheckBoxRestore.setVisible(false);
    //    ButtonRestore.setVisible(false);
        LabelForceStop.setVisible(false);
        ButtonForceStop.setVisible(false);
        if (DownloadVideo.equalsIgnoreCase("Yes")) CheckBoxEnableVideos.setSelected(true);
       // Message("Version : "+version);
        this.setTitle("Flickr Mass Uploader by Luca Passelli                                           Version : "+Version);
        Message("OS : "+OS,Level.INFO);
        Message("INFO : From Version Beta 1.25 photos and videos in Folder called NoBackupPhoto will never be backuped",Level.INFO);
        Message("INFO : For linux users remember to make executable file called cromedriver in the root folder or video download will never work",Level.INFO);
        

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ButtonRequestToken = new javax.swing.JButton();
        ButtonUpload = new javax.swing.JButton();
        LabelApiKey = new javax.swing.JLabel();
        LabelSharedSecret = new javax.swing.JLabel();
        TextFieldApiKey = new javax.swing.JTextField();
        TextFieldSharedSecret = new javax.swing.JTextField();
        LabelUser = new javax.swing.JLabel();
        LabelPhotoDirectory = new javax.swing.JLabel();
        TextFieldPhotoDirectory = new javax.swing.JTextField();
        ButtonDeleteCredentials = new javax.swing.JButton();
        ButtonSave = new javax.swing.JButton();
        LabelSyncType = new javax.swing.JLabel();
        ComboBoxSyncType = new javax.swing.JComboBox<>();
        ButtonChooseDirectory = new javax.swing.JButton();
        ButtonStop = new javax.swing.JButton();
        ProgressBarBackup = new javax.swing.JProgressBar();
        LabelStartTime = new javax.swing.JLabel();
        LabelTimeRemaining = new javax.swing.JLabel();
        LabelSyncDescription = new javax.swing.JLabel();
        ButtonRestore = new javax.swing.JButton();
        CheckBoxRestore = new javax.swing.JCheckBox();
        ButtonForceStop = new javax.swing.JButton();
        LabelForceStop = new javax.swing.JLabel();
        CheckBoxEnableVideos = new javax.swing.JCheckBox();
        ButtonUpdateFlickrCredentials = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        LabelRestoreOptions = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jScrollPane2 = new javax.swing.JScrollPane();
        TextPaneLog = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Flickr Mass Uploader by Luca Passelli");

        ButtonRequestToken.setText("RequestToken");
        ButtonRequestToken.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonRequestTokenActionPerformed(evt);
            }
        });

        ButtonUpload.setBackground(new java.awt.Color(204, 255, 204));
        ButtonUpload.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        ButtonUpload.setText("Backup Now!");
        ButtonUpload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonUploadActionPerformed(evt);
            }
        });

        LabelApiKey.setText("ApiKey:");

        LabelSharedSecret.setText("SharedSecret:");

        LabelUser.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        LabelUser.setText("You don't have a valid Access Token yet, Please request a new Token");

        LabelPhotoDirectory.setText("Photo Directory:");

        ButtonDeleteCredentials.setText("Delete Credentials");
        ButtonDeleteCredentials.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonDeleteCredentialsActionPerformed(evt);
            }
        });

        ButtonSave.setText("Save Options");
        ButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonSaveActionPerformed(evt);
            }
        });

        LabelSyncType.setText("Sync Type:");

        ComboBoxSyncType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "no", "Sync", "FullSync" }));
        ComboBoxSyncType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComboBoxSyncTypeActionPerformed(evt);
            }
        });

        ButtonChooseDirectory.setText("Choose Directory");
        ButtonChooseDirectory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonChooseDirectoryActionPerformed(evt);
            }
        });

        ButtonStop.setBackground(new java.awt.Color(255, 51, 51));
        ButtonStop.setText("Stop Backup");
        ButtonStop.setEnabled(false);
        ButtonStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonStopActionPerformed(evt);
            }
        });

        ProgressBarBackup.setBackground(new java.awt.Color(204, 255, 204));

        LabelStartTime.setText("Started Time:");

        LabelTimeRemaining.setText("Time Remaining:");

        LabelSyncDescription.setText("SyncDescription");
        LabelSyncDescription.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        ButtonRestore.setBackground(new java.awt.Color(204, 255, 255));
        ButtonRestore.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        ButtonRestore.setText("Restore");
        ButtonRestore.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        ButtonRestore.setEnabled(false);
        ButtonRestore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonRestoreActionPerformed(evt);
            }
        });

        CheckBoxRestore.setText("Enable Restore");
        CheckBoxRestore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CheckBoxRestoreActionPerformed(evt);
            }
        });

        ButtonForceStop.setBackground(new java.awt.Color(255, 0, 0));
        ButtonForceStop.setText("Force Stop");
        ButtonForceStop.setEnabled(false);
        ButtonForceStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonForceStopActionPerformed(evt);
            }
        });

        LabelForceStop.setForeground(new java.awt.Color(255, 0, 51));
        LabelForceStop.setText("Wait until current process finish or press Force Stop to immediately kill the process!");

        CheckBoxEnableVideos.setText("Enable Video restore (only for 64 bit Linux or Windows) -> require flickr credentials and Chrome installed");
        CheckBoxEnableVideos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CheckBoxEnableVideosActionPerformed(evt);
            }
        });

        ButtonUpdateFlickrCredentials.setText("Update Flickr credentials and Test");
        ButtonUpdateFlickrCredentials.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonUpdateFlickrCredentialsActionPerformed(evt);
            }
        });

        jSeparator1.setToolTipText("Restore");

        LabelRestoreOptions.setForeground(new java.awt.Color(153, 153, 153));
        LabelRestoreOptions.setText("RESTORE OPTIONS");

        TextPaneLog.setEditable(false);
        jScrollPane2.setViewportView(TextPaneLog);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jSeparator2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(LabelRestoreOptions)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 447, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(ProgressBarBackup, javax.swing.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
                            .addComponent(LabelForceStop, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(LabelStartTime)
                                    .addComponent(LabelTimeRemaining))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(ButtonForceStop, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(ButtonStop, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(ButtonUpload, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(293, 293, 293))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(LabelApiKey, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(LabelSharedSecret, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(LabelSyncType, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(LabelPhotoDirectory, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(TextFieldPhotoDirectory)
                                    .addComponent(TextFieldApiKey)
                                    .addComponent(TextFieldSharedSecret)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(LabelUser, javax.swing.GroupLayout.PREFERRED_SIZE, 448, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 287, Short.MAX_VALUE)))))
                        .addGap(7, 7, 7)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ButtonDeleteCredentials, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(ButtonChooseDirectory, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(ButtonRequestToken, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(17, 17, 17))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(CheckBoxRestore, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addComponent(ButtonRestore, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(ButtonUpdateFlickrCredentials, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(540, 540, 540))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(CheckBoxEnableVideos, javax.swing.GroupLayout.PREFERRED_SIZE, 819, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(96, 96, 96)
                        .addComponent(ComboBoxSyncType, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(LabelSyncDescription, javax.swing.GroupLayout.PREFERRED_SIZE, 559, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ButtonSave, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane2)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabelApiKey)
                    .addComponent(TextFieldApiKey, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ButtonDeleteCredentials))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabelSharedSecret)
                    .addComponent(TextFieldSharedSecret, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ButtonRequestToken))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LabelUser)
                .addGap(37, 37, 37)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TextFieldPhotoDirectory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LabelPhotoDirectory)
                    .addComponent(ButtonChooseDirectory))
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LabelSyncDescription, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ButtonSave, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ButtonUpload, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(LabelSyncType)
                            .addComponent(ComboBoxSyncType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(57, 57, 57)
                        .addComponent(ButtonStop, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(LabelStartTime)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(LabelTimeRemaining))
                    .addComponent(ProgressBarBackup, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabelForceStop)
                    .addComponent(ButtonForceStop))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LabelRestoreOptions)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CheckBoxRestore)
                    .addComponent(ButtonUpdateFlickrCredentials))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CheckBoxEnableVideos)
                    .addComponent(ButtonRestore, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 19, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ButtonRequestTokenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonRequestTokenActionPerformed

        AccessToken = "";
        TokenSecret = "";
        apiKey = TextFieldApiKey.getText();
        sharedSecret = TextFieldSharedSecret.getText();
        boolean connectionOK;
        connectionOK = auth();
        if (connectionOK) {
            ButtonRequestToken.setEnabled(false);
            ButtonUpload.setEnabled(true);
            TextFieldApiKey.setEnabled(false);
            TextFieldSharedSecret.setEnabled(false);
            LabelUser.setText("USER: " + User);
        }

    }//GEN-LAST:event_ButtonRequestTokenActionPerformed

    private void ButtonUploadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonUploadActionPerformed

        sync = ComboBoxSyncType.getSelectedIndex();
        Directory = TextFieldPhotoDirectory.getText();
        StopProcess = false;
        boolean connectionOK;
        connectionOK = auth();
        if (connectionOK) {

            Backup = new backup();
            process = new Thread(Backup);
            process.start();

        }

    }//GEN-LAST:event_ButtonUploadActionPerformed

    private void ButtonDeleteCredentialsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonDeleteCredentialsActionPerformed
        // TODO add your handling code here:
        TextFieldApiKey.setEnabled(true);
        TextFieldSharedSecret.setEnabled(true);
        TextFieldApiKey.setText("");
        TextFieldSharedSecret.setText("");
        AccessToken = "";
        TokenSecret = "";
        apiKey = "";
        sharedSecret = "";
        Username = "";
        Password = "";
        LabelUser.setText("Credentials Deleted!, Please request a new Token");
        ButtonUpload.setEnabled(false);
        ButtonRequestToken.setEnabled(true);
        JOptionPane.showMessageDialog(null, "<html>To confirm crededentials deletion press Save button, otherwise close and reopen the program<br></html>");

    }//GEN-LAST:event_ButtonDeleteCredentialsActionPerformed

    private void ButtonChooseDirectoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonChooseDirectoryActionPerformed
        // TODO add your handling code here:
        JFileChooser fc = new JFileChooser();
        try {
            // TODO add your handling code here:

            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            File dir = new File(Directory);
            if (Directory != null) {
                fc.setCurrentDirectory(new java.io.File(Directory));
            } else {
                fc.setCurrentDirectory(new java.io.File("."));
            }
            int returnVal = fc.showOpenDialog(this);
            if (returnVal == 0) {
                Directory = fc.getSelectedFile().getCanonicalPath();
                TextFieldPhotoDirectory.setText(Directory);
            }
            //  System.out.println(Directory);
        } catch (IOException ex) {
            Message("Error secting Directory -> " + ex.getMessage(),Level.ERROR);
        } catch (java.lang.InternalError ex) {
            //This error occour when the chooser can't open an existing directory
            //If this occour the chooser must open the root directory
            Message("Error secting Directory -> " + ex.getMessage(),Level.ERROR);
            fc.setCurrentDirectory(new java.io.File("."));
            int returnVal = fc.showOpenDialog(this);
            if (returnVal == 0) {
                try {
                    Directory = fc.getSelectedFile().getCanonicalPath();
                } catch (IOException ex1) {
                    Message ("Error choosing directory -> "+ex1.getMessage(),Level.ERROR);
                    //Logger.getLogger(FlickrMassUploader.class.getName()).log(Level.SEVERE, null, ex1);
                }
                TextFieldPhotoDirectory.setText(Directory);
            }
        }
    }//GEN-LAST:event_ButtonChooseDirectoryActionPerformed

    private void ButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonSaveActionPerformed
        // TODO add your handling code here:
        apiKey = TextFieldApiKey.getText();
        sharedSecret = TextFieldSharedSecret.getText();
        Directory = TextFieldPhotoDirectory.getText();
        sync = ComboBoxSyncType.getSelectedIndex();
        
        WritePropertiesFile();
        JOptionPane.showMessageDialog(null, "Options successfully saved!");

    }//GEN-LAST:event_ButtonSaveActionPerformed

    private void ButtonStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonStopActionPerformed
        // TODO add your handling code here:
        StopProcess = true;
       
        ButtonStop.setEnabled(false);
            waitProcess waitprocess = new waitProcess();
            Thread processo = new Thread(waitprocess);
            processo.start();

    }//GEN-LAST:event_ButtonStopActionPerformed

    private void ComboBoxSyncTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ComboBoxSyncTypeActionPerformed
        // TODO add your handling code here:
        if (ComboBoxSyncType.getSelectedIndex() == 0) {
            LabelSyncDescription.setText("<html>With NO option online photos will never be deleted<br>"
                    + "New photos in your computer directory will be uploaded to flickr</html>");
        } else if (ComboBoxSyncType.getSelectedIndex() == 1) {
            LabelSyncDescription.setText("<html>With SYNC option online photos that are no longer present in your local dir will be deleted<br>"
                    + "If exists Photosets online that are not present in your computer will never be deleted<br>"
                    + "The program control the online photosets names with the locals ones<br>"
                    + "New photos in your computer directory will be uploaded to flickr</html>");
        } else if (ComboBoxSyncType.getSelectedIndex() == 2) {
            LabelSyncDescription.setText("<html>With FULLSYNC option online photos that are no longer present in your local dir will be deleted<br>"
                    + "New photos in your computer directory will be uploaded to flickr</html>");
        }
    }//GEN-LAST:event_ComboBoxSyncTypeActionPerformed

    private void CheckBoxRestoreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CheckBoxRestoreActionPerformed
        // TODO add your handling code here:
        if (CheckBoxRestore.isSelected())
            {
                ButtonRestore.setEnabled(true);
            }else ButtonRestore.setEnabled(false);
    }//GEN-LAST:event_CheckBoxRestoreActionPerformed

    private void ButtonForceStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonForceStopActionPerformed
        // TODO add your handling code here:
        process.stop();
        Message("Forced Backup Interruption",Level.INFO);
    }//GEN-LAST:event_ButtonForceStopActionPerformed

    private void ButtonRestoreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonRestoreActionPerformed
        // TODO add your handling code here:
        Directory = TextFieldPhotoDirectory.getText();
        StopProcess = false;
        boolean connectionOK;
        connectionOK = auth();
        if (connectionOK) {
            Restore = new restore();
            process = new Thread(Restore);
            process.start();
        }
    }//GEN-LAST:event_ButtonRestoreActionPerformed

    private void CheckBoxEnableVideosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CheckBoxEnableVideosActionPerformed
        // TODO add your handling code here:
        if (Username==""||Username==null)
            {
                JOptionPane.showMessageDialog(null, "<html>To download Videos you must save your Username and Password<br></html>");
                Username = JOptionPane.showInputDialog("Please insert your Flickr Username");
                Password = JOptionPane.showInputDialog("Please insert your Flickr Password");
                WritePropertiesFile();
            }
        if (CheckBoxEnableVideos.isSelected()) DownloadVideo="Yes";
        
        
    }//GEN-LAST:event_CheckBoxEnableVideosActionPerformed

    private void ButtonUpdateFlickrCredentialsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonUpdateFlickrCredentialsActionPerformed
        
            // TODO add your handling code here:
            
                                    if (OS.lastIndexOf("windows")>-1){
                System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
            }else
                     {
                        System.setProperty("webdriver.chrome.driver", "chromedriver"); 
                     }     
            Username = JOptionPane.showInputDialog("Please insert your Flickr Username",Username);
            Password = JOptionPane.showInputDialog("Please insert your Flickr Password",Password);
            JOptionPane.showMessageDialog(null, "<html>Press ok to test credentials<br>"
                    + "and be patient it will take also a minute to test</html>");
            /*System.setProperty("webdriver.gecko.driver", "geckodriver.exe");
            FirefoxBinary firefoxBinary = new FirefoxBinary();
            firefoxBinary.addCommandLineOptions("--headless");
            FirefoxOptions firefoxOptions = new FirefoxOptions();
            firefoxOptions.setBinary(firefoxBinary);
            FirefoxDriver driver = new FirefoxDriver(firefoxOptions);*/
                       //System.setProperty("webdriver.gecko.driver", "geckodriver.exe");
            
            //System.setProperty("https.protocols", "TLSv1.1");
          /*  FirefoxOptions firefoxOptions = new FirefoxOptions();
            firefoxOptions.setHeadless(true);
            FirefoxDriver driver = new FirefoxDriver(firefoxOptions);*/
            Message("Test Flickr Credentials Started",Level.INFO);
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.setHeadless(true);
             WebDriver driver = new ChromeDriver(chromeOptions);
     
            // LOGIN
             try {
            driver.get("https://www.flickr.com/signin");
            //Message(driver.getCurrentUrl());
            WebElement username = driver.findElement(By.name("username"));
            username.sendKeys(Username);
            WebElement button = driver.findElement(By.name("signin"));
            button.click();
            Thread.sleep(5000);
            WebElement password = driver.findElement(By.name("password"));
            password.sendKeys(Password);
            WebElement buttonpassword = driver.findElement(By.name("verifyPassword"));
            buttonpassword.click();
            Thread.sleep(5000);
           /* driver.navigate().to("https://www.flickr.com/signin");
            
            Thread.sleep(3000);*/
            //Message(driver.getCurrentUrl());
            if (!driver.getCurrentUrl().contains("https://www.flickr.com/"))
            {
                driver.quit();
                JOptionPane.showMessageDialog(null, "<html>Flickr login Failed, please Try again<br></html>");
                Message("Flickr login Failed, please Try again",Level.INFO);
                
            }
            else{
                driver.quit();
                JOptionPane.showMessageDialog(null, "<html>Flickr login OK!!!<br></html>");
                Message("Flickr login OK!!!",Level.INFO);
                WritePropertiesFile();
            } 
        } catch (InterruptedException ex) {
            //Logger.getLogger(FlickrMassUploader.class.getName()).log(Level.SEVERE, null, ex);
            Message ("Error Sleeping -> "+ex.getMessage(),Level.ERROR);
            driver.quit();
        }  catch (org.openqa.selenium.WebDriverException ex) {
            //Logger.getLogger(FlickrMassUploader.class.getName()).log(Level.SEVERE, null, ex);
            Message ("Error retrieving information from site: -> "+ex.getMessage(),Level.ERROR);
            driver.quit();
        }
    }//GEN-LAST:event_ButtonUpdateFlickrCredentialsActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FlickrMassUploader.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FlickrMassUploader.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FlickrMassUploader.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FlickrMassUploader.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            FlickrMassUploader gr;

            public void run() {
                if (args.length == 0) {
                    new FlickrMassUploader().setVisible(true);
                } else {
                    GraphicsOn = false;
                    if (args[0].equalsIgnoreCase("/backup")) {
                        gr = new FlickrMassUploader();
                        gr.Message("Program started in batch mode with BACKUP option",Level.INFO);
                        StopProcess = false;
                        boolean connectionOK;
                        connectionOK = gr.auth();
                        if (connectionOK) {
                            gr.BackupYourFiles();
                        }
                        gr.dispose();
                    } else if (args[0].equalsIgnoreCase("/restore")) {
                        gr = new FlickrMassUploader();
                        gr.Message("Program started in batch mode with RESTORE option",Level.INFO);
                        StopProcess = false;
                        boolean connectionOK;
                        connectionOK = gr.auth();
                        if (connectionOK) {
                            gr.RestoreYourFiles();
                        }
                        gr.dispose();
                    } else

                    {
                        System.out.println("Please launch program without argument for graphics or with /backup or /restore argument for batch option");
                    }

                }

            }
        });
    }

    public boolean auth() {

        //If AccessToken or TokenSecret are missing then you need to get them
        //else we try to authenticate
        boolean connectionOK = true;
        if (AccessToken == null || TokenSecret == null || AccessToken.equalsIgnoreCase("") || TokenSecret.equalsIgnoreCase("")) {
            connectionOK =FirstAuth();
        } else {
            flickr = new Flickr(apiKey, sharedSecret, new REST());
            authInterface = flickr.getAuthInterface();
            try {
                auth = authInterface.checkToken(AccessToken, TokenSecret);
                Nsid = auth.getUser().getId();
                User = auth.getUser().getUsername();
                // This token can be used until the user revokes it.
                Message("Username: " + auth.getUser().getUsername(),Level.INFO);

            } catch (FlickrException ex) {
                Message("Auth2 error: " + ex.getErrorMessage() + " -> " + ex.getErrorCode(),Level.ERROR);
                Message("Request new authentication in progress...",Level.ERROR);
                //if is not possible to have authentication you need new credentials
                connectionOK = FirstAuth();
            } catch (com.flickr4java.flickr.FlickrRuntimeException ex) {
                Message("Auth2 error: " + ex.getMessage() + " -> Probably you have no internet connection or the site is not avaiable",Level.ERROR);
                connectionOK = false;

            }
        }
        return connectionOK;
    }

    public boolean FirstAuth() {
        boolean connectionOK = true;
        //If apiKey or sharedSecret are missing then you need to get them
        //else we try to authenticate
        apiKey = TextFieldApiKey.getText();
        sharedSecret = TextFieldSharedSecret.getText();
        if (apiKey == null || sharedSecret == null || apiKey.equalsIgnoreCase("") || sharedSecret.equalsIgnoreCase("")) {
            //pop up that alert you to insert apikey and shared secret then save
            JOptionPane.showMessageDialog(null, "<html>Please insert apiKey and sharedSecret Values and then Save before proceeding<br>"
                    + "If you don't have any apikey than follow this link <a href=\"url\">https://www.flickr.com/services/apps/create/apply<a><br>"
                    + "and create your own keys</html>");
            connectionOK=false;

        } else {
            flickr = new Flickr(apiKey, sharedSecret, new REST());
            Flickr.debugStream = false;
            authInterface = flickr.getAuthInterface();

            Token token = authInterface.getRequestToken();
            Message("token: " + token,Level.INFO);

            String url = authInterface.getAuthorizationUrl(token, Permission.DELETE);
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI(url));
                } catch (URISyntaxException ex) {
                    Message("Error Getting URL: " + ex.getMessage(),Level.ERROR);
                } catch (IOException ex) {
                    Message("Error Getting URL: " + ex.getMessage(),Level.ERROR);
                }
            }

            String n = JOptionPane.showInputDialog("Please accept authetincation request from your broswer and then paste here your Confirmation Code:");
            ConfirmationCode = n;
            Message("Pasted Confirmation Code:" + ConfirmationCode,Level.INFO);
            String tokenKey = ConfirmationCode;
            if (ConfirmationCode != null) {
                try {
                    Token requestToken = authInterface.getAccessToken(token, new Verifier(tokenKey));
                    auth = authInterface.checkToken(requestToken);
                    // This token can be used until the user revokes it.
                    AccessToken = requestToken.getToken();
                    TokenSecret = requestToken.getSecret();
                    Nsid = auth.getUser().getId();
                    User = auth.getUser().getUsername();
                    //Message("Token: " + requestToken.getToken());
                    //Message("Secret: " + requestToken.getSecret());
                    //Message("nsid: " + auth.getUser().getId());
                    //Message("Realname: " + auth.getUser().getRealName());
                    Message("Username: " + auth.getUser().getUsername(),Level.INFO);
                    //Message("Permission: " + auth.getPermission().getType());
                    WritePropertiesFile();
                    Message("Authentication success",Level.INFO);
                    JOptionPane.showMessageDialog(null, "Authentication Success, now you can upload your files");
                } catch (FlickrException ex) {
                    Message("First Authentication error: " + ex.getErrorMessage() + " -> " + ex.getErrorCode(),Level.ERROR);
                    JOptionPane.showMessageDialog(null, "Confirmation Code:" + ConfirmationCode + " seems to be wrong, please retry the authentication");
                    connectionOK = false;
                } catch (org.scribe.exceptions.OAuthException ex) {
                    Message("First Authentication error: " + ex.getMessage(),Level.ERROR);
                    JOptionPane.showMessageDialog(null, "Confirmation Code:" + ConfirmationCode + " seems to be wrong, please retry the authentication");
                    connectionOK = false;
                }
            } else {
                JOptionPane.showMessageDialog(null, "No Confirmation Code recognized, please retry authentication");
                connectionOK = false;
            }
        }
        return connectionOK;
    }
    
    
    
    
    
    
    
    
     private void DownloadVideos(int[] conta,int numeroMedia, long InitialTime) throws Exception{
          
            //System.setProperty("webdriver.gecko.driver", "geckodriver.exe");
                        if (OS.lastIndexOf("windows")>-1){
                System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
            }else
                     {
                        System.setProperty("webdriver.chrome.driver", "chromedriver"); 
                     }       
            //System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
            Message("Wait... i'm doing the login to Flickr.... it can take a few minutes",Level.INFO);
            System.setProperty("https.protocols", "TLSv1.1");
          /*  FirefoxOptions firefoxOptions = new FirefoxOptions();
            firefoxOptions.setHeadless(true);
            FirefoxDriver driver = new FirefoxDriver(firefoxOptions);*/
             
   
     ChromeOptions chromeOptions = new ChromeOptions();
     chromeOptions.setHeadless(true);
     WebDriver driver = new ChromeDriver(chromeOptions);
     
   
     // LOGIN
     try {
		//driver.get("https://login.yahoo.com");
                driver.get("https://www.flickr.com/signin");
                WebElement username = driver.findElement(By.name("username"));
                username.sendKeys(Username);
                WebElement button = driver.findElement(By.name("signin"));
                button.click();
                Thread.sleep(5000);
                WebElement password = driver.findElement(By.name("password"));
                password.sendKeys(Password);
                WebElement buttonpassword = driver.findElement(By.name("verifyPassword"));
                buttonpassword.click();
                Thread.sleep(5000);
                //driver.navigate().to("https://www.flickr.com/signin");
                
               // Thread.sleep(5000);
                if (!driver.getCurrentUrl().contains("https://www.flickr.com/"))
                    {
                           Message ("Flickr login Failed, please press button 'Update Flickr credentials and Test' and try again",Level.INFO);
                           Message ("FLICKR VIDEO DOWNLOAD FAILED!!!",Level.INFO);
                }
                else{
                Message ("Flickr login OK!!!",Level.INFO);
                
                
                // Send cookies to apache http client to mantain the authentication 
                Set<Cookie> seleniumCookies = driver.manage().getCookies();
                CookieStore cookieStore = new BasicCookieStore();
                

                for(Cookie seleniumCookie : seleniumCookies){
                    BasicClientCookie basicClientCookie =
			new BasicClientCookie(seleniumCookie.getName(), seleniumCookie.getValue());
                    basicClientCookie.setDomain(seleniumCookie.getDomain());
                    basicClientCookie.setExpiryDate(seleniumCookie.getExpiry());
                    basicClientCookie.setPath(seleniumCookie.getPath());
                    cookieStore.addCookie(basicClientCookie);
                    }
                
                
               
                DefaultHttpClient httpClient = new DefaultHttpClient();
                httpClient.setCookieStore(cookieStore);
                //Message("fino a qui ci sono");
               // Thread.sleep(5000);
                driver.quit();
                
                videotodownload.forEach((k, v)
                -> {
                      if (!StopProcess) {
                    try {
                         
                        conta[0]++;
                        
                        HttpGet httpGet = new HttpGet("https://www.flickr.com/video_download.gne?id="+k);
                        Message("Downloading video with ID "+k+" from: " + "https://www.flickr.com/video_download.gne?id="+k+" to "+v,Level.INFO);
                        HttpResponse response = httpClient.execute(httpGet);
                        
                        HttpEntity entity = response.getEntity();
                        if (entity != null) {
                            File TempOutputFile = new File(v+"_temp");
                            File OutputFile = new File(v);
                            InputStream inputStream = entity.getContent();
                            FileOutputStream fileOutputStream = new FileOutputStream(TempOutputFile);
                            int read = 0;
                            byte[] bytes = new byte[1024];
                            while ((read = inputStream.read(bytes)) != -1) {
                                fileOutputStream.write(bytes, 0, read);
                            }
                            fileOutputStream.close();
                            Files.copy(TempOutputFile.toPath(), OutputFile.toPath());
                            TempOutputFile.delete();
                            String Date= remoteLastModifiedDate.get(k);
                            if (Date!=null&&!Date.equalsIgnoreCase("")) OutputFile.setLastModified(sdf.parse(Date).getTime());
                            
                            Message("Download of " +OutputFile.getName()+ " comleted! ---- File Lenght : "+OutputFile.length() + " bytes. ",Level.INFO);
                            //Download of 186.JPG completed!
                        }
                        else {
                            Message("Download failed!",Level.INFO);
                        }
                        if (GraphicsOn) {
                            ProgressBarBackup.setValue(conta[0]);
                            ProgressBarBackup.setString(conta[0] + "/" + numeroMedia);
                            LabelTimeRemaining.setText("Time Remaining: "
                                    + String.valueOf(TimeUnit.MILLISECONDS.toMinutes(((System.currentTimeMillis() - InitialTime) / (long) conta[0]) * (numeroMedia - conta[0]))) + " minutes");
                        }
                        
                        
                        
                    } catch (IOException ex) {
                        Message ("Error Downloading Videos -> "+ex.getMessage(),Level.ERROR);
                    }catch (ParseException ex) {
                        Message ("Error Downloading Videos -> "+ex.getMessage(),Level.ERROR);
                            }
                    } 
                
                });
                }

        } catch (org.openqa.selenium.WebDriverException ex) {
            Message ("Error retrieving information from site: -> "+ex.getMessage(),Level.ERROR);
            driver.quit();
            //service.stop();
        }
     // driver.quit();
     //service.stop();
           }     
    
    
    
    
    
    
    
    
    
    
    
    
  
    
    public String Crypt(String text) 
    {
        String CryptedString="";
        try 
        {
         
            //String key = "Bar12345Bar12345"; // 128 bit key
            // Create key and cipher
            Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            // encrypt the text
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] encrypted = cipher.doFinal(text.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b: encrypted) {
                sb.append((char)b);
            }
            // the encrypted String
            String enc = sb.toString();
            CryptedString=enc;


        }
        catch(Exception e) 
        {
            //Message("Error Crypting String : "+e.getMessage());
            System.out.println("Error Crypting String : "+e.getMessage());
            CryptedString=null;
        }
        return CryptedString;
    }
    
    
        public String Decrypt(String text) 
    {
        String DecryptedString="";
       try 
        {
         
            //String key = "Bar12345Bar12345"; // 128 bit key
            // Create key and cipher
            Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            // encrypt the text
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);

                        // now convert the string to byte array
            // for decryption
            byte[] bb = new byte[text.length()];
            for (int i=0; i<text.length(); i++) {
                bb[i] = (byte) text.charAt(i);
            }

            // decrypt the text
            cipher.init(Cipher.DECRYPT_MODE, aesKey);
            String decrypted = new String(cipher.doFinal(bb));
            DecryptedString=decrypted;
           

        }
        catch(Exception e) 
        {
          // Message("Error Decrypting String : "+e.getMessage());
           System.out.println("Error Decrypting String : "+e.getMessage());
           DecryptedString=null;
        }
        return DecryptedString;
    }
    
    
    
    
    
    
    
    
    

    private String makeSafeFilename(String input) {
        byte[] fname = input.getBytes();
        byte[] bad = new byte[]{'\\', '/', '"', '*'};
        byte replace = '_';
        for (int i = 0; i < fname.length; i++) {
            for (byte element : bad) {
                if (fname[i] == element) {
                    fname[i] = replace;
                }
            }
            if (fname[i] == ' ') {
                fname[i] = '_';
            }
        }
        String st = new String(fname);
        if (st.length() > 0) {
            //if the first character of the name is an underscore i'll delete it
            st = st.substring(0, 1).replaceAll("_", "") + st.substring(1);
        }

        return st;
    }

    public static void saveFile(URL url, String file) throws IOException {
    System.out.println("opening connection");
    InputStream in = url.openStream();
    FileOutputStream fos = new FileOutputStream(new File(file));

    System.out.println("Reading file...");
    int length = -1;
    byte[] buffer = new byte[1024]; // Buffer for portion of data from

    // Connection
    while ((length = in.read(buffer)) > -1) {
        fos.write(buffer, 0, length);
    }

    fos.close();
    in.close();
    System.out.println("File was downloaded");
}
    
    
    
    public void DownloadPhoto(String photoID,File newFile)throws Exception,FlickrException{
            if (!StopProcess) {

                PhotosInterface photoI=flickr.getPhotosInterface();
                Photo p = photoI.getPhoto(photoID);
                
                // if file in not a video i'll download
                // unfortunately video download api seems to be bugged
                File tempFile=new File(newFile.getCanonicalPath()+"_temp");
                //System.out.println(p.getLargeUrl());
                if (!p.getMedia().equalsIgnoreCase("video")){
                Message("Downloading photo with ID "+photoID + " to " + newFile.getCanonicalPath(),Level.INFO);
                BufferedInputStream inStream = new BufferedInputStream(photoI.getImageAsStream(p, Size.ORIGINAL));
                FileOutputStream fos = new FileOutputStream(tempFile);
                int read;
                while ((read = inStream.read()) != -1) {
                    if (StopProcess) break;
                    fos.write(read);
                }
                fos.flush();
                fos.close();
                inStream.close();
                Message("Download of " +newFile.getName()+ " comleted! ---- File Lenght : "+newFile.length() + " bytes. ",Level.INFO);
                Files.copy(tempFile.toPath(), newFile.toPath());
                tempFile.delete();
                if (StopProcess) tempFile.delete();
            }else
                   {
                       
             /*        Message(getOriginalVideoUrl(flickr, photoID));  
                     Message("Downloading " + newFile.getName());
                BufferedInputStream inStream = new BufferedInputStream(photoI.getImageAsStream(p, Size.VIDEO_ORIGINAL));
                FileOutputStream fos = new FileOutputStream(tempFile);
                int read;
                while ((read = inStream.read()) != -1) {
                    if (StopProcess) break;
                    fos.write(read);
                }
                fos.flush();
                fos.close();
                inStream.close();
                Message("Download of " + newFile.getName()+" completed!");
                Files.copy(tempFile.toPath(), newFile.toPath());
                tempFile.delete(); 
                Thread.sleep(100000);*/
                       
                   } 
          }      

     // url=("https://www.flickr.com/video_download.gne?id="+photoID);
   
            
    }
    
    public String uploadfile(String filename, String TitoloFoto, String Album) throws Exception {
        if (!StopProcess) {
            String photoId;

            UploadMetaData metaData = new UploadMetaData();

            String title = TitoloFoto;
            metaData.setTitle(title);
            metaData.setFilename(TitoloFoto);

            boolean uploadableFile = VerifyExtension(filename);

            if (uploadableFile) {
                Uploader uploader = flickr.getUploader();
                File f = new File(filename);
                long fileSizeMB = f.length()/1024;
                metaData.setDescription("OrigFileName:=" + filename.substring(Directory.length()) + "\n"+"DateLastModified:="+sdf.format(f.lastModified())+"\n");
                    Message ("File Size: "+String.valueOf(fileSizeMB)+" KB",Level.INFO);
                    photoId = uploader.upload(f, metaData);
                    Message(" File : " + filename + " uploaded: photoId = " + photoId,Level.INFO);

                PhotosetsInterface psi = flickr.getPhotosetsInterface();

                if (remotealbums.get(Album) == null) {
                    //If don't exist the photo album i'll create it and add photo to album
                    Photoset Pset = psi.create(Album, Album, photoId);
                    remotealbums.put(Album, Pset.getId());
                } else {
                    // else i'll only add the photo to the appropriate album    
                    psi.addPhoto(remotealbums.get(Album), photoId);
                }
            } else {
                photoId = null;
            }

            return (photoId);
        } else {
            return null;
        }
    }
    

    public void RemotePhotoList() {
        // if button stop is pressed StopProcess become true and we have to termiate all tasks
            remotephotoswithdata = new HashMap<>();
            remotephotoswithoutdata = new HashMap<>();
            remoteLastModifiedDate = new HashMap<>();
            remoteoriginalformat = new HashMap<>();
            
        if (!StopProcess) {

            try {
                int NPhotos=0;
                int NAlbums=0;
                PhotosInterface photoI=flickr.getPhotosInterface();
                PhotosetsInterface psi = flickr.getPhotosetsInterface();
                
                //create an itarator to list albums
                //sets is the list of albums
                Iterator sets = psi.getList(Nsid).getPhotosets().iterator();
                

                while (sets.hasNext()) {
                    
                    // if button stop is pressed StopProcess become true and we have to termiate all tasks
                    if (StopProcess) {
                        break;
                    }
                    //Pset it's the selected album
                    Photoset Pset = (Photoset) sets.next();
                    String Album=Pset.getTitle();
                   // Message("Retrieving Album "+Album+" photoList");


                    remotealbums.put(Pset.getTitle(), Pset.getId());
                   // remoteLastModifiedDate
                    
                    //photos is the list of photos on selected album
                    Set<String> extras = new HashSet<String>();
                    extras.add("description");
                    extras.add(Extras.ORIGINAL_FORMAT);
                    int photosperpage=500;


                    
                    //PhotoList photos = psi.getPhotos(Pset.getId(), 100000, 1);
                    
                    PhotoList photos = psi.getPhotos(Pset.getId(), extras, 0, photosperpage, 1);
                    int pages=photos.getPages();                
                    for (int x = 0; x < pages; x++) {
                        if (x>0){
                          photos = psi.getPhotos(Pset.getId(), extras, 0, photosperpage, x+1);  
                        }
                    for (int i = 0; i < photos.size(); i++) {
                        // if button stop is pressed FermaProcessi become true and we have to termiate all tasks
                        if (StopProcess) {
                            break;
                        }
                        //foto is the selected photo
                        
                        Photo foto = (Photo) photos.get(i);
                        //System.out.println(foto.getOriginalFormat());
                        String photoID=foto.getId();
                        String Date="";
                        String Filename="";
                        String Description=foto.getDescription();
                        if (Description!=null)
                            {
                                String lines[]=Description.split("\n");
                                for (int y=0;y<lines.length;y++)
                                    {
                                         if (lines[y].split(":=")[0].equalsIgnoreCase("DateLastModified")&&lines[y].split(":=").length>1) 
                                        {
                                        Date=lines[y].split(":=")[1];
                                        }
                                        if (lines[y].split(":=")[0].equalsIgnoreCase("OrigFileName")&&lines[y].split(":=").length>1) 
                                        {
                                        Filename=lines[y].split(":=")[1];
                                        Filename=ReturnFullFileNameOSDepending(Filename);
                                        } 
                                    }
                                if (!Date.equalsIgnoreCase("")&&!Filename.equalsIgnoreCase(""))
                                    {
                                        if (remotephotoswithdata.get(Album + "|" + Filename)!=null) 
                                            {
                                                Message("WARNING found duplicate on Album:"+Album+" PhotoID:"+photoID+" Filename:"+Filename,Level.INFO);
                                            }
                                        else
                                            {
                                                remotephotoswithdata.put(Album + "|" + Filename, photoID);
                                                remoteLastModifiedDate.put(photoID, Date);
                                            }
                                    }
                                else
                                    {
                                        remotephotoswithoutdata.put(Album + "|" + foto.getId(), photoID);
                                        remoteoriginalformat.put(photoID, foto.getOriginalFormat());
                                    }
                            }
                        else
                            {
                                remotephotoswithoutdata.put(Album + "|" + foto.getId(), photoID);
                                remoteoriginalformat.put(photoID, foto.getOriginalFormat());
                            }
                        
                        NPhotos++;
                        
                    }
                }

                    
                    NAlbums++;
                    
                }

                        int photosperpage=500;
                        PhotoList photosNoAlbum = photoI.getNotInSet(photosperpage, 1);
                        int pages=photosNoAlbum.getPages();
                        //System.out.println(pages);
                        for (int x = 0; x < pages; x++) {
                        if (x>0){
                          photosNoAlbum = photoI.getNotInSet(photosperpage, x+1);  
                        }
                        for (int i = 0; i < photosNoAlbum.size(); i++) {
                        Photo foto = (Photo) photosNoAlbum.get(i);
                        remotephotoswithoutdata.put("NoAlbum" + "|" + foto.getId(), foto.getId());
                        remoteoriginalformat.put(foto.getId(), foto.getOriginalFormat());
                        NPhotos++;
                    }
                        }
                        
                        Message("Number of Flickr Albums="+NAlbums,Level.INFO);
                        Message("Number of Flickr Photos="+NPhotos,Level.INFO);
            } catch (FlickrException ex) {
                Message("Error getting Photo List on Flickr -> " + ex.getErrorCode() + ":" + ex.getErrorMessage(),Level.ERROR);
            }

        }
    }
    
    
    
    public void RemotePhotoList2() {
        // if button stop is pressed StopProcess become true and we have to termiate all tasks
            
            remotephotoswithdata = new HashMap<>();
            remotephotoswithoutdata = new HashMap<>();
            remoteLastModifiedDate = new HashMap<>();
            remoteoriginalformat = new HashMap<>();
            
        if (!StopProcess) {

            try {
                NRPhotos=0;
                NRAlbums=0;
                NRnoBPhotos=0;
                PhotosInterface photoI=flickr.getPhotosInterface();
                PhotosetsInterface psi = flickr.getPhotosetsInterface();
                
                //create an itarator to list albums
                //sets is the list of albums
                Iterator sets = psi.getList(Nsid).getPhotosets().iterator();
                

                while (sets.hasNext()) {
                              
                    
                    //if concurrent process is highr then the value wait until some process finishing
                                while(NumProcess>ConcurrentProcess-1){
                                try {
                                Thread.sleep(100);
                                } catch (InterruptedException ex) {
                                Message("Error Sleeping : "+ex.getMessage(),Level.ERROR);
                                }
                                }  
                                

                    
                                Photoset Pset = (Photoset) sets.next();

                                remotealbums.put(Pset.getTitle(), Pset.getId());
                                
                                RetrievePhotos RP = new RetrievePhotos();
                                RP.setPsetID(Pset.getId());
                                RP.setAlbum(Pset.getTitle());
                                Thread processo = new Thread(RP);
                                processo.start();
                                
                                //take some time to start the process
                                try {
                                Thread.sleep(100);
                                } catch (InterruptedException ex) {
                                Message("Error Sleeping : "+ex.getMessage(),Level.ERROR);
                                }
                                
                                
                                

                                
                                

                    

                }
                                


                                //wait until all process finish
                                while(NumProcess!=0){
                                try {
                                Thread.sleep(100);
                                } catch (InterruptedException ex) {
                                Message("Error Sleeping : "+ex.getMessage(),Level.ERROR);
                                }
                                }
                                
                
                
                
                

                        int photosperpage=500;
                        PhotoList photosNoAlbum = photoI.getNotInSet(photosperpage, 1);
                        int pages=photosNoAlbum.getPages();
                        //System.out.println(pages);
                        for (int x = 0; x < pages; x++) {
                        if (x>0){
                          photosNoAlbum = photoI.getNotInSet(photosperpage, x+1);  
                        }
                        for (int i = 0; i < photosNoAlbum.size(); i++) {
                        Photo foto = (Photo) photosNoAlbum.get(i);
                        remotephotoswithoutdata.put("NoAlbum" + "|" + foto.getId(), foto.getId());
                        remoteoriginalformat.put(foto.getId(), foto.getOriginalFormat());
                        if (foto.getMedia().equalsIgnoreCase("video")) remotemedia.put(foto.getId(), foto.getMedia());
                        
                        NRPhotos++;
                    }
                        }
                        
                        Message("Number of Flickr Albums="+NRAlbums,Level.INFO);
                        Message("Number of Flickr Media backuped with FlickrMassUploader="+NRPhotos,Level.INFO);
                        Message("Number of Flickr Media NOT backuped with FlickrMassUploader="+NRnoBPhotos,Level.INFO);
                        
            } catch (FlickrException ex) {
                Message("Error getting Photo List on Flickr -> " + ex.getErrorCode() + ":" + ex.getErrorMessage(),Level.ERROR);
            }  

        }
    }
    
    
        public void UpdateDateTags(String PHOTOID,String DATE) {
        // if button stop is pressed StopProcess become true and we have to termiate all tasks
        if (!StopProcess) {
            try {

                PhotosInterface photoI=flickr.getPhotosInterface();
                Message ("Photo:"+photoI.getPhoto(PHOTOID).getTitle()+" ID:"+PHOTOID+" with old style LastModifiedDate Format, I'll upgrade it!",Level.INFO);
                
                            Collection<Tag> tags=photoI.getPhoto(PHOTOID).getTags();

                        for (Tag tag : tags) {
                            if (tag.getRaw().split(":=")[0].equalsIgnoreCase("DateLastModified")&&tag.getRaw().split(":=").length>1) 
                            {
                                photoI.removeTag(tag.getId());
                                Message ("Old Tag Deleted -> "+tag.getRaw(),Level.INFO);

                            }
                            }
                String tags1[]=new String[1];
                tags1[0]="DateLastModified:="+DATE;
                photoI.addTags(PHOTOID, tags1);
                Message ("New Tag Created -> "+tags1[0],Level.INFO);

                
                    
            } catch (FlickrException ex) {
                Message("Error in tag update -> " + ex.getErrorCode() + ":" + ex.getErrorMessage(),Level.ERROR);
            }

        }
    }
    
       
    
    public void RestoreYourFiles() {

        // if button stop is pressed StopProcess become true and we have to termiate all tasks
        if (!StopProcess) {
            Long InitialTime = System.currentTimeMillis();
            Message("Restore Started",Level.INFO);
            LabelTimeRemaining.setText("Time Remaining:");
            remotealbums = new HashMap<>();
            phototoupload = new HashMap<>();
            localalbums = new HashMap<>();
            localphotos = new HashMap<>();
            phototodownload = new HashMap<>();
            videotodownload = new HashMap<>();
            remotemedia = new HashMap<>(); 
           // remotePhotosToDelete = new HashMap<>();

            //authentication    
            RequestContext rc = RequestContext.getRequestContext();
            rc.setAuth(auth);
            Message("Retrieving Remote Photo List",Level.INFO);
            RemotePhotoList2();
            remotephotoswithdata.forEach((k, v)
                -> {
                try {
                 String filename="";
                 filename=Directory+k.substring(k.lastIndexOf("|")+1);
                File newFile = new File(filename);
                // This command create subdirs that not exists in localpath
                new File(newFile.getParent()).mkdirs();
                // if fileName don't Exist or LastModified Date was different the file was downloaded
                if (!newFile.exists()){
                    
                    if (remotemedia.get(v)==null)
                        {
                    phototodownload.put(v, filename);
                    }else
                         {
                             videotodownload.put(v, filename);
                             
                        }
                    
                   } 

                    } 
                catch (Exception ex) {
                        Message("Error verifiing files:" + v + "   ->   " + ex.getMessage(),Level.ERROR);
                    }
                     
                });
                remotephotoswithoutdata.forEach((k, v)
                -> {
                try {
                    String filename="";
                    String estensione=remoteoriginalformat.get(v);
                    filename=Directory+"/NoBackupPhoto/"+k.substring(0, k.lastIndexOf("|"))+"/"+k.substring(k.lastIndexOf("|")+1)+"."+estensione;
                    File newFile = new File(filename);
                    // This command create subdirs that not exists in localpath
                    new File(newFile.getParent()).mkdirs();
                    // if fileName don't Exist or LastModified Date was different the file was downloaded
                if (!newFile.exists()){
                    
                                        if (remotemedia.get(v)==null)
                        {
                    phototodownload.put(v, filename);
                    }else
                         {
                             videotodownload.put(v, filename);
                        }

                    
                   } 

                    } 
                catch (Exception ex) {
                        Message("Error verifiing files:" + v + "   ->   " + ex.getMessage(),Level.ERROR);
                    }
                     
                });
                
                
                
                int numeroMedia = phototodownload.size()+videotodownload.size();
                if (GraphicsOn) {
                    ProgressBarBackup.setMaximum(numeroMedia);
                    ProgressBarBackup.setValue(0);
                    ProgressBarBackup.setString("0/" + numeroMedia);
                    ProgressBarBackup.setStringPainted(true);
                    LabelStartTime.setText("Started Time: "
                            + String.valueOf(new Timestamp(InitialTime)));
                }
                //Message("Number photo on your computer -> " + localphotos.size());
                Message("Number of photo to download -> " + phototodownload.size(),Level.INFO);
                Message("Number of video to download -> " + videotodownload.size(),Level.INFO);
                
                final int[] count = {0};
                if (phototodownload.size()>0) Message("Start Downloading Photos",Level.INFO);
                phototodownload.forEach((k, v)
                -> {
                    count[0]++;
                    try {
                    String Date= remoteLastModifiedDate.get(k);
                    File file=new File (v);
                    DownloadPhoto(k,file);
                    if (Date!=null&&!Date.equalsIgnoreCase("")) file.setLastModified(sdf.parse(Date).getTime());
                    if (GraphicsOn) {
                            ProgressBarBackup.setValue(count[0]);
                            ProgressBarBackup.setString(count[0] + "/" + numeroMedia);
                            LabelTimeRemaining.setText("Time Remaining: "
                                    + String.valueOf(TimeUnit.MILLISECONDS.toMinutes(((System.currentTimeMillis() - InitialTime) / (long) count[0]) * (numeroMedia - count[0]))) + " minutes");
                        }
                } catch (FlickrException ex) {
                        Message("Error downloading file:" + k + "   ->   " + ex.getErrorMessage()+"    "+ex.getMessage(),Level.ERROR);
                       // Message("Error downloading file:" + v + "   ->   " + ex.getMessage());
                    }
                    catch (Exception ex) {
                    Message("Error downloading file:" + k + "   ->   " + ex.getMessage(),Level.ERROR);
                }
                    
                    
                    
                
                });
                if (phototodownload.size()>0) Message("Downloading Photos Finished!",Level.INFO);
                
                if (DownloadVideo.equalsIgnoreCase("Yes")&&videotodownload.size()>0)
                {
                try {
                    if (videotodownload.size()>0) Message("Start Downloading Videos",Level.INFO);
                    DownloadVideos(count,numeroMedia,InitialTime);
                    if (videotodownload.size()>0) Message("Downloading Videos Finished!",Level.INFO);
                } catch (Exception ex) {
                    //Logger.getLogger(FlickrMassUploader.class.getName()).log(Level.SEVERE, null, ex);
                    Message("Error downloading videos from flickr -> "+ex.getMessage(),Level.ERROR);
                }
                }
                
                
            
            if (StopProcess) {
            Message("Restore Stopped by User!",Level.INFO);
            Message("-------------------------------------------------",Level.INFO);
            if (GraphicsOn) {
                LabelTimeRemaining.setText("RESTORE STOPPED BY USER!!!");

            }
        } else {
            Message("Restore Finished!",Level.INFO);
            Message("-----------------------------------------------",Level.INFO);
            if (GraphicsOn) {
                LabelTimeRemaining.setText("RESTORE FINISHED at "+String.valueOf(new Timestamp(System.currentTimeMillis())));
                JOptionPane.showMessageDialog(null, "RESTORE FINISHED!!!");
            }
        }
                


            
    }
        }
    
    
      public String ReturnFullFileNameOSDepending(String FILENAME)  {
            String NewName="";
          
            if (OS.lastIndexOf("windows")>-1){
                NewName=FILENAME.replaceAll("/", "\\\\");
            }
            else
            {
                NewName=FILENAME.replaceAll("\\\\", "/");
            }
            
            
		return NewName;
}
        
    
    private static String getOriginalVideoUrl(Flickr flickr, String photoId) throws IOException, FlickrException {
	String siteUrl = null;
	for (Size size : (Collection<Size>) flickr.getPhotosInterface().getSizes(photoId, true)) {
            System.out.println(size.getSource());
                if (size.getSource().contains("/site/orig"))
			siteUrl = size.getSource();
	}
		return siteUrl;
}
    
    
    

    public void BackupYourFiles() {

        // if button stop is pressed StopProcess become true and we have to termiate all tasks
        if (!StopProcess) {
            Message("Backup Started",Level.INFO);
            Message("Directory to sync -> " + Directory,Level.INFO);
            if (sync == 0) {
                Message("Sync Type -> NO",Level.INFO);
            }
            if (sync == 1) {
                Message("Sync Type -> SYNC",Level.INFO);
            }
            if (sync == 2) {
                Message("Sync Type -> FULLSYNC",Level.INFO);
            }

            Long InitialTime = System.currentTimeMillis();
            LabelTimeRemaining.setText("Time Remaining:");
           // remotephotos = new HashMap<>();
            remotealbums = new HashMap<>();
            phototoupload = new HashMap<>();
            localalbums = new HashMap<>();
            localphotos = new HashMap<>();
            remotemedia = new HashMap<>();

            remotePhotosToDelete = new HashMap<>();

            
            //authentication    
            RequestContext rc = RequestContext.getRequestContext();
            rc.setAuth(auth);

            try {
                //Faccio l'elenco degli album e delle foto su flickr

                Message("Retrieving Remote Photo List",Level.INFO);
                RemotePhotoList2();
                //elenco fotolocali non fa altro che compilare l'hasmap fotolocali con l'elenco delle foto sull'hd
                Message("Retrieving Local Photo List",Level.INFO);
                LocalPhotoList(Directory, 0, Directory);

 

                //Faccio L'elenco delle cartelle e foto locali e per ogni file se non lo trovo
                // Faccio l'upload delle foto se non sono già presenti su flickr
                if (!StopProcess) {
                    localphotos.forEach((k, v)
                            -> {

                        if (remotephotoswithdata.get(k) == null) {

                            //se non trovo il corrispondente file locale nel cloud di flickr allora lo carico sul sito
                                phototoupload.put(k, v);

                        } else {
                            // if I find the same remote file but the dataTag was different I'll reUpload the file
                                File f=new File(v);
                                String data=sdf.format(f.lastModified());
                                if (data.equalsIgnoreCase(remoteLastModifiedDate.get(remotephotoswithdata.get(k)))){
                                    //If the date is the same i'll di nothing
                                }
                                else
                                    {
                                            //v is the name of the file
                                            //remotephotoswithdata.get(k) is the photoID
                                            remotePhotosToDelete.put(v, remotephotoswithdata.get(k));
                                            phototoupload.put(k, v);
                                     }
                        }
                    });
                }

                int numeroFoto = phototoupload.size();
                if (GraphicsOn) {
                    ProgressBarBackup.setMaximum(numeroFoto);
                    ProgressBarBackup.setValue(0);
                    ProgressBarBackup.setString("0/" + numeroFoto);
                    ProgressBarBackup.setStringPainted(true);
                    LabelStartTime.setText("Started Time: "
                            + String.valueOf(new Timestamp(InitialTime)));
                }
                Message("Number photo on your computer (with the exclusion of NoBackupPhoto Folder) -> " + localphotos.size(),Level.INFO);
                Message("Remeber that photos in folder NoBackupPhoto will be not considered for backup",Level.INFO);
                Message("Number of photo to upload -> " + numeroFoto,Level.INFO);

                final int[] count = {0};
                phototoupload.forEach((k, v)
                        -> {
                    count[0]++;

                    try {
                        // if button stop is pressed FermaProcessi become true and we have to termiate all tasks
                        // in this case we block the online operation
                        if (!StopProcess) {

                            Message("Uploading file " + v + " to the cloud " + count[0] + " of " + numeroFoto,Level.INFO);
                            uploadfile(v, makeSafeFilename(k.substring(k.lastIndexOf("|")+1)), k.substring(0, k.lastIndexOf("|")));
                            //La funzione di upload si occuperà del controllo e la gestione degli album
                        }
                        if (GraphicsOn) {
                            ProgressBarBackup.setValue(count[0]);
                            ProgressBarBackup.setString(count[0] + "/" + numeroFoto);
                            LabelTimeRemaining.setText("Time Remaining: "
                                    + String.valueOf(TimeUnit.MILLISECONDS.toMinutes(((System.currentTimeMillis() - InitialTime) / (long) count[0]) * (numeroFoto - count[0]))) + " minutes");
                        }
                    } catch (FlickrException ex) {
                        Message("Error uploading file:" + v + "   ->   " + ex.getErrorMessage(),Level.ERROR);
                    } catch (Exception ex) {
                        Message("Error uploading file:" + v + "   ->   " + ex.getMessage(),Level.ERROR);
                    }

                });
                
                
                //Delete Photos that I had reuploaded becouse LastModified date was different that the original
                                remotePhotosToDelete.forEach((k, v)
                        -> {
                                    try {
                                        if (!StopProcess){
                                    Message (k+" was reuploaded becouse the last modified date of the file was changed",Level.INFO);
                                    Message("Deleting old copy of the file from the cloud",Level.INFO);
                                    flickr.getPhotosInterface().delete(v); 
                                    }
                                     } catch (FlickrException ex) {

                                Message("Error deleting file from flickr -> " + ex.getErrorCode() + ":" + ex.getErrorMessage(),Level.ERROR);
                            }
                                });

                //Delete photos if Sync option is active
                remotephotoswithdata.forEach((k, v)
                        -> {

                    //Se non trovo le foto tra quelle locali e il sync è attivo le cancello
                    if (localphotos.get(k) == null && (sync == 1 || sync == 2)) {
                        // i'll delete the file only if the photo album is present in local albums but not the photo
                        // or sync option is fullscreen
                        //cancello il file solo se l'album è presente tra quelli locali ma la foto non esiste più
                        //oppure è attivo il fullsync

                        if (localalbums.get(k.substring(0, k.indexOf("|"))) != null || sync == 2) {
                            try {
                                // if button stop is pressed FermaProcessi become true and we have to termiate all tasks
                                // in this case we block the online operation
                                if (!StopProcess) {
                                    //Delete photos that are no longer present local
                                    Message("Deleting file " + k + " from the cloud",Level.INFO);
                                    flickr.getPhotosInterface().delete(v);
                                }
                            } catch (FlickrException ex) {

                                Message("Error deleting file from flickr -> " + ex.getErrorCode() + ":" + ex.getErrorMessage(),Level.ERROR);
                            }
                        }
                    }
                });
                
                //Delete photos if Sync option is active
                remotephotoswithoutdata.forEach((k, v)
                        -> {

                    //Se non trovo le foto tra quelle locali e il sync è attivo le cancello
                    if (sync == 1 || sync == 2) {
                            if (localalbums.get(k.substring(0, k.indexOf("|"))) != null || sync == 2) {
                            try {
                                // if button stop is pressed FermaProcessi become true and we have to termiate all tasks
                                // in this case we block the online operation
                                if (!StopProcess) {
                                    //Delete photos that are no longer present local
                                    Message("Deleting file " + k + " from the cloud",Level.INFO);
                                    flickr.getPhotosInterface().delete(v);
                                }
                            } catch (FlickrException ex) {

                                Message("Error deleting file from flickr -> " + ex.getErrorCode() + ":" + ex.getErrorMessage(),Level.ERROR);
                            }
                        }
                    }
                });

            } catch (IOException ex) {
                Message("IO Error   ->   " + ex.getMessage(),Level.ERROR);
            }

        }
        if (StopProcess) {
            Message("Backup Stopped by User!",Level.INFO);
            Message("-------------------------------------------------",Level.INFO);
            if (GraphicsOn) {
                LabelTimeRemaining.setText("BACKUP STOPPED BY USER!!!");

            }
        } else {
            Message("Backup Finished!",Level.INFO);
            Message("-----------------------------------------------",Level.INFO);
            if (GraphicsOn) {
                LabelTimeRemaining.setText("BACKUP FINISHED at "+String.valueOf(new Timestamp(System.currentTimeMillis())));
                JOptionPane.showMessageDialog(null, "BACKUP FINISHED!!!");
            }
        }
    }

    public void Message(String messaggio,Level level) {

        String MessaggioCompleto=String.valueOf(sdf2.format(System.currentTimeMillis()))+" : "+level.toString()+" : "+messaggio + "\n";
        if (GraphicsOn) {
            try {
                
                Document doc = TextPaneLog.getDocument();
                SimpleAttributeSet keyWord = new SimpleAttributeSet();
                
                if (level.toString().equalsIgnoreCase("ERROR"))
                    StyleConstants.setForeground(keyWord, java.awt.Color.RED);
                else
                    StyleConstants.setForeground(keyWord, java.awt.Color.BLACK);
                
                doc.insertString(doc.getLength(), MessaggioCompleto,keyWord);

                Logger.getLogger(FlickrMassUploader.class.getName()).log(level, messaggio);
            } catch (BadLocationException ex) {
                java.util.logging.Logger.getLogger(FlickrMassUploader.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
        } else {
            System.out.print(MessaggioCompleto);
            //LogFactory.getLog(FlickrMassUploader.class.getName()).info(messaggio);
            Logger.getLogger(FlickrMassUploader.class.getName()).log(level, messaggio);
        }
    }

    public void LocalPhotoList(String dir, int indentamento, String DirRiferimento) throws IOException {
        // if button stop is pressed FermaProcessi become true and we have to termiate all tasks
        if (!StopProcess) {
            int indent;
            File Dir = new File(dir);

            File[] files = Dir.listFiles();
            //devo fare il controllo sull'esistenza dei files prima di procedere perchè ci sono dei file che windows rinomina

            try {
                for (File file : files) {

                    if (file.isDirectory()) {

                        indent = indentamento + 1;
                        //Se l'indentamento è maggiore di 1 la directory di riferimento
                        //che poi sarà il nome dell'album voglio che sia quello della prima cartella
                        //infatti non posso creare album con sotto album
                        //in sostanza se ho una foto sotto ..\sardegna\lunedi\primafoto.jpg
                        //l'album sarà SARDEGNA e il nome della foto sarà lunedi_primafoto.jpg
                        if (indent > 1) {

                            LocalPhotoList(file.getCanonicalPath(), indent, DirRiferimento);
                        } else {
                            //Folder NoBackupPhoto are not inclued for backup 
                            if (!file.getName().equalsIgnoreCase("NoBackupPhoto")) LocalPhotoList(file.getCanonicalPath(), indent, file.getCanonicalPath());
                        }
                    } else {
                        if (indentamento == 0) {
                            if (VerifyExtension(file.getCanonicalPath())) {
                                localalbums.put("root", "ok");
                                // if indentamento is 0 means that photos are on the root folder so name of the remote album will be root
                                //localphotos.put("root" + "|" + makeSafeFilename(file.getName()), file.getCanonicalPath());
                                localphotos.put("root" + "|" + file.getCanonicalPath().substring(Directory.length()), file.getCanonicalPath());
                            }
                        } else {
                            if (VerifyExtension(file.getCanonicalPath())) {
                                //localalbums.put(makeSafeFilename(DirRiferimento.substring(Directory.length())), "ok");
                                localalbums.put(makeSafeFilename(DirRiferimento.substring(Directory.length())), "ok");
                                // if indentamento is > 1 means there are local sub folders other than the first
                                // so remote album is the name of the first subfolder and the remote photo name is
                                // name of subfolders plus file name
                                // Se l'indentamento è maggiore di uno il nome album è quello della prima cartella e il mome file è l'insieme
                                // del nome delle cartelle successive e il nome file
                                //localphotos.put(makeSafeFilename(DirRiferimento.substring(Directory.length())) + "|" + makeSafeFilename(file.getCanonicalPath().substring(DirRiferimento.length())), file.getCanonicalPath());
                                localphotos.put(makeSafeFilename(DirRiferimento.substring(Directory.length())) + "|" + file.getCanonicalPath().substring(Directory.length()), file.getCanonicalPath());
                            }
                        }

                    }
                }

            } catch (java.lang.NullPointerException ex) {

                Message("Error getting information of folder " + dir + " -> " + ex.getMessage(),Level.ERROR);
            }

        }
    }

    public void WritePropertiesFile() {

        try {
            Properties prop = new Properties();
            OutputStream output = null;
            output = new FileOutputStream("config.properties");
            prop.setProperty("apiKey", apiKey);
            prop.setProperty("sharedSecret", sharedSecret);
            if (AccessToken!=null) prop.setProperty("TokenAccesso", Crypt(AccessToken));
            if (TokenSecret!=null) prop.setProperty("TokenSecret", Crypt(TokenSecret));
            if (Username!=null) prop.setProperty("U", Crypt(Username));
            if (Password!=null) prop.setProperty("P", Crypt(Password));                     
            prop.setProperty("Directory", Directory);
            prop.setProperty("Sync", String.valueOf(sync));
            if (User!=null) prop.setProperty("User", Crypt(User));
            prop.setProperty("VideoDownload",DownloadVideo);
            prop.store(output, null);
            output.close();
        } catch (FileNotFoundException ex) {
            Message("Error Writing properties file -> " + ex.getMessage(),Level.ERROR);
        } catch (IOException ex) {
            Message("Error Writing properties file -> " + ex.getMessage(),Level.ERROR);
        }
    }

    public void ReadPropertiesFile() {

        try {
            String temp;
            Properties prop = new Properties();
            InputStream input;
            input = new FileInputStream("config.properties");
            prop.load(input);
            // get properties values
            apiKey = prop.getProperty("apiKey");
            sharedSecret = prop.getProperty("sharedSecret");
            AccessToken = prop.getProperty("TokenAccesso");
            TokenSecret = prop.getProperty("TokenSecret");
            Directory = prop.getProperty("Directory");
            Username = prop.getProperty("U");
            Password = prop.getProperty("P");
            if (prop.getProperty("VideoDownload")!=null)    DownloadVideo = prop.getProperty("VideoDownload");
            if (prop.getProperty("Sync")!=null)
            sync = Integer.parseInt(prop.getProperty("Sync"));
            else sync=0;
            User = prop.getProperty("User");
            // save properties to project root folder
            input.close();
            
            boolean filecrypted=true;
            temp=Decrypt(User);
            if (temp!=null) User=temp;else filecrypted=false;
            temp=Decrypt(AccessToken);
            if (temp!=null) AccessToken=temp;else filecrypted=false;
            temp=Decrypt(TokenSecret);
            if (temp!=null) TokenSecret=temp;else filecrypted=false;
            temp=Decrypt(Username);
            if (temp!=null) Username=temp;else filecrypted=false;
            temp=Decrypt(Password);
            if (temp!=null) Password=temp;else filecrypted=false;
            
            if (filecrypted==false) WritePropertiesFile();
            
            
        } catch (FileNotFoundException ex) {
            Message("Error reading Property file -> " + ex.getMessage(),Level.ERROR);
        } catch (IOException ex) {
            Message("Error reading Property File -> " + ex.getMessage(),Level.ERROR);
        }

    }

    public boolean VerifyExtension(String filename) {
        //Return true only if the extension of the file is supported
        boolean fileok = false;
        String[] SupportedExtensions = new String[]{"jpg", "jpeg", "png", "avi", "mpeg", "mp4"};
        for (int i = 0; i < SupportedExtensions.length; i++) {
            String suffix = makeSafeFilename(filename).substring(makeSafeFilename(filename).lastIndexOf('.') + 1);
            if (suffix.equalsIgnoreCase(SupportedExtensions[i])) {
                fileok = true;
            }
        }
        return fileok;

    }

    public class backup implements Runnable {

        public void run() {

            //Grayout alla buttons eccept the Stop thread Button
            CheckBoxRestore.setEnabled(false);
            ButtonRestore.setEnabled(false);
            ButtonUpload.setEnabled(false);
            ButtonDeleteCredentials.setEnabled(false);
            ButtonUpdateFlickrCredentials.setEnabled(false);
            ButtonStop.setEnabled(true);
            ButtonStop.setVisible(true);

            BackupYourFiles();
            //BACKUP YOU FILES FUCTION CLONE        

            //Restore graphics interface after backup completed
            CheckBoxRestore.setEnabled(true);
            ButtonUpload.setEnabled(true);
            ButtonDeleteCredentials.setEnabled(true);
            ButtonUpdateFlickrCredentials.setEnabled(true);
            ButtonStop.setEnabled(false);
            ButtonStop.setVisible(false);
            if (CheckBoxRestore.isSelected()) ButtonRestore.setEnabled(true);

        }

    }
    
        public class restore implements Runnable {

        public void run() {

            //Grayout alla buttons eccept the Stop thread Button
            CheckBoxRestore.setEnabled(false);
            ButtonRestore.setEnabled(false);
            ButtonUpload.setEnabled(false);
            ButtonDeleteCredentials.setEnabled(false);
            ButtonUpdateFlickrCredentials.setEnabled(false);
            ButtonStop.setEnabled(true);
            ButtonStop.setVisible(true);

            RestoreYourFiles();
            //BACKUP YOU FILES FUCTION CLONE        

            //Restore graphics interface after backup completed
            CheckBoxRestore.setEnabled(true);
            ButtonUpload.setEnabled(true);
            ButtonDeleteCredentials.setEnabled(true);
            ButtonUpdateFlickrCredentials.setEnabled(true);
            ButtonStop.setEnabled(false);
            ButtonStop.setVisible(false);
            if (CheckBoxRestore.isSelected()) ButtonRestore.setEnabled(true);

        }

    }
    
            public class RetrievePhotos implements Runnable {
            public String PsetID;
            public String Album;
            

            
         public void setPsetID(String PSETID) {
             PsetID=PSETID;
             
         }
         public void setAlbum(String ALBUM) {
             Album=ALBUM;
             
         }
        PhotosetsInterface psi = flickr.getPhotosetsInterface();
        public void run() {

                    NumProcess++;

                    
                    try {
                        RequestContext rc = RequestContext.getRequestContext();
                        rc.setAuth(auth);
                        Set<String> extras = new HashSet<>();
                        extras.add("description");
                        extras.add(Extras.ORIGINAL_FORMAT);
                        extras.add(Extras.MEDIA);
                        int photosperpage=500;
                        PhotoList photos = psi.getPhotos(PsetID, extras, 0, photosperpage, 1);
                        int pages=photos.getPages();
                        for (int x = 0; x < pages; x++) {
                            if (x>0){
                                photos = psi.getPhotos(PsetID, extras, 0, photosperpage, x+1);
                            }
                            for (int i = 0; i < photos.size(); i++) {
                                // if button stop is pressed FermaProcessi become true and we have to termiate all tasks
                                if (StopProcess) {
                                    break;
                                }
                                //foto is the selected photo
                                
                                Photo foto = (Photo) photos.get(i);
                                //System.out.println(foto.getOriginalFormat());
                                String photoID=foto.getId();
                                String Date="";
                                String Filename="";
                                String Description=foto.getDescription();

                                if (Description!=null)
                                {
                                    String lines[]=Description.split("\n");
                                    for (int y=0;y<lines.length;y++)
                                    {
                                        if (lines[y].split(":=")[0].equalsIgnoreCase("DateLastModified")&&lines[y].split(":=").length>1) 
                                        {
                                            Date=lines[y].split(":=")[1];
                                        }
                                        if (lines[y].split(":=")[0].equalsIgnoreCase("OrigFileName")&&lines[y].split(":=").length>1) 
                                        {
                                            Filename=lines[y].split(":=")[1];
                                            Filename=ReturnFullFileNameOSDepending(Filename);
                                        } 
                                    }
                                    if (!Date.equalsIgnoreCase("")&&!Filename.equalsIgnoreCase(""))
                                    {
                                        if (remotephotoswithdata.get(Album + "|" + Filename)!=null)
                                        {
                                            Message("WARNING found duplicate on Album:"+Album+" PhotoID:"+photoID+" Filename:"+Filename,Level.WARN);
                                        }
                                        else
                                        {
                                           // Message(photoID);
                                            remotephotoswithdata.put(Album + "|" + Filename, photoID);
                                            remoteLastModifiedDate.put(photoID, Date);
                                            if (foto.getMedia().equalsIgnoreCase("video")) remotemedia.put(photoID, foto.getMedia());
                                            NRPhotos++;
                                        }
                                    }
                                    else
                                    {
                                        remotephotoswithoutdata.put(Album + "|" + foto.getId(), photoID);
                                        remoteoriginalformat.put(photoID, foto.getOriginalFormat());
                                        if (foto.getMedia().equalsIgnoreCase("video")) remotemedia.put(photoID, foto.getMedia());
                                        NRnoBPhotos++;
                                    }
                                }
                                else
                                {
                                    remotephotoswithoutdata.put(Album + "|" + foto.getId(), photoID);
                                    remoteoriginalformat.put(photoID, foto.getOriginalFormat());
                                    if (foto.getMedia().equalsIgnoreCase("video")) remotemedia.put(photoID, foto.getMedia());
                                    NRnoBPhotos++;
                                }
                                
                                
                            }
                        }
                        
                    
                        NRAlbums++;
                    } catch (FlickrException ex) {
                        Message ("Error retreiving photo list:"+ex.getErrorMessage(),Level.ERROR);
                        //Logger.getLogger(FlickrMassUploader.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    NumProcess--;
                    
                        
                       
 
            
        }


    }
    
            
            
      class CustomOutputStream extends OutputStream 
{
    Logger logger;
    Level level;
    StringBuilder stringBuilder;
     
    //public CustomOutputStream(Logger logger, Level level)
    public CustomOutputStream(Level level)
    {
        //this.logger = logger;
        this.level = level;
        stringBuilder = new StringBuilder();
    }
     
    @Override
    public final void write(int i) throws IOException 
    {
        char c = (char) i;
        if(c == '\r' || c == '\n')
        {
            if(stringBuilder.length()>0)
            {
               // Logger.getLogger(FlickrMassUploader.class.getName()).log(level, Restore);
                if (stringBuilder.toString().contains("Starting ChromeDriver")||stringBuilder.toString().contains("Only local connections are allowed")||stringBuilder.toString().contains("org.openqa.selenium.remote.ProtocolHandshake createSession")||stringBuilder.toString().contains("Detected dialect"))
                Message(stringBuilder.toString(),Level.INFO);
                else Message(stringBuilder.toString(),level);
                //logger.log(level,stringBuilder.toString());
                stringBuilder = new StringBuilder();
            }
        }
        else
            stringBuilder.append(c);
    }
}      
            
            
            
            
            
            
            
    
        public class waitProcess implements Runnable {

        public void run() {

        ButtonForceStop.setVisible(true);
        LabelForceStop.setVisible(true);
        ButtonForceStop.setEnabled(true);

        //Ripristina Interfaccia finita l'esecuzione del software
        while (process.isAlive()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                //Logger.getLogger(FlickrMassUploader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        ButtonUpload.setEnabled(true);
        ButtonDeleteCredentials.setEnabled(true);
        ButtonStop.setEnabled(false);
        ButtonStop.setVisible(false);
        ButtonForceStop.setVisible(false);
        ButtonForceStop.setEnabled(false);
        CheckBoxRestore.setEnabled(true);
        if (CheckBoxRestore.isSelected()) ButtonRestore.setEnabled(true);
        ButtonUpdateFlickrCredentials.setEnabled(true);
        LabelForceStop.setVisible(false);
        JOptionPane.showMessageDialog(null, "BACKUP OR RESTORE STOPPED BY USER!!!");

        }

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ButtonChooseDirectory;
    private javax.swing.JButton ButtonDeleteCredentials;
    private javax.swing.JButton ButtonForceStop;
    private javax.swing.JButton ButtonRequestToken;
    private javax.swing.JButton ButtonRestore;
    private javax.swing.JButton ButtonSave;
    private javax.swing.JButton ButtonStop;
    private javax.swing.JButton ButtonUpdateFlickrCredentials;
    private javax.swing.JButton ButtonUpload;
    private javax.swing.JCheckBox CheckBoxEnableVideos;
    private javax.swing.JCheckBox CheckBoxRestore;
    private javax.swing.JComboBox<String> ComboBoxSyncType;
    private javax.swing.JLabel LabelApiKey;
    private javax.swing.JLabel LabelForceStop;
    private javax.swing.JLabel LabelPhotoDirectory;
    private javax.swing.JLabel LabelRestoreOptions;
    private javax.swing.JLabel LabelSharedSecret;
    private javax.swing.JLabel LabelStartTime;
    private javax.swing.JLabel LabelSyncDescription;
    private javax.swing.JLabel LabelSyncType;
    private javax.swing.JLabel LabelTimeRemaining;
    private javax.swing.JLabel LabelUser;
    private javax.swing.JProgressBar ProgressBarBackup;
    private javax.swing.JTextField TextFieldApiKey;
    private javax.swing.JTextField TextFieldPhotoDirectory;
    private javax.swing.JTextField TextFieldSharedSecret;
    private javax.swing.JTextPane TextPaneLog;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    // End of variables declaration//GEN-END:variables
}

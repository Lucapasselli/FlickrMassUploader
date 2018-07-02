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
import static flickrmassuploader.FlickrMassUploader.Nsid;
import static flickrmassuploader.FlickrMassUploader.auth;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author Luca Passelli
 */
public class FlickrMassUploader extends javax.swing.JFrame {

    /**
     * Creates new form FlickrMassUploader
     */
//    static final Logger logger=Logger.getLogger(FlickrMassUploader.class.getName());
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
    static backup Backup;
    static restore Restore;
    static Thread process;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss");
    SimpleDateFormat sdfOLD = new SimpleDateFormat("yyyy/MM/dd");
    static boolean StopProcess = false;
    static boolean GraphicsOn = true;
    static boolean CheckDate=false;
    static String OS=System.getProperty("os.name").toLowerCase();
    
    static int NRPhotos=0;
    static int NRAlbums=0;


    static int sync = 0;
    //sync=o means no sync
    //sync=1 means sync only directorys
    //sync=2 means full sync

    public FlickrMassUploader() {

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
        Message("Version : "+version);
        Message("OS : "+OS);

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
        jScrollPane1 = new javax.swing.JScrollPane();
        TextAreaLog = new javax.swing.JTextArea();
        ButtonStop = new javax.swing.JButton();
        ProgressBarBackup = new javax.swing.JProgressBar();
        LabelStartTime = new javax.swing.JLabel();
        LabelTimeRemaining = new javax.swing.JLabel();
        LabelSyncDescription = new javax.swing.JLabel();
        ButtonRestore = new javax.swing.JButton();
        CheckBoxRestore = new javax.swing.JCheckBox();
        ButtonForceStop = new javax.swing.JButton();
        LabelForceStop = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Flickr Mass Uploader by Luca Passelli");

        ButtonRequestToken.setText("RequestToken");
        ButtonRequestToken.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonRequestTokenActionPerformed(evt);
            }
        });

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

        ButtonSave.setText("Save");
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

        TextAreaLog.setColumns(20);
        TextAreaLog.setRows(5);
        jScrollPane1.setViewportView(TextAreaLog);

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

        ButtonRestore.setText("Restore");
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(ButtonStop, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(ButtonUpload)
                                .addGap(68, 68, 68)
                                .addComponent(ButtonRequestToken)
                                .addGap(80, 80, 80))
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
                                        .addGap(0, 62, Short.MAX_VALUE)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 232, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ButtonDeleteCredentials, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(ButtonChooseDirectory, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ButtonSave, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(17, 17, 17))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(96, 96, 96)
                        .addComponent(ComboBoxSyncType, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(LabelSyncDescription, javax.swing.GroupLayout.PREFERRED_SIZE, 559, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(ButtonRestore, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(CheckBoxRestore, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE))
                        .addGap(17, 17, 17))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(ProgressBarBackup, javax.swing.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
                            .addComponent(LabelForceStop, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(LabelStartTime)
                                    .addComponent(LabelTimeRemaining))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(ButtonForceStop, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())))))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {ButtonChooseDirectory, ButtonDeleteCredentials, ButtonSave});

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
                    .addComponent(TextFieldSharedSecret, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabelUser)
                    .addComponent(ButtonSave))
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TextFieldPhotoDirectory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LabelPhotoDirectory)
                    .addComponent(ButtonChooseDirectory))
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LabelSyncDescription, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addComponent(CheckBoxRestore)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ButtonRestore)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(ButtonUpload)
                            .addComponent(ButtonRequestToken)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(LabelSyncType)
                            .addComponent(ComboBoxSyncType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ButtonStop, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(LabelStartTime)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(LabelTimeRemaining))
                    .addComponent(ProgressBarBackup, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 2, Short.MAX_VALUE)
                        .addComponent(ButtonForceStop))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(LabelForceStop)
                        .addGap(0, 0, Short.MAX_VALUE))))
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
        LabelUser.setText("Credentials Deleted!, Please request a new Token");
        ButtonUpload.setEnabled(false);
        ButtonRequestToken.setEnabled(true);
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
            Message("Error secting Directory -> " + ex.getMessage());
        } catch (java.lang.InternalError ex) {
            //This error occour when the chooser can't open an existing directory
            //If this occour the chooser must open the root directory
            Message("Error secting Directory -> " + ex.getMessage());
            fc.setCurrentDirectory(new java.io.File("."));
            int returnVal = fc.showOpenDialog(this);
            if (returnVal == 0) {
                try {
                    Directory = fc.getSelectedFile().getCanonicalPath();
                } catch (IOException ex1) {
                    Logger.getLogger(FlickrMassUploader.class.getName()).log(Level.SEVERE, null, ex1);
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
        Message("Forced Backup Interruption");
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
                        StopProcess = false;
                        boolean connectionOK;
                        connectionOK = gr.auth();
                        if (connectionOK) {
                            gr.BackupYourFiles();
                        }
                        gr.dispose();
                    } else {
                        System.out.println("Please launch program without argument for graphics or with /download argument for batch option");
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
                Message("Username: " + auth.getUser().getUsername());

            } catch (FlickrException ex) {
                Message("Auth2 error: " + ex.getErrorMessage() + " -> " + ex.getErrorCode());
                Message("Request new authentication in progress...");
                //if is not possible to have authentication you need new credentials
                connectionOK = FirstAuth();
            } catch (com.flickr4java.flickr.FlickrRuntimeException ex) {
                Message("Auth2 error: " + ex.getMessage() + " -> Probably you have no internet connection or the site is not avaiable");
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
            Message("token: " + token);

            String url = authInterface.getAuthorizationUrl(token, Permission.DELETE);
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI(url));
                } catch (URISyntaxException ex) {
                    Message("Error Getting URL: " + ex.getMessage());
                } catch (IOException ex) {
                    Message("Error Getting URL: " + ex.getMessage());
                }
            }

            String n = JOptionPane.showInputDialog("Please accept authetincation request from your broswer and then paste here your Confirmation Code:");
            ConfirmationCode = n;
            Message("Pasted Confirmation Code:" + ConfirmationCode);
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
                    Message("Username: " + auth.getUser().getUsername());
                    //Message("Permission: " + auth.getPermission().getType());
                    WritePropertiesFile();
                    Message("Authentication success");
                    JOptionPane.showMessageDialog(null, "Authentication Success, now you can upload your files");
                } catch (FlickrException ex) {
                    Message("First Authentication error: " + ex.getErrorMessage() + " -> " + ex.getErrorCode());
                    JOptionPane.showMessageDialog(null, "Confirmation Code:" + ConfirmationCode + " seems to be wrong, please retry the authentication");
                    connectionOK = false;
                } catch (org.scribe.exceptions.OAuthException ex) {
                    Message("First Authentication error: " + ex.getMessage());
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
    
    
    
    public void downloadfile(String photoID,File newFile)throws Exception,FlickrException{
            if (!StopProcess) {

                PhotosInterface photoI=flickr.getPhotosInterface();
                Photo p = photoI.getPhoto(photoID);
                
                // if file in not a video i'll download
                // unfortunately video download api seems to be bugged
                File tempFile=new File(newFile.getCanonicalPath()+"_temp");
                //System.out.println(p.getLargeUrl());
                if (!p.getMedia().equalsIgnoreCase("video")){
                Message("Downloading " + newFile.getName());
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
                Message("Download of " + newFile.getName()+" completed!");
                Files.copy(tempFile.toPath(), newFile.toPath());
                tempFile.delete();
                if (StopProcess) tempFile.delete();
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
                    Message ("File Size: "+String.valueOf(fileSizeMB)+" KB");
                    photoId = uploader.upload(f, metaData);
                    Message(" File : " + filename + " uploaded: photoId = " + photoId);

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
                                                Message("WARNING found duplicate on Album:"+Album+" PhotoID:"+photoID+" Filename:"+Filename);
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
                        
                        Message("Number of Flickr Albums="+NAlbums);
                        Message("Number of Flickr Photos="+NPhotos);
            } catch (FlickrException ex) {
                Message("Error getting Photo List on Flickr -> " + ex.getErrorCode() + ":" + ex.getErrorMessage());
            }

        }
    }
    
    
    
    public void RemotePhotoList2() {
        // if button stop is pressed StopProcess become true and we have to termiate all tasks
            int ConcurrentProcess=5;
            remotephotoswithdata = new HashMap<>();
            remotephotoswithoutdata = new HashMap<>();
            remoteLastModifiedDate = new HashMap<>();
            remoteoriginalformat = new HashMap<>();
            
        if (!StopProcess) {

            try {
                NRPhotos=0;
                NRAlbums=0;
                PhotosInterface photoI=flickr.getPhotosInterface();
                PhotosetsInterface psi = flickr.getPhotosetsInterface();
                
                //create an itarator to list albums
                //sets is the list of albums
                Iterator sets = psi.getList(Nsid).getPhotosets().iterator();
                

                while (sets.hasNext()) {
                   
                                Photoset Pset = (Photoset) sets.next();
                                
                                String Album=Pset.getTitle();
                                String PsetID=Pset.getId();
                                remotealbums.put(Pset.getTitle(), Pset.getId());
                                
                                RetrievePhotos RP = new RetrievePhotos();
                                RP.setPsetID(PsetID);
                                RP.setAlbum(Album);
                               
                                Thread processo = new Thread(RP);
                                processo.start();
                    

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
                        NRPhotos++;
                    }
                        }
                        
                        Message("Number of Flickr Albums="+NRAlbums);
                        Message("Number of Flickr Photos="+NRPhotos);
                        Thread.sleep(100000);
            } catch (FlickrException ex) {
                Message("Error getting Photo List on Flickr -> " + ex.getErrorCode() + ":" + ex.getErrorMessage());
            }   catch (InterruptedException ex) {
                    Logger.getLogger(FlickrMassUploader.class.getName()).log(Level.SEVERE, null, ex);
                }

        }
    }
    
    
        public void UpdateDateTags(String PHOTOID,String DATE) {
        // if button stop is pressed StopProcess become true and we have to termiate all tasks
        if (!StopProcess) {
            try {

                PhotosInterface photoI=flickr.getPhotosInterface();
                Message ("Photo:"+photoI.getPhoto(PHOTOID).getTitle()+" ID:"+PHOTOID+" with old style LastModifiedDate Format, I'll upgrade it!");
                
                            Collection<Tag> tags=photoI.getPhoto(PHOTOID).getTags();

                        for (Tag tag : tags) {
                            if (tag.getRaw().split(":=")[0].equalsIgnoreCase("DateLastModified")&&tag.getRaw().split(":=").length>1) 
                            {
                                photoI.removeTag(tag.getId());
                                Message ("Old Tag Deleted -> "+tag.getRaw());

                            }
                            }
                String tags1[]=new String[1];
                tags1[0]="DateLastModified:="+DATE;
                photoI.addTags(PHOTOID, tags1);
                Message ("New Tag Created -> "+tags1[0]);

                
                    
            } catch (FlickrException ex) {
                Message("Error in tag update -> " + ex.getErrorCode() + ":" + ex.getErrorMessage());
            }

        }
    }
    
       
    
    public void RestoreYourFiles() {

        // if button stop is pressed StopProcess become true and we have to termiate all tasks
        if (!StopProcess) {
            Long InitialTime = System.currentTimeMillis();
            LabelTimeRemaining.setText("Time Remaining:");
            remotealbums = new HashMap<>();
            phototoupload = new HashMap<>();
            localalbums = new HashMap<>();
            localphotos = new HashMap<>();
            phototodownload = new HashMap<>();;
           // remotePhotosToDelete = new HashMap<>();

            //authentication    
            RequestContext rc = RequestContext.getRequestContext();
            rc.setAuth(auth);
            Message("Retrieving Remote Photo List");
            RemotePhotoList();
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
                    
                    phototodownload.put(v, filename);

                    
                   } 

                    } 
                catch (Exception ex) {
                        Message("Error verifiing files:" + v + "   ->   " + ex.getMessage());
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
                    
                    phototodownload.put(v, filename);
                    /*downloadfile(v,newFile);
                    if (Date!=null&&!Date.equalsIgnoreCase("")) newFile.setLastModified(sdf.parse(Date).getTime());*/
                    
                   } 

                    } 

                
               /* catch (FlickrException ex) {
                        Message("Error downloading file:" + v + "   ->   " + ex.getErrorMessage()+"    "+ex.getMessage());
                       // Message("Error downloading file:" + v + "   ->   " + ex.getMessage());
                    }*/ catch (Exception ex) {
                        Message("Error verifiing files:" + v + "   ->   " + ex.getMessage());
                    }
                     
                });
                
                
                
                int numeroFoto = phototodownload.size();
                if (GraphicsOn) {
                    ProgressBarBackup.setMaximum(numeroFoto);
                    ProgressBarBackup.setValue(0);
                    ProgressBarBackup.setString("0/" + numeroFoto);
                    ProgressBarBackup.setStringPainted(true);
                    LabelStartTime.setText("Started Time: "
                            + String.valueOf(new Timestamp(InitialTime)));
                }
                //Message("Number photo on your computer -> " + localphotos.size());
                Message("Number of photo to download -> " + numeroFoto);
                
                
                final int[] count = {0};
                
                phototodownload.forEach((k, v)
                -> {
                    count[0]++;
                    try {
                    String Date= remoteLastModifiedDate.get(k);
                    File file=new File (v);
                    downloadfile(k,file);
                    if (Date!=null&&!Date.equalsIgnoreCase("")) file.setLastModified(sdf.parse(Date).getTime());
                    if (GraphicsOn) {
                            ProgressBarBackup.setValue(count[0]);
                            ProgressBarBackup.setString(count[0] + "/" + numeroFoto);
                            LabelTimeRemaining.setText("Time Remaining: "
                                    + String.valueOf(TimeUnit.MILLISECONDS.toMinutes(((System.currentTimeMillis() - InitialTime) / (long) count[0]) * (numeroFoto - count[0]))) + " minutes");
                        }
                } catch (FlickrException ex) {
                        Message("Error downloading file:" + k + "   ->   " + ex.getErrorMessage()+"    "+ex.getMessage());
                       // Message("Error downloading file:" + v + "   ->   " + ex.getMessage());
                    }
                    catch (Exception ex) {
                    Message("Error downloading file:" + k + "   ->   " + ex.getMessage());
                }
                    
                    
                    
                
                });
                
                
                
            
            if (StopProcess) {
            Message("Restore Stopped by User!");
            Message("-------------------------------------------------");
            if (GraphicsOn) {
                LabelTimeRemaining.setText("RESTORE STOPPED BY USER!!!");

            }
        } else {
            Message("Restore Finished!");
            Message("-----------------------------------------------");
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
            Message("Directory to sync -> " + Directory);
            if (sync == 0) {
                Message("Sync Type -> NO");
            }
            if (sync == 1) {
                Message("Sync Type -> SYNC");
            }
            if (sync == 2) {
                Message("Sync Type -> FULLSYNC");
            }

            Long InitialTime = System.currentTimeMillis();
            LabelTimeRemaining.setText("Time Remaining:");
           // remotephotos = new HashMap<>();
            remotealbums = new HashMap<>();
            phototoupload = new HashMap<>();
            localalbums = new HashMap<>();
            localphotos = new HashMap<>();

            remotePhotosToDelete = new HashMap<>();

            
            //authentication    
            RequestContext rc = RequestContext.getRequestContext();
            rc.setAuth(auth);

            try {
                //Faccio l'elenco degli album e delle foto su flickr

                Message("Retrieving Remote Photo List");
                RemotePhotoList();
                //elenco fotolocali non fa altro che compilare l'hasmap fotolocali con l'elenco delle foto sull'hd
                Message("Retrieving Local Photo List");
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
                                            remotePhotosToDelete.put(k, remotephotoswithdata.get(k));
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
                Message("Number photo on your computer -> " + localphotos.size());
                Message("Number of photo to upload -> " + numeroFoto);

                final int[] count = {0};
                phototoupload.forEach((k, v)
                        -> {
                    count[0]++;

                    try {
                        // if button stop is pressed FermaProcessi become true and we have to termiate all tasks
                        // in this case we block the online operation
                        if (!StopProcess) {

                            Message("Uploading file " + v + " to the cloud " + count[0] + " of " + numeroFoto);
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
                        Message("Error uploading file:" + v + "   ->   " + ex.getErrorMessage());
                    } catch (Exception ex) {
                        Message("Error uploading file:" + v + "   ->   " + ex.getMessage());
                    }

                });
                
                
                //Delete Photos that I had reuploaded becouse LastModified date was different that the original
                                remotePhotosToDelete.forEach((k, v)
                        -> {
                                    try {
                                    Message("Deleting file " + k + " from the cloud");
                                    flickr.getPhotosInterface().delete(v); 
                                     } catch (FlickrException ex) {

                                Message("Error deleting file from flickr -> " + ex.getErrorCode() + ":" + ex.getErrorMessage());
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
                                    Message("Deleting file " + k + " from the cloud");
                                    flickr.getPhotosInterface().delete(v);
                                }
                            } catch (FlickrException ex) {

                                Message("Error deleting file from flickr -> " + ex.getErrorCode() + ":" + ex.getErrorMessage());
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
                                    Message("Deleting file " + k + " from the cloud");
                                    flickr.getPhotosInterface().delete(v);
                                }
                            } catch (FlickrException ex) {

                                Message("Error deleting file from flickr -> " + ex.getErrorCode() + ":" + ex.getErrorMessage());
                            }
                        }
                    }
                });

            } catch (IOException ex) {
                Message("IO Error   ->   " + ex.getMessage());
            }

        }
        if (StopProcess) {
            Message("Backup Stopped by User!");
            Message("-------------------------------------------------");
            if (GraphicsOn) {
                LabelTimeRemaining.setText("BACKUP STOPPED BY USER!!!");

            }
        } else {
            Message("Backup Finished!");
            Message("-----------------------------------------------");
            if (GraphicsOn) {
                LabelTimeRemaining.setText("BACKUP FINISHED at "+String.valueOf(new Timestamp(System.currentTimeMillis())));
                JOptionPane.showMessageDialog(null, "BACKUP FINISHED!!!");
            }
        }
    }

    public void Message(String messaggio) {
        if (GraphicsOn) {
            TextAreaLog.append(String.valueOf(new Timestamp(System.currentTimeMillis()))+" : "+messaggio + "\n");
            TextAreaLog.setCaretPosition(TextAreaLog.getDocument().getLength());

        } else {
            System.out.println(String.valueOf(new Timestamp(System.currentTimeMillis()))+" : "+messaggio);
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

                            LocalPhotoList(file.getCanonicalPath(), indent, file.getCanonicalPath());
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

                Message("Error getting information of folder " + dir + " -> " + ex.getMessage());
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
            if (AccessToken!=null) prop.setProperty("TokenAccesso", AccessToken);
            if (TokenSecret!=null) prop.setProperty("TokenSecret", TokenSecret);
            prop.setProperty("Directory", Directory);
            prop.setProperty("Sync", String.valueOf(sync));
            if (User!=null) prop.setProperty("User", User);
            //prova
            // save properties to project root folder
            prop.store(output, null);
            output.close();
        } catch (FileNotFoundException ex) {
            Message("Error Writing properties file -> " + ex.getMessage());
        } catch (IOException ex) {
            Message("Error Writing properties file -> " + ex.getMessage());
        }
    }

    public void ReadPropertiesFile() {

        try {
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
            if (prop.getProperty("Sync")!=null)
            sync = Integer.parseInt(prop.getProperty("Sync"));
            else sync=0;
            User = prop.getProperty("User");

            // save properties to project root folder
            input.close();
        } catch (FileNotFoundException ex) {
            Message("Error reading Property file -> " + ex.getMessage());
        } catch (IOException ex) {
            Message("Error reading Property File -> " + ex.getMessage());
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
            ButtonStop.setEnabled(true);
            ButtonStop.setVisible(true);

            BackupYourFiles();
            //BACKUP YOU FILES FUCTION CLONE        

            //Restore graphics interface after backup completed
            CheckBoxRestore.setEnabled(true);
            ButtonUpload.setEnabled(true);
            ButtonDeleteCredentials.setEnabled(true);
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
            ButtonStop.setEnabled(true);
            ButtonStop.setVisible(true);

            RestoreYourFiles();
            //BACKUP YOU FILES FUCTION CLONE        

            //Restore graphics interface after backup completed
            CheckBoxRestore.setEnabled(true);
            ButtonUpload.setEnabled(true);
            ButtonDeleteCredentials.setEnabled(true);
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
           
                    
                    try {
                       RequestContext rc = RequestContext.getRequestContext();
                        rc.setAuth(auth);
                        Set<String> extras = new HashSet<>();
                        extras.add("description");
                        extras.add(Extras.ORIGINAL_FORMAT);
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
                                            Message("WARNING found duplicate on Album:"+Album+" PhotoID:"+photoID+" Filename:"+Filename);
                                        }
                                        else
                                        {
                                            Message(photoID);
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
                                
                                NRPhotos++;
                                
                            }
                        }
                        
                    
                        NRAlbums++;
                    } catch (FlickrException ex) {
                        Logger.getLogger(FlickrMassUploader.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                        
                       
 
            
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
                Logger.getLogger(FlickrMassUploader.class.getName()).log(Level.SEVERE, null, ex);
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
    private javax.swing.JButton ButtonUpload;
    private javax.swing.JCheckBox CheckBoxRestore;
    private javax.swing.JComboBox<String> ComboBoxSyncType;
    private javax.swing.JLabel LabelApiKey;
    private javax.swing.JLabel LabelForceStop;
    private javax.swing.JLabel LabelPhotoDirectory;
    private javax.swing.JLabel LabelSharedSecret;
    private javax.swing.JLabel LabelStartTime;
    private javax.swing.JLabel LabelSyncDescription;
    private javax.swing.JLabel LabelSyncType;
    private javax.swing.JLabel LabelTimeRemaining;
    private javax.swing.JLabel LabelUser;
    private javax.swing.JProgressBar ProgressBarBackup;
    private javax.swing.JTextArea TextAreaLog;
    private javax.swing.JTextField TextFieldApiKey;
    private javax.swing.JTextField TextFieldPhotoDirectory;
    private javax.swing.JTextField TextFieldSharedSecret;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}

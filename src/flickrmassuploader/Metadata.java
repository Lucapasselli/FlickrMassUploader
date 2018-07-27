/*
 * The MIT License
 *
 * Copyright 2018 luca.passelli.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package flickrmassuploader;

import com.flickr4java.flickr.tags.Tag;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import pixy.image.tiff.TiffTag;
import pixy.meta.MetadataType;
import pixy.meta.exif.Exif;
import pixy.meta.iptc.IPTC;
import pixy.meta.iptc.IPTCApplicationTag;
import pixy.meta.iptc.IPTCDataSet;
import pixy.meta.jpeg.JpegExif;

/**
 *
 * @author luca.passelli
 */
public class Metadata {
    
    
    
    public String getTitle(String File)
    {   
        String Titolo="";
        try {
            Map<MetadataType, pixy.meta.Metadata> metadataMap = pixy.meta.Metadata.readMetadata(File);
            for(Map.Entry<MetadataType, pixy.meta.Metadata> entry : metadataMap.entrySet()) {
                byte[] data=entry.getValue().getData();
                if(entry.getValue().getType().compareTo(MetadataType.EXIF)==0)
                           if(data != null && data.length > 0) {
                               Exif exif = new JpegExif(data);
                               if (exif.getImageIFD().getFieldAsString(TiffTag.WINDOWS_XP_TITLE).split("=>").length>1)
                                    Titolo=exif.getImageIFD().getFieldAsString(TiffTag.WINDOWS_XP_TITLE).split("=>")[1].trim();
                           }
            }
            
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(FlickrMassUploader.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        return Titolo;
    }
    
    
        public String getComment(String File)
    {   
        String Description="";
        try {
            Map<MetadataType, pixy.meta.Metadata> metadataMap = pixy.meta.Metadata.readMetadata(File);
            for(Map.Entry<MetadataType, pixy.meta.Metadata> entry : metadataMap.entrySet()) {
                byte[] data=entry.getValue().getData();
                if(entry.getValue().getType().compareTo(MetadataType.EXIF)==0)
                           if(data != null && data.length > 0) {
                               Exif exif = new JpegExif(data);
                               if (exif.getImageIFD().getFieldAsString(TiffTag.WINDOWS_XP_COMMENT).split("=>").length>1)
                                    Description=exif.getImageIFD().getFieldAsString(TiffTag.WINDOWS_XP_COMMENT).split("=>")[1].trim();
                           }
            }
            
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(FlickrMassUploader.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        return Description;
    }
        
        
           public List getTags(String File)
    {   

        List Tags=new ArrayList<>();
        try {
            Map<MetadataType, pixy.meta.Metadata> metadataMap = pixy.meta.Metadata.readMetadata(File);
            for(Map.Entry<MetadataType, pixy.meta.Metadata> entry : metadataMap.entrySet()) {
                byte[] data=entry.getValue().getData();
               if(data != null && data.length > 0) {
                           IPTC iptc = new IPTC(data);
                          
                           if (!iptc.getAsString("Keywords").equalsIgnoreCase(""))
                               {     
                                   Tags.addAll(Arrays.asList(iptc.getAsString("Keywords").split(";")));
                               }
                            }
            }
            
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(FlickrMassUploader.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        return Tags;
    }
           
           
           
    public void WriteTags(String FileOri,String FileDest,Collection<Tag> Tags)
    {   

       
        try {
            
            FileInputStream fin = new FileInputStream(FileOri);
            FileOutputStream fout = new FileOutputStream(FileDest);
            pixy.meta.Metadata.insertIPTC(fin, fout, createIPTCDataSet(Tags), true);
            fin.close();
            fout.close();
            

            
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(FlickrMassUploader.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
       
    }
        
    		private static List<IPTCDataSet> createIPTCDataSet(Collection<Tag> Tags) {
			List<IPTCDataSet> iptcs = new ArrayList<IPTCDataSet>();
                        Iterator Tagsi=Tags.iterator();
                        while(Tagsi.hasNext())
                        {
                            
                            Tag tag=(Tag)Tagsi.next();
                            iptcs.add(new IPTCDataSet(IPTCApplicationTag.KEY_WORDS, tag.getRaw()));
			}
			return iptcs;
		}
        
        
        
}

package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.InflaterInputStream;

public final class FileReading {
    
    private FileReading(){}; // interdit l'instanciation de cette classe
    
    public static Byte[] ReadFile(File f) throws FileNotFoundException, IOException{
    // Decompression des objets + stockage des octets dans un tableau
	FileInputStream fis = new FileInputStream(f);
        
        InflaterInputStream decompresser = new InflaterInputStream(fis);
                
        ArrayList<Byte> LectureFichier = new ArrayList();
        int caract;
        
        try {
            while((caract = decompresser.read()) != -1){
                LectureFichier.add( (byte)caract );
            }
        }
        catch(IOException e) {
            throw new IOException("fichier "+f.getName()+" : "+e.getMessage());
        }
         
        return LectureFichier.toArray(new Byte[0]);         
               
    }
    
    public static Byte[] inflate(FileInputStream fis) throws FileNotFoundException, IOException{
        
        InflaterInputStream decompresser = new InflaterInputStream( fis );
                
        ArrayList<Byte> LectureFichier = new ArrayList();
        int caract;
        
        try {
            while((caract = decompresser.read()) != -1){
                LectureFichier.add( (byte)caract );
            }
        }
        catch(IOException e) {
            throw new IOException( e.getMessage() );
        }
         
        return LectureFichier.toArray(new Byte[0]);         
               
    }
    
    public static String stringValue( Byte[] inflated ) {
        // converti les octets du tableau en characteres
        StringBuilder content = new StringBuilder();
        
        int i = 0;
        char c;
        while(i < inflated.length) {
        
            c = (char)inflated[i].byteValue();
            content.append(c);
            i++;
        }
        
        return content.toString();
        
    }
    
    public static String toHex( Byte[] inflated ) {
        
        StringBuilder content = new StringBuilder();
        
        int i = 0;
        
        while(i < inflated.length) {
            content.append(String.format("%02x", inflated[i]));
            i++;
        }
        
        return content.toString();
    }
    
    public static Byte[] removeHeading( Byte[] inflated ) {
        
        int i = 0;
        char c;
        
        do {
            c = (char)inflated[i].byteValue();
            i++;
        }while (c != '\0');
        
                
        ArrayList<Byte> LectureFichier = new ArrayList();
        
        while(i < inflated.length) {
            LectureFichier.add( inflated[i] );
            i++;
        }
         
        return LectureFichier.toArray(new Byte[0]); 
        
    }
    
}

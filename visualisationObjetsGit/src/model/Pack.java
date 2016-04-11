package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 *
 * @author Jarretier Adrien "jarretier.adrien@gmail.com"
 */
public class Pack {
    
    private final static int OBJ_BAD = -1;
    private final static int OBJ_NONE = 0;
    private final static int OBJ_COMMIT = 1;
    private final static int OBJ_TREE = 2;
    private final static int OBJ_BLOB = 3;
    private final static int OBJ_TAG = 4;
    private final static int OBJ_OFS_DELTA = 6;
    private final static int OBJ_REF_DELTA = 7;
    
    private File idx;
    private File pack;
    
    private int version;
        
    private TreeMap<Integer, String> offsetObjects = new TreeMap<>();

    private ArrayList<String> names = new ArrayList<>();
    private int objectsInPack = 0;
    
    public Pack(File packFile) throws IOException {

        File parent = packFile.getParentFile();

        this.idx = new File(parent, packFile.getName().replace(".pack", ".idx"));
        this.pack = packFile;

        byte[] buff;
        Byte[] buffCopy;

        FileInputStream fisIdx = new FileInputStream( this.idx );

//------------------------------------------------------------------------------

    // premiers 4 octets : toujours -1, 116, 79, 99

        buff = new byte[4];

        fisIdx.read(buff);

        for (int i = 0; i < buff.length; i++) {

//            System.out.print( buff[i] + " " );

        }

//        System.out.println( "" );

//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
        
    // 4 octets : version
    
        buff = new byte[4];
        
        fisIdx.read(buff);

        this.version = ByteBuffer.wrap(buff).getInt();

//        System.out.print( "version : " + this.version );

//        System.out.println( "" );
        
//------------------------------------------------------------------------------
         
//------------------------------------------------------------------------------
        
    // 1024 octets
    // table qui permet de connaitre le nombre d'objet dont le nom
    // commence par un des 256 premiers octets possibles (0x00 .. 0xff)
    // chaque entree est sur 4 octets
    
        buff = new byte[1024];
        
        fisIdx.read(buff);
        ByteBuffer wrap1 = ByteBuffer.wrap(buff);
            
        for (int i = 0; i < buff.length; i+=4) {

            this.objectsInPack = wrap1.getInt();

        }
//        System.out.print( "objectsInPack : " + objectsInPack );

//        System.out.println( "" );
        
//------------------------------------------------------------------------------
      
    }

}

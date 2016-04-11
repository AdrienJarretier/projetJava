package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Map;
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
    
    public Pack(File packFile) throws Exception {

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
      
//------------------------------------------------------------------------------
        
    // 20 octets * le nombre d'objets
    // les noms des objets
    
        buff = new byte[20*this.objectsInPack];
        
        fisIdx.read(buff);
        
        buffCopy = new Byte[ 20 ];

        
        for (int i = 0; i < this.objectsInPack; i++) {
            
            for (int j = 0; j < 20; j++) {
                
                buffCopy[j] = buff[j+i*20];
                
            }
            this.names.add( FileReading.toHex(buffCopy) );
            
        }
        
//        for (String name : this.names) {
//            System.out.println(name);
//        }
        
//------------------------------------------------------------------------------
        
//------------------------------------------------------------------------------
        
    // 4 octets Cyclic redundancy check
    // pour chaque objet
    
        buff = new byte[4];

//        System.out.print("crc : ");
        for (int i = 0; i < objectsInPack; i++) {
        
            fisIdx.read(buff);
            for (int j = 0; j < buff.length; j++) {
                
//                System.out.print( buff[j] + " " );
            }
//            System.out.print( "|" );
        }
        
//        System.out.println("");
        
//------------------------------------------------------------------------------
           
//------------------------------------------------------------------------------
        
    // 4 octets * le nombre d'objets
    // decalage pour chaque objet
    
        buff = new byte[4*objectsInPack];
        
        fisIdx.read(buff);
        
        buffCopy = new Byte[ 4 ];
        ByteBuffer wrap2 = ByteBuffer.wrap(buff);

        for (int i = 0; i < objectsInPack; i++) {
            
            for (int j = 0; j < 4; j++) {
                
                buffCopy[j] = buff[j+i*4];
                
            }

            offsetObjects.put( wrap2.getInt() & 0b01111111111111111111111111111111 , names.get( offsetObjects.size() ) );
            
        }
        
//        for (Map.Entry<Integer, String> offset : offsetObjects.entrySet()) {
//            
//            System.out.println( offset.getKey() + " : " + offset.getValue() );
//        }

        fisIdx.close(); // on libere le fichier idx.
        // on va maintenant passer au fichier pack

//------------------------------------------------------------------------------
        
//------------------------------------------------------------------------------
// PACK file
        
        FileInputStream fis = new FileInputStream( pack );

    // premiers 4 octets : mot "PACK"
    
        buff = new byte[4];
        buffCopy = new Byte[ buff.length ];
        
        fis.read(buff);

//        for (int i = 0; i < buff.length; i++) {
//
//            buffCopy[i] = buff[i];
//
//        }
//
//        System.out.println( FileReading.stringValue( buffCopy ) );
        
//------------------------------------------------------------------------------
               
//------------------------------------------------------------------------------
        
    // 4 octets : version
    
        buff = new byte[4];
        
        fis.read(buff);

//        System.out.println( ByteBuffer.wrap(buff).getInt() );

        // les versions ne correspondent pas ERREUR !!
        if ( this.version != ByteBuffer.wrap(buff).getInt() ) {
            throw new Exception("versions non compatibles entre pack et idx");
        }

//        System.out.println( "" );
        
//------------------------------------------------------------------------------
     
//------------------------------------------------------------------------------
        
    // 4 octets : nombre d'objets dans ce Pack
        buff = new byte[4];
        
        fis.read(buff);
        
//        System.out.println("objets : " + ByteBuffer.wrap(buff).getInt());
        
        // le nombre d'objets ne correspond pas
        // avec ce qui ete dans idx ERREUR !!
        if ( this.objectsInPack != ByteBuffer.wrap(buff).getInt() ) {
            throw new Exception("nombre d'objets indiques dans idx different");
        }
        
//------------------------------------------------------------------------------

    }

}

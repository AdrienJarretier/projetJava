package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
    
//    private final static int OBJ_BAD = -1;
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
    protected Git gitInstance;
    private ArrayList<GitObject> gitObjectsList;
    
    public Pack(File packFile, ArrayList<GitObject> objects, Git _gitInstance) throws Exception, FileNotFoundException, IOException {
        
        this.gitObjectsList = objects;
        
        this.gitInstance = _gitInstance;
        
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
//        buffCopy = new Byte[ buff.length ];
        
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
        // avec ce qui etait dans idx ERREUR !!
        if ( this.objectsInPack != ByteBuffer.wrap(buff).getInt() ) {
            throw new Exception("nombre d'objets indiques dans idx different");
        }
        
//------------------------------------------------------------------------------
       
//------------------------------------------------------------------------------

    // on va traiter chaque objet dans le pack
    //
    // chaque objet commence par des metadatas
    // chaque octet de ces metadatas commence par un MSB (most significant bit) 
    //
    // les 3 bits suivants du premier octet indiquent le type de l'objet
    //
    // les 4 derniers bits sont le debut d'un entier a taille variable
    // qui indique la taille de l'objet (apres decompression).
    //
    // si le MSB est a 1 alors il faut lire l'octet suivant,
    // il contient un MSB et 7 bits qui font parti de notre entier
    // et ainsi dessuite jusqu'a avoir un MSB == 0
        
        for (Map.Entry<Integer, String> offset : offsetObjects.entrySet()) {
            
            fis = new FileInputStream( this.pack );
            
            fis.skip( offset.getKey() );
            
            int data = fis.read();

            // test "most significant bit"
            boolean readNextByte = data >= 128;
            // si le premier bit est 1 alors on lira l'octet suivant
            // qui fait partie de notre entier a taille variable
            // (voir plus haut)
            
            while( readNextByte ) {
                
                data = fis.read();
                readNextByte = data >= 128;
                
            }
            
            int type = getType( offset.getKey() );
            
            switch(type) {
                    
                case OBJ_COMMIT:
                case OBJ_TREE:
                case OBJ_BLOB:
//                case OBJ_TAG:
                    this.addObject(offset.getValue(), offset.getKey(), type);
                    break;
                    
                case OBJ_OFS_DELTA:
                    
                    // on va d'abord recuperer la position de 'lobjet de base
                    // ainsi on aura son type : Blob, Commit, ....
                    // ensuite on va instancier un nouvel objet de ce type
                    // et seulement sur demande on appliquera les deltas
                    
                    // le delta commence par un entier a taille variable
                    // c'est un offset negatif pour trouver l'objet de base
                    
                    int absoluteBaseOffset = getAbsoluteBaseObjectOffset(offset.getKey());
                    
                    this.addObject( offset.getValue(), offset.getKey(), getType( absoluteBaseOffset ) );

                    break;
                    
//                case OBJ_REF_DELTA:
//                    System.out.println("OBJ_REF_DELTA");
//                    break;
                
            }
            
        }
    
//------------------------------------------------------------------------------
    }
    
    /**
     * a partir d'un objet a la position donne, suppose un objet delta
     * recherche et renvoi la position de l'objet de base le plus absolu
     * c'est a dire parcours tous les deltas jusqua trouver l'objet
     * Blob, Commit, Tree, ...
     * 
     * @param offset la position de l'objet delta
     * @return la position de l'objet de base
     */
    private int getAbsoluteBaseObjectOffset( int offset ) throws IOException {
        
        int type;
        
        do {
            
            FileInputStream fis = new FileInputStream( this.pack );

            fis.skip( offset );

            int data = fis.read();

            // test "most significant bit"
            boolean readNextByte = data >= 128;

            while( readNextByte ) {

                data = fis.read();
                readNextByte = data >= 128;

            }
            
            // le delta commence par un entier a taille variable
            // c'est un offset negatif pour trouver l'objet de base
            data = fis.read();
            int negativeOffset = data & 0b01111111;

            readNextByte = data >= 128;
            // si le premier bit est 1 alors on lira l'octet suivant
            // qui fait partie de notre entier a taille variable

            long twoPower = 1;

            while( readNextByte ) {

                data = fis.read();
                readNextByte = data >= 128;

                negativeOffset = negativeOffset << 7;
                negativeOffset += data & 0b01111111;

                twoPower *= 128;

            }

            while( twoPower > 1 ) {
                negativeOffset += twoPower;
                twoPower /= 128;
            }

            int resultOffset = offset - negativeOffset;

            type = getType( resultOffset );

            offset = resultOffset;
            
        } while( type >= OBJ_OFS_DELTA );

        return offset;
    }
    
    /**
     * 
     * @param offset la position d'un objet dans ce pack
     * @return le type de cet objet
     */
    private int getType( int offset ) throws FileNotFoundException, IOException {
        
        FileInputStream fis = new FileInputStream( this.pack );

        fis.skip( offset );

        int data = fis.read();

        int type = data & 0b01110000; // masque pour obtenir le type

        String typeStringBin = Integer.toBinaryString(type);
        while( typeStringBin.length() < 8 ) {
            typeStringBin = "0"+typeStringBin;
        }
        typeStringBin = typeStringBin.substring(1,4);
        type = Integer.valueOf(typeStringBin, 2);
        
        return type;
        
    }
    
    private void addObject( String name, int offset, int type ) throws IOException {
        
        switch(type) {
            
            case OBJ_COMMIT:
                this.gitObjectsList.add( new Commit( name, this.gitInstance, offset, this ) );
            break;

            case OBJ_TREE:
                this.gitObjectsList.add( new Tree( name, this.gitInstance, offset, this ) );
            break;

            case OBJ_BLOB:
                this.gitObjectsList.add( new Blob( name, this.gitInstance, offset, this ) );
            break;
                    
//            case OBJ_TAG:
//                objects.add( new Tag( offset.getValue(), this.gitInstance, realObjectOffset ) );
//                break;
            
        }
        
    }
    
    /**
     *
     * @param offset la position de l'objet recherchz
     * @return les donnes presentes apres les metadatas
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    public Byte[] getRawDatas( int offset ) throws FileNotFoundException, IOException {
        
        FileInputStream fis = new FileInputStream( this.pack );
        
        fis.skip(offset);
        
//        System.out.println("rawDatas type : " + getType(offset));
        
        boolean readNextByte = fis.read() >= 128;
        
        while( readNextByte ) {

            readNextByte = fis.read() >= 128;

        }
        
        return FileReading.inflate( fis );
        
    }

}

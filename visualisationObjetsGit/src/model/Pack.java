package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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

    }

}

package game.prototype;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.FileSystems;
import java.time.LocalDateTime;
//import java.util.ArrayList;
import java.util.Arrays;

import game.prototype.framework.States;

public class FileManagement {
    private File location, saveFile;
    private States states;

    public FileManagement(States states) {
        this.states = states;
        location = new File(FileSystems.getDefault().getPath(".").toString() + "/savedata/");
        if (!location.exists()) {
            try {
                location.mkdir();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }      
        loadFiles();
    }

    public void save(int slotIndex) {
        if (location.listFiles().length > 5) {
            Arrays.sort(location.listFiles());
            File delfile = location.listFiles()[0];
            delfile.delete();
        }

        String filename = LocalDateTime.now().toString();
        filename = filename.replace('.', '_');
        filename = filename.replace(':', '_');

        saveFile = new File(location.getPath() + "/" + filename + ".txt");
        
        if (!saveFile.exists()) {
            try {
                saveFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            // save
            FileOutputStream fos = new FileOutputStream(saveFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject("Hola");
            // oos.writeObject(colorRGB);

            oos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load(String filename) {

        File saveFile = new File(location.getPath() + "/" + filename);
        Object o = null;
        //Object o1 = null;
        try {
            FileInputStream fis = new FileInputStream(saveFile);
            ObjectInputStream ois = new ObjectInputStream(fis);

            o = ois.readObject();
            //o1 = ois.readObject();

            ois.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        //@SuppressWarnings("unchecked")
        //ArrayList<float[]> restoredData = (ArrayList<float[]>) o;
        String lol = (String) o;
        System.out.println(lol);
    }

    public void deleteFile(int index) {
        Arrays.sort(location.listFiles());
        File dFile = location.listFiles()[index];
        dFile.delete();
        loadFiles();
    }
    
    public void loadFiles() {
        states.saveSlots().clear();       
        for (int i = 0; i < 6; i++) {
            states.saveSlots().add("Empty");
        }       
        for (int i = 0; i < location.listFiles().length; i++) {
            states.saveSlots().set(i, location.listFiles()[i].getName());
        }
        
    }
}

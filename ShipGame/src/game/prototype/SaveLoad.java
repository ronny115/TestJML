package game.prototype;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.FileSystems;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class SaveLoad{
    
    private String filename;

    public  void saveFile(ArrayList<float[]> colorRGB) {
        
        String filename = LocalDateTime.now().toString();
        filename = filename.replace('.', '_');
        filename = filename.replace(':', '_');
        this.filename = filename;
        
        File location = new File(FileSystems.getDefault().getPath(".").toString() + "/savedata/");
        File saveFile = new File(location.getPath() + "/"  + filename + ".txt");
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
            oos.writeObject(colorRGB);
            
            oos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //provisional
        loadFile();
    }

    public void loadFile() {
        
        File location = new File(FileSystems.getDefault().getPath(".").toString() + "/savedata/");
        File saveFile = new File(location.getPath() + "/"  + filename + ".txt");

        System.out.println(location.listFiles()[0]);
       

        Object o = null;
        Object o1 = null;
        try {
            FileInputStream fis = new FileInputStream(saveFile);
            ObjectInputStream ois = new ObjectInputStream(fis);

            o1 = ois.readObject();
            o = ois.readObject();

            ois.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        @SuppressWarnings("unchecked")
        ArrayList<float[]> restoredData = (ArrayList<float[]>) o;
        String lol = (String) o1;

//
//        list.clear();
//        restoredData.clear();
    }
}

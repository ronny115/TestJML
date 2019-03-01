package game.prototype;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.util.ArrayList;

public class SaveLoad {
    
    private float[] numeros;
    private float[][] rowColumn;

    public  void saveFile(String filename, ArrayList<float[]> colorRGB) {
        numeros = new float[colorRGB.size() * colorRGB.get(0).length];
        rowColumn = new float[colorRGB.size()][colorRGB.get(0).length];
        
        String saveFile_location = FileSystems.getDefault().getPath(".").toString() + "/savedata/";
        File saveFile = new File(saveFile_location + filename + ".txt");
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
            DataOutputStream dos = new DataOutputStream(fos);
            for (float[] f : colorRGB) {
                for(int i = 0; i < colorRGB.get(0).length; i++) {
                    dos.writeFloat(f[i]);                  
                }
            }
            dos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFile(String filename) {
        String saveFile_location = FileSystems.getDefault().getPath(".").toString() + "/savedata/";
        File saveFile = new File(saveFile_location + filename + ".txt");
        
        ArrayList<Float> list = new ArrayList<Float>();   
        ArrayList<float[]> restoredData = new ArrayList<float[]>();
        
        InputStream is;
        try {          
            is = new FileInputStream(saveFile);
            DataInputStream dis = new DataInputStream(is);
            // load to variables
            while (dis.available() > 0) {
                float c = dis.readFloat();
                list.add(c);
            }
            dis.close();
            is.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
            
        //separar por orden de variables
        for (int i = 0; i < list.size(); i++) {
            numeros[i] = list.get(i);
        }

        int counter = 0;
        for (int j = 0; j < rowColumn.length; j++) {
            for (int i = 0; i < rowColumn[0].length; i++) {
                rowColumn[j][i] = numeros[counter];
                counter++;

            }
            restoredData.add(rowColumn[j]);
        }
        System.out.println(counter);
        System.out.println(restoredData.size());
        System.out.println(restoredData.get(2)[0]);
        System.out.println(restoredData.get(2)[1]);
        System.out.println(restoredData.get(2)[2]);

        list.clear();
        restoredData.clear();
    }
}

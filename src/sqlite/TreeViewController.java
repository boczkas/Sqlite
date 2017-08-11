/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sqlite;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author przem
 */
public class TreeViewController {
    
        static public ArrayList<String> getDataBaseFilesListFromDirectory(String directoryName){
        
        ArrayList<String> fileList = new ArrayList<>();
        File directory = new File(directoryName);
        File[] fList = directory.listFiles();
        
        // TBD check if file is a DB file
        for (File file : fList){
            if (file.isFile()){
                System.out.println(file.getName());
                fileList.add(file.getName());
            }
        }
        
        return fileList;
    }
}

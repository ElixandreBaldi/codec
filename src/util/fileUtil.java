/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author FREE
 */
public class fileUtil {
    public static byte[] toByteArray(File file){
        try {
            InputStream in = new FileInputStream(file);
            int bytesRead;
            byte[] buffer = new byte[1024];
            ArrayList< Byte > ret = new ArrayList<>();
            while (true){
                bytesRead = in.read(buffer);
                for (int i=0;i<bytesRead;i++){
                    ret.add(buffer[i]);
                }
                if (bytesRead<1024){
                    break;
                }
            }
            byte[] retorno = new byte[ret.size()];
            for (int i=0;i<ret.size();i++){
                retorno[i] = ret.get(i);
            }
            return(retorno);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(fileUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex){
            Logger.getLogger(fileUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return(null);
    }
}

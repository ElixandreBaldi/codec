/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openGL;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Imagem;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_BGR;

/**
 *
 * @author FREE
 */
public class Texture {
    private int id;
    private int width;
    private int height;
    private static ByteBuffer pixels = null;
    
    /*
    public static Texture loadPNG(File dir, String texName) {
        File f = new File(dir+"/"+texName+".png");
        try {
            if(!f.exists()) throw new IOException();
 
            IntBuffer w = BufferUtils.createIntBuffer(1);
            IntBuffer h = BufferUtils.createIntBuffer(1);
            IntBuffer comp = BufferUtils.createIntBuffer(1);
            ByteBuffer buffer = STBImage.stbi_load(f.toString(),w,h,comp,4);
 
            if(buffer == null) throw new IOException();
 
            int imgWidth = w.get();
    */
        public Texture(Imagem img){
        //PTimer pt = new PTimer("tempo de carregar");
        //pt.startTimer();
            System.out.println("chamando textura");
        try {
            width = img.getWidth();
            height = img.getHeight();
            byte[] pixels_raw = img.imgToBytes();
            if (pixels == null){
                pixels = BufferUtils.createByteBuffer(width*height*3);
            }else{
                pixels.clear();
            }
            pixels.put(pixels_raw);
            /*
            ByteBuffer pixels = BufferUtils.createByteBuffer(width*height*3);
            for (int i=0;i<height;i++){
                for (int j=0;j<width;j++){
                    pixels.put(pixels_raw[(i*width+j)*3+2]); //r
                    pixels.put(pixels_raw[(i*width+j)*3+1]); //g
                    pixels.put(pixels_raw[(i*width+j)*3]); //b
                }
            }
            */
            pixels.flip();
            
            id = glGenTextures();
            
            glBindTexture(GL_TEXTURE_2D, id);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);//pode mudar GL_NEARES para GL_LINEAR
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            //32922
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_BGR, GL_UNSIGNED_BYTE, pixels);
        } catch (Exception ex) {
            Logger.getLogger(Texture.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            //pt.endTimer();
            //System.out.println(1E9/pt.getElapsed() + " fps");
        }
    }
    
    /*
        public Texture(String filename,boolean aux){
            PTimer pt = new PTimer("tempo de carregar "+filename);
            pt.startTimer();
            BufferedImage bi;
            try {
                bi = ImageIO.read(new File(filename));
                width = bi.getWidth();
                height = bi.getHeight();
                int[] pixels_raw = new int[width*height];
                pixels_raw = bi.getRGB(0, 0, width, height, null, 0, width);

                ByteBuffer pixels = BufferUtils.createByteBuffer(width*height*3);
                for (int i=0;i<height;i++){
                    for (int j=0;j<width;j++){
                        int pixel = pixels_raw[i*width+j];
                        pixels.put((byte)((pixel >> 16) & 0xFF)); //r
                        pixels.put((byte)((pixel >> 8) & 0xFF)); //g
                        pixels.put((byte)(pixel & 0xFF)); //b
                    }
                }
                pixels.flip();

                id = glGenTextures();

                glBindTexture(GL_TEXTURE_2D, id);
                glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);//pode mudar GL_NEARES para GL_LINEAR
                glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, pixels);
            } catch (IOException ex) {
                Logger.getLogger(Texture.class.getName()).log(Level.SEVERE, null, ex);
            } finally{
                pt.endTimer();
                System.out.println(1E9/pt.getElapsed() + " fps");
            }
        }
    */
    public void bind(){
        glBindTexture(GL_TEXTURE_2D, id);
    }
    
    public static void main(String args[]){
        //Texture tex = new Texture("./res/test.png");
    }
}

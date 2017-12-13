/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.File;
import javax.swing.JFrame;
import util.PTimer;
import view.imagemPanel;

/**
 *
 * @author FREE
 */
public class ImagemYCBCR {
    private int width;
    private int height;
    
    // /*
    private byte[][] y;
    private byte[][] cb;
    private byte[][] cr;
    // */
    
    /*
    private int[][] y;
    private int[][] cb;
    private int[][] cr;
    // */
    
    public ImagemYCBCR(Imagem rgb){
        this.width = rgb.getWidth();
        this.height = rgb.getHeight();
        
        byte[][] r = rgb.getR();
        byte[][] g = rgb.getG();
        byte[][] b = rgb.getB();
        
        // /*
        y = new byte[height][width];
        cb = new byte[height/2][width/2]; //u
        cr = new byte[height/2][width/2]; //v
        System.out.println("cb : " + cb.length + " , " + cb[0].length);
        // */
        
        /*
        y = new int[height][width];
        cb = new int[height][width];
        cr = new int[height][width];
        // */
        for (int i=0;i<height/2;i++){
            for (int j=0;j<width/2;j++){
                byte[][] auxCb = new byte[2][2];
                byte[][] auxCr = new byte[2][2];
                for (int x=0;x<2;x++){
                    for (int w=0;w<2;w++){
                        int B = (b[i*2+x][j*2+w]&0xFF);
                        int G = (g[i*2+x][j*2+w]&0xFF);
                        int R = (r[i*2+x][j*2+w]&0xFF);

                        int Y = (int)(R * 0.299D + G * 0.587D + B * 0.114D);
                        int U = (int)(R * -0.168736D + G * -0.331264D + B * 0.5D + 128.0D);
                        int V = (int)(R * 0.5D + G * -0.418688D + B * -0.081312D + 128.0D);

                        Y = Y < 0 ? 0 : Y > 255 ? 255 : Y;
                        U = U < 0 ? 0 : U > 255 ? 255 : U;
                        V = V < 0 ? 0 : V > 255 ? 255 : V;
                        //System.out.println("Y["+(i*2+x)+"]["+(j*2+w)+"] : " + Y);
                        y[i*2+x][j*2+w] = (byte)Y;
                        auxCb[x][w] = (byte)U;
                        auxCr[x][w] = (byte)V;
                    }
                }
                cb[i][j] = (byte)(((auxCb[0][0]&0xFF) + (auxCb[0][1]&0xFF) + (auxCb[1][0]&0xFF) + (auxCb[1][1]&0xFF))/4.0);
                cr[i][j] = (byte)(((auxCr[0][0]&0xFF) + (auxCr[0][1]&0xFF) + (auxCr[1][0]&0xFF) + (auxCr[1][1]&0xFF))/4.0);
            }
        }
    }
    
    public static void main(String args[]){
        File file = new File("/home/elixandrebaldi/1Sample01_25/Sample01_000.bmp");
        PTimer t = new PTimer("tempo de rgb para yuv");
        t.startTimer();
        Imagem img = new Imagem(file,true);
        //System.out.println("img 1 : " + img.testPrint());
        //System.out.println("imagem original : " + img.testPrint());
        ImagemYCBCR temp = new ImagemYCBCR(img);
        System.out.println("size ycbcr : " + temp.y.length+","+temp.y[0].length +" ; " + temp.cb.length+","+temp.cb[0].length + " ; " + temp.cr.length+","+temp.cr[0].length);
        //System.out.println("img 2 : " + temp.testPrint());
        t.endTimer();
        System.out.println(t);
        t = new PTimer("tempo de yuv para rgb");
        t.startTimer();
       // System.out.println("imagem ycbcr : " + temp.testPrint());
        Imagem out = new Imagem(temp);
        //System.out.println("img 3 : " + out.testPrint());
        t.endTimer();
        System.out.println(t);
        //System.out.println("imagem final : " + out.testPrint());
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        imagemPanel p = new imagemPanel(out);
        frame.add(p);
        frame.pack();
        frame.setVisible(true);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    // /*
    public byte[][] getY() {
        return y;
    }

    public byte[][] getCb() {
        return cb;
    }

    public byte[][] getCr() {
        return cr;
    }
    // */
    
    /*
    public int[][] getY() {
        return y;
    }

    public int[][] getCb() {
        return cb;
    }

    public int[][] getCr() {
        return cr;
    }
    // */
    
    public String testPrint(){
                StringBuilder aux = new StringBuilder();
        aux.append("size : ").append(height).append(',').append(width).append('\n');
        aux.append("size matriz b= ").append(y.length).append(',').append(y[0].length).append('\n');
        try{
            int linha;
            int coluna;
            for (int i=0;i<height;i++){
                for (int j=0;j<width;j++){
                    linha = (i/2);
                    coluna = (j/2);
                    aux.append('[').append(i).append(',').append(j).append(']').append("=").append('{');
                    aux.append((y[i][j]&0xFF)).append(',');
                    aux.append((cb[linha][coluna]&0xFF)).append(',');
                    aux.append((cr[linha][coluna]&0xFF)).append(")\n");
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            return(aux.toString());
        }
    }
    
    
}

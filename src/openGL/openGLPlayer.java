/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openGL;

import comprensoes.testDCT;
import java.io.BufferedInputStream;
import model.Imagem;
import util.PTimer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

/**
 *
 * @author FREE
 */
public class openGLPlayer {
    int telaWidth = 0;
    int telaHeight = 0;
    int leitorFrame = 0;
    int escritorFrame = 0;
    private Texture texture;
    
    public static final int RAW = 0;
    public static final int DCT = 2;
    
    //static Imagem[] frameBuffer = new Imagem[10];
    
    private Imagem[] frameBuffer;
    
    public void playVideo(File alvo,int tipo,int quantiaBuffers) throws Exception {
        if (tipo!=RAW && tipo!=DCT){
            throw new IllegalArgumentException("Esperava codigo RAW recebeu " + tipo);
        }
        if (!glfwInit()){
            throw new IllegalStateException("Failed to initialize GLFW");
        }
        frameBuffer = new Imagem[quantiaBuffers];
        GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        telaWidth = videoMode.width()/3;
        telaHeight = videoMode.height()/3;
        
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        long window = glfwCreateWindow(telaWidth, telaHeight, "test", 0, 0);
        if (window == 0){
            throw new IllegalStateException("Failed to create window!");
        }
        GL.createCapabilities();
        glfwSetWindowPos(window, (videoMode.width() - telaWidth)/2, (videoMode.height() - telaHeight)/2 );
        
        glfwShowWindow(window);
        glfwMakeContextCurrent(window);
        System.out.println("test"); 
        
           
        glEnable(GL_TEXTURE_2D);
        
        //Texture tex = new Texture("./res/Sample01_000.bmp");
        byte[] intArray = new byte[4];
        InputStream fileIn = new FileInputStream(alvo);
        //fileIn.
        fileIn.read(intArray);
        int lastFrame = ByteBuffer.wrap(intArray).getInt();
        fileIn.read(intArray);
        int imageWidth = ByteBuffer.wrap(intArray).getInt();
        fileIn.read(intArray);
        int imageHeight = ByteBuffer.wrap(intArray).getInt(); 
        fileIn.read(intArray);
        int frameRate = ByteBuffer.wrap(intArray).getInt();
        System.out.println("frameRate leitura : " + frameRate);
        Thread leitor = null;
        if (tipo==RAW){
            leitor = new Thread(){
                @Override
                public void run(){
                    int contador = 0;
                    escritorFrame = 0;
                    int aux;
                    byte[] bytesArrayInicial = new byte[(imageWidth*imageHeight)*3];
                    int frameRemaining = lastFrame;
                    try {
                        //System.out.println("lastFrame : " + lastFrame + ",width : " + telaWidth + ",height : " + telaHeight);
                        while (contador<lastFrame){
                            if ((escritorFrame+1)%quantiaBuffers!=leitorFrame){
                                frameRemaining--;
                                PTimer pt = new PTimer("framesRemaining="+Integer.toString(frameRemaining));
                                pt.startTimer();
                                aux = escritorFrame;
                                if (frameBuffer[aux]==null){
                                    System.out.println("inicial "+aux);
                                    fileIn.read(bytesArrayInicial);
                                    frameBuffer[aux] = new Imagem(bytesArrayInicial,imageWidth,imageHeight);
                                }else{
                                    PTimer pt2 = new PTimer("tempo para leitura disco");
                                    pt2.startTimer();
                                    fileIn.read(bytesArrayInicial);
                                    pt2.endTimer();
                                    System.out.println(pt2);
                                    frameBuffer[aux] = new Imagem(bytesArrayInicial,imageWidth,imageHeight);
                                    //frameBuffer[aux].setImagem(bytesArrayInicial,imageWidth,imageHeight);
                                }
                                //System.out.println("criando : " + aux);
                                contador++;
                                escritorFrame++;
                                if (escritorFrame>(quantiaBuffers-1)){
                                    escritorFrame = 0;
                                }
                                pt.endTimer();
                                System.out.println(pt);
                            }else{
                                try {
                                    Thread.sleep(1);
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(openGLPlayer.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        }
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(openGLPlayer.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(openGLPlayer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };
        }else if (tipo==DCT){
            leitor = new Thread(){
                @Override
                public void run(){
                    int contador = 0;
                    escritorFrame = 0;
                    int aux;
                    byte[] leitura;
                    byte[] tamanho = new byte[4];
                    int frameRemaining = lastFrame;
                    try {
                        //System.out.println("lastFrame : " + lastFrame + ",width : " + telaWidth + ",height : " + telaHeight);
                        while (contador<lastFrame){
                            if ((escritorFrame+1)%quantiaBuffers!=leitorFrame){
                                frameRemaining--;
                                PTimer pt = new PTimer("framesRemaining="+Integer.toString(frameRemaining));
                                pt.startTimer();
                                aux = escritorFrame;
                                System.out.println("inicial "+aux);
                                fileIn.read(tamanho);
                                leitura = new byte[ByteBuffer.wrap(tamanho).getInt()];
                                fileIn.read(leitura);
                                frameBuffer[aux] =  testDCT.carregarImagem(leitura);
                            //System.out.println("criando : " + aux);
                                contador++;
                                escritorFrame++;
                                if (escritorFrame>(quantiaBuffers-1)){
                                    escritorFrame = 0;
                                }
                                pt.endTimer();
                                System.out.println(pt);
                            }else{
                                try {
                                    Thread.sleep(1);
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(openGLPlayer.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        }
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(openGLPlayer.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(openGLPlayer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };
        }
        leitor.start();
        int contador = 0;
        PTimer pt = new PTimer("duracao total");
        Thread.sleep(100);
        pt.startTimer();
        int contadorEsperas = 0;
        int aux = 0;
        int frameRateMili = new Double(Math.floor(1000.0/(frameRate+0.00))).intValue();
        System.out.println("frameRateMili  :" + frameRateMili);
        long time = System.currentTimeMillis()-frameRateMili;
        while(!glfwWindowShouldClose(window) && contador<lastFrame){
            if (leitorFrame!=escritorFrame && (System.currentTimeMillis()-time)>frameRateMili){
                time = System.currentTimeMillis();
                aux = leitorFrame;
                
                Texture tex = new Texture(frameBuffer[aux]);

                glfwPollEvents();

                glClear(GL_COLOR_BUFFER_BIT);

                tex.bind();

                glBegin(GL_QUADS);
                //glColor3f(1,1,1);
                glTexCoord2f(0, 0);
                glVertex2f(-1f,1f);
                glTexCoord2f(1, 0);
                glVertex2f(1f,1f);
                glTexCoord2f(1, 1);
                glVertex2f(1f,-1f);
                glTexCoord2f(0, 1);
                glVertex2f(-1f,-1f);
                glEnd();

                glfwSwapBuffers(window);
                contador++;
                leitorFrame++;
                if (leitorFrame>(quantiaBuffers-1)){
                    leitorFrame = 0;
                }
            }else{
                glfwPollEvents();

                glClear(GL_COLOR_BUFFER_BIT);

                //glBegin(GL_QUADS);
                //glColor3f(1,1,1);
                //glVertex2f(-1f,1f);
                //glVertex2f(1f,1f);
                //glVertex2f(1f,-1f);
                //glVertex2f(-1f,-1f);
                //glEnd();

                //glfwSwapBuffers(window);
                if (leitorFrame==escritorFrame){
                    contadorEsperas++;
                }
                Thread.sleep(1);
            }
        }
        pt.endTimer();
        System.out.println(pt);
        System.out.println("cotnador de esperas : " + contadorEsperas);
        System.out.println("fps medio : " + ((lastFrame+0.00)/(pt.getElapsedSegundos())));
        glfwTerminate();
        
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comprensoes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Imagem;
import util.PTimer;

/**
 *
 * @author FREE
 */
public class VideoWriter {
    public static String retornarStringPorNumero(int a){
        String inicial = Integer.toString(a);
        if (inicial.length()==1){
            return("00"+inicial);
        }else if (inicial.length()==2){
            return("0"+inicial);
        }else{
            return(inicial);
        }
    }
    
    public static void write(File diretorio,String baseNome,String extensao,int ultimoID,File nameOut){
        int ultimoIdFixo = 10;
        FileOutputStream out = null;
        try {
            System.out.println("diretorio : " + diretorio);
            String dirName = diretorio.getName();
            StringBuilder frameRateName = new StringBuilder().append(dirName.charAt(new Long(dirName.length()-2).intValue())).append(dirName.charAt(new Long(dirName.length()-1).intValue()));
            int frameRate = Integer.parseInt(frameRateName.toString());
            System.out.println("frameRate : " + frameRate);
            System.out.println("write start");
            out = new FileOutputStream(nameOut);
            File fileImagemInicial = new File((diretorio.getPath()+"//"+baseNome+retornarStringPorNumero(0)+extensao));
            Imagem imagemInicial = new Imagem(fileImagemInicial,true);
            int width = imagemInicial.getWidth();
            int height = imagemInicial.getHeight();
            
            PTimer t2 = new PTimer("tempo total");
            t2.startTimer();
            PTimer ptstart = new PTimer("primeiroFrame");
            ptstart.startTimer();
            byte[] cabecalho = new byte[16];
            byte[] ultimoIDBytes = ByteBuffer.allocate(4).putInt(ultimoID).array();
            byte[] widthBytes = ByteBuffer.allocate(4).putInt(width).array();
            byte[] heightBytes = ByteBuffer.allocate(4).putInt(height).array();
            byte[] frameRateBytes = ByteBuffer.allocate(4).putInt(frameRate).array();
            System.arraycopy(ultimoIDBytes, 0, cabecalho, 0, 4);
            System.arraycopy(widthBytes, 0, cabecalho, 4, 4);
            System.arraycopy(heightBytes, 0, cabecalho, 8, 4);
            System.arraycopy(frameRateBytes, 0, cabecalho, 12, 4);
            if (true){
                System.out.println("imagem inicial path : " + (diretorio.getPath()+"//"+baseNome+retornarStringPorNumero(0)+extensao));
            }
            out.write(cabecalho);
            out.write(imagemInicial.imgToBytes());
            Imagem imagemBuffer = new Imagem(imagemInicial);
            ptstart.endTimer();
            byte[] aux;
            System.out.println(ptstart);
            for (int i=1;i<=ultimoID;i++){
                PTimer ptloop= new PTimer("frame "+Integer.toString(i));
                ptloop.startTimer();
                File file = new File( (diretorio.getPath()+"//"+baseNome+retornarStringPorNumero(i)+extensao) );
                //imagemInicial.setImagem(file,false);
                //aux = Imagem.diffImagem(imagemBuffer, imagemInicial);
                //imagemInicial.setPixels(imagemBuffer, aux);
                
                //byte[] test = Quadtree.getQuadBytes(width, height, aux);
                
                //System.out.println("tamanho diff : " + aux.length);
                //System.out.println("tamanho arvore : " + test.length);
                
                imagemBuffer = new Imagem(file,true);
                byte[] salvar = testDCT.salvarImagem(imagemBuffer);
                out.write(salvar);
                //out.write(imagemBuffer.imgToBytes());
                //break;
                ptloop.endTimer();
                System.out.println("size saida : " + salvar.length);
                System.out.println(ptloop);
            }
            t2.endTimer();
            System.out.println(t2);
            out.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(VideoWriter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(VideoWriter.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            System.out.println("write fim");
        }
    }
    
    public static void main(String args[]) throws FileNotFoundException, IOException{
        PTimer tempo = new PTimer("Tempo teste escrita e leitura");
        tempo.startTimer();
        File file = new File("C:\\Users\\FREE\\Desktop\\pidFrames\\aux64\\goldswordbmp.bmp");
        Imagem img = new Imagem(file,true);
        byte[] b = img.imgToBytes();
        
        Imagem imgb = new Imagem(b,img.getWidth(),img.getHeight());
        
        if (img.equals(imgb)){
            System.out.println("igual");
        }else{
            System.out.println("diferente");
        }
            
        
        tempo.endTimer();
        System.out.println(tempo);
    }
}

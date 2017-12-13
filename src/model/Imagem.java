/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;
import comprensoes.testDCT;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import util.PTimer;
import view.imagemPanel;

/**
 *
 * @author elixandrebaldi
 */
public class Imagem {
    private static final int min = 0;
    
    private int width;
    
    private int height;
    
    private int deslocamento;
    
    //private byte[] pixel;
    
    private byte[][] r;
    
    private byte[][] g;
    
    private byte[][] b;
    
    private int linhaAux = 0;
    private int colunaAux = 0;
    
    private byte[] auxBuffer;
    
    public Imagem(byte[][] b,byte[][] g,byte[][] r){
        width = b.length;
        height = b[0].length;
        this.b = b;
        this.g = g;
        this.r = r;
        
        /*
        //pixel = new byte[width*height*3];
        int aux;
        for (int i=0;i<height;i++){
            for (int j=0;j<width;j++){
                aux = 3*(i*width+j);
                pixel[aux] = b[i][j];
                pixel[aux+1] = g[i][j];
                pixel[aux+2] = r[i][j];
            }
        }
        */
    }
    
    public static void test(int height,int width){
        for (int i=0;i<height/2;i++){
            for (int j=0;j<width/2;j++){
                for (int x=0;x<2;x++){
                    for (int w=0;w<2;w++){
                        System.out.println("setando["+(i*2+x)+"]["+(j*2+w)+"] : " + "cr["+i+"]["+j+"]");
                    }
                }
            }
        }
        
        for (int i=0;i<height/2;i++){
            for (int j=0;j<width/2;j++){
                for (int x=0;x<2;x++){
                    for (int w=0;w<2;w++){
                        //y[i*2+x][j*2+w] = (byte)Y; (r[i*2+x][j*2+w]&0xFF);
                        System.out.println("y["+(i*2+x)+"]["+(j*2+w) +"] : ==");
                    }
                }
            }
        }
    }
    
    public Imagem(ImagemYCBCR ycbcr){
        // /*
        byte[][] y = ycbcr.getY();
        /*
        for (int i=0;i<y.length;i++){
            for (int j=0;j<y[0].length;j++){
                System.out.println("["+i+"]["+j+"] : " + (y[i][j]&0xFF));
            }
        }
        */
        byte[][] cb = ycbcr.getCb();
        byte[][] cr = ycbcr.getCr();
        // */
        
        /*
        int[][] y = ycbcr.getY();
        int[][] cb = ycbcr.getCb();
        int[][] cr = ycbcr.getCr();
        // */
        this.width = ycbcr.getWidth();
        this.height = ycbcr.getHeight();
        
        b = new byte[height][width];
        g = new byte[height][width];
        r = new byte[height][width];
        
        for (int i=0;i<height/2;i++){
            for (int j=0;j<width/2;j++){
                int CB = (cb[i][j]&0xFF);
                int CR = (cr[i][j]&0xFF);
                for (int x=0;x<2;x++){
                    for (int w=0;w<2;w++){
                        
                        //int B = (int)((y[i+x][j+w]&0xFF) + 1.779D * ((cb[i+x][j+w]&0xFF) - 128));
                        //int G = (int)( (y[i+x][j+w]&0xFF) - 0.3455D * ((cb[i+x][j+w]&0xFF) - 128) - 0.7169D * ((cr[i+x][j+w]&0xFF) - 128));
                        //int R = (int)((y[i+x][j+w]&0xFF) + 1.4075D * ((cr[i+x][j+w]&0xFF) - 128));
                        
                        int B = (int)((y[i*2+x][j*2+w]&0xFF) + 1.779D * (CB - 128));
                        int G = (int)( (y[i*2+x][j*2+w]&0xFF) - 0.3455D * (CB - 128) - 0.7169D * (CR - 128));
                        int R = (int)((y[i*2+x][j*2+w]&0xFF) + 1.4075D * (CR - 128));
                        //System.out.println("para pixel : ["+(i*2+x)+"]["+(j*2+w)+"] = B : " + B + ", Y : " + (y[i*2+x][j*2+w]&0xFF));
                        //System.out.println((y[i+x][j+w]&0xFF) +"+"+ (1.779D * (CB - 128)));
                        R = R < 0 ? 0 : R > 255 ? 255 : R;
                        G = G < 0 ? 0 : G > 255 ? 255 : G;
                        B = B < 0 ? 0 : B > 255 ? 255 : B;
                        r[i*2+x][j*2+w] = (byte)R;
                        g[i*2+x][j*2+w] = (byte)G;
                        b[i*2+x][j*2+w] = (byte)B;
                    }
                }
            }
        }
    }
    
    public Imagem(Imagem[][] imagens){
        int linha = imagens.length;
        int coluna = imagens[0].length;
        
        this.width = coluna*8;
        this.height = linha*8;
        this.b = new byte[linha*8][coluna*8];
        this.g = new byte[linha*8][coluna*8];
        this.r = new byte[linha*8][coluna*8];
        int idColuna;
        int idLinha;
        
        for (int i=0;i<height;i++){
            for (int j=0;j<width;j++){
                idColuna = j/8;
                idLinha = i/8;
                Imagem local = imagens[idLinha][idColuna];
                //System.out.println("["+i+","+j+"]");
                this.b[i][j] = local.b[i%8][j%8];
                this.g[i][j] = local.g[i%8][j%8];
                this.r[i][j] = local.r[i%8][j%8];
            }
        }
    }
    
    public Imagem[][] dividirImagem(){
        if ((this.width)%8!=0 || (this.height)%8!=0){
            throw new IllegalArgumentException("???");
        }
        int linha = this.height/8;
        int coluna = this.width/8;
        Imagem[][] retorno = new Imagem[linha][coluna];
        for (int i=0;i<linha;i++){
            for (int j=0;j<coluna;j++){
                retorno[i][j] = new Imagem(new byte[64*3],8,8); 
            }
        }
        int idLinha;
        int idColuna;
        for (int i=0;i<height;i++){
            for (int j=0;j<width;j++){
                idColuna = j/8;
                idLinha = i/8;
                Imagem local = retorno[idLinha][idColuna];
                local.b[i%8][j%8] = this.b[i][j];
                local.g[i%8][j%8] = this.g[i][j];
                local.r[i%8][j%8] = this.r[i][j];
            }
        }
        return(retorno);
    }
    
    public static void main(String args[]){
        test(6,8);
        if (true){
            return;
        }
        PTimer tempo = new PTimer("tempo divicao e juncao");
        tempo.startTimer();
        File file = new File("C:\\Users\\FREE\\Desktop\\pidFrames\\aux64\\goldswordbmp.bmp");
        Imagem img = new Imagem(file,true);
        //System.out.println("img 1 : " + img.testPrint());
        byte[][][] b1 = {img.b,img.g,img.r};
        Imagem[][] imagens = img.dividirImagem();
        Imagem outra = new Imagem(imagens);
        //System.out.println("img 2 : " + outra.testPrint());
        byte[][][] b2 = {outra.b,outra.g,outra.r};
        if (img.equals(outra)){
            System.out.println("igual!");
        }else{
            System.out.println("diferente!");
        }
        //System.out.println("saida img : " + img.testPrint());

        JFrame frame = new JFrame();
        imagemPanel p = new imagemPanel(outra);
        frame.add(p);
        frame.pack();
        frame.setVisible(true);
        tempo.endTimer();
        System.out.println(tempo);
    }

    public static String getInterpretacaoDiferanca(byte[] b){
        byte valor1,valor2,sinalRed,sinalGreen,sinalBlue;
        byte blue,green,red;
        StringBuilder aux = new StringBuilder();
        for (int i=0;i<b.length/2;i=i+1){
            valor1 = b[i*2];
            valor2 = b[i*2+1];
            sinalBlue = (byte)((valor1 >> 7) & 1);
            sinalGreen = (byte)((valor1 >> 6) & 1);
            sinalRed = (byte)((valor1 >> 5) & 1);
            
            blue = (byte)0;
            green = (byte)0;
            red = (byte)0;
            
            //((ID >> position) & 1);
            
            blue = (byte) (blue | (byte)(((valor1 >> 4) & 1) << 5));
            blue = (byte) (blue | (byte)(((valor1 >> 3) & 1) << 4));
            blue = (byte) (blue | (byte)(((valor1 >> 2) & 1) << 3));
            blue = (byte) (blue | (byte)(((valor1 >> 1) & 1) << 2));
            
            green = (byte) (green | (byte)(((valor2 >> 7) & 1) << 5));
            green = (byte) (green | (byte)(((valor2 >> 6) & 1) << 4));
            green = (byte) (green | (byte)(((valor2 >> 5) & 1) << 3));
            green = (byte) (green | (byte)(((valor2 >> 4) & 1) << 2));
            
            red = (byte) (red | (byte)(((valor2 >> 3) & 1) << 5));
            red = (byte) (red | (byte)(((valor2 >> 2) & 1) << 4));
            red = (byte) (red | (byte)(((valor2 >> 1) & 1) << 3));
            red = (byte) (red | (byte)(((valor2) & 1) << 2));
            
            aux.append('(');
            if ((blue&0xFF)!=0){
                if (sinalBlue!=0){
                    aux.append('-');
                }else{
                    aux.append('+');
                }
                aux.append((blue&0xFF));
            }else{
                aux.append("+0");
            }
            aux.append(',');
            
            if ((green&0xFF)!=0){
                if (sinalGreen!=0){
                    aux.append('-');
                }else{
                    aux.append('+');
                }
                aux.append((green&0xFF));
            }else{
                aux.append("+0");
            }
            aux.append(',');
            
            if ((red&0xFF)!=0){
                if (sinalRed!=0){
                    aux.append('-');
                }else{
                    aux.append('+');
                }
                aux.append((red&0xFF));
            }else{
                aux.append("+0");
            }

            aux.append(")\n");
        }
        return(aux.toString());
    }
    
    public Imagem(byte[] byteArray,int width,int height){
        //pixel = new byte[byteArray.length];
        this.b = new byte[height][width];
        this.g = new byte[height][width];
        this.r = new byte[height][width];
        setImagem(byteArray,width,height);
        //System.arraycopy(byteArray, 0, pixel, 0, byteArray.length);
    }
    
    public void setImagem(byte[] byteArray,int width,int height){
        int bytePos;
        for (int i=0;i<height;i++){
            for (int j=0;j<width;j++){
                bytePos = 3*(i*width+j);
                b[i][j] = byteArray[bytePos];
                g[i][j] = byteArray[bytePos+1];
                r[i][j] = byteArray[bytePos+2];
            }
        }
        //pixel = byteArray;
        this.width = width;
        this.height = height;
        this.deslocamento = -1;
    }
    
    public Imagem(Imagem img){
        this.width = img.width;
        this.height = img.height;
        this.deslocamento = img.deslocamento;
        this.b = new byte[height][width];
        this.g = new byte[height][width];
        this.r = new byte[height][width];
        int iInverso = 0;
        for (int i=0;i<height;i++){
            for (int j=0;j<width;j++){
                b[i][j] = img.b[i][j];
                g[i][j] = img.g[i][j];
                r[i][j] = img.r[i][j];
            }
        }
        //this.pixel = new byte[img.pixel.length];
        //System.arraycopy(img.pixel, 0, this.pixel, 0, img.pixel.length);
    }
    
    public int getSize(){
        return(this.height*this.width);
    }
    
    /*
    public static byte[] diffImagem(Imagem a,Imagem b){
        if (a.height!=b.height || a.width!=b.width){
            throw new IllegalArgumentException("Tamanhos incompativeis em metodo diffImagem de classe Imagem!");
        }
        int numeroDePixels = a.width*a.height;
        byte[] retorno = new byte[(a.height*a.width)*2];
        int difRed,difGreen,difBlue;
        byte valor1 = 0;
        byte valor2 = 0;
        for (int i=0;i<numeroDePixels;i=i+1){
            valor1 = (byte)0;
            valor2 = (byte)0;
            difBlue = (b.pixel[i*3]&0xFF) - (a.pixel[i*3]&0xFF);
            difGreen = (b.pixel[i*3+1]&0xFF) - (a.pixel[i*3+1]&0xFF);
            difRed = (b.pixel[i*3+2]&0xFF) - (a.pixel[i*3+2]&0xFF);
            
            if (difBlue<0){
                valor1 = (byte) (valor1 | (1 << 7));
                difBlue = -difBlue;
            }
            if (difBlue>(min)){
                if (difBlue>=32){
                    valor1 = (byte) (valor1 | (1 << 4));
                    difBlue = difBlue-32;
                }
                if (difBlue>=16){
                    valor1 = (byte) (valor1 | (1 << 3));
                    difBlue = difBlue-16;
                }
                if (difBlue>=8){
                    valor1 = (byte) (valor1 | (1 << 2));
                    difBlue = difBlue-8;
                }
                if (difBlue>=4){
                    valor1 = (byte) (valor1 | (1 << 1));
                    difBlue = difBlue-4;
                }
            }
            if (difGreen<0){
                valor1 = (byte) (valor1 | (1 << 6));
                difGreen = -difGreen;
            }
            if (difGreen>(min)){
                if (difGreen>=32){
                    valor2 = (byte) (valor2 | (1 << 7));
                    difGreen = difGreen-32;
                }
                if (difGreen>=16){
                    valor2 = (byte) (valor2 | (1 << 6));
                    difGreen = difGreen-16;
                }
                if (difGreen>=8){
                    valor2 = (byte) (valor2 | (1 << 5));
                    difGreen = difGreen-8;
                }
                if (difGreen>=4){
                    valor2 = (byte) (valor2 | (1 << 4));
                }
            }
            if (difRed<0){
                valor1 = (byte) (valor1 | (1 << 5));
                difRed = -difRed;
            }
            if (difRed>(min)){
                if (difRed>=32){
                    valor2 = (byte) (valor2 | (1 << 3));
                    difRed = difRed-32;
                }
                if (difRed>=16){
                    valor2 = (byte) (valor2 | (1 << 2));
                    difRed = difRed-16;
                }
                if (difRed>=8){
                    valor2 = (byte) (valor2 | (1 << 1));
                    difRed = difRed-8;
                }
                if (difRed>=4){
                    valor2 = (byte) (valor2 | (1));
                }
            }
            retorno[i*2] = valor1;
            retorno[i*2+1] = valor2;
        }
        return(retorno);
    }
    */
    
    public void setPixels(byte[] byteArray){
        int pixelsS = width*height;
        if (byteArray.length!=pixelsS){
            throw new IllegalArgumentException("tamanhos incompativeis!");
        }
        setImagem(byteArray, width, height);
        //pixel = byteArray;
    }
    
    /*
    public Imagem(Imagem img, byte[] bytesDeDiferenca){
        this.width = img.width;
        this.height = img.height;
        this.deslocamento = img.deslocamento;
        //this.pixel = new byte[img.pixel.length];
        this.b = new byte[height][width];
        this.g = new byte[height][width];
        this.r = new byte[height][width];
        setPixels(img, bytesDeDiferenca);
    }
    */
    
    /*
    public void setPixelsIndividual(Imagem imageAnterior,int id,byte[] diff){
            byte valor1 = diff[0];
            byte valor2 = diff[1];
            byte sinalBlue = (byte)((valor1 >> 7) & 1);
            byte sinalGreen = (byte)((valor1 >> 6) & 1);
            byte sinalRed = (byte)((valor1 >> 5) & 1);
            
            byte blue = (byte)0;
            byte green = (byte)0;
            byte red = (byte)0;
            
            byte[] imgB = new byte[3];
            System.arraycopy(imageAnterior.getBytes(), id*3, imgB, 0, 3);
            
            int vBlue,vGreen,vRed;
            
            //((ID >> position) & 1);
            
            blue = (byte) (blue | (byte)(((valor1 >> 4) & 1) << 5));
            blue = (byte) (blue | (byte)(((valor1 >> 3) & 1) << 4));
            blue = (byte) (blue | (byte)(((valor1 >> 2) & 1) << 3));
            blue = (byte) (blue | (byte)(((valor1 >> 1) & 1) << 2));
            
            green = (byte) (green | (byte)(((valor2 >> 7) & 1) << 5));
            green = (byte) (green | (byte)(((valor2 >> 6) & 1) << 4));
            green = (byte) (green | (byte)(((valor2 >> 5) & 1) << 3));
            green = (byte) (green | (byte)(((valor2 >> 4) & 1) << 2));
            
            red = (byte) (red | (byte)(((valor2 >> 3) & 1) << 5));
            red = (byte) (red | (byte)(((valor2 >> 2) & 1) << 4));
            red = (byte) (red | (byte)(((valor2 >> 1) & 1) << 3));
            red = (byte) (red | (byte)(((valor2) & 1) << 2));
            
            if ((blue&0xFF)!=0){
                if ((sinalBlue&0xFF)!=0){//subtrair
                    vBlue = -(blue&0xFF);
                }else{
                    vBlue = (blue&0xFF);
                }
                vBlue = vBlue + (imgB[0]&0xFF);
                this.pixel[id*3] = (byte)vBlue;
            }else{
                this.pixel[id*3] = imgB[0];
            }

            if ((green&0xFF)!=0){
                if ((sinalGreen&0xFF)!=0){
                    vGreen = -(green&0xFF);
                }else{
                    vGreen = (green&0xFF);
                }
                vGreen = vGreen + (imgB[1]&0xFF);
                this.pixel[id*3+1] = (byte)vGreen;
            }else{
                this.pixel[id*3+1] = imgB[1];
            }
            
            if ((red&0xFF)!=0){
                if ((sinalRed&0xFF)!=0){
                    vRed = -(red&0xFF);
                }else{
                    vRed = (red&0xFF);
                }
                vRed = vRed + (imgB[2]&0xFF);
                this.pixel[id*3+2] = (byte)vRed;
            }else{
                this.pixel[id*3+2] = imgB[2];
            }
    }
    */
    
    /*
    public void setPixels(Imagem img, byte[] bytesDeDiferenca){
        if ((img.getHeight()*img.getWidth())!=(bytesDeDiferenca.length/2) || (bytesDeDiferenca.length/2)!=(this.pixel.length/3)){
            throw new IllegalArgumentException("tamanhos incompativeis!");
        }
        byte[] imgB = img.getBytes();
        byte valor1,valor2,sinalRed,sinalGreen,sinalBlue;
        byte blue,green,red;
        int vRed,vGreen,vBlue;
        //System.out.println("antes : " + img.testPrint());
        for (int i=0;i<img.getSize();i=i+1){
            valor1 = bytesDeDiferenca[i*2];
            valor2 = bytesDeDiferenca[i*2+1];
            sinalBlue = (byte)((valor1 >> 7) & 1);
            sinalGreen = (byte)((valor1 >> 6) & 1);
            sinalRed = (byte)((valor1 >> 5) & 1);
            
            blue = (byte)0;
            green = (byte)0;
            red = (byte)0;
            
            //((ID >> position) & 1);
            
            blue = (byte) (blue | (byte)(((valor1 >> 4) & 1) << 5));
            blue = (byte) (blue | (byte)(((valor1 >> 3) & 1) << 4));
            blue = (byte) (blue | (byte)(((valor1 >> 2) & 1) << 3));
            blue = (byte) (blue | (byte)(((valor1 >> 1) & 1) << 2));
            
            green = (byte) (green | (byte)(((valor2 >> 7) & 1) << 5));
            green = (byte) (green | (byte)(((valor2 >> 6) & 1) << 4));
            green = (byte) (green | (byte)(((valor2 >> 5) & 1) << 3));
            green = (byte) (green | (byte)(((valor2 >> 4) & 1) << 2));
            
            red = (byte) (red | (byte)(((valor2 >> 3) & 1) << 5));
            red = (byte) (red | (byte)(((valor2 >> 2) & 1) << 4));
            red = (byte) (red | (byte)(((valor2 >> 1) & 1) << 3));
            red = (byte) (red | (byte)(((valor2) & 1) << 2));
            
            if ((blue&0xFF)!=0){
                if ((sinalBlue&0xFF)!=0){//subtrair
                    vBlue = -(blue&0xFF);
                }else{
                    vBlue = (blue&0xFF);
                }
                vBlue = vBlue + (imgB[i*3]&0xFF);
                this.pixel[i*3] = (byte)vBlue;
            }else{
                this.pixel[i*3] = imgB[i*3];
            }

            if ((green&0xFF)!=0){
                if ((sinalGreen&0xFF)!=0){
                    vGreen = -(green&0xFF);
                }else{
                    vGreen = (green&0xFF);
                }
                vGreen = vGreen + (imgB[i*3+1]&0xFF);
                this.pixel[i*3+1] = (byte)vGreen;
            }else{
                this.pixel[i*3+1] = imgB[i*3+1];
            }
            
            if ((red&0xFF)!=0){
                if ((sinalRed&0xFF)!=0){
                    vRed = -(red&0xFF);
                }else{
                    vRed = (red&0xFF);
                }
                vRed = vRed + (imgB[i*3+2]&0xFF);
                this.pixel[i*3+2] = (byte)vRed;
            }else{
                this.pixel[i*3+2] = imgB[i*3+2];
            }
        }
        //System.out.println("byte[] : " + Imagem.getInterpretacaoDiferanca(bytesDeDiferenca) + "depois : " + this.testPrint());
    }
    */
    
    public Imagem(File file,boolean resetar){
        //pixel = new ArrayList<>();
        try{
            FileInputStream fis = new FileInputStream(file);
            byte[] intBuffer = new byte[4];
            ByteBuffer bbInt = ByteBuffer.wrap(intBuffer);
            bbInt.order(ByteOrder.LITTLE_ENDIAN);
            fis.skip(10); // deslocamento 10
            fis.read(intBuffer); // deslocamento 14
            int deslocamentoBitMap = bbInt.getInt();//onde esta a tabela bitmap
            this.deslocamento = deslocamentoBitMap;
            bbInt.rewind();
            fis.skip(4); // deslocamento 18
            fis.read(intBuffer); // deslocamento 22
            width = bbInt.getInt();
            bbInt.rewind();
            fis.read(intBuffer);//deslocamento 26
            height = bbInt.getInt();
            bbInt.rewind();
            //pixel = new byte[3*width*height];
            b = new byte[height][width];
            g = new byte[height][width];
            r = new byte[height][width];
            fis.close();
            setImagem(file,resetar);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void setImagem(File file,boolean resetar){
        FileInputStream fis;
        try {
            fis = new FileInputStream(file);
            fis.skip(deslocamento);
            int linhaTamanho = width*3 + (width)%4;
            //System.out.println("linha tam : " + linhaTamanho);
            if (resetar){
                auxBuffer = new byte[linhaTamanho*height];
                //System.out.println("tam aux : " + auxBuffer.length + ", tam pixels : " + pixel.length);
                //System.out.println("extra : " + (width%4));
                //System.out.println("deslocamento : " + deslocamento);
            }
            fis.read(auxBuffer);
            int aux = 0;
            int iInverso;
            for (int i=0;i<height;i++){
                for (int j=0;j<width;j++){
                    iInverso = height-1-i;
                    b[iInverso][j] = auxBuffer[aux];
                    aux++;
                    g[iInverso][j] = auxBuffer[aux];
                    aux++;
                    r[iInverso][j] = auxBuffer[aux];
                    aux++;
                }
                aux = aux+(width%4);
                //System.arraycopy(auxBuffer, i2*linhaTamanho, pixel,i*width*3, width*3);
            }
            fis.close();
        }
        catch (FileNotFoundException ex) {
            Logger.getLogger(Imagem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Imagem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
    
    public String testPrint(){
        StringBuilder aux = new StringBuilder();
        aux.append("size : ").append(height).append(',').append(width).append('\n');
        aux.append("size matriz b= ").append(b.length).append(',').append(b[0].length).append('\n');
        try{
            for (int i=0;i<height;i++){
                for (int j=0;j<width;j++){
                    aux.append('[').append(i).append(',').append(j).append(']').append("=").append('{');
                    aux.append(b[i][j]&0xFF).append(',');
                    aux.append(g[i][j]&0xFF).append(',');
                    aux.append(r[i][j]&0xFF).append(")\n");
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            return(aux.toString());
        }
    }
    
    public byte[] imgToBytes(){
        byte[] retorno = new byte[width*height*3];
        int aux = 0;
        for (int i=0;i<height;i++){
            for (int j=0;j<width;j++){
                retorno[aux] = this.b[i][j];
                aux++;
                retorno[aux] = this.g[i][j];
                aux++;
                retorno[aux] = this.r[i][j];
                aux++;
            }
        }
        return(retorno);
    }
    
    @Override
    public boolean equals(Object outro){
        if (outro instanceof Imagem){
            byte[][][] b1 = {this.b,this.g,this.r};
            byte[][][] b2 = {this.b,this.g,this.r};
            boolean igual = true;
            for (int j=0;j<b1.length;j++){
                for (int i=0;i<b1[0].length;i++){
                    if (Arrays.equals(b1[j][i], b2[j][i])){
                        //System.out.println("igual!");
                    }else{
                        //System.out.println("diferente");
                        igual = false;
                        break;
                    }
                }
            }
            return(igual);
        }
        return(false);
    }

    public byte[][] getR() {
        return r;
    }

    public byte[][] getG() {
        return g;
    }

    public byte[][] getB() {
        return b;
    }
}

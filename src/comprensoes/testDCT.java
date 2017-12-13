/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comprensoes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JFrame;
import model.Imagem;
import util.PTimer;
import view.imagemPanel;

/**
 *
 * @author elixandrebaldi
 */
public class testDCT {
    
    static double[][] EXEMPLO_1 = { { 154, 123, 123, 123, 123, 123, 123, 136 } ,
                           { 192, 180, 136, 154, 154, 154, 136, 110} ,
                           { 254, 198, 154, 154, 180, 154, 123, 123},
                           { 239, 180, 136, 180, 180, 166, 123, 123},
                           { 180, 154, 136, 167, 166, 149, 136, 136},
                           { 128, 136, 123, 136, 154, 180, 198, 154},
                           { 123, 105, 110, 149, 136, 136, 180, 166},
                           { 110, 136, 123, 123, 123, 136, 154, 136}};
    
    static double[][] auxPontos = { { 154, 123, 123, 123, 123, 123, 123, 136 } ,
                           { 192, 180, 136, 154, 154, 154, 136, 110} ,
                           { 254, 198, 154, 154, 180, 154, 123, 123},
                           { 239, 180, 136, 180, 180, 166, 123, 123},
                           { 180, 154, 136, 167, 166, 149, 136, 136},
                           { 128, 136, 123, 136, 154, 180, 198, 154},
                           { 123, 105, 110, 149, 136, 136, 180, 166},
                           { 110, 136, 123, 123, 123, 136, 154, 136}};
    
    static final int[][] matrizComprensao = { { 16, 11, 10, 16, 24, 40, 51, 61 } ,
                           { 12, 12, 14, 19, 26, 58, 60, 55} ,
                           { 14, 13, 16, 24, 40, 57, 69, 56},
                           { 14, 17, 22, 29, 51, 87, 80, 62},
                           { 18, 22, 37, 56, 68, 109, 103, 77},
                           { 24, 35, 55, 64, 81, 104, 113, 92},
                           { 49, 64, 78, 87, 103, 121, 120, 101},
                           { 72, 92, 95, 98, 112, 100, 103, 99}};
    
    public static byte[] zigzagueComprensao(int[][] in){
        //System.out.println("comprensao zigzag");
        int base = 1;
        int limite = in.length;
        boolean aumentar = true;
        boolean sentido = true;
        int inicio = 0;
        int var = 0;
        ArrayList< Byte > retornoA = new ArrayList<>();
        for (int i=0;i<(limite+limite-1);i++){
            //System.out.println("linha i :" + i);
            if (sentido){
                if (i<8){
                    inicio = i%8;
                    var = 0;
                }else{
                    inicio = 7;
                    var = i%7;
                }
                for (int j=0;j<base;j++){
                    //System.out.println("caso 2, inicio,j,var : " + inicio + "," + j + "," + var + ", ??? " + j + "+" + var + " : " + (j+var));
                    //System.out.println("["+(inicio-j)+"]["+(j+var)+"] :" + in[inicio-j][j+var]);
                    retornoA.add((byte)(in[inicio-j][j+var]));
                }
                if (aumentar){
                    base++;
                    if (base==limite){
                        aumentar = false;
                    }
                }else{
                    base--;
                }
                sentido = false;
            }else{
                if (i<8){
                    inicio = i%8;
                    var = 0;
                }else{
                    inicio = 7;
                    var = i%7;
                }
                for (int j=0;j<base;j++){
                    //System.out.println("caso 1");
                    //System.out.println("["+(inicio-j)+"]["+(j+var)+"] : " + in[j+var][inicio-j]);
                    retornoA.add((byte)(in[j+var][inicio-j]));
                }
                if (aumentar){
                    base++;
                    if (base==limite){
                        aumentar = false;
                    }
                }else{
                    base--;
                }
                sentido = true;
            }
        }
        retornoA.set(retornoA.size()-1, (byte)in[7][7]);
        int tamanhoArray = retornoA.size();
        for (int i=tamanhoArray-1;i>=0;i--){
            if (retornoA.get(i)!=0){
                //System.out.println("TRUE EM " + i);
                tamanhoArray = i+1;
                break;
            }
        }
        //System.out.println("tamanhoArray : " + tamanhoArray);
        byte[] retorno = new byte[tamanhoArray+1];
        retorno[0] = (byte)tamanhoArray;
        for (int i=0;i<tamanhoArray;i++){
            retorno[i+1] = retornoA.get(i);
            //System.out.println("retorno["+(i+4)+"] : " + retorno[i+4]);
        }
        return(retorno);
    }
    
    public static byte[][] zigzagDescomprensao(byte[] comp){
        int tamanho = (comp[0]&0xFF);
        //System.out.println("tamanho Descomp : " + tamanho);
        
        if (tamanho==0){
            throw new IllegalArgumentException("Tamanho nao pode ser 0");
        }
        int limite = 8;
        boolean aumentar = true;
        boolean sentido = true;
        int incremento = 1;
        
        byte[][] retorno = new byte[8][8];
        byte byteLido;
        int aux = 1;
        int posI = 0;
        int posJ = 0;
        int interacao = 1;
        int contadorBytesLidos = 0;
        while (contadorBytesLidos < tamanho){
            interacao = Math.min((tamanho-contadorBytesLidos), incremento);
            //System.out.println("tamanho, contadorBytes, incremento, interacao : " + tamanho + "," + contadorBytesLidos + ","+incremento+","+interacao);
            if (interacao < 0){
                throw new IllegalStateException("Essa parte nao devia ser acessada");
            }
            //System.out.println("aumentar em : " + incremento);
            //System.out.println("interacoa em " + interacao);
            if (sentido){
                sentido = false;
                for (int j=0;j<interacao;j++){
                    byteLido = comp[1+contadorBytesLidos];
                    contadorBytesLidos++;
                    retorno[posI-j][posJ+j] = byteLido;
                    //System.out.println("em ["+(posI-j)+"]["+(posJ+j)+"] = " + byteLido);
                }
                if (aumentar){
                    posI = 0;
                    posJ = incremento;
                }else{
                    posI = aux;
                    posJ = limite-1;
                }
            }else{
                sentido = true;
                for (int j=0;j<interacao;j++){
                    byteLido = comp[1+contadorBytesLidos];
                    contadorBytesLidos++;
                    retorno[posI+j][posJ-j] = byteLido;
                    //System.out.println("em ["+(posI+j)+"]["+(posJ-j)+"] = " + byteLido);
                }
                if (aumentar){
                    posI = incremento;
                    posJ = 0;
                }else{
                    posI = limite-1;
                    posJ = aux;
                }
            }
            if (aumentar){
                incremento++;
                if (incremento==limite){
                    aumentar = false;
                }
            }else{
                incremento--;
                aux++;
            }
        }
        return(retorno);
    }
    
    public static void salvarBuffer(double[][] pontos){
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                auxPontos[i][j] = pontos[i][j];
            }
        }
    }
    
    public static void carregarBuffer(double[][] pontos){
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                pontos[i][j] = auxPontos[i][j];
            }
        }
    }
    
    public static void printarMatrixFinal(int[][] pontosFinal){
        StringBuilder aux = new StringBuilder();
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                aux.append(pontosFinal[i][j]).append(" ");
            }
            aux.append('\n');
        }
        System.out.println(aux);
    }
    
    public static void printarMatriz(double[][] pontos){
        StringBuilder aux = new StringBuilder();
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                aux.append(pontos[i][j]).append(" ");
            }
            aux.append('\n');
        }
        System.out.println(aux);
    }
    
    public static double dcpCos(int w, int k){
        double ret = Math.cos( (2*w+1)*(Math.PI*k/16.0) );
        //System.out.println("para w : " + w + " e k : " + k + ", : " + ret);
        return( ret );
    }
    
    public static double soma(int i,int j,double[][] pontos){
        double soma = 0;
        for (int x=0;x<8;x++){
            for (int y=0;y<8;y++){
                //System.out.println("com pontos[x][y] : " + pontos[x][y]);
                soma = soma + (pontos[x][y]*dcpCos(x,i)*dcpCos(y,j));
                //System.out.println("soma : " + soma);
            }
        }
        return(soma);
    }
        
    public static void dctTransformation(double[][] pontos){
        double ci=1.0;
        double cj=1.0;
        double auxCU = 1.0/Math.sqrt(2.0);;
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                if (i==0){
                    ci = auxCU;
                }else{
                    ci = 1;
                }
                if (j==0){
                    cj = auxCU;
                }else{
                    cj = 1;
                }
                //System.out.println("para i = " + i + ",j = " + j + ", ci = " + ci + ",cj= " + cj);
                auxPontos[i][j] = 0.25*ci*cj*soma(i, j,pontos);
            }
        }
    }
    
    public static double somaInversa(int i,int j,double[][] pontos){
        double cx=1.0;
        double cy=1.0;
        double auxCU = 1.0/Math.sqrt(2.0);;
        double soma = 0;
        for (int x=0;x<8;x++){
            for (int y=0;y<8;y++){
                if (x==0){
                    cx = auxCU;
                }else{
                    cx = 1;
                }
                if (y==0){
                    cy = auxCU;
                }else{
                    cy = 1;
                }
                //System.out.println("com pontos["+x+"]["+y+"] : " + pontos[x][y]);
                soma = soma + cx*cy*(pontos[x][y]*dcpCos(i,x)*dcpCos(j,y));
                //System.out.println("soma : " + soma);
            }
        }
        return(soma);
    }
    
    public static void dctInversaTransformation(double[][] pontos){
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                auxPontos[i][j] = 0.25*somaInversa(i, j,pontos);
            }
        }
    }
    
    public static void gerarTransposta(double[][] valor){
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                auxPontos[j][i] = valor[i][j];
            }
        }
    }
    
    public static void fastDct(double[][] valor){
        for (int i=0;i<8;i++){
            FastDct.transform(valor[i]);
        }
        gerarTransposta(valor);
        for (int i=0;i<8;i++){
            FastDct.transform(auxPontos[i]);
        }
        carregarBuffer(valor);
        gerarTransposta(valor);
        carregarBuffer(valor);
    }
    
    public static void fastInversaDct(double[][] valor){
        for (int i=0;i<8;i++){
            FastDct.inverseTransform(valor[i]);
        }
        gerarTransposta(valor);
        for (int i=0;i<8;i++){
            FastDct.inverseTransform(auxPontos[i]);
        }
        carregarBuffer(valor);
        gerarTransposta(valor);
        carregarBuffer(valor);
    }

    public static byte[] comprimir(byte[][] inicioByte){
        //System.out.println("tamanho inicial : " + inicioByte.length*inicioByte[0].length);
        double[][] inicio = new double[8][8];
        int[][] pontosFinal = new int[8][8];
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                inicio[i][j] = (inicioByte[i][j]&0xFF);
            }
        }
        salvarBuffer(inicio);
        
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                inicio[i][j] = inicio[i][j]-128;
            }
        }
        //dctTransformation(inicio);
        fastDct(inicio);
        
        carregarBuffer(inicio);
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                pontosFinal[i][j] = (int)Math.round(inicio[i][j]/(matrizComprensao[i][j]+0.00));
            }
        }
        
        //System.out.println("matriz final : ");
        //printarMatrixFinal(pontosFinal);
        
        byte[] ret = zigzagueComprensao(pontosFinal);
        return(ret);
    }
    
    public static byte[][] descomprimir(byte[] comp){
        byte[][] pontosB = zigzagDescomprensao(comp);
        int[][] pontosFinal = new int[8][8];
        double[][] pontos = new double[8][8];
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                pontosFinal[i][j] = pontosB[i][j];
            }
        }
        
        //System.out.println("descomprensao passo 0 (zigzag)");
        //printarMatrixFinal(pontosFinal);
        
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                pontos[i][j] = pontosFinal[i][j]*matrizComprensao[i][j];
            }
        }
        
        //System.out.println("descomprensao passo 1(mul)");
        //printarMatriz(pontos);
        
        //dctInversaTransformation(pontos);
        fastInversaDct(pontos);
        
        carregarBuffer(pontos);
        //System.out.println("descomprensao passo 2(dct inversa)");
        //printarMatriz(pontos);
        
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                pontosFinal[i][j] = (int)Math.round(pontos[i][j]+128);
            }
        }
        //System.out.println("descomprensao passo 3(soma 128)");
        //printarMatrixFinal(pontosFinal);
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                if (pontosFinal[i][j]>255){
                    //System.out.println("OVERFLOW");
                    pontosB[i][j] = (byte)255;
                }else if (pontosFinal[i][j]<0){
                    //System.out.println("UNDERFLOW");
                    pontosB[i][j] = (byte)0;
                }else{
                    pontosB[i][j] = (byte)pontosFinal[i][j];
                }
            }
        }
        return(pontosB);
    }
    
    public static Imagem carregarImagem(byte[] bytes){
        int linha = (bytes[0]&0xFF);
        int coluna = (bytes[1]&0xFF);
        //System.out.println("linha : " + linha + " coluna : " + coluna);
        Imagem[][] imgs = new Imagem[linha][coluna];
        byte[] bComprimido;
        byte[] gComprimido;
        byte[] rComprimido;
        byte[][] bDescomprimido;
        byte[][] gDescomprimido;
        byte[][] rDescomprimido;
        int tam;
        int contadorDeslocamento = 2;
        for (int i=0;i<linha;i++){
            for (int j=0;j<coluna;j++){
                tam = (bytes[contadorDeslocamento]&0xFF);
                //System.out.println("blue : " + (tam) + " em pos " + (contadorDeslocamento-2));
                bComprimido = new byte[1+tam];
                System.arraycopy(bytes, contadorDeslocamento, bComprimido, 0, 1+tam);
                contadorDeslocamento = contadorDeslocamento+tam+1;
                
                tam = (bytes[contadorDeslocamento]&0xFF);
                //System.out.println("green : " + (tam) + " em pos " + (contadorDeslocamento-2));
                gComprimido = new byte[1+tam];
                System.arraycopy(bytes, contadorDeslocamento, gComprimido, 0, 1+tam);
                contadorDeslocamento = contadorDeslocamento+tam+1;
                
                tam = (bytes[contadorDeslocamento]&0xFF);
                //System.out.println("red : " + (tam) + " em pos " + (contadorDeslocamento-2));
                rComprimido = new byte[1+tam];
                System.arraycopy(bytes, contadorDeslocamento, rComprimido, 0, 1+tam);
                contadorDeslocamento = contadorDeslocamento+tam+1;
                //if (contadorDeslocamento-2)
                
                bDescomprimido = descomprimir(bComprimido);
                gDescomprimido = descomprimir(gComprimido);
                rDescomprimido = descomprimir(rComprimido);
                
                imgs[i][j] = new Imagem(bDescomprimido,gDescomprimido,rDescomprimido);
            }
        }
        Imagem retorno = new Imagem(imgs);
        return(retorno);
    }
    
    public static byte[] salvarImagem(Imagem img){
        Imagem[][] subImagens = img.dividirImagem();
        int linha = subImagens.length;
        int coluna = subImagens[0].length;
        byte[] aux = new byte[linha*coluna*64*4];
        System.out.println("tamanho inicial : " + aux.length + " = " + img.imgToBytes().length);
        
        int contadorTamanho = 0;
        for (int i=0;i<linha;i++){
            for (int j=0;j<coluna;j++){
                //byte[][][] canais = separarCanaisDaImagem(subImagens[i][j]);
                //byte[][] canalB = canais[0];
                //byte[][] canalG = canais[1];
                //byte[][] canalR = canais[2];
                byte[][] canalB = subImagens[i][j].getB();
                byte[][] canalG = subImagens[i][j].getG();
                byte[][] canalR = subImagens[i][j].getR();
                
                byte[] canalBComp = comprimir(canalB);
                //System.out.println("canalBlength : " + canalBComp.length);
                byte[] canalGComp = comprimir(canalG);
                //System.out.println("canalGlength : " + canalGComp.length);
                byte[] canalRComp = comprimir(canalR);
                //System.out.println("canalRlength : " + canalRComp.length);
                
                //System.out.println("blue zigzag : " + (canalBComp[0]&0xFF) + ",pos : " + contadorTamanho);
                System.arraycopy(canalBComp, 0, aux, contadorTamanho, canalBComp.length);
                //System.out.println("blue em pos : " + contadorTamanho);
                contadorTamanho = contadorTamanho+canalBComp.length;
                //System.out.println("green zigzag : " + (canalGComp[0]&0xFF) + ",pos : " + contadorTamanho);
                System.arraycopy(canalGComp, 0, aux, contadorTamanho, canalGComp.length);
                //System.out.println("green em pos : " + contadorTamanho);
                contadorTamanho = contadorTamanho+canalGComp.length;
                //System.out.println("red zigzag : " + (canalRComp[0]&0xFF) + ",pos : " + contadorTamanho);
                System.arraycopy(canalRComp, 0, aux, contadorTamanho, canalRComp.length);
                //System.out.println("canalRlength : " + canalRComp.length);
                //System.out.println("red em pos : " + contadorTamanho);
                contadorTamanho = contadorTamanho+canalRComp.length;
            }
        }
        
        //System.out.println("tamanho final : " + contadorTamanho);
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(contadorTamanho);
        byte[] retorno = new byte[6+contadorTamanho];
        System.arraycopy(bb.array(), 0, retorno, 0, 4);
        retorno[4] = (byte)linha;
        retorno[5] = (byte)coluna;
        System.arraycopy(aux, 0, retorno, 6, contadorTamanho);
        return(retorno);
    }
    
    
    public static void main(String[] args) throws FileNotFoundException, IOException{
        String dir = "/home/elixandrebaldi/1Sample01_25/";
        String nomeIn = "Sample01_000.bmp";
        PTimer t = new PTimer("tempo dct");
        String nomeOut = "out.bmp";
        File f = new File(dir+nomeIn);
        PTimer pt = new PTimer("tempo dct ");
        pt.startTimer();
        Imagem img = new Imagem(f,true);
        //System.out.println("Entrada : " + img.testPrint());
        byte[] b1 = img.imgToBytes();
        
        
        System.out.println("entrada tam : " + b1.length);
        t.startTimer();
        byte[] salvar = salvarImagem(img);
        t.endTimer();
        System.out.println(t);
        System.out.println("saida tam : " + salvar.length);
        FileOutputStream fi = new FileOutputStream(new File(dir+nomeOut));
        fi.write(salvar);
        pt.endTimer();
        System.out.println(pt);
    
        Imagem img2 = new Imagem(b1,img.getWidth(),img.getHeight());
        if (img.equals(img2)){
            System.out.println("equals!");
        }else{
            System.out.println("diferente");
        }
        
        byte[] auxB = new byte[salvar.length-4];
        System.arraycopy(salvar, 4, auxB, 0, auxB.length);
        Imagem imgSaida = carregarImagem(auxB);
        
        JFrame frame = new JFrame();
        frame.setVisible(false);
        imagemPanel p = new imagemPanel(imgSaida);
        frame.add(p);
        frame.pack();
        frame.setVisible(true);
        
        
        /*
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                aux[i][j] = (byte)(EXEMPLO_1[i][j]);
            }
        }
        comp = comprimir(aux);
        t1 = descomprimir(comp);
        aux2 = new int[8][8];
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                aux2[i][j] = (t1[i][j]&0xFF);
            }
        }
        printarMatrixFinal(aux2);
        */
        /*
        System.out.println("test2 : ");
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                aux2[i][j] = i*8+j;
            }
        }
        System.out.println("test : ");
        printarMatrixFinal(aux2);
        comp = zigzagueComprensao(aux2);
        t1 = zigzagDescomprensao(comp);
        aux2 = new int[8][8];
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                aux2[i][j] = t1[i][j];
            }
        }
        printarMatrixFinal(aux2);
        */
        
        /*
        //System.out.println("inicio");
        double[][] pontos = EXEMPLO_1;
        salvarBuffer(pontos);
        printarMatriz(pontos);
        
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                pontos[i][j] = pontos[i][j]-128;
            }
        }
        
        System.out.println("sub : ");
        printarMatriz(pontos);
        
        dctTransformation(pontos);
        //dct transformation
        carregarBuffer(pontos);
        System.out.println("dct transformation");
        printarMatriz(pontos);
        
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                pontosFinal[i][j] = (int)Math.round(pontos[i][j]/(matrizComprensao[i][j]+0.00));
            }
        }
        
        System.out.println("matriz final : ");
        printarMatrixFinal();

        
        //descomp
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                pontos[i][j] = pontosFinal[i][j]*matrizComprensao[i][j];
            }
        }
        
        zigzague(pontosFinal);
        
        System.out.println("descomprensao passo 1");
        printarMatriz(pontos);
        
        dctInversaTransformation(pontos);
        carregarBuffer(pontos);
        System.out.println("descomprensao passo 2");
        printarMatriz(pontos);
        
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                pontosFinal[i][j] = (int)Math.round(pontos[i][j]+128);
            }
        }
        printarMatrixFinal();
        */
    }
}

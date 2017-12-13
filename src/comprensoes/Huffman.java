/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comprensoes;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author FREE
 */
public class Huffman implements ComprensaoI{
    class Arvore implements Comparable{
        Arvore f1 = null;
        Arvore f2 = null;
        public int valor = -1;
        public int frequencia = 0;
        
        public Arvore(int valor){
            this.valor = valor;
        }
        
        public Arvore (Arvore av1,Arvore av2){
           this.f1 = av1;
           this.f2 = av2;
           this.frequencia = av1.frequencia+av2.frequencia;
        }

        @Override
        public int compareTo(Object o) {
            if (o instanceof Arvore){
                Arvore av = (Arvore)o;
                return(av.frequencia - this.frequencia);
            }
            throw new IllegalArgumentException();
        }
    }
    
    class ByteCell{
        int idByte = 0;
        byte[] valor;
        
        private ByteCell(){
            idByte = 0;
        }
        
        public ByteCell(int inicial){
            idByte = 0;
            valor = new byte[1];
            valor[0] = (byte)0;
        }
        
        public ByteCell[] gerarFilhos(){
            ByteCell[] ret = new ByteCell[2];
            ByteCell filho1 = new ByteCell();
            ByteCell filho2 = new ByteCell();
            ret[0] = filho1;
            ret[1] = filho2;
            if (idByte<8){
                //System.out.println("caso 1");
                filho1.valor = new byte[this.valor.length];
                filho1.idByte = this.idByte+1;
                filho2.valor = new byte[this.valor.length];
                filho2.idByte = this.idByte+1;
                System.arraycopy(this.valor, 0, filho1.valor, 0, this.valor.length);
                System.arraycopy(this.valor, 0, filho2.valor, 0, this.valor.length);
                filho1.valor[this.valor.length-1] = (byte)(filho1.valor[this.valor.length-1] | (1 << (7-idByte)));
                //System.out.println("pai idByte : " + idByte + " bytes : " + String.format("%8s", Integer.toBinaryString(this.valor[this.valor.length-1] & 0xFF)).replace(' ', '0'));
                //System.out.println("filho1 " + filho1.idByte + " bytes : " + String.format("%8s", Integer.toBinaryString(filho1.valor[this.valor.length-1] & 0xFF)).replace(' ', '0'));
                //System.out.println("filho2 " + filho2.idByte + " bytes : " + String.format("%8s", Integer.toBinaryString(filho2.valor[this.valor.length-1] & 0xFF)).replace(' ', '0'));
            }else{
                //System.out.println("caso 2");
                byte novoByte = (byte)0;
                filho1.valor = new byte[this.valor.length+1];
                filho1.idByte = 1;
                filho2.valor = new byte[this.valor.length+1];
                filho2.idByte = 1;
                System.arraycopy(this.valor, 0, filho1.valor, 0, this.valor.length);
                System.arraycopy(this.valor, 0, filho2.valor, 0, this.valor.length);
                filho2.valor[filho2.valor.length-1] = novoByte;
                novoByte = (byte)(novoByte | (1 << 7));
                filho1.valor[filho1.valor.length-1] = novoByte;
                //System.out.println("pai idByte : " + idByte + " bytes : " + String.format("%8s", Integer.toBinaryString(this.valor[this.valor.length-1] & 0xFF)).replace(' ', '0'));
                //System.out.println("filho1 " + filho1.idByte + " bytes : " + String.format("%8s", Integer.toBinaryString(filho1.valor[this.valor.length] & 0xFF)).replace(' ', '0'));
                //System.out.println("filho2 " + filho2.idByte + " bytes : " + String.format("%8s", Integer.toBinaryString(filho2.valor[this.valor.length] & 0xFF)).replace(' ', '0'));
            }
            return(ret);
        }
        
        
    }
    
    ArrayList<Arvore> aux = new ArrayList<>();
    
    public void init(){
        aux = new ArrayList<>();
        for (int i=0;i<256;i++){
            aux.add(new Arvore(i));
        }
    }
    
    public Huffman(){
        init();
    }

    @Override
    public byte[] comprimir(byte[] b) {
        for (int i=0;i<b.length;i++){
            byte v = b[i];
            //System.out.println("byte lido : " + (v&0xFF));
            aux.get((v&0xFF)).frequencia++;
        }
        Collections.sort(aux);
        int tamanho = 255;
        //junta 2 menores arvore e diminui tamanho por 1
        while (tamanho>0){
            Arvore a2 = aux.get(tamanho);
            Arvore a1 = aux.get(tamanho-1);
            Arvore novaArvore = new Arvore(a1,a2);
            //System.out.println("em tam : " + tamanho + ", fundindo " + a1.valor + " e " + a2.valor);
            for (int i=tamanho-1;i>=0;i--){
                if (novaArvore.frequencia>=aux.get(i).frequencia){
                    //swap novaArvore(tamanho) com aux(pos i)
                    aux.set(tamanho-1, aux.get(i));
                    aux.set(i, novaArvore);
                    //System.out.println("nova arvore em pos : " + i + ", com freq : " + novaArvore.frequencia);
                    break;
                }
            }
            tamanho--;
        }
        
        List<Arvore> av = new ArrayList<>();
        av.add(aux.get(0));
        Arvore avL;
        
        int[] indicesDeCodigos = new int[256];
        int contadorIndices = 0;
        byte bytebuffer = (byte)0;
        int bytebufferindice = 0;
        int tamanhoArvore = 0;
        List< Byte > arvoreBytes = new LinkedList<>();
        List< Integer > auxLista = new LinkedList<>();
        auxLista.add(0);
        while (av.size()>0){
            avL = av.get(0);
            if (auxLista.get(0)==1){
                bytebuffer = (byte)(bytebuffer | (1 << (7-bytebufferindice)) );
            }
            bytebufferindice++;
            if (bytebufferindice==8){
                arvoreBytes.add(bytebuffer);
                bytebuffer = (byte)0;
                bytebufferindice = 0;
            }
            auxLista.remove(0);
            av.remove(0);
            if (avL.valor!=-1){
                tamanhoArvore = tamanhoArvore+1;
                //System.out.println("valor " + avL.valor + " e freq : " + avL.frequencia);
                auxLista.add(0);
                indicesDeCodigos[contadorIndices] = avL.valor;
                contadorIndices++;
            }else{
                tamanhoArvore = tamanhoArvore+1;
                av.add(avL.f1);
                av.add(avL.f2);
                auxLista.add(1);
            }
        }
        if (bytebufferindice>0){
            arvoreBytes.add(bytebuffer);
        }
        byte[] retorno = new byte[4+arvoreBytes.size()+256];
        byte[] intAux = new byte[4];
        ByteBuffer bbWrapper = ByteBuffer.wrap(intAux);
        bbWrapper.putInt(tamanhoArvore);
        System.arraycopy(intAux, 0, retorno, 0, 4);
        System.out.println("arvore bytes : " + arvoreBytes.size());
        for (int i=0;i<arvoreBytes.size();i++){
            retorno[i+4] = arvoreBytes.get(i);
        }
        int posInicial = 4+arvoreBytes.size();
        for (int i=0;i<indicesDeCodigos.length;i++){
            System.out.println("escrevendo " + indicesDeCodigos[i] + " em pos " + (posInicial+i));
            retorno[posInicial+i] = (byte)indicesDeCodigos[i];
        }
        //System.out.println("freq : " + aux.get(0).frequencia);
        
        return(retorno);
    }

    @Override
    public byte[] descomprimir(byte[] b) {
        byte[] intAux = new byte[4];
        System.arraycopy(b, 0, intAux, 0, 4);
        int tamanhoArvore = ByteBuffer.wrap(intAux).getInt();
        System.out.println("tam Arvore : " + tamanhoArvore);
        int arvoreBytes = tamanhoArvore/8;
        if (tamanhoArvore%8!=0){
            arvoreBytes++;
        }
        System.out.println("arvoreBytes : " + arvoreBytes);
        
        //ler arvore
        byte byteBuffer = (byte)0;
        byte bit;
        int byteBufferCounter = 8;
        int bytesLidos = -1;
        boolean first = true;
        List<ByteCell> bytes = new LinkedList<>();
        ByteCell cellLida;
        List<byte[]> codigos = new ArrayList<>();
        int[] codigosTamanho = new int[256];
        for (int i=0;i<256;i++){
            codigos.add(null);
        }
        //cores
        int contadorCor = 0;
        int i =0;
        while (contadorCor < 256){
            //System.out.println("contador arvore : " + i);
            i++;
            if (byteBufferCounter == 8){
                bytesLidos++;
                byteBufferCounter = 0;
                byteBuffer = b[4+bytesLidos];
                //System.out.println("lendo byte : " + String.format("%8s", Integer.toBinaryString(byteBuffer & 0xFF)).replace(' ', '0'));
            }
            bit = (byte)((byteBuffer >> (7-byteBufferCounter)) & 1);
            //System.out.println("lendo bit : " + (bit&0xFF));
            if (first){
                cellLida = new ByteCell(0);
            }else{
                cellLida = bytes.get(0);
            }
            //System.out.println("cell lida : " + String.format("%8s", Integer.toBinaryString(cellLida.valor[cellLida.valor.length-1] & 0xFF)).replace(' ', '0'));
            if ((bit&0xFF)==0){ //arvore, cria uma bit divisor
                if (first==true){
                    first = false;
                }else{
                    bytes.remove(0);
                }
                ByteCell[] filhos = cellLida.gerarFilhos();
                bytes.add(filhos[0]);
                bytes.add(filhos[1]);
            }else{//cor
                byte cor = b[4+arvoreBytes+contadorCor];
                byte[] codigoCor = cellLida.valor;
                codigos.set((cor&0xFF),codigoCor);
                codigosTamanho[(cor&0xFF)] = (cellLida.valor.length-1)*8 + cellLida.idByte;
                //System.out.println("lendo cor " + contadorCor +  " : " + (cor&0xFF));
                StringBuilder auxS = new StringBuilder();
                for (int j=0;j<codigoCor.length;j++){
                    auxS.append(String.format("%8s", Integer.toBinaryString(codigoCor[j] & 0xFF)).replace(' ', '0')).append(" ");
                }
                System.out.println("codigo cor(" + cellLida.idByte + "): " + auxS);
                System.out.println("tamanho : " + codigosTamanho[(cor&0xFF)]);
                
                //System.out.println("cor deslocamento : " + (4+arvoreBytes+contadorCor));
                contadorCor++;
                bytes.remove(0);
            }
            byteBufferCounter++;
        }
        
        List< Byte > retornoAux = new ArrayList<>();
        //System.out.println("arvore bytes : " + arvoreBytes);
        return(null);
    }
    
    public static void main(String args[]){
        Huffman hf = new Huffman();
        String path = "C:\\Users\\FREE\\Desktop\\pidFrames\\Sample03_99\\testo.txt";
        File file = new File(path);
        byte[] bytes = util.fileUtil.toByteArray(file);
        byte[] comprimido = hf.comprimir(bytes);
        hf.descomprimir(comprimido);
    }
    
}

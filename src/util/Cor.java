/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author elixandrebaldi
 */
public class Cor { 
    public static int getValorInt(byte[] b){
        return ( ((255 & 0xFF) << 24) |
                ((b[2] & 0xFF) << 16) |
                ((b[1] & 0xFF) << 8)  |
                ((b[0] & 0xFF) << 0) );
    }
    
    public static String getInterpretacaoCor(byte[] b){
        StringBuilder aux = new StringBuilder();
        for (int i=0;i<b.length/2;i++){
            byte[] auxB = new byte[3];
            byte valor1,valor2,sinalRed,sinalGreen,sinalBlue;
            byte blue,green,red;
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

            aux.append(")");
            if (i!=(b.length/2-1)){
                aux.append(',');
            }
        }
        return(aux.toString());
    }
    
    public static String getInterpretacaoCorInversa(byte[] b){
        byte b2[] = new byte[2];
        b2[0] = b[1];
        b2[1] = b[0];
        return(getInterpretacaoCor(b2));
    }
}

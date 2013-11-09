/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.brittatorp.common;

import java.util.ArrayList;


/**
 *
 * @author tornbmat
 */
public class Helpers {

    static public int byteArray2Int(byte[] byteArray, int position) {
        //return (byteArray[position]*256) + byteArray[position+1];
        return ((byteArray[position] & 0xFF) << 8) | (byteArray[position + 1] & 0xFF);
    }

    static public int byteArray2Int(byte[] byteArray) {
        //return (byteArray[position]*256) + byteArray[position+1];
        return byteArray2Int(byteArray, 0);
    }

    static public short byteArray2Short(byte[] byteArray, int position) {
        //return (byteArray[position]*256) + byteArray[position+1];
        return (short) (byteArray[position] & 0xFF);
    }

    static public short byteArray2Short(byte[] byteArray) {
        //return (byteArray[position]*256) + byteArray[position+1];
        return byteArray2Short(byteArray, 0);
    }
    
    static public short[] byteArray2ShortArray(byte[] byteArray) {
    	short[] shortArray = new short[byteArray.length];
    	
    	
    	for (int i=0; i<byteArray.length; i++){
    		shortArray[i] = (short) (byteArray[i] & 0xFF);
    	}
    	
        //return (byteArray[position]*256) + byteArray[position+1];
        return shortArray;
    }

    static public void updateArray(byte[] byteArray, int position, int aWord) {
    	byteArray[position+1]=(byte)(aWord & 0xff);
    	byteArray[position]=(byte)((aWord >> 8) & 0xff);
    }

    static public void updateArray(byte[] byteArray, int position, short aByte) {
    	byteArray[position]=(byte)(aByte & 0xff);
    }

}

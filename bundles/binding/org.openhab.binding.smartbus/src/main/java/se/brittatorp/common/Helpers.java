/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.brittatorp.common;

import java.lang.reflect.Field;
import java.util.Arrays;


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

    static public String getDeclaredFieldsAsString(Object object, String separator) {
        String fields=null;
        Field field;
        String fieldName;
        String fieldValue;
    	try {
    		for(int i=0;i<object.getClass().getDeclaredFields().length;i++){
    			field = object.getClass().getDeclaredFields()[i];
    			field.setAccessible(true);
    			fieldName = field.getName();
            
        		Class<?> c = field.getType();
        		if (c == String.class){
        			fieldValue = (String)field.get(object);
        		} else if (c == String[].class){
        			fieldValue = Arrays.toString((String[])field.get(object));
        		} else if (c == boolean[].class){
        			fieldValue = Arrays.toString((boolean[])field.get(object));
        		} else if (c == int[].class){
        			fieldValue = Arrays.toString((int[])field.get(object));
        		} else if (c == short[].class){
        			fieldValue = Arrays.toString((short[])field.get(object));
        		} else {
					fieldValue = field.get(object).toString();
        		}

        		if (fields==null) {
        			fields = fieldName + "=" + fieldValue;
        		} else {
        			fields = fields + separator + fieldName + "=" + fieldValue;
        		}

    		}	
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return fields;
    }

    static public String getDeclaredFieldsAsString(Object object) {
    	return getDeclaredFieldsAsString(object, "; "); 
    }

}

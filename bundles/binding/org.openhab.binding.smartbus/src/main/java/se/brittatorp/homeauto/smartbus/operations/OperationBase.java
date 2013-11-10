/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.brittatorp.homeauto.smartbus.operations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.openhab.binding.smartbus.internal.SmartBusBinding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import se.brittatorp.homeauto.smartbus.transports.SmartbusPacket;

/**
 *
 * @author tornbmat
 */
public abstract class OperationBase {
	
	private static final Logger logger = 
			LoggerFactory.getLogger(SmartBusBinding.class);


    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    protected @interface SmartBusCommand {
        String CommandName() default "";
        int CommandCode() default 0;
        int ResponseCode() default 0;
        boolean Broadcast() default false;
    }

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    protected @interface SmartBusResponse {
        String CommandName() default "";
        int CommandCode() default 0;
        int ResponseCode() default 0;
        boolean Broadcast() default false;
    }
   
/*
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    protected @interface SmartBusField {}
*/

    
    
    protected String commandName;
    protected int commandCode;
    protected int responseCode;
    protected boolean broadcast;

    public OperationBase() {
        this.commandName = this.getClass().getAnnotation(SmartBusCommand.class).CommandName();
        this.broadcast = this.getClass().getAnnotation(SmartBusCommand.class).Broadcast();
        if (this.commandName.isEmpty()) {
        	this.commandName = this.getClass().getSimpleName();
        }
        if (this.getClass().isAnnotationPresent(SmartBusCommand.class)) {
            this.commandCode = this.getClass().getAnnotation(SmartBusCommand.class).CommandCode();
            this.responseCode = this.getClass().getAnnotation(SmartBusCommand.class).ResponseCode();
            if (this.responseCode == 0) {this.responseCode = this.commandCode + 1;}
        } else if (this.getClass().isAnnotationPresent(SmartBusResponse.class)) {
            this.commandCode = this.getClass().getAnnotation(SmartBusCommand.class).CommandCode();
            this.responseCode = this.getClass().getAnnotation(SmartBusCommand.class).ResponseCode();
            if (this.commandCode == 0) {this.commandCode = this.responseCode - 1;}
        } else {
            throw new UnsupportedOperationException("Smartbus operations must be annotated."); //To change body of generated methods, choose Tools | Templates.
        }
    }

    public OperationBase(SmartbusPacket packet) {
    	this();
    	parsePacket(packet);
    }
    
    public String getCommandName() {
        return commandName;
    }

    public int getCommandCode() {
        return commandCode;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public boolean isBroadcast() {
        return broadcast;
    }


/*
    public String getFieldValues() throws IllegalArgumentException, IllegalAccessException {
        String fields=null;
        Field field;
        String fieldName;
        String fieldValue;
        for(int i=0;i<this.getClass().getDeclaredFields().length;i++){
            field = this.getClass().getDeclaredFields()[i];
            fieldName = field.getName();
            
            Class<?> c = field.getType();
            if (c == String.class){
            	fieldValue = (String)field.get(this);
            } else if (c == String[].class){
            	fieldValue = Arrays.toString((String[])field.get(this));
            } else if (c == boolean[].class){
            	fieldValue = Arrays.toString((boolean[])field.get(this));
            } else if (c == int[].class){
            	fieldValue = Arrays.toString((int[])field.get(this));
            } else if (c == short[].class){
            	fieldValue = Arrays.toString((short[])field.get(this));
            } else {
            	fieldValue = field.get(this).toString();
            }

            if (fields==null) {
                fields = fieldName + "=" + fieldValue;
            } else {
                fields = fields + "\n" + fieldName + "=" + fieldValue;
            }

        }
        return fields;
    }
*/
    
    public static OperationBase getOperation(SmartbusPacket smartbusPacket) {
		OperationBase operationBase = getOperation(smartbusPacket.getOperationCode());
        if (operationBase == null) {
        	return null;
        }
		operationBase.parsePacket(smartbusPacket);
     	
		return operationBase;
    }

    
    public static OperationBase getOperation(int operationCode){
    	
    	String operationName;

    	switch (operationCode) {
    		case 0xEFFF : operationName = "ForwardlyReportStatus"; break;
    		case 0x0031 : operationName = "SingleChannelControl"; break;
    		default : operationName = "Unimplemented";	
    	}
    	
        String operationClassName = "se.brittatorp.homeauto.smartbus.operations." + operationName;
    	
        Class<?> operationsClass = null;
        try {
            operationsClass = Class.forName(operationClassName);
            OperationBase operationBase = (OperationBase) operationsClass.newInstance();
            logger.debug("OperationName: " + operationBase.getCommandName());
        	return operationBase;
        } catch( ClassNotFoundException e ) {
        	logger.debug("Operation not implemented: " + String.format("%04x", operationCode).toUpperCase());
        } catch (InstantiationException ex) {
        	logger.debug(ex.getMessage());
        } catch (IllegalAccessException ex) {
        	logger.debug(ex.getMessage());
        }

        return null;
    	
    }
    
    abstract public void parsePacket(SmartbusPacket packet);
    //abstract public String[] getFieldNames();
    //abstract public boolean setField(String fieldName, String value);
    //abstract public String getField(String fieldName);
    //abstract public String getFields();
}

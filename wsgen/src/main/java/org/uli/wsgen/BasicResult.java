package org.uli.wsgen;

public class BasicResult {
    int returnCode;
    String message;
    
    /**
     * @return the returnCode
     */
    public int getReturnCode() {
        return returnCode;
    }
    
    /**
     * @param returnCode the returnCode to set
     */
    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }
    
    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }
    
    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }
}

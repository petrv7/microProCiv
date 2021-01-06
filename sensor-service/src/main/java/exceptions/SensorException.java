package exceptions;

import java.io.Serializable;

public class SensorException extends Exception implements Serializable {
    public SensorException() {
        super();
    }
    public SensorException(String msg)   {
        super(msg);
    }
    public SensorException(String msg, Exception e)  {
        super(msg, e);
    }
}

package cz.muni.fi.pv217.prociv.alerting.service.exceptions;

import java.io.Serializable;

public class AlertException extends Exception implements Serializable {
    public AlertException() {
        super();
    }
    public AlertException(String msg)   {
        super(msg);
    }
    public AlertException(String msg, Exception e)  {
        super(msg, e);
    }
}

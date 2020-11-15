package cz.muni.fi.pv217.prociv.auth.service.exceptions;

import java.io.Serializable;

public class AuthException extends Exception implements Serializable {
    public AuthException() {
        super();
    }
    public AuthException(String msg)   {
        super(msg);
    }
    public AuthException(String msg, Exception e)  {
        super(msg, e);
    }
}

package com.sdkj.bbcat.bean;

import java.io.Serializable;

/**
 * Created by Mr.Yuan on 2015/12/23 0023.
 */
public class baseBean implements Serializable
{
    protected String message;
    protected String returnCode;

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getReturnCode()
    {
        return returnCode;
    }

    public void setReturnCode(String returnCode)
    {
        this.returnCode = returnCode;
    }
}

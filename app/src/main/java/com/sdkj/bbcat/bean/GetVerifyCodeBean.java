package com.sdkj.bbcat.bean;

import java.io.Serializable;

/**
 * Created by Mr.Yuan on 2015/12/23 0023.
 */
public class GetVerifyCodeBean extends  baseBean implements Serializable
{
    private String sessionId;
    public String getSessionId()
    {
        return sessionId;
    }

    public void setSessionId(String sessionId)
    {
        this.sessionId = sessionId;
    }
}

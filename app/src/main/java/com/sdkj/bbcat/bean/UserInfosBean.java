package com.sdkj.bbcat.bean;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by Mr.Yuan on 2015/12/27 0027.
 */
    private String nickname;
    private String sex;
    private String birthday;
    @Expose
    private String avatar = "http://img0.imgtn.bdimg.com/it/u=3710103736,733712610&fm=21&gp=0.jpg";
	private String baby_status;
    private String avatar

        return nickname;
    }

        this.nickname = nickname;
    }

        return sex;
    }

        this.sex = sex;
    }

        return birthday;
    }

        this.birthday = birthday;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
	
    public String getBaby_status()
    {
        return baby_status;
    }

    public void setBaby_status(String baby_status)
    {
        this.baby_status = baby_status;
    }
}
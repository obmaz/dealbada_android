package com.moon.dealocean.network.vo;

/**
 * Created by zambo on 2017-04-03.
 */

public class UserVO {
    public String thumbNailImgUrl;
    public String nickName;
    public String point;
    public String pointRank;
    public String message;
    public String scrap;

    @Override
    public String toString() {
        return "UserVO{" +
                "thumbNailImgUrl='" + thumbNailImgUrl + '\'' +
                ", nickName='" + nickName + '\'' +
                ", point='" + point + '\'' +
                ", pointRank='" + pointRank + '\'' +
                ", message='" + message + '\'' +
                ", scrap='" + scrap + '\'' +
                '}';
    }
}

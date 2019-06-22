package com.moon.dealocean.network.vo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by zambo on 2016-07-05.
 */
public class DetailItemVO implements Serializable {
    public ArrayList<LinkInfoVO> linkInfoList = new ArrayList<>();
    public String content = "";
    public int commentCount;
    public int titleMaginLeft;
    public String thumbNailImgUrl = "";
    public String title = "";
    public String nickName = "";
    public String date = "";
    public int viewCount;
    public int likeCount;

    @Override
    public String toString() {
        return "DetailItemVO{" +
                "linkInfoList=" + linkInfoList +
                ", content='" + content + '\'' +
                ", commentCount=" + commentCount +
                ", titleMaginLeft=" + titleMaginLeft +
                ", thumbNailImgUrl='" + thumbNailImgUrl + '\'' +
                ", title='" + title + '\'' +
                ", nickName='" + nickName + '\'' +
                ", date='" + date + '\'' +
                ", viewCount=" + viewCount +
                ", likeCount=" + likeCount +
                '}';
    }
}



package com.moon.dealocean.network.vo;

import java.io.Serializable;

/**
 * Created by zambo on 2016-06-27.
 */
public class BoardItemVO implements Serializable {
    public String thumbNailImgUrl = "";
    public String title = "";
    public String category = "";
    public String detailPageUrl = "";
    public String nickNameImgUrl = "";
    public String nickName = "";
    public String date = "";
    public String commentCount = "";
    public String viewCount = "";
    public boolean isDealFinish = false;

    @Override
    public String toString() {
        return "BoardItemVO{" + "\n" +
                "thumbNailImgUrl='" + thumbNailImgUrl + '\'' + "\n" +
                ", title='" + title + '\'' + "\n" +
                ", category='" + category + '\'' + "\n" +
                ", detailPageUrl='" + detailPageUrl + '\'' + "\n" +
                ", nickNameImgUrl='" + nickNameImgUrl + '\'' + "\n" +
                ", nickName='" + nickName + '\'' + "\n" +
                ", date='" + date + '\'' + "\n" +
                ", commentCount=" + commentCount + "\n" +
                ", isDealFinish=" + isDealFinish + "\n" +
                ", viewCount=" + viewCount + "\n" +
                '}';
    }
}

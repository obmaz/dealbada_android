package com.moon.dealocean.parser.dealbada;

import android.util.Log;

import com.moon.dealocean.network.vo.UserVO;

import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

/**
 * Created by zambo on 2017-04-03.
 */

public class DealBadaUserParser {

    private static DealBadaUserParser mDealBadaUserParser;

    public static DealBadaUserParser getInstance() {
        if (mDealBadaUserParser == null) {
            mDealBadaUserParser = new DealBadaUserParser();
        }
        return mDealBadaUserParser;
    }

    public UserVO getUser(Source source) {
        try {
            UserVO user = new UserVO();
            source.fullSequentialParse();
            user.nickName = source.getElementById("ol_after_hd").getFirstElement(HTMLElementName.STRONG).getContent().toString().trim();
            user.message = source.getElementById("ol_after_memo").getFirstElement(HTMLElementName.STRONG).getContent().toString().trim();
            user.point = source.getElementById("ol_after_pt").getFirstElement(HTMLElementName.STRONG).getContent().toString().trim();

            Log.d("moon", "Success logined user info");
            return user;
        } catch (Exception e) {
            Log.d("moon", "Fail logined user info: " + e.toString());
            return null;
        }
    }
}


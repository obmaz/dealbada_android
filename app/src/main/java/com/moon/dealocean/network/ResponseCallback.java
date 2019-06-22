package com.moon.dealocean.network;

import net.htmlparser.jericho.Source;

/**
 * Created by zambo on 2016-06-28.
 */
public interface ResponseCallback {
    void opSuccess(Source source);

    void onFail();
}

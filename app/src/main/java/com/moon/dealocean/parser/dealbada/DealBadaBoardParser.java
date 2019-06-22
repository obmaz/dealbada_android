package com.moon.dealocean.parser.dealbada;

import android.text.Html;
import android.util.Log;

import com.moon.dealocean.network.vo.BoardItemVO;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Source;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zambo on 2016-07-17.
 */
public class DealBadaBoardParser {
    private static DealBadaBoardParser mDealBadaBoardParser;

    public static DealBadaBoardParser getInstance() {
        if (mDealBadaBoardParser == null) {
            mDealBadaBoardParser = new DealBadaBoardParser();
        }
        return mDealBadaBoardParser;
    }

    private String getTitleWithoutChild(Element element) {
        OutputDocument outputDocument = new OutputDocument(element);
        outputDocument.remove(element.getAllElementsByClass("sound_only"));
        outputDocument.remove(element.getFirstElementByClass("cnt_cmt"));
        try {
            outputDocument.remove(element.getAllElements(HTMLElementName.IMG));
        } catch (Exception e) {
        }

        return Html.fromHtml(outputDocument.toString()).toString();
    }

    public ArrayList<BoardItemVO> getBoardItemList(Source source) {
        source.fullSequentialParse();
        List<Element> trList = source.getFirstElement(HTMLElementName.TBODY).getAllElements(HTMLElementName.TR);
        ArrayList<BoardItemVO> boardItemList = new ArrayList<>();

        for (Element tr : trList) {
            BoardItemVO boardItem = new BoardItemVO();

            try {
                Element tdSubject;
                try {
                    tdSubject = tr.getFirstElementByClass("td_subject").getFirstElement(HTMLElementName.A);
                    boardItem.title = getTitleWithoutChild(tdSubject);
                } catch (Exception e) {
                    tdSubject = tr.getFirstElementByClass("td_subject").getAllElements(HTMLElementName.A).get(1);
                    boardItem.title = getTitleWithoutChild(tdSubject);
                }

                boardItem.detailPageUrl = tdSubject.getAttributeValue("href");
                boardItem.commentCount = tdSubject.getFirstElementByClass("cnt_cmt").getTextExtractor().toString();

                try {
                    tdSubject.getFirstElement(HTMLElementName.IMG).toString();
                    boardItem.isDealFinish = true;
                } catch (Exception e) {
                    boardItem.isDealFinish = false;
                }

                if (tr.getFirstElementByClass("td_cate") != null) {
                    boardItem.category = tr.getFirstElementByClass("td_cate").getTextExtractor().toString();
                }

                if (tr.getFirstElementByClass("td_img") != null) {
                    boardItem.thumbNailImgUrl = tr.getFirstElementByClass("td_img").getFirstElement(HTMLElementName.IMG).getAttributeValue("src");
                }

                boardItem.nickName = tr.getFirstElementByClass("td_name sv_use").getFirstElementByClass("div_nickname").getTextExtractor().toString();
                boardItem.date = tr.getFirstElementByClass("td_date").getTextExtractor().toString();

                try {
                    // 국내핫딜...
                    boardItem.viewCount = tr.getAllElementsByClass("td_num").get(1).getTextExtractor().toString();
                } catch (Exception e) {
                    // 사이렌오더
                    boardItem.viewCount = tr.getFirstElementByClass("td_num_v").getTextExtractor().toString();
                }
                boardItemList.add(boardItem);
            } catch (Exception e) {
                Log.e("moon", e.getMessage());
            }
        }
        return boardItemList;
    }
}

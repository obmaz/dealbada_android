package com.moon.dealocean.parser.dealbada;

import android.util.Log;

import com.moon.dealocean.network.vo.DetailItemVO;
import com.moon.dealocean.network.vo.LinkInfoVO;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zambo on 2016-07-17.
 */
public class DealBadaDetailParser {
    private static DealBadaDetailParser mDealBadaDetailParser;

    public static DealBadaDetailParser getInstance() {
        if (mDealBadaDetailParser == null) {
            mDealBadaDetailParser = new DealBadaDetailParser();
        }
        return mDealBadaDetailParser;
    }

    public DetailItemVO getDetailMain(Source source) {
        DetailItemVO detailContent = new DetailItemVO();
        try {
            source.fullSequentialParse();
            detailContent.thumbNailImgUrl = source.getElementById("bo_v_info").getAllElements(HTMLElementName.DIV).get(0).getFirstElement(HTMLElementName.IMG).getAttributeValue("src");
            detailContent.title = source.getElementById("bo_v_info").getAllElements(HTMLElementName.DIV).get(1).getFirstElement(HTMLElementName.SPAN).getTextExtractor().toString();
            detailContent.nickName = source.getElementById("bo_v_info").getAllElements(HTMLElementName.DIV).get(1).getFirstElementByClass("div_nickname").getTextExtractor().toString();
            detailContent.date = source.getElementById("bo_v_info").getAllElements(HTMLElementName.DIV).get(1).getAllElements(HTMLElementName.SPAN).get(7).getTextExtractor().toString();

            // 레벨3이상의 게시물은 SPAN이 한개 더 있어서 9개 이후 부터 하나씩 밀림
            try {
                String viewCountNormal = source.getElementById("bo_v_info").getAllElements(HTMLElementName.DIV).get(1).getAllElements(HTMLElementName.SPAN).get(9).getTextExtractor().toString();
                detailContent.viewCount = Integer.parseInt(viewCountNormal.replaceAll("[,]", ""));
            } catch (NumberFormatException e) {
                String viewCountLevel3 = source.getElementById("bo_v_info").getAllElements(HTMLElementName.DIV).get(1).getAllElements(HTMLElementName.SPAN).get(10).getTextExtractor().toString();
                detailContent.viewCount = Integer.parseInt(viewCountLevel3.replaceAll("[,]", ""));
            }

            try {
                String commentCountNormal = source.getElementById("bo_v_info").getAllElements(HTMLElementName.DIV).get(1).getAllElements(HTMLElementName.SPAN).get(18).getTextExtractor().toString();
                detailContent.commentCount = Integer.parseInt(commentCountNormal.replaceAll("[,]", ""));
            } catch (NumberFormatException e) {
                String commentCountLevel3 = source.getElementById("bo_v_info").getAllElements(HTMLElementName.DIV).get(1).getAllElements(HTMLElementName.SPAN).get(19).getTextExtractor().toString();
                detailContent.commentCount = Integer.parseInt(commentCountLevel3.replaceAll("[,]", ""));
            }

            detailContent.content = source.getElementById("bo_v_con").toString();

            List<Element> lIElement = source.getElementById("bo_v_link").getAllElements(HTMLElementName.LI);
            for (Element lI : lIElement) {
                LinkInfoVO linkInfo = new LinkInfoVO();
                linkInfo.linkUrl = lI.getFirstElement(HTMLElementName.A).getAttributeValue("href");
                linkInfo.linkUrlText = lI.getFirstElement(HTMLElementName.STRONG).getTextExtractor().toString();
                linkInfo.linkCount = lI.getFirstElementByClass("bo_v_link_cnt").getTextExtractor().toString();
                detailContent.linkInfoList.add(linkInfo);
            }
        } catch (Exception e) {
            Log.d("moon", "getDetailMain error : " + e.toString());
        }
        return detailContent;
    }

    public ArrayList<DetailItemVO> getDetailCommentList(Source source) {
        ArrayList<DetailItemVO> detailCommentList = new ArrayList<>();
        try {
            source.fullSequentialParse();

            List<Element> commentTableList = source.getElementById("bo_vc").getAllElements(HTMLElementName.TABLE);

            for (Element commentTable : commentTableList) {
                try {
                    DetailItemVO detailItemComment = new DetailItemVO();
                    detailItemComment.thumbNailImgUrl = commentTable.getAllElements(HTMLElementName.TD).get(0).getFirstElement(HTMLElementName.IMG).getAttributeValue("src");
                    detailItemComment.nickName = commentTable.getAllElements(HTMLElementName.TD).get(1).getFirstElementByClass("div_nickname").getTextExtractor().toString();
                    detailItemComment.date = commentTable.getAllElements(HTMLElementName.TD).get(1).getFirstElement(HTMLElementName.TIME).getTextExtractor().toString();
                    detailItemComment.title = commentTable.getAllElements(HTMLElementName.TD).get(1).getFirstElement(HTMLElementName.TEXTAREA).getTextExtractor().toString();

                    try {
                        String styleString = commentTable.getAttributeValue("style");

                        if (styleString.contains("margin-left:")) {
                            int index = styleString.indexOf("margin-left:");
                            int titleMarginLeft = Integer.parseInt(styleString.substring(index + 12, index + 14)) * 2;
                            detailItemComment.titleMaginLeft = titleMarginLeft;
                        }
                    } catch (Exception e) {
                        detailItemComment.titleMaginLeft = 0;
                    }

                    String likeCount = commentTable.getAllElements(HTMLElementName.TD).get(1).getAllElementsByClass("bo_v_act_gng").get(1).getTextExtractor().toString();
                    detailItemComment.likeCount = Integer.parseInt(likeCount.replaceAll("\",\\s", ""));
                    detailCommentList.add(detailItemComment);
                } catch (Exception e) {
                    Log.e("moon", "댓글 맨 마지막 이후 TR 태그에 도달, 파싱시 맨 마지막을 알 수 없어 Exception 나는게 정상임" + e.toString());
                }
            }
        } catch (Exception e) {
            Log.d("moon", "getDetailCommentList error : " + e.toString());
        }
        return detailCommentList;
    }
}

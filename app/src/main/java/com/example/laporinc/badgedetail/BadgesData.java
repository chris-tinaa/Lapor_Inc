package com.example.laporinc.badgedetail;

import java.util.ArrayList;

public class BadgesData {

    public static String[][] data = new String[][]{
            {"Lorem Ipsum", "100"},{"Lorem Ipsum", "75"}, {"Lorem Ipsum", "25"},
            {"Lorem Ipsum", "10"}, {"Lorem Ipsum", "0"}, {"Lorem Ipsum", "10"},
            {"Lorem Ipsum", "90"}, {"Lorem Ipsum", "35"},{"Lorem Ipsum", "95"},
            {"Lorem Ipsum", "40"},{"Lorem Ipsum", "65"},{"Lorem Ipsum", "30"},
            {"Lorem Ipsum", "80"},{"Lorem Ipsum", "70"},{"Lorem Ipsum", "20"}
    };

    public static ArrayList<BadgeDetail> getListData(){
        ArrayList<BadgeDetail> list = new ArrayList<>(  );
        for (String[] aData : data){
            BadgeDetail badgeDetail = new BadgeDetail(  );
            badgeDetail.setBadgeName( aData[0] );
            badgeDetail.setProgress( Integer.parseInt( aData[1] ) );
            list.add( badgeDetail );
        }

        return list;

    }

}

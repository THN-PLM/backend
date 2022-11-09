package server.thn.Route.entity;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
@AllArgsConstructor
public class RoutePreset {
    public String[][] projectRouteName = {

            {
                    "All도 Registration",
                    "All도 Complete",
                    "Proto Registration",
                    "Proto Complete",
                    "P1 Registration",
                    "P1 Complete",
                    "P2 Registration",
                    "P2 Complete",
                    "M Registration",
                    "M Complete"
            },
            //type 0 - 선행개발 , 양산개발
    };

    public String[][] projectRouteType = {
            {"TIME", "TIME", "TIME", "TIME", "TIME", "TIME", "TIME", "TIME", "TIME", "TIME"}


    };

    public String[][] projectRouteTypeModule = {

            // 타입이 속하는 모듈명
            {"TIME", "TIME", "TIME", "TIME", "TIME", "TIME", "TIME", "TIME", "TIME", "TIME"}
    };

    /**
     * 리뷰 대상인 타입 아이디를 저장
     */
    public String[] reviewRouteList = {"4", "5", "6", "12", "16", "17", "19", "23", "25"};
    public ArrayList<String> reviewRouteArrList = new ArrayList<>(Arrays.asList(reviewRouteList));

}




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
                    "All Do Registration",
                    "All Do Complete",
                    "Proto Registration",
                    "Proto Complete",
                    "P1 Registration",
                    "P1 Complete",
                    "P2 Registration",
                    "P2 Complete",
                    "M Registration",
                    "M Complete"
            },
            //type 0 - 양산 개발

            {
                    "초도",
                    "개선"
            },

            // type 1 - 선행 개발
    };

    public String[][] projectRouteType = {
            {"TIME", "TIME", "TIME", "TIME", "TIME", "TIME", "TIME", "TIME", "TIME", "TIME"},
            {"TIME", "TIME"}

    };

    public String[][] projectRouteTypeModule = {
            {"PROJECT", "PROJECT", "PROJECT", "PROJECT", "PROJECT", "PROJECT", "PROJECT", "PROJECT", "PROJECT", "PROJECT"},
            {"PROJECT", "PROJECT"}
    };


    // ==================================== REVIEW ==================================== //
    /**
     * 리뷰 대상인 타입 아이디를 저장
     */
    public String[] reviewRouteList = {};

    public ArrayList<String> reviewRouteArrList = new ArrayList<>(Arrays.asList(reviewRouteList));
}




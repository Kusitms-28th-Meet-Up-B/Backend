package kusitms.gallae.global;


import kusitms.gallae.dto.tourapi.TourApiDto;
import lombok.NoArgsConstructor;
import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@NoArgsConstructor
public class TourApiService {

    @Value("${tour.appName}")
    private String appName;

    @Value("${tour.mobileOs}")
    private String mobileOs;

    @Value("${tour.apiKey}")
    private String apiKey;

    public List<TourApiDto> getTourDatas(String region) {

        String areaCode = changeAreaCode(region);
        Map<String, Object> resultMap = new HashMap<>();
        List<TourApiDto> data = new ArrayList<>();
        try {
            StringBuilder urlBuilder = new StringBuilder("https://apis.data.go.kr/B551011/KorService1/areaBasedList1"); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + URLEncoder.encode(apiKey, "UTF-8")); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("4", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("0", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("arrange","UTF-8") + "=" + URLEncoder.encode("Q", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("contentTypeId","UTF-8") + "=" + URLEncoder.encode("12", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("MobileOS","UTF-8") + "=" + URLEncoder.encode(mobileOs, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("MobileApp","UTF-8") + "=" + URLEncoder.encode(appName, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("areaCode","UTF-8") + "=" + URLEncoder.encode(areaCode, "UTF-8"));
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            System.out.println("Response code: " + conn.getResponseCode());
            BufferedReader rd;
            if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();

            JSONObject jsonObject = XML.toJSONObject(sb.toString());
            JSONObject response = (JSONObject)jsonObject.get("response");
            JSONObject body = (JSONObject)response.get("body");
            JSONObject items = (JSONObject) body.get("items");
            JSONArray item = (JSONArray) items.get("item");
            if (item.length() > 0){
                for(int i=0; i < item.length(); i++){
                    TourApiDto tourApiDto = new TourApiDto();
                    JSONObject itemIndex = (JSONObject) item.get(i);
                    tourApiDto.setTitle(itemIndex.getString("title"));
                    tourApiDto.setPhotoUrl(itemIndex.getString("firstimage"));
                    tourApiDto.setLink("https://parks.seoul.go.kr/template/sub/cheonho.do");
                    data.add(tourApiDto);
                }

            }
        } catch (Exception e){
            e.printStackTrace();
            resultMap.clear();
            resultMap.put("Result", "0001");
        }

        return data;
    }

    public List<TourApiDto> getTourLodgment(String region) {

        String areaCode = changeAreaCode(region);
        Map<String, Object> resultMap = new HashMap<>();
        List<TourApiDto> data = new ArrayList<>();
        try {
            StringBuilder urlBuilder = new StringBuilder("https://apis.data.go.kr/B551011/KorService1/areaBasedList1"); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + URLEncoder.encode(apiKey, "UTF-8")); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("4", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("0", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("arrange","UTF-8") + "=" + URLEncoder.encode("Q", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("contentTypeId","UTF-8") + "=" + URLEncoder.encode("32", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("MobileOS","UTF-8") + "=" + URLEncoder.encode(mobileOs, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("MobileApp","UTF-8") + "=" + URLEncoder.encode(appName, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("areaCode","UTF-8") + "=" + URLEncoder.encode(areaCode, "UTF-8"));
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            System.out.println("Response code: " + conn.getResponseCode());
            BufferedReader rd;
            if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();

            JSONObject jsonObject = XML.toJSONObject(sb.toString());
            JSONObject response = (JSONObject)jsonObject.get("response");
            JSONObject body = (JSONObject)response.get("body");
            JSONObject items = (JSONObject) body.get("items");
            JSONArray item = (JSONArray) items.get("item");
            if (item.length() > 0){
                for(int i=0; i < item.length(); i++){
                    TourApiDto tourApiDto = new TourApiDto();
                    JSONObject itemIndex = (JSONObject) item.get(i);
                    tourApiDto.setTitle(itemIndex.getString("title"));
                    tourApiDto.setPhotoUrl(itemIndex.getString("firstimage"));
                    tourApiDto.setLink("https://www.marriott.com/ko/hotels/seljw-jw-marriott-hotel-seoul/overview/");
                    data.add(tourApiDto);
                }

            }
        } catch (Exception e){
            e.printStackTrace();
            resultMap.clear();
            resultMap.put("Result", "0001");
        }

        return data;
    }

    private String changeAreaCode(String region) {
        if(region.contains("서울")) {
            return "1";
        }else if(region.contains("인천")) {
            return "2";
        }else if(region.contains("대전")) {
            return "3";
        }else if(region.contains("대구")) {
            return "4";
        }else if(region.contains("광주")) {
            return "5";
        }else if(region.contains("부산")) {
            return "6";
        }else if(region.contains("울산")) {
            return "7";
        }else if(region.contains("세종")) {
            return "8";
        }else if(region.contains("경기")) {
            return "9";
        }else if(region.contains("강원")) {
            return "10";
        }else if(region.contains("충청북")) {
            return "11";
        }else if(region.contains("충청남")) {
            return "12";
        }else if(region.contains("경상북")) {
            return "13";
        }else if(region.contains("경상남")) {
            return "14";
        }else if(region.contains("전라북")) {
            return "15";
        }else if(region.contains("전라남")) {
            return "16";
        }else if(region.contains("제주")) {
            return "17";
        }else{
            return "없음";
        }
    }


}

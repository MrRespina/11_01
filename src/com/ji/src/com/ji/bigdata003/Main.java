package com.ji.bigdata003;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.ji.http001.JiHttpClient;

public class Main {

	public static void main(String[] args) throws Exception {
		
		String addr = "http://openapi.seoul.go.kr:8088/70646b63596a69683437716f794e49/json/fsiRadioactivityInfo/1/10/";

		InputStream is;
		try {
			
			is = JiHttpClient.download(addr);
			String str = JiHttpClient.convert(is, "UTF-8");
			JSONParser jp = new JSONParser();
			
			JSONObject jo = (JSONObject) jp.parse(str);

			JSONObject jo2 = (JSONObject) jo.get("fsiRadioactivityInfo");
			JSONArray jArray = (JSONArray) jo2.get("row");
			JSONObject joMain = null;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
			System.out.println("====================");
			
			for (int i = 0; i < jArray.size(); i++) {

				joMain = (JSONObject) jArray.get(i);

				String type = (String) joMain.get("CHECK_TYPE");
				String name = (String) joMain.get("PRDT_NM");
				String origin = (String) joMain.get("PRDT_ORIGIN");
				Date ydm = sdf.parse((String)joMain.get("COL_YMD"));
				int cesium_standard = Integer.parseInt((String) joMain.get("CHECK_CESIUM_STANDARD"));
				String cesium_result = (String) joMain.get("CHECK_CESIUM_RESULT");
				int iodin_standard = Integer.parseInt((String) joMain.get("CHECK_IODIN_STANDARD"));
				String iodin_result = (String) joMain.get("CHECK_IODIN_RESULT");
				String desision = (String) joMain.get("DESISION");

				System.out.println("카테고리 : " + type);
				System.out.println("이름 : " + name);
				System.out.println("원산지 : " + origin);
				System.out.println("검사 날짜 : "+ydm);
				System.out.println("세슘 수치 : " + cesium_standard);
				System.out.println("세슘 검출정도 : " + cesium_result);
				System.out.println("아이오딘 수치 : " + iodin_standard);
				System.out.println("아이오딘 검출정도 : " + iodin_result);
				System.out.println("적합도 : "+desision);
				System.out.println("====================");

			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
	}
	
}

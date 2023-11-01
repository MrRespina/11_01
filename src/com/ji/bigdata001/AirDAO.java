package com.ji.bigdata001;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.chung.db.managaer.JiDBManager;
import com.ji.http001.JiHttpClient;

// http://openapi.seoul.go.kr:8088/70646b63596a69683437716f794e49/xml/RealtimeCityAir/1/25/
// Model > DAO
// 원래 Method는 알파벳 순으로 정령.

public class AirDAO {

	public static ArrayList<Air> getAir() throws Exception {

		String url = "http://openapi.seoul.go.kr:8088/70646b63596a69683437716f794e49/xml/RealtimeCityAir/1/25/";
		InputStream is = JiHttpClient.download(url);
		ArrayList<Air> ar = new ArrayList<Air>();
		Air a = null;

		XmlPullParserFactory xppf = XmlPullParserFactory.newInstance();
		XmlPullParser xpp = xppf.newPullParser();
		xpp.setInput(is, "UTF-8");

		int type = xpp.getEventType();
		String tagName = null;

		while (type != XmlPullParser.END_DOCUMENT) {

			if (type == XmlPullParser.START_TAG) { // <>

				tagName = xpp.getName();
				if (tagName.equals("row")) {
					a = new Air();
				}

			} else if (type == XmlPullParser.TEXT) {

				if (tagName.equals("MSRDT")) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
					Date d = sdf.parse(xpp.getText());
					a.setAir_msrdt(d);

				} else if (tagName.equals("MSRRGN_NM")) {
					a.setAir_msrrgn_nm(xpp.getText());
				} else if (tagName.equals("MSRSTE_NM")) {
					a.setAir_msrste_nm(xpp.getText());
				} else if (tagName.equals("PM10")) {
					a.setAir_pm10(Integer.parseInt(xpp.getText()));
				} else if (tagName.equals("PM25")) {
					a.setAir_pm25(Integer.parseInt(xpp.getText()));
				} else if (tagName.equals("O3")) {
					a.setAir_o3(Double.parseDouble(xpp.getText()));
				} else if (tagName.equals("IDEX_NM")) {
					a.setAir_idex_nm(xpp.getText());
					ar.add(a);
				}

			} else if (type == XmlPullParser.END_TAG) { // </>
				tagName = "";
			}

			xpp.next(); // 다음 Data로 넘어감.
			type = xpp.getEventType(); // 다음 TAG 값을 가짐.

		}

		return ar;

	}

	public static String writeAir(Air alar) {

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		int res = 0;

		try {
			con = JiDBManager.connect();
			
				sql = "INSERT INTO seoul_air VALUES(seoul_air_seq.nextval,to_date(?,'YYYY-MM-DD HH24:MI:SS'),?,?,?,?,?,?)";
				pstmt = con.prepareStatement(sql);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

				// util.Date 형 > sql.Date 형으로 변환하는 작업. (같은 이름의 Date형이지만 다른 객체이다.)
				// Date 형을 변환하면 시/분/초가 사라져서 넘어가기 떄문에 원하는 값을 넣을수가 없다. ('2023-10-31' 이런 식으로 변환되어버림.)
				
				/*	util.Date > sql.Date 변환 코드
				java.util.Date utilDate = new java.util.Date();
				utilDate = alar.get(i).getAir_msrdt();
				java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
				*/

				// 그래서 String 형으로 형변환 후에 to_date로 데이터 형으로 만들어주는 방법을 선택했다.
				
				String dat = sdf.format(alar.getAir_msrdt());

				pstmt.setString(1, dat);
				pstmt.setString(2, alar.getAir_msrrgn_nm());
				pstmt.setString(3, alar.getAir_msrste_nm());
				pstmt.setInt(4, alar.getAir_pm10());
				pstmt.setInt(5, alar.getAir_pm25());
				pstmt.setDouble(6, alar.getAir_o3());
				pstmt.setString(7, alar.getAir_idex_nm());

				res = pstmt.executeUpdate();
				
				if(res == 1) {
					return "DB 입력 성공";		
				}
				return "DB 입력 실패";
				
		} catch (Exception e) {
			e.printStackTrace();
			return "DB 입력 에러";
		} finally {
			JiDBManager.close(con, pstmt, rs);
		}

	}
	
	public static ArrayList<Air> getAirDBWithSort() {
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM seoul_air ORDER BY air_MSRDT ASC,air_msrrgn_nm ASC,air_msrste_nm ASC";	
		Air ar = null;
		ArrayList<Air> alar = new ArrayList<Air>();
		
		try {
			
			con = JiDBManager.connect();
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				
				ar = new Air(rs.getInt("air_num"),rs.getDate("air_MSRDT"),rs.getString("air_MSRRGN_NM"),rs.getString("air_MSRSTE_NM"),
						rs.getInt("air_PM10"),rs.getInt("air_PM25"),rs.getDouble("air_O3"),rs.getString("air_idex_nm"));
				alar.add(ar);
				
			}
			
			return alar;	
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {
			JiDBManager.close(con, pstmt, rs);
		}		
		
	}
	
	// .csv(Comma Separated Value)
	// 각각의 값들이 ,(comma)로 구분된 파일
	// 엑셀에서 가장 잘 열림. > 하지만 엑셀에서 UTF-8을 소화하기 힘듬.
	// 메모장에서 열 것.
	
	public static void writeToCSV(ArrayList<Air> airs) {
		
		FileOutputStream fos = null;
		try {
			// 파일 위치 지정 + 누적으로 할 지 안할지?(누적 : true, 리셋 : 암것도 안함.)
			fos = new FileOutputStream("C:/Users/sdedu/Desktop/Dev/Example/air.csv",true);
			
			// 프로그램 > 파일로 내보내기 : outputstream + 파일 쓰기 : writer , 인코딩 방식 지정
			OutputStreamWriter osw = new OutputStreamWriter(fos,"utf-8");
			
			// 더 큰 최대 용량까지 커버가 가능하도록
			BufferedWriter bw = new BufferedWriter(osw);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy,MM,dd,kk,mm");
			
			for (Air a : airs) {
				
				bw.write(sdf.format(a.getAir_msrdt())+ ",");
				bw.write(a.getAir_msrrgn_nm()+ ",");
				bw.write(a.getAir_msrste_nm()+ ",");
				bw.write(a.getAir_pm10()+ ",");
				bw.write(a.getAir_pm25()+ ",");
				bw.write(a.getAir_o3()+ ",");
				bw.write(a.getAir_idex_nm()+ "\n");
				bw.flush();
				
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

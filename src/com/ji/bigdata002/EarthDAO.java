package com.ji.bigdata002;

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

public class EarthDAO {

	// http://openapi.seoul.go.kr:8088/70646b63596a69683437716f794e49/xml/TlEtqkP/1/999/
	// pk,시군구명,수용시설명,상세주소,경도,위도
	// 1. xml Parsing ArrayList에 담기
	// 2. 담긴 정보를 DB에 Insert
	// 3. DB에 있는 정보 ArrayList에 담기(SELECT 출력)
	// 4. CSV파일로 제작.

	public static ArrayList<Earth> getEarth() throws Exception {

		String url = "http://openapi.seoul.go.kr:8088/70646b63596a69683437716f794e49/xml/TlEtqkP/1/500/";
		InputStream is = JiHttpClient.download(url);
		ArrayList<Earth> aler = new ArrayList<Earth>();
		Earth e = null;

		XmlPullParserFactory xppf = XmlPullParserFactory.newInstance();
		XmlPullParser xpp = xppf.newPullParser();
		xpp.setInput(is, "UTF-8");

		int type = xpp.getEventType();
		String tagName = null;

		while (type != XmlPullParser.END_DOCUMENT) {

			if (type == XmlPullParser.START_TAG) { // <>

				tagName = xpp.getName();
				if (tagName.equals("row")) {
					e = new Earth();
				}

			} else if (type == XmlPullParser.TEXT) {

				if (tagName.equals("SGG_NM")) {
					e.setEarth_sgg_nm(xpp.getText());
				} else if (tagName.equals("EQUP_NM")) {
					e.setEarth_equp_nm(xpp.getText());
				} else if (tagName.equals("LOC_SFPR_A")) {
					e.setEarth_loc_sfpr_a(xpp.getText());
				} else if (tagName.equals("XCORD")) {
					e.setEarth_xcord(Double.parseDouble(xpp.getText()));
				} else if (tagName.equals("YCORD")) {
					e.setEarth_ycord(Double.parseDouble(xpp.getText()));
					aler.add(e);
				}

			} else if (type == XmlPullParser.END_TAG) { // </>
				tagName = "";
			}

			xpp.next(); // 다음 Data로 넘어감.
			type = xpp.getEventType(); // 다음 TAG 값을 가짐.

		}

		return aler;

	}

	public static int insertEarthInfo(ArrayList<Earth> aler) {

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		int res = 0;
		int cnt = 0;

		try {

			con = JiDBManager.connect();

			for (int i = 0; i < aler.size(); i++) {

				sql = "INSERT INTO earth VALUES(earth_seq.nextval,?,?,?,?,?)";

				pstmt = con.prepareStatement(sql);

				pstmt.setString(1, aler.get(i).getEarth_sgg_nm());
				pstmt.setString(2, aler.get(i).getEarth_equp_nm());
				pstmt.setString(3, aler.get(i).getEarth_loc_sfpr_a());
				pstmt.setDouble(4, aler.get(i).getEarth_xcord());
				pstmt.setDouble(5, aler.get(i).getEarth_ycord());

				res = pstmt.executeUpdate();

				cnt++;
				if (cnt % 100 == 0) {
					JiDBManager.close(con, pstmt, rs);
					con = JiDBManager.connect();
				}

			}

			return cnt;

		} catch (Exception e) {
			e.printStackTrace();
			return 0;
			// TODO: handle exception
		} finally {
			JiDBManager.close(con, pstmt, rs);
		}

	}

	public static ArrayList<Earth> selectEarthInfo() {

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM earth ORDER BY SGG_NM ASC";
		ArrayList<Earth> aler = new ArrayList<Earth>();

		try {

			con = JiDBManager.connect();
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {

				Earth ea = new Earth(rs.getInt("num"), rs.getString("SGG_NM"), rs.getString("EQUP_NM"),
						rs.getString("LOC_SFPR_A"), rs.getDouble("XCORD"), rs.getDouble("YCORD"));
				aler.add(ea);

			}

			return aler;

		} catch (Exception e) { // TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {
			JiDBManager.close(con, pstmt, rs);
		}

	}

	public static void writeToCSV(ArrayList<Earth> airs) {

		FileOutputStream fos = null;
		try {
			
			fos = new FileOutputStream("C:/Users/sdedu/Desktop/Dev/Example/earth.csv", true);
			OutputStreamWriter osw = new OutputStreamWriter(fos, "utf-8");
			BufferedWriter bw = new BufferedWriter(osw);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy,MM,dd,kk,mm");

			for (Earth e : airs) {

				bw.write(e.getEarth_num() + ",");
				bw.write(e.getEarth_sgg_nm() + ",");
				bw.write(e.getEarth_equp_nm() + ",");
				bw.write(e.getEarth_loc_sfpr_a()+ ",");
				bw.write(e.getEarth_xcord() + ",");
				bw.write(e.getEarth_ycord() + "\n");
				bw.flush();

			}

		} catch (Exception e) {
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

package com.ji.bigdata002;

import java.util.ArrayList;

public class EarthController {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<Earth> ar = new ArrayList<Earth>();
		
		try {
			
			ar=getEarthMain();
			insertEarth(ar);
			
			ar=printEarth();
			createEarthCSV(ar);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static ArrayList<Earth> getEarthMain() throws Exception {

		
		System.out.println("XML 데이터 Parsing중 ...");
		Thread.sleep(2000);
		ArrayList<Earth> aler = new ArrayList<Earth>();
		aler = EarthDAO.getEarth();
		
		return aler;

	}

	public static void insertEarth(ArrayList<Earth> aler) throws InterruptedException {

		System.out.println("DB에 Parsing받은 데이터 삽입중 ...");
		Thread.sleep(2000);
		int cnt = EarthDAO.insertEarthInfo(aler);
		System.out.println(cnt + "번 만큼 DB에 입력하였습니다.");
		Thread.sleep(2000);

	}
	
	public static ArrayList<Earth> printEarth() throws InterruptedException {
		
		ArrayList<Earth> ar = new ArrayList<Earth>();
		ar = EarthDAO.selectEarthInfo();
		
		System.out.println("DB에 저장된 데이터 콘솔에 입력중 ...");
		Thread.sleep(2000);
		System.out.println("===========");
		
		for(int i=0;i<ar.size();i++) {
			
			System.out.println(ar.get(i).getEarth_num());
			System.out.println(ar.get(i).getEarth_sgg_nm());
			System.out.println(ar.get(i).getEarth_equp_nm());
			System.out.println(ar.get(i).getEarth_loc_sfpr_a());
			System.out.println(ar.get(i).getEarth_xcord());
			System.out.println(ar.get(i).getEarth_ycord());
			System.out.println("===========");
			
		}
		
		return ar;
		
	}
	
	public static void createEarthCSV(ArrayList<Earth> al) throws InterruptedException {
		
		System.out.println("입력받은 데이터 바탕으로 CSV 파일 생성중 ...");
		Thread.sleep(2000);
		EarthDAO.writeToCSV(al);
		System.out.println("CSV 파일 생성 완료.");
		
		
	}

}

package com.ji.bigdata001;

import java.util.ArrayList;

public class ConvertAirController {
	
	public static void main(String[] args) {
		
		ArrayList<Air> alar = new ArrayList<Air>();
		Air ar = new Air();
		
		alar = AirDAO.getAirDBWithSort();
		
		System.out.println("Start ConvertAirController");
		System.out.println("=====================");
		
		for (int i = 0; i < alar.size(); i++) {

			ar = alar.get(i);
			
			System.out.println("num(pk) : " + ar.getAir_num());
			System.out.println("기록일시 : " + ar.getAir_msrdt());
			System.out.println("권역 : " + ar.getAir_msrrgn_nm());
			System.out.println("구 : " + ar.getAir_msrste_nm());
			System.out.println("PM10 : " + ar.getAir_pm10());
			System.out.println("PM25 : " + ar.getAir_pm25());
			System.out.println("O3 : " + ar.getAir_o3());
			System.out.println("환경상태 : " + ar.getAir_idex_nm());
			System.out.println("=====================");

		}
		
		AirDAO.writeToCSV(alar);
		System.out.println("CSV 파일 생성 완료.");
		
	}

}

package com.ji.bigdata001;

import java.util.ArrayList;

public class GetAirController {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		ArrayList<Air> alar = new ArrayList<Air>();
		Air ar = new Air();

		try {

			System.out.println("데이터 광장에서 데이터 받는 중 .");

			alar = AirDAO.getAir();

			for (int i = 0; i < alar.size(); i++) {

				ar = alar.get(i);
				String result = AirDAO.writeAir(ar);
				System.out.println(result);

			}
			System.out.println("처리 완료.");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

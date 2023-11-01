package com.ji.bigdata002;

public class Earth {
	
	private int earth_num;
	private String earth_sgg_nm;
	private String earth_equp_nm;
	private String earth_loc_sfpr_a;
	private double earth_xcord;
	private double earth_ycord;
	
	public Earth(int earth_num, String earth_sgg_nm, String earth_equp_nm, String earth_loc_sfpr_a, double earth_xcord,
			double earth_ycord) {
		super();
		this.earth_num = earth_num;
		this.earth_sgg_nm = earth_sgg_nm;
		this.earth_equp_nm = earth_equp_nm;
		this.earth_loc_sfpr_a = earth_loc_sfpr_a;
		this.earth_xcord = earth_xcord;
		this.earth_ycord = earth_ycord;
	}

	public Earth() {
		super();
	}

	public int getEarth_num() {
		return earth_num;
	}

	public void setEarth_num(int earth_num) {
		this.earth_num = earth_num;
	}

	public String getEarth_sgg_nm() {
		return earth_sgg_nm;
	}

	public void setEarth_sgg_nm(String earth_sgg_nm) {
		this.earth_sgg_nm = earth_sgg_nm;
	}

	public String getEarth_loc_sfpr_a() {
		return earth_loc_sfpr_a;
	}

	public void setEarth_loc_sfpr_a(String earth_loc_sfpr_a) {
		this.earth_loc_sfpr_a = earth_loc_sfpr_a;
	}

	public String getEarth_equp_nm() {
		return earth_equp_nm;
	}

	public void setEarth_equp_nm(String earth_equp_nm) {
		this.earth_equp_nm = earth_equp_nm;
	}

	public double getEarth_xcord() {
		return earth_xcord;
	}

	public void setEarth_xcord(double earth_xcord) {
		this.earth_xcord = earth_xcord;
	}

	public double getEarth_ycord() {
		return earth_ycord;
	}

	public void setEarth_ycord(double earth_ycord) {
		this.earth_ycord = earth_ycord;
	}
	
	
	
}

package com.example.kds.bean;

import java.util.ArrayList;

public class Express {

	public int status;
	public String message;
	public int errCode;
	public ArrayList<Data> data;
	public String html;
	public String mailNo;
	public String expTextName;
	public String expSpellName;
	public int updata;
	public int cache;
	public String ord;
	public String tel;

	public Express(int status, String message, int errCode, ArrayList<Data> data, String html, String mailNo,
			String expTextName, String expSpellName, int updata, int cache, String ord, String tel) {
		super();
		this.status = status;
		this.message = message;
		this.errCode = errCode;
		this.data = data;
		this.html = html;
		this.mailNo = mailNo;
		this.expTextName = expTextName;
		this.expSpellName = expSpellName;
		this.updata = updata;
		this.cache = cache;
		this.ord = ord;
		this.tel = tel;
	}

}

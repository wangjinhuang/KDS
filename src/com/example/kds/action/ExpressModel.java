package com.example.kds.action;

import java.util.ArrayList;

import com.example.kds.bean.Express;
import com.example.kds.bean.HistoryExpress;
import com.example.kds.db.ExpressSqlite;
import com.google.gson.Gson;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class ExpressModel {

	Context context;
	SQLiteDatabase dbWrite;
	SQLiteDatabase dbRead;

	public ExpressModel(Context context) {
		super();
		this.context = context;
		ExpressSqlite helper = new ExpressSqlite(context);
		dbRead = helper.getReadableDatabase();
		dbWrite = helper.getWritableDatabase();
	}

	/**
	 * 查询
	 */
	public ArrayList<HistoryExpress> queryAll() {
		ArrayList<HistoryExpress> list = new ArrayList<HistoryExpress>();
		Cursor cur = dbRead.query("express", null, null, null, null, null, null);
		if (cur != null) {
			while (cur.moveToNext()) {
				HistoryExpress express = new HistoryExpress(cur.getString(cur.getColumnIndex("mailNo")),
						cur.getString(cur.getColumnIndex("expTextName")),
						cur.getString(cur.getColumnIndex("expSpellName")), cur.getString(cur.getColumnIndex("json")));
				list.add(express);

			}
		}
		return list;
	}

	/**
	 * 插入
	 */

	public void insert(Express express, String json) {

		if (isExpressExit(express)) {
			Toast.makeText(context, "已更新", Toast.LENGTH_LONG).show();
			updata(express, json);
		} else {
			ContentValues values = new ContentValues();
			values.put("expSpellName", express.expSpellName);
			values.put("expTextName", express.expTextName);
			values.put("mailNo", express.mailNo);
			values.put("json", json);
			dbWrite.insert("express", null, values);
		}
	}

	/**
	 * 更新
	 */
	public void updata(Express express, String json) {

		ContentValues values = new ContentValues();
		values.put("json", json);
		dbWrite.update("express", values, "mailNo=?", new String[] { express.mailNo });
	}

	/**
	 * 判断是否已存在
	 */
	public boolean isExpressExit(Express express) {
		Cursor cur = dbRead.query("express", null, "mailNo=?", new String[] { express.mailNo }, null, null, null);
		while (cur.moveToNext()) {
			return true;

		}
		return false;
	}
}

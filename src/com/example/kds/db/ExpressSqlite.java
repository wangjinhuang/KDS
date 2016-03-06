package com.example.kds.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ExpressSqlite extends SQLiteOpenHelper {

	public ExpressSqlite(Context context) {
		super(context, "ExpressSqlite", null, 1);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL("create table express("
				+ "_id integer primary key autoincrement,"
				+ " expTextName text not null,"
				+ " expSpellName text not null," 
				+ " mailNo text not null, json text)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		
	}

}

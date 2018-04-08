/**
 * 
 */
package com.lzf.memo.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author MJCoder
 * 
 */
public class OpenSQLite extends SQLiteOpenHelper {

	public OpenSQLite(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 数据库第一次创建时被调用(non-Javadoc)
	 * 
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite
	 *      .SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {
		// TODO Auto-generated method stub
		sqLiteDatabase
				.execSQL("CREATE TABLE \"memoGroup\"([id] INT PRIMARY KEY ASC NOT NULL UNIQUE,[name] TEXT NOT NULL)");
		sqLiteDatabase
				.execSQL("CREATE TABLE [memo]([id] INT PRIMARY KEY ASC NOT NULL UNIQUE, [title] TEXT NOT NULL, [content] TEXT NOT NULL, [notify] TIMESTAMP NOT NULL, [last_modify] TIMESTAMP NOT NULL, [group_id] INT DEFAULT (- 6003))");
	}

	/**
	 * 软件版本号发生改变时调用 (non-Javadoc)
	 * 
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite
	 *      .SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int arg1, int arg2) {
		// TODO Auto-generated method stub
		Log.v("SQLiteOpenHelper.onUpgrade", "sqLiteDatabase：" + sqLiteDatabase
				+ "；arg1：" + arg1 + "；arg2：" + arg2);
	}
}

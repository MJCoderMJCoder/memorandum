/**
 * 
 */
package com.lzf.memo.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lzf.memo.bean.Group;
import com.lzf.memo.bean.Memo;

/**
 * @author MJCoder
 * 
 */
public class ReadableDatabase {
	private static SQLiteDatabase sqLiteDatabase;
	private static ReadableDatabase readableDatabase;

	public static ReadableDatabase getInstance(Context context) {
		if (sqLiteDatabase == null) {
			sqLiteDatabase = new OpenSQLite(context, "memo.db", null, 1)
					.getWritableDatabase();
		}
		if (readableDatabase == null) {
			readableDatabase = new ReadableDatabase();
		}
		return readableDatabase;
	}

	public int selectMaxIdGroup() {
		int maxId = 0;
		Cursor cursor = sqLiteDatabase.rawQuery(
				"select max(id) from memoGroup", null);
		cursor.moveToFirst();
		maxId = cursor.getInt(0);
		cursor.close();
		return maxId;
	}

	public List<Group> selectAllGroup() {
		List<Group> groups = new ArrayList<Group>();
		Cursor cursor = sqLiteDatabase
				.rawQuery("select * from memoGroup", null);
		while (cursor.moveToNext()) {
			int id = cursor.getInt(cursor.getColumnIndex("id"));
			String name = cursor.getString(cursor.getColumnIndex("name"));
			groups.add(new Group(id, name));
		}
		cursor.close();
		return groups;
	}

	public Group selectGroup(int selectId) {
		List<Group> groups = new ArrayList<Group>();
		Cursor cursor = sqLiteDatabase.rawQuery(
				"select * from memoGroup where id=?", new String[] { ""
						+ selectId });
		cursor.moveToFirst();
		int id = cursor.getInt(cursor.getColumnIndex("id"));
		String name = cursor.getString(cursor.getColumnIndex("name"));
		Group group = new Group(id, name);
		cursor.close();
		return group;
	}

	public int selectMaxIdMemo() {
		int maxId = 0;
		Cursor cursor = sqLiteDatabase.rawQuery("select max(id) from memo",
				null);
		cursor.moveToFirst();
		maxId = cursor.getInt(0);
		cursor.close();
		return maxId;
	}

	public int countAllMemo() {
		int total = 0;
		Cursor cursor = sqLiteDatabase.rawQuery("select count(*) from memo",
				null);
		cursor.moveToFirst();
		total = cursor.getInt(0);
		cursor.close();
		return total;
	}

	public List<Memo> selectMemoLimit(int maxResult, int offset) {
		List<Memo> memos = new ArrayList<Memo>();
		Cursor cursor = sqLiteDatabase
				.rawQuery(
						"select * from memo order by last_modify desc limit ? offset ? ",
						new String[] { String.valueOf(maxResult),
								String.valueOf(offset * maxResult) });
		while (cursor.moveToNext()) {
			int id = cursor.getInt(cursor.getColumnIndex("id"));
			String title = cursor.getString(cursor.getColumnIndex("title"));
			String content = cursor.getString(cursor.getColumnIndex("content"));
			long notify = cursor.getLong(cursor.getColumnIndex("notify"));
			long last_modify = cursor.getLong(cursor
					.getColumnIndex("last_modify"));
			int group = cursor.getInt(cursor.getColumnIndex("group_id"));
			memos.add(new Memo(id, title, content, notify, last_modify, group));
		}
		cursor.close();
		return memos;
	}

	public Memo selectMemo(int selectId) {
		List<Memo> memos = new ArrayList<Memo>();
		Cursor cursor = sqLiteDatabase.rawQuery(
				"select * from memo where id = ?",
				new String[] { "" + selectId });
		cursor.moveToFirst();
		int id = cursor.getInt(cursor.getColumnIndex("id"));
		String title = cursor.getString(cursor.getColumnIndex("title"));
		String content = cursor.getString(cursor.getColumnIndex("content"));
		long notify = cursor.getLong(cursor.getColumnIndex("notify"));
		long last_modify = cursor.getLong(cursor.getColumnIndex("last_modify"));
		int group = cursor.getInt(cursor.getColumnIndex("group_id"));
		Memo memo = new Memo(id, title, content, notify, last_modify, group);
		cursor.close();
		return memo;
	}

	public List<Memo> search(int groupId, String search) {
		List<Memo> memos = new ArrayList<Memo>();
		Cursor cursor = sqLiteDatabase
				.rawQuery(
						"select * from memo where group_id = ? and (title like '%'||?||'%' or content like '%'||?||'%') order by last_modify desc ",
						new String[] { groupId + "", search, search });
		while (cursor.moveToNext()) {
			int id = cursor.getInt(cursor.getColumnIndex("id"));
			String title = cursor.getString(cursor.getColumnIndex("title"));
			String content = cursor.getString(cursor.getColumnIndex("content"));
			long notify = cursor.getLong(cursor.getColumnIndex("notify"));
			long last_modify = cursor.getLong(cursor
					.getColumnIndex("last_modify"));
			int group = cursor.getInt(cursor.getColumnIndex("group_id"));
			memos.add(new Memo(id, title, content, notify, last_modify, group));
		}
		cursor.close();
		return memos;
	}

	public List<Memo> search(String search) {
		List<Memo> memos = new ArrayList<Memo>();
		Cursor cursor = sqLiteDatabase
				.rawQuery(
						"select * from memo where (title like '%'||?||'%' or content like '%'||?||'%') order by last_modify desc ",
						new String[] { search, search });
		while (cursor.moveToNext()) {
			int id = cursor.getInt(cursor.getColumnIndex("id"));
			String title = cursor.getString(cursor.getColumnIndex("title"));
			String content = cursor.getString(cursor.getColumnIndex("content"));
			long notify = cursor.getLong(cursor.getColumnIndex("notify"));
			long last_modify = cursor.getLong(cursor
					.getColumnIndex("last_modify"));
			int group = cursor.getInt(cursor.getColumnIndex("group_id"));
			memos.add(new Memo(id, title, content, notify, last_modify, group));
		}
		cursor.close();
		return memos;
	}

	public List<Memo> selectNotify() {
		List<Memo> memos = new ArrayList<Memo>();
		Cursor cursor = sqLiteDatabase.rawQuery(
				"select * from memo where last_modify < notify", null);
		while (cursor.moveToNext()) {
			int id = cursor.getInt(cursor.getColumnIndex("id"));
			String title = cursor.getString(cursor.getColumnIndex("title"));
			String content = cursor.getString(cursor.getColumnIndex("content"));
			long notify = cursor.getLong(cursor.getColumnIndex("notify"));
			long last_modify = cursor.getLong(cursor
					.getColumnIndex("last_modify"));
			int group = cursor.getInt(cursor.getColumnIndex("group_id"));
			memos.add(new Memo(id, title, content, notify, last_modify, group));
		}
		cursor.close();
		return memos;
	}
}

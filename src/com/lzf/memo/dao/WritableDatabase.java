/**
 * 
 */
package com.lzf.memo.dao;

import com.lzf.memo.bean.Group;
import com.lzf.memo.bean.Memo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author MJCoder
 * 
 */
public class WritableDatabase {
	private static SQLiteDatabase sqLiteDatabase;
	private static WritableDatabase writableDatabase;

	public static WritableDatabase getInstance(Context context) {
		if (sqLiteDatabase == null) {
			sqLiteDatabase = new OpenSQLite(context, "memo.db", null, 1)
					.getWritableDatabase();
		}
		if (writableDatabase == null) {
			writableDatabase = new WritableDatabase();
		}
		return writableDatabase;
	}

	public void addGroup(Group group) {
		sqLiteDatabase.execSQL("insert into memoGroup (id, name) values (?,?)",
				group.toObjects());
	}

	public void deleteGroup(Group group) {
		sqLiteDatabase.execSQL("delete from memoGroup where id = ?",
				new Object[] { group.getId() });
	}

	public void updateGroup(Group group) {
		sqLiteDatabase.execSQL("update memoGroup set name = ? where id = ?",
				new Object[] { group.getName(), group.getId() });
	}

	public void addMemo(Memo memo) {
		sqLiteDatabase
				.execSQL(
						"insert into memo (id, title, content, notify, last_modify, group_id) values (?,?,?,?,?,?)",
						memo.toObjects());
	}

	public void updateMemo(Memo memo) {
		sqLiteDatabase
				.execSQL(
						"update memo set title = ?, content=?, notify=?, last_modify=?, group_id=? where id=?",
						new Object[] { memo.getTitle(), memo.getContent(),
								memo.getNotify(), memo.getLast_modify(),
								memo.getGroup(), memo.getId() });
	}

	public void deleteMemo(Memo memo) {
		sqLiteDatabase.execSQL("delete from memo where id=?",
				new Object[] { memo.getId() });
	}
}

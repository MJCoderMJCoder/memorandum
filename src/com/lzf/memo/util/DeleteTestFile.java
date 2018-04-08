package com.lzf.memo.util;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by MJCoder on 2017-10-09.
 */
public class DeleteTestFile {
	/*
	 * try { FileOutputStream output =
	 * DataLoadActivity.this.openFileOutput("NEWPLANTRAININGLIST.txt",
	 * Context.MODE_PRIVATE); output.write(jsonTemp.getBytes());
	 * //将String字符串以字节流的形式写入到输出流中 output.close(); } catch (FileNotFoundException
	 * e) { e.printStackTrace(); } catch (IOException e) { e.printStackTrace();
	 * }
	 */
	public static void txtFile(String strTemp) {
		FileOutputStream fos = null;
		try {
			// 文件复制到sd卡中
			fos = new FileOutputStream(Environment
					.getExternalStorageDirectory().getAbsolutePath()
					+ "/copyTxt.txt");
			fos.write(strTemp.getBytes()); // 将String字符串以字节流的形式写入到输出流中
			fos.close();

			fos.flush();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭数据流
			try {
				if (fos != null)
					fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public static void dbFile(String packageName) {
		Log.v("packageName", packageName);
		// 找到文件的路径 /data/data/包名/databases/数据库名称
		File dbFile = new File("/data/data/com.lzf.memo/databases/memo.db");
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			// 文件复制到sd卡中
			fis = new FileInputStream(dbFile);
			fos = new FileOutputStream(Environment
					.getExternalStorageDirectory().getAbsolutePath()
					+ "/copyMemo.db");
			int len = 0;
			byte[] buffer = new byte[2048];
			while (-1 != (len = fis.read(buffer))) {
				fos.write(buffer, 0, len);
			}
			fos.flush();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭数据流
			try {
				if (fos != null)
					fos.close();
				if (fis != null)
					fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public static void sharedPrefsFile(String packageName) {
		// 找到文件的路径 /data/data/包名/databases/数据库名称
		File dbFile = new File(Environment.getDataDirectory().getAbsolutePath()
				+ "/data/" + packageName + "/shared_prefs/basicInfoSet.xml");
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			// 文件复制到sd卡中
			fis = new FileInputStream(dbFile);
			fos = new FileOutputStream(Environment
					.getExternalStorageDirectory().getAbsolutePath()
					+ "/basicInfoSet.xml");
			int len = 0;
			byte[] buffer = new byte[2048];
			while (-1 != (len = fis.read(buffer))) {
				fos.write(buffer, 0, len);
			}
			fos.flush();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭数据流
			try {
				if (fos != null)
					fos.close();
				if (fis != null)
					fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
}

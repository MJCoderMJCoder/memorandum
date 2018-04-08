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
	 * //��String�ַ������ֽ�������ʽд�뵽������� output.close(); } catch (FileNotFoundException
	 * e) { e.printStackTrace(); } catch (IOException e) { e.printStackTrace();
	 * }
	 */
	public static void txtFile(String strTemp) {
		FileOutputStream fos = null;
		try {
			// �ļ����Ƶ�sd����
			fos = new FileOutputStream(Environment
					.getExternalStorageDirectory().getAbsolutePath()
					+ "/copyTxt.txt");
			fos.write(strTemp.getBytes()); // ��String�ַ������ֽ�������ʽд�뵽�������
			fos.close();

			fos.flush();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// �ر�������
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
		// �ҵ��ļ���·�� /data/data/����/databases/���ݿ�����
		File dbFile = new File("/data/data/com.lzf.memo/databases/memo.db");
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			// �ļ����Ƶ�sd����
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
			// �ر�������
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
		// �ҵ��ļ���·�� /data/data/����/databases/���ݿ�����
		File dbFile = new File(Environment.getDataDirectory().getAbsolutePath()
				+ "/data/" + packageName + "/shared_prefs/basicInfoSet.xml");
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			// �ļ����Ƶ�sd����
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
			// �ر�������
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

package com.lzf.memo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.lzf.memo.bean.Memo;
import com.lzf.memo.dao.ReadableDatabase;
import com.lzf.memo.dao.WritableDatabase;
import com.lzf.memo.fragment.GroupFragment;
import com.lzf.memo.fragment.ListFragment;
import com.lzf.memo.fragment.NotifyFragment;
import com.lzf.memo.fragment.SearchFragment;
import com.lzf.memo.util.DeleteTestFile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("NewApi")
public class MainActivity extends Activity implements OnClickListener {
	private TextView listTxt;
	private TextView searchTxt;
	private TextView notifyTxt;
	private TextView groupTxt;
	private ImageView listIcon;
	private ImageView searchIcon;
	private ImageView notifyIcon;
	private ImageView groupIcon;
	private ImageView notifyRed;

	private FragmentManager fragmentManager;
	private ListFragment listFragment;
	private SearchFragment searchFragment;
	private NotifyFragment notifyFragment;
	private GroupFragment groupFragment;

	private boolean isNotify = true;
	private List<Memo> notifyMemo;
	private List<Memo> notifyShow = new ArrayList<Memo>();
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
			"yyyyMMddHHmm");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		listTxt = (TextView) findViewById(R.id.listTxt);
		searchTxt = (TextView) findViewById(R.id.searchTxt);
		notifyTxt = (TextView) findViewById(R.id.notifyTxt);
		groupTxt = (TextView) findViewById(R.id.groupTxt);
		listIcon = (ImageView) findViewById(R.id.listIcon);
		searchIcon = (ImageView) findViewById(R.id.searchIcon);
		notifyIcon = (ImageView) findViewById(R.id.notifyIcon);
		groupIcon = (ImageView) findViewById(R.id.groupIcon);
		notifyRed = (ImageView) findViewById(R.id.notifyRed);
		findViewById(R.id.listMenu).setOnClickListener(this);
		findViewById(R.id.searchMenu).setOnClickListener(this);
		findViewById(R.id.notifyMenu).setOnClickListener(this);
		findViewById(R.id.groupMenu).setOnClickListener(this);
		resetSelected();
		fragmentManager = getFragmentManager();
		findViewById(R.id.listMenu).performClick();

		WritableDatabase.getInstance(this);
		DeleteTestFile.dbFile(getPackageCodePath());

		new Thread() {
			public void run() {
				while (isNotify) {
					try {
						Thread.sleep(2000);
						notifyMemo = ReadableDatabase.getInstance(
								MainActivity.this).selectNotify();
						for (Memo temp : notifyMemo) {
							if (!isContains(temp)) {
								if (Long.parseLong(simpleDateFormat.format(temp
										.getNotify())) == Long
										.parseLong(simpleDateFormat
												.format(System
														.currentTimeMillis()))) {
									notifyShow.add(temp);
									runOnUiThread(new Runnable() {
										@Override
										public void run() {
											// TODO Auto-generated method stub
											notifyRed
													.setVisibility(View.VISIBLE);
											MediaPlayer mediaPlayer = MediaPlayer
													.create(MainActivity.this,
															R.raw.speed);
											mediaPlayer.setLooping(false);
											mediaPlayer.start();
										}
									});
								}
							}
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			};
		}.start();
	}

	private boolean isContains(Memo memo) {
		for (Memo temp : notifyShow) {
			if (temp.getId() == memo.getId()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		resetSelected();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		hideAllFragment(fragmentTransaction);
		switch (view.getId()) {
		case R.id.listMenu:
			listIcon.setSelected(true);
			listTxt.setSelected(true);
			if (listFragment == null) {
				listFragment = new ListFragment();
				fragmentTransaction.add(R.id.frameLayout, listFragment);
			} else {
				listFragment.onResume();
				fragmentTransaction.show(listFragment);
			}
			break;
		case R.id.searchMenu:
			searchIcon.setSelected(true);
			searchTxt.setSelected(true);
			if (searchFragment == null) {
				searchFragment = new SearchFragment();
				fragmentTransaction.add(R.id.frameLayout, searchFragment);
			} else {
				searchFragment.onResume();
				fragmentTransaction.show(searchFragment);
			}
			break;
		case R.id.notifyMenu:
			notifyIcon.setSelected(true);
			notifyTxt.setSelected(true);
			if (notifyFragment == null) {
				notifyFragment = new NotifyFragment(notifyShow);
				fragmentTransaction.add(R.id.frameLayout, notifyFragment);
			} else {
				notifyFragment.setNotifyCourier(notifyShow);
				notifyFragment.onResume();
				fragmentTransaction.show(notifyFragment);
			}
			break;
		case R.id.groupMenu:
			groupIcon.setSelected(true);
			groupTxt.setSelected(true);
			if (groupFragment == null) {
				groupFragment = new GroupFragment();
				fragmentTransaction.add(R.id.frameLayout, groupFragment);
			} else {
				groupFragment.onResume();
				fragmentTransaction.show(groupFragment);
			}
			break;
		default:
			break;
		}
		fragmentTransaction.commit();

	}

	/**
	 * 隐藏所有Fragment
	 * 
	 * @param fragmentTransaction
	 */
	private void hideAllFragment(FragmentTransaction fragmentTransaction) {
		if (listFragment != null)
			fragmentTransaction.hide(listFragment);
		if (notifyFragment != null)
			fragmentTransaction.hide(notifyFragment);
		if (searchFragment != null)
			fragmentTransaction.hide(searchFragment);
		if (groupFragment != null)
			fragmentTransaction.hide(groupFragment);
	}

	/**
	 * 重置所有文本的选中状态
	 */
	private void resetSelected() {
		listTxt.setSelected(false);
		searchTxt.setSelected(false);
		notifyTxt.setSelected(false);
		groupTxt.setSelected(false);
		listIcon.setSelected(false);
		searchIcon.setSelected(false);
		notifyIcon.setSelected(false);
		groupIcon.setSelected(false);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		isNotify = false;
	}
}

/**
 * 
 */
package com.lzf.memo.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.lzf.memo.AddModifyActivity;
import com.lzf.memo.MainActivity;
import com.lzf.memo.R;
import com.lzf.memo.bean.Memo;
import com.lzf.memo.util.ReusableAdapter;

/**
 * @author MJCoder
 * 
 */
@SuppressLint("NewApi")
public class NotifyFragment extends Fragment {
	private ReusableAdapter adapter;
	private ListView notifyList;
	private TextView noData;

	private List<Memo> notifyShow = new ArrayList<Memo>();
	private List<Memo> notifyCourier;
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
			"yyyyƒÍMM‘¬dd»’ HH:mm");

	public NotifyFragment(List<Memo> notifyCourier) {
		super();
		this.notifyCourier = notifyCourier;
	}

	public void setNotifyCourier(List<Memo> notifyCourier) {
		this.notifyCourier = notifyCourier;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater
				.inflate(R.layout.fragment_notify, container, false);
		noData = (TextView) view.findViewById(R.id.noData);
		notifyList = (ListView) view.findViewById(R.id.notifyList);
		adapter = new ReusableAdapter<Memo>(notifyShow, R.layout.item_memo_list) {

			@Override
			public void bindView(
					com.lzf.memo.util.ReusableAdapter.ViewHolder holder,
					Memo obj) {
				// TODO Auto-generated method stub
				holder.setText(R.id.lastModifyTime,
						simpleDateFormat.format(obj.getNotify()));
				holder.setText(R.id.title, obj.getTitle());
				holder.setText(R.id.content, obj.getContent());
			}

		};
		notifyList.setAdapter(adapter);
		notifyList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Memo memo = notifyShow.get(arg2);
				Intent intent = new Intent(getActivity(),
						AddModifyActivity.class);
				intent.putExtra("oprate", "look");
				intent.putExtra("memo", memo);
				startActivity(intent);
			}
		});
		if (notifyShow == null || notifyShow.size() < 1) {
			noData.setVisibility(View.VISIBLE);
		} else {
			noData.setVisibility(View.GONE);
		}
		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		notifyShow.clear();
		notifyShow.addAll(notifyCourier);
		if (notifyShow == null || notifyShow.size() < 1) {
			noData.setVisibility(View.VISIBLE);
		} else {
			noData.setVisibility(View.GONE);
		}
		adapter.notifyDataSetChanged();
	}
}

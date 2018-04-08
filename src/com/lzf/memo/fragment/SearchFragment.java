/**
 * 
 */
package com.lzf.memo.fragment;

import java.text.SimpleDateFormat;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.lzf.memo.AddModifyActivity;
import com.lzf.memo.R;
import com.lzf.memo.bean.Group;
import com.lzf.memo.bean.Memo;
import com.lzf.memo.dao.ReadableDatabase;
import com.lzf.memo.dao.WritableDatabase;
import com.lzf.memo.util.ReusableAdapter;

/**
 * @author MJCoder
 * 
 */
@SuppressLint("NewApi")
public class SearchFragment extends Fragment {
	private List<Group> groups;
	private Group selectedGroup = null;
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
			"yyyy��MM��dd�� HH:mm");
	private boolean isManuaSelect = false;// �ж��Ƿ�Ϊ�ս�ȥʱ����onItemSelected�ı�־
	private List<Memo> memos;
	private Memo memo;
	private int page = 0;
	private int totalCount;

	private TextView valueGroup;
	private EditText edit;
	private Spinner spinner;
	private ListView memoList;
	private ReusableAdapter<Memo> adapter;
	private View footerView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		final View view = inflater.inflate(R.layout.fragment_search, container,
				false);
		edit = (EditText) view.findViewById(R.id.edit);
		valueGroup = (TextView) view.findViewById(R.id.valueGroup);
		spinner = (Spinner) view.findViewById(R.id.spinner);
		groups = ReadableDatabase.getInstance(getActivity()).selectAllGroup();
		spinner.setAdapter(new ReusableAdapter<Group>(groups,
				R.layout.item_group_spinner) {

			@Override
			public void bindView(
					com.lzf.memo.util.ReusableAdapter.ViewHolder holder,
					Group obj) {
				// TODO Auto-generated method stub
				holder.setText(R.id.groupName, obj.getName());
			}
		});
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				Log.v("onItemSelected", arg0 + "��" + arg1 + "��" + arg2 + "��"
						+ arg3);
				if (isManuaSelect) {
					selectedGroup = groups.get(arg2);
					valueGroup.setText(selectedGroup.getName());
				} else {
					if (groups.size() > 0) {
						selectedGroup = groups.get(0);
						valueGroup.setText(groups.get(0).getName());
					} else {
						valueGroup.setText("Ĭ�Ϸ���");
					}
				}
				isManuaSelect = true;
				view.findViewById(R.id.search).performClick();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				Log.v("onNothingSelected", arg0 + "");
				selectedGroup = null;
				view.findViewById(R.id.search).performClick();
			}
		});
		totalCount = ReadableDatabase.getInstance(getActivity()).countAllMemo();
		memos = ReadableDatabase.getInstance(getActivity()).selectMemoLimit(20,
				page);
		memoList = (ListView) view.findViewById(R.id.memoList);
		footerView = LayoutInflater.from(getActivity()).inflate(
				R.layout.loading, null, false);
		memoList.addFooterView(footerView, null, false); // isSelectable����Ϊ����ѡ��ҳ����ͼ
		memoList.setOnScrollListener(new OnScrollListener() {
			/** ��¼��һ��Item����ֵ */
			private int firstVisibleItem;

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// ���������ײ�ʱ
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
						&& firstVisibleItem != 0) {
					if (totalCount > memos.size()) {
						loading();
					}
				}
			}

			/*
			 * firstVisibleItem��ʾ����ʱ��Ļ��һ��ListItem(������ʾ��ListItemҲ��)������ListView��λ��
			 * ���±��0��ʼ��
			 * 
			 * visibleItemCount��ʾ����ʱ��Ļ���Լ�����ListItem(������ʾ��ListItemҲ��)����
			 * 
			 * totalItemCount��ʾListView��ListItem����
			 */
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				this.firstVisibleItem = firstVisibleItem;

				// �жϿ���Item�Ƿ����ڵ�ǰҳ����ȫ��ʾ
				if (visibleItemCount == totalItemCount
						|| totalCount <= memos.size()) {
					// removeFooterView(footerView);
					footerView.setVisibility(View.GONE);// ���صײ�����
				} else {
					// addFooterView(footerView);
					footerView.setVisibility(View.VISIBLE);// ��ʾ�ײ�����
				}
			}
		});
		adapter = new ReusableAdapter<Memo>(memos, R.layout.item_memo_list) {

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
		memoList.setAdapter(adapter);
		memoList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				memo = memos.get(arg2);

				final String[] operate = new String[] { "�鿴", "�༭", "ɾ��" };
				AlertDialog alert = null;
				Builder builder = new Builder(getActivity());
				alert = builder
						.setTitle("ѡ��һ����в���")
						.setItems(operate,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										Log.v("ѡ����", operate[which]);
										Intent intent = new Intent(
												getActivity(),
												AddModifyActivity.class);
										switch (which) {
										case 0: // �鿴
											intent.putExtra("oprate", "look");
											intent.putExtra("memo", memo);
											startActivity(intent);
											break;
										case 1: // �༭
											intent.putExtra("oprate", "modify");
											intent.putExtra("memo", memo);
											startActivity(intent);
											break;
										case 2: // ɾ��
											WritableDatabase.getInstance(
													getActivity()).deleteMemo(
													memo);
											adapter.delete(memo);
											break;
										default:
											break;
										}
									}
								}).create();
				alert.show();
			}
		});
		view.findViewById(R.id.search).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Log.v("searchClick", arg0 + "");
						memos.clear();
						if (selectedGroup != null) {
							memos.addAll(ReadableDatabase.getInstance(
									getActivity()).search(
									selectedGroup.getId(),
									edit.getText().toString()));
						} else {
							memos.addAll(ReadableDatabase.getInstance(
									getActivity()).search(
									edit.getText().toString()));

						}
						totalCount = memos.size();
						adapter.notifyDataSetChanged();
					}
				});
		return view;
	}

	private void loading() {
		++page;
		memos.addAll(ReadableDatabase.getInstance(getActivity())
				.selectMemoLimit(20, page));
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		groups = ReadableDatabase.getInstance(getActivity()).selectAllGroup();
		spinner.setAdapter(new ReusableAdapter<Group>(groups,
				R.layout.item_group_spinner) {

			@Override
			public void bindView(
					com.lzf.memo.util.ReusableAdapter.ViewHolder holder,
					Group obj) {
				// TODO Auto-generated method stub
				holder.setText(R.id.groupName, obj.getName());
			}
		});
		memos.clear();
		if (selectedGroup != null) {
			memos.addAll(ReadableDatabase.getInstance(getActivity()).search(
					selectedGroup.getId(), edit.getText().toString()));
		} else {
			memos.addAll(ReadableDatabase.getInstance(getActivity()).search(
					edit.getText().toString()));

		}
		totalCount = memos.size();
		adapter.notifyDataSetChanged();
	}
}

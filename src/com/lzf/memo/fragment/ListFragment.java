/**
 * 
 */
package com.lzf.memo.fragment;

import java.text.SimpleDateFormat;
import java.util.List;

import android.R.bool;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

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
public class ListFragment extends Fragment {
	private ListView memoList;
	private ReusableAdapter<Memo> adapter;
	private View footerView;

	private List<Memo> memos;
	private Memo memo;
	private boolean isAddMemo = false;
	private int totalCount;
	private int page = 0;
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
			"yyyy年MM月dd日 HH:mm");

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_list, container, false);
		view.findViewById(R.id.add).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				initPopWindow(arg0);

			}
		});

		totalCount = ReadableDatabase.getInstance(getActivity()).countAllMemo();
		memos = ReadableDatabase.getInstance(getActivity()).selectMemoLimit(20,
				page);
		Log.v("memos", memos.toString());
		memoList = (ListView) view.findViewById(R.id.memoList);
		footerView = LayoutInflater.from(getActivity()).inflate(
				R.layout.loading, null, false);
		memoList.addFooterView(footerView, null, false); // isSelectable设置为不可选择页脚视图
		memoList.setOnScrollListener(new OnScrollListener() {
			/** 记录第一行Item的数值 */
			private int firstVisibleItem;

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// 当滑动到底部时
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
						&& firstVisibleItem != 0) {
					if (totalCount > memos.size()) {
						loading();
					}
				}
			}

			/*
			 * firstVisibleItem表示在现时屏幕第一个ListItem(部分显示的ListItem也算)在整个ListView的位置
			 * （下标从0开始）
			 * 
			 * visibleItemCount表示在现时屏幕可以见到的ListItem(部分显示的ListItem也算)总数
			 * 
			 * totalItemCount表示ListView的ListItem总数
			 */
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				this.firstVisibleItem = firstVisibleItem;

				// 判断可视Item是否能在当前页面完全显示
				if (visibleItemCount == totalItemCount
						|| totalCount <= memos.size()) {
					// removeFooterView(footerView);
					footerView.setVisibility(View.GONE);// 隐藏底部布局
				} else {
					// addFooterView(footerView);
					footerView.setVisibility(View.VISIBLE);// 显示底部布局
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

				final String[] operate = new String[] { "查看", "编辑", "删除" };
				AlertDialog alert = null;
				Builder builder = new Builder(getActivity());
				alert = builder
						.setTitle("选择一项进行操作")
						.setItems(operate,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										Log.v("选择了", operate[which]);
										Intent intent = new Intent(
												getActivity(),
												AddModifyActivity.class);
										switch (which) {
										case 0: // 查看
											intent.putExtra("oprate", "look");
											intent.putExtra("memo", memo);
											startActivity(intent);
											break;
										case 1: // 编辑
											intent.putExtra("oprate", "modify");
											intent.putExtra("memo", memo);
											startActivity(intent);
											break;
										case 2: // 删除
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
		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (memo != null) {
			for (int i = 0; i < memos.size(); i++) {
				Memo temp = memos.get(i);
				if (temp.getId() == memo.getId()) {
					memos.remove(i);
					memos.add(i, ReadableDatabase.getInstance(getActivity())
							.selectMemo(memo.getId()));
					temp = memo;
					break;
				}
			}
			adapter.notifyDataSetChanged();
		} else if (isAddMemo) {
			Log.v("isAddMemo", isAddMemo + "");
			try {
				memos.add(
						0,
						ReadableDatabase.getInstance(getActivity()).selectMemo(
								ReadableDatabase.getInstance(getActivity())
										.selectMaxIdMemo()));
				adapter.notifyDataSetChanged();
				isAddMemo = false;
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			page = 0;
			memos.clear();
			memos.addAll(ReadableDatabase.getInstance(getActivity())
					.selectMemoLimit(20, page));
			adapter.notifyDataSetChanged();
		}
	}

	private void loading() {
		++page;
		memos.addAll(ReadableDatabase.getInstance(getActivity())
				.selectMemoLimit(20, page));
		adapter.notifyDataSetChanged();
	}

	private void initPopWindow(View v) {
		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.add_memo_group, null, false);
		TextView group = (TextView) view.findViewById(R.id.group);
		TextView memo = (TextView) view.findViewById(R.id.memo);
		// 1.构造一个PopupWindow，参数依次是加载的View，宽高
		final PopupWindow popWindow = new PopupWindow(view,
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT, true);

		// 这些为了点击非PopupWindow区域，PopupWindow会消失的，如果没有下面的
		// 代码的话，你会发现，当你把PopupWindow显示出来了，无论你按多少次后退键
		// PopupWindow并不会关闭，而且退不出程序，加上下述代码可以解决这个问题
		popWindow.setTouchable(true);
		popWindow.setTouchInterceptor(new View.OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return false;
				// 这里如果返回true的话，touch事件将被拦截
				// 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
			}
		});
		popWindow.setBackgroundDrawable(new ColorDrawable(0x00000000)); // 要为popWindow设置一个背景才有效

		// 设置popupWindow显示的位置，参数依次是参照View，x轴的偏移量，y轴的偏移量
		popWindow.showAsDropDown(v, 50, 0);

		// 设置popupWindow里的按钮的事件
		group.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addGroupDialog();
				popWindow.dismiss();
			}
		});
		memo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				isAddMemo = true;
				Intent intent = new Intent(getActivity(),
						AddModifyActivity.class);
				intent.putExtra("oprate", "add");
				startActivity(intent);
				popWindow.dismiss();
			}
		});
	}

	private void addGroupDialog() {
		// 初始化Builder
		Builder builder = new Builder(getActivity());
		// 加载自定义的那个View,同时设置下
		final LayoutInflater inflater = getActivity().getLayoutInflater();
		View view_custom = inflater.inflate(R.layout.dialog_edit_submit, null,
				false);
		builder.setView(view_custom);
		builder.setCancelable(false);
		final AlertDialog alert = builder.create();
		final EditText groupName = (EditText) view_custom
				.findViewById(R.id.groupName);
		view_custom.findViewById(R.id.cancel).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						alert.dismiss();
					}
				});

		view_custom.findViewById(R.id.add).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						WritableDatabase.getInstance(getActivity()).addGroup(
								new Group(ReadableDatabase.getInstance(
										getActivity()).selectMaxIdGroup() + 1,
										groupName.getText().toString()));
						alert.dismiss();
						Toast.makeText(getActivity(), "添加分组成功",
								Toast.LENGTH_SHORT).show();
					}
				});
		alert.setView(view_custom, 0, 0, 0, 0);
		alert.show();
	}
}
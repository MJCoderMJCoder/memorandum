/**
 * 
 */
package com.lzf.memo.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.lzf.memo.R;
import com.lzf.memo.bean.Group;
import com.lzf.memo.dao.ReadableDatabase;
import com.lzf.memo.dao.WritableDatabase;
import com.lzf.memo.util.ReusableAdapter;

/**
 * @author MJCoder
 * 
 */
@SuppressLint("NewApi")
public class GroupFragment extends Fragment {
	private ReusableAdapter<Group> adapter;
	private ListView groupList;
	private TextView noData;

	private List<Group> groupShow = new ArrayList<Group>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater
				.inflate(R.layout.fragment_notify, container, false);
		noData = (TextView) view.findViewById(R.id.noData);
		noData.setVisibility(View.GONE);
		groupList = (ListView) view.findViewById(R.id.notifyList);
		groupShow.clear();
		groupShow.addAll(ReadableDatabase.getInstance(getActivity())
				.selectAllGroup());
		adapter = new ReusableAdapter<Group>(groupShow,
				R.layout.item_group_list) {

			@Override
			public void bindView(
					com.lzf.memo.util.ReusableAdapter.ViewHolder holder,
					Group obj) {
				// TODO Auto-generated method stub
				holder.setText(R.id.groupNameTxt, obj.getName());
				holder.setText(R.id.groupNameEdit, obj.getName());
			}

		};
		groupList.setAdapter(adapter);

		groupList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				final Group data = groupShow.get(arg2);
				final TextView groupEdit = (TextView) arg1
						.findViewById(R.id.groupEdit);
				final EditText groupNameEdit = (EditText) arg1
						.findViewById(R.id.groupNameEdit);
				final TextView groupNameTxt = (TextView) arg1
						.findViewById(R.id.groupNameTxt);
				groupEdit.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						if (groupEdit.getText().equals("±‡º≠")) {
							groupEdit.setText("ÕÍ≥…");
							// …Ë÷√ø…±‡º≠◊¥Ã¨
							groupNameEdit.setVisibility(View.VISIBLE);
							groupNameTxt.setVisibility(View.GONE);
							groupNameEdit.requestFocus();
						} else {
							data.setName(groupNameEdit.getText().toString());
							groupNameTxt.setText(data.getName());
							WritableDatabase.getInstance(getActivity())
									.updateGroup(data);
							groupEdit.setText("±‡º≠");
							// …Ë÷√≤ªø…±‡º≠◊¥Ã¨
							groupNameEdit.setVisibility(View.GONE);
							groupNameTxt.setVisibility(View.VISIBLE);
							adapter.notifyDataSetChanged();
						}

					}
				});
				arg1.findViewById(R.id.groupDelete).setOnClickListener(
						new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								WritableDatabase.getInstance(getActivity())
										.deleteGroup(data);
								adapter.delete(data);
							}
						});

			}
		});
		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		noData.setVisibility(View.GONE);
		groupShow.clear();
		groupShow.addAll(ReadableDatabase.getInstance(getActivity())
				.selectAllGroup());
		adapter.notifyDataSetChanged();
	}
}

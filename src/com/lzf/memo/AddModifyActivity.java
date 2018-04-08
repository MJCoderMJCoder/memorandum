/**
 * 
 */
package com.lzf.memo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.lzf.memo.bean.Group;
import com.lzf.memo.bean.Memo;
import com.lzf.memo.dao.ReadableDatabase;
import com.lzf.memo.dao.WritableDatabase;
import com.lzf.memo.util.ReusableAdapter;

/**
 * @author MJCoder
 * 
 */
public class AddModifyActivity extends Activity {
	private boolean isManuaSelect = false;// 判断是否为刚进去时触发onItemSelected的标志
	private Group selectedGroup = null;
	private List<Group> groups;
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
			"yyyy年MM月dd日 HH:mm");
	private Memo memo;

	private TextView valueGroup;
	private Spinner spinner;
	private TextView notifyDate;
	private TextView notifytime;
	private EditText memoName;
	private EditText memoContent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_modify);
		valueGroup = (TextView) findViewById(R.id.valueGroup);
		spinner = (Spinner) findViewById(R.id.spinner);
		notifyDate = (TextView) findViewById(R.id.notifyDate);
		notifytime = (TextView) findViewById(R.id.notifytime);
		memoName = (EditText) findViewById(R.id.memoName);
		memoContent = (EditText) findViewById(R.id.memoContent);

		groups = ReadableDatabase.getInstance(AddModifyActivity.this)
				.selectAllGroup();
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
				Log.v("onItemSelected", arg0 + "；" + arg1 + "；" + arg2 + "；"
						+ arg3);
				if (isManuaSelect) {
					selectedGroup = groups.get(arg2);
					valueGroup.setText(selectedGroup.getName());
				} else {
					if (groups.size() > 0) {
						selectedGroup = groups.get(0);
						valueGroup.setText(groups.get(0).getName());
					} else {
						valueGroup.setText("默认分组");
					}
				}
				isManuaSelect = true;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				Log.v("onNothingSelected", arg0 + "");
				selectedGroup = null;
			}
		});

		notifyDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				Calendar cale1 = Calendar.getInstance();
				new DatePickerDialog(AddModifyActivity.this,
						new DatePickerDialog.OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker view, int year,
									int monthOfYear, int dayOfMonth) {
								// 这里获取到的月份需要加上1哦~
								notifyDate.setText(year + "年"
										+ (monthOfYear + 1) + "月" + dayOfMonth
										+ "日");
							}
						}, cale1.get(Calendar.YEAR), cale1.get(Calendar.MONTH),
						cale1.get(Calendar.DAY_OF_MONTH)).show();

			}
		});
		notifytime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Calendar cale2 = Calendar.getInstance();
				new TimePickerDialog(AddModifyActivity.this,
						new TimePickerDialog.OnTimeSetListener() {
							@Override
							public void onTimeSet(TimePicker view,
									int hourOfDay, int minute) {
								notifytime.setText(hourOfDay + ":" + minute);
							}
						}, cale2.get(Calendar.HOUR_OF_DAY), cale2
								.get(Calendar.MINUTE), true).show();
			}
		});
		Intent intent = getIntent();
		if (intent != null) {
			String oprate = intent.getStringExtra("oprate");
			if ("add".equals(oprate)) {
				findViewById(R.id.complete).setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								Calendar calendar = Calendar.getInstance();
								try {
									calendar.setTime(simpleDateFormat
											.parse(notifyDate.getText()
													.toString().trim()
													+ " "
													+ notifytime.getText()
															.toString().trim()));
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								String title = memoName.getText().toString()
										.trim();
								String content = memoContent.getText()
										.toString().trim();
								if (title.length() < 1) {
									Toast.makeText(AddModifyActivity.this,
											"请输入标题", Toast.LENGTH_SHORT).show();
								} else if (content.length() < 1) {
									Toast.makeText(AddModifyActivity.this,
											"请输入内容", Toast.LENGTH_SHORT).show();
								} else {
									int id = ReadableDatabase.getInstance(
											AddModifyActivity.this)
											.selectMaxIdMemo() + 1;
									WritableDatabase
											.getInstance(AddModifyActivity.this)
											.addMemo(
													new Memo(
															id,
															title,
															content,
															calendar.getTimeInMillis(),
															System.currentTimeMillis(),
															(selectedGroup == null ? -6003
																	: selectedGroup
																			.getId())));
									Toast.makeText(AddModifyActivity.this,
											"备忘录添加成功", Toast.LENGTH_SHORT)
											.show();
									AddModifyActivity.this.finish();
								}
							}
						});
			} else {
				memo = (Memo) intent.getSerializableExtra("memo");
				if (memo.getGroup() == -6003) {
					valueGroup.setText("默认分组");
				} else {
					valueGroup.setText((ReadableDatabase.getInstance(this)
							.selectGroup(memo.getGroup())).getName());
				}
				memoName.setText(memo.getTitle());
				memoContent.setText(memo.getContent());
				String[] notify = simpleDateFormat.format(memo.getNotify())
						.split(" ");
				notifyDate.setText(notify[0]);
				notifytime.setText(notify[1]);
				if ("look".equals(oprate)) {
					findViewById(R.id.onlyRead).setVisibility(View.VISIBLE);
					findViewById(R.id.onlyRead).setOnClickListener(
							new OnClickListener() {

								@Override
								public void onClick(View arg0) {
									// TODO Auto-generated method stub
									Log.v("onlyRead", "" + arg0);
								}
							});
				} else if ("modify".equals(oprate)) {
					findViewById(R.id.complete).setOnClickListener(
							new OnClickListener() {

								@Override
								public void onClick(View arg0) {
									// TODO Auto-generated method stub
									Calendar calendar = Calendar.getInstance();
									try {
										calendar.setTime(simpleDateFormat
												.parse(notifyDate.getText()
														.toString().trim()
														+ " "
														+ notifytime.getText()
																.toString()
																.trim()));
									} catch (ParseException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									String title = memoName.getText()
											.toString().trim();
									String content = memoContent.getText()
											.toString().trim();
									if (title.length() < 1) {
										Toast.makeText(AddModifyActivity.this,
												"请输入标题", Toast.LENGTH_SHORT)
												.show();
									} else if (content.length() < 1) {
										Toast.makeText(AddModifyActivity.this,
												"请输入内容", Toast.LENGTH_SHORT)
												.show();
									} else {
										memo.setTitle(title);
										memo.setContent(content);
										memo.setNotify(calendar
												.getTimeInMillis());
										memo.setLast_modify(System
												.currentTimeMillis());
										memo.setGroup((selectedGroup == null ? -6003
												: selectedGroup.getId()));
										WritableDatabase.getInstance(
												AddModifyActivity.this)
												.updateMemo(memo);
										Toast.makeText(AddModifyActivity.this,
												"备忘录修改成功", Toast.LENGTH_SHORT)
												.show();
										AddModifyActivity.this.finish();
									}
								}
							});
				}
			}
		}
	}
}

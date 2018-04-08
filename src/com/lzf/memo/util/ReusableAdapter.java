package com.lzf.memo.util;

import java.util.List;

import com.lzf.memo.bean.Memo;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * �ɸ��õ�������
 */
public abstract class ReusableAdapter<T> extends BaseAdapter {
	private List<T> listData;
	private int listItemResource; // id

	public ReusableAdapter(List<T> listData, int listItemResource) {
		this.listData = listData;
		this.listItemResource = listItemResource;
	}

	public int getCount() {
		// ���listDara��Ϊ�գ��򷵻�listData.size;���򷵻�0
		return (listData != null) ? listData.size() : 0;
	}

	public Object getItem(int position) {
		return listData.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	/**
	 * View AnimalAdapter.getView(int position, View convertView, ViewGroup
	 * parent) ��ʵ���convertView��ϵͳ�ṩ�����ǵĿɹ����õ�View �Ļ������
	 * 
	 * �ж����оͻ���ö��ٴ�getView(�ж��ٸ�Item����ôgetView�����ͻᱻ���ö��ٴ�)
	 */
	@SuppressWarnings("unchecked")
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = ViewHolder.bind(parent.getContext(), convertView,
				parent, listItemResource, position);
		bindView(holder, (T) getItem(position));
		return holder.getItemView();
	}

	/**
	 * ����һ�����󷽷������ViewHolder��������ݼ��İ�
	 * 
	 * ���Ǵ����µ�BaseAdapter��ʱ��ʵ����������ͺã����⣬�����˰������Զ��� ��BaseAdapter�ĳ�abstact����ģ�
	 */
	public abstract void bindView(ViewHolder holder, T obj);

	/**
	 * ViewHolder���ܣ�
	 * 
	 * 1. findViewById�����ÿؼ�״̬��
	 * 
	 * 2. ����һ�����ҿؼ��ķ��������ǵ�˼·��ͨ����¶�����ķ��������÷���ʱ���ݹ��� �ؼ�id���Լ����õ����ݣ�����TextView�����ı���
	 * public ViewHolder setText(int id, CharSequence text){�ı�����}
	 * 
	 * 3. ��convertView���ò��ְᵽ����Ǿ���Ҫ����һ��context�����ˣ����ǰ���Ҫ��ȡ �Ĳ��ֶ�д�����췽���У�
	 * 
	 * 4. дһ�����÷���(public)�������������ִ�С��ɫ��ͼƬ�����ȣ�
	 */
	public static class ViewHolder {
		// �洢ListView �� item�е�View
		private SparseArray<View> viewsOflistViewItem;
		private View storeConvertView; // ���convertView
		private int position; // λ�á���λ
		private Context context; // Context������

		// ���췽���������س�ʼ��
		private ViewHolder(Context context, ViewGroup parent,
				int listItemResource) {
			// �洢ListView �� item�е�View
			viewsOflistViewItem = new SparseArray<View>();
			this.context = context;
			// View android.view.LayoutInflater.inflate(int resource, ViewGroup
			// root, boolean attachToRoot)��LayoutInflater�������������
			View convertView = LayoutInflater.from(context).inflate(
					listItemResource, parent, false);
			convertView.setTag(this);
			storeConvertView = convertView; // ���convertView
		}

		// ��ViewHolder��item
		public static ViewHolder bind(Context context, View convertView,
				ViewGroup parent, int listItemResource, int position) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder(context, parent, listItemResource);
			} else {
				holder = (ViewHolder) convertView.getTag();
				holder.storeConvertView = convertView;
			}
			holder.position = position;
			return holder;
		}

		// ����id��ȡ�����б���Ŀؼ�
		@SuppressWarnings("unchecked")
		public <T extends View> T getView(int id) {
			T t = (T) viewsOflistViewItem.get(id);
			if (t == null) {
				t = (T) storeConvertView.findViewById(id);
				viewsOflistViewItem.put(id, t);
			}
			return t;
		}

		// ���������ٶ���һ�ѱ�¶�����ķ���
		// ��ȡ��ǰ��Ŀ
		public View getItemView() {
			return storeConvertView;
		}

		// ��ȡ��Ŀλ��
		public int getItemPosition() {
			return position;
		}

		// ��������
		public ViewHolder setText(int id, CharSequence text) {
			View view = getView(id);
			if (view instanceof TextView || view instanceof EditText) {
				((TextView) view).setText(text);
			}
			return this;
		}

		// ����TextView�е��ı�����
		public ViewHolder setTVGravity(int id, int gravity) {
			View view = getView(id);
			if (view instanceof TextView) {
				((TextView) view).setGravity(gravity);
			}
			return this;
		}

		// ����TextView�е��ı���ɫ
		public ViewHolder setTextColor(int id, int color) {
			View view = getView(id);
			if (view instanceof TextView) {
				((TextView) view).setTextColor(color);
			}
			return this;
		}

		// ����ͼƬ
		public ViewHolder setImageResource(int id, int drawableRes) {
			View view = getView(id);
			if (view instanceof ImageView) {
				((ImageView) view).setImageResource(drawableRes);
			} else {
				view.setBackgroundResource(drawableRes);
			}
			return this;
		}

		// ���õ������
		public ViewHolder setOnClickListener(int id,
				View.OnClickListener listener) {
			getView(id).setOnClickListener(listener);
			return this;
		}

		// ���ÿɼ�
		public ViewHolder setVisibility(int id, int visible) {
			getView(id).setVisibility(visible);
			return this;
		}

		// ���ñ�ǩ
		public ViewHolder setTag(int id, Object obj) {
			getView(id).setTag(obj);
			return this;
		}

		// ����������������չ
	}

	/**
	 * ����ListView�����ݸ���(why?????)
	 * 
	 * ɾ����������
	 * 
	 * public void clear() {
	 * 
	 * if (listData != null) {
	 * 
	 * listData.clear();
	 * 
	 * }
	 * 
	 * notifyDataSetChanged();
	 * 
	 * }
	 */

	public void add(T data) {
		listData.add(data);
		notifyDataSetChanged();
	}

	public void delete(T data) {
		listData.remove(data);
		notifyDataSetChanged();
	}
}

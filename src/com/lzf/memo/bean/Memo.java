/**
 * 
 */
package com.lzf.memo.bean;

import java.io.Serializable;

/**
 * @author MJCoder
 * 
 */
public class Memo implements Serializable {
	private int id;
	private String title;
	private String content;
	private long notify; // 提醒时间
	private long last_modify; // 最后修改时间
	private int group;

	public Memo(int id, String title, String content, long notify,
			long last_modify, int group) {
		super();
		this.id = id;
		this.title = title;
		this.content = content;
		this.notify = notify;
		this.last_modify = last_modify;
		this.group = group;
	}

	public Object[] toObjects() {
		return new Object[] { id, title, content, notify, last_modify, group };
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getNotify() {
		return notify;
	}

	public void setNotify(long notify) {
		this.notify = notify;
	}

	public long getLast_modify() {
		return last_modify;
	}

	public void setLast_modify(long last_modify) {
		this.last_modify = last_modify;
	}

	public int getGroup() {
		return group;
	}

	public void setGroup(int group) {
		this.group = group;
	}

	@Override
	public String toString() {
		return "Memo [id=" + id + ", title=" + title + ", content=" + content
				+ ", notify=" + notify + ", last_modify=" + last_modify
				+ ", group=" + group + "]";
	}

}

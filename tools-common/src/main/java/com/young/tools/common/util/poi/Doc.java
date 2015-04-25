package com.young.tools.common.util.poi;

import java.util.Date;
import java.util.List;

public class Doc {

	private String title;

	private String docName;

	private Date createTime;

	private String html;

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	private List<DocFragment> fragments;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public List<DocFragment> getFragments() {
		return fragments;
	}

	public void setFragments(List<DocFragment> fragments) {
		this.fragments = fragments;
	}

}

package com.young.tools.common.util.poi;

import java.util.ArrayList;
import java.util.List;

public class DocFragment {

	public List<DocFragment> getSubFragments() {
		return subFragments;
	}

	private String type;
	
	private String text;
	
	private List<DocFragment> subFragments = new ArrayList<DocFragment>();

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

   public void addFragment(DocFragment frag){
	   this.subFragments.add(frag);
   }
	
	
}

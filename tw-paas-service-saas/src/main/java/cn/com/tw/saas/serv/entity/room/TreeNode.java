package cn.com.tw.saas.serv.entity.room;

import java.util.List;

public class TreeNode {
	
	private String value;  
	  
	private String text;  
	  
    private List<TreeNode> children;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<TreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}

	      
}

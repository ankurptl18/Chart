package com.ankur.pdf2;

import java.util.List;


public class ConditionList {

	private String listLabel;
	
	private List<Condition> conditions;

	public String getListLabel() {
		return listLabel;
	}

	public void setListLabel(String listLabel) {
		this.listLabel = listLabel;
	}

	public ConditionList(String listLabel, List<Condition> conditions) {
		super();
		this.listLabel = listLabel;
		this.conditions = conditions;
	}

	public List<Condition> getConditions() {
		return conditions;
	}

	public void setConditions(List<Condition> conditions) {
		this.conditions = conditions;
	} 
	
}

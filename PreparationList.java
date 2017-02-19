package com.ankur.pdf;

import java.util.List;

public class PreparationList {

	private String listName;
	
	int order ;
	
	List<PreparationResult> preparationResultList;
	
	public PreparationList(String listName, int order, List<PreparationResult> list) {
		super();
		this.listName = listName;
		this.order = order;
		this.preparationResultList = list;
	}

	public String getListName() {
		return listName;
	}

	public void setListName(String listName) {
		this.listName = listName;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public List<PreparationResult> getPreparationResultList() {
		return preparationResultList;
	}

	public void setPreparationResultList(List<PreparationResult> preparationResultList) {
		this.preparationResultList = preparationResultList;
	}
	
}

package com.ankur.pdf;

public class PreparationResult {

	String label;
	String value;
	String unit;
	int order;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public PreparationResult(String label, String value, String unit, int order) {
		super();
		this.label = label;
		this.value = value;
		this.unit = unit;
		this.order = order;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
}

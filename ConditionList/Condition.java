package com.ankur.pdf2;

public class Condition {
	
	private String label;
	
	private String value;

	public String getLabel() {
		return label;
	}

	public Condition(String label, String value) {
		super();
		this.label = label;
		this.value = value;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}

package com.credit_suisse.app.util;

public enum emWorkerProfile {
	
	PROFILE_INSTRUMENT ("INSTRUMENT", 1);
	
	private final String EnumName;
	private final int EnumValue ; 

	emWorkerProfile(String n, int v)
	{
		this.EnumName = n;
		this.EnumValue = v;
	}
	
	public String toString() { return EnumName; }

	public int toValue() { return EnumValue; }
	
}

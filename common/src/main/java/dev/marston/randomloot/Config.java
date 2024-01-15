package dev.marston.randomloot;

import java.util.Map;

public class Config {


	public static double CaseChance;
	public static double ModChance;
	public static double Goodness;

	public static Map<String, Boolean> ModsEnabled;


	public static boolean traitEnabled(String tagName) {
		return ModsEnabled.get(tagName);
	}

}

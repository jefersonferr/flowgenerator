package org.flowgenerator.gui;
import java.util.Locale;
import java.util.ResourceBundle;

public final class MyLanguageProperties {
	private static ResourceBundle rb;
	public String getString(String key) {
		return rb.getString(key);
	}
	public MyLanguageProperties() {
		try {
			//Locale.setDefault(new Locale(FlowGenerator.getLanguage()));
			rb = ResourceBundle.getBundle("FlowGeneratorResource_"+Locale.getDefault());
		} catch (Exception e){
			rb = ResourceBundle.getBundle("FlowGeneratorResource_en_US");
		}
	}
}
package com.selenium.webdriver.idobj.controller;


import com.selenium.webdriver.idobj.model.IDriverSetup;
import com.selenium.webdriver.idobj.model.IdentifiableObject;
import com.selenium.webdriver.idobj.view.IidentifyElementView;

public interface IController {
		
	public enum EControlledItem {
		_Url,
		_CoordinatesSetup,
		_IdentifyElement,
		_ElementDetails,
		_Xpath
	}
	
	public void setModel(IdentifiableObject identifiableObject);
	public void setView(IidentifyElementView elementView);
	public void setDriverSetup(IDriverSetup driverSetup);
	public String[] getSupportedBrowsers();
	public void modelDidChange(IidentifyElementView View);
	public String controll(EControlledItem item ,Object ControlledItem);

}

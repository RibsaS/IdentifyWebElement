package com.selenium.webdriver.idobj.factory;

import com.selenium.webdriver.idobj.controller.IController;
import com.selenium.webdriver.idobj.model.DriverSetup;
import com.selenium.webdriver.idobj.model.IDriverSetup;
import com.selenium.webdriver.idobj.model.IdentifiableElement;
import com.selenium.webdriver.idobj.model.IdentifiableObject;
import com.selenium.webdriver.idobj.view.IidentifyElementView;

public class ModelFactory extends AbstractFactory {
	

	@Override
	public IdentifiableObject getIdentifiableModel() {
			return new IdentifiableElement();
	
	}
	
	@Override
	public IDriverSetup getDriverSetup() {
		return new DriverSetup();
	}
		
	
	
	
	@Override
	public IidentifyElementView getView() {
		return null;
	}
	@Override
	public IController getController() {
		return null;
	}









}

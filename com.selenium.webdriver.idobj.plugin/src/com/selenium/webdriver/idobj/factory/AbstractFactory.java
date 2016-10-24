package com.selenium.webdriver.idobj.factory;

import com.selenium.webdriver.idobj.controller.IController;
import com.selenium.webdriver.idobj.model.IDriverSetup;
import com.selenium.webdriver.idobj.model.IdentifiableObject;
import com.selenium.webdriver.idobj.view.IidentifyElementView;

public abstract class AbstractFactory {
	

	
	public abstract IdentifiableObject  getIdentifiableModel();
	public abstract IDriverSetup getDriverSetup();
	public abstract IidentifyElementView getView();
	public abstract IController getController();

}

package com.selenium.webdriver.idobj.factory;

import com.selenium.webdriver.idobj.controller.IController;
import com.selenium.webdriver.idobj.model.IDriverSetup;
import com.selenium.webdriver.idobj.model.IdentifiableObject;
import com.selenium.webdriver.idobj.view.IdentifyElementConcretView;
import com.selenium.webdriver.idobj.view.IidentifyElementView;

public class ViewFactory extends AbstractFactory {

	

	@Override
	public IidentifyElementView getView() {
		return new IdentifyElementConcretView();
	}
	
	
	
	@Override
	public IdentifiableObject getIdentifiableModel() {
		return null;
	}
	@Override
	public IController getController() {
		return null;
	}



	@Override
	public IDriverSetup getDriverSetup() {
		// TODO Auto-generated method stub
		return null;
	}

}

package com.selenium.webdriver.idobj.factory;

import com.selenium.webdriver.idobj.controller.IController;
import com.selenium.webdriver.idobj.controller.IdentifyObjectController;
import com.selenium.webdriver.idobj.model.IDriverSetup;
import com.selenium.webdriver.idobj.model.IdentifiableObject;
import com.selenium.webdriver.idobj.view.IidentifyElementView;

public class ControllerFactory extends AbstractFactory {

	@Override
	public IdentifiableObject getIdentifiableModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IidentifyElementView getView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IController getController() {
		return new IdentifyObjectController();
	}

	@Override
	public IDriverSetup getDriverSetup() {
		// TODO Auto-generated method stub
		return null;
	}

}

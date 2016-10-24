package com.selenium.webdriver.idobj;







import com.selenium.webdriver.idobj.controller.IController;
import com.selenium.webdriver.idobj.factory.FactoryProducer;
import com.selenium.webdriver.idobj.model.IDriverSetup;
import com.selenium.webdriver.idobj.model.IdentifiableObject;
import com.selenium.webdriver.idobj.view.IidentifyElementView;

public class IdentifyObject {
	
	public static void start(){
		IdentifiableObject identifiableObject;
		IDriverSetup driverSetup;
		IController controller;
		IidentifyElementView view;
		
		//create the driverSetup
		driverSetup = FactoryProducer.getFactory(FactoryProducer.EFactory.Model).getDriverSetup();
		
		//create the model 
		identifiableObject = FactoryProducer.getFactory(FactoryProducer.EFactory.Model).getIdentifiableModel();
		
		//create the controller
		controller = FactoryProducer.getFactory(FactoryProducer.EFactory.Controller).getController();
		controller.setDriverSetup(driverSetup);
		
		
		//create the view
		view = FactoryProducer.getFactory(FactoryProducer.EFactory.View).getView();
		
		//
		view.setViewController(controller);
		controller.setView(view);
		controller.setModel(identifiableObject);

		view.display();
	}

	public static void main(String[] args) {
		
		start();

	}

}

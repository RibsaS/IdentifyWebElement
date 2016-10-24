package com.selenium.webdriver.idobj.factory;

public class FactoryProducer {
	
	public static enum EFactory{
		Model,
		View,
		Controller
	}
	
	
	public static AbstractFactory getFactory(EFactory factory){
		
		if (factory.equals(EFactory.Model)){
			return new ModelFactory();
		}else if (factory.equals(EFactory.View)){
			return new ViewFactory();
		}else if (factory.equals(EFactory.Controller)){
			return new ControllerFactory();
		}else {
			return null;
		}
			
		
	}

}

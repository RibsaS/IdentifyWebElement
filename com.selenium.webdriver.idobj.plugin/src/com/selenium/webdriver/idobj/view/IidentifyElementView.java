package com.selenium.webdriver.idobj.view;

import java.awt.Point;
import java.util.Map;

import com.selenium.webdriver.idobj.controller.IController;

public interface IidentifyElementView {
	
	public void display();
	public void setViewController(IController listener);
	public void willConnectToUrl();
	public void UrlConnectionFailed();
	public void UrlConnectionPassed();
	
	public void updateCoordinates(Point absoluteCoordinates, Point relativeCoordinates);
	public void updateWebElementDetails(Map<String, String> ElementDetails);
	public void updateTagName(String TagName);
	
	
	

}

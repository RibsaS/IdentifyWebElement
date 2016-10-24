package com.selenium.webdriver.idobj.model;



import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;


public interface IDriverSetup {
	
	
	
	public void Setup(String sUrl, Capabilities capabilities) throws Exception;
	public String[] getSupportedBrowsers();
	public WebDriver getDriver();

}

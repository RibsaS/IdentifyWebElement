package com.selenium.webdriver.idobj.model;

import java.net.URL;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

public class DriverSetup implements IDriverSetup {
	
	private  WebDriver _driver = null;
	private final String[] _supportedBrowser = 
		{	DesiredCapabilities.internetExplorer().getBrowserName(),
			DesiredCapabilities.chrome().getBrowserName(),
			DesiredCapabilities.firefox().getBrowserName(),
			DesiredCapabilities.safari().getBrowserName(),
			}; 
	
	public void Setup(String sUrl, Capabilities capabilities) throws Exception {
		
		InitDriver(sUrl,capabilities);
	}
	
	

	public String[] getSupportedBrowsers() {
		return _supportedBrowser;
	}



	private  void InitDriver(String sUrl, Capabilities capabilities) throws Exception{
		
		if ( _driver == null) { 
			try
			{
				URL url = new URL(sUrl);
				_driver = new RemoteWebDriver(url, capabilities);
			}
			catch (Exception ex)
			{
				
				if (capabilities.getBrowserName().equalsIgnoreCase(DesiredCapabilities.chrome().getBrowserName())){
					//chrome
					_driver = new ChromeDriver();
				
					
				}else if (capabilities.getBrowserName().equalsIgnoreCase(DesiredCapabilities.firefox().getBrowserName()))
				{
					//firefox
					_driver = new FirefoxDriver();
					
				}else if (capabilities.getBrowserName().equalsIgnoreCase(DesiredCapabilities.internetExplorer().getBrowserName()))
				{
					//Internet Explorer
					_driver = new InternetExplorerDriver();
					
				}else if (capabilities.getBrowserName().equalsIgnoreCase(DesiredCapabilities.safari().getBrowserName()))
				{
					//Safari
					_driver = new SafariDriver();
				}
				
				_driver.get(sUrl);
				_driver.manage().window().maximize();
			}
		}
	
	}



	public WebDriver getDriver() {
		return _driver;
		
	} 

}

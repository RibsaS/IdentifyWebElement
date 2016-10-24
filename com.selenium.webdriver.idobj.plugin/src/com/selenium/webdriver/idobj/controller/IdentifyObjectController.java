package com.selenium.webdriver.idobj.controller;

import java.awt.Point;
import java.util.Map;
import org.apache.commons.validator.routines.UrlValidator;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.DesiredCapabilities;
import com.selenium.webdriver.idobj.model.IDriverSetup;
import com.selenium.webdriver.idobj.model.IdentifiableObject;
import com.selenium.webdriver.idobj.view.IidentifyElementView;

/**
 * @author anas sabir
 *
 */
public class IdentifyObjectController implements IController {

	IdentifiableObject identifiableObject;
	IDriverSetup driverSetup;
	IidentifyElementView view;
	
	
	




	
	public void setDriverSetup(IDriverSetup driverSetup) {
		this.driverSetup = driverSetup;
	}

	public void setModel(IdentifiableObject identifiableObject) {
		this.identifiableObject =  identifiableObject;
	}

	public void setView(IidentifyElementView elementView) {
		this.view = elementView;
	}

	public String[] getSupportedBrowsers() {	
		return driverSetup.getSupportedBrowsers();
	}

	public String controll(EControlledItem item,Object ControlledItem) {
		String returnedMessage = "";
		if (item.equals(EControlledItem._Url)) {
			String[] neededSetups = (String[])ControlledItem;
			String Url = neededSetups[0];
			String Browser = neededSetups[1];
			returnedMessage = ControllUrl(Url,Browser);
			if (identifiableObject.get_driver() == null){
				identifiableObject.set_driver(driverSetup.getDriver());
			}
		}
		else if (item.equals(EControlledItem._CoordinatesSetup)) {
			Point TopLeftCornerAbsolutePosition = (Point) ControlledItem;
			identifiableObject.set_absolutePointSetup(TopLeftCornerAbsolutePosition);
		} else if (item.equals(EControlledItem._IdentifyElement)){
			Point ClickPosition = (Point) ControlledItem;
			if (identifiableObject.get_driver() == null){
				identifiableObject.set_driver(driverSetup.getDriver());
			}
			identifiableObject.IdentifyElement(ClickPosition);
		} else if (item.equals(EControlledItem._ElementDetails)){
			String tagName = identifiableObject.getTagName();
			if (tagName != null && !tagName.isEmpty()){
				view.updateTagName(tagName);
			}
			Map<String , String> ElementDetails =  identifiableObject.getElementDetails();
			if (ElementDetails != null) {
				view.updateWebElementDetails(ElementDetails);
			}	
		}else if (item.equals(EControlledItem._Xpath)){
			String Xpath = (String)ControlledItem;
			returnedMessage = ControlXpath(Xpath);
		}

		return returnedMessage;
	}
	
	public void modelDidChange(IidentifyElementView View) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 
	 * @param url : the url to validate, only http(s) is accepted 
	 * @return true if OK
	 */
	private boolean ValidateUrl(String url){
		String[] schemes = {"http","https"}; // DEFAULT schemes = "http", "https", "ftp"
	
		UrlValidator urlValidator = new UrlValidator(schemes);
		System.out.println("is valid" + urlValidator.isValid(url));
		return urlValidator.isValid(url);
		
	}




	private String ControllUrl(String Url, String Browser){
		String returnedMessage = "";
		if (! ValidateUrl(Url)){
			returnedMessage =  "The given Url is not Valid";
		}else{
			Capabilities capabilities = getBrowserCapabilitiesFromBrowserName(Browser);
			try {
				view.willConnectToUrl();
				driverSetup.Setup(Url, capabilities);
				view.UrlConnectionPassed();
			} catch (Exception e) {
				returnedMessage = e.getMessage();
				view.UrlConnectionFailed();
			}
		}
		
		return returnedMessage;
	}
	
	private Capabilities getBrowserCapabilitiesFromBrowserName(String BrowserName)
	{
		if (BrowserName.equalsIgnoreCase(DesiredCapabilities.internetExplorer().getBrowserName())){
			return DesiredCapabilities.internetExplorer();
		}else if (BrowserName.equalsIgnoreCase(DesiredCapabilities.firefox().getBrowserName())){
			return DesiredCapabilities.firefox();
		}else if (BrowserName.equalsIgnoreCase(DesiredCapabilities.chrome().getBrowserName())){
			return DesiredCapabilities.chrome();
		}else if (BrowserName.equalsIgnoreCase(DesiredCapabilities.safari().getBrowserName())){
			return DesiredCapabilities.safari();
		}else{
			return null;
		}
			
	}
	

	
	private String ControlXpath(String Xpath){
		String returnedMessage = "";
		try {
			identifiableObject.testXPath(Xpath);
			
		} catch (Exception e) {
			returnedMessage = e.getMessage();
		}
		return returnedMessage;
	}

}

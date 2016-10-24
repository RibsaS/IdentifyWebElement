package com.selenium.webdriver.idobj.model;



import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.InvalidSelectorException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;

public class IdentifiableElement extends IdentifiableObject {

	public IdentifiableElement() {

	}
	
	
	@Override
	public Map<String, String> getElementDetails() {
		
		if (CurWebElement != null)
		{
			Object _JsArguments[] = {CurWebElement};
			JavascriptExecutor js = (JavascriptExecutor) _driver;

			Object AttributesObject = js.executeScript("var items = {}; for (index = 0; index < arguments[0].attributes.length; ++index) { items[arguments[0].attributes[index].name] = arguments[0].attributes[index].value }; return items;",_JsArguments );
			if (AttributesObject != null) {
				String  Attributes = AttributesObject.toString();
				Attributes = Attributes.replaceAll("\\{", "");
				Attributes = Attributes.replaceAll("\\}", "");
				String [] AttributesArray = Attributes.split(", ");
				if (AttributesArray != null){
					Map<String, String> attributesAndDetails = new HashMap<String, String>() ;
					String attribute;
					String Value;
					
					for (int i = 0; i < AttributesArray.length; i++) {
						System.out.println(AttributesArray[i]);
						attribute = AttributesArray[i].split("=")[0];
						if (AttributesArray[i].split("=").length == 1) {
							Value = "";
						} else {
							Value = AttributesArray[i].split("=")[1] ;
						}		
						attributesAndDetails.put(attribute,Value );
					}
					if (PreviousWebElementStyle != null){
						// replace the style changed by the enableHighlight function
						attributesAndDetails.replace("style", PreviousWebElementStyle);
					}else{
						//remove the style changed by the enableHighlight function
						attributesAndDetails.remove("style");
					}
					
					return attributesAndDetails;
				}else
					System.out.println(AttributesObject);
					return null;
			}
			
		}else
			System.out.println("CurWebElement != null");
		return null;
	}


	public void IdentifyElement(Point ElementCurAbsolutePoint){
	
		
		CurWebElement = getWebElementByCoordinates(ElementCurAbsolutePoint);
		if (CurWebElement instanceof WebElement ) {
			if (! isTheSameWebElement(CurWebElement, PreviousWebElement)  ) {
				if (PreviousWebElement != null){
					disableHighlight(PreviousWebElement,PreviousWebElementStyle);
				}
				
				//get the old style before changing It 
				PreviousWebElement = CurWebElement ;
				PreviousWebElementStyle = PreviousWebElement.getAttribute("style");
				EnableHighlight(CurWebElement,PreviousWebElementStyle);
				
			}
		}
	}


	@Override
	public String getTagName() {
		if (CurWebElement != null)
		{
			return GetElementXPath(CurWebElement);
		}
		return null;
	}


	


	@Override
	public boolean testXPath(String xpathExpression) throws  InvalidSelectorException {
		WebElement webElement = _driver.findElement(By.xpath(xpathExpression));
		if (webElement != null && webElement instanceof WebElement) {
			String oldStyle = webElement.getAttribute("style");
			for (int i = 0; i < 3; i++) {
				try {
					EnableHighlight(webElement, oldStyle,"2px solid black");
					Thread.sleep(500);
					disableHighlight(webElement, oldStyle);
					Thread.sleep(500);
				} catch (InterruptedException e) {
					System.out.println(e.getMessage());
				}
				
			}
			
			return true;
		}
		System.out.println("model: webelement null ");
		return false;
	}


	private boolean isTheSameWebElement(WebElement element1, WebElement element2){
		try{
			if (element1 != null && element2 != null){
				return element1.getLocation().equals(element2.getLocation());
			}else{
				return false;
				}
			}
		catch (StaleElementReferenceException e) {
			return false;
		}
	}	

	private  void EnableHighlight(WebElement element, String Style, String newBorder) {
		try {
			
			//Style = UpdateStyleByTag(Style,"color","red");
			Style = UpdateStyleByTag(Style, "border", newBorder);
			Object _JsArguments[] = {element,Style};
			JavascriptExecutor js = (JavascriptExecutor) _driver;

			js.executeScript("arguments[0].setAttribute('style', arguments[1]);",_JsArguments);
		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private  void EnableHighlight(WebElement element, String Style) {
		EnableHighlight(element, Style, "2px solid red");
	}

	private void disableHighlight(WebElement element,String OldStyle) {
		try {

			Object _JsArguments[] = {element,""};
			if (OldStyle != null && !OldStyle.isEmpty()) {
				_JsArguments[1]=OldStyle;
			}
			JavascriptExecutor js = (JavascriptExecutor) _driver;
			js.executeScript("arguments[0].setAttribute('style', arguments[1]);",_JsArguments);
		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String UpdateStyleByTag(String Style, String Tag, String NewValue){
		int indexTag;
		int indexEndTag;
		indexTag = Style.indexOf(Tag);
		if (indexTag >= 0) {
			indexEndTag = Style.indexOf(";", indexTag);
			Style = Style.replaceAll(Style.substring(indexTag, indexEndTag), Tag + ": " + NewValue );
		} else {
			if (Style.endsWith(";")) {
				Style = Style + Tag + ": " + NewValue + ";";
			} else {
				Style = ";" + Style + Tag + ": " + NewValue + ";";
			}
		}
		
		return Style;
	}


	private WebElement getWebElementByCoordinates(Point ElementAbsolutePoint){
		Object _JsArguments[] = {""};
		JavascriptExecutor js = (JavascriptExecutor) _driver;
		if (js != null) {
			Object Obj = js.executeScript("return document.elementFromPoint("+ Math.abs(ElementAbsolutePoint.x - _absolutePointSetup.getX()) +", " +  Math.abs(ElementAbsolutePoint.y - _absolutePointSetup.getY()) +");",_JsArguments);
			if (Obj instanceof WebElement ) {
				System.out.println( "Model Element Found");
				return (WebElement)Obj;
			}else{
				System.out.println( "Model Element NOT Found !!!!!!");
				System.out.println("ElementAbsolutePoint.x - _absolutePointSetup.getX() :" + (ElementAbsolutePoint.x - _absolutePointSetup.getX()));
				System.out.println("ElementAbsolutePoint.y - _absolutePointSetup.getY() :" + (ElementAbsolutePoint.y - _absolutePointSetup.getY()));
				return null;
			}
		}
		return null;
		
	}
	
	@SuppressWarnings("unused")
	private String generateXPATH(WebElement childElement, String current) {
	    String childTag = childElement.getTagName();
	    if(childTag.equals("html")) {
	        return "/html[1]"+current;
	    }
	    WebElement parentElement = childElement.findElement(By.xpath(".."));
	    List<WebElement> childrenElements = parentElement.findElements(By.xpath("*"));
	    int count = 0;
	    for(int i=0;i<childrenElements.size(); i++) {
	        WebElement childrenElement = childrenElements.get(i);
	        String childrenElementTag = childrenElement.getTagName();
	        if(childTag.equals(childrenElementTag)) {
	            count++;
	        }
	        if(childElement.equals(childrenElement)) {
	            return generateXPATH(parentElement, "/" + childTag + "[" + count + "]"+current);
	        }
	    }
	    
	    return null;
	}
	

	private String GetElementXPath(WebElement element)
	{
	    return (String) ((JavascriptExecutor) _driver).executeScript(
	    "getXPath=function(node)" +
	    "{" +
	        "if (node.id !== '')" +
	        "{" +
	            "return '//' + node.tagName.toLowerCase() + '[@id=\"' + node.id + '\"]'" +
	        "}" +

	        "if (node === document.body)" +
	        "{" +
	            "return node.tagName.toLowerCase()" +
	        "}" +

	        "var nodeCount = 0;" +
	        "var childNodes = node.parentNode.childNodes;" +

	        "for (var i=0; i<childNodes.length; i++)" +
	        "{" +
	            "var currentNode = childNodes[i];" +

	            "if (currentNode === node)" +
	            "{" +
	                "return getXPath(node.parentNode) + '/' + node.tagName.toLowerCase() + '[' + (nodeCount+1) + ']'" +
	            "}" +

	            "if (currentNode.nodeType === 1 && " +
	                "currentNode.tagName.toLowerCase() === node.tagName.toLowerCase())" +
	            "{" +
	                "nodeCount++" +
	            "}" +
	        "}" +
	    "};" +

	    "return getXPath(arguments[0]);", element);
	}
	
}

package com.selenium.webdriver.idobj.model;

import java.awt.Point;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.selenium.webdriver.idobj.observer.IObservable;
import com.selenium.webdriver.idobj.observer.IObserver;


public abstract class IdentifiableObject implements IObservable{
	
	protected List<IObserver> _observers; 
	protected WebDriver _driver;
	protected Point _absolutePointSetup;
	protected WebElement CurWebElement;
	protected String PreviousWebElementStyle; 
	protected WebElement PreviousWebElement;
	
	
	
	
	/**
	 * 
	 */
	protected IdentifiableObject() {
		super();
		
	}
	public abstract void IdentifyElement(Point ElementCurAbsolutePoint);
	public abstract Map<String, String> getElementDetails();
	public abstract String getTagName();
	public abstract boolean testXPath(String Xpath);
	/**
	 * @return the _driver
	 */
	public WebDriver get_driver() {
		return _driver;
	}
	/**
	 * @return the _absolutePoint
	 */
	public Point get_absolutePointSetup() {
		return _absolutePointSetup;
	}
	/**
	 * @param _absolutePoint the _absolutePoint to set
	 */
	public void set_absolutePointSetup(Point _absolutePoint) {
		this._absolutePointSetup = _absolutePoint;
	}


	/**
	 * @param _driver the _driver to set
	 */
	public void set_driver(WebDriver _driver) {
		this._driver = _driver;
	}
	
	

	//manage observers
	
	public void addObserver(IObserver observer) {	
		_observers.add(observer);
	}
	public void deleteObserver(IObserver observer) {
		_observers.remove(observer);
	}
	public void setChanged() {
		// TODO Auto-generated method stub
		
	}
	public void notifyObservers() {
		
		for (Iterator<IObserver> iterator = _observers.iterator(); iterator.hasNext();) {
			IObserver observer =  iterator.next();
			observer.update();	
		}	
	}
	
	
	

}

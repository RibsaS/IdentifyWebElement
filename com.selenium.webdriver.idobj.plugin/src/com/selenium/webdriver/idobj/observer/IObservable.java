package com.selenium.webdriver.idobj.observer;

public interface IObservable {
	
	public void addObserver(IObserver observer);
	public void deleteObserver(IObserver observer);
	public void notifyObservers();
	public void setChanged();

}

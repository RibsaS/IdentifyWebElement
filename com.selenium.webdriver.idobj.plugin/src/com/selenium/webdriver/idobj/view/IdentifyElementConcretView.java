package com.selenium.webdriver.idobj.view;

import java.awt.Point;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;


import com.selenium.webdriver.idobj.controller.IController;

public class IdentifyElementConcretView implements IidentifyElementView {
	
	
	private Display display;
	private Shell shell;
	private IdentifyElementDialog elementGui;
	

	/**
	 * 
	 */
	public IdentifyElementConcretView() {
		super();
		display = Display.getCurrent();
		shell = new Shell(display, SWT.SHELL_TRIM | SWT.TOOL);
//		display = new Display();
//		shell = new Shell(display);
		elementGui  =  new IdentifyElementDialog(shell, SWT.DIALOG_TRIM ) ;
		
	}

	public void setViewController(IController listener) {
		elementGui.setViewController(listener);

	}

	public void willConnectToUrl() {
		elementGui.willConnectToUrl();
	}

	public void UrlConnectionFailed() {
		elementGui.UrlConnectionFailed();

	}

	public void UrlConnectionPassed() {
		elementGui.UrlConnectionPassed();

	}

	public void updateCoordinates(Point absoluteCoordinates,
			Point relativeCoordinates) {
		elementGui.updateCoordinates(absoluteCoordinates, relativeCoordinates);

	}

	public void display() {
		elementGui.display();	
	}

	public void updateWebElementDetails(
			Map<String, String> ElementDetails) {
		elementGui.updateWebElementDetails(ElementDetails);
	}

	public void updateTagName(String TagName) {
		elementGui.updateTagName(TagName);
		
	}


}

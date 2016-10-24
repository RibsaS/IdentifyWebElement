package com.selenium.webdriver.idobj.view;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
//import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;



public class ImageTextToolTip extends ToolTip
{



	public ImageTextToolTip(Control control) {
		super(control, NO_RECREATE, false);
		// TODO Auto-generated constructor stub
	}




	
	
	
	
	//protected Composite createToolTipContentArea(final Event event, final Composite parent)
	@Override
	protected Composite createToolTipContentArea(final Event event, final Composite parent)
	{

		Composite body = new Composite(parent, SWT.NONE);

		body.setLayout(new GridLayout());
		
		
		Label labelText = new Label(body, SWT.LEAD);
		labelText.setFont(
			    JFaceResources.getFontRegistry().getBold(JFaceResources.BANNER_FONT)
				);
		labelText.setText("Click on the top left corner of the opened webpage like shown below ");
		
		Label labelImage = new Label(body, SWT.CENTER);
		Image image = new Image(Display.getCurrent(),IdentifyElementDialog.class.getClassLoader().getResourceAsStream("Images/SetupCoordinatesPosition.gif"));
		labelImage.setImage(image);

		
		return body;
	}
}
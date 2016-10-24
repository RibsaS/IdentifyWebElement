package com.selenium.webdriver.idobj.plugin.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
//import org.eclipse.ui.IWorkbenchWindow;
//import org.eclipse.ui.handlers.HandlerUtil;
//import org.eclipse.jface.dialogs.MessageDialog;

import com.selenium.webdriver.idobj.IdentifyObject;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class IdentifyObjectPlugin extends AbstractHandler {
	/**
	 * The constructor.
	 */
	public IdentifyObjectPlugin() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
//		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
//		MessageDialog.openInformation(
//				window.getShell(),
//				"Identify Object",
//				"Hello, Eclipse world");
		IdentifyObject.start();
		return null;
	}
}

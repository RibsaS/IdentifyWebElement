package com.selenium.webdriver.idobj.view;

import java.awt.MouseInfo;
import java.awt.Point;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Table;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.TableColumn;

import com.selenium.webdriver.idobj.controller.IController;
import com.selenium.webdriver.idobj.controller.IController.EControlledItem;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.NativeInputEvent;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseListener;
import org.jnativehook.mouse.NativeMouseMotionListener;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.ProgressBar;
//import org.eclipse.ui.forms.widgets.FormToolkit;



public class IdentifyElementDialog extends Dialog implements IidentifyElementView{
	
	private ImageTextToolTip tip; 
	protected Object result;
	protected Shell shlIdentifyObject;
	private Text textTagName;
	private Table DetailsTable;
	private Text textUrl;
	
	private Combo combo_Browser;
	
	private Button btnStartCoordinates;
	private Label lblAbsoluteX;
	private Label lblAbsoluteY;
	private Label lblRelativeX;
	private Label lblRelativeY;
	
	private Button btnIdentify;
	private Combo comboIdentificationMode;
	private Combo comboIdentificationKey;
	private Label lblKey;
	
	
	private IController controller;
	private Point _topLeftCornerAbsolutePosition;
	private String[] _supportedBrowser;
	private final String IDENTIFICATION_BY_CLICK = "Click"; 
	private final String IDENTIFICATION_BY_HOTKEY = "HotKey";
	private String[] _IdentificationMode = {IDENTIFICATION_BY_CLICK,IDENTIFICATION_BY_HOTKEY};
	private String[] _IdedntificationKeys = {NativeInputEvent.getModifiersText(NativeInputEvent.ALT_MASK) +
			"+" + NativeInputEvent.getModifiersText(NativeInputEvent.CTRL_MASK),
			NativeInputEvent.getModifiersText(NativeInputEvent.ALT_MASK) +
			"+" + NativeInputEvent.getModifiersText(NativeInputEvent.SHIFT_MASK)};
	
	
	private  SetupCoordinatesMouseListner setupCoordinatesMouseListner;
	private  IdentificationListner identificationListner;
	private ScrolledComposite scrolledCompositeTable;
	private TableColumn tblclmnProperty;
	private TableColumn tblclmnValue;
	private DetailsTableEditorListener tableEditorListener;
	
	private Button btnTest;
	
	private ProgressBar progressBar;
	//private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());


	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public IdentifyElementDialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * @param _supportedBrowser the _supportedBrowser to set
	 */
	public void set_supportedBrowser(String[] _supportedBrowser) {
		this._supportedBrowser = _supportedBrowser;
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shlIdentifyObject.open();
		shlIdentifyObject.layout();
		Display display = getParent().getDisplay();
		while (!shlIdentifyObject.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	public void setViewController(IController listener) {
		
		this.controller = listener ;
		if (this.controller != null){
			this._supportedBrowser = this.controller.getSupportedBrowsers();
		}
		
	}

	public void willConnectToUrl() {
		
	}

	public void UrlConnectionFailed() {
		UpdateGui(GUI_Element.connectionFailed, new Object());
	}

	public void UrlConnectionPassed() {
		UpdateGui(GUI_Element.ConnectionPassed, new Object());
	}
	
	public void updateCoordinates(final Point absoluteCoordinates, final Point relativeCoordinates) {
		 
    	_topLeftCornerAbsolutePosition = new Point(absoluteCoordinates.getLocation());   
    	lblAbsoluteX.setText("X:" + absoluteCoordinates.getX());
   		lblAbsoluteY.setText("Y: " + absoluteCoordinates.getY());
   		lblRelativeX.setText("X: " + relativeCoordinates.getX());
   		lblRelativeY.setText("Y: " + relativeCoordinates.getY());
   		shlIdentifyObject.redraw();
   		controller.controll(EControlledItem._CoordinatesSetup, _topLeftCornerAbsolutePosition);
   		StopCoordinatesSetup();	
	}
	
	public void updateWebElementDetails(final Map<String, String> ElementDetails) {
	
		UpdateGui(GUI_Element.setWebElementDetail, ElementDetails);
	}

	public void updateTagName(String TagName) {
		
		UpdateGui(GUI_Element.setTagName, TagName);
	}

	public void display() {
		open();
		
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shlIdentifyObject = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.ON_TOP | SWT.APPLICATION_MODAL);
		shlIdentifyObject.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				try {
					GlobalScreen.unregisterNativeHook();
				} catch (NativeHookException ex) {
					MessageDialog.openError(shlIdentifyObject, "Error", ex.getMessage());
				}
			}
		});
		shlIdentifyObject.setSize(627, 504);
		shlIdentifyObject.setText("Identify Web Element");
		
		tip = new ImageTextToolTip(shlIdentifyObject);
		tip.deactivate();
		shlIdentifyObject.setLayout(null);
		
		Group grpIdentify = new Group(shlIdentifyObject, SWT.NONE);
		grpIdentify.setBounds(10, 149, 598, 82);
		grpIdentify.setText("Identify Element");
		
		btnIdentify = new Button(grpIdentify, SWT.NONE);
		btnIdentify.setEnabled(false);
		btnIdentify.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {	
				UpdateGui(GUI_Element.changeStartIdentificationButtonCaption, "stop");
				StartWebElementIdentification(comboIdentificationMode.getText());
			}
		});
		btnIdentify.setBounds(24, 32, 75, 25);
		btnIdentify.setText("Start");
		
		comboIdentificationMode = new Combo(grpIdentify, SWT.NONE);
		comboIdentificationMode.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean bVisible = comboIdentificationMode.getText().equalsIgnoreCase("hotkey") ;
				comboIdentificationKey.setVisible(bVisible);
				lblKey.setVisible(bVisible);
			}
		});
		comboIdentificationMode.setBounds(183, 34, 124, 23);
		InitCombo(comboIdentificationMode, _IdentificationMode, 0);
		
		Label lblMode = new Label(grpIdentify, SWT.NONE);
		lblMode.setAlignment(SWT.RIGHT);
		lblMode.setBounds(122, 37, 55, 15);
		lblMode.setText("Mode:");
		
		lblKey = new Label(grpIdentify, SWT.NONE);
		lblKey.setAlignment(SWT.RIGHT);
		lblKey.setBounds(335, 40, 55, 15);
		lblKey.setText("Keys:");
		lblKey.setVisible(false);
		
		
		comboIdentificationKey = new Combo(grpIdentify, SWT.NONE);
		comboIdentificationKey.setBounds(396, 32, 124, 23);
		InitCombo(comboIdentificationKey,_IdedntificationKeys,0);
		comboIdentificationKey.setVisible(false);
		
		Group grpProperties = new Group(shlIdentifyObject, SWT.NONE);
		grpProperties.setBounds(10, 237, 601, 204);
		grpProperties.setText("Element Properties");
		grpProperties.setLayout(null);
		
		Label lblLocator = new Label(grpProperties, SWT.NONE);
		lblLocator.setBounds(10, 25, 34, 15);
		lblLocator.setText("XPath:");
		
		textTagName = new Text(grpProperties, SWT.BORDER);
		textTagName.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				btnTest.setEnabled(!textTagName.getText().isEmpty());
			}
		});
		textTagName.setBounds(50, 22, 473, 21);
		
		scrolledCompositeTable = new ScrolledComposite(grpProperties, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledCompositeTable.setBounds(10, 60, 588, 134);
		scrolledCompositeTable.setExpandHorizontal(true);
		scrolledCompositeTable.setExpandVertical(true);
		
		DetailsTable = new Table(scrolledCompositeTable, SWT.BORDER | SWT.FULL_SELECTION |  SWT.V_SCROLL | SWT.H_SCROLL );
		DetailsTable.setHeaderVisible(true);
		DetailsTable.setLinesVisible(true);
		if (tableEditorListener== null) {
			tableEditorListener = new DetailsTableEditorListener(DetailsTable, 1);
		}
		DetailsTable.addSelectionListener(tableEditorListener);
		
		tblclmnProperty = new TableColumn(DetailsTable, SWT.NONE);
		tblclmnProperty.setWidth(300);
		tblclmnProperty.setText("Property");
		
		tblclmnValue = new TableColumn(DetailsTable, SWT.NONE);
		tblclmnValue.setWidth(259);
		tblclmnValue.setText("Value");
		scrolledCompositeTable.setContent(DetailsTable);
		scrolledCompositeTable.setMinSize(DetailsTable.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		btnTest = new Button(grpProperties, SWT.NONE);
		btnTest.setEnabled(false);
		btnTest.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String returnedMessage = controller.controll(EControlledItem._Xpath, textTagName.getText());
				System.out.println("Xpath: " + textTagName.getText());
				System.out.println("returnedMessage:" + returnedMessage);
				if (returnedMessage != null && ! returnedMessage.isEmpty()){
					MessageDialog.openError(shlIdentifyObject, "Error", returnedMessage);
				}
			}
		});
		btnTest.setBounds(523, 20, 56, 25);
		btnTest.setText("Test");
		grpProperties.setTabList(new Control[]{textTagName, btnTest, scrolledCompositeTable});
		
		Group grpCoordinatesSetup = new Group(shlIdentifyObject, SWT.NONE);
		grpCoordinatesSetup.setBounds(10, 57, 598, 82);
		grpCoordinatesSetup.setText("Coordinates Setup");
		
		Label lblAbsolute = new Label(grpCoordinatesSetup, SWT.NONE);
		lblAbsolute.setBounds(188, 20, 55, 15);
		lblAbsolute.setText("Absolute: ");
		
		Label lblRelative = new Label(grpCoordinatesSetup, SWT.NONE);
		lblRelative.setEnabled(false);
		lblRelative.setBounds(188, 46, 55, 15);
		lblRelative.setText("Relative:");
		
		lblAbsoluteX = new Label(grpCoordinatesSetup, SWT.NONE);
		lblAbsoluteX.setBounds(298, 20, 55, 15);
		lblAbsoluteX.setText("X:");
		
		lblAbsoluteY = new Label(grpCoordinatesSetup, SWT.NONE);
		lblAbsoluteY.setBounds(427, 20, 55, 15);
		lblAbsoluteY.setText("Y:");
		
		lblRelativeX = new Label(grpCoordinatesSetup, SWT.NONE);
		lblRelativeX.setEnabled(false);
		lblRelativeX.setBounds(298, 46, 55, 15);
		lblRelativeX.setText("X:");
		
		lblRelativeY = new Label(grpCoordinatesSetup, SWT.NONE);
		lblRelativeY.setEnabled(false);
		lblRelativeY.setBounds(427, 46, 55, 15);
		lblRelativeY.setText("Y:");
		
		btnStartCoordinates = new Button(grpCoordinatesSetup, SWT.NONE);
		btnStartCoordinates.setEnabled(false);
		btnStartCoordinates.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (e.getSource() instanceof Button){
					if (btnStartCoordinates.getText().equalsIgnoreCase("start"))
					{	UpdateGui(GUI_Element.setToolTipMessage, "");
						StartCoordinatesSetup();
					}else if ((btnStartCoordinates.getText().equalsIgnoreCase("stop")))
							{
						StopCoordinatesSetup();
							}
				}
			}
		});
		btnStartCoordinates.setBounds(22, 30, 75, 25);
		btnStartCoordinates.setText("Start");
		
		Label lblUrl = new Label(shlIdentifyObject, SWT.NONE);
		lblUrl.setBounds(10, 17, 37, 15);
		lblUrl.setAlignment(SWT.RIGHT);
		lblUrl.setText("URL:");
		
		textUrl = new Text(shlIdentifyObject, SWT.BORDER);
		textUrl.setBounds(53, 14, 399, 21);
		
		Button btnGo = new Button(shlIdentifyObject, SWT.NONE);
		btnGo.setBounds(553, 12, 55, 25);
		btnGo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {			
				ExecutorService executorService = Executors.newSingleThreadExecutor();
				RunnableOpenBrowser runnableOpenBrowser = new RunnableOpenBrowser(textUrl.getText(),combo_Browser.getText());
				executorService.execute(runnableOpenBrowser);
				progressBar.setVisible(true);
				executorService.shutdown();
				while (! runnableOpenBrowser.isFinished() && progressBar.isVisible()) {
					
					progressBar.redraw();
					progressBar.update();
					try {
						Thread.sleep(100);
					} catch (InterruptedException e1) {
					}
					if ( runnableOpenBrowser.getReturnedMessage() != null && ! runnableOpenBrowser.getReturnedMessage().isEmpty() ){
						MessageDialog.openError(shlIdentifyObject, "Error", runnableOpenBrowser.getReturnedMessage());
						break;
					}
				}
				progressBar.setVisible(false);
			}
		});
		btnGo.setText("Go");
		
		combo_Browser = new Combo(shlIdentifyObject, SWT.NONE);
		combo_Browser.setBounds(458, 13, 91, 23);
		InitSupportedBrowsers(combo_Browser);
		
		progressBar = new ProgressBar(shlIdentifyObject, SWT.INDETERMINATE| SWT.HORIZONTAL);
		progressBar.setBounds(20, 447, 588, 17);
		progressBar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		progressBar.setVisible(false);
		shlIdentifyObject.setTabList(new Control[]{textUrl, combo_Browser, btnGo, grpCoordinatesSetup, grpIdentify, grpProperties, progressBar});
	
		
	
	}

	/**
	 * Init the supported browser combo
	 * @param combo
	 */
	private void InitSupportedBrowsers(Combo combo){
		
		InitCombo(combo,_supportedBrowser,0);
	}

	private void InitCombo(Combo combo,String[] data, int selectedItem )
	{
		if (data != null){
			for (int i = 0; i < data.length; i++) {
				combo.add(data[i]);
			}
			combo.select(selectedItem);
		}
	}
	
	private void connectionFailed(){
		textUrl.selectAll();
		progressBar.setVisible(false);
	}
	
	private void connectionPassed(){
		textUrl.setEnabled(false);
		combo_Browser.setEnabled(false);
		btnStartCoordinates.setEnabled(true);
	}

	private String openBrowser(String Url,String Browser){
		String returnedMessage="";
		String[]  NeededSetups= new String[2];
		NeededSetups[0] = Url;
		NeededSetups[1] = Browser;
		returnedMessage = controller.controll(EControlledItem._Url,NeededSetups);
//		if (returnedMessage != ""){
//			MessageDialog.openError(shlIdentifyObject, "Error", returnedMessage);
//		}
		return returnedMessage;
	}

	private void StartCoordinatesSetup(){
	
		try {
			setupCoordinatesMouseListner = new SetupCoordinatesMouseListner();
			GlobalScreen.registerNativeHook();
			GlobalScreen.addNativeMouseListener(setupCoordinatesMouseListner );
			
		} catch (NativeHookException ex) {
			
			MessageDialog.openError(shlIdentifyObject, "Error", ex.getMessage());
			
		}
		
		btnStartCoordinates.setText("Stop");
		
	}

	private void StopCoordinatesSetup(){
		
		try {
			GlobalScreen.removeNativeMouseListener(setupCoordinatesMouseListner);
			GlobalScreen.unregisterNativeHook();
			btnStartCoordinates.setText("Start");
			btnIdentify.setEnabled(true);
		} catch (NativeHookException ex) {
			MessageDialog.openError(shlIdentifyObject, "Error", ex.getMessage());
		}
	}

	private void StartWebElementIdentification(String sIdentificationMode){
		
		try {
			
			if (identificationListner == null ) {
				identificationListner = new IdentificationListner();
			}
			
			//start native listner
			GlobalScreen.registerNativeHook();
			GlobalScreen.addNativeMouseMotionListener(identificationListner);
			
			if (sIdentificationMode.equalsIgnoreCase(IDENTIFICATION_BY_CLICK)) {	
				GlobalScreen.addNativeMouseListener(identificationListner);
			} else if (sIdentificationMode.equalsIgnoreCase(IDENTIFICATION_BY_HOTKEY)) {
				System.out.println("keylistener is going to be added");
				GlobalScreen.addNativeKeyListener(identificationListner);
			}
			
			
		} catch (NativeHookException ex) {	
			MessageDialog.openError(shlIdentifyObject, "Error", ex.getMessage());	
		}
	}

	private void changeTextOnBtnStartIdentification(final String Text)
	{
		  btnIdentify.setText(Text);
	}
	
	

	private String getSelectedIdentificationKeys(){
		return comboIdentificationKey.getText();
	}
	
	
	
	private void setWebElementDetails(final Map<String, String> ElementDetails){
		System.out.println("size " + DetailsTable.getItemCount() + "=" + ElementDetails.size());
		DetailsTable.removeAll();

		for (Map.Entry<String, String> entry : ElementDetails.entrySet()) {

			TableItem TableItemLine = new TableItem(DetailsTable, SWT.NONE);
			TableItemLine.setText(0, entry.getKey());
			TableItemLine.setText(1, entry.getValue() );
			
		}
		
		
		DetailsTable.pack();

	}
	
	private Object UpdateGui(GUI_Element element,Object ... InputData){
		RunnableUpdateGui runnableUpdateGui = new RunnableUpdateGui(element,InputData);
		Thread thread = new Thread(runnableUpdateGui);
		thread.start();
		try {
			thread.join(3000);
		} catch (InterruptedException ex) {
			MessageDialog.openError(shlIdentifyObject, "Error", ex.getMessage());
		}
		return runnableUpdateGui.getReturnedData();
		
	}


	private void setTagName(String TagName)
	{
		textTagName.setVisible(true);
		textTagName.setText(TagName);
		textTagName.setEditable(true);
		shlIdentifyObject.pack();
		
	}
	
	private  void showProgressBar(boolean isToShow){
		if (progressBar.isDisposed())
            return;
		if (progressBar.isVisible() != isToShow)
			progressBar.setVisible(isToShow);
		System.out.println("set visible");

			if (progressBar.isDisposed())
            return;
				shlIdentifyObject.update();
	     }
	
	private void ShowStartSetupCoordinatesToolTip(){
		
		tip.createToolTipContentArea(null, shlIdentifyObject);
		tip.show(btnIdentify.getLocation());
		shlIdentifyObject.update();
	}

	static enum GUI_Element{
		setCoordianatesSetup,
		setWebElementDetail,
		getSelectedIdentificationKeys,
		changeStartIdentificationButtonCaption,
		setTagName,
		progressBar,
		connectionFailed,
		ConnectionPassed,
		setToolTipMessage
	}
	
	class RunnableUpdateGui implements Runnable {
		
		boolean updateDone = false;
		GUI_Element elementToUpdate;
		Object[] InputData;
		Object returnedData;
		
		
		
		/**
		 * @return the returnedData
		 */
		public Object getReturnedData() {
			return returnedData;
		}

		protected RunnableUpdateGui(GUI_Element element, Object ... InputData) {
			super();
			this.elementToUpdate = element;
			this.InputData = InputData;
		}

		public void ManageElementToUpdate(){
			if (elementToUpdate.equals(GUI_Element.setCoordianatesSetup)) {
				Point AbsolutePoint = (Point) InputData[0];
				Point RelatifPoint = (Point) InputData[1];
				updateCoordinates(AbsolutePoint,RelatifPoint);
			} else if (elementToUpdate.equals(GUI_Element.changeStartIdentificationButtonCaption)) {
				String NewCaption = (String) InputData[0];
				changeTextOnBtnStartIdentification(NewCaption);
			} else if (elementToUpdate.equals(GUI_Element.setWebElementDetail)) {
				@SuppressWarnings("unchecked")
				Map<String, String> details = (Map<String, String>) InputData[0];
				setWebElementDetails(details);
			}else if (elementToUpdate.equals(GUI_Element.setTagName)) {
				String TagName = (String) InputData[0];
				setTagName(TagName);
			} else if (elementToUpdate.equals(GUI_Element.getSelectedIdentificationKeys)) {
				returnedData = getSelectedIdentificationKeys();
			}else if (elementToUpdate.equals(GUI_Element.progressBar)) {
				Boolean visible = (Boolean)InputData[0];
				showProgressBar(visible);
			}else if (elementToUpdate.equals(GUI_Element.connectionFailed)) {
				connectionFailed();
			}else if (elementToUpdate.equals(GUI_Element.ConnectionPassed)) {
				connectionPassed();
			}else if (elementToUpdate.equals(GUI_Element.setToolTipMessage)) {
				ShowStartSetupCoordinatesToolTip();
			}
		}
		
		public void run() {
			while (! updateDone) {
				try { Thread.sleep(1000); } catch (Exception e) { }
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						ManageElementToUpdate();
						updateDone = true;
					}
				});
			}
		}

	}
	
	class SetupCoordinatesMouseListner implements  NativeMouseListener {
		
		public void nativeMouseReleased(NativeMouseEvent e) {
			// TODO Auto-generated method stub

		}

		public void nativeMousePressed(NativeMouseEvent e) {

		}

		public void nativeMouseClicked(NativeMouseEvent e) {
			if  ( e.getSource() != btnStartCoordinates){
				UpdateGui(GUI_Element.progressBar,true);
				UpdateGui(GUI_Element.setCoordianatesSetup, MouseInfo.getPointerInfo().getLocation(), new Point(0, 0));
				UpdateGui(GUI_Element.progressBar,false);
			}
		}
	}
	//********************************************************************************************************
	//                                     Listners used when identification is started
	//********************************************************************************************************
	class IdentificationListner implements NativeMouseMotionListener, NativeMouseListener, 
	NativeKeyListener
	{

		// override NativeMouseListener Methods
		public void nativeMouseMoved(final NativeMouseEvent e) {
			controller.controll(EControlledItem._IdentifyElement, e.getPoint());	
		}

		public void nativeMouseDragged(NativeMouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		// override NativeMouseListener Methods

		public void nativeMouseReleased(NativeMouseEvent e) {
			// TODO Auto-generated method stub

		}

		public void nativeMousePressed(NativeMouseEvent e) {

		}

		public void nativeMouseClicked(NativeMouseEvent e) {
			try {
				UpdateGui(GUI_Element.progressBar,true);
				GlobalScreen.removeNativeMouseListener(this);
				GlobalScreen.removeNativeMouseMotionListener(this);
				GlobalScreen.unregisterNativeHook();
				controller.controll(EControlledItem._ElementDetails, null);
				UpdateGui(GUI_Element.changeStartIdentificationButtonCaption, "Start");
				UpdateGui(GUI_Element.progressBar,false);
			} catch (NativeHookException ex) {
				MessageDialog.openError(shlIdentifyObject, "Error", ex.getMessage());
			}
		}

		public void nativeKeyPressed(NativeKeyEvent e) {
			System.out.println("nativeKeypressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
			String ShiftText = NativeInputEvent.getModifiersText(NativeInputEvent.SHIFT_MASK);
			String CtrlText  = NativeInputEvent.getModifiersText(NativeInputEvent.CTRL_MASK);
			String AltText   = NativeInputEvent.getModifiersText(NativeInputEvent.ALT_MASK);
			String selectedIdentificationKey;
			selectedIdentificationKey = (String) UpdateGui(GUI_Element.getSelectedIdentificationKeys, new Object());
			if (selectedIdentificationKey != null && ! selectedIdentificationKey.isEmpty()) {

				boolean isAltCtrl = selectedIdentificationKey.equalsIgnoreCase(AltText + "+" + CtrlText) && keyModifiersMatchText(e, AltText+"+"+CtrlText,  CtrlText+"+"+AltText);
				boolean isAltShift = selectedIdentificationKey.equalsIgnoreCase(AltText + "+" + ShiftText) && keyModifiersMatchText(e, ShiftText+"+"+AltText,  AltText+"+"+ShiftText);

				if (isAltShift || isAltCtrl) {
					try {
						UpdateGui(GUI_Element.progressBar,true);
						GlobalScreen.removeNativeMouseListener(this);
						GlobalScreen.removeNativeMouseMotionListener(this);
						GlobalScreen.unregisterNativeHook();
						controller.controll(EControlledItem._ElementDetails, null);
						UpdateGui(GUI_Element.changeStartIdentificationButtonCaption, "Start");
						UpdateGui(GUI_Element.progressBar,false);
					} catch (NativeHookException ex) {
						MessageDialog.openError(shlIdentifyObject, "Error", ex.getMessage());
					}
				}
			}
			
		}

		public void nativeKeyReleased(NativeKeyEvent e) {
			//nothing
		}

		public void nativeKeyTyped(NativeKeyEvent e) {
			//nothing
		}
		
		private boolean keyModifiersMatchText(NativeKeyEvent event, String... texts) {
			for (String text : texts) {
				System.out.println("keyModifiersMatchText : " + NativeKeyEvent.getModifiersText(event.getModifiers()));
				if (NativeKeyEvent.getModifiersText(event.getModifiers()).equals(text)) {
					return true;
				}
			}
			return false;
		}
		
	}
	
	class RunnableOpenBrowser implements  Runnable {
		private volatile Boolean isFinish = false;
		private String returnedMessage = null; 
		private String url;
		private String Browser;
		public Boolean isFinished(){
			return isFinish;
		}
		
		
		public String getReturnedMessage() {
			return returnedMessage;
		}

		public RunnableOpenBrowser(String url,String Browser){
			this.url = url;
			this.Browser = Browser;
		}
		
		public void run() {

        	System.out.println("###############################################################");
        	System.out.println("Start Operation");
        	returnedMessage = openBrowser(url,Browser);
        	System.out.println("Fin Operation");
        	System.out.println("###############################################################");
        	isFinish = true;
		}
	};
	
	class DetailsTableEditorListener extends SelectionAdapter {
		
		private final TableEditor editor; 
		private Table table;
		private int editableColumn;
		
		public DetailsTableEditorListener(Table table,int editableColumn) {
			this.table = table;
			this.editor = new TableEditor(table);
			this.editor.horizontalAlignment = SWT.LEFT;
			this.editor.grabHorizontal = true;
			this.editor.minimumWidth = 50;
			this.editableColumn = editableColumn;
		}
		
		
        public void widgetSelected(SelectionEvent e) {
            // Clean up any previous editor control
            Control oldEditor = editor.getEditor();
            if (oldEditor != null) oldEditor.dispose();
            

            // Identify the selected row
            TableItem item = (TableItem)e.item;
            if (item == null) return;

            // The control that will be the editor must be a child of the Table
            Text newEditor = new Text(table, SWT.NONE);
            newEditor.setText(item.getText(editableColumn));
            newEditor.setVisible(true);
            newEditor.selectAll();
            newEditor.setFocus();
            editor.setEditor(newEditor, item, editableColumn);
            table.pack();
    }
}
}





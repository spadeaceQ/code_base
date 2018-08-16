package slm;

import com.forthdd.commlib.R11CommLibWrapper;
import com.forthdd.commlib.R4CommLibWrapper;
import com.forthdd.commlib.R4R11CommLibInterface;
import com.forthdd.commlib.exceptions.AbstractException;
import com.forthdd.metrocon.FPGARegisterEditor;
import com.forthdd.metrocon.View;
import com.forthdd.metrocon.controller.Controller;
import com.forthdd.metrocon.controller.MainController;
import com.forthdd.metrocon.event.ControlRetrievalEvent;
import com.forthdd.metrocon.event.ModelADCValuesEvent;
import com.forthdd.metrocon.event.ModelActivationStateEvent;
import com.forthdd.metrocon.event.ModelActivationTypeEvent;
import com.forthdd.metrocon.event.ModelBitplaneNumberEvent;
import com.forthdd.metrocon.event.ModelBoardEvent;
import com.forthdd.metrocon.event.ModelBoardIDEvent;
import com.forthdd.metrocon.event.ModelConnectEvent;
import com.forthdd.metrocon.event.ModelDBTypeEvent;
import com.forthdd.metrocon.event.ModelDisconnectEvent;
import com.forthdd.metrocon.event.ModelDisplayEvent;
import com.forthdd.metrocon.event.ModelEvent;
import com.forthdd.metrocon.event.ModelFPGARegisterEvent;
import com.forthdd.metrocon.event.ModelFailedEvent;
import com.forthdd.metrocon.event.ModelFlipTPEvent;
import com.forthdd.metrocon.event.ModelLEDValueEvent;
import com.forthdd.metrocon.event.ModelMaintLEDEvent;
import com.forthdd.metrocon.event.ModelOperationDoneEvent;
import com.forthdd.metrocon.event.ModelProgressEvent;
import com.forthdd.metrocon.event.ModelROCountEvent;
import com.forthdd.metrocon.event.ModelRODefaultEvent;
import com.forthdd.metrocon.event.ModelROSelectedEvent;
import com.forthdd.metrocon.event.ModelRemoteCodeVersionEvent;
import com.forthdd.metrocon.event.ModelRepertoireEvent;
import com.forthdd.metrocon.event.ModelRepertoireNameEvent;
import com.forthdd.metrocon.event.ModelSerialNumberEvent;
import com.forthdd.metrocon.event.ModelSessionRetriesEvent;
import com.forthdd.metrocon.event.ModelTimestampEvent;
import com.forthdd.metrocon.event.UserActivateEvent;
import com.forthdd.metrocon.event.UserConnectEvent;
import com.forthdd.metrocon.event.UserConnectEvent.LinkType;
import com.forthdd.metrocon.event.UserDeactivateEvent;
import com.forthdd.metrocon.event.UserDisconnectEvent;
import com.forthdd.metrocon.event.UserEraseAPEvent;
import com.forthdd.metrocon.event.UserEraseExtFlashEvent;
import com.forthdd.metrocon.event.UserEraseRepertoireEvent;
import com.forthdd.metrocon.event.UserEvent;
import com.forthdd.metrocon.event.UserFlipTPEvent;
import com.forthdd.metrocon.event.UserLedSliderEvent;
import com.forthdd.metrocon.event.UserMaintLedEvent;
import com.forthdd.metrocon.event.UserPFBEvent;
import com.forthdd.metrocon.event.UserProgramFPGAEvent;
import com.forthdd.metrocon.event.UserProgramMicroEvent;
import com.forthdd.metrocon.event.UserRebootEvent;
import com.forthdd.metrocon.event.UserRepertoireSendEvent;
import com.forthdd.metrocon.event.UserSaveSettingsEvent;
import com.forthdd.metrocon.event.UserSerialNumberEvent;
import com.forthdd.metrocon.event.UserSetDefaultROEvent;
import com.forthdd.metrocon.event.UserSetSelectedROEvent;
import com.forthdd.metrocon.model.BoardInfoModel;
import com.forthdd.metrocon.model.FPGARegisterDescriptor;
import com.forthdd.metrocon.model.FPGARegisterID_R11;
import com.forthdd.metrocon.model.FPGARegisterID_R4;
import com.forthdd.metrocon.model.FPGARegisterMapStore;
import com.forthdd.metrocon.model.MainModel;
import com.forthdd.metrocon.model.MainModel.ConnectionStatus;
import com.forthdd.metrocon.model.MicroProgrammingModel;
import com.forthdd.metrocon.model.ProgramEnvironmentModel;
import com.forthdd.metrocon.model.RepertoireModel;
import com.forthdd.metrocon.model.RepertoireProgrammingModel;
import com.forthdd.metrocon.model.USBDevice;
import com.forthdd.metrocon.model.VoltageModel;
import com.forthdd.metrocon.model.VoltageModel.Supply;
import com.forthdd.replib.Config;
import com.forthdd.replib.model.repertoire.RunningOrder;
import com.forthdd.replib.model.repertoire.RunningOrder.Activation;
import com.forthdd.replib.model.repertoire.RunningOrder.ActivationState;
import com.forthdd.util.HexNumber;
import com.forthdd.util.PathFinder;
import com.forthdd.util.SimpleFileFilter;
import com.forthdd.util.StringComparator;
import com.forthdd.util.UpdateableComboBoxModel;
import com.forthdd.replib.*;
import com.forthdd.metrocon.*;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Formatter;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.AbstractListModel;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.SoftBevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.DefaultFormatterFactory;
import ij.*;

public class Main_View 
  extends JFrame
  implements View
  
{
  public MainModel mainModel;
  StatusView statusView;
  SettingsView settingsView;
  ProgramView programView;
  FactoryView factoryView;
  FPGARegisterEditor fred;
  Controller mainController;
  boolean retrieving;
  boolean canActivate = false;
  boolean canDeactivate = false;
  private RunningOrder.ActivationState actState = RunningOrder.ActivationState.RLD;
  public R4R11CommLibInterface r4r11cl;
  public final boolean runningFromJar;
  
  public static void libLogCallback(String log) {}
  
  public static void slaveLogCallback(String log) {}
  
  
  public Main_View()  throws NullPointerException
  {
    System.out.println("The value of Config.DISPLAY_PLATFORM is '" +  "'");
  
   //   this.r4r11cl = new R4CommLibWrapper();
   
    this.runningFromJar = (!PathFinder.runningFromLooseClasses(this));
    try
    {
      this.retrieving = false;
  //    this.mainModel = new MainModel(this);
      
      initComponents();
      File classFile;
      if (this.runningFromJar)
      {
        System.out.println("We seem to be running from a Jar.");
        File jarFile = PathFinder.getJarPath(this);
        System.out.println("jarFile = " + jarFile);
      }
      else
      {
        System.out.println("We seem to be running from unbundled class files, not a Jar.");
        
        classFile = PathFinder.getClassPath(this);
      }
      try
      {
        this.metroConLabel.setText("");
      }
      catch (MissingResourceException e)
      {
        this.metroConLabel.setText("V3.1? (no resource bundle)");
      }
      setTitle("SLM Control");
      try
      {
        this.metroLibLabel.setText("some version");
      }
      catch (UnsatisfiedLinkError e)
      {
        this.metroLibLabel.setText("No MetroLib");
      }
      setTabVisibility(this.mainTabbedPane, "", this.factoryPanel, false);
      setTabVisibility(this.mainTabbedPane, "", this.flashPanel, false);
      this.rs485_Panel.setVisible(false);
      
      setLocation(1, 1);
   //   this.fred = new FPGARegisterEditor(this.fpgaPanel, this.mainModel.boardInfoModel);
      try
      {
        setIconImage(this.mainModel.programEnvironmentModel.icon.getImage());
        
 //       this.mainController = new MainController(this, this.mainModel);
        this.statusView = new StatusView();
        this.settingsView = new SettingsView();
        this.factoryView = new FactoryView();
        this.programView = new ProgramView();
      }
      catch (Exception e)
      {
    	  
    	  if (e.toString() == "java.lang.NullPointerException"){
    		  
    	  }else{e.printStackTrace();}
        
      }
      startTimer();
    }
    catch (NullPointerException e)
    {
      //e.printStackTrace();
      System.exit(1);
    }
  }
  
 
	  
  
  
  void startTimer()
  {
    Action action = new AbstractAction()
    {
      public void actionPerformed(ActionEvent e)
      {
        try
        {
          Main_View.this.periodicUpdate();
        }
        catch (UnsatisfiedLinkError le)
        {
          le.printStackTrace();
        }
      }
    };
    Timer mainViewTimer = new Timer(1000, action);
    mainViewTimer.start();
  }
  
  void periodicUpdate()
  {
    try{
	  switch (this.mainModel.connectionStatus)
    {
    case RS232: 
    case RS485: 
    case USB: 
      allUpdates();
      break;
    case NO: 
      this.statusView.updateRS232PortAvailibility();
      this.programView.updateConnectionEnables();
      this.statusView.updateUsbAvailibility();
      break;
    case BUSY: 
      this.programView.doConnectionDisables();
    }
   }catch(Exception e){
 	  if (e.toString() == "NullPointerException"){
		  
 	  }else{}

   }
  
  }
  
  void allUpdates()
  {
    if (this.mainModel.boardInfoModel.isExpectedBoardType())
    {
      this.retrieving = true;
      
      this.retrieving = false;
    }
    this.programView.updateConnectionEnables();
  }
  
  void adcUpdate(ModelADCValuesEvent evt)
  {
    DecimalFormat df = new DecimalFormat("0.00");
    JLabel[] powerLabels = { this.power12VLabel, this.power5VLabel, this.power3V3Label, this.power2V5Label, this.power1V8Label, this.power1V2Label };
    
    VoltageModel.Supply[] supplies = false ? VoltageModel.suppliesR11 : VoltageModel.suppliesR4;
    
    boolean allInRange = true;
    for (int i = 0; i < powerLabels.length; i++)
    {
      VoltageModel.Supply s = supplies[i];
      double measuredVoltage = VoltageModel.calculateVoltage(s, evt.getValue(s.channel));
      String text = df.format(measuredVoltage);
      powerLabels[i].setText(text);
      boolean inRange = VoltageModel.inRange(s, measuredVoltage);
      if (!inRange) {
        allInRange = false;
      }
      powerLabels[i].setForeground(inRange ? Color.BLACK : Color.RED);
    }
    this.supplyInLabel.setText("+" + this.power12VLabel.getText() + "V");
    this.supplyInLabel.setForeground(this.power12VLabel.getForeground());
    if (allInRange)
    {
      this.systemPowerLabel.setText("OK");
      this.systemPowerLabel.setForeground(Color.BLACK);
    }
    else
    {
      this.systemPowerLabel.setText("Bad");
      this.systemPowerLabel.setForeground(Color.RED);
    }
  }
  
  static final DecimalFormat displayTempDecForm = new DecimalFormat("0.0");
  
  void displayUpdate(ModelDisplayEvent evt)
  {
    int displayID = evt.id;
    String text1;   
    if (false)
    {
      String text;
      switch (displayID)
      {
      case 255: 
        text = "UNKNOWN"; break;
      case 0: 
        text = "None"; break;
      case 3: 
        text = "M150"; break;
      default: 
        text = String.format("0x%02X", new Object[] { Integer.valueOf(displayID) });break;
      }
    }
    else
    {
      switch (displayID)
      {
      case 0: 
        text1 = "None"; break;
      case 1: 
        text1 = "M149"; break;
      case 2: 
        text1 = "M108"; break;
      default: 
        text1 = String.format("0x%02X", new Object[] { Integer.valueOf(displayID) });
      }
    }
    this.displayConnectedLabel.setText("");
    if (evt.temperatureValid)
    {
      String str = displayTempDecForm.format(evt.temperature) + "C";
      this.displayTemperatureLabel.setText(str);
      this.displayTemperatureLabel.setToolTipText(null);
    }
    else
    {
      this.displayTemperatureLabel.setText("-");
      this.displayTemperatureLabel.setToolTipText("Remote display has no sensor.");
    }
  }
  
  void fpgaEditorUpdate(ModelFPGARegisterEvent evt)
  {
    FPGARegisterMapStore regStore = evt.regStore;
    if (regStore != null)
    {
      for (FPGARegisterDescriptor rd : FPGARegisterMapStore.registerMap)
      {
        int regVal = regStore.getIntValue(rd.id);
        this.fred.setField(rd.id, regVal);
      }
      if (false)
      {
        int identRev = regStore.getIntValue(FPGARegisterID_R11.IDENT);
        int ident = identRev >> 8 & 0xFF;
        int rev = identRev & 0xFF;
        int source = regStore.getIntValue(FPGARegisterID_R11.SOURCE_REV_CORE);
        int build = regStore.getIntValue(FPGARegisterID_R11.BUILD_NUM_CORE);
        this.fpgaCodeVersionLabel.setText(ident + "-" + rev + "-" + source + "-" + build);
      }
      else
      {
        int ident = regStore.getIntValue(FPGARegisterID_R4.IDENT0);
        int rev = regStore.getIntValue(FPGARegisterID_R4.IDENT1);
        int source = regStore.getIntValue(FPGARegisterID_R4.SOURCE_REV);
        int build = regStore.getIntValue(FPGARegisterID_R4.BUILD_NUM);
        this.fpgaCodeVersionLabel.setText(ident + "-" + rev + "-" + source + "-" + build);
      }
    }
    else
    {
      for (FPGARegisterDescriptor rd : FPGARegisterMapStore.registerMap) {
        this.fred.setField(rd.id, 0);
      }
      this.fpgaCodeVersionLabel.setText("Unknown");
    }
  }
  
  RepertoireEditor getRepertoireEditor()
  {
    return (RepertoireEditor)this.repertoireEditorPanel;
  }
  
  public void process(ModelEvent evt)
  {
    try
    {
      if ((evt instanceof ModelEvent))
      {
        ModelEvent me = evt;
        if (me.affectsMainResultLabel)
        {
          if (evt.message != null) {
            this.mainResultLabel.setText(evt.message);
          }
          if (evt.throwable != null) {
            this.mainResultLabel.setToolTipText(evt.throwable.toString());
          } else {
            this.mainResultLabel.setToolTipText(null);
          }
          if ((evt instanceof ModelFailedEvent)) {
            this.mainResultLabel.setForeground(Color.RED);
          } else {
            this.mainResultLabel.setForeground(Color.BLACK);
          }
        }
      }
      if (evt.isConnectionRelevant())
      {
        this.factoryView.updateConnectionEnables();
        this.settingsView.updateConnectionEnables();
        this.statusView.updateConnectionEnables();
        this.programView.updateConnectionEnables();
        getRepertoireEditor().updateControlEnables();
        if ((evt instanceof ModelConnectEvent))
        {
          this.statusView.updateConnectionFields();
          this.settingsView.updateConnectionFields();
        }
        if ((evt instanceof ModelDisconnectEvent))
        {
          ModelDisconnectEvent evt2 = (ModelDisconnectEvent)evt;
          this.statusView.clearFields();
          this.settingsView.clearFields();
          if (evt2.switchToMainTab) {
            this.mainTabbedPane.setSelectedIndex(0);
          }
        }
        if ((evt instanceof ModelFailedEvent))
        {
          ModelFailedEvent evt2 = (ModelFailedEvent)evt;
          if (evt2.dropConnection)
          {
            this.mainModel.disconnectDontReportToView();
            this.statusView.clearFields();
            this.settingsView.clearFields();
            this.mainTabbedPane.setSelectedIndex(0);
          }
        }
      }
      if ((evt instanceof ModelProgressEvent))
      {
        ModelProgressEvent mpe = (ModelProgressEvent)evt;
        this.mainProgressBar.setIndeterminate(mpe.indeterminate);
        this.mainProgressBar.setValue(mpe.percent);
        if (mpe.extraText == null)
        {
          this.mainProgressBar.setStringPainted(false);
        }
        else
        {
          this.mainProgressBar.setString(mpe.extraText);
          this.mainProgressBar.setStringPainted(true);
        }
      }
      else
      {
        this.mainProgressBar.setIndeterminate(false);
        this.mainProgressBar.setStringPainted(false);
        this.mainProgressBar.setValue(0);
        setCursor(Cursor.getPredefinedCursor(0));
      }
      if ((evt instanceof ModelOperationDoneEvent))
      {
        ModelOperationDoneEvent evt2 = (ModelOperationDoneEvent)evt;
        if ((evt2.cause instanceof UserRepertoireSendEvent)) {
          ((RepertoireEditor)this.repertoireEditorPanel).sendDone();
        }
        if (evt2.switchToMainTab) {
          this.mainTabbedPane.setSelectedIndex(0);
        }
      }
      if ((evt instanceof ModelRepertoireEvent)) {
        ((RepertoireEditor)this.repertoireEditorPanel).process((ModelRepertoireEvent)evt);
      }
      if ((evt instanceof ModelBoardEvent))
      {
        if ((evt instanceof ModelLEDValueEvent))
        {
          ModelLEDValueEvent evt2 = (ModelLEDValueEvent)evt;
          this.retrieving = true;
          this.ledSlider.setValue(evt2.value);
          this.ledSpinner.setValue(Integer.valueOf(evt2.value));
          this.retrieving = false;
        }
        if ((evt instanceof ModelFlipTPEvent))
        {
          ModelFlipTPEvent evt2 = (ModelFlipTPEvent)evt;
          this.retrieving = true;
          
          byte value = (byte)evt2.value;
          this.testPatternComboBox.setSelectedIndex((value & 0xC) >> 2);
          this.testPatternCheckBox.setSelected((value & 0x10) != 0);
          this.lrFlipCheckBox.setSelected((value & 0x2) != 0);
          this.tbFlipCheckBox.setSelected((value & 0x1) != 0);
          
          this.retrieving = false;
        }
        if ((evt instanceof ModelMaintLEDEvent))
        {
          ModelMaintLEDEvent evt2 = (ModelMaintLEDEvent)evt;
          this.retrieving = true;
          
          this.maintenanceLEDCheckBox.setEnabled(evt2.supported);
          this.maintenanceLEDCheckBox.setSelected(evt2.enabled);
          
          this.retrieving = false;
        }
        if ((evt instanceof ModelSerialNumberEvent))
        {
          ModelSerialNumberEvent evt2 = (ModelSerialNumberEvent)evt;
          int serialNumber = evt2.value;
          this.serialNumberLabel.setText(Integer.toString(serialNumber));
          this.serialNumberLabel.setToolTipText("0x" + HexNumber.toHexString(serialNumber));
        }
        if ((evt instanceof ModelSessionRetriesEvent))
        {
          ModelSessionRetriesEvent evt2 = (ModelSessionRetriesEvent)evt;
          this.sessionRetriesLabel.setText(Integer.toString(evt2.value));
        }
        if ((evt instanceof ModelBoardIDEvent))
        {
          ModelBoardIDEvent evt2 = (ModelBoardIDEvent)evt;
          int boardID = evt2.value;
          String text;
          switch (boardID)
          {
          case 0: 
            text = "Rev A";
            break;
          case 3: 
            text = "Rev D";
            break;
          case 4: 
            text = "Rev C";
            break;
          case 8: 
            text = "Rev B";
            break;
          case 1: 
          case 2: 
          case 5: 
          case 6: 
          case 7: 
          default: 
            text = String.format("0x%02X", new Object[] { Integer.valueOf(boardID) });
          }
          this.revisionLabel.setText(text);
        }
        if ((evt instanceof ModelDBTypeEvent))
        {
          ModelDBTypeEvent evt2 = (ModelDBTypeEvent)evt;
          int dbType = evt2.value;
          String text;
          switch (dbType)
          {
          case 0: 
            text = "None";
            break;
          case 1: 
            text = "M118 Rev A";
            break;
          case 2: 
            text = "M118 Rev B";
            break;
          default: 
            text = String.format("0x%02X", new Object[] { Integer.valueOf(dbType) });
          }
          this.daughterboardLabel.setText(text);
        }
        if ((evt instanceof ModelADCValuesEvent))
        {
          ModelADCValuesEvent evt2 = (ModelADCValuesEvent)evt;
          adcUpdate(evt2);
        }
        if ((evt instanceof ModelDisplayEvent))
        {
          ModelDisplayEvent evt2 = (ModelDisplayEvent)evt;
          displayUpdate(evt2);
        }
        if ((evt instanceof ModelRepertoireNameEvent))
        {
          ModelRepertoireNameEvent evt2 = (ModelRepertoireNameEvent)evt;
          this.repertoireNameLabel.setText(evt2.name);
        }
        if ((evt instanceof ModelROCountEvent))
        {
          ModelROCountEvent evt2 = (ModelROCountEvent)evt;
          int count = evt2.value;
          
          List<String> nn = evt2.numberedNames();
          ((UpdateableComboBoxModel)this.roSelectComboBox.getModel()).updateTo(nn);
          ((UpdateableComboBoxModel)this.roDefaultComboBox.getModel()).updateTo(nn);
          
          String text = Integer.toString(count);
          this.roCountLabel.setText(text);
        }
        if ((evt instanceof ModelROSelectedEvent))
        {
          ModelROSelectedEvent evt2 = (ModelROSelectedEvent)evt;
          if (!this.roSelectComboBox.hasFocus()) {
            if (this.roSelectComboBox.getItemCount() > evt2.value) {
              this.roSelectComboBox.setSelectedIndex(evt2.value);
            }
          }
        }
        if ((evt instanceof ModelRODefaultEvent))
        {
          ModelRODefaultEvent evt2 = (ModelRODefaultEvent)evt;
          if (!this.roDefaultComboBox.hasFocus()) {
            if (this.roDefaultComboBox.getItemCount() > evt2.value) {
              this.roDefaultComboBox.setSelectedIndex(evt2.value);
            }
          }
        }
        if ((evt instanceof ModelActivationTypeEvent))
        {
          ModelActivationTypeEvent evt2 = (ModelActivationTypeEvent)evt;
          RunningOrder.Activation activation = evt2.activation;
          String text = activation.toString();
          this.activationTypeLabel.setText(text);
        }
        if ((evt instanceof ModelActivationStateEvent))
        {
          ModelActivationStateEvent evt2 = (ModelActivationStateEvent)evt;
          this.actState = evt2.state;
          this.canActivate = this.actState.acceptsActivate();
          this.canDeactivate = this.actState.acceptsDeactivate();
          String text = this.actState.toString();
          this.activationStateLabel.setText(text);
          this.statusView.updateROEnables();
          getRepertoireEditor().process(evt2);
        }
        if ((evt instanceof ModelFPGARegisterEvent))
        {
          ModelFPGARegisterEvent evt2 = (ModelFPGARegisterEvent)evt;
          fpgaEditorUpdate(evt2);
        }
        if ((evt instanceof ModelTimestampEvent))
        {
          ModelTimestampEvent evt2 = (ModelTimestampEvent)evt;
          this.microCodeTimestampLabel.setText(evt2.timeString);
        }
        if ((evt instanceof ModelRemoteCodeVersionEvent))
        {
          ModelRemoteCodeVersionEvent evt2 = (ModelRemoteCodeVersionEvent)evt;
          this.microCodeVersionLabel.setText(evt2.versionString);
        }
        if ((evt instanceof ModelBitplaneNumberEvent))
        {
          ModelBitplaneNumberEvent evt2 = (ModelBitplaneNumberEvent)evt;
          int percentFull = evt2.value * 100 / Config.MAX_BITPLANES;
          String detail = evt2.value + "/" + Config.MAX_BITPLANES;
          this.imageStoreFullProgressBar.setValue(percentFull);
          this.imageStoreFullProgressBar.setString(detail);
        }
      }
    }
    catch (Throwable t)
    {
      t.printStackTrace();
    }
  }
  
  void clearFields()
  {
    this.statusView.clearFields();
  }
  
  void sendToController(UserEvent ue, boolean awaitResponse)
  {
    if (this.mainController == null) {
      return;
    }
    UserEvent fue = ue;
    this.mainController.submit(fue);
    if (awaitResponse) {
      setCursor(Cursor.getPredefinedCursor(3));
    }
  }
  
  void sendToController(UserEvent ue)
  {
    sendToController(ue, true);
  }
  
  private void setTabVisibility(JTabbedPane tp, String tabName, JPanel p, boolean show)
  {
    if (show) {
      tp.addTab(tabName, p);
    } else {
      tp.remove(p);
    }
  }
  
  private void initComponents()
  {
    this.jLabel3 = new JLabel();
    this.jComboBox2 = new JComboBox();
    this.jScrollPane1 = new JScrollPane();
    this.jList1 = new JList();
    this.flashSelectionButtonGroup = new ButtonGroup();
    this.mainTabbedPane = new JTabbedPane();
    this.statusPanel = new JPanel();
    this.boardStatusPanel = new JPanel();
    this.comPortPanel = new JPanel();
    this.jPanel12 = new JPanel();
    this.comPortComboBox = new JComboBox();
    this.comPortConnectButton = new JButton();
    this.comPortDisconnectButton = new JButton();
    this.rs485_Panel = new JPanel();
    this.rs485_CheckBox = new JCheckBox();
    this.rs485_Spinner = new JSpinner();
    this.usbPanel = new JPanel();
    this.usbConnectButton = new JButton();
    this.usbDisconnectButton = new JButton();
    this.usbComboBox = new JComboBox();
    this.jPanel13 = new JPanel();
    this.jPanel14 = new JPanel();
    this.jPanel15 = new JPanel();
    this.microStatusPanel = new JPanel();
    this.jLabel2 = new JLabel();
    this.jLabel8 = new JLabel();
    this.microCodeVersionLabel = new JLabel();
    this.microCodeTimestampLabel = new JLabel();
    this.fpgaStatusPanel = new JPanel();
    this.jLabel10 = new JLabel();
    this.fpgaCodeVersionLabel = new JLabel();
    this.imageStoreStatusPanel = new JPanel();
    this.jPanel16 = new JPanel();
    this.jLabel41 = new JLabel();
    this.repertoireNameLabel = new JLabel();
    this.jPanel17 = new JPanel();
    this.imageStoreFullProgressBar = new JProgressBar();
    this.jLabel14 = new JLabel();
    this.displayStatusPanel = new JPanel();
    this.jLabel4 = new JLabel();
    this.jLabel19 = new JLabel();
    this.displayConnectedLabel = new JLabel();
    this.displayTemperatureLabel = new JLabel();
    this.boardPanel = new JPanel();
    this.jLabel1 = new JLabel();
    this.revisionLabel = new JLabel();
    this.jLabel24 = new JLabel();
    this.jLabel15 = new JLabel();
    this.daughterboardHeaderLabel = new JLabel();
    this.serialNumberLabel = new JLabel();
    this.supplyInLabel = new JLabel();
    this.daughterboardLabel = new JLabel();
    this.jLabel42 = new JLabel();
    this.systemPowerLabel = new JLabel();
    this.roPanel = new JPanel();
    this.jLabel9 = new JLabel();
    this.roCountLabel = new JLabel();
    this.roSelectButton = new JButton();
    this.jLabel11 = new JLabel();
    this.jLabel6 = new JLabel();
    this.roDefaultButton = new JButton();
    this.jSeparator1 = new JSeparator();
    this.jLabel5 = new JLabel();
    this.activationTypeLabel = new JLabel();
    this.jLabel7 = new JLabel();
    this.activationStateLabel = new JLabel();
    this.activateButton = new JButton();
    this.deactivateButton = new JButton();
    this.roDefaultComboBox = new JComboBox();
    this.roSelectComboBox = new JComboBox();
    this.programPanel = new JPanel();
    this.microCodePanel = new JPanel();
    this.microCodeFileTextField = new JTextField();
    this.microCodeBrowseButton = new JButton();
    this.microCodeProgramButton = new JButton();
    this.microCodePFBButton = new JButton();
    this.fpgaCodePanel = new JPanel();
    this.fpgaCodeFileTextField = new JTextField();
    this.fpgaCodeBrowseButton = new JButton();
    this.fpgaCodeProgramButton = new JButton();
    this.repertoirePanel = new JPanel();
    this.repertoireEditorPanel = null;
    this.settingsPanel = new JPanel();
    this.jPanel2 = new JPanel();
    this.imageFlipPanel = new JPanel();
    this.lrFlipCheckBox = new JCheckBox();
    this.tbFlipCheckBox = new JCheckBox();
    this.testPatternPanel = new JPanel();
    this.testPatternCheckBox = new JCheckBox();
    this.testPatternComboBox = new JComboBox();
    this.ledBrightnessPanel = new JPanel();
    this.jLabel29 = new JLabel();
    this.ledSlider = new JSlider();
    this.ledSpinner = new JSpinner();
    this.settingsStoreButton = new JButton();
    this.maintenanceModePanel = new JPanel();
    this.maintenanceLEDCheckBox = new JCheckBox();
    this.factoryPanel = new JPanel();
    this.powerPanel = new JPanel();
    this.jLabel30 = new JLabel();
    this.jLabel31 = new JLabel();
    this.jLabel32 = new JLabel();
    this.jLabel33 = new JLabel();
    this.jLabel34 = new JLabel();
    this.jLabel35 = new JLabel();
    this.power12VLabel = new JLabel();
    this.power5VLabel = new JLabel();
    this.power3V3Label = new JLabel();
    this.power2V5Label = new JLabel();
    this.power1V8Label = new JLabel();
    this.power1V2Label = new JLabel();
    this.serialNumberPanel = new JPanel();
    this.jLabel43 = new JLabel();
    this.serialNumberTextField = new JTextField();
    this.setSerialNumberButton = new JButton();
    this.jPanel7 = new JPanel();
    this.hostNameTextField = new JTextField();
    this.hostPortSpinner = new JSpinner();
    this.hostActiveCheckBox = new JCheckBox();
    this.jPanel5 = new JPanel();
    this.microCodeEraseButton = new JButton();
    this.microFlashEraseButton = new JButton();
    this.repertoireEraseButton = new JButton();
    this.externalFlashEraseButton = new JButton();
    this.rebootButton = new JButton();
    this.fpgaPanel = new JPanel();
    this.jPanel1 = new JPanel();
    this.jLabel21 = new JLabel();
    this.sessionRetriesLabel = new JLabel();
    this.flashPanel = new JPanel();
    this.jPanel8 = new JPanel();
    this.internalFlashRadioButton = new JRadioButton();
    this.virtualFlashRadioButton = new JRadioButton();
    this.physicalFlashRadioButton = new JRadioButton();
    this.flashPageSpinner = new JSpinner();
    this.flashBaseLabel = new JLabel();
    this.reReadButton = new JButton();
    this.jPanel9 = new JPanel();
    this.jScrollPane2 = new JScrollPane();
    this.flashDataTextArea = new JTextArea();
    this.jPanel10 = new JPanel();
    this.jLabel37 = new JLabel();
    this.jLabel36 = new JLabel();
    this.jLabel38 = new JLabel();
    this.jLabel39 = new JLabel();
    this.eccLabel0 = new JLabel();
    this.eccLabel1 = new JLabel();
    this.eccLabel2 = new JLabel();
    this.eccLabel3 = new JLabel();
    this.aboutPanel = new JPanel();
    this.jLabel16 = new JLabel();
    this.jLabel20 = new JLabel();
    this.jPanel3 = new JPanel();
    this.jPanel4 = new JPanel();
    this.jLabel17 = new JLabel();
    this.jLabel25 = new JLabel();
    this.jPanel6 = new JPanel();
    this.jLabel22 = new JLabel();
    this.jLabel23 = new JLabel();
    this.jLabel26 = new JLabel();
    this.versionsPanel = new JPanel();
    this.metroConLabel = new JLabel();
    this.metroLibLabel = new JLabel();
    this.jLabel12 = new JLabel();
    this.jLabel18 = new JLabel();
    this.jPanel11 = new JPanel();
    this.jLabel13 = new JLabel();
    this.jLabel27 = new JLabel();
    this.jLabel28 = new JLabel();
    this.jLabel40 = new JLabel();
    this.progressPanel = new JPanel();
    this.mainProgressBar = new JProgressBar();
    this.mainResultLabel = new JLabel();
    
    this.jLabel3.setText("jLabel3");
    
    this.jComboBox2.setModel(new DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
    
    this.jList1.setModel(new AbstractListModel()
    {
      String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
      
      public int getSize()
      {
        return this.strings.length;
      }
      
      public Object getElementAt(int i)
      {
        return this.strings[i];
      }
    });
    this.jScrollPane1.setViewportView(this.jList1);
    
    setDefaultCloseOperation(0);
    setTitle("Unconfigured");
    setMinimumSize(new Dimension(800, 800));
    addWindowListener(new WindowAdapter()
    {
      public void windowClosing(WindowEvent evt)
      {
    	  System.exit(1);
        Main_View.this.formWindowClosing(evt);
        
      }
    });
    addComponentListener(new ComponentAdapter()
    {
      public void componentMoved(ComponentEvent evt)
      {
        Main_View.this.formComponentMoved(evt);
      }
    });
    getContentPane().setLayout(new BoxLayout(getContentPane(), 3));
    
    this.mainTabbedPane.setMinimumSize(new Dimension(0, 0));
    this.mainTabbedPane.addMouseListener(new MouseAdapter()
    {
      public void mouseClicked(MouseEvent evt)
      {
        Main_View.this.mainTabbedPaneMouseClicked(evt);
      }
    });
    this.statusPanel.setPreferredSize(new Dimension(889, 767));
    this.statusPanel.addComponentListener(new ComponentAdapter()
    {
      public void componentShown(ComponentEvent evt)
      {
        Main_View.this.statusPanelComponentShown(evt);
      }
    });
    this.statusPanel.setLayout(new GridBagLayout());
    
    this.boardStatusPanel.setBorder(BorderFactory.createTitledBorder("Connection"));
    this.boardStatusPanel.setLayout(new GridBagLayout());
    
    this.comPortPanel.setBorder(BorderFactory.createTitledBorder(false ? "RS-232/485" : "RS-232"));
    this.comPortPanel.setLayout(new GridBagLayout());
    
    this.jPanel12.setLayout(new GridBagLayout());
    
    this.comPortComboBox.setMaximumSize(null);
    this.comPortComboBox.setMinimumSize(null);
    this.comPortComboBox.setPreferredSize(null);
    this.comPortComboBox.addMouseListener(new MouseAdapter()
    {
      public void mouseEntered(MouseEvent evt) throws NullPointerException
      {
        Main_View.this.comPortComboBoxMouseEntered(evt);
      }
    });
    GridBagConstraints gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridwidth = 3;
    gridBagConstraints.fill = 2;
    gridBagConstraints.anchor = 18;
    gridBagConstraints.weightx = 0.1D;
    this.jPanel12.add(this.comPortComboBox, gridBagConstraints);
    
    this.comPortConnectButton.setText("Connect");
    this.comPortConnectButton.setMaximumSize(null);
    this.comPortConnectButton.setMinimumSize(null);
    this.comPortConnectButton.setName("rs232Connect");
    this.comPortConnectButton.setPreferredSize(null);
    this.comPortConnectButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent evt)
      {
        Main_View.this.comPortConnectButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = 18;
    this.jPanel12.add(this.comPortConnectButton, gridBagConstraints);
    
    this.comPortDisconnectButton.setText("Disconnect");
    this.comPortDisconnectButton.setMaximumSize(null);
    this.comPortDisconnectButton.setMinimumSize(null);
    this.comPortDisconnectButton.setName("rs232Disconnect");
    this.comPortDisconnectButton.setPreferredSize(null);
    this.comPortDisconnectButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent evt)
      {
        Main_View.this.comPortDisconnectButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.anchor = 13;
    this.jPanel12.add(this.comPortDisconnectButton, gridBagConstraints);
    
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.fill = 2;
    gridBagConstraints.weightx = 0.1D;
    this.comPortPanel.add(this.jPanel12, gridBagConstraints);
    
    this.rs485_Panel.setLayout(new GridBagLayout());
    
    this.rs485_CheckBox.setText("RS-485");
    this.rs485_CheckBox.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent evt)
      {
        Main_View.this.rs485_CheckBoxActionPerformed(evt);
      }
    });
    this.rs485_Panel.add(this.rs485_CheckBox, new GridBagConstraints());
    
    this.rs485_Spinner.setModel(new SpinnerNumberModel(0, 0, 7, 1));
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = 2;
    this.rs485_Panel.add(this.rs485_Spinner, gridBagConstraints);
    
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.insets = new Insets(0, 10, 0, 0);
    this.comPortPanel.add(this.rs485_Panel, gridBagConstraints);
    
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = 2;
    gridBagConstraints.weightx = 0.1D;
    this.boardStatusPanel.add(this.comPortPanel, gridBagConstraints);
    
    this.usbPanel.setBorder(BorderFactory.createTitledBorder("USB"));
    this.usbPanel.setLayout(new GridBagLayout());
    
    this.usbConnectButton.setText("Connect");
    this.usbConnectButton.setMaximumSize(null);
    this.usbConnectButton.setMinimumSize(null);
    this.usbConnectButton.setName("usbConnect");
    this.usbConnectButton.setPreferredSize(null);
    this.usbConnectButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent evt)
      {
        Main_View.this.usbConnectButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = 18;
    gridBagConstraints.weightx = 1.0D;
    gridBagConstraints.weighty = 1.0D;
    this.usbPanel.add(this.usbConnectButton, gridBagConstraints);
    
    this.usbDisconnectButton.setText("Disconnect");
    this.usbDisconnectButton.setMaximumSize(null);
    this.usbDisconnectButton.setMinimumSize(null);
    this.usbDisconnectButton.setName("usbDisconnect");
    this.usbDisconnectButton.setPreferredSize(null);
    this.usbDisconnectButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent evt)
      {
        Main_View.this.usbDisconnectButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = 12;
    gridBagConstraints.weightx = 1.0D;
    gridBagConstraints.weighty = 1.0D;
    this.usbPanel.add(this.usbDisconnectButton, gridBagConstraints);
    
    this.usbComboBox.setMaximumSize(null);
    this.usbComboBox.setMinimumSize(null);
    this.usbComboBox.setPreferredSize(null);
    this.usbComboBox.addMouseListener(new MouseAdapter()
    {
      public void mouseEntered(MouseEvent evt)
      {
        Main_View.this.usbComboBoxMouseEntered(evt);
      }
    });
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = 2;
    gridBagConstraints.anchor = 18;
    gridBagConstraints.weightx = 1.0D;
    gridBagConstraints.weighty = 1.0D;
    this.usbPanel.add(this.usbComboBox, gridBagConstraints);
    
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 3;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = 2;
    gridBagConstraints.weightx = 0.1D;
    this.boardStatusPanel.add(this.usbPanel, gridBagConstraints);
    
    this.jPanel13.setPreferredSize(new Dimension(40, 40));
    
    GroupLayout jPanel13Layout = new GroupLayout(this.jPanel13);
    this.jPanel13.setLayout(jPanel13Layout);
    jPanel13Layout.setHorizontalGroup(jPanel13Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 110, 32767));
    
    jPanel13Layout.setVerticalGroup(jPanel13Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 40, 32767));
    
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = 2;
    gridBagConstraints.weightx = 0.1D;
    this.boardStatusPanel.add(this.jPanel13, gridBagConstraints);
    
    this.jPanel14.setPreferredSize(new Dimension(40, 40));
    
    GroupLayout jPanel14Layout = new GroupLayout(this.jPanel14);
    this.jPanel14.setLayout(jPanel14Layout);
    jPanel14Layout.setHorizontalGroup(jPanel14Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 110, 32767));
    
    jPanel14Layout.setVerticalGroup(jPanel14Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 40, 32767));
    
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = 2;
    gridBagConstraints.weightx = 0.1D;
    this.boardStatusPanel.add(this.jPanel14, gridBagConstraints);
    
    this.jPanel15.setPreferredSize(new Dimension(40, 40));
    
    GroupLayout jPanel15Layout = new GroupLayout(this.jPanel15);
    this.jPanel15.setLayout(jPanel15Layout);
    jPanel15Layout.setHorizontalGroup(jPanel15Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 110, 32767));
    
    jPanel15Layout.setVerticalGroup(jPanel15Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 40, 32767));
    
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 4;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = 2;
    gridBagConstraints.weightx = 0.1D;
    this.boardStatusPanel.add(this.jPanel15, gridBagConstraints);
    
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = 2;
    gridBagConstraints.anchor = 18;
    this.statusPanel.add(this.boardStatusPanel, gridBagConstraints);
    
    this.microStatusPanel.setBorder(BorderFactory.createTitledBorder("Micro"));
    this.microStatusPanel.setLayout(new GridBagLayout());
    
    this.jLabel2.setFont(new Font("Tahoma", 1, 11));
    this.jLabel2.setText("Micro Code");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.insets = new Insets(0, 24, 0, 24);
    this.microStatusPanel.add(this.jLabel2, gridBagConstraints);
    
    this.jLabel8.setFont(new Font("Tahoma", 1, 11));
    this.jLabel8.setText("Timestamp");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.insets = new Insets(0, 32, 0, 32);
    this.microStatusPanel.add(this.jLabel8, gridBagConstraints);
    
    this.microCodeVersionLabel.setText("-");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    this.microStatusPanel.add(this.microCodeVersionLabel, gridBagConstraints);
    
    this.microCodeTimestampLabel.setText("-");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    this.microStatusPanel.add(this.microCodeTimestampLabel, gridBagConstraints);
    
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = 2;
    gridBagConstraints.anchor = 18;
    gridBagConstraints.weightx = 0.1D;
    this.statusPanel.add(this.microStatusPanel, gridBagConstraints);
    
    this.fpgaStatusPanel.setBorder(BorderFactory.createTitledBorder("FPGA"));
    this.fpgaStatusPanel.setLayout(new GridBagLayout());
    
    this.jLabel10.setFont(new Font("Tahoma", 1, 11));
    this.jLabel10.setText("FPGA Code");
    this.fpgaStatusPanel.add(this.jLabel10, new GridBagConstraints());
    
    this.fpgaCodeVersionLabel.setText("-");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    this.fpgaStatusPanel.add(this.fpgaCodeVersionLabel, gridBagConstraints);
    
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = 2;
    gridBagConstraints.anchor = 18;
    gridBagConstraints.weightx = 0.1D;
    this.statusPanel.add(this.fpgaStatusPanel, gridBagConstraints);
    
    this.imageStoreStatusPanel.setBorder(BorderFactory.createTitledBorder("Repertoire"));
    this.imageStoreStatusPanel.setLayout(new GridBagLayout());
    
    this.jPanel16.setLayout(new GridBagLayout());
    
    this.jLabel41.setText("Name");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = 17;
    gridBagConstraints.insets = new Insets(2, 2, 2, 2);
    this.jPanel16.add(this.jLabel41, gridBagConstraints);
    
    this.repertoireNameLabel.setText("-");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = 17;
    gridBagConstraints.weightx = 0.1D;
    gridBagConstraints.insets = new Insets(2, 13, 2, 0);
    this.jPanel16.add(this.repertoireNameLabel, gridBagConstraints);
    
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.fill = 2;
    gridBagConstraints.insets = new Insets(3, 0, 3, 0);
    this.imageStoreStatusPanel.add(this.jPanel16, gridBagConstraints);
    
    this.jPanel17.setLayout(new GridBagLayout());
    
    this.imageStoreFullProgressBar.setString("");
    this.imageStoreFullProgressBar.setStringPainted(true);
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.ipadx = 617;
    gridBagConstraints.anchor = 18;
    this.jPanel17.add(this.imageStoreFullProgressBar, gridBagConstraints);
    
    this.jLabel14.setText("Images");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.insets = new Insets(0, 0, 0, 8);
    this.jPanel17.add(this.jLabel14, gridBagConstraints);
    
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.insets = new Insets(3, 0, 3, 0);
    this.imageStoreStatusPanel.add(this.jPanel17, gridBagConstraints);
    
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = 2;
    gridBagConstraints.anchor = 18;
    this.statusPanel.add(this.imageStoreStatusPanel, gridBagConstraints);
    
    this.displayStatusPanel.setBorder(BorderFactory.createTitledBorder("Display"));
    this.displayStatusPanel.setLayout(new GridBagLayout());
    
    this.jLabel4.setFont(new Font("Tahoma", 1, 11));
    this.jLabel4.setText("Type");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.insets = new Insets(0, 16, 0, 16);
    this.displayStatusPanel.add(this.jLabel4, gridBagConstraints);
    
    this.jLabel19.setFont(new Font("Tahoma", 1, 11));
    this.jLabel19.setText("Temperature");
    this.displayStatusPanel.add(this.jLabel19, new GridBagConstraints());
    
    this.displayConnectedLabel.setText("-");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    this.displayStatusPanel.add(this.displayConnectedLabel, gridBagConstraints);
    
    this.displayTemperatureLabel.setText("-");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    this.displayStatusPanel.add(this.displayTemperatureLabel, gridBagConstraints);
    
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = 2;
    gridBagConstraints.anchor = 18;
    this.statusPanel.add(this.displayStatusPanel, gridBagConstraints);
    
    this.boardPanel.setBorder(BorderFactory.createTitledBorder("Board"));
    this.boardPanel.setLayout(new GridBagLayout());
    
    this.jLabel1.setFont(new Font("Tahoma", 1, 11));
    this.jLabel1.setText("Revision");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.insets = new Insets(0, 16, 0, 16);
    this.boardPanel.add(this.jLabel1, gridBagConstraints);
    
    this.revisionLabel.setText("-");
    this.revisionLabel.setName("revisionLabel");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    this.boardPanel.add(this.revisionLabel, gridBagConstraints);
    
    this.jLabel24.setFont(new Font("Tahoma", 1, 11));
    this.jLabel24.setText("Serial Number");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.insets = new Insets(0, 16, 0, 16);
    this.boardPanel.add(this.jLabel24, gridBagConstraints);
    
    this.jLabel15.setFont(new Font("Tahoma", 1, 11));
    this.jLabel15.setText("System Power");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 3;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.insets = new Insets(0, 16, 0, 16);
    this.boardPanel.add(this.jLabel15, gridBagConstraints);
    
    this.daughterboardHeaderLabel.setFont(new Font("Tahoma", 1, 11));
    this.daughterboardHeaderLabel.setText("LED Daughterboard");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 4;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.insets = new Insets(0, 16, 0, 16);
    this.boardPanel.add(this.daughterboardHeaderLabel, gridBagConstraints);
    
    this.serialNumberLabel.setText("-");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    this.boardPanel.add(this.serialNumberLabel, gridBagConstraints);
    
    this.supplyInLabel.setText("-");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 1;
    this.boardPanel.add(this.supplyInLabel, gridBagConstraints);
    
    this.daughterboardLabel.setText("-");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 4;
    gridBagConstraints.gridy = 1;
    this.boardPanel.add(this.daughterboardLabel, gridBagConstraints);
    
    this.jLabel42.setFont(new Font("Tahoma", 1, 11));
    this.jLabel42.setText("Supply In");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.insets = new Insets(0, 16, 0, 16);
    this.boardPanel.add(this.jLabel42, gridBagConstraints);
    
    this.systemPowerLabel.setText("-");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 3;
    gridBagConstraints.gridy = 1;
    this.boardPanel.add(this.systemPowerLabel, gridBagConstraints);
    
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = 2;
    gridBagConstraints.anchor = 18;
    this.statusPanel.add(this.boardPanel, gridBagConstraints);
    
    this.roPanel.setBorder(BorderFactory.createTitledBorder("Running Order"));
    this.roPanel.setLayout(new GridBagLayout());
    
    this.jLabel9.setFont(new Font("Tahoma", 1, 11));
    this.jLabel9.setText("Number of ROs");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.insets = new Insets(0, 0, 0, 4);
    this.roPanel.add(this.jLabel9, gridBagConstraints);
    
    this.roCountLabel.setText("-");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = 17;
    gridBagConstraints.insets = new Insets(0, 4, 0, 0);
    this.roPanel.add(this.roCountLabel, gridBagConstraints);
    
    this.roSelectButton.setText("Select");
    this.roSelectButton.setMaximumSize(null);
    this.roSelectButton.setMinimumSize(null);
    this.roSelectButton.setPreferredSize(null);
    this.roSelectButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent evt)
      {
        Main_View.this.roSelectButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 5;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.anchor = 17;
    gridBagConstraints.insets = new Insets(0, 4, 0, 0);
    this.roPanel.add(this.roSelectButton, gridBagConstraints);
    
    this.jLabel11.setFont(new Font("Tahoma", 1, 11));
    this.jLabel11.setText("Default");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.anchor = 13;
    gridBagConstraints.insets = new Insets(0, 0, 0, 4);
    this.roPanel.add(this.jLabel11, gridBagConstraints);
    
    this.jLabel6.setFont(new Font("Tahoma", 1, 11));
    this.jLabel6.setText("Selected");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.anchor = 13;
    gridBagConstraints.insets = new Insets(0, 0, 0, 4);
    this.roPanel.add(this.jLabel6, gridBagConstraints);
    
    this.roDefaultButton.setText("Set Default");
    this.roDefaultButton.setMaximumSize(null);
    this.roDefaultButton.setMinimumSize(null);
    this.roDefaultButton.setPreferredSize(null);
    this.roDefaultButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent evt)
      {
        Main_View.this.roDefaultButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 5;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.anchor = 17;
    gridBagConstraints.insets = new Insets(0, 4, 0, 0);
    this.roPanel.add(this.roDefaultButton, gridBagConstraints);
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.gridwidth = 6;
    gridBagConstraints.fill = 2;
    gridBagConstraints.insets = new Insets(4, 0, 4, 0);
    this.roPanel.add(this.jSeparator1, gridBagConstraints);
    
    this.jLabel5.setFont(new Font("Tahoma", 1, 11));
    this.jLabel5.setText("Type");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 5;
    this.roPanel.add(this.jLabel5, gridBagConstraints);
    
    this.activationTypeLabel.setText("-");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 6;
    this.roPanel.add(this.activationTypeLabel, gridBagConstraints);
    
    this.jLabel7.setFont(new Font("Tahoma", 1, 11));
    this.jLabel7.setText("State");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 5;
    this.roPanel.add(this.jLabel7, gridBagConstraints);
    
    this.activationStateLabel.setText("-");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 6;
    this.roPanel.add(this.activationStateLabel, gridBagConstraints);
    
    this.activateButton.setText("Activate");
    this.activateButton.setMaximumSize(null);
    this.activateButton.setMinimumSize(null);
    this.activateButton.setPreferredSize(null);
    this.activateButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent evt)
      {
        Main_View.this.activateButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 5;
    gridBagConstraints.gridheight = 2;
    gridBagConstraints.insets = new Insets(0, 8, 0, 4);
    this.roPanel.add(this.activateButton, gridBagConstraints);
    
    this.deactivateButton.setText("Deactivate");
    this.deactivateButton.setMaximumSize(null);
    this.deactivateButton.setMinimumSize(null);
    this.deactivateButton.setPreferredSize(null);
    this.deactivateButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent evt)
      {
        Main_View.this.deactivateButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 3;
    gridBagConstraints.gridy = 5;
    gridBagConstraints.gridheight = 2;
    this.roPanel.add(this.deactivateButton, gridBagConstraints);
    
    this.roDefaultComboBox.setModel(new UpdateableComboBoxModel());
    this.roDefaultComboBox.setPreferredSize(new Dimension(300, 20));
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.gridwidth = 4;
    gridBagConstraints.fill = 2;
    this.roPanel.add(this.roDefaultComboBox, gridBagConstraints);
    
    this.roSelectComboBox.setModel(new UpdateableComboBoxModel());
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.gridwidth = 4;
    gridBagConstraints.fill = 2;
    this.roPanel.add(this.roSelectComboBox, gridBagConstraints);
    
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 5;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = 2;
    gridBagConstraints.anchor = 11;
    gridBagConstraints.weighty = 0.1D;
    this.statusPanel.add(this.roPanel, gridBagConstraints);
    
    this.mainTabbedPane.addTab("Status", this.statusPanel);
    
    this.programPanel.addComponentListener(new ComponentAdapter()
    {
      public void componentShown(ComponentEvent evt)
      {
        Main_View.this.programPanelComponentShown(evt);
      }
    });
    this.programPanel.setLayout(new GridBagLayout());
    
    this.microCodePanel.setBorder(BorderFactory.createTitledBorder("Micro Code"));
    this.microCodePanel.setLayout(new GridBagLayout());
    
 //   this.microCodeFileTextField.setText(this.mainModel.programEnvironmentModel.appProps.getProperty("bin3dm"));
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridwidth = 3;
    gridBagConstraints.fill = 1;
    gridBagConstraints.weightx = 0.1D;
    this.microCodePanel.add(this.microCodeFileTextField, gridBagConstraints);
    
    this.microCodeBrowseButton.setText("...");
    this.microCodeBrowseButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent evt)
      {
        Main_View.this.microCodeBrowseButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 4;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = 13;
    this.microCodePanel.add(this.microCodeBrowseButton, gridBagConstraints);
    
    this.microCodeProgramButton.setText("Program Micro");
    this.microCodeProgramButton.setEnabled(false);
    this.microCodeProgramButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent evt)
      {
        Main_View.this.microCodeProgramButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = 17;
    this.microCodePanel.add(this.microCodeProgramButton, gridBagConstraints);
    
    this.microCodePFBButton.setText("Program from blank");
    this.microCodePFBButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent evt)
      {
        Main_View.this.microCodePFBButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    this.microCodePanel.add(this.microCodePFBButton, gridBagConstraints);
    
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = 2;
    gridBagConstraints.anchor = 11;
    gridBagConstraints.weightx = 0.1D;
    this.programPanel.add(this.microCodePanel, gridBagConstraints);
    
    this.fpgaCodePanel.setBorder(BorderFactory.createTitledBorder("FPGA"));
    this.fpgaCodePanel.setLayout(new GridBagLayout());
    
    this.fpgaCodeFileTextField.setText("");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = 2;
    gridBagConstraints.weightx = 0.1D;
    this.fpgaCodePanel.add(this.fpgaCodeFileTextField, gridBagConstraints);
    
    this.fpgaCodeBrowseButton.setText("...");
    this.fpgaCodeBrowseButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent evt)
      {
        Main_View.this.fpgaCodeBrowseButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 0;
    this.fpgaCodePanel.add(this.fpgaCodeBrowseButton, gridBagConstraints);
    
    this.fpgaCodeProgramButton.setText("Program FPGA");
    this.fpgaCodeProgramButton.setEnabled(false);
    this.fpgaCodeProgramButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent evt)
      {
        Main_View.this.fpgaCodeProgramButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = 17;
    this.fpgaCodePanel.add(this.fpgaCodeProgramButton, gridBagConstraints);
    
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = 2;
    gridBagConstraints.anchor = 11;
    gridBagConstraints.weightx = 0.1D;
    gridBagConstraints.weighty = 0.1D;
    this.programPanel.add(this.fpgaCodePanel, gridBagConstraints);
    
    this.mainTabbedPane.addTab("Program", this.programPanel);
    
    this.repertoirePanel.addComponentListener(new ComponentAdapter()
    {
      public void componentShown(ComponentEvent evt)
      {
        Main_View.this.repertoirePanelComponentShown(evt);
      }
    });
   // this.repertoireEditorPanel.setMinimumSize(new Dimension(0, 0));
    
    GroupLayout repertoirePanelLayout = new GroupLayout(this.repertoirePanel);
    this.repertoirePanel.setLayout(repertoirePanelLayout);
 //   repertoirePanelLayout.setHorizontalGroup(repertoirePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.repertoireEditorPanel, -1, 895, 32767));
    
 //   repertoirePanelLayout.setVerticalGroup(repertoirePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.repertoireEditorPanel, -1, 764, 32767));
    
    this.mainTabbedPane.addTab("Repertoire", this.repertoirePanel);
    
    this.settingsPanel.setMinimumSize(new Dimension(0, 0));
    this.settingsPanel.addComponentListener(new ComponentAdapter()
    {
      public void componentShown(ComponentEvent evt)
      {
        Main_View.this.settingsPanelComponentShown(evt);
      }
    });
    this.settingsPanel.setLayout(new GridBagLayout());
    
    this.jPanel2.setMinimumSize(new Dimension(0, 0));
    this.jPanel2.setLayout(new BoxLayout(this.jPanel2, 2));
    
    this.imageFlipPanel.setBorder(BorderFactory.createTitledBorder("Image Flip"));
    this.imageFlipPanel.setPreferredSize(new Dimension(200, 100));
    this.imageFlipPanel.setLayout(new GridBagLayout());
    
    this.lrFlipCheckBox.setText("Left/Right");
    this.lrFlipCheckBox.setEnabled(false);
    this.lrFlipCheckBox.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent evt)
      {
        Main_View.this.lrFlipCheckBoxActionPerformed(evt);
      }
    });
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.anchor = 17;
    this.imageFlipPanel.add(this.lrFlipCheckBox, gridBagConstraints);
    
    this.tbFlipCheckBox.setText("Top/Bottom");
    this.tbFlipCheckBox.setEnabled(false);
    this.tbFlipCheckBox.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent evt)
      {
        Main_View.this.tbFlipCheckBoxActionPerformed(evt);
      }
    });
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = 17;
    this.imageFlipPanel.add(this.tbFlipCheckBox, gridBagConstraints);
    
    this.jPanel2.add(this.imageFlipPanel);
    
    this.testPatternPanel.setBorder(BorderFactory.createTitledBorder("Test Pattern"));
    this.testPatternPanel.setPreferredSize(new Dimension(200, 100));
    this.testPatternPanel.setLayout(new GridBagLayout());
    
    this.testPatternCheckBox.setText("Enable test pattern");
    this.testPatternCheckBox.setEnabled(false);
    this.testPatternCheckBox.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent evt)
      {
        Main_View.this.testPatternCheckBoxActionPerformed(evt);
      }
    });
    this.testPatternPanel.add(this.testPatternCheckBox, new GridBagConstraints());
    
    this.testPatternComboBox.setModel(new DefaultComboBoxModel(new String[] { "Bars 1", "Black", "White", "Bars 2" }));
    this.testPatternComboBox.setEnabled(false);
    this.testPatternComboBox.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent evt)
      {
        Main_View.this.testPatternComboBoxActionPerformed(evt);
      }
    });
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = 2;
    this.testPatternPanel.add(this.testPatternComboBox, gridBagConstraints);
    
    this.jPanel2.add(this.testPatternPanel);
    
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.fill = 2;
    gridBagConstraints.anchor = 11;
    this.settingsPanel.add(this.jPanel2, gridBagConstraints);
    
    this.ledBrightnessPanel.setBorder(BorderFactory.createTitledBorder("LED Brightness"));
    this.ledBrightnessPanel.setMinimumSize(new Dimension(0, 0));
    this.ledBrightnessPanel.setPreferredSize(new Dimension(490, 100));
    
    this.jLabel29.setFont(new Font("Tahoma", 1, 11));
    this.jLabel29.setText("Brightness");
    this.ledBrightnessPanel.add(this.jLabel29);
    
    this.ledSlider.setMaximum(255);
    this.ledSlider.setValue(0);
    this.ledSlider.setEnabled(false);
    this.ledSlider.setPreferredSize(new Dimension(320, 45));
    this.ledSlider.addChangeListener(new ChangeListener()
    {
      public void stateChanged(ChangeEvent evt)
      {
        Main_View.this.ledSliderStateChanged(evt);
      }
    });
    this.ledBrightnessPanel.add(this.ledSlider);
    
    this.ledSpinner.setModel(new SpinnerNumberModel(0, 0, 255, 1));
    this.ledSpinner.setEnabled(false);
    this.ledSpinner.setMaximumSize(null);
    this.ledSpinner.setMinimumSize(null);
    this.ledSpinner.setPreferredSize(null);
    this.ledSpinner.addMouseWheelListener(new MouseWheelListener()
    {
      public void mouseWheelMoved(MouseWheelEvent evt)
      {
        Main_View.this.ledSpinnerMouseWheelMoved(evt);
      }
    });
    this.ledSpinner.addChangeListener(new ChangeListener()
    {
      public void stateChanged(ChangeEvent evt)
      {
        Main_View.this.ledSpinnerStateChanged(evt);
      }
    });
    this.ledBrightnessPanel.add(this.ledSpinner);
    
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = 2;
    this.settingsPanel.add(this.ledBrightnessPanel, gridBagConstraints);
    
    this.settingsStoreButton.setText("Store Settings");
    this.settingsStoreButton.setEnabled(false);
    this.settingsStoreButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent evt)
      {
        Main_View.this.settingsStoreButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    this.settingsPanel.add(this.settingsStoreButton, gridBagConstraints);
    
    this.maintenanceModePanel.setBorder(BorderFactory.createTitledBorder("Maintenance Mode"));
    this.maintenanceModePanel.setLayout(new GridBagLayout());
    
    this.maintenanceLEDCheckBox.setText("Enable LED in maintenance mode");
    this.maintenanceLEDCheckBox.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent evt)
      {
        Main_View.this.maintenanceLEDCheckBoxActionPerformed(evt);
      }
    });
    this.maintenanceModePanel.add(this.maintenanceLEDCheckBox, new GridBagConstraints());
    
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = 2;
    this.settingsPanel.add(this.maintenanceModePanel, gridBagConstraints);
    
    this.mainTabbedPane.addTab("Settings", this.settingsPanel);
    
    this.factoryPanel.addComponentListener(new ComponentAdapter()
    {
      public void componentShown(ComponentEvent evt)
      {
        Main_View.this.factoryPanelComponentShown(evt);
      }
    });
    this.factoryPanel.setLayout(new GridBagLayout());
    
    this.powerPanel.setBorder(BorderFactory.createTitledBorder("Power"));
    this.powerPanel.setMinimumSize(new Dimension(0, 0));
    this.powerPanel.setLayout(new GridBagLayout());
    
    this.jLabel30.setFont(new Font("Tahoma", 1, 11));
    this.jLabel30.setText("12V");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.insets = new Insets(0, 8, 0, 0);
    this.powerPanel.add(this.jLabel30, gridBagConstraints);
    
    this.jLabel31.setFont(new Font("Tahoma", 1, 11));
    this.jLabel31.setText("5V");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.insets = new Insets(0, 8, 0, 0);
    this.powerPanel.add(this.jLabel31, gridBagConstraints);
    
    this.jLabel32.setFont(new Font("Tahoma", 1, 11));
    this.jLabel32.setText("3.3V");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.insets = new Insets(0, 8, 0, 2);
    this.powerPanel.add(this.jLabel32, gridBagConstraints);
    
    this.jLabel33.setFont(new Font("Tahoma", 1, 11));
    this.jLabel33.setText("2.5V");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.insets = new Insets(0, 8, 0, 2);
    this.powerPanel.add(this.jLabel33, gridBagConstraints);
    
    this.jLabel34.setFont(new Font("Tahoma", 1, 11));
    this.jLabel34.setText("1.8V");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.insets = new Insets(0, 8, 0, 2);
    this.powerPanel.add(this.jLabel34, gridBagConstraints);
    
    this.jLabel35.setFont(new Font("Tahoma", 1, 11));
    this.jLabel35.setText("1.2V");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 5;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.insets = new Insets(0, 8, 0, 0);
    this.powerPanel.add(this.jLabel35, gridBagConstraints);
    
    this.power12VLabel.setText("-");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.insets = new Insets(0, 8, 0, 0);
    this.powerPanel.add(this.power12VLabel, gridBagConstraints);
    
    this.power5VLabel.setText("-");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.insets = new Insets(0, 8, 0, 0);
    this.powerPanel.add(this.power5VLabel, gridBagConstraints);
    
    this.power3V3Label.setText("-");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.insets = new Insets(0, 8, 0, 0);
    this.powerPanel.add(this.power3V3Label, gridBagConstraints);
    
    this.power2V5Label.setText("-");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 3;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.insets = new Insets(0, 8, 0, 0);
    this.powerPanel.add(this.power2V5Label, gridBagConstraints);
    
    this.power1V8Label.setText("-");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 4;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.insets = new Insets(0, 8, 0, 0);
    this.powerPanel.add(this.power1V8Label, gridBagConstraints);
    
    this.power1V2Label.setText("-");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 5;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.insets = new Insets(0, 8, 0, 0);
    this.powerPanel.add(this.power1V2Label, gridBagConstraints);
    
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = 2;
    gridBagConstraints.weightx = 0.1D;
    this.factoryPanel.add(this.powerPanel, gridBagConstraints);
    
    this.serialNumberPanel.setBorder(BorderFactory.createTitledBorder("Serial Number"));
    this.serialNumberPanel.setLayout(new GridBagLayout());
    
    this.jLabel43.setFont(new Font("Tahoma", 1, 11));
    this.jLabel43.setText("Serial Number");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.insets = new Insets(0, 4, 0, 4);
    this.serialNumberPanel.add(this.jLabel43, gridBagConstraints);
    
    this.serialNumberTextField.setHorizontalAlignment(4);
    this.serialNumberTextField.setEnabled(false);
    this.serialNumberTextField.setMaximumSize(null);
    this.serialNumberTextField.setMinimumSize(null);
    this.serialNumberTextField.setPreferredSize(null);
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.ipadx = 156;
    this.serialNumberPanel.add(this.serialNumberTextField, gridBagConstraints);
    
    this.setSerialNumberButton.setText("Set");
    this.setSerialNumberButton.setMaximumSize(null);
    this.setSerialNumberButton.setMinimumSize(null);
    this.setSerialNumberButton.setPreferredSize(null);
    this.setSerialNumberButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent evt)
      {
        Main_View.this.setSerialNumberButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.insets = new Insets(0, 4, 0, 0);
    this.serialNumberPanel.add(this.setSerialNumberButton, gridBagConstraints);
    
    this.jPanel7.setBorder(BorderFactory.createTitledBorder("Host"));
    this.jPanel7.setLayout(new GridBagLayout());
    
    this.hostNameTextField.setHorizontalAlignment(4);
    this.hostNameTextField.setText("holly");
    this.hostNameTextField.setMaximumSize(null);
    this.hostNameTextField.setMinimumSize(null);
    this.hostNameTextField.setPreferredSize(null);
    this.hostNameTextField.addKeyListener(new KeyAdapter()
    {
      public void keyTyped(KeyEvent evt)
      {
        Main_View.this.hostNameTextFieldKeyTyped(evt);
      }
    });
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.fill = 2;
    gridBagConstraints.ipadx = 40;
    gridBagConstraints.insets = new Insets(6, 20, 6, 0);
    this.jPanel7.add(this.hostNameTextField, gridBagConstraints);
    
    this.hostPortSpinner.setModel(new SpinnerNumberModel(Integer.valueOf(8365), null, null, Integer.valueOf(1)));
    this.hostPortSpinner.setMaximumSize(null);
    this.hostPortSpinner.setMinimumSize(null);
    this.hostPortSpinner.setPreferredSize(null);
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.fill = 2;
    gridBagConstraints.ipadx = 10;
    gridBagConstraints.insets = new Insets(6, 8, 6, 0);
    this.jPanel7.add(this.hostPortSpinner, gridBagConstraints);
    
    this.hostActiveCheckBox.setText("Active");
    this.hostActiveCheckBox.setEnabled(false);
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.fill = 2;
    gridBagConstraints.insets = new Insets(6, 8, 6, 20);
    this.jPanel7.add(this.hostActiveCheckBox, gridBagConstraints);
    
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 3;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = 13;
    gridBagConstraints.weightx = 0.1D;
    this.serialNumberPanel.add(this.jPanel7, gridBagConstraints);
    
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = 2;
    gridBagConstraints.anchor = 18;
    this.factoryPanel.add(this.serialNumberPanel, gridBagConstraints);
    
    this.jPanel5.setBorder(BorderFactory.createTitledBorder("Erase"));
    this.jPanel5.setLayout(new GridBagLayout());
    
    this.microCodeEraseButton.setText("Erase AP code");
    this.microCodeEraseButton.setToolTipText("This will cause the connection to be lost.");
    this.microCodeEraseButton.setEnabled(false);
    this.microCodeEraseButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent evt)
      {
        Main_View.this.microCodeEraseButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = 18;
    this.jPanel5.add(this.microCodeEraseButton, gridBagConstraints);
    
    this.microFlashEraseButton.setText("Erase all micro flash");
    this.microFlashEraseButton.setEnabled(false);
    this.microFlashEraseButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent evt)
      {
        Main_View.this.microFlashEraseButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = 18;
    this.jPanel5.add(this.microFlashEraseButton, gridBagConstraints);
    
    this.repertoireEraseButton.setText("Erase Repertoire");
    this.repertoireEraseButton.setEnabled(false);
    this.repertoireEraseButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent evt)
      {
        Main_View.this.repertoireEraseButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = 18;
    this.jPanel5.add(this.repertoireEraseButton, gridBagConstraints);
    
    this.externalFlashEraseButton.setText("Erase External Flash");
    this.externalFlashEraseButton.setEnabled(false);
    this.externalFlashEraseButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent evt)
      {
        Main_View.this.externalFlashEraseButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 3;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = 18;
    this.jPanel5.add(this.externalFlashEraseButton, gridBagConstraints);
    
    this.rebootButton.setText("Reboot");
    this.rebootButton.setEnabled(false);
    this.rebootButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent evt)
      {
        Main_View.this.rebootButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 4;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = 18;
    this.jPanel5.add(this.rebootButton, gridBagConstraints);
    
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = 2;
    gridBagConstraints.anchor = 11;
    this.factoryPanel.add(this.jPanel5, gridBagConstraints);
    
    this.fpgaPanel.setBorder(BorderFactory.createTitledBorder("FPGA"));
    this.fpgaPanel.setMinimumSize(new Dimension(0, 0));
    this.fpgaPanel.setLayout(new GridBagLayout());
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.fill = 1;
    gridBagConstraints.anchor = 18;
    gridBagConstraints.weighty = 0.4D;
    this.factoryPanel.add(this.fpgaPanel, gridBagConstraints);
    
    this.jPanel1.setBorder(BorderFactory.createTitledBorder("Comms"));
    this.jPanel1.setMinimumSize(new Dimension(0, 0));
    this.jPanel1.setLayout(new GridBagLayout());
    
    this.jLabel21.setFont(new Font("Tahoma", 1, 11));
    this.jLabel21.setText("Session retries");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.insets = new Insets(0, 0, 0, 8);
    this.jPanel1.add(this.jLabel21, gridBagConstraints);
    
    this.sessionRetriesLabel.setText("-");
    this.jPanel1.add(this.sessionRetriesLabel, new GridBagConstraints());
    
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.fill = 2;
    gridBagConstraints.anchor = 18;
    this.factoryPanel.add(this.jPanel1, gridBagConstraints);
    
    this.mainTabbedPane.addTab("Factory", this.factoryPanel);
    
    this.flashPanel.addComponentListener(new ComponentAdapter()
    {
      public void componentShown(ComponentEvent evt)
      {
        Main_View.this.flashPanelComponentShown(evt);
      }
    });
    this.flashPanel.setLayout(new GridBagLayout());
    
    this.jPanel8.setBorder(BorderFactory.createTitledBorder("Address"));
    this.jPanel8.setLayout(new GridBagLayout());
    
    this.flashSelectionButtonGroup.add(this.internalFlashRadioButton);
    this.internalFlashRadioButton.setText("Internal");
    this.internalFlashRadioButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent evt)
      {
        Main_View.this.internalFlashRadioButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = 18;
    this.jPanel8.add(this.internalFlashRadioButton, gridBagConstraints);
    
    this.flashSelectionButtonGroup.add(this.virtualFlashRadioButton);
    this.virtualFlashRadioButton.setSelected(true);
    this.virtualFlashRadioButton.setText("External (Virtual)");
    this.virtualFlashRadioButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent evt)
      {
        Main_View.this.virtualFlashRadioButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = 18;
    this.jPanel8.add(this.virtualFlashRadioButton, gridBagConstraints);
    
    this.flashSelectionButtonGroup.add(this.physicalFlashRadioButton);
    this.physicalFlashRadioButton.setText("External (Physical)");
    this.physicalFlashRadioButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent evt)
      {
        Main_View.this.physicalFlashRadioButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.anchor = 18;
    this.jPanel8.add(this.physicalFlashRadioButton, gridBagConstraints);
    
    this.flashPageSpinner.setModel(new SpinnerNumberModel(0, 0, 196607, 1));
    this.flashPageSpinner.setMaximumSize(null);
    this.flashPageSpinner.setMinimumSize(null);
    this.flashPageSpinner.setPreferredSize(null);
    JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor)this.flashPageSpinner.getEditor();
    JFormattedTextField tf = editor.getTextField();
    tf.setFormatterFactory(new MyFormatterFactory());
    this.flashPageSpinner.addChangeListener(new ChangeListener()
    {
      public void stateChanged(ChangeEvent evt)
      {
        Main_View.this.flashPageSpinnerStateChanged(evt);
      }
    });
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 4;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = 18;
    this.jPanel8.add(this.flashPageSpinner, gridBagConstraints);
    
    this.flashBaseLabel.setText("-");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 3;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = 18;
    gridBagConstraints.insets = new Insets(0, 4, 0, 4);
    this.jPanel8.add(this.flashBaseLabel, gridBagConstraints);
    
    this.reReadButton.setText("Re-read");
    this.reReadButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent evt)
      {
        Main_View.this.reReadButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 4;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = 18;
    this.jPanel8.add(this.reReadButton, gridBagConstraints);
    
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = 1;
    gridBagConstraints.anchor = 18;
    gridBagConstraints.weightx = 0.1D;
    this.flashPanel.add(this.jPanel8, gridBagConstraints);
    
    this.jPanel9.setBorder(BorderFactory.createTitledBorder("Data"));
    
    this.flashDataTextArea.setColumns(20);
    this.flashDataTextArea.setFont(new Font("Monospaced", 0, 11));
    this.flashDataTextArea.setRows(5);
    this.flashDataTextArea.setDisabledTextColor(new Color(0, 0, 0));
    this.flashDataTextArea.setEnabled(false);
    this.jScrollPane2.setViewportView(this.flashDataTextArea);
    
    GroupLayout jPanel9Layout = new GroupLayout(this.jPanel9);
    this.jPanel9.setLayout(jPanel9Layout);
    jPanel9Layout.setHorizontalGroup(jPanel9Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel9Layout.createSequentialGroup().addContainerGap().addComponent(this.jScrollPane2, -1, 863, 32767).addContainerGap()));
    
    jPanel9Layout.setVerticalGroup(jPanel9Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel9Layout.createSequentialGroup().addComponent(this.jScrollPane2, -1, 483, 32767).addContainerGap()));
    
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = 2;
    gridBagConstraints.anchor = 11;
    gridBagConstraints.weighty = 0.1D;
    this.flashPanel.add(this.jPanel9, gridBagConstraints);
    
    this.jPanel10.setBorder(BorderFactory.createTitledBorder("ECC"));
    this.jPanel10.setLayout(new GridBagLayout());
    
    this.jLabel37.setText("Sector 0:");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = 18;
    gridBagConstraints.insets = new Insets(0, 0, 0, 6);
    this.jPanel10.add(this.jLabel37, gridBagConstraints);
    
    this.jLabel36.setText("Sector 1:");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = 18;
    gridBagConstraints.insets = new Insets(0, 0, 0, 6);
    this.jPanel10.add(this.jLabel36, gridBagConstraints);
    
    this.jLabel38.setText("Sector 2:");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.anchor = 18;
    gridBagConstraints.insets = new Insets(0, 0, 0, 6);
    this.jPanel10.add(this.jLabel38, gridBagConstraints);
    
    this.jLabel39.setText("Sector 3:");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.anchor = 18;
    gridBagConstraints.insets = new Insets(0, 0, 0, 6);
    this.jPanel10.add(this.jLabel39, gridBagConstraints);
    
    this.eccLabel0.setText("-");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = 18;
    this.jPanel10.add(this.eccLabel0, gridBagConstraints);
    
    this.eccLabel1.setText("-");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = 18;
    this.jPanel10.add(this.eccLabel1, gridBagConstraints);
    
    this.eccLabel2.setText("-");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.anchor = 18;
    this.jPanel10.add(this.eccLabel2, gridBagConstraints);
    
    this.eccLabel3.setText("-");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.anchor = 18;
    this.jPanel10.add(this.eccLabel3, gridBagConstraints);
    
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = 1;
    gridBagConstraints.anchor = 18;
    gridBagConstraints.weightx = 0.1D;
    this.flashPanel.add(this.jPanel10, gridBagConstraints);
    
    this.mainTabbedPane.addTab("Flash", this.flashPanel);
    
    this.aboutPanel.setMinimumSize(new Dimension(0, 0));
    this.aboutPanel.addComponentListener(new ComponentAdapter()
    {
      public void componentShown(ComponentEvent evt)
      {
        Main_View.this.aboutPanelComponentShown(evt);
      }
    });
    this.aboutPanel.setLayout(new GridBagLayout());
    
    this.jLabel16.setForeground(new Color(153, 153, 153));
    this.jLabel16.setText("MetroCon V3.1");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.insets = new Insets(0, 0, 16, 0);
    this.aboutPanel.add(this.jLabel16, gridBagConstraints);
    
    this.jLabel20.setIcon(new ImageIcon(getClass().getResource("/com/forthdd/Images/fddLogo.gif")));
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.insets = new Insets(0, 0, 16, 0);
    this.aboutPanel.add(this.jLabel20, gridBagConstraints);
    
    this.jPanel3.setBorder(BorderFactory.createTitledBorder("Copyright"));
    this.jPanel3.setPreferredSize(new Dimension(700, 385));
    this.jPanel3.setLayout(new GridBagLayout());
    
    this.jPanel4.setBorder(BorderFactory.createTitledBorder("Application"));
    this.jPanel4.setPreferredSize(new Dimension(650, 100));
    this.jPanel4.setLayout(new GridBagLayout());
    
    this.jLabel17.setForeground(new Color(153, 153, 153));
    this.jLabel17.setText("MetroCon is 2008-2014 Forth Dimension Displays Limited, and is proprietary software.");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.anchor = 18;
    this.jPanel4.add(this.jLabel17, gridBagConstraints);
    
    this.jLabel25.setForeground(new Color(153, 153, 153));
    this.jLabel25.setText("All rights reserved.");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = 18;
    this.jPanel4.add(this.jLabel25, gridBagConstraints);
    
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = 2;
    gridBagConstraints.anchor = 18;
    this.jPanel3.add(this.jPanel4, gridBagConstraints);
    
    this.jPanel6.setBorder(BorderFactory.createTitledBorder("Libraries"));
    this.jPanel6.setLayout(new GridBagLayout());
    
    this.jLabel22.setForeground(new Color(153, 153, 153));
    this.jLabel22.setText("");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = 18;
    this.jPanel6.add(this.jLabel22, gridBagConstraints);
    
    this.jLabel23.setForeground(new Color(153, 153, 153));
    this.jLabel23.setText("");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = 18;
    this.jPanel6.add(this.jLabel23, gridBagConstraints);
    
    this.jLabel26.setForeground(new Color(153, 153, 153));
    this.jLabel26.setText("All rights reserved.");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.anchor = 18;
    this.jPanel6.add(this.jLabel26, gridBagConstraints);
    
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = 2;
    gridBagConstraints.anchor = 18;
    this.jPanel3.add(this.jPanel6, gridBagConstraints);
    
    this.versionsPanel.setBorder(BorderFactory.createTitledBorder("Versions"));
    this.versionsPanel.setPreferredSize(new Dimension(400, 100));
    this.versionsPanel.setLayout(new GridBagLayout());
    
    this.metroConLabel.setText("-");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = 17;
    gridBagConstraints.insets = new Insets(0, 6, 0, 0);
    this.versionsPanel.add(this.metroConLabel, gridBagConstraints);
    
    this.metroLibLabel.setText("-");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = 17;
    gridBagConstraints.insets = new Insets(0, 6, 8, 0);
    this.versionsPanel.add(this.metroLibLabel, gridBagConstraints);
    
    this.jLabel12.setForeground(new Color(153, 153, 153));
    this.jLabel12.setText("");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = 13;
    gridBagConstraints.insets = new Insets(0, 0, 8, 0);
    this.versionsPanel.add(this.jLabel12, gridBagConstraints);
    
    this.jLabel18.setForeground(new Color(153, 153, 153));
    this.jLabel18.setText("MetroCon");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = 13;
    this.versionsPanel.add(this.jLabel18, gridBagConstraints);
    
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = 2;
    gridBagConstraints.anchor = 18;
    this.jPanel3.add(this.versionsPanel, gridBagConstraints);
    
    this.jPanel11.setBorder(BorderFactory.createTitledBorder("System"));
    this.jPanel11.setLayout(new GridBagLayout());
    
    this.jLabel13.setText(System.getProperty("java.version") + " " + System.getProperty("os.arch"));
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = 17;
    this.jPanel11.add(this.jLabel13, gridBagConstraints);
    
    this.jLabel27.setForeground(new Color(153, 153, 153));
    this.jLabel27.setText("OS");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = 22;
    gridBagConstraints.insets = new Insets(0, 0, 0, 4);
    this.jPanel11.add(this.jLabel27, gridBagConstraints);
    
    this.jLabel28.setForeground(new Color(153, 153, 153));
    this.jLabel28.setText("JRE");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = 22;
    gridBagConstraints.insets = new Insets(0, 0, 0, 4);
    this.jPanel11.add(this.jLabel28, gridBagConstraints);
    
    this.jLabel40.setText(System.getProperty("os.name"));
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.anchor = 21;
    this.jPanel11.add(this.jLabel40, gridBagConstraints);
    
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.fill = 2;
    this.jPanel3.add(this.jPanel11, gridBagConstraints);
    
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    this.aboutPanel.add(this.jPanel3, gridBagConstraints);
    
    //this.mainTabbedPane.addTab("About", this.aboutPanel);
    
    getContentPane().add(this.mainTabbedPane);
    
    this.progressPanel.setBorder(new SoftBevelBorder(0));
    this.progressPanel.setMaximumSize(new Dimension(32767, 30));
    this.progressPanel.setPreferredSize(new Dimension(826, 30));
    this.progressPanel.setLayout(new GridBagLayout());
    
    this.mainProgressBar.setString("");
    this.mainProgressBar.setStringPainted(true);
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = 2;
    gridBagConstraints.anchor = 18;
    gridBagConstraints.weightx = 0.1D;
    gridBagConstraints.insets = new Insets(0, 4, 0, 0);
    this.progressPanel.add(this.mainProgressBar, gridBagConstraints);
    
    this.mainResultLabel.setHorizontalAlignment(4);
    this.mainResultLabel.setText("Ready");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.ipadx = 318;
    gridBagConstraints.insets = new Insets(0, 0, 0, 4);
    this.progressPanel.add(this.mainResultLabel, gridBagConstraints);
    
    getContentPane().add(this.progressPanel);
    
    pack();
  }
  
  private void formWindowClosing(WindowEvent evt)
  {
    if (!this.mainModel.repertoireModel.canCloseAllWithoutAsking())
    {
      int answer = JOptionPane.showConfirmDialog(this, "Repertoires has unsaved changes. Do you want to discard all changes and close anyway?", "Are you sure?", 0, 3);
      if (answer == 1) {
        return;
      }
    }
    this.mainModel.programEnvironmentModel.saveProperties();
    System.exit(0);
  }
  
  private void formComponentMoved(ComponentEvent evt)
  {
    Component c = evt.getComponent();
 //   this.mainModel.programEnvironmentModel.updateLocation(c.getX(), c.getY());
  }
  
  public static final ControlRetrievalEvent statusControlRetrievalEvent = new ControlRetrievalEvent(true, true, true, false, true);
  static final ControlRetrievalEvent programControlRetrievalEvent = new ControlRetrievalEvent(false, false, false, false, false);
  static final ControlRetrievalEvent repertoireControlRetrievalEvent = new ControlRetrievalEvent(false, false, false, false, false);
  static final ControlRetrievalEvent settingsControlRetrievalEvent = new ControlRetrievalEvent(false, false, false, true, false);
  static final ControlRetrievalEvent factoryControlRetrievalEvent = new ControlRetrievalEvent(true, false, true, false, false);
  static final ControlRetrievalEvent flashControlRetrievalEvent = new ControlRetrievalEvent(false, false, false, false, false);
  static final ControlRetrievalEvent aboutControlRetrievalEvent = new ControlRetrievalEvent(false, false, false, false, false);
  
  private void factoryPanelComponentShown(ComponentEvent evt)
  {
    sendToController(factoryControlRetrievalEvent, false);
  }
  
  private void repertoireEraseButtonActionPerformed(ActionEvent evt)
  {
    sendToController(new UserEraseRepertoireEvent());
  }
  
  private void microFlashEraseButtonActionPerformed(ActionEvent evt) {}
  
  private void microCodeEraseButtonActionPerformed(ActionEvent evt)
  {
    sendToController(new UserEraseAPEvent());
  }
  
  private HashServiceClient makeNewHashServiceClient()
  {
    String hostName = this.hostNameTextField.getText();
    int portNumber = ((Integer)this.hostPortSpinner.getValue()).intValue();
    HashServiceClient hashServiceClient = new HashServiceClient(hostName, portNumber);
    return hashServiceClient;
  }
  
  private void updateHashService()
  {
    new SwingWorker()
    {
      protected Object doInBackground()
        throws Exception
      {
        HashServiceClient hashServiceClient = Main_View.this.makeNewHashServiceClient();
        final boolean active = hashServiceClient.isActive();
        
        SwingUtilities.invokeLater(new Runnable()
        {
          public void run()
          {
            Main_View.this.hostActiveCheckBox.setSelected(active);
          }
        });
        return null;
      }
    }.execute();
  }
  
  private void setSerialNumberButtonActionPerformed(ActionEvent evt)
  {
    try
    {
      String serString = this.serialNumberTextField.getText();
      int separator = serString.indexOf("_");
      if (separator == -1)
      {
        HashServiceClient hsc = makeNewHashServiceClient();
        serString = hsc.getHashString(serString);
        separator = serString.indexOf("_");
      }
      String serStr = serString.substring(0, separator);
      String keyStr = serString.substring(separator + 1);
      int ser = Integer.decode(serStr).intValue();
      int key = Integer.decode(keyStr).intValue();
      
      sendToController(new UserSerialNumberEvent(ser, key));
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  private void settingsStoreButtonActionPerformed(ActionEvent evt)
  {
    sendToController(new UserSaveSettingsEvent());
  }
  
  private void ledSpinnerStateChanged(ChangeEvent evt)
  {
    int newValue = ((Integer)this.ledSpinner.getValue()).intValue();
    
    boolean isFocusOwner = this.ledSpinner.getEditor().getComponent(0).isFocusOwner();
    if (isFocusOwner)
    {
      this.ledSlider.setValue(newValue);
      if (!this.retrieving) {
        sendToController(new UserLedSliderEvent(newValue));
      }
    }
  }
  
  private void ledSpinnerMouseWheelMoved(MouseWheelEvent evt)
  {
    SpinnerNumberModel snm = (SpinnerNumberModel)this.ledSpinner.getModel();
    int newValue = ((Integer)this.ledSpinner.getValue()).intValue() - evt.getWheelRotation();
    int max = ((Integer)snm.getMaximum()).intValue();
    int min = ((Integer)snm.getMinimum()).intValue();
    if (newValue > max) {
      newValue = max;
    }
    if (newValue < min) {
      newValue = min;
    }
    this.ledSpinner.setValue(Integer.valueOf(newValue));
    this.ledSlider.setValue(newValue);
  }
  
  private void ledSliderStateChanged(ChangeEvent evt)
  {
    int newValue = this.ledSlider.getValue();
    boolean isFocusOwner = this.ledSlider.isFocusOwner();
    if (isFocusOwner)
    {
      this.ledSpinner.setValue(Integer.valueOf(newValue));
      if (!this.retrieving) {
        sendToController(new UserLedSliderEvent(newValue));
      }
    }
  }
  
  private void fpgaCodeProgramButtonActionPerformed(ActionEvent evt)
  {
    String fileName = this.fpgaCodeFileTextField.getText();
    sendToController(new UserProgramFPGAEvent(fileName));
  }
  
  private void fpgaCodeBrowseButtonActionPerformed(ActionEvent evt)
  {
    File f = chooseFile(AppConfig.FPGA_CODE_FILE_EXTENSION, new SimpleFileFilter("." + AppConfig.FPGA_CODE_FILE_EXTENSION, "FPGA code"), false);
    if (f != null) {
      this.fpgaCodeFileTextField.setText(f.getAbsolutePath());
    }
  }
  
  private void microCodeProgramButtonActionPerformed(ActionEvent evt)
  {
    String microCodeFileName = this.microCodeFileTextField.getText();
    sendToController(new UserProgramMicroEvent(microCodeFileName));
  }
  
  private void microCodeBrowseButtonActionPerformed(ActionEvent evt)
  {
    File f = chooseFile(AppConfig.MICRO_CODE_FILE_EXTENSION, new SimpleFileFilter("." + AppConfig.MICRO_CODE_FILE_EXTENSION, "Micro code"), false);
    if (f != null) {
      this.microCodeFileTextField.setText(f.getAbsolutePath());
    }
  }
  
  private void deactivateButtonActionPerformed(ActionEvent evt)
  {
    sendToController(new UserDeactivateEvent());
  }
  
  private void activateButtonActionPerformed(ActionEvent evt)
  {
    sendToController(new UserActivateEvent());
  }
  
  private void roDefaultButtonActionPerformed(ActionEvent evt)
  {
    try
    {
      int newValue = this.roDefaultComboBox.getSelectedIndex();
      sendToController(new UserSetDefaultROEvent(newValue));
    }
    catch (Throwable t)
    {
      t.printStackTrace();
    }
  }
  
  private void roSelectButtonActionPerformed(ActionEvent evt)
  {
    try
    {
      int newValue = this.roSelectComboBox.getSelectedIndex();
      sendToController(new UserSetSelectedROEvent(newValue));
    }
    catch (Throwable t)
    {
      t.printStackTrace();
    }
  }
  
  private void lrFlipCheckBoxActionPerformed(ActionEvent evt)
  {
    handleFlipTPEvent();
  }
  
  private void tbFlipCheckBoxActionPerformed(ActionEvent evt)
  {
    handleFlipTPEvent();
  }
  
  private void testPatternCheckBoxActionPerformed(ActionEvent evt)
  {
    handleFlipTPEvent();
  }
  
  private void testPatternComboBoxActionPerformed(ActionEvent evt)
  {
    handleFlipTPEvent();
  }
  
  private void mainTabbedPaneMouseClicked(MouseEvent evt)
  {
    if ((evt.isShiftDown()) && (evt.isControlDown()) && ((evt.getModifiers() & 0x4) != 0))
    {
      setTabVisibility(this.mainTabbedPane, "Factory", this.factoryPanel, true);
      setTabVisibility(this.mainTabbedPane, "Flash", this.flashPanel, true);
      pack();
    }
  }
  
  private void microCodePFBButtonActionPerformed(ActionEvent evt)
  {
    String microCodeFileName = this.microCodeFileTextField.getText();
    sendToController(new UserPFBEvent(microCodeFileName));
  }
  
  boolean selectExternalFlash = true;
  boolean selectPhysicalFlash = true;
  int baseAddress = 16777216;
  private static final byte TP_EN = 16;
  private static final byte INV_LR = 2;
  private static final byte INV_TB = 1;
  private static Main_View theMainView;
  private JPanel aboutPanel;
  private JButton activateButton;
  private JLabel activationStateLabel;
  private JLabel activationTypeLabel;
  private JPanel boardPanel;
  private JPanel boardStatusPanel;
  private JComboBox comPortComboBox;
  private JButton comPortConnectButton;
  private JButton comPortDisconnectButton;
  private JPanel comPortPanel;
  private JLabel daughterboardHeaderLabel;
  private JLabel daughterboardLabel;
  private JButton deactivateButton;
  private JLabel displayConnectedLabel;
  private JPanel displayStatusPanel;
  private JLabel displayTemperatureLabel;
  private JLabel eccLabel0;
  private JLabel eccLabel1;
  private JLabel eccLabel2;
  private JLabel eccLabel3;
  private JButton externalFlashEraseButton;
  private JPanel factoryPanel;
  private JLabel flashBaseLabel;
  private JTextArea flashDataTextArea;
  private JSpinner flashPageSpinner;
  private JPanel flashPanel;
  private ButtonGroup flashSelectionButtonGroup;
  private JButton fpgaCodeBrowseButton;
  private JTextField fpgaCodeFileTextField;
  private JPanel fpgaCodePanel;
  private JButton fpgaCodeProgramButton;
  private JLabel fpgaCodeVersionLabel;
  private JPanel fpgaPanel;
  private JPanel fpgaStatusPanel;
  private JCheckBox hostActiveCheckBox;
  private JTextField hostNameTextField;
  private JSpinner hostPortSpinner;
  private JPanel imageFlipPanel;
  private JProgressBar imageStoreFullProgressBar;
  private JPanel imageStoreStatusPanel;
  private JRadioButton internalFlashRadioButton;
  private JComboBox jComboBox2;
  private JLabel jLabel1;
  private JLabel jLabel10;
  private JLabel jLabel11;
  private JLabel jLabel12;
  private JLabel jLabel13;
  private JLabel jLabel14;
  private JLabel jLabel15;
  private JLabel jLabel16;
  private JLabel jLabel17;
  private JLabel jLabel18;
  private JLabel jLabel19;
  private JLabel jLabel2;
  private JLabel jLabel20;
  private JLabel jLabel21;
  private JLabel jLabel22;
  private JLabel jLabel23;
  private JLabel jLabel24;
  private JLabel jLabel25;
  private JLabel jLabel26;
  private JLabel jLabel27;
  private JLabel jLabel28;
  private JLabel jLabel29;
  private JLabel jLabel3;
  private JLabel jLabel30;
  private JLabel jLabel31;
  private JLabel jLabel32;
  private JLabel jLabel33;
  private JLabel jLabel34;
  private JLabel jLabel35;
  private JLabel jLabel36;
  private JLabel jLabel37;
  private JLabel jLabel38;
  private JLabel jLabel39;
  private JLabel jLabel4;
  private JLabel jLabel40;
  private JLabel jLabel41;
  private JLabel jLabel42;
  private JLabel jLabel43;
  private JLabel jLabel5;
  private JLabel jLabel6;
  private JLabel jLabel7;
  private JLabel jLabel8;
  private JLabel jLabel9;
  private JList jList1;
  private JPanel jPanel1;
  private JPanel jPanel10;
  private JPanel jPanel11;
  private JPanel jPanel12;
  private JPanel jPanel13;
  private JPanel jPanel14;
  private JPanel jPanel15;
  private JPanel jPanel16;
  private JPanel jPanel17;
  private JPanel jPanel2;
  private JPanel jPanel3;
  private JPanel jPanel4;
  private JPanel jPanel5;
  private JPanel jPanel6;
  private JPanel jPanel7;
  private JPanel jPanel8;
  private JPanel jPanel9;
  private JScrollPane jScrollPane1;
  private JScrollPane jScrollPane2;
  private JSeparator jSeparator1;
  private JPanel ledBrightnessPanel;
  private JSlider ledSlider;
  private JSpinner ledSpinner;
  private JCheckBox lrFlipCheckBox;
  private JProgressBar mainProgressBar;
  private JLabel mainResultLabel;
  private JTabbedPane mainTabbedPane;
  private JCheckBox maintenanceLEDCheckBox;
  private JPanel maintenanceModePanel;
  private JLabel metroConLabel;
  private JLabel metroLibLabel;
  private JButton microCodeBrowseButton;
  private JButton microCodeEraseButton;
  private JTextField microCodeFileTextField;
  private JButton microCodePFBButton;
  private JPanel microCodePanel;
  private JButton microCodeProgramButton;
  private JLabel microCodeTimestampLabel;
  private JLabel microCodeVersionLabel;
  private JButton microFlashEraseButton;
  private JPanel microStatusPanel;
  private JRadioButton physicalFlashRadioButton;
  private JLabel power12VLabel;
  private JLabel power1V2Label;
  private JLabel power1V8Label;
  private JLabel power2V5Label;
  private JLabel power3V3Label;
  private JLabel power5VLabel;
  private JPanel powerPanel;
  private JPanel programPanel;
  private JPanel progressPanel;
  private JButton reReadButton;
  private JButton rebootButton;
  private JPanel repertoireEditorPanel;
  private JButton repertoireEraseButton;
  private JLabel repertoireNameLabel;
  private JPanel repertoirePanel;
  private JLabel revisionLabel;
  private JLabel roCountLabel;
  private JButton roDefaultButton;
  private JComboBox roDefaultComboBox;
  private JPanel roPanel;
  private JButton roSelectButton;
  private JComboBox roSelectComboBox;
  private JCheckBox rs485_CheckBox;
  private JPanel rs485_Panel;
  private JSpinner rs485_Spinner;
  private JLabel serialNumberLabel;
  private JPanel serialNumberPanel;
  private JTextField serialNumberTextField;
  private JLabel sessionRetriesLabel;
  private JButton setSerialNumberButton;
  private JPanel settingsPanel;
  private JButton settingsStoreButton;
  private JPanel statusPanel;
  private JLabel supplyInLabel;
  private JLabel systemPowerLabel;
  private JCheckBox tbFlipCheckBox;
  private JCheckBox testPatternCheckBox;
  private JComboBox testPatternComboBox;
  private JPanel testPatternPanel;
  private JComboBox usbComboBox;
  private JButton usbConnectButton;
  private JButton usbDisconnectButton;
  private JPanel usbPanel;
  private JPanel versionsPanel;
  private JRadioButton virtualFlashRadioButton;
  
  private void updateFlashSelection()
  {
    this.selectExternalFlash = ((this.virtualFlashRadioButton.isSelected()) || (this.physicalFlashRadioButton.isSelected()));
    this.selectPhysicalFlash = this.physicalFlashRadioButton.isSelected();
    this.baseAddress = (this.selectExternalFlash ? 16777216 : this.selectPhysicalFlash ? 33554432 : 0);
    
    short basePrefix = (short)(this.baseAddress >> 20);
    Formatter formatter = new Formatter();
    this.flashBaseLabel.setText("0x" + formatter.format("%03X", new Object[] { Short.valueOf(basePrefix) }));
    updateFlashPage();
  }
  
  private void updateFlashPage()
  {
    JLabel[] eccLabels = { this.eccLabel0, this.eccLabel1, this.eccLabel2, this.eccLabel3 };
    if (!this.flashPanel.isShowing()) {
      return;
    }
    if (this.mainModel.connectionStatus == MainModel.ConnectionStatus.NO) {
      return;
    }
    int offsetAddress = ((Integer)this.flashPageSpinner.getValue()).intValue();
    int address = this.baseAddress + offsetAddress;
    System.out.println(HexNumber.toHexString(address));
    int pageSize = this.selectExternalFlash ? 2112 : 256;
    this.flashDataTextArea.setText("");
    
    byte[] contents = this.mainModel.repertoireProgrammingModel.getFlashPage(address, true);
    if (false) {
      for (int i = 0; i < pageSize / 2; i++)
      {
        if (i % 16 == 0) {
          this.flashDataTextArea.append(HexNumber.toHexString((short)i) + ": ");
        }
        int wordValue = (contents[(2 * i)] & 0xFF) + ((contents[(2 * i + 1)] & 0xFF) << 8);
        this.flashDataTextArea.append(HexNumber.toHexString((short)wordValue));
        switch (i % 16)
        {
        case 7: 
          this.flashDataTextArea.append("  "); break;
        case 15: 
          this.flashDataTextArea.append("\n"); break;
        default: 
          this.flashDataTextArea.append(" ");
        }
      }
    } else {
      for (int i = 0; i < pageSize; i++)
      {
        if (i % 32 == 0) {
          this.flashDataTextArea.append(HexNumber.toHexString((short)i) + ": ");
        }
        this.flashDataTextArea.append(HexNumber.toHexString(contents[i]));
        switch (i % 32)
        {
        case 15: 
          this.flashDataTextArea.append("  "); break;
        case 31: 
          this.flashDataTextArea.append("\n"); break;
        default: 
          this.flashDataTextArea.append(" ");
        }
      }
    }
    this.flashDataTextArea.setCaretPosition(0);
    if (false)
    {
      int origEcc = 0;
      for (int byteNum = 0; byteNum < 4; byteNum++)
      {
        byte b = contents[(2088 + byteNum)];
        origEcc += ((b & 0xFF) << byteNum * 8);
      }
      int[] contentsW = new int['?'];
      for (int w = 0; w < contentsW.length; w++)
      {
        int bn = w * 2;
        int wordVal = (contents[(bn + 1)] << 8) + (contents[bn] & 0xFF);
        contentsW[w] = wordVal;
      }
      int compEcc = ECC16.computeECC(contentsW);
      eccLabels[0].setText("1");
    }
    else
    {
      for (int sectorNum = 0; sectorNum < 4; sectorNum++)
      {
        byte[] origEcc = new byte[3];
        for (int byteNum = 0; byteNum < 3; byteNum++) {
          origEcc[byteNum] = contents[(2100 + 3 * sectorNum + byteNum)];
        }
        byte[] compEcc ;
        eccLabels[sectorNum].setText("");
      }
    }
  }
  
  private static class MyFormatterFactory
    extends DefaultFormatterFactory
  {
    public JFormattedTextField.AbstractFormatter getDefaultFormatter()
    {
      return new Main_View.HexFormatter();
    }
  }
  
  private static class HexFormatter
    extends DefaultFormatter
  {
    public Object stringToValue(String text)
      throws ParseException
    {
      try
      {
        return Integer.valueOf(text, 16);
      }
      catch (NumberFormatException nfe)
      {
        throw new ParseException(text, 0);
      }
    }
    
    public String valueToString(Object value)
      throws ParseException
    {
      int iValue = ((Integer)value).intValue();
      return HexNumber.toHexString(iValue).substring(3, 8);
    }
  }
  
  private void internalFlashRadioButtonActionPerformed(ActionEvent evt)
  {
    updateFlashSelection();
  }
  
  private void virtualFlashRadioButtonActionPerformed(ActionEvent evt)
  {
    updateFlashSelection();
  }
  
  private void flashPageSpinnerStateChanged(ChangeEvent evt)
  {
    updateFlashPage();
  }
  
  private void physicalFlashRadioButtonActionPerformed(ActionEvent evt)
  {
    updateFlashSelection();
  }
  
  private void reReadButtonActionPerformed(ActionEvent evt)
  {
    updateFlashSelection();
  }
  
  private void externalFlashEraseButtonActionPerformed(ActionEvent evt)
  {
    sendToController(new UserEraseExtFlashEvent());
  }
  
  private void rebootButtonActionPerformed(ActionEvent evt)
  {
    sendToController(new UserRebootEvent());
  }
  
  private void usbComboBoxMouseEntered(MouseEvent evt)throws NullPointerException
  {
//    if (this.mainModel.connectionStatus == MainModel.ConnectionStatus.NO) {
//      this.statusView.updateUsbAvailibility();
//    }
  }
  
  private void usbDisconnectButtonActionPerformed(ActionEvent evt)
  {
    sendToController(new UserDisconnectEvent());
  }
  
  private void usbConnectButtonActionPerformed(ActionEvent evt)
  {
    Object selectedPort = this.usbComboBox.getSelectedItem();
    USBDevice usbd = (USBDevice)selectedPort;
    sendToController(new UserConnectEvent(usbd.portName, UserConnectEvent.LinkType.USB, 0));
  }
  
  private void comPortDisconnectButtonActionPerformed(ActionEvent evt)
  {
    sendToController(new UserDisconnectEvent());
  }
  
  private void comPortConnectButtonActionPerformed(ActionEvent evt)
  {
    Object selectedPort = this.comPortComboBox.getSelectedItem();
    UserConnectEvent.LinkType linkType = this.rs485_CheckBox.isSelected() ? UserConnectEvent.LinkType.RS485 : UserConnectEvent.LinkType.RS232;
    if (this.rs485_CheckBox.isSelected())
    {
      int rs485Address = ((Integer)this.rs485_Spinner.getValue()).intValue();
      sendToController(new UserConnectEvent(selectedPort, linkType, rs485Address));
    }
    else
    {
      sendToController(new UserConnectEvent(selectedPort, linkType, 0));
    }
  }
  
  private void comPortComboBoxMouseEntered(MouseEvent evt) throws NullPointerException
  {
	  
//    this.statusView.updateRS232PortAvailibility();
	
  }
  
  private void hostNameTextFieldKeyTyped(KeyEvent evt)
  {
    updateHashService();
  }
  
  private void maintenanceLEDCheckBoxActionPerformed(ActionEvent evt)
  {
    if (!this.retrieving) {
      sendToController(new UserMaintLedEvent(this.maintenanceLEDCheckBox.isSelected()));
    }
  }
  
  private void statusPanelComponentShown(ComponentEvent evt)
  {
    sendToController(statusControlRetrievalEvent, false);
  }
  
  private void programPanelComponentShown(ComponentEvent evt)
  {
    sendToController(programControlRetrievalEvent, false);
  }
  
  private void repertoirePanelComponentShown(ComponentEvent evt)
  {
    sendToController(repertoireControlRetrievalEvent, false);
  }
  
  private void settingsPanelComponentShown(ComponentEvent evt)
  {
    sendToController(settingsControlRetrievalEvent, false);
  }
  
  private void flashPanelComponentShown(ComponentEvent evt)
  {
    sendToController(flashControlRetrievalEvent, false);
  }
  
  private void aboutPanelComponentShown(ComponentEvent evt)
  {
    sendToController(aboutControlRetrievalEvent, false);
  }
  
  private void rs485_CheckBoxActionPerformed(ActionEvent evt)
  {
    this.statusView.updateConnectionEnables();
  }
  
  private void handleFlipTPEvent()
  {
    byte value = (byte)(this.testPatternComboBox.getSelectedIndex() << 2);
    if (this.testPatternCheckBox.isSelected()) {
      value = (byte)(value | 0x10);
    }
    if (this.lrFlipCheckBox.isSelected()) {
      value = (byte)(value | 0x2);
    }
    if (this.tbFlipCheckBox.isSelected()) {
      value = (byte)(value | 0x1);
    }
    if (!this.retrieving) {
      sendToController(new UserFlipTPEvent(value));
    }
  }
  
  public static Main_View getMainView()
  {
    return theMainView;
  }
  
  public void appendLog(String str)
  {
    if (str != null) {
      System.out.println("LOG: " + str);
    }
  }
  
  File chooseFile(String propKey, FileFilter ff, boolean save)
  {
    Properties appProps = this.mainModel.programEnvironmentModel.appProps;
    String startPath = appProps.getProperty(propKey);
    
    JFileChooser jfc = new JFileChooser(startPath);
    if (ff != null) {
      jfc.setFileFilter(ff);
    }
    if (save)
    {
      if (jfc.showSaveDialog(this) != 0) {
        return null;
      }
    }
    else if (jfc.showOpenDialog(this) != 0) {
      return null;
    }
    File f = jfc.getSelectedFile();
    if (f != null) {
      appProps.setProperty(propKey, f.getAbsolutePath());
    }
    return f;
  }
  
  File[] chooseFiles(String propKey, FileFilter ff, boolean save)
  {
    Properties appProps = this.mainModel.programEnvironmentModel.appProps;
    String startPath = appProps.getProperty(propKey);
    
    JFileChooser jfc = new JFileChooser(startPath);
    jfc.setMultiSelectionEnabled(true);
    if (ff != null) {
      jfc.setFileFilter(ff);
    }
    if (save) {
      jfc.showSaveDialog(this);
    } else {
      jfc.showOpenDialog(this);
    }
    File[] fa = jfc.getSelectedFiles();
    if ((fa == null) || (fa[0] == null)) {
      return null;
    }
    appProps.setProperty(propKey, fa[0].getAbsolutePath());
    return fa;
  }
  
  public static void main(String[] args) throws NullPointerException
  {
    try
    {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch (Exception e)
    {
      System.out.println("Look and feel: " + e);
    }
    
      theMainView = new Main_View();
	  theMainView.setVisible(true);
    
  }
  
  public void run(String args){
	  theMainView = new Main_View();
	  theMainView.setVisible(true);
  }
  
  public class FactoryView
  {
    public FactoryView()
    {
      updateConnectionEnables();
    }
    
    private void updateConnectionEnables()
    {
      boolean available = (Main_View.this.mainModel.connectionStatus != MainModel.ConnectionStatus.NO) && (Main_View.this.mainModel.connectionStatus != MainModel.ConnectionStatus.BUSY);
      
      Main_View.this.microCodeEraseButton.setEnabled(available);
      Main_View.this.repertoireEraseButton.setEnabled(available);
      Main_View.this.externalFlashEraseButton.setEnabled(available);
      Main_View.this.rebootButton.setEnabled(available);
      Main_View.this.serialNumberTextField.setEnabled(available);
      Main_View.this.updateHashService();
//      MainView.this.fred.setEnabled(available);
      Main_View.this.updateFlashSelection();
    }
  }
  
  public final class StatusView
  {
    private String[] oldPortNames;
    private String[] oldUsbDeviceNames;
    
    public StatusView()
    {
      try
      {
        updateConnectionEnables();
        updateRS232PortAvailibility();
        updateUsbAvailibility();
      }
      catch (UnsatisfiedLinkError e)
      {
        Main_View.this.comPortComboBox.addItem("Device driver not found");
        Main_View.this.usbComboBox.addItem("Device driver not found");
      }
    }
    
    void updateRS232PortAvailibility()
    {
      String[] portNames = null;
      try
      {
        portNames = Main_View.this.r4r11cl.devEnumerateComPorts();
      }
      catch (AbstractException ex) {}
      if (portNames == null) {
        portNames = new String[0];
      }
      if (!StringComparator.sameStrings(portNames, this.oldPortNames))
      {
        Main_View.this.comPortComboBox.removeAllItems();
        if (portNames.length > 0)
        {
          for (int i = 0; i < portNames.length; i++) {
            Main_View.this.comPortComboBox.addItem(portNames[i]);
          }
          Main_View.this.comPortComboBox.setToolTipText(null);
        }
        else
        {
          Main_View.this.comPortComboBox.addItem("No serial ports found");
          Main_View.this.comPortComboBox.setToolTipText("Either there are no serial ports, or they are all in use.");
        }
        updateConnectionEnables();
      }
      this.oldPortNames = portNames;
    }
    
    void updateUsbAvailibility()
    {
      String[] metroDeviceNames = null;
      try
      {
        String[] mdnH = Main_View.this.r4r11cl.devEnumerateHID(6636, 769);
        String[] mdnW = Main_View.this.r4r11cl.devEnumerateWinUSB("54ED7AC9-CC23-4165-BE32-79016BAFB950");
        metroDeviceNames = new String[mdnH.length + mdnW.length];
        System.arraycopy(mdnH, 0, metroDeviceNames, 0, mdnH.length);
        System.arraycopy(mdnW, 0, metroDeviceNames, mdnH.length, mdnW.length);
      }
      catch (AbstractException ex) {}
      if (metroDeviceNames == null) {
        metroDeviceNames = new String[0];
      }
      if (!StringComparator.sameStrings(metroDeviceNames, this.oldUsbDeviceNames))
      {
        Main_View.this.usbComboBox.removeAllItems();
        for (int i = 0; i < metroDeviceNames.length; i++)
        {
          String str = metroDeviceNames[i];
          USBDevice wudev = new USBDevice(str);
          Main_View.this.usbComboBox.addItem(wudev);
        }
        updateConnectionEnables();
      }
      this.oldUsbDeviceNames = metroDeviceNames;
    }
    
    public void updateConnectionEnables()
    {
      boolean connected = Main_View.this.mainModel.connectionStatus != MainModel.ConnectionStatus.NO;
      
      Main_View.this.comPortComboBox.setEnabled(!connected);
      Main_View.this.usbComboBox.setEnabled(!connected);
      
      Main_View.this.comPortConnectButton.setEnabled((!connected) && (Main_View.this.comPortComboBox.getItemCount() != 0) && (Main_View.this.comPortComboBox.getSelectedItem() != null));
      Main_View.this.comPortDisconnectButton.setEnabled(MainModel.COM_PORT_CONNECTION_STATUSES.contains(Main_View.this.mainModel.connectionStatus));
      
      Main_View.this.rs485_CheckBox.setEnabled((!connected) && (Main_View.this.comPortComboBox.getItemCount() != 0));
      Main_View.this.rs485_Spinner.setEnabled((!connected) && (Main_View.this.comPortComboBox.getItemCount() != 0) && (Main_View.this.rs485_CheckBox.isSelected()));
      
      Main_View.this.usbConnectButton.setEnabled((!connected) && (Main_View.this.usbComboBox.getItemCount() != 0) && (Main_View.this.usbComboBox.getSelectedItem() != null));
      Main_View.this.usbDisconnectButton.setEnabled(Main_View.this.mainModel.connectionStatus == MainModel.ConnectionStatus.USB);
      
      updateROEnables();
    }
    
    public void updateROEnables()
    {
      boolean connected = Main_View.this.mainModel.connectionStatus != MainModel.ConnectionStatus.NO;
      boolean reloading = Main_View.this.actState.isReloading();
      
      Main_View.this.roSelectComboBox.setEnabled(connected);
      Main_View.this.roDefaultComboBox.setEnabled(connected);
      Main_View.this.roSelectButton.setEnabled(connected & !reloading);
      Main_View.this.roDefaultButton.setEnabled(connected);
      Main_View.this.activateButton.setEnabled(connected & Main_View.this.canActivate & !reloading);
      Main_View.this.deactivateButton.setEnabled(connected & Main_View.this.canDeactivate & !reloading);
    }
    
    public void updateConnectionFields() {}
    
    void clearFields()
    {
      Main_View.this.revisionLabel.setText("-");
      Main_View.this.serialNumberLabel.setText("-");
      Main_View.this.supplyInLabel.setText("-");
      Main_View.this.supplyInLabel.setForeground(Color.BLACK);
      Main_View.this.systemPowerLabel.setText("-");
      Main_View.this.systemPowerLabel.setForeground(Color.BLACK);
      Main_View.this.daughterboardLabel.setText("-");
      Main_View.this.microCodeVersionLabel.setText("-");
      Main_View.this.microCodeTimestampLabel.setText("-");
      Main_View.this.fpgaCodeVersionLabel.setText("-");
      
      Main_View.this.imageStoreFullProgressBar.setValue(0);
      Main_View.this.imageStoreFullProgressBar.setString("");
      
      Main_View.this.displayConnectedLabel.setText("-");
      Main_View.this.displayTemperatureLabel.setText("-");
      
      Main_View.this.repertoireNameLabel.setText("-");
      Main_View.this.roCountLabel.setText("-");
      Main_View.this.roSelectComboBox.removeAllItems();
      Main_View.this.roDefaultComboBox.removeAllItems();
      
      Main_View.this.activationTypeLabel.setText("-");
      Main_View.this.activationStateLabel.setText("-");
    }
  }
  
  public class SettingsView
  {
    public SettingsView()
    {
      updateConnectionEnables();
    }
    
    public void updateConnectionEnables()
    {
      boolean connected = Main_View.this.mainModel.connectionStatus != MainModel.ConnectionStatus.NO;
      Main_View.this.lrFlipCheckBox.setEnabled(connected);
      Main_View.this.tbFlipCheckBox.setEnabled(connected);
      Main_View.this.testPatternCheckBox.setEnabled(connected);
      Main_View.this.testPatternComboBox.setEnabled(connected);
      Main_View.this.ledSlider.setEnabled(connected);
      Main_View.this.ledSpinner.setEnabled(connected);
      Main_View.this.settingsStoreButton.setEnabled(connected);
      Main_View.this.maintenanceLEDCheckBox.setEnabled(connected);
    }
    
    public void updateConnectionFields() {}
    
    void clearFields()
    {
      Main_View.this.lrFlipCheckBox.setSelected(false);
      Main_View.this.tbFlipCheckBox.setSelected(false);
      Main_View.this.testPatternCheckBox.setSelected(false);
      Main_View.this.maintenanceLEDCheckBox.setSelected(false);
    }
  }
  
  public class ProgramView
  {
    public ProgramView()
    {
      updateConnectionEnables();
    }
    
    private void setTFFeedback(JTextField tf, boolean valid)
    {
      tf.setForeground(valid ? Color.black : Color.red);
      tf.setToolTipText(valid ? null : "File not found");
    }
    
    private boolean fileValid(JTextField tf, String extension)
    {
      File f = new File(tf.getText());
      boolean exists = f.exists();
      boolean extensionMatches = f.getName().endsWith(extension);
      return exists & extensionMatches;
    }
    
    public final void updateConnectionEnables()
    {
      boolean connectedAndNotBusy = Main_View.this.mainModel.connectionStatus.connectedAndNotBusy();
      boolean noConnection = Main_View.this.mainModel.connectionStatus == MainModel.ConnectionStatus.NO;
      boolean connectedUSB = Main_View.this.mainModel.connectionStatus == MainModel.ConnectionStatus.USB;
      boolean fpgaCodeValid = fileValid(Main_View.this.fpgaCodeFileTextField, AppConfig.FPGA_CODE_FILE_EXTENSION);
      boolean microCodeValid = fileValid(Main_View.this.microCodeFileTextField, AppConfig.MICRO_CODE_FILE_EXTENSION);
      
      setTFFeedback(Main_View.this.fpgaCodeFileTextField, fpgaCodeValid);
      setTFFeedback(Main_View.this.microCodeFileTextField, microCodeValid);
      if (microCodeValid) {
        Main_View.this.mainModel.programEnvironmentModel.updateMicroCodeFileName(Main_View.this.microCodeFileTextField.getText());
      }
      if (fpgaCodeValid) {
        Main_View.this.mainModel.programEnvironmentModel.updateFPGACodeFileName(Main_View.this.fpgaCodeFileTextField.getText());
      }
      Main_View.this.fpgaCodeProgramButton.setEnabled(connectedAndNotBusy & fpgaCodeValid);
      Main_View.this.microCodeProgramButton.setEnabled(connectedUSB & microCodeValid);
      Main_View.this.microCodeEraseButton.setEnabled(connectedAndNotBusy);
      
      Main_View.this.microCodePFBButton.setEnabled((noConnection) && (microCodeValid) && (Main_View.this.mainModel.microProgrammingModel.isBlankBoardPresent()));
    }
    
    public void doConnectionDisables()
    {
      Main_View.this.fpgaCodeProgramButton.setEnabled(false);
      Main_View.this.microCodeProgramButton.setEnabled(false);
      Main_View.this.microCodeEraseButton.setEnabled(false);
      Main_View.this.microCodePFBButton.setEnabled(false);
    }
    
    void clearFields() {}
  }
}

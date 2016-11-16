import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.plaf.metal.MetalIconFactory;

public class FoilMakerClientView
  extends JFrame
  implements ActionListener
{
  private FoilMakerClient clientController = null;
  private boolean isLeader = false;
  private String currentCardFront = null;
  private String currentCardBack = null;
  private static final int FRAME_WIDTH = 300;
  private static final int FRAME_HEIGHT = 500;
  private static final int STATUS_MSG_PANEL_HEIGHT = 50;
  private static final int MAIN_PANEL_WIDTH = 290;
  private static final int MAIN_PANEL_HEIGHT = 440;
  private static final int PANEL_WIDGET_WIDTH = 270;
  private static final int PANEL_WIDGET_HEIGHT = 40;
  private static final int TEXT_WIDTH = 28;
  private static final int SUGGESTION_LENGTH = 20;
  private JLabel statusMessageLabel = null;
  private JLabel topMessageLabel = null;
  private JPanel mainPanel = null;
  
  private static enum ViewNames
  {
    LOGINVIEW,  CHOOSEGAMEVIEW,  NEWGAMEKEYVIEW,  ENTERGAMEKEYVIEW,  WAITINGFORLEADERVIEW,  ENTERSUGGESTIONVIEW,  WAITINGFOROTHERSVIEW,  PICKOPTIONVIEW,  ROUNDRESULTVIEW;
    
    private ViewNames() {}
  }
  
  private JPanel loginViewPanel = null;
  private JTextField loginPanelUserName = null;
  private JPasswordField loginPanelPassword = null;
  private JButton loginPanelLoginButton = null;
  private JButton loginPanelCreateNewUserButton = null;
  private JPanel chooseGameViewPanel = null;
  private JButton chooseGamePanelNewGameButton = null;
  private JButton chooseGamePanelJoinGameButton = null;
  private JPanel newGameKeyPanel = null;
  private JTextField newGameKeyPanelKey = null;
  private JButton newGameKeyPanelStartGameButton = null;
  DefaultListModel participantListModel = null;
  private JList newGameKeyPanelParticipantListPanel = null;
  private JPanel enterGameKeyPanel = null;
  private JButton enterGameKeyPanelJoinButton = null;
  private JTextField enterGameKeyPanelKey = null;
  private JPanel waitingForLeaderPanel = null;
  private JPanel waitingForOthersPanel = null;
  private JPanel enterSuggestionPanel = null;
  private JTextArea enterSuggestionPanelCardFrontTextArea = null;
  private JTextField enterSuggestionPanelSuggestionArea = null;
  private JButton enterSuggestionPanelSendButton = null;
  private JPanel pickOptionPanel = null;
  private JPanel pickOptionPanelOptionsPanel = null;
  private JRadioButton[] pickOptionPanelRadioButton = null;
  private ButtonGroup pickOptionPanelButtons = null;
  private JButton pickOptionPanelSendButton = null;
  private JPanel roundResultPanel = null;
  private JButton roundResultPanelContinueButton = null;
  private JTextArea roundResultTextArea = null;
  private DefaultListModel resultModel = null;
  private JList resultListPanel = null;
  
  public FoilMakerClientView(final FoilMakerClient controller)
  {
    setupFrame();
    createLoginView();
    createChooseGameView();
    createNewGameKeyView();
    createEnterGameKeyView();
    createWaitingForLeaderView();
    createWaitingForOthersView();
    createParticipantView();
    createRoundOptionsView();
    createRoundResultsView();
    
    showLoginView();
    this.clientController = controller;
    
    setDefaultCloseOperation(0);
    
    addWindowListener(new WindowAdapter()
    {
      public void windowClosing(WindowEvent e)
      {
        int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to quit?", null, 0, 3);
        if (response == 0)
        {
          try
          {
            controller.logout();
          }
          catch (IOException ex)
          {
            ex.printStackTrace();
          }
          System.exit(0);
        }
      }
    });
  }
  
  private void setupFrame()
  {
    Container contentPane = getContentPane();
    contentPane.setLayout(new BorderLayout());
    
    this.mainPanel = new JPanel(new CardLayout());
    this.mainPanel.setSize(new Dimension(290, 440));
    
    this.statusMessageLabel = new JLabel("Welcome!");
    this.topMessageLabel = new JLabel("FoilMaker!");
    this.topMessageLabel.setHorizontalAlignment(0);
    contentPane.add(this.statusMessageLabel, "South");
    contentPane.add(this.topMessageLabel, "North");
    contentPane.add(this.mainPanel, "Center");
    
    setTitle("FoilMaker");
    setSize(new Dimension(300, 500));
    repaint();
  }
  
  private void createLoginView()
  {
    GridBagConstraints constraints = new GridBagConstraints();
    constraints.fill = 2;
    constraints.insets = new Insets(5, 5, 5, 5);
    constraints.weightx = 1.0D;
    
    this.loginViewPanel = new JPanel(new GridBagLayout());
    this.loginViewPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
    this.loginViewPanel.setSize(new Dimension(290, 440));
    
    this.loginPanelUserName = new JTextField("Enter user name", 20);
    this.loginPanelPassword = new JPasswordField("Enter password", 20);
    this.loginPanelLoginButton = new JButton("Login");
    this.loginPanelLoginButton.addActionListener(this);
    this.loginPanelCreateNewUserButton = new JButton("Register");
    this.loginPanelCreateNewUserButton.addActionListener(this);
    
    JPanel tempPanel1 = new JPanel(new GridLayout(2, 2));
    tempPanel1.add(new JLabel("Username"));
    tempPanel1.add(this.loginPanelUserName);
    tempPanel1.add(new JLabel("Password"));
    tempPanel1.add(this.loginPanelPassword);
    
    JPanel tempPanel2 = new JPanel(new GridLayout(0, 2));
    tempPanel2.add(this.loginPanelLoginButton);
    tempPanel2.add(this.loginPanelCreateNewUserButton);
    
    constraints.weighty = 0.25D;
    constraints.gridx = 0;
    constraints.gridy = 0;
    
    this.loginViewPanel.add(new JPanel(), constraints);
    constraints.gridy = 1;
    constraints.weighty = 0.5D;
    this.loginViewPanel.add(tempPanel1, constraints);
    constraints.gridy = 2;
    this.loginViewPanel.add(tempPanel2, constraints);
    constraints.gridy = 3;
    constraints.weighty = 0.25D;
    this.loginViewPanel.add(new JPanel(), constraints);
    
    this.loginViewPanel.setBorder(BorderFactory.createEtchedBorder());
    
    this.mainPanel.add(this.loginViewPanel, FoilMakerClientView.ViewNames.LOGINVIEW.toString());
  }
  
  private void createChooseGameView()
  {
    GridBagConstraints constraints = new GridBagConstraints();
    constraints.fill = 0;
    constraints.insets = new Insets(5, 5, 5, 5);
    constraints.weightx = 1.0D;
    
    this.chooseGameViewPanel = new JPanel(new GridBagLayout());
    this.chooseGameViewPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
    this.chooseGameViewPanel.setSize(new Dimension(290, 440));
    
    this.chooseGamePanelNewGameButton = new JButton("Start New Game");
    this.chooseGamePanelNewGameButton.addActionListener(this);
    
    this.chooseGamePanelJoinGameButton = new JButton("Join a Game");
    this.chooseGamePanelJoinGameButton.addActionListener(this);
    
    constraints.weighty = 0.25D;
    constraints.gridx = 0;
    constraints.gridy = 0;
    
    this.chooseGameViewPanel.add(new JPanel());
    constraints.gridx = 1;
    constraints.weighty = 0.5D;
    this.chooseGameViewPanel.add(this.chooseGamePanelNewGameButton);
    constraints.gridx = 2;
    this.chooseGameViewPanel.add(this.chooseGamePanelJoinGameButton);
    constraints.gridx = 3;
    constraints.weighty = 0.25D;
    this.chooseGameViewPanel.add(new JPanel());
    
    this.chooseGameViewPanel.setBorder(BorderFactory.createEtchedBorder());
    
    this.mainPanel.add(this.chooseGameViewPanel, FoilMakerClientView.ViewNames.CHOOSEGAMEVIEW.toString());
  }
  
  private void createNewGameKeyView()
  {
    GridBagConstraints constraints = new GridBagConstraints();
    constraints.fill = 0;
    constraints.insets = new Insets(5, 5, 5, 5);
    
    this.newGameKeyPanelKey = new JTextField("", 3);
    this.newGameKeyPanelKey.setEditable(false);
    JLabel label = new JLabel("Others should use this key to join your game");
    
    this.participantListModel = new DefaultListModel();
    this.newGameKeyPanelParticipantListPanel = new JList(this.participantListModel);
    this.newGameKeyPanelParticipantListPanel.setBackground(Color.getHSBColor(1.86F, 1.2F, 0.8F));
    this.newGameKeyPanelParticipantListPanel.setCellRenderer(new FoilMakerClientView.FoilMakerIconListRenderer());
    
    this.newGameKeyPanelStartGameButton = new JButton("Start Game");
    this.newGameKeyPanelStartGameButton.addActionListener(this);
    this.newGameKeyPanelStartGameButton.setEnabled(false);
    
    this.newGameKeyPanel = new JPanel(new GridBagLayout());
    this.newGameKeyPanel.setSize(290, 440);
    
    constraints.weighty = 0.0D;
    constraints.gridx = 0;
    constraints.gridy = 0;
    
    this.newGameKeyPanel.add(label, constraints);
    constraints.gridy = 1;
    this.newGameKeyPanel.add(this.newGameKeyPanelKey, constraints);
    
    constraints.gridy = 2;
    
    constraints.fill = 1;
    JScrollPane scrollPane = new JScrollPane(this.newGameKeyPanelParticipantListPanel);
    scrollPane.setBorder(BorderFactory.createTitledBorder("Participants"));
    scrollPane.setMinimumSize(new Dimension(285, 200));
    this.newGameKeyPanel.add(scrollPane, constraints);
    
    constraints.fill = 0;
    constraints.gridy = 3;
    this.newGameKeyPanel.add(this.newGameKeyPanelStartGameButton, constraints);
    
    this.newGameKeyPanel.setBorder(BorderFactory.createEtchedBorder());
    
    this.mainPanel.add(this.newGameKeyPanel, FoilMakerClientView.ViewNames.NEWGAMEKEYVIEW.toString());
  }
  
  private void createEnterGameKeyView()
  {
    GridBagConstraints constraints = new GridBagConstraints();
    constraints.fill = 0;
    constraints.insets = new Insets(5, 5, 5, 5);
    
    this.enterGameKeyPanelKey = new JTextField("", 3);
    JLabel label = new JLabel("Enter the game key to join a game");
    this.enterGameKeyPanelJoinButton = new JButton("Join Game");
    this.enterGameKeyPanelJoinButton.addActionListener(this);
    
    this.enterGameKeyPanel = new JPanel(new GridBagLayout());
    this.enterGameKeyPanel.setSize(290, 440);
    constraints.weighty = 0.0D;
    constraints.gridx = 0;
    constraints.gridy = 0;
    
    this.enterGameKeyPanel.add(label, constraints);
    constraints.gridy = 1;
    this.enterGameKeyPanel.add(this.enterGameKeyPanelKey, constraints);
    constraints.gridy = 2;
    this.enterGameKeyPanel.add(this.enterGameKeyPanelJoinButton, constraints);
    
    this.enterGameKeyPanel.setBorder(BorderFactory.createEtchedBorder());
    
    this.mainPanel.add(this.enterGameKeyPanel, FoilMakerClientView.ViewNames.ENTERGAMEKEYVIEW.toString());
  }
  
  private void createWaitingForLeaderView()
  {
    this.waitingForLeaderPanel = new JPanel(new GridLayout(0, 1));
    JLabel label = new JLabel("Waiting for leader ...");
    label.setHorizontalAlignment(0);
    this.waitingForLeaderPanel.add(label);
    
    this.waitingForLeaderPanel.setBorder(BorderFactory.createEtchedBorder());
    
    this.mainPanel.add(this.waitingForLeaderPanel, FoilMakerClientView.ViewNames.WAITINGFORLEADERVIEW.toString());
  }
  
  private void createWaitingForOthersView()
  {
    this.waitingForOthersPanel = new JPanel(new GridLayout(0, 1));
    JLabel label = new JLabel("Waiting for other players ...");
    label.setHorizontalAlignment(0);
    this.waitingForOthersPanel.add(label);
    
    this.waitingForOthersPanel.setBorder(BorderFactory.createEtchedBorder());
    
    this.mainPanel.add(this.waitingForOthersPanel, FoilMakerClientView.ViewNames.WAITINGFOROTHERSVIEW.toString());
  }
  
  private void createParticipantView()
  {
    GridBagConstraints constraints = new GridBagConstraints();
    constraints.fill = 2;
    constraints.insets = new Insets(5, 5, 5, 5);
    constraints.weightx = 0.8D;
    
    this.enterSuggestionPanelCardFrontTextArea = new JTextArea();
    JScrollPane scrollPaneFront = new JScrollPane(this.enterSuggestionPanelCardFrontTextArea);
    this.enterSuggestionPanelCardFrontTextArea.setEditable(false);
    this.enterSuggestionPanelCardFrontTextArea.setBackground(Color.getHSBColor(1.46F, 1.2F, 0.8F));
    
    this.enterSuggestionPanelSuggestionArea = new JTextField(20);
    
    this.enterSuggestionPanelSendButton = new JButton("Submit Suggestion");
    this.enterSuggestionPanelSendButton.addActionListener(this);
    
    this.enterSuggestionPanel = new JPanel(new GridBagLayout());
    this.enterSuggestionPanel.setSize(290, 440);
    constraints.weighty = 0.0D;
    constraints.gridx = 0;
    constraints.gridy = 0;
    this.enterSuggestionPanel.add(new JLabel("What is the word for"), constraints);
    
    constraints.gridy = 1;
    constraints.weighty = 0.5D;
    constraints.fill = 1;
    
    this.enterSuggestionPanel.add(scrollPaneFront, constraints);
    
    JPanel tempPanel = new JPanel();
    tempPanel.add(this.enterSuggestionPanelSuggestionArea);
    tempPanel.setBorder(BorderFactory.createTitledBorder("Your Suggestion"));
    
    constraints.gridy = 2;
    constraints.weighty = 0.5D;
    
    this.enterSuggestionPanel.add(tempPanel, constraints);
    
    constraints.gridy = 3;
    constraints.weighty = 0.0D;
    constraints.fill = 0;
    this.enterSuggestionPanel.add(this.enterSuggestionPanelSendButton, constraints);
    
    this.enterSuggestionPanel.setBorder(BorderFactory.createEtchedBorder());
    
    this.mainPanel.add(this.enterSuggestionPanel, FoilMakerClientView.ViewNames.ENTERSUGGESTIONVIEW.toString());
  }
  
  private void createRoundOptionsView()
  {
    GridBagConstraints constraints = new GridBagConstraints();
    constraints.fill = 2;
    constraints.insets = new Insets(5, 5, 5, 5);
    
    this.pickOptionPanelOptionsPanel = new JPanel(new GridLayout(0, 1));
    this.pickOptionPanelSendButton = new JButton("Submit Option");
    this.pickOptionPanelSendButton.addActionListener(this);
    
    this.pickOptionPanel = new JPanel(new GridBagLayout());
    constraints.weighty = 0.0D;
    constraints.gridx = 0;
    constraints.gridy = 0;
    this.pickOptionPanel.add(new JLabel("Pick your option below"), constraints);
    
    constraints.gridy = 1;
    constraints.weighty = 1.0D;
    this.pickOptionPanel.add(this.pickOptionPanelOptionsPanel, constraints);
    
    constraints.gridy = 2;
    constraints.weighty = 0.0D;
    this.pickOptionPanel.add(this.pickOptionPanelSendButton, constraints);
    
    this.pickOptionPanel.setBorder(BorderFactory.createEtchedBorder());
    
    this.mainPanel.add(this.pickOptionPanel, FoilMakerClientView.ViewNames.PICKOPTIONVIEW.toString());
  }
  
  private void updateRoundOptionsView(String[] options)
  {
    int numOptions = options.length;
    if (numOptions < 1)
    {
      System.err.println("Unexpected number of tokens for view.createRoundOptionsView");
      return;
    }
    this.pickOptionPanelButtons = new ButtonGroup();
    this.pickOptionPanelOptionsPanel.removeAll();
    this.pickOptionPanelRadioButton = new JRadioButton[numOptions];
    for (int i = 0; i < numOptions; i++)
    {
      this.pickOptionPanelRadioButton[i] = new JRadioButton(options[i]);
      this.pickOptionPanelButtons.add(this.pickOptionPanelRadioButton[i]);
      this.pickOptionPanelOptionsPanel.add(this.pickOptionPanelRadioButton[i]);
    }
    this.pickOptionPanelRadioButton[0].setSelected(true);
  }
  
  public void createRoundResultsView()
  {
    GridBagConstraints constraints = new GridBagConstraints();
    constraints.fill = 2;
    constraints.insets = new Insets(5, 5, 5, 5);
    
    this.roundResultPanelContinueButton = new JButton("Next Round");
    this.roundResultPanelContinueButton.addActionListener(this);
    
    this.roundResultPanel = new JPanel(new GridBagLayout());
    this.roundResultPanel.setSize(290, 440);
    
    this.roundResultTextArea = new JTextArea();
    this.roundResultTextArea.setEditable(false);
    this.roundResultTextArea.setBackground(Color.getHSBColor(1.46F, 1.2F, 0.8F));
    this.roundResultTextArea.setText("");
    
    JScrollPane scrollPaneFront = new JScrollPane(this.roundResultTextArea);
    scrollPaneFront.setBorder(BorderFactory.createTitledBorder("Round Result"));
    scrollPaneFront.setMinimumSize(new Dimension(285, 100));
    
    this.resultModel = new DefaultListModel();
    this.resultListPanel = new JList(this.resultModel);
    this.resultListPanel.setBackground(Color.getHSBColor(1.86F, 1.2F, 0.8F));
    this.resultListPanel.setCellRenderer(new FoilMakerClientView.FoilMakerIconListRenderer());
    
    constraints.weighty = 0.0D;
    constraints.gridx = 0;
    constraints.gridy = 0;
    
    constraints.gridy = 0;
    this.roundResultPanel.add(scrollPaneFront, constraints);
    
    constraints.gridy = 1;
    constraints.fill = 1;
    JScrollPane scrollPane = new JScrollPane(this.resultListPanel);
    scrollPane.setMinimumSize(new Dimension(285, 200));
    scrollPane.setBorder(BorderFactory.createTitledBorder("Overall Results"));
    
    this.roundResultPanel.add(scrollPane, constraints);
    
    constraints.gridy = 2;
    constraints.fill = 0;
    
    this.roundResultPanel.add(this.roundResultPanelContinueButton, constraints);
    
    this.roundResultPanel.setBorder(BorderFactory.createEtchedBorder());
    
    this.mainPanel.add(this.roundResultPanel, FoilMakerClientView.ViewNames.ROUNDRESULTVIEW.toString());
  }
  
  public void updateRoundResultsView(String[] resultTokens)
  {
    for (int i = 0; i < resultTokens.length; i++) {
      if (resultTokens[i].equalsIgnoreCase(this.topMessageLabel.getText()))
      {
        this.roundResultTextArea.setText(resultTokens[(i + 1)]);
        break;
      }
    }
    for (int i = 1; i < resultTokens.length; i++)
    {
      String result = "";
      if ((i - 1) % 5 == 0)
      {
        result = result + resultTokens[i] + " => ";
        result = result + "Score : " + resultTokens[(i + 2)] + " | ";
        result = result + "Fooled : " + resultTokens[(i + 3)] + " player(s) | ";
        result = result + "Fooled by : " + resultTokens[(i + 4)] + " player(s)";
        
        this.resultModel.addElement(result);
      }
    }
  }
  
  private JPanel createResultPanelColumn(String[] displayValues)
  {
    JPanel panel = new JPanel(new GridLayout(0, 1));
    panel.setSize(270, 40);
    if ((displayValues == null) || (displayValues.length < 1)) {
      return panel;
    }
    for (String s : displayValues) {
      panel.add(new JLabel(s));
    }
    return panel;
  }
  
  private void showView(FoilMakerClientView.ViewNames view)
  {
    CardLayout cl = (CardLayout)this.mainPanel.getLayout();
    cl.show(this.mainPanel, view.toString());
    validate();
    repaint();
    setVisible(true);
  }
  
  public void showLoginView()
  {
    showView(FoilMakerClientView.ViewNames.LOGINVIEW);
  }
  
  public void showChooseGameView()
  {
    showView(FoilMakerClientView.ViewNames.CHOOSEGAMEVIEW);
  }
  
  public void showLeaderGameView()
  {
    showView(FoilMakerClientView.ViewNames.NEWGAMEKEYVIEW);
    this.newGameKeyPanelKey.setText(this.clientController.getCurrentGameKey());
    setStatusMsg("Press <Start Game> when all users have joined");
  }
  
  public void showParticipantView(boolean isLeader)
  {
    this.isLeader = isLeader;
    showView(FoilMakerClientView.ViewNames.WAITINGFORLEADERVIEW);
  }
  
  public void addParticipant(String name)
  {
    this.participantListModel.addElement(name);
    this.newGameKeyPanelStartGameButton.setEnabled(true);
  }
  
  public void showCard(String[] cardDetails)
  {
    showView(FoilMakerClientView.ViewNames.ENTERSUGGESTIONVIEW);
    if (cardDetails.length != 2)
    {
      System.err.println("Unexpected number of serverTokens in view.showCard");
      return;
    }
    this.currentCardFront = cardDetails[0];
    this.currentCardBack = cardDetails[1];
    this.enterSuggestionPanelCardFrontTextArea.setText(trimText(this.currentCardFront));
    this.enterSuggestionPanelSuggestionArea.setText("");
    setStatusMsg("Enter your suggestion then click on <Send Response>");
  }
  
  public void showRoundOptionsView(String[] options)
  {
    updateRoundOptionsView(options);
    showView(FoilMakerClientView.ViewNames.PICKOPTIONVIEW);
    setStatusMsg("Pick your option and click <Send> ");
  }
  
  public void showRoundResultView(String[] results)
  {
    updateRoundResultsView(results);
    showView(FoilMakerClientView.ViewNames.ROUNDRESULTVIEW);
    setStatusMsg(" Click <Next Round> when ready ");
  }
  
  public void setStatusMsg(String msg)
  {
    if (msg != null) {
      this.statusMessageLabel.setText(msg);
    }
  }
  
  public void setTopMsg(String msg)
  {
    this.topMessageLabel.setText(msg);
  }
  
  public void actionPerformed(ActionEvent actionEvent)
  {
    Object target = actionEvent.getSource();
    if (target == this.loginPanelLoginButton)
    {
      String userName = this.loginPanelUserName.getText();
      char[] password = this.loginPanelPassword.getPassword();
      if (isValidPassword(password))
      {
        String userPassword = new String(password);
        this.clientController.loginToServer(userName, userPassword);
      }
      else
      {
        setStatusMsg("Invalid  Password");
      }
      return;
    }
    if (target == this.loginPanelCreateNewUserButton)
    {
      String userName = this.loginPanelUserName.getText();
      char[] password = this.loginPanelPassword.getPassword();
      if (!isValidUserName(userName))
      {
        setStatusMsg("Invalid User Name");
        return;
      }
      if (isValidPassword(password))
      {
        String userPassword = new String(password);
        this.clientController.createNewUser(userName, userPassword);
      }
      else
      {
        setStatusMsg("Invalid  Password");
      }
      return;
    }
    if (target == this.chooseGamePanelNewGameButton)
    {
      this.clientController.startNewGame();
      return;
    }
    if (target == this.chooseGamePanelJoinGameButton)
    {
      showView(FoilMakerClientView.ViewNames.ENTERGAMEKEYVIEW);
      return;
    }
    if (target == this.enterGameKeyPanelJoinButton)
    {
      this.clientController.joinGame(this.enterGameKeyPanelKey.getText());
      return;
    }
    if (target == this.newGameKeyPanelStartGameButton)
    {
      this.clientController.allPlayersReady(); return;
    }
    String suggestion;
    if (target == this.enterSuggestionPanelSendButton)
    {
      suggestion = this.enterSuggestionPanelSuggestionArea.getText();
      if (suggestion.length() < 1)
      {
        setStatusMsg("Please enter your suggestion");
        return;
      }
      showView(FoilMakerClientView.ViewNames.WAITINGFOROTHERSVIEW);
      this.clientController.sendSuggestion(suggestion);
      return;
    }
    if (target == this.pickOptionPanelSendButton)
    {
      for (JRadioButton b : this.pickOptionPanelRadioButton) {
        if (b.isSelected())
        {
          this.clientController.sendOption(b.getText());
          showView(FoilMakerClientView.ViewNames.WAITINGFOROTHERSVIEW);
        }
      }
      return;
    }
    if (target == this.roundResultPanelContinueButton) {
      this.clientController.notifyModelToContinue();
    }
  }
  
  public void disableContinueButton()
  {
    this.roundResultPanelContinueButton.setEnabled(false);
  }
  
  private boolean isValidPassword(char[] password)
  {
    String passString = new String(password);
    if ((passString.indexOf("--") >= 0) || 
      (passString.compareTo(passString.trim()) != 0)) {
      return false;
    }
    return true;
  }
  
  private boolean isValidUserName(String userName)
  {
    if ((userName.indexOf("--") >= 0) || 
      (userName.compareTo(userName.trim()) != 0)) {
      return false;
    }
    return true;
  }
  
  private String trimText(String text)
  {
    Scanner scanner = new Scanner(text);
    StringBuilder outputStr = new StringBuilder("");
    
    int currentLineLength = 0;
    while (scanner.hasNext())
    {
      String word = scanner.next();
      if (currentLineLength + word.length() > 28)
      {
        outputStr.append("\n" + word + " ");
        currentLineLength = word.length() + 1;
      }
      else
      {
        outputStr.append(word + " ");
        currentLineLength += word.length() + 1;
      }
    }
    System.err.println(outputStr.toString());
    return outputStr.toString();
  }
  
  public class FoilMakerIconListRenderer
    extends DefaultListCellRenderer
  {
    public FoilMakerIconListRenderer() {}
    
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
    {
      JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
      label.setIcon(MetalIconFactory.getFileChooserDetailViewIcon());
      return label;
    }
  }
}

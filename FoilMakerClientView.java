/*     */ import java.awt.Dimension;
/*     */ import java.awt.GridBagConstraints;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.Insets;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JList;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JTextArea;
/*     */ import javax.swing.JTextField;
/*     */ 
/*     */ public class FoilMakerClientView extends javax.swing.JFrame implements java.awt.event.ActionListener
/*     */ {
/*  16 */   private FoilMakerClient clientController = null;
/*  17 */   private boolean isLeader = false;
/*  18 */   private String currentCardFront = null; private String currentCardBack = null;
/*     */   private static final int FRAME_WIDTH = 300;
/*     */   private static final int FRAME_HEIGHT = 500;
/*     */   private static final int STATUS_MSG_PANEL_HEIGHT = 50;
/*     */   private static final int MAIN_PANEL_WIDTH = 290;
/*     */   private static final int MAIN_PANEL_HEIGHT = 440;
/*     */   private static final int PANEL_WIDGET_WIDTH = 270;
/*     */   private static final int PANEL_WIDGET_HEIGHT = 40;
/*     */   private static final int TEXT_WIDTH = 28;
/*  27 */   private static final int SUGGESTION_LENGTH = 20; private JLabel statusMessageLabel = null;
/*  28 */   private JLabel topMessageLabel = null;
/*  29 */   private JPanel mainPanel = null;
/*  30 */   private static enum ViewNames { LOGINVIEW,  CHOOSEGAMEVIEW,  NEWGAMEKEYVIEW,  ENTERGAMEKEYVIEW,  WAITINGFORLEADERVIEW, 
/*  31 */     ENTERSUGGESTIONVIEW,  WAITINGFOROTHERSVIEW,  PICKOPTIONVIEW,  ROUNDRESULTVIEW;
/*     */     
/*     */     private ViewNames() {} }
/*  34 */   private JPanel loginViewPanel = null;
/*  35 */   private JTextField loginPanelUserName = null;
/*  36 */   private javax.swing.JPasswordField loginPanelPassword = null;
/*  37 */   private JButton loginPanelLoginButton = null; private JButton loginPanelCreateNewUserButton = null;
/*     */   
/*     */ 
/*  40 */   private JPanel chooseGameViewPanel = null;
/*  41 */   private JButton chooseGamePanelNewGameButton = null; private JButton chooseGamePanelJoinGameButton = null;
/*     */   
/*     */ 
/*  44 */   private JPanel newGameKeyPanel = null;
/*  45 */   private JTextField newGameKeyPanelKey = null;
/*  46 */   private JButton newGameKeyPanelStartGameButton = null;
/*  47 */   javax.swing.DefaultListModel participantListModel = null;
/*  48 */   private JList newGameKeyPanelParticipantListPanel = null;
/*     */   
/*     */ 
/*  51 */   private JPanel enterGameKeyPanel = null;
/*  52 */   private JButton enterGameKeyPanelJoinButton = null;
/*  53 */   private JTextField enterGameKeyPanelKey = null;
/*     */   
/*     */ 
/*  56 */   private JPanel waitingForLeaderPanel = null;
/*     */   
/*     */ 
/*  59 */   private JPanel waitingForOthersPanel = null;
/*     */   
/*     */ 
/*  62 */   private JPanel enterSuggestionPanel = null;
/*  63 */   private JTextArea enterSuggestionPanelCardFrontTextArea = null;
/*  64 */   private JTextField enterSuggestionPanelSuggestionArea = null;
/*  65 */   private JButton enterSuggestionPanelSendButton = null;
/*     */   
/*     */ 
/*  68 */   private JPanel pickOptionPanel = null;
/*  69 */   private JPanel pickOptionPanelOptionsPanel = null;
/*  70 */   private javax.swing.JRadioButton[] pickOptionPanelRadioButton = null;
/*  71 */   private javax.swing.ButtonGroup pickOptionPanelButtons = null;
/*  72 */   private JButton pickOptionPanelSendButton = null;
/*     */   
/*     */ 
/*  75 */   private JPanel roundResultPanel = null;
/*  76 */   private JButton roundResultPanelContinueButton = null;
/*  77 */   private JTextArea roundResultTextArea = null;
/*  78 */   private javax.swing.DefaultListModel resultModel = null;
/*  79 */   private JList resultListPanel = null;
/*     */   
/*     */   public FoilMakerClientView(final FoilMakerClient controller)
/*     */   {
/*  83 */     setupFrame();
/*  84 */     createLoginView();
/*  85 */     createChooseGameView();
/*  86 */     createNewGameKeyView();
/*  87 */     createEnterGameKeyView();
/*  88 */     createWaitingForLeaderView();
/*  89 */     createWaitingForOthersView();
/*  90 */     createParticipantView();
/*  91 */     createRoundOptionsView();
/*  92 */     createRoundResultsView();
/*     */     
/*  94 */     showLoginView();
/*  95 */     this.clientController = controller;
/*     */     
/*  97 */     setDefaultCloseOperation(0);
/*     */     
/*  99 */     addWindowListener(new java.awt.event.WindowAdapter()
/*     */     {
/*     */       public void windowClosing(java.awt.event.WindowEvent e) {
/* 102 */         int response = javax.swing.JOptionPane.showConfirmDialog(null, "Are you sure you want to quit?", null, 0, 3);
/*     */         
/*     */ 
/* 105 */         if (response == 0) {
/*     */           try {
/* 107 */             controller.logout();
/*     */           } catch (java.io.IOException ex) {
/* 109 */             ex.printStackTrace();
/*     */           }
/* 111 */           System.exit(0);
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   private void setupFrame() {
/* 118 */     java.awt.Container contentPane = getContentPane();
/* 119 */     contentPane.setLayout(new java.awt.BorderLayout());
/*     */     
/* 121 */     this.mainPanel = new JPanel(new java.awt.CardLayout());
/* 122 */     this.mainPanel.setSize(new Dimension(290, 440));
/*     */     
/* 124 */     this.statusMessageLabel = new JLabel("Welcome!");
/* 125 */     this.topMessageLabel = new JLabel("FoilMaker!");
/* 126 */     this.topMessageLabel.setHorizontalAlignment(0);
/* 127 */     contentPane.add(this.statusMessageLabel, "South");
/* 128 */     contentPane.add(this.topMessageLabel, "North");
/* 129 */     contentPane.add(this.mainPanel, "Center");
/*     */     
/* 131 */     setTitle("FoilMaker");
/* 132 */     setSize(new Dimension(300, 500));
/* 133 */     repaint();
/*     */   }
/*     */   
/*     */   private void createLoginView()
/*     */   {
/* 138 */     GridBagConstraints constraints = new GridBagConstraints();
/* 139 */     constraints.fill = 2;
/* 140 */     constraints.insets = new Insets(5, 5, 5, 5);
/* 141 */     constraints.weightx = 1.0D;
/*     */     
/* 143 */     this.loginViewPanel = new JPanel(new GridBagLayout());
/* 144 */     this.loginViewPanel.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
/* 145 */     this.loginViewPanel.setSize(new Dimension(290, 440));
/*     */     
/* 147 */     this.loginPanelUserName = new JTextField("Enter user name", 20);
/* 148 */     this.loginPanelPassword = new javax.swing.JPasswordField("Enter password", 20);
/* 149 */     this.loginPanelLoginButton = new JButton("Login");
/* 150 */     this.loginPanelLoginButton.addActionListener(this);
/* 151 */     this.loginPanelCreateNewUserButton = new JButton("Register");
/* 152 */     this.loginPanelCreateNewUserButton.addActionListener(this);
/*     */     
/* 154 */     JPanel tempPanel1 = new JPanel(new java.awt.GridLayout(2, 2));
/* 155 */     tempPanel1.add(new JLabel("Username"));
/* 156 */     tempPanel1.add(this.loginPanelUserName);
/* 157 */     tempPanel1.add(new JLabel("Password"));
/* 158 */     tempPanel1.add(this.loginPanelPassword);
/*     */     
/* 160 */     JPanel tempPanel2 = new JPanel(new java.awt.GridLayout(0, 2));
/* 161 */     tempPanel2.add(this.loginPanelLoginButton);
/* 162 */     tempPanel2.add(this.loginPanelCreateNewUserButton);
/*     */     
/*     */ 
/* 165 */     constraints.weighty = 0.25D;
/* 166 */     constraints.gridx = 0;
/* 167 */     constraints.gridy = 0;
/*     */     
/* 169 */     this.loginViewPanel.add(new JPanel(), constraints);
/* 170 */     constraints.gridy = 1;
/* 171 */     constraints.weighty = 0.5D;
/* 172 */     this.loginViewPanel.add(tempPanel1, constraints);
/* 173 */     constraints.gridy = 2;
/* 174 */     this.loginViewPanel.add(tempPanel2, constraints);
/* 175 */     constraints.gridy = 3;
/* 176 */     constraints.weighty = 0.25D;
/* 177 */     this.loginViewPanel.add(new JPanel(), constraints);
/*     */     
/* 179 */     this.loginViewPanel.setBorder(BorderFactory.createEtchedBorder());
/*     */     
/* 181 */     this.mainPanel.add(this.loginViewPanel, FoilMakerClientView.ViewNames.LOGINVIEW.toString());
/*     */   }
/*     */   
/*     */   private void createChooseGameView() {
/* 185 */     GridBagConstraints constraints = new GridBagConstraints();
/* 186 */     constraints.fill = 0;
/* 187 */     constraints.insets = new Insets(5, 5, 5, 5);
/* 188 */     constraints.weightx = 1.0D;
/*     */     
/* 190 */     this.chooseGameViewPanel = new JPanel(new GridBagLayout());
/* 191 */     this.chooseGameViewPanel.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
/* 192 */     this.chooseGameViewPanel.setSize(new Dimension(290, 440));
/*     */     
/*     */ 
/* 195 */     this.chooseGamePanelNewGameButton = new JButton("Start New Game");
/* 196 */     this.chooseGamePanelNewGameButton.addActionListener(this);
/*     */     
/* 198 */     this.chooseGamePanelJoinGameButton = new JButton("Join a Game");
/* 199 */     this.chooseGamePanelJoinGameButton.addActionListener(this);
/*     */     
/* 201 */     constraints.weighty = 0.25D;
/* 202 */     constraints.gridx = 0;
/* 203 */     constraints.gridy = 0;
/*     */     
/* 205 */     this.chooseGameViewPanel.add(new JPanel());
/* 206 */     constraints.gridx = 1;
/* 207 */     constraints.weighty = 0.5D;
/* 208 */     this.chooseGameViewPanel.add(this.chooseGamePanelNewGameButton);
/* 209 */     constraints.gridx = 2;
/* 210 */     this.chooseGameViewPanel.add(this.chooseGamePanelJoinGameButton);
/* 211 */     constraints.gridx = 3;
/* 212 */     constraints.weighty = 0.25D;
/* 213 */     this.chooseGameViewPanel.add(new JPanel());
/*     */     
/* 215 */     this.chooseGameViewPanel.setBorder(BorderFactory.createEtchedBorder());
/*     */     
/* 217 */     this.mainPanel.add(this.chooseGameViewPanel, FoilMakerClientView.ViewNames.CHOOSEGAMEVIEW.toString());
/*     */   }
/*     */   
/*     */   private void createNewGameKeyView() {
/* 221 */     GridBagConstraints constraints = new GridBagConstraints();
/* 222 */     constraints.fill = 0;
/* 223 */     constraints.insets = new Insets(5, 5, 5, 5);
/*     */     
/* 225 */     this.newGameKeyPanelKey = new JTextField("", 3);
/* 226 */     this.newGameKeyPanelKey.setEditable(false);
/* 227 */     JLabel label = new JLabel("Others should use this key to join your game");
/*     */     
/*     */ 
/* 230 */     this.participantListModel = new javax.swing.DefaultListModel();
/* 231 */     this.newGameKeyPanelParticipantListPanel = new JList(this.participantListModel);
/* 232 */     this.newGameKeyPanelParticipantListPanel.setBackground(java.awt.Color.getHSBColor(1.86F, 1.2F, 0.8F));
/* 233 */     this.newGameKeyPanelParticipantListPanel.setCellRenderer(new FoilMakerClientView.FoilMakerIconListRenderer());
/*     */     
/* 235 */     this.newGameKeyPanelStartGameButton = new JButton("Start Game");
/* 236 */     this.newGameKeyPanelStartGameButton.addActionListener(this);
/* 237 */     this.newGameKeyPanelStartGameButton.setEnabled(false);
/*     */     
/* 239 */     this.newGameKeyPanel = new JPanel(new GridBagLayout());
/* 240 */     this.newGameKeyPanel.setSize(290, 440);
/*     */     
/* 242 */     constraints.weighty = 0.0D;
/* 243 */     constraints.gridx = 0;
/* 244 */     constraints.gridy = 0;
/*     */     
/* 246 */     this.newGameKeyPanel.add(label, constraints);
/* 247 */     constraints.gridy = 1;
/* 248 */     this.newGameKeyPanel.add(this.newGameKeyPanelKey, constraints);
/*     */     
/* 250 */     constraints.gridy = 2;
/*     */     
/* 252 */     constraints.fill = 1;
/* 253 */     JScrollPane scrollPane = new JScrollPane(this.newGameKeyPanelParticipantListPanel);
/* 254 */     scrollPane.setBorder(BorderFactory.createTitledBorder("Participants"));
/* 255 */     scrollPane.setMinimumSize(new Dimension(285, 200));
/* 256 */     this.newGameKeyPanel.add(scrollPane, constraints);
/*     */     
/* 258 */     constraints.fill = 0;
/* 259 */     constraints.gridy = 3;
/* 260 */     this.newGameKeyPanel.add(this.newGameKeyPanelStartGameButton, constraints);
/*     */     
/* 262 */     this.newGameKeyPanel.setBorder(BorderFactory.createEtchedBorder());
/*     */     
/* 264 */     this.mainPanel.add(this.newGameKeyPanel, FoilMakerClientView.ViewNames.NEWGAMEKEYVIEW.toString());
/*     */   }
/*     */   
/*     */   private void createEnterGameKeyView() {
/* 268 */     GridBagConstraints constraints = new GridBagConstraints();
/* 269 */     constraints.fill = 0;
/* 270 */     constraints.insets = new Insets(5, 5, 5, 5);
/*     */     
/* 272 */     this.enterGameKeyPanelKey = new JTextField("", 3);
/* 273 */     JLabel label = new JLabel("Enter the game key to join a game");
/* 274 */     this.enterGameKeyPanelJoinButton = new JButton("Join Game");
/* 275 */     this.enterGameKeyPanelJoinButton.addActionListener(this);
/*     */     
/*     */ 
/* 278 */     this.enterGameKeyPanel = new JPanel(new GridBagLayout());
/* 279 */     this.enterGameKeyPanel.setSize(290, 440);
/* 280 */     constraints.weighty = 0.0D;
/* 281 */     constraints.gridx = 0;
/* 282 */     constraints.gridy = 0;
/*     */     
/* 284 */     this.enterGameKeyPanel.add(label, constraints);
/* 285 */     constraints.gridy = 1;
/* 286 */     this.enterGameKeyPanel.add(this.enterGameKeyPanelKey, constraints);
/* 287 */     constraints.gridy = 2;
/* 288 */     this.enterGameKeyPanel.add(this.enterGameKeyPanelJoinButton, constraints);
/*     */     
/* 290 */     this.enterGameKeyPanel.setBorder(BorderFactory.createEtchedBorder());
/*     */     
/* 292 */     this.mainPanel.add(this.enterGameKeyPanel, FoilMakerClientView.ViewNames.ENTERGAMEKEYVIEW.toString());
/*     */   }
/*     */   
/*     */   private void createWaitingForLeaderView() {
/* 296 */     this.waitingForLeaderPanel = new JPanel(new java.awt.GridLayout(0, 1));
/* 297 */     JLabel label = new JLabel("Waiting for leader ...");
/* 298 */     label.setHorizontalAlignment(0);
/* 299 */     this.waitingForLeaderPanel.add(label);
/*     */     
/* 301 */     this.waitingForLeaderPanel.setBorder(BorderFactory.createEtchedBorder());
/*     */     
/* 303 */     this.mainPanel.add(this.waitingForLeaderPanel, FoilMakerClientView.ViewNames.WAITINGFORLEADERVIEW.toString());
/*     */   }
/*     */   
/*     */   private void createWaitingForOthersView() {
/* 307 */     this.waitingForOthersPanel = new JPanel(new java.awt.GridLayout(0, 1));
/* 308 */     JLabel label = new JLabel("Waiting for other players ...");
/* 309 */     label.setHorizontalAlignment(0);
/* 310 */     this.waitingForOthersPanel.add(label);
/*     */     
/* 312 */     this.waitingForOthersPanel.setBorder(BorderFactory.createEtchedBorder());
/*     */     
/* 314 */     this.mainPanel.add(this.waitingForOthersPanel, FoilMakerClientView.ViewNames.WAITINGFOROTHERSVIEW.toString());
/*     */   }
/*     */   
/*     */   private void createParticipantView() {
/* 318 */     GridBagConstraints constraints = new GridBagConstraints();
/* 319 */     constraints.fill = 2;
/* 320 */     constraints.insets = new Insets(5, 5, 5, 5);
/* 321 */     constraints.weightx = 0.8D;
/*     */     
/* 323 */     this.enterSuggestionPanelCardFrontTextArea = new JTextArea();
/* 324 */     JScrollPane scrollPaneFront = new JScrollPane(this.enterSuggestionPanelCardFrontTextArea);
/* 325 */     this.enterSuggestionPanelCardFrontTextArea.setEditable(false);
/* 326 */     this.enterSuggestionPanelCardFrontTextArea.setBackground(java.awt.Color.getHSBColor(1.46F, 1.2F, 0.8F));
/*     */     
/* 328 */     this.enterSuggestionPanelSuggestionArea = new JTextField(20);
/*     */     
/* 330 */     this.enterSuggestionPanelSendButton = new JButton("Submit Suggestion");
/* 331 */     this.enterSuggestionPanelSendButton.addActionListener(this);
/*     */     
/* 333 */     this.enterSuggestionPanel = new JPanel(new GridBagLayout());
/* 334 */     this.enterSuggestionPanel.setSize(290, 440);
/* 335 */     constraints.weighty = 0.0D;
/* 336 */     constraints.gridx = 0;
/* 337 */     constraints.gridy = 0;
/* 338 */     this.enterSuggestionPanel.add(new JLabel("What is the word for"), constraints);
/*     */     
/* 340 */     constraints.gridy = 1;
/* 341 */     constraints.weighty = 0.5D;
/* 342 */     constraints.fill = 1;
/*     */     
/* 344 */     this.enterSuggestionPanel.add(scrollPaneFront, constraints);
/*     */     
/* 346 */     JPanel tempPanel = new JPanel();
/* 347 */     tempPanel.add(this.enterSuggestionPanelSuggestionArea);
/* 348 */     tempPanel.setBorder(BorderFactory.createTitledBorder("Your Suggestion"));
/*     */     
/* 350 */     constraints.gridy = 2;
/* 351 */     constraints.weighty = 0.5D;
/*     */     
/* 353 */     this.enterSuggestionPanel.add(tempPanel, constraints);
/*     */     
/* 355 */     constraints.gridy = 3;
/* 356 */     constraints.weighty = 0.0D;
/* 357 */     constraints.fill = 0;
/* 358 */     this.enterSuggestionPanel.add(this.enterSuggestionPanelSendButton, constraints);
/*     */     
/* 360 */     this.enterSuggestionPanel.setBorder(BorderFactory.createEtchedBorder());
/*     */     
/* 362 */     this.mainPanel.add(this.enterSuggestionPanel, FoilMakerClientView.ViewNames.ENTERSUGGESTIONVIEW.toString());
/*     */   }
/*     */   
/*     */   private void createRoundOptionsView() {
/* 366 */     GridBagConstraints constraints = new GridBagConstraints();
/* 367 */     constraints.fill = 2;
/* 368 */     constraints.insets = new Insets(5, 5, 5, 5);
/*     */     
/* 370 */     this.pickOptionPanelOptionsPanel = new JPanel(new java.awt.GridLayout(0, 1));
/* 371 */     this.pickOptionPanelSendButton = new JButton("Submit Option");
/* 372 */     this.pickOptionPanelSendButton.addActionListener(this);
/*     */     
/* 374 */     this.pickOptionPanel = new JPanel(new GridBagLayout());
/* 375 */     constraints.weighty = 0.0D;
/* 376 */     constraints.gridx = 0;
/* 377 */     constraints.gridy = 0;
/* 378 */     this.pickOptionPanel.add(new JLabel("Pick your option below"), constraints);
/*     */     
/* 380 */     constraints.gridy = 1;
/* 381 */     constraints.weighty = 1.0D;
/* 382 */     this.pickOptionPanel.add(this.pickOptionPanelOptionsPanel, constraints);
/*     */     
/* 384 */     constraints.gridy = 2;
/* 385 */     constraints.weighty = 0.0D;
/* 386 */     this.pickOptionPanel.add(this.pickOptionPanelSendButton, constraints);
/*     */     
/* 388 */     this.pickOptionPanel.setBorder(BorderFactory.createEtchedBorder());
/*     */     
/* 390 */     this.mainPanel.add(this.pickOptionPanel, FoilMakerClientView.ViewNames.PICKOPTIONVIEW.toString());
/*     */   }
/*     */   
/*     */   private void updateRoundOptionsView(String[] options) {
/* 394 */     int numOptions = options.length;
/*     */     
/* 396 */     if (numOptions < 1) {
/* 397 */       System.err.println("Unexpected number of tokens for view.createRoundOptionsView");
/* 398 */       return;
/*     */     }
/*     */     
/* 401 */     this.pickOptionPanelButtons = new javax.swing.ButtonGroup();
/* 402 */     this.pickOptionPanelOptionsPanel.removeAll();
/* 403 */     this.pickOptionPanelRadioButton = new javax.swing.JRadioButton[numOptions];
/* 404 */     for (int i = 0; i < numOptions; i++) {
/* 405 */       this.pickOptionPanelRadioButton[i] = new javax.swing.JRadioButton(options[i]);
/* 406 */       this.pickOptionPanelButtons.add(this.pickOptionPanelRadioButton[i]);
/* 407 */       this.pickOptionPanelOptionsPanel.add(this.pickOptionPanelRadioButton[i]);
/*     */     }
/*     */     
/* 410 */     this.pickOptionPanelRadioButton[0].setSelected(true);
/*     */   }
/*     */   
/*     */   public void createRoundResultsView() {
/* 414 */     GridBagConstraints constraints = new GridBagConstraints();
/* 415 */     constraints.fill = 2;
/* 416 */     constraints.insets = new Insets(5, 5, 5, 5);
/*     */     
/*     */ 
/*     */ 
/* 420 */     this.roundResultPanelContinueButton = new JButton("Next Round");
/* 421 */     this.roundResultPanelContinueButton.addActionListener(this);
/*     */     
/* 423 */     this.roundResultPanel = new JPanel(new GridBagLayout());
/* 424 */     this.roundResultPanel.setSize(290, 440);
/*     */     
/* 426 */     this.roundResultTextArea = new JTextArea();
/* 427 */     this.roundResultTextArea.setEditable(false);
/* 428 */     this.roundResultTextArea.setBackground(java.awt.Color.getHSBColor(1.46F, 1.2F, 0.8F));
/* 429 */     this.roundResultTextArea.setText("");
/*     */     
/* 431 */     JScrollPane scrollPaneFront = new JScrollPane(this.roundResultTextArea);
/* 432 */     scrollPaneFront.setBorder(BorderFactory.createTitledBorder("Round Result"));
/* 433 */     scrollPaneFront.setMinimumSize(new Dimension(285, 100));
/*     */     
/* 435 */     this.resultModel = new javax.swing.DefaultListModel();
/* 436 */     this.resultListPanel = new JList(this.resultModel);
/* 437 */     this.resultListPanel.setBackground(java.awt.Color.getHSBColor(1.86F, 1.2F, 0.8F));
/* 438 */     this.resultListPanel.setCellRenderer(new FoilMakerClientView.FoilMakerIconListRenderer());
/*     */     
/* 440 */     constraints.weighty = 0.0D;
/* 441 */     constraints.gridx = 0;
/* 442 */     constraints.gridy = 0;
/*     */     
/* 444 */     constraints.gridy = 0;
/* 445 */     this.roundResultPanel.add(scrollPaneFront, constraints);
/*     */     
/* 447 */     constraints.gridy = 1;
/* 448 */     constraints.fill = 1;
/* 449 */     JScrollPane scrollPane = new JScrollPane(this.resultListPanel);
/* 450 */     scrollPane.setMinimumSize(new Dimension(285, 200));
/* 451 */     scrollPane.setBorder(BorderFactory.createTitledBorder("Overall Results"));
/*     */     
/* 453 */     this.roundResultPanel.add(scrollPane, constraints);
/*     */     
/* 455 */     constraints.gridy = 2;
/* 456 */     constraints.fill = 0;
/*     */     
/* 458 */     this.roundResultPanel.add(this.roundResultPanelContinueButton, constraints);
/*     */     
/* 460 */     this.roundResultPanel.setBorder(BorderFactory.createEtchedBorder());
/*     */     
/* 462 */     this.mainPanel.add(this.roundResultPanel, FoilMakerClientView.ViewNames.ROUNDRESULTVIEW.toString());
/*     */   }
/*     */   
/*     */   public void updateRoundResultsView(String[] resultTokens) {
/* 466 */     for (int i = 0; i < resultTokens.length; i++) {
/* 467 */       if (resultTokens[i].equalsIgnoreCase(this.topMessageLabel.getText())) {
/* 468 */         this.roundResultTextArea.setText(resultTokens[(i + 1)]);
/* 469 */         break;
/*     */       }
/*     */     }
/*     */     
/* 473 */     for (int i = 1; i < resultTokens.length; i++) {
/* 474 */       String result = "";
/*     */       
/* 476 */       if ((i - 1) % 5 == 0) {
/* 477 */         result = result + resultTokens[i] + " => ";
/* 478 */         result = result + "Score : " + resultTokens[(i + 2)] + " | ";
/* 479 */         result = result + "Fooled : " + resultTokens[(i + 3)] + " player(s) | ";
/* 480 */         result = result + "Fooled by : " + resultTokens[(i + 4)] + " player(s)";
/*     */         
/* 482 */         this.resultModel.addElement(result);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private JPanel createResultPanelColumn(String[] displayValues) {
/* 488 */     JPanel panel = new JPanel(new java.awt.GridLayout(0, 1));
/* 489 */     panel.setSize(270, 40);
/* 490 */     if ((displayValues == null) || (displayValues.length < 1))
/* 491 */       return panel;
/* 492 */     for (String s : displayValues) {
/* 493 */       panel.add(new JLabel(s));
/*     */     }
/* 495 */     return panel;
/*     */   }
/*     */   
/*     */   private void showView(FoilMakerClientView.ViewNames view) {
/* 499 */     java.awt.CardLayout cl = (java.awt.CardLayout)this.mainPanel.getLayout();
/* 500 */     cl.show(this.mainPanel, view.toString());
/* 501 */     validate();
/* 502 */     repaint();
/* 503 */     setVisible(true);
/*     */   }
/*     */   
/* 506 */   public void showLoginView() { showView(FoilMakerClientView.ViewNames.LOGINVIEW); }
/*     */   
/* 508 */   public void showChooseGameView() { showView(FoilMakerClientView.ViewNames.CHOOSEGAMEVIEW); }
/*     */   
/*     */   public void showLeaderGameView() {
/* 511 */     showView(FoilMakerClientView.ViewNames.NEWGAMEKEYVIEW);
/* 512 */     this.newGameKeyPanelKey.setText(this.clientController.getCurrentGameKey());
/* 513 */     setStatusMsg("Press <Start Game> when all users have joined");
/*     */   }
/*     */   
/*     */   public void showParticipantView(boolean isLeader) {
/* 517 */     this.isLeader = isLeader;
/* 518 */     showView(FoilMakerClientView.ViewNames.WAITINGFORLEADERVIEW);
/*     */   }
/*     */   
/*     */   public void addParticipant(String name) {
/* 522 */     this.participantListModel.addElement(name);
/* 523 */     this.newGameKeyPanelStartGameButton.setEnabled(true);
/*     */   }
/*     */   
/*     */   public void showCard(String[] cardDetails) {
/* 527 */     showView(FoilMakerClientView.ViewNames.ENTERSUGGESTIONVIEW);
/* 528 */     if (cardDetails.length != 2) {
/* 529 */       System.err.println("Unexpected number of serverTokens in view.showCard");
/* 530 */       return;
/*     */     }
/* 532 */     this.currentCardFront = cardDetails[0];
/* 533 */     this.currentCardBack = cardDetails[1];
/* 534 */     this.enterSuggestionPanelCardFrontTextArea.setText(trimText(this.currentCardFront));
/* 535 */     this.enterSuggestionPanelSuggestionArea.setText("");
/* 536 */     setStatusMsg("Enter your suggestion then click on <Send Response>");
/*     */   }
/*     */   
/*     */   public void showRoundOptionsView(String[] options) {
/* 540 */     updateRoundOptionsView(options);
/* 541 */     showView(FoilMakerClientView.ViewNames.PICKOPTIONVIEW);
/* 542 */     setStatusMsg("Pick your option and click <Send> ");
/*     */   }
/*     */   
/*     */   public void showRoundResultView(String[] results) {
/* 546 */     updateRoundResultsView(results);
/* 547 */     showView(FoilMakerClientView.ViewNames.ROUNDRESULTVIEW);
/* 548 */     setStatusMsg(" Click <Next Round> when ready ");
/*     */   }
/*     */   
/*     */   public void setStatusMsg(String msg) {
/* 552 */     if (msg != null)
/* 553 */       this.statusMessageLabel.setText(msg);
/*     */   }
/*     */   
/*     */   public void setTopMsg(String msg) {
/* 557 */     this.topMessageLabel.setText(msg);
/*     */   }
/*     */   
/*     */ 
/*     */   public void actionPerformed(java.awt.event.ActionEvent actionEvent)
/*     */   {
/* 563 */     Object target = actionEvent.getSource();
/*     */     
/* 565 */     if (target == this.loginPanelLoginButton)
/*     */     {
/* 567 */       String userName = this.loginPanelUserName.getText();
/* 568 */       char[] password = this.loginPanelPassword.getPassword();
/* 569 */       if (isValidPassword(password)) {
/* 570 */         String userPassword = new String(password);
/* 571 */         this.clientController.loginToServer(userName, userPassword);
/*     */       } else {
/* 573 */         setStatusMsg("Invalid  Password");
/*     */       }
/* 575 */       return;
/*     */     }
/*     */     
/* 578 */     if (target == this.loginPanelCreateNewUserButton)
/*     */     {
/* 580 */       String userName = this.loginPanelUserName.getText();
/* 581 */       char[] password = this.loginPanelPassword.getPassword();
/* 582 */       if (!isValidUserName(userName)) {
/* 583 */         setStatusMsg("Invalid User Name");
/* 584 */         return;
/*     */       }
/* 586 */       if (isValidPassword(password)) {
/* 587 */         String userPassword = new String(password);
/* 588 */         this.clientController.createNewUser(userName, userPassword);
/*     */       } else {
/* 590 */         setStatusMsg("Invalid  Password");
/*     */       }
/* 592 */       return;
/*     */     }
/*     */     
/* 595 */     if (target == this.chooseGamePanelNewGameButton) {
/* 596 */       this.clientController.startNewGame();
/* 597 */       return;
/*     */     }
/*     */     
/* 600 */     if (target == this.chooseGamePanelJoinGameButton) {
/* 601 */       showView(FoilMakerClientView.ViewNames.ENTERGAMEKEYVIEW);
/* 602 */       return;
/*     */     }
/*     */     
/* 605 */     if (target == this.enterGameKeyPanelJoinButton) {
/* 606 */       this.clientController.joinGame(this.enterGameKeyPanelKey.getText());
/* 607 */       return;
/*     */     }
/*     */     
/* 610 */     if (target == this.newGameKeyPanelStartGameButton)
/*     */     {
/* 612 */       this.clientController.allPlayersReady(); return;
/*     */     }
/*     */     
/*     */     String suggestion;
/* 616 */     if (target == this.enterSuggestionPanelSendButton)
/*     */     {
/* 618 */       suggestion = this.enterSuggestionPanelSuggestionArea.getText();
/* 619 */       if (suggestion.length() < 1) {
/* 620 */         setStatusMsg("Please enter your suggestion");
/* 621 */         return;
/*     */       }
/* 623 */       showView(FoilMakerClientView.ViewNames.WAITINGFOROTHERSVIEW);
/* 624 */       this.clientController.sendSuggestion(suggestion);
/* 625 */       return;
/*     */     }
/*     */     
/* 628 */     if (target == this.pickOptionPanelSendButton) {
/* 629 */       for (javax.swing.JRadioButton b : this.pickOptionPanelRadioButton)
/* 630 */         if (b.isSelected()) {
/* 631 */           this.clientController.sendOption(b.getText());
/* 632 */           showView(FoilMakerClientView.ViewNames.WAITINGFOROTHERSVIEW);
/*     */         }
/* 634 */       return;
/*     */     }
/*     */     
/* 637 */     if (target == this.roundResultPanelContinueButton) {
/* 638 */       this.clientController.notifyModelToContinue();
/*     */     }
/*     */   }
/*     */   
/*     */   public void disableContinueButton()
/*     */   {
/* 644 */     this.roundResultPanelContinueButton.setEnabled(false);
/*     */   }
/*     */   
/*     */   private boolean isValidPassword(char[] password) {
/* 648 */     String passString = new String(password);
/* 649 */     if ((passString.indexOf("--") >= 0) || 
/* 650 */       (passString.compareTo(passString.trim()) != 0))
/*     */     {
/* 652 */       return false; }
/* 653 */     return true;
/*     */   }
/*     */   
/*     */   private boolean isValidUserName(String userName) {
/* 657 */     if ((userName.indexOf("--") >= 0) || 
/* 658 */       (userName.compareTo(userName.trim()) != 0))
/*     */     {
/* 660 */       return false; }
/* 661 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   private String trimText(String text)
/*     */   {
/* 667 */     java.util.Scanner scanner = new java.util.Scanner(text);
/* 668 */     StringBuilder outputStr = new StringBuilder("");
/*     */     
/* 670 */     int currentLineLength = 0;
/* 671 */     while (scanner.hasNext()) {
/* 672 */       String word = scanner.next();
/* 673 */       if (currentLineLength + word.length() > 28) {
/* 674 */         outputStr.append("\n" + word + " ");
/* 675 */         currentLineLength = word.length() + 1;
/*     */       } else {
/* 677 */         outputStr.append(word + " ");
/* 678 */         currentLineLength += word.length() + 1;
/*     */       }
/*     */     }
/* 681 */     System.err.println(outputStr.toString());
/* 682 */     return outputStr.toString();
/*     */   }
/*     */   
/*     */   public class FoilMakerIconListRenderer extends javax.swing.DefaultListCellRenderer {
/*     */     public FoilMakerIconListRenderer() {}
/*     */     
/*     */     public java.awt.Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
/* 689 */       JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
/* 690 */       label.setIcon(javax.swing.plaf.metal.MetalIconFactory.getFileChooserDetailViewIcon());
/* 691 */       return label;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/brandonnguyen/Desktop/PurdueStuffs/CS18000/Foiler/Foilmaker.client.jar!/FoilMakerClientView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */
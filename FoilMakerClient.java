/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FoilMakerClient
/*     */ {
/*     */   private FoilMakerClientModel model;
/*     */   private FoilMakerClientView view;
/*     */   
/*     */   public FoilMakerClient(String[] args)
/*     */   {
/*  14 */     this.model = new FoilMakerClientModel(this, args);
/*  15 */     this.view = new FoilMakerClientView(this);
/*     */     try {
/*  17 */       this.model.connectToServer();
/*     */     } catch (Exception e) {
/*  19 */       System.err.println("Unable to to establish to server:" + e.getMessage());
/*  20 */       e.printStackTrace();
/*  21 */       this.view.setStatusMsg("Unable to Connect to Server");
/*     */     }
/*     */   }
/*     */   
/*     */   public void loginToServer(String name, String password) {
/*     */     try {
/*  27 */       if (this.model.loginToServer(name, password)) {
/*  28 */         this.view.showChooseGameView();
/*  29 */         this.model.setStatus(FoilMakerClientModel.STATUS_T.CHOOSEGAME);
/*  30 */         this.view.setTopMsg(name);
/*     */       }
/*     */     } catch (FoilMakerException e) {
/*  33 */       this.view.setStatusMsg("Login Failure: " + e.getMessage());
/*     */     }
/*     */   }
/*     */   
/*     */   public void logout() throws IOException {
/*  38 */     this.model.logout();
/*  39 */     this.model.closeConnectionToServer();
/*     */   }
/*     */   
/*     */   public void createNewUser(String name, String password)
/*     */   {
/*  44 */     if (this.model.createNewUser(name, password)) {
/*  45 */       this.view.setStatusMsg("New user created");
/*     */     } else
/*  47 */       this.view.setStatusMsg("User creation failure ");
/*     */   }
/*     */   
/*     */   public void startNewGame() {
/*  51 */     if (this.model.startNewGame()) {
/*  52 */       this.view.showLeaderGameView();
/*  53 */       this.model.setStatus(FoilMakerClientModel.STATUS_T.GAMELEADERWAITING);
/*  54 */       this.view.setStatusMsg("Game started: You are the leader");
/*  55 */       this.model.playGame();
/*     */     } else {
/*  57 */       this.view.setStatusMsg("Failure: ");
/*     */     }
/*     */   }
/*     */   
/*  61 */   public void joinGame(String gameKey) { if (this.model.joinGame(gameKey)) {
/*  62 */       this.view.showParticipantView(false);
/*  63 */       this.model.setStatus(FoilMakerClientModel.STATUS_T.PARTICIPANT);
/*  64 */       this.view.setStatusMsg("Joined game: waiting for leader");
/*  65 */       this.model.playGame();
/*     */     } else {
/*  67 */       this.view.setStatusMsg("Failure: ");
/*     */     }
/*     */   }
/*     */   
/*  71 */   public void allPlayersReady() { this.model.allPlayersReady();
/*  72 */     this.view.showParticipantView(true);
/*     */   }
/*     */   
/*     */   public void showCard(String[] serverMessageTokens) {
/*  76 */     this.view.showCard(serverMessageTokens);
/*  77 */     this.view.setStatusMsg("Enter your suggestion");
/*     */   }
/*     */   
/*     */ 
/*  81 */   public void sendSuggestion(String suggestion) { this.model.sendSuggestionToServer(suggestion); }
/*     */   
/*     */   public void showRoundOptions(String[] serverMessageTokens) {
/*  84 */     this.view.showRoundOptionsView(serverMessageTokens);
/*  85 */     this.view.setStatusMsg("Pick your choice");
/*     */   }
/*     */   
/*  88 */   public void sendOption(String optionString) { this.model.sendOptionToServer(optionString); }
/*     */   
/*     */   public void showRoundResult(String[] serverMessageTokens) {
/*  91 */     this.view.showRoundResultView(serverMessageTokens);
/*     */   }
/*     */   
/*     */   public void notifyModelToContinue() {
/*  95 */     synchronized (this.model) {
/*  96 */       this.model.notify();
/*     */     }
/*     */   }
/*     */   
/* 100 */   public void addNewPlayer(String name) { this.view.addParticipant(name);
/* 101 */     this.view.setStatusMsg("Press <Start game> to start game");
/*     */   }
/*     */   
/*     */   public void showGameOver() {
/* 105 */     this.view.disableContinueButton();
/* 106 */     this.view.setStatusMsg("Game over!");
/*     */   }
/*     */   
/*     */   public String getCurrentGameKey() {
/* 110 */     return this.model.getCurrentGameKey();
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {
/* 114 */     if (args.length == 0) {
/* 115 */       System.err.println("Using default server (local server) and port:9999");
/*     */     }
/* 117 */     FoilMakerClient client = new FoilMakerClient(args);
/* 118 */     client.view.showLoginView();
/*     */   }
/*     */   
/*     */   public void setStatusMsg(String msg) {
/* 122 */     this.view.setStatusMsg(msg);
/*     */   }
/*     */ }


/* Location:              /Users/brandonnguyen/Desktop/PurdueStuffs/CS18000/Foiler/Foilmaker.client.jar!/FoilMakerClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketException;
/*     */ 
/*     */ public class FoilMakerClientModel extends Thread
/*     */ {
/*     */   public static enum STATUS_T
/*     */   {
/*  11 */     LOGIN,  CHOOSEGAME,  GAMELEADERWAITING,  GAMELEADERPLAYING,  PARTICIPANT,  GAMEOVER;
/*     */     private STATUS_T() {} }
/*  13 */   private final String DEFAULT_SERVER = "127.0.0.1";
/*  14 */   private final int DEFAULT_PORT = 9999;
/*  15 */   private FoilMakerClient controller = null;
/*  16 */   private FoilMakerClientModel.STATUS_T status = FoilMakerClientModel.STATUS_T.LOGIN;
/*  17 */   private String userName = null; private String userPassword = null;
/*  18 */   private String loginToken = null;
/*  19 */   private String currentGameKey = null;
/*  20 */   private java.util.HashMap<String, Integer> allParticipants = null;
/*     */   
/*     */ 
/*  23 */   private Socket socket = null;
/*  24 */   private String serverName = null;
/*  25 */   private int serverPortNumber = 0;
/*  26 */   private Socket serverSocket = null;
/*  27 */   private BufferedReader inFromServer = null;
/*  28 */   private java.io.PrintWriter outToServer = null;
/*  29 */   private boolean connectedToServer = false;
/*     */   
/*     */   public FoilMakerClientModel(FoilMakerClient controller, String[] args) {
/*  32 */     this.controller = controller;
/*  33 */     if (args.length == 2) {
/*  34 */       this.serverName = args[0];
/*  35 */       this.serverPortNumber = Integer.parseInt(args[1]);
/*  36 */     } else if (args.length == 0) {
/*  37 */       this.serverName = "127.0.0.1";
/*  38 */       this.serverPortNumber = 9999;
/*     */     } else {
/*  40 */       System.err.println("Usage: java FoilMakerClient server port");
/*  41 */       System.exit(1);
/*     */     }
/*  43 */     this.connectedToServer = false;
/*     */   }
/*     */   
/*     */   public void connectToServer() throws IOException {
/*  47 */     this.serverSocket = new Socket(this.serverName, this.serverPortNumber);
/*  48 */     if (this.serverSocket != null) {
/*  49 */       this.inFromServer = new BufferedReader(new java.io.InputStreamReader(this.serverSocket.getInputStream()));
/*  50 */       this.outToServer = new java.io.PrintWriter(this.serverSocket.getOutputStream(), true);
/*  51 */       this.connectedToServer = true;
/*     */     } else {
/*  53 */       this.connectedToServer = false;
/*     */     }
/*     */   }
/*     */   
/*     */   public void closeConnectionToServer() throws IOException {
/*  58 */     if (this.serverSocket != null)
/*  59 */       this.serverSocket.close();
/*  60 */     if (this.inFromServer != null)
/*  61 */       this.inFromServer.close();
/*  62 */     if (this.outToServer != null)
/*  63 */       this.outToServer.close();
/*  64 */     this.connectedToServer = false;
/*     */   }
/*     */   
/*     */   public boolean loginToServer(String user, String password) throws FoilMakerException {
/*  68 */     String[] sendTokens = new String[2];
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  74 */     if (!this.connectedToServer) {
/*  75 */       throw new FoilMakerException(FoilMakerNetworkProtocol.MSG_DETAIL_T.NO_CONNECTION_TO_SERVER);
/*     */     }
/*     */     
/*  78 */     if ((user == null) || (user.length() < 1) || (password == null) || (password.length() < 1)) {
/*  79 */       return false;
/*     */     }
/*  81 */     sendTokens[0] = (this.userName = user);
/*  82 */     sendTokens[1] = (this.userPassword = password);
/*     */     
/*  84 */     sendToServer(FoilMakerNetworkProtocol.MSG_TYPE.LOGIN, sendTokens);
/*     */     
/*  86 */     String serverMessage = readLineFromServer();
/*  87 */     if (serverMessage == null)
/*  88 */       return false;
/*  89 */     String[] serverMessageTokens = parseServerMessage(serverMessage);
/*  90 */     if (serverMessageTokens == null)
/*  91 */       return false;
/*  92 */     FoilMakerNetworkProtocol.MSG_TYPE serverMsgType = FoilMakerNetworkProtocol.MSG_TYPE.valueOf(serverMessageTokens[0]);
/*  93 */     FoilMakerNetworkProtocol.MSG_TYPE clientRequestType = FoilMakerNetworkProtocol.MSG_TYPE.valueOf(serverMessageTokens[1]);
/*  94 */     FoilMakerNetworkProtocol.MSG_DETAIL_T msgDetail = FoilMakerNetworkProtocol.MSG_DETAIL_T.valueOf(serverMessageTokens[2]);
/*     */     
/*  96 */     if ((serverMsgType == FoilMakerNetworkProtocol.MSG_TYPE.RESPONSE) && (clientRequestType == FoilMakerNetworkProtocol.MSG_TYPE.LOGIN) && (msgDetail == FoilMakerNetworkProtocol.MSG_DETAIL_T.SUCCESS))
/*     */     {
/*     */ 
/*  99 */       this.loginToken = serverMessageTokens[3];
/* 100 */       setStatus(FoilMakerClientModel.STATUS_T.CHOOSEGAME);
/* 101 */       return true;
/*     */     }
/* 103 */     return false;
/*     */   }
/*     */   
/*     */   public boolean createNewUser(String user, String password) {
/* 107 */     String[] sendTokens = new String[2];
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 113 */     if (!this.connectedToServer) {
/* 114 */       return false;
/*     */     }
/*     */     
/* 117 */     if ((user == null) || (user.length() < 1) || (password == null) || (password.length() < 1)) {
/* 118 */       System.err.println("Error with arguments" + user + password);
/* 119 */       return false;
/*     */     }
/* 121 */     sendTokens[0] = (this.userName = user);
/* 122 */     sendTokens[1] = (this.userPassword = password);
/*     */     
/* 124 */     sendToServer(FoilMakerNetworkProtocol.MSG_TYPE.CREATENEWUSER, sendTokens);
/* 125 */     String serverMessage = readLineFromServer();
/* 126 */     String[] serverMessageTokens = parseServerMessage(serverMessage);
/* 127 */     if (serverMessageTokens == null) {
/* 128 */       return false;
/*     */     }
/* 130 */     FoilMakerNetworkProtocol.MSG_TYPE serverMsgType = FoilMakerNetworkProtocol.MSG_TYPE.valueOf(serverMessageTokens[0]);
/* 131 */     FoilMakerNetworkProtocol.MSG_TYPE clientRequestType = FoilMakerNetworkProtocol.MSG_TYPE.valueOf(serverMessageTokens[1]);
/* 132 */     FoilMakerNetworkProtocol.MSG_DETAIL_T msgDetail = FoilMakerNetworkProtocol.MSG_DETAIL_T.valueOf(serverMessageTokens[2]);
/*     */     
/* 134 */     if ((serverMsgType == FoilMakerNetworkProtocol.MSG_TYPE.RESPONSE) && (clientRequestType == FoilMakerNetworkProtocol.MSG_TYPE.CREATENEWUSER) && (msgDetail == FoilMakerNetworkProtocol.MSG_DETAIL_T.SUCCESS))
/*     */     {
/*     */ 
/* 137 */       return true;
/*     */     }
/* 139 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean startNewGame()
/*     */   {
/* 148 */     sendToServer(FoilMakerNetworkProtocol.MSG_TYPE.STARTNEWGAME, this.loginToken);
/* 149 */     String serverMessage = readLineFromServer();
/* 150 */     String[] serverMessageTokens = parseServerMessage(serverMessage);
/* 151 */     if (serverMessageTokens == null) {
/* 152 */       return false;
/*     */     }
/* 154 */     FoilMakerNetworkProtocol.MSG_TYPE serverMsgType = FoilMakerNetworkProtocol.MSG_TYPE.valueOf(serverMessageTokens[0]);
/* 155 */     FoilMakerNetworkProtocol.MSG_TYPE clientRequestType = FoilMakerNetworkProtocol.MSG_TYPE.valueOf(serverMessageTokens[1]);
/* 156 */     FoilMakerNetworkProtocol.MSG_DETAIL_T msgDetail = FoilMakerNetworkProtocol.MSG_DETAIL_T.valueOf(serverMessageTokens[2]);
/*     */     
/* 158 */     if ((serverMsgType == FoilMakerNetworkProtocol.MSG_TYPE.RESPONSE) && (clientRequestType == FoilMakerNetworkProtocol.MSG_TYPE.STARTNEWGAME) && (msgDetail == FoilMakerNetworkProtocol.MSG_DETAIL_T.SUCCESS))
/*     */     {
/*     */ 
/* 161 */       setStatus(FoilMakerClientModel.STATUS_T.GAMELEADERWAITING);
/* 162 */       this.currentGameKey = serverMessageTokens[3];
/* 163 */       this.allParticipants = new java.util.HashMap();
/* 164 */       return true;
/*     */     }
/* 166 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean joinGame(String gameKey)
/*     */   {
/* 176 */     String[] sendTokens = new String[2];
/* 177 */     sendTokens[0] = this.loginToken;
/*     */     
/* 179 */     sendTokens[1] = gameKey;
/* 180 */     sendToServer(FoilMakerNetworkProtocol.MSG_TYPE.JOINGAME, sendTokens);
/* 181 */     String serverMessage = readLineFromServer();
/* 182 */     String[] serverMessageTokens = parseServerMessage(serverMessage);
/* 183 */     if (serverMessageTokens == null) {
/* 184 */       return false;
/*     */     }
/* 186 */     FoilMakerNetworkProtocol.MSG_TYPE serverMsgType = FoilMakerNetworkProtocol.MSG_TYPE.valueOf(serverMessageTokens[0]);
/* 187 */     FoilMakerNetworkProtocol.MSG_TYPE clientRequestType = FoilMakerNetworkProtocol.MSG_TYPE.valueOf(serverMessageTokens[1]);
/* 188 */     FoilMakerNetworkProtocol.MSG_DETAIL_T msgDetail = FoilMakerNetworkProtocol.MSG_DETAIL_T.valueOf(serverMessageTokens[2]);
/*     */     
/* 190 */     if ((serverMsgType == FoilMakerNetworkProtocol.MSG_TYPE.RESPONSE) && (clientRequestType == FoilMakerNetworkProtocol.MSG_TYPE.JOINGAME) && (msgDetail == FoilMakerNetworkProtocol.MSG_DETAIL_T.SUCCESS))
/*     */     {
/*     */ 
/* 193 */       setStatus(FoilMakerClientModel.STATUS_T.PARTICIPANT);
/* 194 */       this.currentGameKey = serverMessageTokens[3];
/* 195 */       return true;
/*     */     }
/* 197 */     return false;
/*     */   }
/*     */   
/*     */   private void sendToServer(FoilMakerNetworkProtocol.MSG_TYPE msg_type, String arg) {
/* 201 */     String[] args = new String[1];
/* 202 */     args[0] = arg;
/* 203 */     sendToServer(msg_type, args);
/*     */   }
/*     */   
/*     */   public void playGame()
/*     */   {
/* 208 */     start();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void run()
/*     */   {
/* 215 */     System.err.println("Starting game thread");
/* 216 */     if (isLeader()) {
/* 217 */       waitForParticipants();
/*     */     }
/* 219 */     System.err.println("Done with waiting for participants");
/* 220 */     participateInGame();
/*     */     
/*     */ 
/* 223 */     setStatus(FoilMakerClientModel.STATUS_T.CHOOSEGAME);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void waitForParticipants()
/*     */   {
/*     */     for (;;)
/*     */     {
/* 236 */       synchronized (this.status) {
/* 237 */         if (this.status == FoilMakerClientModel.STATUS_T.GAMELEADERPLAYING)
/*     */           break;
/*     */       }
/*     */       try {
/* 241 */         this.serverSocket.setSoTimeout(1000);
/* 242 */         String serverMessage = readLineFromServer();
/* 243 */         if (serverMessage != null)
/*     */         {
/* 245 */           String[] serverMessageTokens = parseServerMessage(serverMessage);
/* 246 */           FoilMakerNetworkProtocol.MSG_TYPE serverMsgType = FoilMakerNetworkProtocol.MSG_TYPE.valueOf(serverMessageTokens[0]);
/* 247 */           if (serverMsgType != FoilMakerNetworkProtocol.MSG_TYPE.NEWPARTICIPANT) {
/* 248 */             System.err.println("Unexpected message  from server: " + serverMessage);
/*     */           }
/*     */           else {
/* 251 */             String participantName = serverMessageTokens[1];
/* 252 */             int score = Integer.parseInt(serverMessageTokens[2]);
/*     */             
/* 254 */             this.controller.addNewPlayer(participantName);
/* 255 */             this.allParticipants.put(participantName, Integer.valueOf(score));
/*     */           }
/* 257 */         } } catch (SocketException e) { System.err.println("Unexpected error when setting socket timeout");
/*     */       }
/*     */     }
/*     */     try
/*     */     {
/* 262 */       this.serverSocket.setSoTimeout(0);
/* 263 */       System.err.println("Reset timeout to 0");
/*     */     } catch (SocketException e) {
/* 265 */       System.err.println("Unexpected error when setting socket timeout");
/*     */     }
/* 267 */     System.err.println("Leader done waiting for participants");
/*     */     
/* 269 */     String[] sendTokens = new String[3];
/* 270 */     sendTokens[0] = this.loginToken;
/* 271 */     sendTokens[1] = this.currentGameKey;
/* 272 */     sendToServer(FoilMakerNetworkProtocol.MSG_TYPE.ALLPARTICIPANTSHAVEJOINED, sendTokens);
/*     */   }
/*     */   
/*     */   public void allPlayersReady() {
/* 276 */     System.err.println("Changing leader status to all PLAYING");
/* 277 */     setStatus(FoilMakerClientModel.STATUS_T.GAMELEADERPLAYING);
/*     */   }
/*     */   
/*     */   private void participateInGame()
/*     */   {
/* 282 */     boolean stillPlaying = true;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 288 */     while (stillPlaying) {
/* 289 */       System.err.println("Starting new round");
/* 290 */       String serverMessage = readLineFromServer();
/* 291 */       if (serverMessage != null)
/*     */       {
/* 293 */         String[] serverMessageTokens = parseServerMessage(serverMessage);
/* 294 */         FoilMakerNetworkProtocol.MSG_TYPE serverMsgType = FoilMakerNetworkProtocol.MSG_TYPE.valueOf(serverMessageTokens[0]);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 301 */         if (serverMsgType == FoilMakerNetworkProtocol.MSG_TYPE.GAMEOVER) {
/* 302 */           stillPlaying = false;
/* 303 */           setStatus(FoilMakerClientModel.STATUS_T.GAMEOVER);
/*     */           
/* 305 */           this.controller.showGameOver();
/*     */         }
/*     */         else
/*     */         {
/* 309 */           System.err.println("Received new Card");
/* 310 */           int numTokensToDrop = 1;
/* 311 */           String[] controllerTokens = new String[serverMessageTokens.length - numTokensToDrop];
/* 312 */           for (int i = 0; i < controllerTokens.length; i++)
/* 313 */             controllerTokens[i] = serverMessageTokens[(i + numTokensToDrop)];
/* 314 */           this.controller.showCard(controllerTokens);
/*     */           
/*     */ 
/* 317 */           serverMessage = readLineFromServer();
/* 318 */           if (serverMessage == null) {
/* 319 */             System.err.println("Unexpected null message while waiting for ROUNDOPTIONS");
/*     */           }
/*     */           else {
/* 322 */             serverMessageTokens = parseServerMessage(serverMessage);
/* 323 */             serverMsgType = FoilMakerNetworkProtocol.MSG_TYPE.valueOf(serverMessageTokens[0]);
/* 324 */             if (serverMsgType != FoilMakerNetworkProtocol.MSG_TYPE.ROUNDOPTIONS) {
/* 325 */               System.err.println("Unexpected message  from server: " + serverMessage);
/*     */             }
/*     */             else {
/* 328 */               numTokensToDrop = 1;
/* 329 */               controllerTokens = new String[serverMessageTokens.length - numTokensToDrop];
/* 330 */               for (int i = 0; i < controllerTokens.length; i++)
/* 331 */                 controllerTokens[i] = serverMessageTokens[(i + numTokensToDrop)];
/* 332 */               this.controller.showRoundOptions(controllerTokens);
/*     */               
/*     */ 
/* 335 */               serverMessage = readLineFromServer();
/* 336 */               if (serverMessage != null)
/*     */               {
/* 338 */                 serverMessageTokens = parseServerMessage(serverMessage);
/* 339 */                 serverMsgType = FoilMakerNetworkProtocol.MSG_TYPE.valueOf(serverMessageTokens[0]);
/* 340 */                 if (serverMsgType != FoilMakerNetworkProtocol.MSG_TYPE.ROUNDRESULT) {
/* 341 */                   System.err.println("Unexpected message  from server: " + serverMessage);
/*     */ 
/*     */ 
/*     */                 }
/*     */                 else
/*     */                 {
/*     */ 
/* 348 */                   this.controller.showRoundResult(serverMessageTokens);
/*     */                   
/* 350 */                   synchronized (this) {
/*     */                     try {
/* 352 */                       wait();
/*     */                     } catch (InterruptedException e) {
/* 354 */                       System.err.println("Model interrupted while waiting for continue signal");
/*     */                     }
/*     */                   }
/*     */                 }
/*     */               }
/*     */             }
/*     */           } } } } }
/*     */   
/* 362 */   public void logout() { sendToServer(FoilMakerNetworkProtocol.MSG_TYPE.LOGOUT, ""); }
/*     */   
/*     */   public void sendSuggestionToServer(String suggestion)
/*     */   {
/* 366 */     String[] sendTokens = new String[3];
/*     */     
/* 368 */     sendTokens[0] = this.loginToken;
/* 369 */     sendTokens[1] = this.currentGameKey;
/* 370 */     sendTokens[2] = suggestion;
/* 371 */     sendToServer(FoilMakerNetworkProtocol.MSG_TYPE.PLAYERSUGGESTION, sendTokens);
/*     */   }
/*     */   
/*     */   public void sendOptionToServer(String option) {
/* 375 */     String[] sendTokens = new String[3];
/*     */     
/* 377 */     sendTokens[0] = this.loginToken;
/* 378 */     sendTokens[1] = this.currentGameKey;
/* 379 */     sendTokens[2] = option;
/* 380 */     sendToServer(FoilMakerNetworkProtocol.MSG_TYPE.PLAYERCHOICE, sendTokens);
/*     */   }
/*     */   
/* 383 */   public String getCurrentGameKey() { return this.currentGameKey; }
/*     */   
/*     */   public void setStatus(FoilMakerClientModel.STATUS_T status) {
/* 386 */     synchronized (status) {
/* 387 */       this.status = status;
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isLeader() {
/* 392 */     synchronized (this.status) {
/* 393 */       return (this.status == FoilMakerClientModel.STATUS_T.GAMELEADERWAITING) || (this.status == FoilMakerClientModel.STATUS_T.GAMELEADERPLAYING);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isParticipant() {
/* 398 */     synchronized (this.status) {
/* 399 */       return this.status == FoilMakerClientModel.STATUS_T.PARTICIPANT;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void sendToServer(FoilMakerNetworkProtocol.MSG_TYPE msg_type, String[] args)
/*     */   {
/* 406 */     if (this.outToServer == null)
/* 407 */       return;
/* 408 */     StringBuilder message = new StringBuilder("" + msg_type);
/* 409 */     for (String s : args) {
/* 410 */       if (s != null)
/* 411 */         message.append("--" + s);
/*     */     }
/* 413 */     synchronized (this.outToServer) {
/* 414 */       this.outToServer.println(message);
/*     */     }
/* 416 */     System.err.println("Sent to server:" + message);
/*     */   }
/*     */   
/*     */   private String readLineFromServer()
/*     */   {
/* 421 */     if ((!this.connectedToServer) || (this.inFromServer == null)) {
/* 422 */       this.controller.setStatusMsg("Not connected to server");
/* 423 */       return null;
/*     */     }
/*     */     try { String serverMessage;
/* 426 */       synchronized (this.inFromServer) {
/* 427 */         serverMessage = this.inFromServer.readLine(); }
/*     */       String serverMessage;
/* 429 */       System.err.println("Read from server:" + serverMessage);
/*     */     } catch (java.net.SocketTimeoutException e) {
/* 431 */       return null;
/*     */     } catch (SocketException e) {
/* 433 */       return null;
/*     */     } catch (IOException e) {
/* 435 */       System.err.println("Error reading from server for user: " + this.userName + e.getMessage());
/* 436 */       e.printStackTrace();
/* 437 */       return null; }
/*     */     String serverMessage;
/* 439 */     return serverMessage;
/*     */   }
/*     */   
/*     */   private String[] parseServerMessage(String message) {
/* 443 */     if (message == null)
/* 444 */       return null;
/* 445 */     return message.split("--");
/*     */   }
/*     */ }


/* Location:              /Users/brandonnguyen/Desktop/PurdueStuffs/CS18000/Foiler/Foilmaker.client.jar!/FoilMakerClientModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */
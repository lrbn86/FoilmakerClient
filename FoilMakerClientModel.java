import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;

public class FoilMakerClientModel
  extends Thread
{
  public static enum STATUS_T
  {
    LOGIN,  CHOOSEGAME,  GAMELEADERWAITING,  GAMELEADERPLAYING,  PARTICIPANT,  GAMEOVER;
    
    private STATUS_T() {}
  }
  
  private final String DEFAULT_SERVER = "127.0.0.1";
  private final int DEFAULT_PORT = 9999;
  private FoilMakerClient controller = null;
  private FoilMakerClientModel.STATUS_T status = FoilMakerClientModel.STATUS_T.LOGIN;
  private String userName = null;
  private String userPassword = null;
  private String loginToken = null;
  private String currentGameKey = null;
  private HashMap<String, Integer> allParticipants = null;
  private Socket socket = null;
  private String serverName = null;
  private int serverPortNumber = 0;
  private Socket serverSocket = null;
  private BufferedReader inFromServer = null;
  private PrintWriter outToServer = null;
  private boolean connectedToServer = false;
  
  public FoilMakerClientModel(FoilMakerClient controller, String[] args)
  {
    this.controller = controller;
    if (args.length == 2)
    {
      this.serverName = args[0];
      this.serverPortNumber = Integer.parseInt(args[1]);
    }
    else if (args.length == 0)
    {
      this.serverName = "127.0.0.1";
      this.serverPortNumber = 9999;
    }
    else
    {
      System.err.println("Usage: java FoilMakerClient server port");
      System.exit(1);
    }
    this.connectedToServer = false;
  }
  
  public void connectToServer()
    throws IOException
  {
    this.serverSocket = new Socket(this.serverName, this.serverPortNumber);
    if (this.serverSocket != null)
    {
      this.inFromServer = new BufferedReader(new InputStreamReader(this.serverSocket.getInputStream()));
      this.outToServer = new PrintWriter(this.serverSocket.getOutputStream(), true);
      this.connectedToServer = true;
    }
    else
    {
      this.connectedToServer = false;
    }
  }
  
  public void closeConnectionToServer()
    throws IOException
  {
    if (this.serverSocket != null) {
      this.serverSocket.close();
    }
    if (this.inFromServer != null) {
      this.inFromServer.close();
    }
    if (this.outToServer != null) {
      this.outToServer.close();
    }
    this.connectedToServer = false;
  }
  
  public boolean loginToServer(String user, String password)
    throws FoilMakerException
  {
    String[] sendTokens = new String[2];
    if (!this.connectedToServer) {
      throw new FoilMakerException(FoilMakerNetworkProtocol.MSG_DETAIL_T.NO_CONNECTION_TO_SERVER);
    }
    if ((user == null) || (user.length() < 1) || (password == null) || (password.length() < 1)) {
      return false;
    }
    sendTokens[0] = (this.userName = user);
    sendTokens[1] = (this.userPassword = password);
    
    sendToServer(FoilMakerNetworkProtocol.MSG_TYPE.LOGIN, sendTokens);
    
    String serverMessage = readLineFromServer();
    if (serverMessage == null) {
      return false;
    }
    String[] serverMessageTokens = parseServerMessage(serverMessage);
    if (serverMessageTokens == null) {
      return false;
    }
    FoilMakerNetworkProtocol.MSG_TYPE serverMsgType = FoilMakerNetworkProtocol.MSG_TYPE.valueOf(serverMessageTokens[0]);
    FoilMakerNetworkProtocol.MSG_TYPE clientRequestType = FoilMakerNetworkProtocol.MSG_TYPE.valueOf(serverMessageTokens[1]);
    FoilMakerNetworkProtocol.MSG_DETAIL_T msgDetail = FoilMakerNetworkProtocol.MSG_DETAIL_T.valueOf(serverMessageTokens[2]);
    if ((serverMsgType == FoilMakerNetworkProtocol.MSG_TYPE.RESPONSE) && (clientRequestType == FoilMakerNetworkProtocol.MSG_TYPE.LOGIN) && (msgDetail == FoilMakerNetworkProtocol.MSG_DETAIL_T.SUCCESS))
    {
      this.loginToken = serverMessageTokens[3];
      setStatus(FoilMakerClientModel.STATUS_T.CHOOSEGAME);
      return true;
    }
    return false;
  }
  
  public boolean createNewUser(String user, String password)
  {
    String[] sendTokens = new String[2];
    if (!this.connectedToServer) {
      return false;
    }
    if ((user == null) || (user.length() < 1) || (password == null) || (password.length() < 1))
    {
      System.err.println("Error with arguments" + user + password);
      return false;
    }
    sendTokens[0] = (this.userName = user);
    sendTokens[1] = (this.userPassword = password);
    
    sendToServer(FoilMakerNetworkProtocol.MSG_TYPE.CREATENEWUSER, sendTokens);
    String serverMessage = readLineFromServer();
    String[] serverMessageTokens = parseServerMessage(serverMessage);
    if (serverMessageTokens == null) {
      return false;
    }
    FoilMakerNetworkProtocol.MSG_TYPE serverMsgType = FoilMakerNetworkProtocol.MSG_TYPE.valueOf(serverMessageTokens[0]);
    FoilMakerNetworkProtocol.MSG_TYPE clientRequestType = FoilMakerNetworkProtocol.MSG_TYPE.valueOf(serverMessageTokens[1]);
    FoilMakerNetworkProtocol.MSG_DETAIL_T msgDetail = FoilMakerNetworkProtocol.MSG_DETAIL_T.valueOf(serverMessageTokens[2]);
    if ((serverMsgType == FoilMakerNetworkProtocol.MSG_TYPE.RESPONSE) && (clientRequestType == FoilMakerNetworkProtocol.MSG_TYPE.CREATENEWUSER) && (msgDetail == FoilMakerNetworkProtocol.MSG_DETAIL_T.SUCCESS)) {
      return true;
    }
    return false;
  }
  
  public boolean startNewGame()
  {
    sendToServer(FoilMakerNetworkProtocol.MSG_TYPE.STARTNEWGAME, this.loginToken);
    String serverMessage = readLineFromServer();
    String[] serverMessageTokens = parseServerMessage(serverMessage);
    if (serverMessageTokens == null) {
      return false;
    }
    FoilMakerNetworkProtocol.MSG_TYPE serverMsgType = FoilMakerNetworkProtocol.MSG_TYPE.valueOf(serverMessageTokens[0]);
    FoilMakerNetworkProtocol.MSG_TYPE clientRequestType = FoilMakerNetworkProtocol.MSG_TYPE.valueOf(serverMessageTokens[1]);
    FoilMakerNetworkProtocol.MSG_DETAIL_T msgDetail = FoilMakerNetworkProtocol.MSG_DETAIL_T.valueOf(serverMessageTokens[2]);
    if ((serverMsgType == FoilMakerNetworkProtocol.MSG_TYPE.RESPONSE) && (clientRequestType == FoilMakerNetworkProtocol.MSG_TYPE.STARTNEWGAME) && (msgDetail == FoilMakerNetworkProtocol.MSG_DETAIL_T.SUCCESS))
    {
      setStatus(FoilMakerClientModel.STATUS_T.GAMELEADERWAITING);
      this.currentGameKey = serverMessageTokens[3];
      this.allParticipants = new HashMap();
      return true;
    }
    return false;
  }
  
  public boolean joinGame(String gameKey)
  {
    String[] sendTokens = new String[2];
    sendTokens[0] = this.loginToken;
    
    sendTokens[1] = gameKey;
    sendToServer(FoilMakerNetworkProtocol.MSG_TYPE.JOINGAME, sendTokens);
    String serverMessage = readLineFromServer();
    String[] serverMessageTokens = parseServerMessage(serverMessage);
    if (serverMessageTokens == null) {
      return false;
    }
    FoilMakerNetworkProtocol.MSG_TYPE serverMsgType = FoilMakerNetworkProtocol.MSG_TYPE.valueOf(serverMessageTokens[0]);
    FoilMakerNetworkProtocol.MSG_TYPE clientRequestType = FoilMakerNetworkProtocol.MSG_TYPE.valueOf(serverMessageTokens[1]);
    FoilMakerNetworkProtocol.MSG_DETAIL_T msgDetail = FoilMakerNetworkProtocol.MSG_DETAIL_T.valueOf(serverMessageTokens[2]);
    if ((serverMsgType == FoilMakerNetworkProtocol.MSG_TYPE.RESPONSE) && (clientRequestType == FoilMakerNetworkProtocol.MSG_TYPE.JOINGAME) && (msgDetail == FoilMakerNetworkProtocol.MSG_DETAIL_T.SUCCESS))
    {
      setStatus(FoilMakerClientModel.STATUS_T.PARTICIPANT);
      this.currentGameKey = serverMessageTokens[3];
      return true;
    }
    return false;
  }
  
  private void sendToServer(FoilMakerNetworkProtocol.MSG_TYPE msg_type, String arg)
  {
    String[] args = new String[1];
    args[0] = arg;
    sendToServer(msg_type, args);
  }
  
  public void playGame()
  {
    start();
  }
  
  public void run()
  {
    System.err.println("Starting game thread");
    if (isLeader()) {
      waitForParticipants();
    }
    System.err.println("Done with waiting for participants");
    participateInGame();
    
    setStatus(FoilMakerClientModel.STATUS_T.CHOOSEGAME);
  }
  
  private void waitForParticipants()
  {
    for (;;)
    {
      synchronized (this.status)
      {
        if (this.status == FoilMakerClientModel.STATUS_T.GAMELEADERPLAYING) {
          break;
        }
      }
      try
      {
        this.serverSocket.setSoTimeout(1000);
        String serverMessage = readLineFromServer();
        if (serverMessage != null)
        {
          String[] serverMessageTokens = parseServerMessage(serverMessage);
          FoilMakerNetworkProtocol.MSG_TYPE serverMsgType = FoilMakerNetworkProtocol.MSG_TYPE.valueOf(serverMessageTokens[0]);
          if (serverMsgType != FoilMakerNetworkProtocol.MSG_TYPE.NEWPARTICIPANT)
          {
            System.err.println("Unexpected message  from server: " + serverMessage);
          }
          else
          {
            String participantName = serverMessageTokens[1];
            int score = Integer.parseInt(serverMessageTokens[2]);
            
            this.controller.addNewPlayer(participantName);
            this.allParticipants.put(participantName, Integer.valueOf(score));
          }
        }
      }
      catch (SocketException e)
      {
        System.err.println("Unexpected error when setting socket timeout");
      }
    }
    try
    {
      this.serverSocket.setSoTimeout(0);
      System.err.println("Reset timeout to 0");
    }
    catch (SocketException e)
    {
      System.err.println("Unexpected error when setting socket timeout");
    }
    System.err.println("Leader done waiting for participants");
    
    String[] sendTokens = new String[3];
    sendTokens[0] = this.loginToken;
    sendTokens[1] = this.currentGameKey;
    sendToServer(FoilMakerNetworkProtocol.MSG_TYPE.ALLPARTICIPANTSHAVEJOINED, sendTokens);
  }
  
  public void allPlayersReady()
  {
    System.err.println("Changing leader status to all PLAYING");
    setStatus(FoilMakerClientModel.STATUS_T.GAMELEADERPLAYING);
  }
  
  private void participateInGame()
  {
    boolean stillPlaying = true;
    while (stillPlaying)
    {
      System.err.println("Starting new round");
      String serverMessage = readLineFromServer();
      if (serverMessage != null)
      {
        String[] serverMessageTokens = parseServerMessage(serverMessage);
        FoilMakerNetworkProtocol.MSG_TYPE serverMsgType = FoilMakerNetworkProtocol.MSG_TYPE.valueOf(serverMessageTokens[0]);
        if (serverMsgType == FoilMakerNetworkProtocol.MSG_TYPE.GAMEOVER)
        {
          stillPlaying = false;
          setStatus(FoilMakerClientModel.STATUS_T.GAMEOVER);
          
          this.controller.showGameOver();
        }
        else
        {
          System.err.println("Received new Card");
          int numTokensToDrop = 1;
          String[] controllerTokens = new String[serverMessageTokens.length - numTokensToDrop];
          for (int i = 0; i < controllerTokens.length; i++) {
            controllerTokens[i] = serverMessageTokens[(i + numTokensToDrop)];
          }
          this.controller.showCard(controllerTokens);
          
          serverMessage = readLineFromServer();
          if (serverMessage == null)
          {
            System.err.println("Unexpected null message while waiting for ROUNDOPTIONS");
          }
          else
          {
            serverMessageTokens = parseServerMessage(serverMessage);
            serverMsgType = FoilMakerNetworkProtocol.MSG_TYPE.valueOf(serverMessageTokens[0]);
            if (serverMsgType != FoilMakerNetworkProtocol.MSG_TYPE.ROUNDOPTIONS)
            {
              System.err.println("Unexpected message  from server: " + serverMessage);
            }
            else
            {
              numTokensToDrop = 1;
              controllerTokens = new String[serverMessageTokens.length - numTokensToDrop];
              for (int i = 0; i < controllerTokens.length; i++) {
                controllerTokens[i] = serverMessageTokens[(i + numTokensToDrop)];
              }
              this.controller.showRoundOptions(controllerTokens);
              
              serverMessage = readLineFromServer();
              if (serverMessage != null)
              {
                serverMessageTokens = parseServerMessage(serverMessage);
                serverMsgType = FoilMakerNetworkProtocol.MSG_TYPE.valueOf(serverMessageTokens[0]);
                if (serverMsgType != FoilMakerNetworkProtocol.MSG_TYPE.ROUNDRESULT)
                {
                  System.err.println("Unexpected message  from server: " + serverMessage);
                }
                else
                {
                  this.controller.showRoundResult(serverMessageTokens);
                  synchronized (this)
                  {
                    try
                    {
                      wait();
                    }
                    catch (InterruptedException e)
                    {
                      System.err.println("Model interrupted while waiting for continue signal");
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }
  
  public void logout()
  {
    sendToServer(FoilMakerNetworkProtocol.MSG_TYPE.LOGOUT, "");
  }
  
  public void sendSuggestionToServer(String suggestion)
  {
    String[] sendTokens = new String[3];
    
    sendTokens[0] = this.loginToken;
    sendTokens[1] = this.currentGameKey;
    sendTokens[2] = suggestion;
    sendToServer(FoilMakerNetworkProtocol.MSG_TYPE.PLAYERSUGGESTION, sendTokens);
  }
  
  public void sendOptionToServer(String option)
  {
    String[] sendTokens = new String[3];
    
    sendTokens[0] = this.loginToken;
    sendTokens[1] = this.currentGameKey;
    sendTokens[2] = option;
    sendToServer(FoilMakerNetworkProtocol.MSG_TYPE.PLAYERCHOICE, sendTokens);
  }
  
  public String getCurrentGameKey()
  {
    return this.currentGameKey;
  }
  
  public void setStatus(FoilMakerClientModel.STATUS_T status)
  {
    synchronized (status)
    {
      this.status = status;
    }
  }
  
  public boolean isLeader()
  {
    synchronized (this.status)
    {
      return (this.status == FoilMakerClientModel.STATUS_T.GAMELEADERWAITING) || (this.status == FoilMakerClientModel.STATUS_T.GAMELEADERPLAYING);
    }
  }
  
  public boolean isParticipant()
  {
    synchronized (this.status)
    {
      return this.status == FoilMakerClientModel.STATUS_T.PARTICIPANT;
    }
  }
  
  private void sendToServer(FoilMakerNetworkProtocol.MSG_TYPE msg_type, String[] args)
  {
    if (this.outToServer == null) {
      return;
    }
    StringBuilder message = new StringBuilder("" + msg_type);
    for (String s : args) {
      if (s != null) {
        message.append("--" + s);
      }
    }
    synchronized (this.outToServer)
    {
      this.outToServer.println(message);
    }
    System.err.println("Sent to server:" + message);
  }
  
  private String readLineFromServer()
  {
    if ((!this.connectedToServer) || (this.inFromServer == null))
    {
      this.controller.setStatusMsg("Not connected to server");
      return null;
    }
    try
    {
      String serverMessage;
      synchronized (this.inFromServer)
      {
        serverMessage = this.inFromServer.readLine();
      }
      String serverMessage;
      System.err.println("Read from server:" + serverMessage);
    }
    catch (SocketTimeoutException e)
    {
      return null;
    }
    catch (SocketException e)
    {
      return null;
    }
    catch (IOException e)
    {
      System.err.println("Error reading from server for user: " + this.userName + e.getMessage());
      e.printStackTrace();
      return null;
    }
    String serverMessage;
    return serverMessage;
  }
  
  private String[] parseServerMessage(String message)
  {
    if (message == null) {
      return null;
    }
    return message.split("--");
  }
}

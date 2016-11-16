import java.io.IOException;
import java.io.PrintStream;

public class FoilMakerClient
{
  private FoilMakerClientModel model;
  private FoilMakerClientView view;
  
  public FoilMakerClient(String[] args)
  {
    this.model = new FoilMakerClientModel(this, args);
    this.view = new FoilMakerClientView(this);
    try
    {
      this.model.connectToServer();
    }
    catch (Exception e)
    {
      System.err.println("Unable to to establish to server:" + e.getMessage());
      e.printStackTrace();
      this.view.setStatusMsg("Unable to Connect to Server");
    }
  }
  
  public void loginToServer(String name, String password)
  {
    try
    {
      if (this.model.loginToServer(name, password))
      {
        this.view.showChooseGameView();
        this.model.setStatus(FoilMakerClientModel.STATUS_T.CHOOSEGAME);
        this.view.setTopMsg(name);
      }
    }
    catch (FoilMakerException e)
    {
      this.view.setStatusMsg("Login Failure: " + e.getMessage());
    }
  }
  
  public void logout()
    throws IOException
  {
    this.model.logout();
    this.model.closeConnectionToServer();
  }
  
  public void createNewUser(String name, String password)
  {
    if (this.model.createNewUser(name, password)) {
      this.view.setStatusMsg("New user created");
    } else {
      this.view.setStatusMsg("User creation failure ");
    }
  }
  
  public void startNewGame()
  {
    if (this.model.startNewGame())
    {
      this.view.showLeaderGameView();
      this.model.setStatus(FoilMakerClientModel.STATUS_T.GAMELEADERWAITING);
      this.view.setStatusMsg("Game started: You are the leader");
      this.model.playGame();
    }
    else
    {
      this.view.setStatusMsg("Failure: ");
    }
  }
  
  public void joinGame(String gameKey)
  {
    if (this.model.joinGame(gameKey))
    {
      this.view.showParticipantView(false);
      this.model.setStatus(FoilMakerClientModel.STATUS_T.PARTICIPANT);
      this.view.setStatusMsg("Joined game: waiting for leader");
      this.model.playGame();
    }
    else
    {
      this.view.setStatusMsg("Failure: ");
    }
  }
  
  public void allPlayersReady()
  {
    this.model.allPlayersReady();
    this.view.showParticipantView(true);
  }
  
  public void showCard(String[] serverMessageTokens)
  {
    this.view.showCard(serverMessageTokens);
    this.view.setStatusMsg("Enter your suggestion");
  }
  
  public void sendSuggestion(String suggestion)
  {
    this.model.sendSuggestionToServer(suggestion);
  }
  
  public void showRoundOptions(String[] serverMessageTokens)
  {
    this.view.showRoundOptionsView(serverMessageTokens);
    this.view.setStatusMsg("Pick your choice");
  }
  
  public void sendOption(String optionString)
  {
    this.model.sendOptionToServer(optionString);
  }
  
  public void showRoundResult(String[] serverMessageTokens)
  {
    this.view.showRoundResultView(serverMessageTokens);
  }
  
  public void notifyModelToContinue()
  {
    synchronized (this.model)
    {
      this.model.notify();
    }
  }
  
  public void addNewPlayer(String name)
  {
    this.view.addParticipant(name);
    this.view.setStatusMsg("Press <Start game> to start game");
  }
  
  public void showGameOver()
  {
    this.view.disableContinueButton();
    this.view.setStatusMsg("Game over!");
  }
  
  public String getCurrentGameKey()
  {
    return this.model.getCurrentGameKey();
  }
  
  public static void main(String[] args)
  {
    if (args.length == 0) {
      System.err.println("Using default server (local server) and port:9999");
    }
    FoilMakerClient client = new FoilMakerClient(args);
    client.view.showLoginView();
  }
  
  public void setStatusMsg(String msg)
  {
    this.view.setStatusMsg(msg);
  }
}

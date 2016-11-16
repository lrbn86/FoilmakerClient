/*    */ 
/*    */ public class FoilMakerNetworkProtocol
/*    */ {
/*    */   public static final String SEPARATOR = "--";
/*    */   public static final int LOGIN_TOKEN_LENGTH = 10;
/*    */   public static final int GAME_KEY_LENGTH = 3;
/*    */   
/*    */   public static enum MSG_TYPE
/*    */   {
/* 10 */     CREATENEWUSER, 
/* 11 */     LOGIN, 
/* 12 */     LOGOUT, 
/* 13 */     STARTNEWGAME, 
/* 14 */     JOINGAME, 
/* 15 */     ALLPARTICIPANTSHAVEJOINED, 
/*    */     
/*    */ 
/* 18 */     PLAYERCHOICE, 
/* 19 */     PLAYERSUGGESTION, 
/*    */     
/*    */ 
/* 22 */     NEWPARTICIPANT, 
/* 23 */     RESPONSE, 
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 32 */     NEWGAMEWORD, 
/* 33 */     ROUNDOPTIONS, 
/* 34 */     ROUNDRESULT, 
/* 35 */     GAMEOVER;
/*    */     
/*    */     private MSG_TYPE() {} }
/*    */   
/* 39 */   public static enum MSG_DETAIL_T { SUCCESS, 
/*    */     
/* 41 */     INVALIDUSERNAME, 
/* 42 */     INVALIDUSERPASSWORD, 
/* 43 */     USERALREADYEXISTS, 
/* 44 */     UNKNOWNUSER, 
/* 45 */     USERALREADYLOGGEDIN, 
/* 46 */     GAMEKEYNOTFOUND, 
/* 47 */     NO_CONNECTION_TO_SERVER, 
/* 48 */     ERROR_OPENING_NETWORK_CONNECTION, 
/* 49 */     USERNOTLOGGEDIN, 
/* 50 */     USERNOTGAMELEADER, 
/* 51 */     INVALIDGAMETOKEN, 
/* 52 */     UNEXPECTEDMESSAGETYPE, 
/* 53 */     INVALIDMESSAGEFORMAT, 
/* 54 */     FAILURE;
/*    */     
/*    */     private MSG_DETAIL_T() {}
/*    */   }
/*    */ }


/* Location:              /Users/brandonnguyen/Desktop/PurdueStuffs/CS18000/Foiler/Foilmaker.client.jar!/FoilMakerNetworkProtocol.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */
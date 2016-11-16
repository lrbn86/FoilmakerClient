/*    */ public class FoilMakerException
/*    */   extends Exception
/*    */ {
/*    */   private FoilMakerNetworkProtocol.MSG_DETAIL_T msgDetail;
/*    */   
/*    */   public FoilMakerException(FoilMakerNetworkProtocol.MSG_DETAIL_T msgDetail)
/*    */   {
/*  8 */     super("" + msgDetail);
/*  9 */     this.msgDetail = msgDetail; }
/*    */   
/* 11 */   public FoilMakerNetworkProtocol.MSG_DETAIL_T getType() { return this.msgDetail; }
/*    */ }


/* Location:              /Users/brandonnguyen/Desktop/PurdueStuffs/CS18000/Foiler/Foilmaker.client.jar!/FoilMakerException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */
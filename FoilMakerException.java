public class FoilMakerException
  extends Exception
{
  private FoilMakerNetworkProtocol.MSG_DETAIL_T msgDetail;
  
  public FoilMakerException(FoilMakerNetworkProtocol.MSG_DETAIL_T msgDetail)
  {
    super("" + msgDetail);
    this.msgDetail = msgDetail;
  }
  
  public FoilMakerNetworkProtocol.MSG_DETAIL_T getType()
  {
    return this.msgDetail;
  }
}

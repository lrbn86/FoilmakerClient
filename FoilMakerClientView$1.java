import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import javax.swing.JOptionPane;

class FoilMakerClientView$1
  extends WindowAdapter
{
  FoilMakerClientView$1(FoilMakerClientView this$0, FoilMakerClient paramFoilMakerClient) {}
  
  public void windowClosing(WindowEvent e)
  {
    int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to quit?", null, 0, 3);
    if (response == 0)
    {
      try
      {
        this.val$controller.logout();
      }
      catch (IOException ex)
      {
        ex.printStackTrace();
      }
      System.exit(0);
    }
  }
}

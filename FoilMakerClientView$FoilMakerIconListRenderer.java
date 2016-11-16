import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.plaf.metal.MetalIconFactory;

public class FoilMakerClientView$FoilMakerIconListRenderer
  extends DefaultListCellRenderer
{
  public FoilMakerClientView$FoilMakerIconListRenderer(FoilMakerClientView this$0) {}
  
  public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
  {
    JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    label.setIcon(MetalIconFactory.getFileChooserDetailViewIcon());
    return label;
  }
}

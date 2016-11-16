/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.io.IOException;
/*     */ import javax.swing.JOptionPane;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class FoilMakerClientView$1
/*     */   extends WindowAdapter
/*     */ {
/*     */   FoilMakerClientView$1(FoilMakerClientView this$0, FoilMakerClient paramFoilMakerClient) {}
/*     */   
/*     */   public void windowClosing(WindowEvent e)
/*     */   {
/* 102 */     int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to quit?", null, 0, 3);
/*     */     
/*     */ 
/* 105 */     if (response == 0) {
/*     */       try {
/* 107 */         this.val$controller.logout();
/*     */       } catch (IOException ex) {
/* 109 */         ex.printStackTrace();
/*     */       }
/* 111 */       System.exit(0);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/brandonnguyen/Desktop/PurdueStuffs/CS18000/Foiler/Foilmaker.client.jar!/FoilMakerClientView$1.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */
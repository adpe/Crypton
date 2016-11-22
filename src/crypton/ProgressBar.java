package crypton;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

public class ProgressBar extends JPanel implements Runnable {
	 
	private static final long serialVersionUID = 1L;
	JProgressBar pbar;
 
	  public ProgressBar() {
	     pbar = new JProgressBar();
	     pbar.setMinimum(0);
	     pbar.setMaximum(EncryptAES.bytesFile);
	     add(pbar);
	     System.out.println(EncryptAES.bytesFile);
	  }
	 
	  public void updateBar(int newValue) {
	    pbar.setValue(newValue);
	  }
	 


	  @Override
	  public void run() {
		  // TODO Auto-generated method stub
		  final ProgressBar it = new ProgressBar();
			 
		     JFrame frame = new JFrame("Progress");
		     frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   
		     frame.setContentPane(it);
		     frame.pack();
		     frame.setVisible(true);
		 
		     for (int i = 0; i <= EncryptAES.bytesFile; i++) {
		       final int percent=i;
		       try {
		         SwingUtilities.invokeLater(new Runnable() {
		             public void run() {
		               it.updateBar(percent);
		             }
		         });
		         java.lang.Thread.sleep(100);
		       } catch (InterruptedException e) {;}
		     } 
	  }
}
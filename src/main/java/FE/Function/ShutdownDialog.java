package FE.Function;

import BE.RMI.IRemoteDesktop;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.rmi.RemoteException;

public class ShutdownDialog extends JDialog implements Runnable{
  public final static int WIDTH_DIALOG = 800;
  public final static int HEIGHT_DIALOG = 800;


  private IRemoteDesktop remote_obj;
  private Thread update_thread;

  private boolean shutdown;

  public ShutdownDialog(JFrame owner, IRemoteDesktop remote_obj) throws RemoteException {
    super(owner);
    this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    this.setTitle("SHUTDOWN INFORMATION");
    this.setResizable(false);
    this.getContentPane().setPreferredSize(new Dimension(ProcessDialog.WIDTH_DIALOG, ProcessDialog.HEIGHT_DIALOG));
    this.setLayout(null);
    this.pack();

    this.remote_obj = remote_obj;

    //add components
//    this.initComponents();

    // TODO: start graph
    this.update_thread = new Thread(this);
    this.update_thread.setDaemon(true);
    this.update_thread.start();
  }

  public void initComponents()  {
    try {
      this.shutdown = this.remote_obj.shutdownServer();
    } catch (Exception e) {
      e.printStackTrace();
    }

    if (this.shutdown == true) {
      JOptionPane.showMessageDialog(this,"Shutdown successfully");
    }
    else {
      JOptionPane.showMessageDialog(this,"Shutdown fail.","Alert",JOptionPane.WARNING_MESSAGE);
    }
  }



  @Override
  public void run() {

  }

  @Override
  public void dispose() {
    super.setVisible(false);
    super.dispose();
    if(!this.update_thread.isInterrupted())
      this.update_thread.interrupt();
  }

}

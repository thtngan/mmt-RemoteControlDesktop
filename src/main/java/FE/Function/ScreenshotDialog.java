package FE.Function;

import BE.RMI.IRemoteDesktop;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class ScreenshotDialog extends JDialog implements Runnable{
  public final static int WIDTH_DIALOG = 800;
  public final static int HEIGHT_DIALOG = 800;

  private JScrollPane process_scroll;

  private IRemoteDesktop remote_obj;
  private Thread update_thread;

  private byte[] pic;

  public ScreenshotDialog(JFrame owner, IRemoteDesktop remote_obj) throws RemoteException {
    super(owner);
    this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    this.setTitle("PROCESS INFORMATION");
    this.setResizable(false);
    this.getContentPane().setPreferredSize(new Dimension(ProcessDialog.WIDTH_DIALOG, ProcessDialog.HEIGHT_DIALOG));
    this.setLayout(null);
    this.pack();

    this.remote_obj = remote_obj;
    try {
      this.pic = this.remote_obj.takeScreenshotServer("png");
    } catch (Exception e) {
      e.printStackTrace();
    }
    //add components
    this.initComponents();

    // TODO: start graph
    this.update_thread = new Thread(this);
    this.update_thread.setDaemon(true);
    this.update_thread.start();
  }
  private void initComponents() throws RemoteException {
    // TODO: label
    JLabel label = new JLabel();
    label.setText("PROCESS INFORMATION");
    label.setFont(new Font("segoe ui", Font.BOLD, 14));
    label.setBounds(150, 20, 500, 30);
    this.add(label);

    // TODO: list process
    ByteArrayInputStream bis = new ByteArrayInputStream(this.pic);
    BufferedImage screenshot = null;
    try {
      screenshot = ImageIO.read(bis);
    } catch (IOException e) {
      e.printStackTrace();
    }

    JLabel picLabel = new JLabel(new ImageIcon(screenshot));
    this.process_scroll =  new JScrollPane(picLabel);
    this.process_scroll.setBounds(20, 80, 430, 240);
    this.add(process_scroll);
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

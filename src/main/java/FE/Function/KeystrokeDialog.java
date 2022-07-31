package FE.Function;

import BE.RMI.IRemoteDesktop;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.ByteArrayInputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class KeystrokeDialog extends JDialog implements Runnable{
  public final static int WIDTH_DIALOG = 480;
  public final static int HEIGHT_DIALOG = 380;

  private JScrollPane process_scroll;

  private IRemoteDesktop remote_obj;
  private Thread update_thread;

  private ArrayList<String> keyPressed = new ArrayList<>();


  public KeystrokeDialog(JFrame owner, IRemoteDesktop remote_obj) throws RemoteException {
    super(owner);
    this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    this.setTitle("KEYSTROKE INFORMATION");
    this.setResizable(false);
    this.getContentPane().setPreferredSize(new Dimension(ProcessDialog.WIDTH_DIALOG, ProcessDialog.HEIGHT_DIALOG));
    this.setLayout(null);
    this.pack();

    this.remote_obj = remote_obj;
//    this.keyPressed = this.remote_obj.
//    this.listProcess = this.remote_obj.keyPressedServer();

    //add components
    this.initComponents();

    // TODO: start graph
    this.update_thread = new Thread(this);
    this.update_thread.setDaemon(true);
    this.update_thread.start();
  }
  public void initComponents() throws RemoteException {
    this.keyPressed.add("");
    ArrayList<String> a = new ArrayList<>();
    a = this.remote_obj.getKeystroke(this.keyPressed);

    // TODO: label
    JLabel label = new JLabel();
    label.setText("KEYSTROKE INFORMATION");
    label.setFont(new Font("segoe ui", Font.BOLD, 14));
    label.setBounds(150, 20, 500, 30);
    this.add(label);

    // TODO: list process
    System.out.println("KEYSTROKE");
    this.keyPressed.forEach(key -> System.out.println("Key: " + key));
//    System.out.println(this.keyPressed.parallelStream());


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
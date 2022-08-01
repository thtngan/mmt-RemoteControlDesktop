package FE.Function;

import BE.RMI.IRemoteDesktop;
import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayInputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Vector;

public class KeystrokeDialog extends JDialog implements Runnable{
  public final static int WIDTH_DIALOG = 480;
  public final static int HEIGHT_DIALOG = 380;

  private JScrollPane process_scroll;

  private IRemoteDesktop remote_obj;
  private Thread update_thread;

  private ArrayList<String> listKeyPressed = new ArrayList<>();

  private GlobalKeyboardHook keyboardHook;

  public KeystrokeDialog(JFrame owner, IRemoteDesktop remote_obj) throws RemoteException {
    super(owner);
    this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    this.setTitle("KEYSTROKE INFORMATION");
    this.setResizable(true);
    this.getContentPane().setPreferredSize(new Dimension(ProcessDialog.WIDTH_DIALOG, ProcessDialog.HEIGHT_DIALOG));
    this.setLayout(null);
    this.pack();

    this.remote_obj = remote_obj;
//    this.keyboardHook = new GlobalKeyboardHook(true);

    //add components
    this.initComponents();

    // TODO: start graph
    this.update_thread = new Thread(this);
    this.update_thread.setDaemon(true);
    this.update_thread.start();
  }
  public void initComponents() throws RemoteException {
    // TODO: label
    JLabel label = new JLabel();
    label.setText("KEYSTROKE INFORMATION");
    label.setFont(new Font("segoe ui", Font.BOLD, 14));
    label.setBounds(150, 20, 500, 30);
    this.add(label);

    // TODO: list process
    System.out.println("KEYSTROKE====");

    // TODO: two button
    Button btnStart = new Button("START");
    btnStart.setBounds(20, 50, 210,30);
    btnStart.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        startSaveText();
      }
    });
    this.add(btnStart);

    Button btnStop = new Button("STOP");
    btnStop.setBounds(240, 50, 210,30);
    btnStop.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        stopSaveText();
      }
    });
    this.add(btnStop);

    this.process_scroll = new JScrollPane();
    this.process_scroll.setBounds(20, 90, 430, 300);
    this.add(this.process_scroll, BorderLayout.CENTER);

  }

//  public static void main(String[] args) {
//    KeystrokeDialog reg = null;
//    try {
//      reg = new KeystrokeDialog();
//    } catch (RemoteException e) {
//      e.printStackTrace();
//    }
//
//    reg.setVisible(true);
//  }

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

  private void startSaveText() {
    ArrayList<String> list = new ArrayList<>();
    list.clear();
    try {
      this.remote_obj.getKeystroke(list);
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }

  private void stopSaveText() {
    try {
      this.listKeyPressed = this.remote_obj.printKeyStroke();
    } catch (RemoteException e) {
      e.printStackTrace();
    }
    System.out.println(this.listKeyPressed.toString());
    String listString = String.join("", this.listKeyPressed);

    DefaultListModel<String> model = new DefaultListModel<>();
    model.addElement(listString);

    System.out.println("++++++" + listString);
    JTextPane textPane = new JTextPane();
    textPane.setText(listString);

    this.process_scroll.revalidate();
    this.process_scroll.repaint();
    JTextArea txtMain = new JTextArea();
    txtMain.setText(listString);
    this.process_scroll.setViewportView(txtMain);

    this.process_scroll.setBounds(20, 90, 430, 300);
    this.add(this.process_scroll, BorderLayout.CENTER);
  }

}
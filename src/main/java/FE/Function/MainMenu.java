package FE.Function;

import BE.Common.CommonBus;
import BE.RMI.IRemoteDesktop;
import FE.Panel.ClientPanel;

import javax.swing.*;
import java.awt.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;

public class MainMenu extends JFrame implements Runnable {
  public final static int WIDTH_DIALOG = 380;
  public final static int HEIGHT_DIALOG = 380;
  private ClientPanel client_panel;
  private CommonBus common_bus;
  private IRemoteDesktop remote_obj;

  private ProcessDialog dialog_process;
  private AppDialog dialog_app;

  private volatile Thread screen_thread;

  public MainMenu(ClientPanel client_panel, CommonBus common_bus) throws RemoteException {
    this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    this.setTitle("REMOTE MENU");
    this.setResizable(false);
    this.getContentPane().setPreferredSize(new Dimension(MainMenu.WIDTH_DIALOG, MainMenu.HEIGHT_DIALOG));
    this.setLayout(null);
    this.pack();
    this.setVisible(true);

    this.client_panel = client_panel;
    this.common_bus = common_bus;
    this.remote_obj = this.common_bus.getRmiClient().getRemoteObject();

    //add components
    this.initComponents();

    // TODO: start thread to share partner's screen
    this.screen_thread = new Thread(this);
    this.screen_thread.setDaemon(true);
    this.screen_thread.start();
  }

  private void initComponents() throws RemoteException {
    this.dialog_process = new ProcessDialog(this, this.remote_obj);
    this.dialog_app = new AppDialog(this, this.remote_obj);

    JLabel label = new JLabel();
    label.setText("REMOTE CONTROL FUNCTIONS");
    label.setFont(new Font("segoe ui", Font.BOLD, 14));
    label.setBounds(90, 20, 510, 30);
    this.add(label);

    // TODO: Function - Process Running
    Button button1 = new Button("LIST PROCESS RUNNING");
    button1.setBounds(85, 50, 220, 30);
    button1.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        processMousePressed(e);
      }
    });
    this.add(button1);

    // TODO: Function - App Running
    Button button2 = new Button("LIST APP RUNNING");
    button2.setBounds(85, 100, 220, 30);
    button2.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        appMousePressed(e);
      }
    });
    this.add(button2);

    // TODO: Function - Edit registry
    Button button3 = new Button("LIST REGISTRY");
    button3.setBounds(85, 250, 220, 30);
    this.add(button3);

    // TODO: Function - Keystroke
    Button button4 = new Button("KEYSTROKE");
    button4.setBounds(85, 200, 220, 30);
    this.add(button4);

    // TODO: Function - Screenshot
    Button button5 = new Button("SCREENSHOT");
    button5.setBounds(85, 150, 220, 30);
    this.add(button5);

    // TODO: Function - Shutdown
    Button button6 = new Button("SHUTDOWN");
    button6.setBounds(85, 300, 220, 30);
    this.add(button6);
  }

  @Override
  public void run() {

  }

  @Override
  public void dispose() {
    try {
      super.setVisible(false);
      super.dispose();
      this.client_panel.setEnabled(true);
      this.common_bus.getRmiClient().setRemoteServer(false);
      this.common_bus.getTcpClient().setConnectedServer(false);
      this.common_bus.getTcpClient().getClient().close();
      if(!this.screen_thread.isInterrupted())
        this.screen_thread.isInterrupted();
    }
    catch(Exception exception) {
      JOptionPane.showMessageDialog(null, "Can't close connection");
    }
  }


//  public static void main(String[] args) throws RemoteException {
////    MainMenu menu = new MainMenu();
//
//  }

  //TODO: get process info of server
  private void processMousePressed(MouseEvent e) {
    if(e.getButton() == MouseEvent.BUTTON1) {
      this.dialog_process.setVisible(true);
    }
  }

  //TODO: get app info of server
  private void appMousePressed(MouseEvent e) {
    if(e.getButton() == MouseEvent.BUTTON1) {
      this.dialog_app.setVisible(true);
    }
  }

}

package FE;

import BE.Common.CommonBus;
import BE.RMI.IRemoteDesktop;
import FE.Function.AppDialog;
import FE.Function.ProcessDialog;
import FE.Function.ScreenshotDialog;
import FE.Function.ShutdownDialog;
import FE.Function.HardwareDialog;
import FE.Panel.ClientPanel;

import javax.swing.*;
import java.awt.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;


public class MainMenu extends JFrame implements Runnable {
  public final static int WIDTH_DIALOG = 380;
  public final static int HEIGHT_DIALOG = 380;
  private ClientPanel client_panel;
  private CommonBus common_bus;
  private IRemoteDesktop remote_obj;

  private HardwareDialog dialog_hardware;
  private ProcessDialog dialog_process;
  private AppDialog dialog_app;
//  private KeystrokeDialog dialog_keystroke;
  private ScreenshotDialog dialog_screenshot;
  private ShutdownDialog dialog_shutdown;

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
    this.dialog_hardware = new HardwareDialog(this, this.remote_obj);
    this.dialog_process = new ProcessDialog(this, this.remote_obj);
    this.dialog_app = new AppDialog(this, this.remote_obj);
//    this.dialog_keystroke = new KeystrokeDialog(this, this.remote_obj);
    this.dialog_screenshot = new ScreenshotDialog(this, this.remote_obj);
    this.dialog_shutdown = new ShutdownDialog(this, this.remote_obj);

    JLabel label = new JLabel();
    label.setText("REMOTE CONTROL FUNCTIONS");
    label.setFont(new Font("segoe ui", Font.BOLD, 14));
    label.setBounds(90, 20, 510, 30);
    this.add(label);

    // TODO: Function - Hardware Information
    Button button0 = new Button("HARDWARE INFORMATION");
    button0.setBounds(85, 50, 220, 30);
    button0.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        menuMonitorMousePressed(e);
      }
    });
    this.add(button0);

    // TODO: Function - Process Running
    Button button1 = new Button("LIST PROCESS RUNNING");
    button1.setBounds(85, 100, 220, 30);
    button1.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        processMousePressed(e);
      }
    });
    this.add(button1);

    // TODO: Function - App Running
    Button button2 = new Button("LIST APP RUNNING");
    button2.setBounds(85, 150, 220, 30);
    button2.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        appMousePressed(e);
      }
    });
    this.add(button2);

    // TODO: Function - Edit registry
    Button button3 = new Button("LIST REGISTRY");
    button3.setBounds(85, 200, 220, 30);
    this.add(button3);

    // TODO: Function - Keystroke
    Button button4 = new Button("KEYSTROKE");
    button4.setBounds(85, 250, 220, 30);
//    button4.addMouseListener(new MouseAdapter() {
//      @Override
//      public void mousePressed(MouseEvent e) {
//        keyStrokeMousePressed(e);
//      }
//    });
    this.add(button4);

    // TODO: Function - Screenshot
    Button button5 = new Button("SCREENSHOT");
    button5.setBounds(85, 250, 220, 30);
    button5.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        screenshotMousePressed(e);
      }
    });
    this.add(button5);

    // TODO: Function - Shutdown
    Button button6 = new Button("SHUTDOWN");
    button6.setBounds(85, 300, 220, 30);
    button6.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        shutdownMousePressed(e);
      }
    });
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

  // TODO: get hardware info of server
  private void menuMonitorMousePressed(MouseEvent e) {
    if(e.getButton() == MouseEvent.BUTTON1) {
      this.dialog_hardware.setVisible(true);
    }
  }

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

  //TODO: get keystroke of server
//  private void keyStrokeMousePressed(MouseEvent e) {
//    if(e.getButton() == MouseEvent.BUTTON1) {
//      this.dialog_keystroke.setVisible(true);
//    }
//  }

  // TODO: get screenshot of server
  private void screenshotMousePressed(MouseEvent e) {
    if(e.getButton() == MouseEvent.BUTTON1) {
      this.dialog_screenshot.initComponents();
      this.dialog_screenshot.setVisible(true);
    }
  }

  //TODO: get server shutdown
  private void shutdownMousePressed(MouseEvent e) {
    if(e.getButton() == MouseEvent.BUTTON1) {
      this.dialog_shutdown.initComponents();
    }
  }

}

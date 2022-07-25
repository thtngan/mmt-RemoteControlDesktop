package FE.Function;

import BE.RMI.IRemoteDesktop;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class ProcessDialog extends JDialog implements Runnable{
  public final static int WIDTH_DIALOG = 480;
  public final static int HEIGHT_DIALOG = 380;

  private JScrollPane process_scroll;

  private IRemoteDesktop remote_obj;
  private Thread update_thread;

  private ArrayList<String> listProcess;


  public ProcessDialog(JFrame owner, IRemoteDesktop remote_obj) throws RemoteException {
    super(owner);
    this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    this.setTitle("PROCESS INFORMATION");
    this.setResizable(false);
    this.getContentPane().setPreferredSize(new Dimension(ProcessDialog.WIDTH_DIALOG, ProcessDialog.HEIGHT_DIALOG));
    this.setLayout(null);
    this.pack();

    this.remote_obj = remote_obj;
    this.listProcess = this.remote_obj.getListProcess();

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
    String[] array = this.listProcess.toArray(new String[0]);
    JList list = new JList(array);

    this.process_scroll = new JScrollPane(list);
    this.process_scroll.setBounds(20, 80, 450, 240);
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

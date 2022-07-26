package FE.Function;

import BE.RMI.IRemoteDesktop;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class AppDialog extends JDialog implements Runnable{
  public final static int WIDTH_DIALOG = 480;
  public final static int HEIGHT_DIALOG = 380;

  private JScrollPane process_scroll;

  private IRemoteDesktop remote_obj;
  private Thread update_thread;

  private String app;


  public AppDialog(JFrame owner, IRemoteDesktop remote_obj) throws RemoteException {
    super(owner);
    this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    this.setTitle("PROCESS INFORMATION");
    this.setResizable(false);
    this.getContentPane().setPreferredSize(new Dimension(ProcessDialog.WIDTH_DIALOG, ProcessDialog.HEIGHT_DIALOG));
    this.setLayout(null);
    this.pack();

    this.remote_obj = remote_obj;
    this.app = this.remote_obj.getAppList();

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
    label.setText("APPLICATION INFORMATION");
    label.setFont(new Font("segoe ui", Font.BOLD, 14));
    label.setBounds(150, 20, 500, 30);
    this.add(label);

    // TODO: list app
    System.out.println("PROCESS");
    System.out.println(this.app);

    String rows[] = this.app.split("\n");


    Vector<Vector<String>> dataVector = new Vector<Vector<String>>();
    for (String row : rows) {
      row = row.trim();  //UPDATE
      Vector<String> data = new Vector<String>();
      data.addAll(Arrays.asList(row.split("\\s+")));
      dataVector.add(data);
    }

    //remove redundant
    dataVector.remove(0);
    dataVector.remove(0);
    dataVector.remove(0);


    Vector<String> header = new Vector<String>(2);
    header.add("Image Name");
    header.add("PID");
    header.add("Session Name");
    header.add("Session#");
    header.add("Mem Usage");

    TableModel model = new DefaultTableModel(dataVector, header);
    JTable table = new JTable(model);
//    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    this.process_scroll =  new JScrollPane(table);
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


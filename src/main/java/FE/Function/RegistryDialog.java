package FE.Function;

import BE.RMI.IRemoteDesktop;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class RegistryDialog extends JDialog implements Runnable{
  public final static int WIDTH_DIALOG = 470;
  public final static int HEIGHT_DIALOG = 430;

  private JList list;

  private IRemoteDesktop remote_obj;
  private Thread update_thread;

  private ArrayList<String> listReg = new ArrayList<>();

  public RegistryDialog(JFrame owner, IRemoteDesktop remote_obj)  {
    super(owner);
    this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    this.setTitle("REGISTRY INFORMATION");
    this.setResizable(true);
    this.getContentPane().setPreferredSize(new Dimension(ProcessDialog.WIDTH_DIALOG, ProcessDialog.HEIGHT_DIALOG));
    this.setLayout(null);
    this.pack();

    this.remote_obj = remote_obj;
//    this.process = this.remote_obj.getProcessList();
//    this.initComponents();

    // TODO: start graph
    this.update_thread = new Thread(this);
    this.update_thread.setDaemon(true);
    this.update_thread.start();
  }

  public void initComponents() {
//    this.process = this.remote_obj.getProcessList();
    // TODO: label
    JLabel label = new JLabel();
    label.setText("REGISTRY INFORMATION");
    label.setFont(new Font("segoe ui", Font.BOLD, 14));
    label.setBounds(150, 20, 500, 30);
    this.add(label);

    // TODO: list registry
//    System.out.println("REGISTRY");
//    System.out.println(this.process);

    Label location = new Label("Place:");
    location.setBounds(20, 50, 50, 30);
    this.add(location);

    TextField txtLocation = new TextField("Enter location");
    txtLocation.setBounds(80, 50, 370, 30);
    this.add(txtLocation);

    Label key = new Label("Key:");
    key.setBounds(20, 90, 50, 30);
    this.add(key);

    TextField txtKey = new TextField("Enter key");
    txtKey.setBounds(80, 90, 370, 30);
    this.add(txtKey);

    Label valKey = new Label("Value:");
    valKey.setBounds(20, 130, 50, 30);
    this.add(valKey);

    TextField txtVal = new TextField("");
    txtVal.setBounds(80, 130, 370, 30);
    this.add(txtVal);

    Button btnCreate = new Button("CREATE");
    btnCreate.setBounds(20, 170, 100,30);
    btnCreate.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        createUpdateReg(txtLocation.getText(), txtKey.getText(), txtVal.getText());
      }
    });
    this.add(btnCreate);

    Button btnRead = new Button("READ");
    btnRead.setBounds(130, 170, 100,30);
    btnRead.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        listReg(txtLocation.getText(), txtKey.getText());
      }
    });
    this.add(btnRead);

    Button btnUpdate = new Button("UPDATE");
    btnUpdate.setBounds(240, 170, 100,30);
    btnUpdate.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        createUpdateReg(txtLocation.getText(), txtKey.getText(), txtVal.getText());
      }
    });
    this.add(btnUpdate);

    Button btnDelete = new Button("DELETE");
    btnDelete.setBounds(350, 170, 100,30);
    btnDelete.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        delReg(txtLocation.getText(), txtKey.getText());
      }
    });
    this.add(btnDelete);


    // TODO: add list registry
    String str = "";
    DefaultListModel<String> model = new DefaultListModel<>();
    model.addElement(str);
    this.list = new JList<>( model );
    list.setBounds(20, 210, 430, 420);
    this.add(list);

  }

//  public static void main(String[] args) {
//    RegistryDialog reg = new RegistryDialog();
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

  private void listReg(String keyPath, String keyName) {


    try {
      String reg = this.remote_obj.getRegistryList(keyPath, keyName);
      this.listReg.add(reg);

      DefaultListModel<String> model = new DefaultListModel<>();
      for (int i = 0; i < this.listReg.size(); i++) {
        model.add(i, this.listReg.get(i));

      }
      this.remove(this.list);
      this.list = new JList<>( model );
      this.list.setBounds(20, 210, 430, 420);
      this.add(this.list);

    } catch (RemoteException e) {
      e.printStackTrace();
    }




  }

  private void createUpdateReg(String keyPath, String keyName, String keyValue) {
//    try {
//      Process writer = Runtime.getRuntime().exec(
//          "reg add " + keyPath + " /t REG_SZ /v \"" + keyName + "\" /d "
//              + keyValue + " /f");
//    } catch (IOException e) {
//      e.printStackTrace();
//    }

    try {
      boolean flag = this.remote_obj.createRegistry(keyPath, keyName, keyValue);

      if (flag == true) {
        JOptionPane.showMessageDialog(this,"Successfully");
      }
      else {
        JOptionPane.showMessageDialog(this,"Fail.","Alert",JOptionPane.WARNING_MESSAGE);
      }
    } catch (RemoteException e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(this,"Error.","Alert",JOptionPane.WARNING_MESSAGE);
    }
  }

  private void delReg(String keyPath, String keyName) {
    try {
      boolean flag = this.remote_obj.delRegistry(keyPath, keyName);

      if (flag == true) {
        JOptionPane.showMessageDialog(this,"Successfully");
      }
      else {
        JOptionPane.showMessageDialog(this,"Fail.","Alert",JOptionPane.WARNING_MESSAGE);
      }
    } catch (RemoteException e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(this,"Error.","Alert",JOptionPane.WARNING_MESSAGE);
    }
  }

}

package FE.Panel;

import BE.Common.CommonBus;
import FE.Common.CommonLabel;
import FE.Common.CommonPanel;
import FE.Function.MainMenu;
import FE.Information.RemoteFrame;
import FE.MainFrame;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ClientPanel extends JPanel {
  public final static String BACKGROUND = "0xfefefd";
  public final static String FOREGROUND = "0x000000";

  private CommonPanel main_panel;
  private CommonLabel connect_label;
  private JLabel loader_label;

  private CommonBus common_bus;

  public ClientPanel(CommonBus common_bus) {
    // TODO: style ClientPanel
    this.setLocation(0, MainFrame.HEIGHT_TASKBAR);
    this.setSize(MainFrame.WIDTH_FRAME, MainFrame.HEIGHT_FRAME - MainFrame.HEIGHT_TASKBAR);
    this.setBackground(Color.decode(ClientPanel.BACKGROUND));
    this.setLayout(null);

    // TODO: class for handle events
    this.common_bus = common_bus;

    // TODO: add components
    this.initComponents();
  }

  private void initComponents() {
    // TODO: constructor
    this.main_panel = new CommonPanel();
    this.connect_label = new CommonLabel();
    this.loader_label = new JLabel();

    // TODO: style main panel
    this.main_panel.setBorder(BorderFactory.createTitledBorder(null, "CONNECT TO SERVER",
        TitledBorder.DEFAULT_JUSTIFICATION,
        TitledBorder.DEFAULT_POSITION,
        new Font("SEGOE UI", Font.BOLD, 16),
        Color.decode(ClientPanel.FOREGROUND))
    );
    this.add(this.main_panel);

    // TODO: style host_label
    this.main_panel.getHostLabel().setText("IP:");

    // TODO: style host_text
    this.main_panel.getHostCombo().setVisible(false);
    this.main_panel.getHostText().setVisible(true);

    // TODO: style port_label
    this.main_panel.getPortLabel().setText("Port:");

    // TODO: style connect_label
    this.connect_label.setText("CONNECT NOW");
    this.connect_label.setBounds(150, 270, 115, 40);
    this.connect_label.setForeground(Color.decode(ClientPanel.FOREGROUND));
    this.connect_label.setFont(new Font("SEGOE UI", Font.BOLD, 15));
    Border BLACKLINE = BorderFactory.createLineBorder(Color.black);
    this.connect_label.setBorder(BLACKLINE);
    this.connect_label.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        connectLabelMousePressed(e);
      }
    });
    this.add(this.connect_label);


    // TODO: style loader_label
    this.loader_label.setBounds(340, 307, 16, 16);
    this.loader_label.setVisible(false);
    this.add(this.loader_label);
  }

  @Override
  public void setEnabled(boolean b) {
    this.main_panel.setEnabled(b);
    this.connect_label.setEnabled(b);
  }

  private boolean isFormatIpv4(String host) {
    int count = 0;
    for(int i = 0; i < host.length(); ++i) {
      if(host.charAt(i) == '.') ++count;
    }
    // TODO: count = 3 for ipv4
    // TODO: count = 0 for host name
    return count == 3 || count == 0;
  }

  // TODO: handle events of connect_label
  private void connectLabelMousePressed(MouseEvent e) {
    if(e.getButton() == MouseEvent.BUTTON1 && this.connect_label.isEnabled()) {
      this.setEnabled(false);
      this.loader_label.setVisible(true);

      Thread connect_thread = new Thread(() -> {
        try {
          String host = this.main_panel.getHostText().getText().trim();
          int port = Integer.parseInt(this.main_panel.getPortText().getText().trim());
          String password = "123456";

          if(!this.isFormatIpv4(host)) throw new Exception("Wrong format IPV4");

          this.common_bus.startConnectingToServer(host, port, password);
          System.out.println("aa");

          // TODO: show remote screen
          EventQueue.invokeLater(() -> {
            try {
                new RemoteFrame(this, this.common_bus, "png");
                new MainMenu(this, this.common_bus);
            }
            catch(Exception exception) {
              JOptionPane.showMessageDialog(this, "Can't start remoting to server:\n" + exception.getMessage());
            }
          });
        }
        catch(Exception exception) {
          System.out.println(exception);
          JOptionPane.showMessageDialog(this, "Can't connect to server:\n" + exception.getMessage());
        }
        this.setEnabled(true);
        this.loader_label.setVisible(false);
      });
      connect_thread.setDaemon(true);
      connect_thread.start();
    }
  }
}


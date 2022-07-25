package FE.Common;

import FE.MainFrame;
import FE.Panel.ClientPanel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class CommonPanel extends JPanel {
  private JLabel host_label;
  private JComboBox<String> host_combo;
  private JTextField host_text;
  private JLabel port_label;
  private JTextField port_text;
  private JTextField pass_text;
  private JPasswordField pass_field;
  private JLabel help_label;

  public CommonPanel() {
    // TODO: style common panel
    this.setBorder(BorderFactory.createTitledBorder(null, "Server Listening",
        TitledBorder.DEFAULT_JUSTIFICATION,
        TitledBorder.DEFAULT_POSITION,
        new Font("SEGOE UI", Font.BOLD, 16),
        Color.decode(ClientPanel.FOREGROUND))
    );
    this.setBounds(50, 30, MainFrame.WIDTH_FRAME - 100, 230);
    this.setBackground(Color.decode(ClientPanel.BACKGROUND));
    this.setForeground(Color.decode(ClientPanel.FOREGROUND));
    this.setLayout(null);

    // TODO: add components
    this.initComponents();
  }

  private void initComponents() {
    // TODO: constructor
    this.host_label = new JLabel();
    this.host_combo = new JComboBox<>();
    this.host_text = new JTextField();
    this.port_label = new JLabel();
    this.port_text = new JTextField();
    this.pass_text = new JTextField();
    this.pass_field = new JPasswordField();
    this.help_label = new JLabel();

    // TODO: style host_label
    this.host_label.setText("Server IP:");
    this.host_label.setBounds(30, 30, 100, 30);
    this.host_label.setFont(new Font("SEGOE UI", Font.BOLD, 14));
    this.add(this.host_label);

    // TODO: style host_combo for server panel
    this.host_combo.setBounds(140, 35, 130, 20);
    this.add(this.host_combo);

    // TODO: style host_text for client panel
    this.host_text.setBounds(140, 35, 130, 20);
    this.host_text.setVisible(false);
    this.add(this.host_text);

    // TODO: style port_label
    this.port_label.setText("Server port:");
    this.port_label.setBounds(30, 60, 100, 30);
    this.port_label.setFont(new Font("SEGOE UI", Font.BOLD, 14));
    this.add(this.port_label);

    // TODO: style port_text
    this.port_text.setBounds(140, 65, 130, 20);
    this.port_text.setText("9999");
    this.add(this.port_text);
  }

  @Override
  public void setEnabled(boolean b) {
    this.host_combo.setEnabled(b);
    this.host_text.setEnabled(b);
    this.pass_field.setEnabled(b);
    this.pass_text.setEnabled(b);
    this.port_text.setEnabled(b);
  }

  public JLabel getHostLabel() {
    return host_label;
  }

  public JComboBox<String> getHostCombo() {
    return host_combo;
  }

  public JTextField getHostText() {
    return host_text;
  }

  public JLabel getPortLabel() {
    return port_label;
  }

  public JTextField getPortText() {
    return port_text;
  }

  public JLabel getHelpLabel() {
    return help_label;
  }
}

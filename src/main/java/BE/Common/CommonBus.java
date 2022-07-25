package BE.Common;

import BE.RMI.RMIClient;
import BE.RMI.RMIServer;
import BE.TCP.TCPClient;
import BE.TCP.TCPServer;

import java.awt.*;
import java.io.IOException;
import java.rmi.NotBoundException;

public class CommonBus {
  // TODO: for server
  private TCPServer tcp_server;
  private RMIServer rmi_server;

  // TODO: for client
  private TCPClient tcp_client;
  private RMIClient rmi_client;

  public CommonBus() {
    this.rmi_server = new RMIServer();
    this.rmi_client = new RMIClient();
  }

  public TCPServer getTCPServer() {
    return this.tcp_server;
  }

  public void setMain() {
    this.tcp_server = new TCPServer();
    this.tcp_client = new TCPClient();
  }

  public RMIServer getRmiServer() {
    return this.rmi_server;
  }

  public TCPClient getTcpClient() {
    return this.tcp_client;
  }

  public RMIClient getRmiClient() {
    return this.rmi_client;
  }

  // TODO: handle events of server
  public void startListeningOnServer(String host, int port, String password) throws IOException, AWTException {
    if(!this.tcp_server.isListening() && !this.rmi_server.isBinding()) {
      this.tcp_server.startListeningOnTcpServer(host, port, password);
      this.rmi_server.startBindingOnRmiServer(host, port + 1);
    }
  }

  public void stopListeningOnServer() throws IOException, NotBoundException {
    if(this.tcp_server.isListening() && this.rmi_server.isBinding()) {
      this.tcp_server.stopListeningOnTcpServer();
      this.rmi_server.stopBindingOnRmiServer();
    }
  }

  // TODO: handle event in client
  public void startConnectingToServer(String host, int port, String password) throws Exception {
    // TODO: check server is listening?
    if(this.tcp_server.isListening()) {
      String ip_server = this.tcp_server.getServer().getInetAddress().getHostAddress();
      if(host.equals(ip_server)) throw new Exception("Can't remote yourself!");
      System.out.println(ip_server);
      System.out.println(host);
    }
    if(this.tcp_client.isConnectedServer()) throw new Exception("You are remoting!");
    this.tcp_client.startConnectingToTcpServer(host, port, password);
    this.rmi_client.startConnectingToRmiServer(host, port + 1);
  }
}

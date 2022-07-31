package BE.RMI;

import FE.Information.ComputerInfo;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IRemoteDesktop extends Remote {
  // TODO: for client remote mouse of server
  void mouseMovedServer(int x, int y) throws RemoteException;
  void mousePressedServer(int buttons) throws RemoteException;
  void mouseReleasedServer(int buttons) throws RemoteException;
  void mouseWheelServer(int wheel_amt) throws RemoteException;

  // TODO: for client remote keyboard of server
  void keyPressedServer(int keycode) throws RemoteException;
  void keyReleasedServer(int keycode) throws RemoteException;

  // TODO: for get hardware info of server
  double getCpuLoadServer() throws RemoteException;
  double getRamUsageServer() throws RemoteException;
  long[] getRamMemories() throws RemoteException;
  int getCpus() throws RemoteException;
  ComputerInfo getComputerInformation() throws RemoteException;

  // TODO: for get process of server
  String getProcessList() throws RemoteException;
  boolean createNewProcess(String name) throws RemoteException;
  boolean killProcess(String name) throws RemoteException;

  // TODO: for get app of server
  String getAppList() throws RemoteException;

  // TODO: for get screenshot of server
  byte[] takeScreenshotServer(String quality) throws Exception;

  // TODO: for get registry of server
  boolean createRegistry(String keyPath, String keyName, String keyValue) throws RemoteException;
  boolean delRegistry(String keyPath, String keyName) throws RemoteException;
  String getRegistryList(String keyPath, String keyName) throws RemoteException;

  // TODO: for key press of server
  ArrayList<String> getKeystroke(ArrayList<String> keyList) throws RemoteException;

  //TODO: for shutdown server
  boolean shutdownServer() throws RemoteException;
}

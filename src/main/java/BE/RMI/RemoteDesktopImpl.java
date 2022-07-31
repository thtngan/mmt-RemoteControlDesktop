package BE.RMI;

import FE.Information.ComputerInfo;
import FE.Information.DriveInfo;
import com.sun.management.OperatingSystemMXBean;
import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;


import javax.imageio.ImageIO;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import java.util.Date;
import java.util.Optional;

public class RemoteDesktopImpl extends UnicastRemoteObject implements IRemoteDesktop {
  public final static int GB = 1024 * 1024 * 1024;

  private Robot mr_robot;
  private OperatingSystemMXBean os;
//  private K


  public RemoteDesktopImpl() throws RemoteException, AWTException {
    super();
    this.mr_robot = new Robot();
    this.os = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
  }

  @Override
  public byte[] takeScreenshotServer(String quality) throws Exception {
    Dimension screen_size = Toolkit.getDefaultToolkit().getScreenSize();
    Rectangle bounds = new Rectangle(screen_size);
    BufferedImage screenshot = this.mr_robot.createScreenCapture(bounds);
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ImageIO.setUseCache(false); // TODO: not using disk cache (using ram)
    ImageIO.write(screenshot, quality, bos);
    return bos.toByteArray();
  }

  // TODO: mouse
  @Override
  public void mouseMovedServer(int x, int y) throws RemoteException {
    this.mr_robot.mouseMove(x, y);
  }

  @Override
  public void mousePressedServer(int buttons) throws RemoteException {
    this.mr_robot.mousePress(buttons);
  }

  @Override
  public void mouseReleasedServer(int buttons) throws RemoteException {
    this.mr_robot.mouseRelease(buttons);
  }

  @Override
  public void mouseWheelServer(int wheel_amt) throws RemoteException {
    this.mr_robot.mouseWheel(wheel_amt);
  }

  // TODO: keyboard
  @Override
  public void keyPressedServer(int keycode) throws RemoteException {
    this.mr_robot.keyPress(keycode);
  }

  @Override
  public void keyReleasedServer(int keycode) throws RemoteException {
    this.mr_robot.keyRelease(keycode);
  }

  // TODO: for get hardware info

  @Override
  public double getCpuLoadServer() throws RemoteException {
    return this.os.getProcessCpuLoad();
  }

  @Override
  public double getRamUsageServer() throws RemoteException {
    double ratio = (double) (this.os.getTotalPhysicalMemorySize() - this.os.getFreePhysicalMemorySize()) / this.os.getTotalPhysicalMemorySize();
    return ratio;
  }

  @Override
  public long[] getRamMemories() throws RemoteException {
    return new long[] {
        1 + this.os.getTotalPhysicalMemorySize() / RemoteDesktopImpl.GB,
        1 + this.os.getTotalSwapSpaceSize() / RemoteDesktopImpl.GB
    };
  }

  @Override
  public int getCpus() throws RemoteException {
    return this.os.getAvailableProcessors();
  }

  @Override
  public ComputerInfo getComputerInformation() throws RemoteException {
    ComputerInfo pc_info = new ComputerInfo(this.os.getName());
    for(File file : File.listRoots()) {
      pc_info.getDrives().add(
          new DriveInfo(
              FileSystemView.getFileSystemView().getSystemDisplayName(file),
              file.getFreeSpace() / RemoteDesktopImpl.GB,
              file.getTotalSpace() / RemoteDesktopImpl.GB
          )
      );
    }
    return pc_info;
  }

  @Override
  public String getProcessList() throws RemoteException {
    String str = "";
    try {
      Process p = Runtime.getRuntime().exec("tasklist");
      BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));

      String line;
      StringBuilder stringBuilder = new StringBuilder();

      while ((line = input.readLine()) != null) {
        stringBuilder.append(line);
        stringBuilder.append("\n");
      }

      str = stringBuilder.toString();

      System.out.println(str);
    } catch (Exception err) {
      err.printStackTrace();
    }
    return str;
  }

  @Override
  public boolean createNewProcess(String name) throws RemoteException {
    try {
      Runtime.getRuntime().exec("powershell " + "start " + name + ".exe");
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  @Override
  public boolean killProcess(String name) throws RemoteException {
    try {
      Runtime.getRuntime().exec("taskkill /F /PID " + name);
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  @Override
  public String getAppList() throws RemoteException {
    String str = "";
    try {
      Process p = Runtime.getRuntime().exec("powershell \"gps | where {$_.MainWindowTitle } | select id, name");
      BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));

      String line;
      StringBuilder stringBuilder = new StringBuilder();

      while ((line = input.readLine()) != null) {
        stringBuilder.append(line);
        stringBuilder.append("\n");
      }

      str = stringBuilder.toString();

      System.out.println(str);
    } catch (Exception err) {
      err.printStackTrace();
    }
    return str;
  }

  @Override
  public boolean createRegistry(String keyPath, String keyName, String keyValue) throws RemoteException {
    try {
      Process writer = Runtime.getRuntime().exec(
            "reg add " + keyPath + " /t REG_SZ /v \"" + keyName + "\" /d "
                + keyValue + " /f" );
    } catch (IOException e) {
      return false;
    }
    return true;
  }

  @Override
  public boolean delRegistry(String keyPath, String keyName) throws RemoteException {
    try {
      Process writer = Runtime.getRuntime().exec("reg delete " + keyPath + " /v " + keyName + " /f");
    } catch (IOException e) {
      return false;
    }
    return true;
  }

  @Override
  public String getRegistryList(String keyPath, String keyName) throws RemoteException {
    String str = "";
    try {
      Process p = null;
      if (!keyName.isEmpty() || !keyName.equals("")) {
        p = Runtime.getRuntime().exec("reg query " +
            '"'+ keyPath + "\" /v " + keyName);
      }
      else {
        p = Runtime.getRuntime().exec("reg query " +
            '"'+ keyPath + "\" ");
      }

      BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));

      String line;
      StringBuilder stringBuilder = new StringBuilder();

      while ((line = input.readLine()) != null) {
        stringBuilder.append(line);
        stringBuilder.append("\n");
      }

      str = stringBuilder.toString();
      System.out.println(keyPath);
      System.out.println(keyName);
      System.out.println(str);
    } catch (Exception err) {
      err.printStackTrace();
    }
    return str;
  }

  @Override
  public ArrayList<String> getKeystroke(ArrayList<String> keyList) throws RemoteException {
    ArrayList<String> a = new ArrayList<>();
    GlobalKeyboardHook keyboardHook = new GlobalKeyboardHook(true);
    keyboardHook.addKeyListener(new GlobalKeyAdapter() {
      @Override
      public void keyPressed(GlobalKeyEvent event) {
        System.out.println(new Date() + " " + event.getKeyChar());
        a.add(String.valueOf(event.getKeyChar()));
      }

    });
    System.out.println("Keyy" + a.toString());

    return a;
  }

  @Override
  public boolean shutdownServer() throws RemoteException {
    try {
      Runtime.getRuntime().exec("shutdown -s -t 0");
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

}

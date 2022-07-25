package BE.RMI;

import FE.Function.MainMenu;
import FE.Function.User32;
import FE.Information.ComputerInfo;
import FE.Information.DriveInfo;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.management.OperatingSystemMXBean;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.management.ManagementFactory;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RemoteDesktopImpl extends UnicastRemoteObject implements IRemoteDesktop {
  public final static int GB = 1024 * 1024 * 1024;

  private Robot mr_robot;
  private OperatingSystemMXBean os;


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
  public ArrayList<String> getListProcess() throws RemoteException {
    ArrayList<String> list = new ArrayList<>();
    ProcessHandle.allProcesses()
        .forEach(process ->
            list.add(processDetails(process))
        );
    return list;
  }

  private String processDetails(ProcessHandle process) {
    return String.format("%8s %8s %30s %10s",
        process.pid(),
        text(process.info().user()),
        text(process.info().startInstant()),
        text(process.info().command()));
  }

  private static String text(Optional<?> optional) {
    return optional.map(Object::toString).orElse("-");
  }


  @Override
  public ArrayList<String> getListApp() {
    final ArrayList<String> windowNames = new ArrayList<String>();
    final User32 user32 = User32.INSTANCE;
    user32.EnumWindows(new User32.WNDENUMPROC() {
      @Override
      public boolean callback(Pointer hWnd, Pointer arg) {
        byte[] windowText = new byte[512];
        IntByReference pidPointer = new IntByReference();

        user32.GetWindowTextA(hWnd, windowText, 512);
        user32.GetWindowThreadProcessId(hWnd, pidPointer);
        int pid = pidPointer.getValue();

        String wText = Native.toString(windowText).trim();
        if (!wText.isEmpty()) {
          String wTexts = Native.toString(windowText).trim() + " - " + String.valueOf(pid);
          windowNames.add(wTexts);
        }
        return true;
      }
    }, null);

    return windowNames;
  }

}

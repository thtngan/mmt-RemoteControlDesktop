package FE.Function;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;

public interface User32 {
  User32 INSTANCE = (User32) Native.loadLibrary("user32", User32.class);

  interface WNDENUMPROC extends StdCallLibrary.StdCallCallback {
    boolean callback(Pointer hWnd, Pointer arg);
  }

  boolean EnumWindows(WNDENUMPROC lpEnumFunc, Pointer userData);
  int GetWindowTextA(Pointer hWnd, byte[] lpString, int nMaxCount);
  int GetWindowThreadProcessId(Pointer hWnd, IntByReference lpString);
}

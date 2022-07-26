package FE.Panel;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Test {
  public static void main (String[] args) {
    BufferedReader input = null;
    try {
      Process p = Runtime.getRuntime().exec("reg query " + '"'+ "HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\"
          + "Explorer\\Shell Folders");
      input = new BufferedReader(new InputStreamReader(p.getInputStream()));

      String line;
      StringBuilder stringBuilder = new StringBuilder();

      while ((line = input.readLine()) != null) {
        stringBuilder.append(line);
        stringBuilder.append("\n");
      }

      String str = stringBuilder.toString();

      System.out.println(str);
    } catch (Exception err) {
      err.printStackTrace();
    }
  }
}

package FE.Panel;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Test {
  public static void main (String[] args) {
    BufferedReader input = null;
    try {
      String name = "notepad";
      Process process = Runtime.getRuntime().exec("powershell " + "start " + name + ".exe");
      input = new BufferedReader(new InputStreamReader(process.getInputStream()));

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

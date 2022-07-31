package FE.Panel;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Test {
  public static void main (String[] args) {
    BufferedReader input = null;
    try {
      String location = "HKCU\\Test";
          String key = "ten";
      Process p = Runtime.getRuntime().exec("reg query " +
          '"'+ location + "\" ");
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

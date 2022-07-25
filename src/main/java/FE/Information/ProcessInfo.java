package FE.Information;

import java.io.Serializable;

public class ProcessInfo implements Serializable {
  private long pid;
  private String username;
  private String info;
  private String instant;
  private String totalCPU;

  public ProcessInfo(long pid, String username, String info, String instant, String totalCPU) {
    this.pid = pid;
    this.username = username;
    this.info = info;
    this.instant = instant;
    this.totalCPU = totalCPU;
  }

  public long getPid() {
    return pid;
  }

  public void setPid(long pid) {
    this.pid = pid;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getInfo() {
    return info;
  }

  public void setInfo(String info) {
    this.info = info;
  }

  public String getInstant() {
    return instant;
  }

  public void setInstant(String instant) {
    this.instant = instant;
  }

  public String getTotalCPU() {
    return totalCPU;
  }

  public void setTotalCPU(String totalCPU) {
    this.totalCPU = totalCPU;
  }
}

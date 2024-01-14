package org.huanmin.utils.common.obj.serializable;

import com.sun.management.OperatingSystemMXBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.management.ManagementFactory;

public class MemDisk {
  private static final Logger logger = LoggerFactory.getLogger(MemDisk.class);
  public static void main(String[] args) {
    getMemInfo();
    getDiskInfo();
  }

  public static void getDiskInfo() {
    File[] disks = File.listRoots();
    for (File file : disks) {
      System.out.print(file.getPath() + "    ");
      System.out.print("空闲未使用 = " + file.getFreeSpace() / 1024 / 1024 + "M" + "    "); // 空闲空间
      System.out.print("已经使用 = " + file.getUsableSpace() / 1024 / 1024 + "M" + "    "); // 可用空间
      System.out.print("总容量 = " + file.getTotalSpace() / 1024 / 1024 + "M" + "    "); // 总空间
    }
  }

  public static void getMemInfo() {
    OperatingSystemMXBean mem =
        (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
    long l = mem.getTotalPhysicalMemorySize() / 1024 / 1024;
    System.out.println("总内存大小：" + l + "MB");
    long l1 = mem.getFreePhysicalMemorySize() / 1024 / 1024;
    System.out.println("当前使用内存大小:：" + l1 + "MB");
    long l2 = l - l1;
    System.out.println("当前剩余内存大小:：" + l2 + "MB");
  }
}

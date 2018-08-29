package com.trophonix.xrayshield;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Logs {

  private File parentFile;
  private SimpleDateFormat fileNameFormat;

  private String currentFileName;
  private List<String> messages = new ArrayList<>();

  public Logs(File parentFile, String fileNameFormat) {
    this.parentFile = parentFile;
    this.fileNameFormat = new SimpleDateFormat(fileNameFormat);
  }

  public void push(String message) {
    String fileName = fileNameFormat.format(new Date());
    if (currentFileName == null || !currentFileName.equals(fileName)) {
      currentFileName = fileName;
      save();
      messages.clear();
      messages.add(new SimpleDateFormat(message).format(new Date()));
    }
  }

  public void save() {
    if (!messages.isEmpty()) {
      try {
        File file = new File(parentFile, currentFileName);
        FileWriter fileWriter = new FileWriter(file, true);
        if (!file.exists()) {
          file.getParentFile().mkdirs();
          file.createNewFile();
        }
        for (String message : messages) fileWriter.write(message);
        fileWriter.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

}

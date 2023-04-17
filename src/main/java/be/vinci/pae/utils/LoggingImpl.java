package be.vinci.pae.utils;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggingImpl implements Logging {

  private static Logger logger;


  @Override
  public void readFile() {
    if (logger == null) {
      logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
      FileHandler fileHandler;
      logger.setLevel(Level.ALL);
      try {
        fileHandler = new FileHandler("log.txt");
        SimpleFormatter formatter = new SimpleFormatter();
        fileHandler.setFormatter(formatter);
        logger.addHandler(fileHandler);

      } catch (SecurityException | IOException e) {
        e.printStackTrace();
      }
    }
  }

  public void log(Level level, String msg) {
    readFile();
    logger.log(level, msg);
  }
}

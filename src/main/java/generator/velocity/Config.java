package generator.velocity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.velocity.app.Velocity;

/**
 * 读取 gen.properties
 */
public class Config {

  private static final String PROPERTIES_NAME = "gen.properties";
  private static Properties properties = null;

  public static synchronized void read() {

    if (properties == null) {
      InputStream in;
      try {
        File file = new File(System.getProperty("user.dir") + File.separator + PROPERTIES_NAME);
        if (!file.exists()) {
          in = Config.class.getClassLoader().getResourceAsStream(PROPERTIES_NAME);
        } else {
          in = new FileInputStream(
              System.getProperty("user.dir") + File.separator + PROPERTIES_NAME);
        }
        properties = new Properties();
        properties.load(in);
      } catch (IOException e) {
        System.out.println("读取配置文件: " + PROPERTIES_NAME + "出错");
      }
    }

  }

  public static String getProperty(String pName) {
    read();
    return null == properties.get(pName) ? "" : properties.get(pName).toString();
  }

  /**
   * 获取数据库链接属性
   */
  public static Properties getDatabaseProperty() {
    Properties dp = new Properties();
    dp.put("user", getProperty("user"));
    dp.put("password", getProperty("pwd"));
    dp.put("remarksReporting", getProperty("remarksReporting"));
    dp.put("remarks", getProperty("remarksReporting"));// 设置可以获取remarks
    dp.put("useInformationSchema", getProperty("remarksReporting"));// 设置可以获取tables的remarks
    return dp;
  }

  /**
   * 获取Velocity模板属性
   */
  public static Properties getVelocityProperty() {
    Properties vp = new Properties();
    vp.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
    vp.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");
    vp.setProperty(Velocity.RESOURCE_LOADER, "class");
    vp.setProperty(Velocity.FILE_RESOURCE_LOADER_CACHE, "true");
    vp.setProperty("class.resource.loader.class",
        "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
    return vp;
  }

}

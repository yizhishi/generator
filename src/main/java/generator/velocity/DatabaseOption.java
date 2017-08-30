package generator.velocity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

public class DatabaseOption {

  // 模板文件
  private static final String[] templates = {"domain.vm", "mapper.vm", "mapperInterface.vm"};

  // 输出路径
  private static final String[] paths = {Config.getProperty("domainPackage"),
      Config.getProperty("mapperPackage"), Config.getProperty("mapperPackage")};

  // 生成文件后缀
  private static final String[] fileNames = {".java", "Mapper.xml", "Mapper.java"};

  private List<TableMetadata> getTableMetas() throws ClassNotFoundException, SQLException {

    System.out.println("begin connect database....");

    Class.forName(Config.getProperty("driver"));
    Connection conn =
        DriverManager.getConnection(Config.getProperty("url"), Config.getDatabaseProperty());

    DatabaseMetaData dm = conn.getMetaData();

    List<TableMetadata> tableMetadata = new ArrayList<>();

    ResultSet rs = null;

    String[] tables = Config.getProperty("tables").split(","); // 多个table

    System.out.println("begin parse database....");

    for (int i = 0; i < tables.length; i++) {

      rs = dm.getTables(null, null, tables[i].trim(), new String[] {"TABLE"});

      while (rs.next()) {

        String tbName = rs.getString("TABLE_NAME");
        String tbDesc = rs.getString("REMARKS");
        TableMetadata tmd = new TableMetadata(tbName, tbDesc);

        System.out.println("parsing table: " + tbName);
        String pk = "$";
        ResultSet rsPK = dm.getPrimaryKeys(null, null, tbName);
        while (rsPK.next()) {
          pk = pk + rsPK.getString("COLUMN_NAME") + "$";
        }
        System.out.println("table: " + tbName + "\t\tprimary key: " + pk);

        ResultSet rsCol = dm.getColumns(null, null, tbName, null);
        while (rsCol.next()) {
          tmd.addCol(rsCol.getString("COLUMN_NAME"), rsCol.getString("TYPE_NAME"),
              rsCol.getString("DECIMAL_DIGITS"), rsCol.getString("REMARKS"),
              pk.indexOf("$" + rsCol.getString("COLUMN_NAME") + "$") >= 0);
        }

        tableMetadata.add(tmd);
      }
    }

    conn.close();
    System.out.println("end parse database....");
    return tableMetadata;

  }

  public void wirteFiles() throws IOException, ClassNotFoundException, SQLException {

    // 初始化Velocity模板属性
    Velocity.init(Config.getVelocityProperty());

    // 获取配置文件输出路径
    String outPath = Config.getProperty("outPath");
    if (StringUtils.isEmpty(outPath)) {
      outPath = System.getProperty("user.dir") + File.separator + "output";
    }

    // 获取表信息
    List<TableMetadata> tables = this.getTableMetas();

    VelocityContext context = null;
    FileWriter fileWriter = null;

    // 遍历每张表
    for (TableMetadata table : tables) {

      context = new VelocityContext();
      context.put("meta", table);

      // 遍历每个模板，依次生成实体，mapper.xml，mapper.java
      for (int i = 0; i < templates.length; i++) {

        Template template = Velocity.getTemplate(templates[i]);
        template.setEncoding("utf-8");

        String outPutPath = outPath + File.separator + paths[i] + File.separator;
        String fileName = outPutPath + table.getDomainClassName() + fileNames[i];

        File file = new File(outPutPath);
        if (!file.exists()) {
          file.mkdirs();
        }

        fileWriter = new FileWriter(fileName, false);
        template.merge(context, fileWriter);
        fileWriter.close();

        System.out.println("write File: " + fileName);
      }

    }

  }

}

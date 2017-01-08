package generator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class TableMetadata {

	private List<ColomnMetadata> cols = new ArrayList<ColomnMetadata>();
	private String tableDesc;
	private String tableName;
	private String domainClassName;
	private String domainPackageName;
	private String mapperClassName;
	private String mapperPackageName;

	public TableMetadata(String tableName, String tableDesc) {
		this.tableName = tableName;
		this.tableDesc = tableDesc;
		this.domainClassName = NameParser.getDomainName(tableName);
		this.domainPackageName = Config.getProperty("domainPackage");
		this.mapperClassName = this.domainClassName + "Mapper";
		this.mapperPackageName = Config.getProperty("mapperPackage");
	}

	public void addCol(String colName, String colType, String pec, String desc, boolean isPK) {
		ColomnMetadata col = new ColomnMetadata();
		col.setColName(colName);
		col.setColDesc(StringUtils.isEmpty(desc) ? "" : desc);
		col.setColType(parseDataType(colType, pec));
		col.setColDBType(colType);
		col.setPkFlag(isPK);
		cols.add(col);
	}

	private final String TYPE_FLOAT = "java.math.BigDecimal";
	private final String TYPE_INTEGER = "Integer";
	private final String TYPE_LONG = "Long";
	private final String TYPE_STRING = "String";
	private final String TYPE_DATE = "java.util.Date";
	private final String TYPE_BYTE = "Byte";

	// 转化jdbcType 为javaType
	private String parseDataType(String colType, String digits) {
		String ct;
		if ("VARCHAR".equals(colType) || "CHAR".equals(colType) || "LONGVARCHAR".equals(colType)) {
			ct = TYPE_STRING;
		} else if ("DECIMAL".equals(colType) || "FLOAT".equals(colType) || "DOUBLE".equals(colType)) {
			ct = TYPE_FLOAT;
		} else if ("TINYINT".equals(colType)) {
			ct = TYPE_BYTE;
		} else if ("INTEGER".equals(colType) || "SMALLINT".equals(colType)) {
			ct = TYPE_INTEGER;
		} else if ("BIGINT".equals(colType)) {
			ct = TYPE_LONG;
		} else if ("NUMBER".equals(colType) || "NUMERIC".equals(colType)) {
			if ("0".equals(digits)) {
				ct = TYPE_INTEGER;
			} else {
				ct = TYPE_FLOAT;
			}
		} else if ("DATE".equals(colType) || "TIMESTAMP".equals(colType) || "DATETIME".equals(colType)) {
			ct = TYPE_DATE;
		} else if ("TEXT".equals(colType)) {
			ct = TYPE_STRING;
		} else {
			ct = TYPE_STRING;
		}
		return ct;
	}

	public List<ColomnMetadata> getCols() {
		return cols;
	}

	public String getTableDesc() {
		return tableDesc;
	}

	public String getTableName() {
		return tableName;
	}

	public String getDomainClassName() {
		return domainClassName;
	}

	public String getDomainPackageName() {
		return domainPackageName;
	}

	public String getMapperClassName() {
		return mapperClassName;
	}

	public String getMapperPackageName() {
		return mapperPackageName;
	}

}

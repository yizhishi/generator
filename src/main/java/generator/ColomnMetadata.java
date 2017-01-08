package generator;

public class ColomnMetadata {

	private String colName; // 数据库字段名称
	private String colDesc; // 字段注释
	private String colType; // javaType
	private String fieldName; // 处理过的字段名称
	private String colDBType; // jdbcType
	private String seOperName; // setMethod
	private String geOperName; // getMethod
	private boolean pkFlag; // 是否主键

	public boolean isPkFlag() {
		return pkFlag;
	}

	public void setPkFlag(boolean pkFlag) {
		this.pkFlag = pkFlag;
	}

	public String getGeOperName() {
		return geOperName;
	}

	public void setGeOperName(String geOperName) {
		this.geOperName = geOperName;
	}

	public String getSeOperName() {
		return seOperName;
	}

	public void setSeOperName(String seOperName) {
		this.seOperName = seOperName;
	}

	public String getColName() {
		return colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
		String fn = NameParser.capitalize(colName, false);
		String fnS = "set" + Character.toUpperCase(fn.charAt(0)) + fn.substring(1);
		String fnG = "get" + Character.toUpperCase(fn.charAt(0)) + fn.substring(1);
		this.setFieldName(fn);
		this.setSeOperName(fnS);
		this.setGeOperName(fnG);
	}

	public String getColType() {
		return colType;
	}

	public void setColType(String colType) {

		this.colType = colType;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getColDesc() {
		return colDesc;
	}

	public void setColDesc(String colDesc) {
		this.colDesc = colDesc;
	}

	public String getColDBType() {
		return colDBType;
	}

	public void setColDBType(String colDBType) {
		this.colDBType = colDBType;
	}

}

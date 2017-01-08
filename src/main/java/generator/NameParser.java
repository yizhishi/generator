package generator;

public class NameParser {

	/**
	 * 转化表名，第一个下划线前减除，并改为驼峰命名（首位大写）
	 * 
	 * @param tableName
	 * @return TB_DEMO_DEMO 返回DemoDemo
	 */
	public static String getDomainName(String tableName) {
		int index = tableName.indexOf('_');
		String className = tableName.substring(index + 1);
		return capitalize(className, true);
	}

	/**
	 * 转化字符串
	 * 
	 * @param value
	 *            带转化字符
	 * @param firstUpper
	 *            首位是否大写
	 * @return 转化过的字符
	 */
	public static String capitalize(String value, boolean firstUpper) {
		StringBuilder stringBuilder = new StringBuilder(value.length());
		for (int i = 0; i < value.length(); i++) {
			char c = value.charAt(i);
			if (c != '_') {
				if (i == 0 && firstUpper) {
					stringBuilder.append(Character.toUpperCase(c));
				} else {
					stringBuilder.append(Character.toLowerCase(c));
				}
			} else {
				i++;
				c = value.charAt(i);
				stringBuilder.append(Character.toUpperCase(c));
			}
		}
		return stringBuilder.toString();
	}

}

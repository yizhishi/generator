package gen;

import generator.DatabaseOption;

public class Main {

	/**
	 * 可以打jar包使用，放gen.properties和jar同一目录即可
	 */
	public static void main(String[] args) throws Exception {
		DatabaseOption opt = new DatabaseOption();
		opt.wirteFiles();
		System.out.println("OK!");
	}

}
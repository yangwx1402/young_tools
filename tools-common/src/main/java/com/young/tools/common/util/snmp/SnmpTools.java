package com.young.tools.common.util.snmp;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.young.tools.common.util.backend.CMDUtils;

public class SnmpTools {

	private static final String snmpwalk = "snmpwalk -v 2c -r 3 -c ";
	
	private static final String snmpget = "snmpget -v 2c -r 3 -c ";
	
	public static void coll(String[] coll_fields, String filename,
			String outfilename) throws IOException {
		List<String> collInfos = FileUtils.readLines(new File(filename),
				"utf-8");
		File outFile = new File(outfilename);
		if (collInfos == null || collInfos.size() == 0) {
			return;
		}
		StringBuilder sb = null;
		for (String collInfo : collInfos) {
			sb = snmpwalk(collInfo, coll_fields);
			FileUtils.writeStringToFile(outFile, sb.toString(), "utf-8", true);
		}
	}

	private static StringBuilder snmpwalk(String collInfo, String[] coll_fields)
			throws IOException {
		String[] temp = collInfo.split(",");
		List<String> collResult = null;
		StringBuilder sb = new StringBuilder();
		sb.append(temp[4] + "," + temp[2] + "," + temp[6] + "," + temp[7]);
		for (String field : coll_fields) {
			String command = snmpwalk + temp[5] + " "
					+ temp[4] + " " + field + "." + temp[7];
			collResult = CMDUtils.executeCommand(command);
			if (collResult != null && collResult.size() == 1) {
				sb.append(collResult.get(0) + ",");
			}
		}
		sb.append("\n");
		return sb;
	}
	public static void main(String[] args) throws IOException {
		String fields = args[0];
		String inputFile = args[1];
		String outFile = args[2];
		SnmpTools.coll(fields.split(","), inputFile, outFile);
	}
}

package compressor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import java.util.TreeSet;

public class DataProcess {
	private static final char[] DIC_A = new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't' };
	//private static final char[] DIC_B = new char[] { '~', '!', '%', '^', '&', '*', '(', ')', '|', '[', ']', '{', '}', ';', '\'', '\"', '<', '��', '��', '��', '��', '��', '>' };
	private static final char[] DIC_B = new char[]{'!','"','$','%','&','\'','(',')','*',';','<','=','>','@','[',']','^','{','|','}','~','\\'}; 
	private static final int DIC_B_LENGTH = DIC_B.length;
	// �������
	private static final char END = '`';
	// �������
	private static final String ENDs = "`";
	// һ���ֵ���
	private static final char ONE_DIC_B = '?';

	// �Ʊ��
	private static char TAB = '\t';
	// �Ʊ��
	private static String TABs = "\t";

	public static String[][] readBaseData(int[] lines, String org_filename) throws Exception {
		Arrays.sort(lines);
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(org_filename)));
		String currLine;
		int comment = 0;
		while (((currLine = br.readLine()) != null) && (currLine.startsWith("##"))) {
			comment++;
		}
		for (int i = 0; i < lines.length; i++) {
			lines[i] += comment;
		}
		br.close();
		BufferedReader br1 = new BufferedReader(new InputStreamReader(new FileInputStream(org_filename)));
		String[][] datas = new String[lines.length][];
		int count = 0;
		boolean flag;
		for (int i = 0; i < lines.length; i++) {
			do {
				currLine = br1.readLine();
				count++;
				flag = true;
				if (count == lines[i]) {
					datas[i] = currLine.trim().split(TABs);
					flag = false;
				}
			} while (flag);
		}
		br1.close();
		return datas;
	}

	public static int[] generateIndexInfo(String baseLine) {

		StringBuffer sb = new StringBuffer(baseLine);
		sb.append('\t');
		int len = baseLine.split("\t").length;
		int[] start = new int[len + 1];
		start[0] = 0;
		int[] end = new int[len];
		int j = 0;
		char c;
		for (int i = 0; i < sb.length(); i++) {
			c = sb.charAt(i);
			if (c == '\t') {
				end[j] = i;
				start[j + 1] = i + 1;
				j++;
			}

		}
		return start;
	}

	public static int fenZu(String[] idata, String[][] datas) {
		int max = 0;
		int l = 0;
		int count = 0;
		for (int i = 0; i < datas.length; i++) {
			for (int j = 0; j < idata.length; j++) {
				if (datas[i][j].equals(idata[j]))
					count++;
			}
			if (count >= max) {
				max = count;
				l = i;
			}
			count = 0;
		}
		return l;
	}

	public static String lineCompress2(String[][] basedata, String line) {
		StringBuilder sb = new StringBuilder();
		String[] lineArr = line.split(TABs);
		int type = fenZu(lineArr, basedata);
		sb.append(DIC_A[type]);
		sb.append(" ");
		sb.append(lineCompress(basedata[type], lineArr));
		return sb.toString();
	}

	public static StringBuilder lineCompress(String[] basedata, String[] data) {
		StringBuilder sb = new StringBuilder();
		int count = 0;
		for (int i = 0; i < data.length; i++) {
			if (!basedata[i].equals(data[i])) {
				sb.append(DIC_B[count] + data[i]);
				count = 0;
			} else {
				count++;
			}
			if (count == DIC_B_LENGTH) {
				sb.append(ONE_DIC_B);
				count = 0;
			}
		}
		if (count != 0) {
			sb.append(ENDs + count);
		}
		return sb;
	}

	public static String lineRecover3(String line, String[][] base) {
		int type = line.charAt(0) - 97;
		return toStringForm(lineRecover(line.substring(2), base[type]));
	}

	public static String[] lineRecover2(String line, String[][] base) {
		int type = line.charAt(0) - 97;
		return lineRecover(line.substring(2), base[type]);
	}

	public static String[] lineRecover(String data, String[] base) {
		String[] base_clone = base.clone();
		int base_clone_count = 0;
		int index;
		StringBuilder sb;
		char temp;

		for (int i = 0; i < data.length(); i++) {
			temp = data.charAt(i);
			if ((index = indexOf(temp)) != -1) {
				sb = new StringBuilder();
				sb.append(data.charAt(i + 1));
				i++;
				if (i + 1 < data.length()) {
					while (isNotMark(data.charAt(i + 1))) {
						if (i + 1 == data.length() - 1) {
							sb.append(data.charAt(i + 1));
							break;
						}
						sb.append(data.charAt(i + 1));
						i++;
					}
				}
				base_clone_count += index;
				base_clone[base_clone_count] = sb.toString();
				base_clone_count++;
			} else if (ONE_DIC_B == temp) {
				base_clone_count += DIC_B_LENGTH;
			} else if (END == temp) {
				return base_clone;
			}
		}
		return base_clone;
	}

	private static boolean isNotMark(char s) {

		if (indexOf(s) != -1 || END == s || ONE_DIC_B == s) {
			return false;
		}
		return true;
	}

	public static int indexOf(char c) {
		for (int i = 0; i < DIC_B.length; i++) {
			if (c == DIC_B[i]) {
				return i;
			}
		}
		return -1;
	}

	public static String[][] getBaseData(ArrayList<String[]> list) {
		String[][] baseData = new String[list.size()][];
		for (int i = 0; i < list.size(); i++) {
			baseData[i] = list.get(i);
		}
		return baseData;
	}

	public static String toStringForm(String[] base_clone) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < base_clone.length; i++) {
			sb.append(base_clone[i]);
			sb.append(TAB);
		}
		return sb.toString().trim();
	}

	public static int countFileLine(String fileName) throws Exception {
		File test = new File(fileName);
		long fileLength = test.length();
		LineNumberReader rf = new LineNumberReader(new FileReader(test));
		int lines = 0;
		rf.skip(fileLength);
		lines = rf.getLineNumber();
		rf.close();
		return lines;
	}

	public static int[] parse(String row) {
		TreeSet<Integer> set = new TreeSet<Integer>();
		String[] testArr = row.split(",");
		String[] temp;
		for (int i = 0; i < testArr.length; i++) {
			temp = testArr[i].trim().split("-");
			if (temp.length == 1) {
				set.add(Integer.parseInt(temp[0].trim()));
			} else {

				int a = Integer.parseInt(temp[0].trim());
				int b = Integer.parseInt(temp[1].trim());
				if (b - a >= 0) {
					for (int j = a; j <= b; j++) {
						set.add(j);
					}
				} else {
					for (int j = b; j <= a; j++) {
						set.add(j);
					}
				}
			}
		}
		int[] result = new int[set.size()];
		int count = 0;
		Iterator<Integer> iter = set.iterator();
		while (iter.hasNext()) {

			result[count] = iter.next();

			count++;
		}
		return result;
	}
	public static String random(int fileLine,int kk0){
		Random random=new Random();
		StringBuffer sb1=new StringBuffer("");
		for(int i=1;i<=kk0;i++){
			sb1.append(random.nextInt(fileLine-1));
			if(i!=kk0){
				sb1.append(",");
			}
		}
		return sb1.toString();
	}
	
	
	
	
}

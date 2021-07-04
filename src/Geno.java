package compressor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Geno {
	private static String COMMENTMARK = "##";
	private static String TAB = "\t";
	private static String NEXTLINE = "\r\n";

	public void genoDivide(String fileName, String headFileName, String genoFileName) throws Exception {
		long start_time = System.currentTimeMillis();
		int genoLine = countGenoLine(fileName);
		String[] idName = new String[genoLine];
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
		BufferedWriter headOut = new BufferedWriter(new FileWriter(FileUtil.file_exitst(headFileName)));
		BufferedWriter genoOut = new BufferedWriter(new FileWriter(FileUtil.file_exitst(genoFileName)));
		String currLine = br.readLine();
		while (currLine.startsWith(COMMENTMARK)) {
			currLine = br.readLine();
		}
		int index = genoIndex(currLine);
		String[] genoName = currLine.split(TAB);
		headOut.write(getHeaders(genoName));
		headOut.write(NEXTLINE);
		ArrayList<byte[]> allGeno = new ArrayList<byte[]>();
		for (int i = 0; i < genoName.length-index; i++) {
			allGeno.add(new byte[genoLine]);
		}
		
		int lineCount=0;
		currLine=br.readLine();
		while (currLine != null) {
			String[] temp = currLine.split(TAB);
			idName[lineCount]=temp[2];
			headOut.write(getHeaders(temp));
			headOut.write(NEXTLINE);
			byte[] geno = processGenoLine(temp, index);
			int genoCount=0;
			for (byte[] b : allGeno) {
				b[lineCount]=geno[genoCount];
				genoCount++;
			}
			currLine= br.readLine();
			lineCount++;
		}
		
		genoOut.write("ID"+TAB);
		for (int i = 0; i < idName.length-1; i++) {
			genoOut.write(idName[i]);
			genoOut.write(TAB);
		}
		genoOut.write(idName[idName.length-1]+NEXTLINE);
		
		for (int i = index; i < genoName.length; i++) {
			genoOut.write(genoName[i]+TAB);
			byte[] b = allGeno.get(i-index);
			for (int j = 0; j < b.length; j++) {
				if(b[j]==0) genoOut.write('0');
				if(b[j]==1) genoOut.write('1');
				if(b[j]==2) genoOut.write('2');
				genoOut.write(TAB);
			}
			genoOut.write(NEXTLINE);
		}
		

		headOut.flush();
		genoOut.flush();
		br.close();
		headOut.close();
		genoOut.close();
		System.out.println("Totle:" + (System.currentTimeMillis() - start_time) / 1000 + "s");
	}

	private byte[] processGenoLine(String[] temp, int index) {
		int rs0count = 0;
		int rs1count = 0;
		int rs2count = 0;
		ArrayList<Integer> indexOfUN = new ArrayList<Integer>();
		byte[] geno = new byte[temp.length - index];
		for (int i = index; i < temp.length; i++) {
			String s = temp[i];
			if (s.charAt(0) != '.') {
				byte genoValue = (byte) (s.charAt(0) + s.charAt(2) - 96);
				geno[i-index]=genoValue;
				if (genoValue == 0)
					rs0count++;
				if (genoValue == 1)
					rs1count++;
				if (genoValue == 2)
					rs2count++;
			}else{
				indexOfUN.add(i-index);
			}
		}
		byte max = 0;
		if(rs1count>rs0count){
			max=1;
		}
		if(rs2count>rs0count){
			max=2;
		}
		if(rs1count>rs2count){
			max=1;
		}
		for (int t : indexOfUN) {
			geno[t] = max;
		}

		return geno;
	}



	private String getHeaders(String[] temp) {
		StringBuilder sb = new StringBuilder();
		sb.append(temp[2]);
		sb.append(TAB);
		sb.append(temp[0]);
		sb.append(TAB);
		sb.append(temp[1]);
		return sb.toString();
	}

	private int countGenoLine(String fileName) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
		int commentLine = 0;
		String currLine = br.readLine();
		while (currLine.startsWith(COMMENTMARK)) {
			commentLine++;
			currLine = br.readLine();
		}
		br.close();
		int fileLine = DataProcess.countFileLine(fileName);
		return fileLine - commentLine - 1;
	}

	private int genoIndex(String currLine) {
		String[] temp = currLine.split(TAB);
		int INFO_INDEX = 0;
		int FORMAT_INDEX = 0;
		for (int i = 0; i < temp.length; i++) {
			if (temp[i].equals("FORMAT")) {
				FORMAT_INDEX = i;
			}
			if (temp[i].equals("INFO")) {
				INFO_INDEX = i;
			}
		}
		return Math.max(INFO_INDEX, FORMAT_INDEX) + 1;
	}

	public static void main(String[] args) throws Exception {
		Geno g = new Geno();
		//String fileName = "F:\\testdata\\all_145_snps_imputation.vcf";
		String fileName = "D:\\otherfile\\all145\\vcf0924.txt.vcf";
		String headFileName = "D:\\otherfile\\all145\\vcf0924_SNP_information.txt";
		String genoFileName = "D:\\otherfile\\all145\\vcf0924_numeric.txt";
		g.genoDivide(fileName, headFileName, genoFileName);
	}
}

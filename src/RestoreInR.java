package compressor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class RestoreInR {
	private static String BASEMARK = "@@";
	private int bufferedLine;
	private String fileName;
	private int fileLines;
	private int columns;
	private int times;
	private int currentTime = 1;
	private int baseLines;
	String param;
	private String[][] baseData;
	String[][] bufferedData;
	BufferedReader br;

	public RestoreInR(String param, String fileName, int num) {
		super();
		this.param = param;
		this.bufferedLine = num;
		this.fileName = fileName;

		try {
			baseLines = baseLines(fileName);
			fileLines = DataProcess.countFileLine(fileName) - baseLines;
		} catch (Exception e) {
			System.out.println("File error");
			return;
		}
		if (fileLines % bufferedLine == 0) {
			times = fileLines / bufferedLine;
		} else {
			times = fileLines / bufferedLine + 1;
		}
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					fileName)));
		} catch (FileNotFoundException e) {
			System.out.println("File error");
			return;
		}
		try {
			createRscript();
		} catch (Exception e) {
			System.out.println("cannot create R script");
		}
	}

	public void getData() throws Exception {
		if (currentTime < times) {
			BufferedWriter output = new BufferedWriter(new FileWriter(
					"c:\\tempfile" + currentTime));
			String baseline = br.readLine();
			while (baseline.startsWith(BASEMARK)) {
				baseline = br.readLine();
			}
			String firstLine = baseline;
			String[] firstLineArr = DataProcess.lineRecover2(firstLine,
					baseData);
			output.write(DataProcess.toStringForm(firstLineArr));
			output.write("\r\n");
			for (int i = 1; i < bufferedLine; i++) {
				output.write(DataProcess.toStringForm(DataProcess.lineRecover2(
						br.readLine(), baseData)));
				output.write("\r\n");
			}
			output.close();
			currentTime++;
		} else if (currentTime == times) {
			BufferedWriter output = new BufferedWriter(new FileWriter(
					"c:\\tempfile" + currentTime));
			String baseline = br.readLine();
			while (baseline.startsWith(BASEMARK)) {
				baseline = br.readLine();
			}
			String firstLine = baseline;
			String[] firstLineArr = DataProcess.lineRecover2(firstLine,
					baseData);
			output.write(DataProcess.toStringForm(firstLineArr));
			output.write("\r\n");
		
			for (int i = 1; i < fileLines % bufferedLine; i++) {
				output.write(DataProcess.toStringForm(DataProcess.lineRecover2(
						br.readLine(), baseData)));
				output.write("\r\n");
			}
			output.close();
			br.close();
		}

	}

	private int baseLines(String fileName) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(fileName)));
		String line = br.readLine();
		int LineCount = 0;
		ArrayList<String[]> baselist = new ArrayList<String[]>();
		if (!line.startsWith(BASEMARK)) {
			br.close();
			throw new Exception();
		} else {

			while (line.startsWith(BASEMARK)) {
				LineCount++;
				baselist.add(line.substring(2).split("\t"));
				line = br.readLine();
			}
		}
		baseData = DataProcess.getBaseData(baselist);
		columns = baseData[0].length;
		br.close();
		return LineCount;
	}

	private void createRscript() throws Exception {
		BufferedWriter output = new BufferedWriter(new FileWriter("c:\\test.R"));
		output.write(param + "<-matrix(data = NA, nr = " + fileLines
				+ ", nc = " + columns + ")" + "\r\n");
		for (int j = 1; j <= times; j++) {
			if (j == times) {
				int startLast = (times - 1) * bufferedLine + 1;
				int endLast = fileLines % bufferedLine + startLast-1;
				output.write("restore$getData()" + "\r\n");
				output.write(param + "[" + startLast + ":" + endLast
						+ ",]<- as.matrix(read.delim(" + "\"C:tempfile" + j
						+ "\", head = FALSE))\r\n");
				output.write("file.remove("+"\"C:tempfile" + j+"\")\r\n");
				output.write("gc()\r\n");
				output.write(param +"<- as.data.frame("+param+")\r\n");
				output.write("gc()\r\n");
			} else if (j < times) {

				int start = (j - 1) * bufferedLine + 1;
				int end = start + bufferedLine-1;
				output.write("restore$getData()" + "\r\n");
				output.write(param + "[" + start + ":" + end
						+ ",]<- as.matrix(read.delim(" + "\"C:tempfile" + j
						+ "\", head = FALSE))\r\n");
				output.write("file.remove("+"\"C:tempfile" + j+"\")\r\n");
				if (j % 5 == 0) {
					output.write("gc()\r\n");
				}
			}
		}
		output.close();

	}

	public int getTimes() {
		return times;
	}

/*	public static void main(String[] args) throws Exception {
		long start_time = System.currentTimeMillis();
		RestoreInR r = new RestoreInR("myC",
				"D:\\Backup\\����\\test2\\hmp321.txt_c", 10000);
		int a = r.getTimes();
		System.out.println(a);
		System.out.println("��ѹ�ܼ�ʱ:" + (System.currentTimeMillis() - start_time)
				/ 1000 + "s");

	}*/

}

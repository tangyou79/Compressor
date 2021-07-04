package compressor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class RestoreData {
	private static String SAMPLEMARK = "@@";
	// ����
	private static String NEXTLINE = "\r\n";
	// �Ʊ��
	private static String TAB = "\t";

	private static String COMMENTMARK = "##";
        
        /**
         * 解压缩文件
         * @param compress_filename 等待解压的文件名
         * @param new_filename 解压完成之后输入的文件名
         */
	public void restore(String compress_filename, String new_filename)
			throws Exception {

		long start_time = System.currentTimeMillis();
		File file = FileUtil.file_exitst(new_filename);
		BufferedWriter output = new BufferedWriter(new FileWriter(file));
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(compress_filename)));

		// �����׼���� �õ���׼����
		ArrayList<String[]> list = new ArrayList<String[]>();
		String baseline = br.readLine();
		while (baseline.startsWith(SAMPLEMARK)) {
			list.add(baseline.substring(SAMPLEMARK.length()).split(TAB));
			baseline = br.readLine();
		}
		String[][] sampleData = DataProcess.getBaseData(list);
		String currLine = baseline;
		while (currLine.startsWith(COMMENTMARK)) {
			output.write(currLine);
			output.write(NEXTLINE);
			currLine = br.readLine();
		}
		while (currLine != null) {
			output.write(DataProcess.lineRecover3(currLine, sampleData));
			output.write(NEXTLINE);
			currLine = br.readLine();
		}
		output.flush();
		output.close();
		br.close();
		System.out.println("Totle:" + (System.currentTimeMillis() - start_time)
				/ 1000 + "s");
	}
}

package compressor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

public class CssForC {

	private static String SAMPLEMARK = "@@";
	// ����
	private static String NEXTLINE = "\r\n";
	// �Ʊ��
	private static String TAB = "\t";

	private static String COMMENTMARK = "##";

	public void compress(String org_filename, String newfilename, String[][] sample) throws Exception {
		long start_time = System.currentTimeMillis();
		// basedata = GenerateDefaultSample.HapMapSample(org_filename);
		int fileLine = DataProcess.countFileLine(org_filename);
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(org_filename)));
		BufferedWriter output = new BufferedWriter(new FileWriter(FileUtil.file_exitst(newfilename)));
		
		int sampleLen=0;
		for(String s:sample[0]){
			sampleLen=sampleLen+s.length()+1;
		}
		output.write("! "+fileLine+" "+sample[0].length+" "+sampleLen+" "+sample[0][sample[0].length-1].length()+" "+NEXTLINE);
		
		// д���׼����
		for (int i = 0; i < sample.length; i++) {
			output.write("@ ");
			for (int j = 0; j < sample[i].length; j++) {
				output.write(sample[i][j]);
				output.write(" ");
			}
			output.write(NEXTLINE);
		}
		String currLine = br.readLine();
		while (currLine.startsWith(COMMENTMARK)) {
			output.write(currLine);
			output.write(NEXTLINE);
			currLine = br.readLine();
		}
		//
		output.write(currLine.length()+NEXTLINE);
		// ѹ������
		while (currLine != null) {
			output.write(DataProcess.lineCompress2(sample, currLine));
			output.write(NEXTLINE);
			currLine = br.readLine();
		}
		output.flush();
		output.close();
		br.close();
		System.out.println("Totle:" + (System.currentTimeMillis() - start_time) / 1000 + "s");
	}

	public static void main(String[] args) throws Exception {
		
		Scanner sc = new Scanner(System.in);			
		//String fileName="D:\\mdp_genotype_test.hmp.txt";
		//String newFileName="D:\\mdp_genotype_test.hmp_c1129.txt";
		//int[] lines={12,30,50,90,120};		
		//String[][] sample=DataProcess.readBaseData(lines, fileName);
		//CssForC c = new CssForC();
		//c.compress(fileName, newFileName, sample);		
			
		System.out.print("Please choose the file format:\n1.HapMap\n2.Numeric\nPlease select a number to continue: ");
			String choose1 = sc.nextLine();
			if (choose1.equals("1")) {
				System.out.print("Enter the name of file to be compressed:");
				String filename = sc.nextLine().trim();
				System.out.print("Enter the name of generating file:");
				String newfilename = sc.nextLine().trim();
				System.out.print("1: Data compression by default methods according to file format\n2: Data compression by grouping samples according to the number you input \nPlease select a number to continue:");
				String choose2 = sc.nextLine();
				if (choose2.endsWith("1")) {
					try {
						String[][] sample = GenerateDefaultSample.HapMapSample(filename);
						CssForC c = new CssForC();				
						System.out.println("Data is being processed,please wait...");
						c.compress(filename, newfilename, sample);
	
					} catch (Exception e) {
						System.out.println("Error,please check input and try again");
					}
				} else if (choose2.equals("2")) {
					System.out.print("Enter the sample Numbers:");
					try {
						String param = sc.nextLine().trim();
						int kk0=Integer.parseInt(param);
						int fileLine = DataProcess.countFileLine(filename);
													
						String param1=DataProcess.random(fileLine, kk0);
						
						int[] lines = DataProcess.parse(param1);
						String[][] sample = DataProcess.readBaseData(lines, filename);
						CssForC c = new CssForC();				
						System.out.println("Data is being processed,please wait...");
						c.compress(filename, newfilename, sample);
					} catch (Exception e) {
						System.out.println("Error,please check input and try again");
					}

				} else {
					System.out.println("please check input and try again!");
					System.exit(0);
				}
			} else if (choose1.equals("2")) {
				System.out.print("Enter the name of file to be compressed:");
				String filename = sc.nextLine().trim();
				System.out.print("Enter the name of generating file:");
				String newfilename = sc.nextLine().trim();
				System.out.print("1: Data compression by default methods according to file format\n2:Data compression by grouping samples according to the number you input \nPlease select a number to continue:");
				String choose2 = sc.nextLine();
				if (choose2.endsWith("1")) {
					try {
						String[][] sample = GenerateDefaultSample.NumericSample(filename);
						CssForC c = new CssForC();				
						System.out.println("Data is being processed,please wait...");
						c.compress(filename, newfilename, sample);
					} catch (Exception e) {
						System.out.println("Error,please check input and try again");
					}
				} else if (choose2.equals("2")) {
					System.out.print("Enter the sample Numbers:");
						try {
							String param = sc.nextLine().trim();
							int kk0=Integer.parseInt(param);
							int fileLine = DataProcess.countFileLine(filename);														
							String param1=DataProcess.random(fileLine, kk0);							
							int[] lines = DataProcess.parse(param1);
							String[][] sample = DataProcess.readBaseData(lines, filename);
							CssForC c = new CssForC();				
							System.out.println("Data is being processed,please wait...");
							c.compress(filename, newfilename, sample);

					} catch (Exception e) {
						System.out.println("Error,please check input and try again");
					}

				} else {
					System.out.println("please check input and try again!");
					System.exit(0);
				}

			} else {
				System.out.println("please check input and try again!");
				System.exit(0);
			}
		sc.close();			
	}	
}

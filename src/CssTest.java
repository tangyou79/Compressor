package compressor;

import java.util.Scanner;

public class CssTest {

	public static void main(String[] args) {

		System.out.print("1:Data compression\n2:Restore data\n3:File compare\nPlease select a number to continue: ");
		Scanner sc = new Scanner(System.in);
		String choose = sc.nextLine();
		if (choose.equals("1")) {
			System.out.print("Filetype choose\n1.HapMap\n2.VCF\nPlease select a number to continue:");
			String choose1 = sc.nextLine();
			if (choose1.equals("1")) {
				System.out.print("Enter the name of the file needs to be compressed:");
				String filename = sc.nextLine().trim();
				System.out.print("Enter the name of the file after the compression:");
				String newfilename = sc.nextLine().trim();
				System.out.print("1:Compress data by default sample\n2:Compress data by random sample\nPlease select a number to continue:");
				String choose2 = sc.nextLine();
				if (choose2.endsWith("1")) {
					try {
                                                
						String[][] sample = GenerateDefaultSample.HapMapSample(filename);
						VcfCompression v = new VcfCompression();
						System.out.println("Data is being processed,please wait...");
						v.compress(filename, newfilename, sample);
					} catch (Exception e) {
						System.out.println("Error,please check input and try again");
					}
				} else if (choose2.equals("2")) {
					System.out.print("Enter the sample parameters:");

					try {   
						String param = sc.nextLine().trim();
						int[] lines = DataProcess.parse(param);
						String[][] sample = DataProcess.readBaseData(lines, filename);
						VcfCompression v = new VcfCompression();
						System.out.println("Data is being processed,please wait...");
						v.compress(filename, newfilename, sample);
					} catch (Exception e) {
						System.out.println("Error,please check input and try again");
					}

				} else {
					System.out.println("please check input and try again!");
					System.exit(0);
				}
			} else if (choose1.equals("2")) {
				System.out.print("Enter the name of the file needs to be compressed:");
				String filename = sc.nextLine().trim();
				System.out.print("Enter the name of the file after the compression:");
				String newfilename = sc.nextLine().trim();
				System.out.print("Enter the sample parameters:");

				try {
                                    
                                       
					String param = sc.nextLine().trim();
					int[] lines = DataProcess.parse(param);
					String[][] sample = DataProcess.readBaseData(lines, filename);
					VcfCompression v = new VcfCompression();
					System.out.println("Data is being processed,please wait...");
					v.compress(filename, newfilename, sample);

				} catch (Exception e) {
					System.out.println("Error,please check input and try again");
				}

			} else {
				System.out.println("please check input and try again!");
				System.exit(0);
			}

		} else if (choose.equals("2")) {
			System.out.print("Enter the name of the file needs to be restored:");
			String compress_filename = sc.nextLine().trim();
			System.out.print("Please enter a new generated file name:");
			String new_filename = sc.nextLine().trim();
			System.out.println("Data is being processed,please wait...");
			try {
				RestoreData r = new RestoreData();
				r.restore(compress_filename, new_filename);
			} catch (Exception e) {
				System.out.println("Error,please check input and try again");
			}

		} else if (choose.equals("3")) {
			System.out.print("please input filename1:");
			String filename1 = sc.nextLine().trim();
			System.out.print("please input filename2:");
			String filename2 = sc.nextLine().trim();
			try {
				if (FileUtil.cmp(filename1, filename2)) {
					System.out.println("Two files are the same");
				} else {
					System.out.println("Two files are not the same");
				}
			} catch (Exception e) {
				System.out.println("Error,please check input and try again");
			}

		} else {
			System.out.println("please check input and try again!");
			System.exit(0);
		}
		sc.close();
	}

}

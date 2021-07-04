package compressor;

import java.io.File;
import java.util.Scanner;

public class CssTest2 {

	public static void main(String[] args) {
                
                //打开软件的提示。软件一共有三种功能，Data compression为数据压缩，Restore data为数据解压，File compare为文件对比
		System.out.print("1:Data compression\n2:Restore data\n3:File compare\nPlease select a number to continue: ");
		Scanner sc = new Scanner(System.in);//从键盘获取内容
		String choose = sc.nextLine().trim();//将键盘的内容赋值给choose
		if (choose.equals("1")) {//Data compression，数据压缩
			//输入等待压缩的文件名
                        System.out.print("Enter the name of the file needs to be compressed:");
			String filename = sc.nextLine().trim();
                        
                        //输入压缩之后输出文件名
			System.out.print("Enter the name of the file after the compression:");
			String newfilename = sc.nextLine().trim();
                        
                        //压缩功能有两种，1是使用默认设置。2是自己输入压缩比例
			System.out.print("1:Compress data by default sample\n2:Compress data by random sample\nPlease select a number to continue:");
			String choose1 = sc.nextLine().trim();
			
                        if (choose1.equals("1")) {//选择默认设置
                               
                            //选择文件的输入格式
				System.out.print("Filetype choose\n1.HapMap\n2.VCF\n3.PLink\nPlease select a number to continue:");
				String choose2 = sc.nextLine().trim();
                                
				if (choose2.equals("1")) {//输入文件格式为HapMap格式
					try {
						String[][] sample = GenerateDefaultSample.HapMapSample(filename);//开始读取HapMap格式文件
						VcfCompression v = new VcfCompression();//开始数据压缩
						System.out.println("Data is being processed,please wait...");
						v.compress(filename, newfilename, sample);//将压缩好的文件写入到硬盘
					} catch (Exception e) {
						System.out.println("Error,please check input and try again");
					}
				} else if (choose2.equals("2")) {//输入文件格式为VCF格式
					try {
                                                
                                                filename = VcfCompare.Compare(filename);//首先判断VCF文件是否是纯0/1还是带有其他内容的                                               
						String[][] sample = GenerateDefaultSample.VcfSample(filename);//开始读取VCF格式文件
						VcfCompression v = new VcfCompression();//开始数据压缩
						System.out.println("Data is being processed,please wait...");
						v.compress(filename, newfilename, sample);//将压缩好的文件写入到硬盘
                                                 new File(filename).delete();//将中间文件删除
					} catch (Exception e) {
						System.out.println("Error,please check input and try again");
					}
				} else if(choose2.equals("3")){//输入文件格式为PLink格式
					try {
						String[][] sample = GenerateDefaultSample.PLinkSample(filename);//开始读取pLINK格式文件
						VcfCompression v = new VcfCompression();//开始数据压缩
						System.out.println("Data is being processed,please wait...");
						v.compress(filename, newfilename, sample);//将压缩好的文件写入到硬盘
					} catch (Exception e) {
						System.out.println("Error,please check input and try again");
					}
					
				}else {
					System.out.println("please check input and try again!"); 
					System.exit(0);
				}

			} else if (choose1.equals("2")) {//自己输入压缩比例
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

		} else if (choose.equals("2")) {//文件解压
			System.out.print("Enter the name of the file needs to be restored:");
			String compress_filename = sc.nextLine().trim();//等待解压的文件名
			System.out.print("Please enter a new generated file name:");
			String new_filename = sc.nextLine().trim();//解压完成后输出的文件名
			System.out.println("Data is being processed,please wait...");
			try {
				RestoreData r = new RestoreData();
				r.restore(compress_filename, new_filename);//开始解压文件
			} catch (Exception e) {
				System.out.println("Error,please check input and try again");
			}

		} else if (choose.equals("3")) {//文件对比
			System.out.print("please input filename1:");
			String filename1 = sc.nextLine().trim();
			System.out.print("please input filename2:");
			String filename2 = sc.nextLine().trim();
			try {
				if (FileUtil.cmp(filename1, filename2)) {//比对两个文件是否一致
					System.out.println("Two files are the same");
				} else {
					System.out.println("Two files are not the same");
				}
			} catch (Exception e) {
                            System.out.println(e);
				System.out.println("Error,please check input and try again");
			}

		} else {
			System.out.println("please check input and try again!");
			System.exit(0);
		}
		sc.close();
	}
	
}

package compressor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class FileUtil {

    /**
     * �ж��ļ��Ƿ���ڣ������ڣ�����ֹͣ���У����򣬴����ļ���ִ������
     *
     * @param filename �ļ����� ��ȫ·�� c:\xx\xx.txt ��ʽ��
     * @return
     */
    public static File file_exitst(String filename) {
        File f = new File(filename);
        try {
            if (f.exists()) {
                System.out.print("File exists, delete files or change the file and try again");
                return null;
            } else {
                System.out.println(filename + " created");
                f.createNewFile();// �������򴴽�
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }

    /**
     * 比对两个文件是否一致
     *
     * @param filename1 文件名1��
     * @param filename2 �文件名2�
     * @return true:�一致 flase:�不一致
     */
    public static boolean cmp(String filename1, String filename2)
            throws Exception {
        BufferedReader br1 = new BufferedReader(new InputStreamReader(
                new FileInputStream(filename1)));
        BufferedReader br2 = new BufferedReader(new InputStreamReader(
                new FileInputStream(filename2)));
        boolean f = true;
        String data1 = br1.readLine();
        String data2 = br2.readLine();

        //判断是否vcf文件
        if (data1.startsWith("#")) {
            while (data1 != null && data2 != null) {
              //  System.out.println(data1);
                //System.out.println(data2);
                 String[] te1 = data1.split("\t");
                String[] te2 = data2.split("\t");
                if (data1.startsWith("##")) {//遇到##就原样输出        
                } else if (data1.startsWith("#C")) {//遇到#c就原样输出               
                } else {
                    for (int i = 0; i < te1.length; i++) {

                        if (i < 9) {
                            if (!te1[i].equals(te2[i])) {
                                f = false;
                                System.out.println(data1);
                                System.out.println(data2);
                                break;
                            }
                        } else {
                            if (!te1[i].substring(0, 3).equals(te2[i].substring(0, 3))) {
                                f = false;
                                System.out.println(data1);
                                System.out.println(data2);
                                break;
                            }
                        }
                    }
                }
                data1 = br1.readLine();
                data2 = br2.readLine();

            }

        } else {
            while (data1 != null && data2 != null) {
                if (!data1.trim().equals(data2.trim())) {
                    f = false;
                    System.out.println(data1);
                    System.out.println(data2);
                }
                data1 = br1.readLine();
                data2 = br2.readLine();
            }
        }

        br1.close();
        br2.close();
        return f;

    }
}

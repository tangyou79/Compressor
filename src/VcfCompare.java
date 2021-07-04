/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compressor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Mr-LiZhuo
 */
public class VcfCompare {

    /**
     * 对输入的VCF文件进行判断，如果存在多余的内容，那么将其删掉。
     *
     * @param filename 文件地址
     * @return 新的文件地址
     */
    public static String Compare(String filename) {

        File file = new File(filename);
        String[] te = null;

        String fileNameOut = filename + "-out";

        BufferedReader reader = null;
        FileWriter writer = null;
        try {
            writer = new FileWriter(fileNameOut, true);
         //   System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;

            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                if (tempString.startsWith("##")) {//遇到##就原样输出
                    writer.write(tempString+"\n");
                } else if (tempString.startsWith("#C")) {//遇到#c就原样输出
                    writer.write(tempString+"\n");
                } else {
                    te = tempString.split("\t");
                    String temp = "";
                    for (int i = 0; i < te.length; i++) {
                        if (i < 9) {
                            temp = temp + te[i] + "\t";//前九列原样输出
                        } else {
                            temp = temp + te[i].substring(0, 3) + "\t";//从第九列开始，只输出前三个字符
                        }
                    }
                    temp = temp + "\n";
                    writer.write(temp);
                }
            }

            reader.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                    writer.close();
                } catch (IOException e1) {
                }
            }
        }

        return fileNameOut;
    }

}

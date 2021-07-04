package compressor;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class GenerateDefaultSample {
	private static final int NUM_HAPMAP_NON_TAXA_HEADERS = 11;
	
	private static final int NUM_PLINK_NON_SITE_HEADERS = 6;

        /**
         * 读取HapMap格式文件
         * @param fileName 文件地址
         * @return 返回文件内容
         **/
	public static String[][] HapMapSample(String fileName) throws Exception {
		String[][] sample = new String[5][];
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
		String currLine = br.readLine();
		while (currLine.startsWith("##")) {
			currLine = br.readLine();
		}
		currLine = br.readLine();
		currLine = br.readLine();
		String letter = currLine.split("\t")[NUM_HAPMAP_NON_TAXA_HEADERS + 1];
		int sampleLen = currLine.split("\t").length;
		int len = letter.length();
		if (len == 1) {
			sample[0] = generateOneHapSample(sampleLen, "N");
			sample[1] = generateOneHapSample(sampleLen, "A");
			sample[2] = generateOneHapSample(sampleLen, "T");
			sample[3] = generateOneHapSample(sampleLen, "C");
			sample[4] = generateOneHapSample(sampleLen, "G");
		} else if (len == 2) {
			sample[0] = generateOneHapSample(sampleLen, "NN");
			sample[1] = generateOneHapSample(sampleLen, "AA");
			sample[2] = generateOneHapSample(sampleLen, "TT");
			sample[3] = generateOneHapSample(sampleLen, "CC");
			sample[4] = generateOneHapSample(sampleLen, "GG");
		} else {
			br.close();
			throw new Exception("ProcessHapMapBlock: Genotype coded wrong use 1 or 2 letters per genotype");
		}

		br.close();
		return sample;
	}

	private static String[] generateOneHapSample(int len, String s) {
		String[] sample = new String[len];
		for (int i = 0; i < sample.length; i++) {
			sample[i] = s;
		}
		for (int i = 0; i < NUM_HAPMAP_NON_TAXA_HEADERS; i++) {
			sample[i] = "NA";
		}
		sample[1] = "A/T";
		sample[2] = "1";
		sample[4] = "+";
		return sample;
	}
	//Ĭ��numeric
	public static String[][] NumericSample(String fileName) throws Exception {
		String[][] sample = new String[3][];
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
		String currLine = br.readLine();
	
		br.close();
		int sampleLen = currLine.split("\t").length;
		sample[0]=generateOnePlinkSample(sampleLen, "0");
		sample[1]=generateOnePlinkSample(sampleLen, "1");
		sample[2]=generateOnePlinkSample(sampleLen, "2");

		return sample;
	}
	
	public static String[][] VcfSample(String fileName) throws Exception {
		String[][] sample = new String[3][];
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
		String currLine = br.readLine();
		while (currLine.startsWith("##")) {
			currLine = br.readLine();
		}
		HeaderPositions hp=new HeaderPositions(currLine.split("\t"));
		currLine = br.readLine();
		int sampleLen = currLine.split("\t").length;
		sample[0]=generateOneVcfSample(sampleLen, "0/0", hp);
		sample[1]=generateOneVcfSample(sampleLen, "./.", hp);
		sample[2]=generateOneVcfSample(sampleLen, "1/1", hp);
		br.close();
		return sample;
	}
	
	private static String[] generateOneVcfSample(int len, String s,HeaderPositions hp){
		String[] sample = new String[len];
		for (int i = 0; i < sample.length; i++) {
			sample[i] = s;
		}
		for (int i = 0; i < hp.NUM_HAPMAP_NON_TAXA_HEADERS; i++) {
			sample[i] = "NA";
		}
		sample[0]="1";
		sample[hp.QUAL_INDEX]=".";
		sample[hp.FILTER_INDEX]="PASS";
		sample[hp.INFO_INDEX]=".";
		sample[hp.FORMAT_INDEX]="GT";
		sample[hp.ALT_INDEX]=".";
		sample[hp.REF_INDEX]="A";
		return sample;
	}
	
	public static String[][] PLinkSample(String fileName) throws Exception {
		String[][] sample = new String[5][];
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
		String currLine = br.readLine();
		while (currLine.startsWith("##")) {
			currLine = br.readLine();
		}
		currLine = br.readLine();
		br.close();
		int sampleLen = currLine.split("\t").length;
		sample[0]=generateOnePlinkSample(sampleLen, "0");
		sample[1]=generateOnePlinkSample(sampleLen, "A");
		sample[2]=generateOnePlinkSample(sampleLen, "C");
		sample[3]=generateOnePlinkSample(sampleLen, "G");
		sample[4]=generateOnePlinkSample(sampleLen, "T");
		return sample;
	}
	
	private static String[] generateOnePlinkSample(int len, String s) {
		String[] sample = new String[len];
		for (int i = 0; i < sample.length; i++) {
			sample[i] = s;
		}
		for (int i = 0; i < NUM_PLINK_NON_SITE_HEADERS; i++) {
			sample[i] = "NA";
		}
		return sample;
	}

}



class HeaderPositions {
	final int NUM_HAPMAP_NON_TAXA_HEADERS;
	final int GENOIDX;
	final int SNPID_INDEX;
	// final int VARIANT_INDEX;
	final int FILTER_INDEX;
	final int QUAL_INDEX;
	final int CHROMOSOME_INDEX;
	final int POSITION_INDEX;
	final int REF_INDEX;
	final int ALT_INDEX;
	final int INFO_INDEX;
	final int FORMAT_INDEX;

	public HeaderPositions(String[] header) {
		int chrIdx = firstEqualIndex(header, "#CHROM");
		if (chrIdx < 0)
			chrIdx = firstEqualIndex(header, "#CHR");
		CHROMOSOME_INDEX = chrIdx;
		POSITION_INDEX = firstEqualIndex(header, "POS");
		SNPID_INDEX = firstEqualIndex(header, "ID");
		REF_INDEX = firstEqualIndex(header, "REF");
		ALT_INDEX = firstEqualIndex(header, "ALT");
		QUAL_INDEX = firstEqualIndex(header, "QUAL");
		FILTER_INDEX = firstEqualIndex(header, "FILTER");
		INFO_INDEX = firstEqualIndex(header, "INFO");
		FORMAT_INDEX = firstEqualIndex(header, "FORMAT");

		NUM_HAPMAP_NON_TAXA_HEADERS = Math.max(INFO_INDEX, FORMAT_INDEX) + 1;
		GENOIDX = NUM_HAPMAP_NON_TAXA_HEADERS;
	}

	private static int firstEqualIndex(String[] sa, String match) {
		for (int i = 0; i < sa.length; i++) {
			if (sa[i].equals(match))
				return i;
		}
		return -1;
	}

}
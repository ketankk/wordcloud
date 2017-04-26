package driver;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.CircleBackground;
import com.kennycason.kumo.bg.PixelBoundryBackground;
import com.kennycason.kumo.bg.RectangleBackground;
import com.kennycason.kumo.font.scale.LinearFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.palette.ColorPalette;

public class TestMain {
	public static void main(String[] args) throws IOException {

		String filePath = "D:\\deploymentFinal\\reports\\daily\\abc.xlsx";// args[0];
		int column = 7; // Integer.parseInt(args[1]);
		System.out.println(filePath);
		// Map<String, Integer> freq = readFile(filePath);
		// createFile();
		readKRAFile();

		wordCloud();

	}

	private static void createFile() {
		// TODO Auto-generated method stub

	}

	static void wordCloud() throws IOException {
		final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
		//frequencyAnalyzer.setWordTokenizer(wordTokenizer);
		frequencyAnalyzer.setWordFrequenciesToReturn(300);
		frequencyAnalyzer.setMinWordLength(4);
		frequencyAnalyzer.setStopWords(loadStopWords());

		final List<WordFrequency> wordFrequencies = frequencyAnalyzer
				.load("D:\\deploymentFinal\\reports\\daily\\KRA.txt");
		final Dimension dimension = new Dimension(300, 200);
		final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
		wordCloud.setPadding(2);
		wordCloud.setBackground(new CircleBackground(100));
		wordCloud.setBackgroundColor(new Color(0xffffff));
		// wordCloud.setBackground(new
		// PixelBoundryBackground("backgrounds/whale_small.png"));
		wordCloud.setColorPalette(new ColorPalette(new Color(0x6600ff), new Color(0x66ff33)));
		wordCloud.setFontScalar(new LinearFontScalar(10, 40));
		wordCloud.build(wordFrequencies);
		wordCloud.writeToFile("D:\\deploymentFinal\\reports\\daily\\ITCKRA2.png");
	}

	static void readKRAFile() throws IOException {

		String file = "D:\\deploymentFinal\\reports\\daily\\abc.xlsx";
		FileInputStream is = new FileInputStream(new File(file));
		XSSFWorkbook workbook = new XSSFWorkbook(is);

		Sheet sheet = workbook.getSheetAt(0);
		System.out.println("workbook");


		// Iterate through each rows from first sheet
		Iterator<Row> rowIterator = sheet.iterator();
		Map<String, Integer> wordFreq = new HashMap<>();
		StringBuilder builder = new StringBuilder();
		int count = 0;
		while (rowIterator.hasNext())

		{
			Row row = rowIterator.next();

			System.out.println(count);
			if(count>16)
				break;
			if (count++ < 4) {
				continue;

			}
			Cell cell = row.getCell(0);
			Cell cellfre = row.getCell(1);

			String word = cell.getStringCellValue();
			word = word.replaceAll(" ", "~");
			double freq = (cellfre.getNumericCellValue());
			System.out.println(word + freq);
			while (freq-- > 0) {
				builder.append(word + " ");
			}
			System.out.println(word);
			

		}

		File file2 = new File("D:\\deploymentFinal\\reports\\daily\\KRA.txt");
		FileWriter fileWriter = new FileWriter(file2);
		fileWriter.write(builder.toString());
		fileWriter.close();

	}

	private static Collection<String> loadStopWords() {
		return Arrays.asList("abc1");
	}

	static Map<String, Integer> readFile(String file) throws FileNotFoundException, IOException {
		FileInputStream is = new FileInputStream(new File(file));
		XSSFWorkbook workbook = new XSSFWorkbook(is);

		Sheet sheet = workbook.getSheetAt(0);
		System.out.println("workbook");

		// Iterate through each rows from first sheet
		Iterator<Row> rowIterator = sheet.iterator();
		Map<String, Integer> wordFreq = new HashMap<>();
		StringBuilder builder = new StringBuilder();
		while (rowIterator.hasNext())

		{
			Row row = rowIterator.next();

			Cell cell = row.getCell(6);
			if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
				String abc = cell.getStringCellValue();
				builder.append(abc);
				String words[] = abc.split(" ");
				for (String word : words) {
					int count = 1;
					if (wordFreq.containsKey(word.toLowerCase())) {
						count = wordFreq.get(word.toLowerCase()) + 1;

					}

					wordFreq.put(word.toLowerCase(), count);
				}
				System.out.println(abc);

			}
		}
		File file2 = new File("D:\\deploymentFinal\\reports\\daily\\datarank2.txt");
		FileWriter fileWriter = new FileWriter(file2);
		fileWriter.write(builder.toString());
		fileWriter.close();
		System.out.println(wordFreq);
		return wordFreq;
	}
}
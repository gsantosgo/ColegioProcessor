package es.madrid.colegio;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.google.common.io.Files;

public class Main {

	public static void main(String[] args) {

		System.setProperty("http.proxyHost", "10.14.79.204");
		System.setProperty("http.proxyPort", "8080");
		
		String inputFileNamePath = "src/main/resources/ColegioMadrid592.csv";
		String outputDirectoryNamePath = "src/main/resources";

		Preconditions.checkNotNull(inputFileNamePath,
				"Input filename should NOT be NULL");
		Preconditions.checkNotNull(outputDirectoryNamePath,
				"Output directory should NOT be NULL.");

		File inputFile = new File(inputFileNamePath);
		File outputDirectory = new File(outputDirectoryNamePath);

		Preconditions.checkArgument(inputFile.exists(),
				"File does not exist: %s", inputFile);
		Preconditions.checkArgument(outputDirectory.exists(),
				"Directory does not exist: %s", outputDirectory);

		System.out.println("Starting process  ....");
		System.out.println("> Waiting....");
		Stopwatch stopWatch = new Stopwatch();
		stopWatch.start();
		String resultado = "";
		try {
			File outputFile = new File(outputDirectoryNamePath + File.separator
					+ "salida.csv");
			// if file exists then remove it
			if (outputFile.exists())
				outputFile.delete();
						
			resultado = Files.readLines(inputFile, Charsets.UTF_8, new ColegioLineProcessor());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(String.format("Processed registers %s ", resultado));
		System.out.println(String.format(
				"Complete process. Elapsed time %d min, %d sec.",
				stopWatch.elapsedTime(TimeUnit.MINUTES),
				stopWatch.elapsedTime(TimeUnit.SECONDS)));
		System.out.println("");

	}
}

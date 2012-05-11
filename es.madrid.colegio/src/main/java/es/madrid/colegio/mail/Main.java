package es.madrid.colegio.mail;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.google.common.io.Files;

import es.madrid.colegio.ColegioLineProcessor;

public class Main {	
	
	
	/**
	 * 
	 * Programa principal. Todos los colegios y envia correo electrónicos   
	 *   
	 * 
	 * @param args Argumentos 
	 */
	public static void main(String args[]) {			
		String inputFileNamePath = "src/main/resources/Salida.Final.csv";
		
		// Estableciendo proxy =====
		System.setProperty("http.proxyHost", "10.14.79.204");
		System.setProperty("http.proxyPort", "8080");

		
		Preconditions.checkNotNull(inputFileNamePath,
				"Input filename should NOT be NULL.");
				
		File inputFile = new File(inputFileNamePath);

		Preconditions.checkArgument(inputFile.exists(),
				"File does not exist: %s", inputFile);
		
		System.out.println("Starting process  ....");
		System.out.println("> Waiting....");
		Stopwatch stopWatch = new Stopwatch();
		stopWatch.start();
		String resultado = "";
		try {						
			resultado = Files.readLines(inputFile, Charsets.ISO_8859_1, new MailLineProcessor());
			
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

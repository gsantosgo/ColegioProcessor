package es.madrid.colegio;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.google.common.base.Strings;
import com.google.common.io.Files;

public class Main2 {

	public static void main(String[] args) {

		System.setProperty("http.proxyHost", "10.14.79.204");
		System.setProperty("http.proxyPort", "8080");
		
		String inputDirectoryNamePath = "src/main/resources";
		String outputFileNamePath = "src/main/resources/salida.csv";
		
		Preconditions.checkNotNull(inputDirectoryNamePath,
				"Input directory should NOT be NULL.");
		Preconditions.checkNotNull(outputFileNamePath, "Output FileName should NOT be NULL.");		

		File inputDirectory = new File(inputDirectoryNamePath);

		Preconditions.checkArgument(inputDirectory.exists(),
				"Directory does not exist: %s", inputDirectory);

		System.out.println("Starting process  ....");
		System.out.println("> Waiting....");
		Stopwatch stopWatch = new Stopwatch();
		stopWatch.start();
		String[] listFiles; 

		listFiles = inputDirectory.list(new FilenameFilter() {
			public boolean accept(File dir, String name) {
			        return name.endsWith(".htm");
		    }					
		}); 
		
		String patternTLCodigo = "<input type=\"hidden\" name=\"tlCodigo\" value=\"([^\"]*)\" />";
		Pattern pTLCodigo = Pattern.compile(patternTLCodigo); 
		String patternTLGenericoCentro = "<input type=\"hidden\" name=\"tlGenericoCentro\" value=\"([^\"]*)\" />";		
		Pattern pTLGenericoCentro = Pattern.compile(patternTLGenericoCentro);
		String patternTLNombreCentro = "<input type=\"hidden\" name=\"tlNombreCentro\" value=\"([^\"]*)\" />"; // Mayusculas
		Pattern pTLNombreCentro = Pattern.compile(patternTLNombreCentro);
		String patternTLAreaTerritorial = "<input TYPE='hidden' name='tlAreaTerritorial' value=\"([^\"]*)\"/>";
		Pattern pTLAreaTerritorial = Pattern.compile(patternTLAreaTerritorial);
		
		String patternTLTitularidad = "<input TYPE='hidden' name='tlTitularidad' value='([^\']*)'/>";
		Pattern pTLTitularidad = Pattern.compile(patternTLTitularidad);
		
		String patternTLTel = "<input TYPE='hidden' name='tlTelefono' value='([^\']*)'/>";
		Pattern pTLTel = Pattern.compile(patternTLTel);
		
		String patternTLFax = "<input TYPE='hidden' name='tlFax' value='([^\']*)'/>";
		Pattern pTLFax = Pattern.compile(patternTLFax);
		
		String patternTLWeb = "<input TYPE='hidden' name='tlWeb' value='([^\']*)'/>";
		Pattern pTLWeb = Pattern.compile(patternTLWeb);
		
		String patternTLMail = "<input TYPE='hidden' name='tlMail' value='([^\']*)'/>"; 
		Pattern pTLMail = Pattern.compile(patternTLMail);
		String patternTLProvincia = "<input TYPE='hidden' name='tlProvincia' value='([^\']*)'/>";
		Pattern pTLProvincia = Pattern.compile(patternTLProvincia);
		String patternTLMunicipio = "<input TYPE='hidden' name='tlMunicipio' value='([^\']*)'/>";
		Pattern pTLMunicipio = Pattern.compile(patternTLMunicipio);
		String patternTLLocalidad = "<input TYPE='hidden' name='tlLocalidad' value='([^\']*)'/>";
		Pattern pTLLocalidad = Pattern.compile(patternTLLocalidad);
		String patternTLCodigoPostal = "<input TYPE='hidden' name='tlCodigoPostal' value='([^\']*)'/>";
		Pattern pTLCodigoPostal = Pattern.compile(patternTLCodigoPostal);
		String patternTLDireccion  = "<input TYPE='hidden' name='tlDireccion' value='([^\']*)'/>"; //Capitalize
		Pattern pTLDireccion = Pattern.compile(patternTLDireccion);
		String patternTLNumero = "<input TYPE='hidden' name='tlDir_Numero' value='([^\']*)'/>"; // Numero
		Pattern pTLNumero = Pattern.compile(patternTLNumero);
		
		
		// ==========================================		
		// PROBLEMAS CON ACENTOS 
		// ------------------------------------------
		
		File outputFile = new File(outputFileNamePath);
		if (outputFile.exists()) {
			outputFile.delete(); 
		}
		
		// Cabecera 
		String cabecera = "Código;Código Generico Centro;Nombre Centro;Area Territorial;Titularidad;Tel;Fax;Web;E-Mail;Provincia;Municipio;Localidad;Código Postal;Dirección;Número";
		try { 
			Files.append(cabecera + "\n", outputFile, Charsets.ISO_8859_1);
		} catch (IOException e) {
			e.printStackTrace();
		}  			
			
		
		String SEPARATOR = ";";
		for (int i = 0; i < listFiles.length; i++) {
			String nameFile = listFiles[i]; 
			File file = new File(inputDirectoryNamePath + "/" + nameFile);
			
			if (file.exists() && file.length() > 0) {
				try {
					
					/* 
					 * 11.Mayo.2012 
					 * 
					 * Importante. Existia un problema con los acentos,
					 * antes el Charsets era UTF-8 y ahora 
					 * es ISO-8859-1.   
					 *     
					 */ 					
					String line = Files.readFirstLine(file, Charsets.ISO_8859_1);					
					
					
					String tlCodigo = "";
					String tlGenericoCentro = "";
					String tlNombreCentro = ""; 
					String tlAreaTerritorial = "";
					String tlTitularidad = ""; 
					String tlTel = ""; 
					String tlFax = "";
					String tlWeb = ""; 
					String tlMail = ""; 
					String tlProvincia = "";
					String tlMunicipio = ""; 
					String tlLocalidad = "";
					String tlCodigoPostal = "";
					String tlDireccion = "";
					String tlNumero = "";
					
					StringBuffer outputLine = new StringBuffer(); 
					
					Matcher m = pTLCodigo.matcher(line);
					if (m.find()) {
						tlCodigo = m.group(1);
						outputLine.append(Strings.nullToEmpty(tlCodigo));  
					}					
					outputLine.append(SEPARATOR); 
					
					m = pTLGenericoCentro.matcher(line);
					if (m.find()) {
						tlGenericoCentro = m.group(1);
						outputLine.append(Strings.nullToEmpty(tlGenericoCentro));
					}					
					outputLine.append(SEPARATOR);
					
					m = pTLNombreCentro.matcher(line);
					if (m.find()) {
						tlNombreCentro = m.group(1);
						outputLine.append(Strings.nullToEmpty(tlNombreCentro).toUpperCase());
					}										
					outputLine.append(SEPARATOR); 
					
					m = pTLAreaTerritorial.matcher(line);
					if (m.find()) {
						tlAreaTerritorial = m.group(1);
						outputLine.append(Strings.nullToEmpty(tlAreaTerritorial));
					}					
					outputLine.append(SEPARATOR);
					
					m = pTLTitularidad.matcher(line);
					if (m.find()) {
						tlTitularidad = m.group(1);
						outputLine.append(Strings.nullToEmpty(tlTitularidad));
						//System.out.println(tlTitularidad);
					}
					outputLine.append(SEPARATOR); 
										
					m = pTLTel.matcher(line);
					if (m.find()) {
						tlTel = m.group(1);
						outputLine.append(Strings.nullToEmpty(tlTel));
						//System.out.println(tlTel);
					}
					outputLine.append(SEPARATOR);

					m = pTLFax.matcher(line);
					if (m.find()) {
						tlFax = m.group(1);
						outputLine.append(Strings.nullToEmpty(tlFax));
					}
					outputLine.append(SEPARATOR);
					
					m = pTLWeb.matcher(line);
					if (m.find()) {
						tlWeb = m.group(1);
						outputLine.append(Strings.nullToEmpty(tlWeb));
					}					
					outputLine.append(SEPARATOR);

					m = pTLMail.matcher(line);
					if (m.find()) {
						tlMail = m.group(1);
						if (tlMail.equals("null")) tlMail = "";
						outputLine.append(Strings.nullToEmpty(tlMail));
					}
					outputLine.append(SEPARATOR);
					
					m = pTLProvincia.matcher(line);
					if (m.find()) {
						tlProvincia = m.group(1);
						outputLine.append(Strings.nullToEmpty(tlProvincia).toUpperCase());
					}
					outputLine.append(SEPARATOR);
					
					m = pTLMunicipio.matcher(line);
					if (m.find()) {
						tlMunicipio = m.group(1);
						outputLine.append(Strings.nullToEmpty(tlMunicipio).toUpperCase());
					}
					outputLine.append(SEPARATOR);

					m = pTLLocalidad.matcher(line);
					if (m.find()) {
						tlLocalidad = m.group(1);
						//System.out.println("" + tlLocalidad);
						outputLine.append(Strings.nullToEmpty(tlLocalidad).toUpperCase());
					}
					outputLine.append(SEPARATOR);
					
					m = pTLCodigoPostal.matcher(line);
					if (m.find()) {
						tlCodigoPostal = m.group(1);
						outputLine.append(Strings.nullToEmpty(tlCodigoPostal));
					}
					outputLine.append(SEPARATOR);
					
					m = pTLDireccion.matcher(line);
					if (m.find()) {
						tlDireccion = m.group(1);
						outputLine.append(Strings.nullToEmpty(tlDireccion).toUpperCase());
					}
					outputLine.append(SEPARATOR);
					
					m = pTLNumero.matcher(line);
					if (m.find()) {
						tlNumero = m.group(1);
						outputLine.append(Strings.nullToEmpty(tlNumero));
					}					
					System.out.println(outputLine.toString());										
					Files.append(outputLine + "\n", outputFile, Charsets.ISO_8859_1); 
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  			
			}
		}
		
		System.out.println("Ficheros : " + listFiles.length);
		
		//System.out.println(String.format("Processed registers %s ", resultado));
		System.out.println(String.format(
				"Complete process. Elapsed time %d min, %d sec.",
				stopWatch.elapsedTime(TimeUnit.MINUTES),
				stopWatch.elapsedTime(TimeUnit.SECONDS)));
		System.out.println("");

	}
}

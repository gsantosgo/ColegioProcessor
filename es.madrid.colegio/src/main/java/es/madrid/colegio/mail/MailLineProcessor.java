package es.madrid.colegio.mail;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.io.LineProcessor;

public class MailLineProcessor implements LineProcessor<String> {
	public final String SEPARATOR = ";";
	private int lineCount = 0;
	private int lineCountCorrect = 0;

	/**
	 * Constructor 
	 */
	public MailLineProcessor() {
	}

	/**
	 * Procesando linea 
	 */
	public boolean processLine(String line) throws IOException {
		if (!Strings.isNullOrEmpty(line)) {
			String[] columns = line.split(SEPARATOR);
			if (columns.length > 0) {
				String emailAddress = columns[8].trim(); 
				lineCount++;
												
				/** 
				 * Puede terminar en ",", 
				 * puede haber varias direcciones separadas por ",". 
				 */
								
				Iterable<String> listEmailAddress = Splitter.on(',').trimResults().omitEmptyStrings().split(emailAddress);
				for (String email : listEmailAddress) {					
					if (emailAddress.length() > 0  && isValidEmailAddress(email)) {
						System.out.println("> " + email);
						lineCountCorrect++;
						
				        EnviadorMail EnviadorMail = new EnviadorMail("gsantosgo@yahoo.es",
				                "Este es el asunto de mi correo", "Este es el cuerpo de mi correo");						
					}
					else {
						System.out.println("Incorrecta>" + email);
					}					
				}
								
			}
		}
		return true;
	}


	/**
	 * Resultado 
	 */
	public String getResult() {
		return new StringBuffer().append("Total: " + lineCount).append("\n").append("Total Correctas: " + lineCountCorrect).toString();
	}
	

	/**
	 * Comprueba si la dirección correo electrónico es correcta. 
	 * 
	 * @param emailAddress
	 * @return
	 */
	private boolean isValidEmailAddress(String emailAddress){  
		   String  expression="^[\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";  
		   CharSequence inputStr = emailAddress;  
		   Pattern pattern = Pattern.compile(expression,Pattern.CASE_INSENSITIVE);  
		   Matcher matcher = pattern.matcher(inputStr);  
		   return matcher.matches();  
		  
		 }  	

}
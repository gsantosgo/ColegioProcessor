package es.madrid.colegio;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestRegularExpression {

	/**
	 * Programa principal
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		// String input =
		// "<input type=\"hidden\" name=\"tlCodigo\" value=\"28000157\" />";
		String input = "<input TYPE='hidden' name='tlTitularidad' value='Privado Concertado'/>";

		// String pattern =
		// "<input type=\"hidden\" name=\"tlCodigo\" value=\"([^\"]*)\" />";
		String pattern = "<input TYPE='hidden' name='tlTitularidad' value='([^\"]*)'/>";

		// String pattern ="<input value=\"([^\"]*)\" />";

		// create pattern object
		Pattern p = Pattern.compile(pattern);
		// create mather object
		Matcher m = p.matcher(input);

		if (m.find()) {
			/*
			 * System.out.println(m.start());
			 * System.out.println(m.groupCount());
			 * System.out.println(m.group());
			 * System.out.println(m.hasAnchoringBounds());
			 * System.out.println(m.group(0));
			 */
			System.out.println(m.group(1));
		}

	}

}

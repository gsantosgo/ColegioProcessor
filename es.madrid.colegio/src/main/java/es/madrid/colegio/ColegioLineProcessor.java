package es.madrid.colegio;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.common.base.Strings;
import com.google.common.io.LineProcessor;

/**
 * Colegio LineProcessor
 * 
 * @author Guillermo Santos (gsantosgo@yahoo.es)
 */
public class ColegioLineProcessor implements LineProcessor<String> {
	public final String SEPARATOR = ";";
	private int lineCount = 0;

	/**
	 * Constructor 
	 */
	public ColegioLineProcessor() {
	}

	/**
	 * 
	 */
	public boolean processLine(String line) throws IOException {
		if (!Strings.isNullOrEmpty(line)) {
			String[] columns = line.split(SEPARATOR);
			if (columns.length > 0) {
				String codCentro = columns[1];
				lineCount++;

				System.out.println(">" + codCentro);
				conexionPOST(
						"http://gestiona.madrid.org/wpad_pub/run/j/MostrarFichaCentro.icm",
						codCentro);
				System.out.println(">fin " + codCentro);
				/*
				 * String name = columns[3]; if
				 * (organization.equals(this.filter) &&
				 * !Strings.isNullOrEmpty(name)) { lineCount++;
				 * Files.append(name + "\n", this.file, Charsets.UTF_8);
				 */
			}
		}
		return true;
	}

	public String getResult() {
		return new StringBuffer().append(lineCount).toString();
	}

	/**
	 * Conexion POST
	 * 
	 * @param request
	 * @return
	 */
	private void conexionPOST(String request, String codCentro) {
		DataOutputStream out = null;
		BufferedReader rd = null;
		Writer writeFile = null;

		try {

			URL url = new URL(request);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			// Escribir los parametros en el mensaje
			conn.setDoOutput(true);
			out = new DataOutputStream(conn.getOutputStream());
			StringBuffer contenido = new StringBuffer("");
			contenido.append("cdCentro=");
			contenido.append(codCentro);
			contenido.append("&cdEspecialidad=");
			contenido.append("-2");
			contenido.append("&cdFamilia=");
			contenido.append("-2");
			contenido.append("&cdGenerico=");
			contenido.append("null");
			contenido.append("&cdLegislacionSE=");
			contenido.append("LOGSE");
			contenido.append("&cdMuni=");
			contenido.append("null");
			contenido.append("&cdNivelEdu=");
			contenido.append("6032");
			contenido.append("&cdTramoEdu=");
			contenido.append("0020");
			contenido.append("&consultarCentroUnico=");
			contenido.append("false");
			contenido.append("&dsCentro=");
			contenido.append("null");
			contenido.append("&formularioConsulta=");
			contenido.append("centrosGeneral");
			contenido.append("&recargaDatos=");
			contenido.append("");
			contenido.append("&sinboton=");
			contenido.append("S");
			contenido.append("&tipoCurso=");
			contenido.append("ADM");
			contenido.append("&titularidadPrivada=");
			contenido.append("S");
			contenido.append("&titularidadPrivadaConc=");
			contenido.append("S");
			out.writeBytes(contenido.toString());
			out.flush();
			out.close();

			File file = new File("src/main/resources/" + codCentro + ".htm");
			if (file.exists()) {
				file.delete();
			} else {
				boolean success = file.createNewFile();
				if (success) {
					System.out.println("Crea Fichero: " + file.getName());
				} else {
					System.out.println("No Crea Fichero: " + file.getName());
				}
			}

			// Recibir respuesta
			rd = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			writeFile = new FileWriter(file);
			String line;
			while ((line = rd.readLine()) != null) {
				// Process line...
				writeFile.write(line);
			}
			System.out.println("aaaaaaaa");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null)
					out.close();
				if (rd != null)
					rd.close();
				if (writeFile != null)
					writeFile.close();

			} catch (IOException ex) {
				System.out
						.println("Exception al cerrar el lector o el escritor");
				ex.printStackTrace();
			}

		}
	}

	/*
	 * public void postData(String codCentro) { // Create a new HttpClient and
	 * Post Header HttpPost httpPost = new HttpPost(
	 * "http://gestiona.madrid.org/wpad_pub/run/j/MostrarFichaCentro.icm");
	 * 
	 * httpPost.addHeader("Referer",
	 * "http://gestiona.madrid.org/wpad_pub/run/j/ListadoConsultaGeneral.icm");
	 * httpPost.addHeader("User-Agent",
	 * "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; WOW64; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; InfoPath.3)"
	 * );
	 * 
	 * 
	 * try { // Add your data System.out.println("Codcentro:" + codCentro);
	 * 
	 * 
	 * List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
	 * nameValuePairs.add(new BasicNameValuePair("cdCentro", codCentro));
	 * nameValuePairs.add(new BasicNameValuePair("cdEspecialidad", "-2"));
	 * nameValuePairs.add(new BasicNameValuePair("cdFamilia", "-2"));
	 * nameValuePairs.add(new BasicNameValuePair("cdGenerico", "null"));
	 * nameValuePairs.add(new BasicNameValuePair("cdLegislacionSE", "LOGSE"));
	 * nameValuePairs.add(new BasicNameValuePair("cdMuni","null"));
	 * nameValuePairs.add(new BasicNameValuePair("cdNivelEdu","6032"));
	 * nameValuePairs.add(new BasicNameValuePair("cdTramoEdu","0020"));
	 * nameValuePairs.add(new
	 * BasicNameValuePair("consultarCentroUnico","false"));
	 * nameValuePairs.add(new BasicNameValuePair("dsCentro","null"));
	 * nameValuePairs.add(new BasicNameValuePair("formularioConsulta",
	 * "centrosGeneral")); nameValuePairs.add(new
	 * BasicNameValuePair("recargaDatos","")); nameValuePairs.add(new
	 * BasicNameValuePair("sinboton","S")); nameValuePairs.add(new
	 * BasicNameValuePair("tipoCurso","ADM")); nameValuePairs.add(new
	 * BasicNameValuePair("titularidadPrivada","S")); nameValuePairs.add(new
	 * BasicNameValuePair("titularidadPrivadaConc","S")); httpPost.setEntity(new
	 * UrlEncodedFormEntity(nameValuePairs));
	 * 
	 * // Execute HTTP Post Request HttpResponse response =
	 * httpClient.execute(httpPost); StatusLine statusLine =
	 * response.getStatusLine();
	 * 
	 * if (statusLine.getStatusCode() == 200) { System.out.println("" +
	 * statusLine.getStatusCode()); System.out.println("" +
	 * statusLine.getReasonPhrase()); // Get hold of the response entity
	 * HttpEntity entity = response.getEntity(); if (entity != null) {
	 * System.out.println("Longitud " + entity.getContentLength());
	 * 
	 * InputStream instream = entity.getContent(); try { File file = new
	 * File("src/main/resources/" + codCentro + ".htm"); if (file.exists()) {
	 * file.delete(); } else { boolean success = file.createNewFile(); if
	 * (success) { System.out.println("Crea Fichero: " +file.getName()); } else
	 * { System.out.println("No Crea Fichero: " + file.getName()); } }
	 * 
	 * BufferedReader reader = new BufferedReader( new
	 * InputStreamReader(instream));
	 * 
	 * Writer writeFile = new FileWriter(file); String str; while (null != ((str
	 * = reader.readLine()))) { writeFile.write(str); } reader.close();
	 * writeFile.close();
	 * 
	 * 
	 * } catch (IOException ex) {
	 * 
	 * // In case of an IOException the connection will be // released // back
	 * to the connection manager automatically throw ex;
	 * 
	 * } catch (RuntimeException ex) {
	 * 
	 * // In case of an unexpected exception you may want to // abort // the
	 * HTTP request in order to shut down the underlying // connection and
	 * release it back to the connection // manager. httpPost.abort(); throw ex;
	 * 
	 * } finally {
	 * 
	 * // Closing the input stream will trigger connection // release
	 * instream.close(); } } } else {
	 * System.out.println(statusLine.getStatusCode()); }
	 * 
	 * } catch (ClientProtocolException e) { // TODO Auto-generated catch block
	 * } catch (IOException e) { // TODO Auto-generated catch block } }
	 */
}
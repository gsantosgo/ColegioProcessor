package es.madrid.colegio.mail;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;

import java.io.UnsupportedEncodingException;
import java.util.*;

public class EnviadorMail {
	public static final String DEFAULT_ENCODING = "UTF-8";
	public static final String CONTENT_TYPE_HTML_ENCODING = "text/html; charset=utf-8";
	public static final String CONTENT_TYPE_TEXT_ENCODING = "text/plain; charset=utf-8";
	public static final String DEFAULT_XMAILER = "Servicio Correo Electronico";

	public static final String DEFAULT_FROM = "gsantosgo@gmail.com";
	public static final String DEFAULT_NICK_FROM = "Guillermo Santos";

	// FALA
	public static final String DEFAULT_SUBJECT = "Presentación de Aroa";

	public static final String SEPARATOR_EMAIL = ";";
	public static final String SEPARATOR_NICK = "$";

	final String miCorreo = "gsantosgo@gmail.com";
	final String miContraseña = "xxxxxxxx";
	final String servidorSMTP = "smtp.gmail.com";
	final String puertoEnvio = "465";
	String mailReceptor = null;
	String asunto = null;
	String cuerpo = null;

	public EnviadorMail(String mailReceptor, String asunto, String cuerpo) {
		this.mailReceptor = mailReceptor;
		this.asunto = asunto;
		this.cuerpo = cuerpo;

		Properties props = new Properties();
		props.put("mail.smtp.user", miCorreo);
		props.put("mail.smtp.host", servidorSMTP);
		props.put("mail.smtp.port", puertoEnvio);
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.socketFactory.port", puertoEnvio);
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.socketFactory.fallback", "false");

		SecurityManager security = System.getSecurityManager();

		try {
			Authenticator auth = new autentificadorSMTP();
			Session session = Session.getInstance(props, auth);
			// session.setDebug(true);

			MimeMessage msg = new MimeMessage(session);
			msg.setText(cuerpo);
			msg.setSubject(asunto);
			msg.setFrom(new InternetAddress(miCorreo));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
					mailReceptor));
			Transport.send(msg);
		} catch (Exception mex) {
			mex.printStackTrace();
		}

	}

	public static boolean SendEmail(String mailHost, String type, int priority,
			boolean ackRead, String from, String to, String cc, String cco,
			String subject, String content, String pathFileLogo) {

		String contentTypeEnconding = CONTENT_TYPE_HTML_ENCODING;
		if ((mailHost != null) && (mailHost.length() > 0)) {
			Properties props = new Properties();
			props.put("mail.smtp.host", mailHost);
			javax.mail.Session sessionMail = javax.mail.Session
					.getDefaultInstance(props);

			try {
				MimeMessage message = new MimeMessage(sessionMail);
				message.addHeader("Content-Type", contentTypeEnconding);
				if ((type != null) && type.equals("text"))
					contentTypeEnconding = CONTENT_TYPE_TEXT_ENCODING;
				message.addHeader("Content-Type", contentTypeEnconding);
				message.addHeader("X-Mailer", DEFAULT_XMAILER);

				// From ======
				InternetAddress addressFrom = null;
				if (from != null && from.length() > 0) {
					StringTokenizer stFrom = new StringTokenizer(from,
							SEPARATOR_NICK);
					String emailFrom = "";
					String nickNameFrom = "";
					if (stFrom.hasMoreElements())
						emailFrom = stFrom.nextToken();
					if (stFrom.hasMoreElements())
						nickNameFrom = stFrom.nextToken();
					addressFrom = new InternetAddress(emailFrom, nickNameFrom,
							DEFAULT_ENCODING);
					message.setFrom(addressFrom);
				} else {
					addressFrom = new InternetAddress(DEFAULT_FROM,
							DEFAULT_NICK_FROM);
				}

				// Aviso de lectura =====
				if (ackRead) {
					message.addHeader("Disposition-Notification-To",
							addressFrom.getAddress());
				}
				if ((to != null && to.length() > 0)
						|| (cc != null && cc.length() > 0)
						|| (cco != null && cco.length() > 0)) {

					// To ======
					if (to != null && to.length() > 0) {
						StringTokenizer stTo = new StringTokenizer(to,
								SEPARATOR_EMAIL);
						InternetAddress address[] = new InternetAddress[stTo
								.countTokens()];
						for (int i = 0; stTo.hasMoreTokens(); i++) {
							String cadena = stTo.nextToken();
							StringTokenizer stCadena = new StringTokenizer(
									cadena, SEPARATOR_NICK);
							String email = "";
							String nickName = "";
							if (stCadena.hasMoreElements())
								email = stCadena.nextToken();
							if (stCadena.hasMoreElements())
								nickName = stCadena.nextToken();
							address[i] = new InternetAddress(email, nickName,
									DEFAULT_ENCODING);
						}

						message.setRecipients(
								javax.mail.Message.RecipientType.TO, address);
					}

					// CC ======
					if (cc != null && cc.length() > 0) {
						StringTokenizer stCc = new StringTokenizer(cc,
								SEPARATOR_EMAIL);
						InternetAddress address[] = new InternetAddress[stCc
								.countTokens()];
						for (int i = 0; stCc.hasMoreTokens(); i++) {
							String cadena = stCc.nextToken();
							StringTokenizer stCadena = new StringTokenizer(
									cadena, SEPARATOR_NICK);
							String email = "";
							String nickName = "";
							if (stCadena.hasMoreElements())
								email = stCadena.nextToken();
							if (stCadena.hasMoreElements())
								nickName = stCadena.nextToken();
							address[i] = new InternetAddress(email, nickName,
									DEFAULT_ENCODING);
						}
						message.setRecipients(
								javax.mail.Message.RecipientType.CC, address);
					}

					// CCO ======
					if (cco != null && cco.length() > 0) {
						StringTokenizer stCco = new StringTokenizer(cco,
								SEPARATOR_EMAIL);
						InternetAddress address[] = new InternetAddress[stCco
								.countTokens()];
						for (int i = 0; stCco.hasMoreTokens(); i++) {
							String cadena = stCco.nextToken();
							StringTokenizer stCadena = new StringTokenizer(
									cadena, SEPARATOR_NICK);
							String email = "";
							String nickName = "";
							if (stCadena.hasMoreElements())
								email = stCadena.nextToken();
							if (stCadena.hasMoreElements())
								nickName = stCadena.nextToken();
							address[i] = new InternetAddress(email, nickName,
									DEFAULT_ENCODING);
						}
						message.setRecipients(
								javax.mail.Message.RecipientType.BCC, address);
					}

					// Subject ====
					if (subject != null && subject.length() > 0) {
						message.setSubject(subject, DEFAULT_ENCODING);
					} else {
						message.setSubject(DEFAULT_SUBJECT, DEFAULT_ENCODING);
					}
					// Fecha y hora =====
					message.setSentDate(new Date());

					MimeMultipart multipart = new MimeMultipart("related");

					// Primera parte, mensaje HTML
					BodyPart messageBodyPart = new MimeBodyPart();
					messageBodyPart.setHeader("Content-Type",
							contentTypeEnconding);

					// ================================================
					// Nota. El parametro contentTypeEncoding
					// para que palabras con acentos, caracteres
					// raros (ÁÉÍÓÚáéíóú€) salgan adecuadamente.
					// Contenido ====
					if (content != null && content.length() > 0)
						messageBodyPart.setContent(content,
								contentTypeEnconding);
					multipart.addBodyPart(messageBodyPart);

					// Segunda parte, incluir logo
					if (!pathFileLogo.equals("")) {
						messageBodyPart = new MimeBodyPart();
						DataSource fds = new FileDataSource(pathFileLogo);
						messageBodyPart.setDataHandler(new DataHandler(fds));
						messageBodyPart.setHeader("Content-ID", "logo");
						multipart.addBodyPart(messageBodyPart);
					}

					message.setHeader("Content-Type", contentTypeEnconding);
					message.setContent(multipart);
					message.setSentDate(new Date());

					// Envio del Mensaje =====
					Transport.send(message);
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return false;
			} catch (MessagingException mex) {
				System.out.println("Excepcion: Envio Mail ......");
				System.out.println(mex.getMessage());
				mex.printStackTrace();
				System.out.println();
				Exception excepcion = mex;
				do { // Excepcion
					if (excepcion instanceof NoSuchProviderException) {
						NoSuchProviderException nspe = (NoSuchProviderException) excepcion;
						System.out.println(" NoSuchProviderException. "
								+ nspe.getMessage());
					}
					// Excepcion Fallo Envio
					if (excepcion instanceof SendFailedException) {
						SendFailedException sfex = (SendFailedException) excepcion;
						javax.mail.Address invalid[] = sfex
								.getInvalidAddresses();
						if (invalid != null) {
							System.out
									.println(" Direcciones que no recibieron el mensaje ");
							if (invalid != null) {
								for (int i = 0; i < invalid.length; i++) {
									System.out.println("         "
											.concat(String.valueOf(String
													.valueOf(invalid[i]))));
								}

							}
						}
						javax.mail.Address validSent[] = sfex
								.getValidSentAddresses();
						if (validSent != null) {
							System.out
									.println(" Direcciones al que fue el mensaje enviado con exito:");
							if (validSent != null) {
								for (int i = 0; i < validSent.length; i++) {
									System.out.println("         "
											.concat(String.valueOf(String
													.valueOf(validSent[i]))));
								}

							}
						}
						javax.mail.Address validUnsent[] = sfex
								.getValidUnsentAddresses();
						if (validUnsent != null) {
							System.out
									.println(" Direcciones que son validas, pero el mensaje no fue enviado con exito:");
							if (validUnsent != null) {
								for (int i = 0; i < validUnsent.length; i++) {
									System.out.println("         "
											.concat(String.valueOf(String
													.valueOf(validUnsent[i]))));
								}

							}
						}
					}
				} while ((excepcion = ((MessagingException) excepcion)
						.getNextException()) != null);
				return true;
			} finally {
				/*
				 * if (transport != null) { try { transport.close(); } catch
				 * (Exception e) { e.printStackTrace(); } }
				 */
			}
			return true;
		} else {
			return false;
		}
	}

	private class autentificadorSMTP extends javax.mail.Authenticator {
		public PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(miCorreo, miContraseña);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EnviadorMail EnviadorMail = new EnviadorMail("dire@dire",
				"Este es el asunto de mi correo",
				"Este es el cuerpo de mi correo");
	}

}
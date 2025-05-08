package org.palomafp.examen;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.*;

public class GestorExamenes {

	private Map<String, List<String>> bancoPreguntas = new HashMap<>();
	private Map<String, List<String>> alumnos = new LinkedHashMap<>();

	/*
	 * public void cargarBancoPreguntasCSV(String rutaCSV) { bancoPreguntas.clear();
	 * // Limpiar contenido anterior
	 * 
	 * File archivo = new File(rutaCSV);
	 * 
	 * // Leer las preguntas del archivo CSV InputStream is =
	 * getClass().getResourceAsStream(rutaCSV); BufferedReader br;
	 * 
	 * try { br= new BufferedReader(new InputStreamReader(is));
	 * 
	 * String linea; while ((linea = br.readLine()) != null) {
	 * System.out.println(linea); String[] partes = linea.split(";", 2); if
	 * (partes.length < 2) continue;
	 * 
	 * String ra = partes[0].trim(); String pregunta = partes[1].trim();
	 * 
	 * if (!ra.isEmpty() && !pregunta.isEmpty()) {
	 * bancoPreguntas.computeIfAbsent(ra, k -> new ArrayList<>()).add(pregunta); } }
	 * } catch (IOException e) { e.printStackTrace(); } }
	 */

	public void cargarAlumnos(String file) {
		try {
			alumnos = cargarDesdeCSV(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (Map.Entry<String, List<String>> entry : alumnos.entrySet()) {
			System.out.println(entry.getKey() + " -> " + entry.getValue());
		}

	}

	public void generarExamenes() throws Exception {
		for (Map.Entry<String, List<String>> entry : alumnos.entrySet()) {
			String nombre = entry.getKey();
			List<String> ras = entry.getValue();
			guardarExamenPdf(nombre, ras);
		}
	}

	private void guardarExamenPdf(String nombre, List<String> ras) throws Exception {
		String fileName = generarCabecera(nombre);
		
		String fileNameSalida=fileName.replace(".pdf","_2.pdf");
		
		//Añadimos las preguntas
		PdfReader reader = new PdfReader(fileName);
		Document documento = new Document(reader.getPageSizeWithRotation(1));
		PdfCopy copy = new PdfCopy(documento, new FileOutputStream(fileNameSalida));
        documento.open();
        PdfImportedPage page = copy.getImportedPage(reader, 1);
        copy.addPage(page);

        //añade los RAs
		for (String ra : ras) {

			// Crea el lector del PDF existente
			PdfReader readerRA = new PdfReader(ra + ".pdf");

	
			// Importar cada página del PDF original
			for (int i = 1; i <= readerRA.getNumberOfPages(); i++) {
				PdfImportedPage page2 = copy.getImportedPage(readerRA, i);
                copy.addPage(page2);
			}

			readerRA.close();
		}

		documento.close();
	}

	/**
	 * @param nombre
	 * @return
	 * @throws DocumentException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws BadElementException
	 * @throws MalformedURLException
	 */
	private String generarCabecera(String nombre)
			throws DocumentException, FileNotFoundException, IOException, BadElementException, MalformedURLException {
		String fileName = "examenes/" + nombre.replace(" ", "_") + ".pdf";
		File dir = new File("examenes");
		if (!dir.exists())
			dir.mkdir();

		Document document = new Document();
		PdfWriter writer =PdfWriter.getInstance(document, new FileOutputStream(fileName));
		document.open();

		// Cargar la imagen desde disco
		InputStream is = getClass().getResourceAsStream("/img/IESPaloma.png");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		byte[] buffer = new byte[1024];
		int bytesRead;
		while ((bytesRead = is.read(buffer)) != -1) {
			baos.write(buffer, 0, bytesRead);
		}
		is.close();

		Image imagen = Image.getInstance(baos.toByteArray());

		// Opcional: redimensionar la imagen
		imagen.scaleToFit(400, 194); // ancho x alto máximos

		// Opcional: posicionar centrado
		imagen.setAlignment(Image.ALIGN_CENTER);

		// Añadir al documento
		document.add(imagen);

		Font fontTitle = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
		Font fontText = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
		Font fontBold = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);

		Paragraph title = new Paragraph("Examen de Programación 1AMM 2", fontTitle);
		title.setSpacingAfter(20f); // espacio en puntos

		document.addAuthor("Arturo Martinez");
		document.addCreationDate();

		title.setAlignment(Element.ALIGN_CENTER);
		document.add(title);

		document.add(new Paragraph("Alumno:       " + nombre, fontBold));
		document.add(new Paragraph("", fontText));
		document.add(new Paragraph("", fontText));
		document.add(new Paragraph("Fecha: __________________________", fontBold));
		document.add(new Paragraph("", fontText));
		document.add(new Paragraph(" ", fontText));

		document.close();
		return fileName;
	}

	private Map<String, List<String>> cargarDesdeCSV(String rutaArchivo) throws IOException {
		Map<String, List<String>> alumnosRA = new LinkedHashMap<>();

		InputStream is = getClass().getResourceAsStream(rutaArchivo);
		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(is));
			String headerLine = reader.readLine();
			if (headerLine == null)
				return alumnosRA;

			String[] headers = headerLine.split(",");

			String line;
			while ((line = reader.readLine()) != null) {
				String[] campos = line.split(",");
				String alumno = campos[0] + " " + campos[1];
				List<String> ras = new ArrayList<>();

				for (int i = 2; i < campos.length; i++) {
					if (campos[i].trim().equalsIgnoreCase("1")) {
						ras.add(headers[i]);
					}
				}

				alumnosRA.put(alumno, ras);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return alumnosRA;
	}

}

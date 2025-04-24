package org.palomafp.examen;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class GestorExamenes {

	 private  Map<String, List<String>> bancoPreguntas = new HashMap<>();
	 private  Map<String, List<String>> alumnos = new LinkedHashMap<>();

	
	 public  void cargarBancoPreguntas() {
	        bancoPreguntas.put("RA1", List.of(
	            "Explica qué es una variable en Java.",
	            "Escribe un programa que imprima 'Hola Mundo'.",
	            "¿Qué tipos de datos primitivos hay en Java?"
	        ));

	        bancoPreguntas.put("RA2", List.of(
	            "Explica el concepto de clase y objeto.",
	            "Implementa una clase Coche con atributos y métodos.",
	            "¿Qué es la encapsulación en POO?"
	        ));

	        bancoPreguntas.put("RA3", List.of(
	            "Escribe un ejemplo de estructura if-else.",
	            "¿Cómo funciona un bucle for en Java?",
	            "Haz un programa con while que sume números hasta 100."
	        ));
	        
	        bancoPreguntas.put("RA4", List.of(
	                "Escribe un ejemplo de estructura if-else.",
	                "¿Cómo funciona un bucle for en Java?",
	                "Haz un programa con while que sume números hasta 100."
	            ));
	        
	        bancoPreguntas.put("RA5", List.of(
	                "Escribe un ejemplo de estructura if-else.",
	                "¿Cómo funciona un bucle for en Java?",
	                "Haz un programa con while que sume números hasta 100."
	            ));
	        
	        bancoPreguntas.put("RA6", List.of(
	                "Escribe un ejemplo de estructura if-else.",
	                "¿Cómo funciona un bucle for en Java?",
	                "Haz un programa con while que sume números hasta 100."
	            ));
	        
	        bancoPreguntas.put("RA7", List.of(
	                "Escribe un ejemplo de estructura if-else.",
	                "¿Cómo funciona un bucle for en Java?",
	                "Haz un programa con while que sume números hasta 100."
	            ));
	    }

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

	 public  void generarExamenes() throws Exception {
	        for (Map.Entry<String, List<String>> entry : alumnos.entrySet()) {
	            String nombre = entry.getKey();
	            List<String> ras = entry.getValue();

	            List<String> preguntas = new ArrayList<>();
	            

	            for (String ra : ras) {
	                List<String> listaPreguntas = new ArrayList<>(bancoPreguntas.getOrDefault(ra, List.of()));
	                Collections.shuffle(listaPreguntas);
	                preguntas.addAll(listaPreguntas.subList(0, Math.min(2, listaPreguntas.size())));
	            }

	            guardarExamenPdf(nombre, preguntas);
	        }
	    }

	 private void guardarExamenPdf(String nombre, List<String> preguntas) throws Exception {
	        String fileName = "examenes/" + nombre.replace(" ", "_") + ".pdf";
	        File dir = new File("examenes");
	        if (!dir.exists()) dir.mkdir();

	        Document document = new Document();
	        PdfWriter.getInstance(document, new FileOutputStream(fileName));
	        document.open();

	        Font fontTitle = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
	        Font fontText = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);

	        Paragraph title = new Paragraph("Examen de Programación 1AMM 2", fontTitle);
	        
	        document.addSubject("Tituto");
	        document.addAuthor("Arturo Martinez");
	        document.addCreationDate();
	        document.addHeader("Header1", "Header");
	        document.addTitle("Titulo");
	        
	        
	        
	        
	        title.setAlignment(Element.ALIGN_CENTER);
	        document.add(title);

	        document.add(new Paragraph("Alumno: " + nombre, fontText));
	        document.add(new Paragraph("", fontText));
	        document.add(new Paragraph("Fecha: __________________________", fontText));
	        document.add(new Paragraph("", fontText));
	        document.add(new Paragraph(" ", fontText));

	        int num = 1;
	        for (String pregunta : preguntas) {
	            document.add(new Paragraph(num++ + ". " + pregunta, fontText));
	            document.add(new Paragraph(" ", fontText));
	        }

	        document.close();
	    }
	    
	    
	    private  Map<String, List<String>> cargarDesdeCSV(String rutaArchivo) throws IOException {
	        Map<String, List<String>> alumnosRA = new LinkedHashMap<>();

	        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {
	            String headerLine = reader.readLine();
	            if (headerLine == null) return alumnosRA;

	            String[] headers = headerLine.split(",");
	            
	            String line;
	            while ((line = reader.readLine()) != null) {
	                String[] campos = line.split(",");
	                String alumno = campos[0]+" "+campos[1];
	                List<String> ras = new ArrayList<>();

	                for (int i = 2; i < campos.length; i++) {
	                    if (campos[i].trim().equalsIgnoreCase("1")) {
	                        ras.add(headers[i]);
	                    }
	                }

	                alumnosRA.put(alumno, ras);
	            }
	        }

	        return alumnosRA;
	    }
	    

}

package org.palomafp.examen;

public class Examenes {

    public static void main(String[] args) throws Exception {
    	GestorExamenes gestor=new GestorExamenes();
    	gestor.cargarBancoPreguntasCSV("/config/preguntas.csv");
    	gestor.cargarAlumnos("/config/Alumnos_RA.csv");
    	gestor.generarExamenes();
    }

    }

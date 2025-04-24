package org.palomafp.examen;

public class GeneradorExamenes {

   
    public static void main(String[] args) throws Exception {
    	GestorExamenes gestor=new GestorExamenes();
    	gestor.cargarBancoPreguntas();
    	gestor.cargarAlumnos("config/Alumnos_RA.csv");
    	gestor.generarExamenes();
    }

    }

package org.palomafp.examen;

public class Examenes {

    public static void main(String[] args) throws Exception {
    	
    	GestorExamenes gestor=new GestorExamenes();
    	gestor.cargarAlumnos("/Alumnos_RA.csv");
    	gestor.generarExamenes();
    	System.gc();
    	Thread.sleep(5000);
    	gestor.borrarTemporales();
    
    }

    }

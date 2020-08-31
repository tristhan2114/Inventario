package com.denisse.inventario.Utils;

public class Constantes {

    public static boolean ISPRODUCTION = false;

    public static final String REQUEST_EMPLEADOS = ISPRODUCTION ? "/EMPLEADOS/" : "/TEST_EMPLEADOS/";
    public static final String REQUEST_INVENTARIOS = ISPRODUCTION ? "/INVENTARIOS/" : "/TEST_INVENTARIOS/";

    public static final String REQUEST_GENEROS = ISPRODUCTION ? "/GENEROS/" : "/TEST_GENEROS/";
    public static final String REQUEST_AREAS = ISPRODUCTION ? "/AREAS/" : "/TEST_AREAS/";
    public static final String REQUEST_JORNADAS = ISPRODUCTION ? "/JORNADAS/" : "/TEST_JORNADAS/";



}

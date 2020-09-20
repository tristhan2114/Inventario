package com.denisse.implemento.Utils;

public class Constantes {

    public static boolean ISPRODUCTION = false;

    public static final String REQUEST_EMPLEADOS = ISPRODUCTION ? "/EMPLEADOS/" : "/TEST_EMPLEADOS/";
    public static final String REQUEST_USUARIO = ISPRODUCTION ? "/USUARIO/" : "/TEST_USUARIO/";
    public static final String REQUEST_IMPLEMENTOS = ISPRODUCTION ? "/IMPLEMENTOS/" : "/TEST_IMPLEMENTOS/";
    public static final String REQUEST_ENTREGAS = ISPRODUCTION ? "/ENTREGAS/" : "/TEST_ENTREGAS/";

    public static final String REQUEST_GENEROS = ISPRODUCTION ? "/GENEROS/" : "/TEST_GENEROS/";
    public static final String REQUEST_AREAS = ISPRODUCTION ? "/AREAS/" : "/TEST_AREAS/";
    public static final String REQUEST_JORNADAS = ISPRODUCTION ? "/JORNADAS/" : "/TEST_JORNADAS/";


    public static final String ROL_ADMINISTRADOR = "Administrador";
    public static final String ROL_JEFE = "Jefe";
    public static final String ROL_ASISTENTE = "Asistente";
    public static final String ROL_INSPECTOR = "Inspector";



}

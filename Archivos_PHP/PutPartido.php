<?php
require_once __DIR__ . '/db_config.php';
	$equipoLocal=$_REQUEST ["eL"];	
	$equipoVisitante=$_REQUEST ["eV"];	
	$direccion=$_REQUEST ["direc"];
	$fecha=$_REQUEST ["fecha"];
	$hora=$_REQUEST ["horaEncuentro"];
	$canchaDe=$_REQUEST ["canchaDe"];

	//echo "variable nombre agregada";
if ($equipo!="" & $direccion!="") {
	$conexion = new mysqli(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);
	//echo "conectada a la BD";
	$sql = "insert INTO partido(equipoLocal, equipoVisitante, id_partido, horaEncuentro, golesEquipoLocal, golesEquipoVisitante, canchaDe, direccion, fecha, ganador, jugado) VALUES ($equipoLocal,$equipoVisitante,0,$hora,0,0,0,$canchaDe,$direccion,fecha,0)";
	//echo "Compiado sql insert";
	if (mysqli_query($conexion, $sql)) {
	 	echo "Insertado";
	 }else{
	 	echo "no incertado";
	 }; // Cerramos la conexion con la base de datos
	
}else{
	echo "faltan campos";
}

?>
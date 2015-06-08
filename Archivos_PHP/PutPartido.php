<?php
require_once __DIR__ . '/db_config.php';
	$eL=$_REQUEST ["eL"];	
	$eV=$_REQUEST ["eV"];
	$canchaDe=$_REQUEST ["canchaDe"];
	$hora=$_REQUEST ["horaEncuentro"];	
	$direccion=$_REQUEST ["direc"];
	$fecha=$_REQUEST ["fecha"];
	//echo "variable nombre agregada";
	$conexion = new mysqli(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);
	//echo "conectada a la BD";
	$sql = "insert into partido (equipoLocal ,equipoVisitante,id_partido ,horaEncuentro ,golesEquipoLocal ,golesEquipoVisitante ,canchaDe ,direccion ,fecha ,ganador ,jugado) VALUES ('$eL','$eV','0' ,'$hora','0',  '0','$canchaDe', '$direccion', '$fecha', 'Empatado','0')";
	//echo "Compiado sql insert";
	if (mysqli_query($conexion, $sql)) {
	 	echo "Insertado";
	 }else{
	 	echo "no incertado";
	 }; // Cerramos la conexion con la base de datos

?>
<?php
require_once __DIR__ . '/db_config.php';
	$equipo=$_REQUEST ["nombreEquipo"];	
	$direccion=$_REQUEST ["direccionCancha"];
	$fechaInicio=$_REQUEST ["fechaInicio"];
	$fechaRegistro=$_REQUEST ["fechaRegistro"];
	$libre=$_REQUEST ["libre"];

	//echo "variable nombre agregada";
if ($equipo!="" & $direccion!="") {
	$conexion = new mysqli(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);
	//echo "conectada a la BD";
	$sql = "insert into equipo(nombreEquipo,direccionCancha,fechaInicio,fechaRegistro,partidosGanados,partidosEmpatados,partidosPerdidos,libre,mesPago) values ('$equipo','$direccion','$fechaInicio','$fechaRegistro','0','0','0','$libre','0000-00-00')";
	//echo "Compiado sql insert";
	if (mysqli_query($conexion, $sql)) {
	 	echo "Insertado";
	 }else{
	 	echo "no insertado";
	 }; // Cerramos la conexion con la base de datos
	
}else{
	echo "faltan campos";
}

?>
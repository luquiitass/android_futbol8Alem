<?php



require_once __DIR__ . '/db_config.php';

$libre=$_REQUEST ["libre"];

$con = new mysqli(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);



if (!$con) {

    echo "no conecto\n";

}





$sql = "select id_partido as part, equipoLocal as eqL, equipoVisitante as eqV, horaEncuentro as hora, golesEquipoLocal as glL, golesEquipoVisitante as glV, canchaDe, direccion ,ganador, fecha, jugado FROM partido where libre='$libre' ";



$datos=array();

$result = mysqli_query($con, $sql);

while ($row=mysqli_fetch_object($result)) {

	$datos[]=$row;

}



$sql = "select nombreEquipo as eq,fechaInicio as fI,fechaRegistro as fR, direccionCancha as dire FROM equipo where libre='$libre'";



$result = mysqli_query($con, $sql);

while ($row=mysqli_fetch_object($result)) {

	$datos[]=$row;

}

 echo json_encode($datos);

?>
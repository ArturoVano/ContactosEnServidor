<?php

$contactos = array("contactos"=>array(array("contacto_id"=>1, "nombre"=>"Achiles", "telefono"=>"6666", "email"=>"achiles@gmail.com"),
array("contacto_id"=>2, "nombre"=>"Basha", "telefono"=>"76543", "email"=>"basha@gmail.com"),
array("contacto_id"=>3, "nombre"=>"Calypso", "telefono"=>"98765432", "email"=>"calypso@gmail.com"))); 

$texto_JSON = json_encode($contactos);
echo $texto_JSON;

?>

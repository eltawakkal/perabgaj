<?php
	
	require 'connection.php';

	$userId = $_POST['user_id'];
	$gaji = $_POST['gaji'];
	$makan = $_POST['makan'];

	$sql = "UPDATE tb_gaji SET gaji = '$gaji', makan = '$makan' WHERE user_id = '$userId'";

	if ($conn->query($sql)) {
		$data = array('status'=>'berhasil');
		echo json_encode($data);
	} else {
		echo $conn->error;
	}
	
?>
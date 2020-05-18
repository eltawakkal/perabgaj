<?php
	
	require 'connection.php';

	$userId = $_POST['user_id'];
	$gaji = $_POST['gaji'];
	$makan = $_POST['makan'];

	$sql = "INSERT INTO tb_gaji (user_id, gaji, makan) VALUES
	('$userId', '$gaji', '$makan')";

	if ($conn->query($sql)) {
		$data = array('status'=>'berhasil');
		echo json_encode($data);
	} else {
		echo $conn->error;
	}
	
?>
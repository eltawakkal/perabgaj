<?php
	
	require 'connection.php';

	$id = $_POST['id'];

	$sql = "DELETE FROM tb_buyer WHERE id = '$id'";
	
	if ($conn->query($sql)) {
		$data = array('status'=>'berhasil');
		echo json_encode($data);
	} else {
		echo $conn->error;
	}
	
?>
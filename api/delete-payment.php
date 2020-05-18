<?php
	
	require 'connection.php';

	$payId = $_POST['pay_id'];

	$sql = "DELETE FROM tb_cicilan WHERE tb_cicilan.id = '$payId'";

	if ($conn->query($sql)) {
		$data = array('status'=>'berhasil');
		echo json_encode($data);
	} else {
		echo $conn->error;
	}
	
?>
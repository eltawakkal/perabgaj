<?php
	
	require 'connection.php';

	$buyerId = $_POST['buyer_id'];
	$houseId = $_POST['house_id'];
	$unit = $_POST['unit'];
	$cicilan = $_POST['cicilan'];
	$transDate = $_POST['trans_date'];
	$type = $_POST['type'];

	$sql = "INSERT INTO tb_cicilan (buyer_id, house_id, unit, cicilan, type, trans_date) VALUES
	('$buyerId', '$houseId', '$unit', '$cicilan', '$type', '$transDate')";

	if ($conn->query($sql)) {
		$data = array('status'=>'berhasil');
		echo json_encode($data);
	} else {
		echo $conn->error;
	}
	
?>
<?php
	
	require 'connection.php';

	date_default_timezone_set("Asia/Jakarta");

	$userId = $_POST['user_id'];
	$dateIn = date('Y-m-d');

	$sql = "SELECT * FROM tb_absen WHERE user_id = $userId AND date_in = '$dateIn'";
	$result = $conn->query($sql);

	$data = array();

	while ($row = $result->fetch_assoc()) {
		$data[] = $row;
	}

	if (count($data) != 0) {
		echo json_encode($data);	
	} else {
		echo "[]";
	}

?>
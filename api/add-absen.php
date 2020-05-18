<?php
	
	require 'connection.php';

	date_default_timezone_set("Asia/Jakarta");

	$dateIn = date("Y-m-d");
	$timeIn = date("H:i");
	$userId = $_POST['user_id'];
	$status = "";
	$type = $_POST['type'];

	if ($type == 0) {
		if ($timeIn > '08:30') { 
			$timeInTamp = strtotime($timeIn);
			$checkInTime = strtotime('8:30');
			$timeLateInMinute = ($timeInTamp - $checkInTime) / 60;

			if ($timeLateInMinute <= 10) {
				$status = 'Terlambat T';
			} else {
				$status = 'Terlambat';
			}
		} else {
			$status = "Tepat Waktu";
		}
	} else {
		$status = "Checkout";
	}

	$sql = "INSERT INTO tb_absen(date_in, time_in, user_id, status, type)
			VALUES ('$dateIn', '$timeIn', '$userId', '$status', '$type')";
	
	$result = $conn->query($sql);

	if ($result) {
		echo "{}";
	} else {
		echo("Gagal");
	}

?>
<?php
	
	require 'connection.php';

	$userId = $_POST['user_id'];
	$dateIn = $_POST['date_in'];

	$dateArr = explode("-", $dateIn);
	$startDate = $dateArr[0] . '-' . ($dateArr[1] -1) . '-25';
	$totDate = $dateArr[0] . '-' . ($dateArr[1]) . '-25';

	$sql = "SELECT ta.id, ta.date_in dateIn, ta.time_in timeIn, tu.name, ta.status, ta.type
			FROM tb_user as tu, tb_absen as ta
			WHERE tu.id = ta.user_id 
            AND ta.user_id = '$userId'
            AND date_in > '$startDate'
            AND date_in <= '$totDate'";

	$result = $conn->query($sql);

	$data = array();
	$fixData = array();

	while ($row = $result->fetch_assoc()) {
		$data[] = $row;
	}

	for ($i=0; $i < count($data); $i++) { 

		$reFormatDate = date("D-d-m-Y", strtotime($data[$i]['dateIn']));
		$dates = explode("-", $reFormatDate);
		$dayInEng = $dates[0];
		$dayInIndo = "";
		$montInIndo = "";

		if ($dayInEng == "Sun") {
			$dayInIndo = "Minggu";
		} elseif ($dayInEng == "Mon") {
			$dayInIndo = "Senin";
		} elseif ($dayInEng == "Tue") {
			$dayInIndo = "Selasa";
		} elseif ($dayInEng == "Wed") {
			$dayInIndo = "Rabu";
		} elseif ($dayInEng == "Thu") {
			$dayInIndo = "Kamis";
		} elseif ($dayInEng == "Fri") {
			$dayInIndo = "Jumat";
		} elseif ($dayInEng == "Sat") {
			$dayInIndo = "Sabtu";
		}


		if ($dates[2] == '01') {
			$montInIndo = "Januari";
		} elseif ($dates[2] == '02') {
			$montInIndo = "Februari";
		} elseif ($dates[2] == '03') {
			$montInIndo = "Maret";
		} elseif ($dates[2] == '04') {
			$montInIndo = "April";
		} elseif ($dates[2] == '05') {
			$montInIndo = "Mei";
		} elseif ($dates[2] == '06') {
			$montInIndo = "Juni";
		} elseif ($dates[2] == '07') {
			$montInIndo = "Juli";
		} elseif ($dates[2] == '08') {
			$montInIndo = "Agustus";
		} elseif ($dates[2] == '09') {
			$montInIndo = "September";
		} elseif ($dates[2] == '10') {
			$montInIndo = "Oktober";
		} elseif ($dates[2] == '11') {
			$montInIndo = "November";
		} elseif ($dates[2] == '12') {
			$montInIndo = "Desember";
		}


		array_push($fixData, array(
			'id' => $data[$i]['id'],
			'dateIn' => $dayInIndo . ', ' . $dates[1] . ' ' . $montInIndo . ' ' . $dates[3],
			'timeIn' => $data[$i]['timeIn'],
			'name' => $data[$i]['name'],
			'status' => $data[$i]['status'],
			'type' => $data[$i]['type']
		));
	}

	echo json_encode($fixData);

?>
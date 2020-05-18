<?php
	
	require 'connection.php';

	$houseId = $_POST['house_id'];

	$sql = "SELECT c1.id, b1.nama, b1.no_telp, b1.unit, c1.cicilan, c1.type, c1.trans_date 
			FROM tb_cicilan c1 
			LEFT JOIN tb_cicilan c2 ON (c1.buyer_id = c2.buyer_id AND c1.id < c2.id) 
			LEFT JOIN tb_buyer b1 ON (b1.id = c1.buyer_id) 
			WHERE c2.id IS NULL AND c1.house_id = '$houseId' AND c1.type !='selesai'";

	$sqlfirstData = "SELECT c1.id, b1.nama, b1.no_telp, b1.unit, c1.cicilan, c1.type, c1.trans_date 
			FROM tb_cicilan c1 
			LEFT JOIN tb_cicilan c2 ON (c1.buyer_id = c2.buyer_id AND c1.id > c2.id) 
			LEFT JOIN tb_buyer b1 ON (b1.id = c1.buyer_id) 
			WHERE c2.id IS NULL AND c1.house_id = '$houseId' AND c1.type !='selesai'";

	$result = $conn->query($sql);
	$resultfirstData = $conn->query($sqlfirstData);

	$data = array();
	$firstData = array();

	$fixData = array();
	$type;
	$status = "lewat";

	while ($row = $result->fetch_assoc()) {
		$data[] = $row;
	}

	while ($row = $resultfirstData->fetch_assoc()) {
		$firstData[] = $row;
	}

	// echo json_encode($data);

	for ($i=0; $i < count($data); $i++) { 
		// $fixDueDate = date('d-m-Y', strtotime($dueDate . '+1 month'));

		// echo $dueDate;

		$day1 = date_create($data[$i]['trans_date']);
		$day2 = date_create(date('Y-m-d'));
		$diff = date_diff($day1, $day2);
		$rangeDate = $diff->days;

		// echo $data[$i]['type'] . '</br>';
		// echo date('d-m-Y', strtotime($data[$i]['trans_date'])) . '</br>';
		// echo date('d-m-Y') . '</br>;';
		// echo $rangeDate . '</br>';

		if ($data[$i]['type'] == 'Booking Fee') {
			$day1 = date_create($data[$i]['trans_date']);
			$day2 = date_create(date('Y-m-d'));
			$diff = date_diff($day1, $day2);
			$rangeDate = $diff->days;

			$type = "Cicilan ke 1";

			if ($rangeDate > 14) {
				$status = "lewat";
			} else {
				$status  = "Pass";
			}
		} else {
			// $day1 = date_create($dueDate);
			// $day2 = date_create(date('Y-m-d'));
			// $diff = date_diff($day1, $day2);
			// $rangeDate = $diff->days;

			$dueDate = date('d', strtotime($firstData[$i]['trans_date'] . '+14 day')) . '-' . date('m-Y', strtotime($data[$i]['trans_date'] . '+1 month'));

			$takeCicilan = explode(" ", $data[$i]['type']);
			$nextCicilan = $takeCicilan[2] +1;

			$type = "Cicilan ke " . $nextCicilan;

			if ($dueDate <= date('d-m-Y')) {
				$status = "pass";
			} else {
				$status = "lewat";
			}
		}

		if ($status == 'lewat') {
			array_push($fixData, array(
				'id' => $data[$i]['id'],
				'nama' => $data[$i]['nama'],
				'no_telp' => $data[$i]['no_telp'],
				'unit' => $data[$i]['unit'],
				'type' => $type
			));	
		}

	}

	echo json_encode($fixData);
	// echo json_encode($firstData);

?>
<?php
	
	require 'connection.php';

	$houseId = $_POST['house_id'];
	
	$sql = "SELECT b1.id, c1.house_id, b1.nama, b1.no_telp, b1.unit, c1.cicilan, c1.type, c1.trans_date 
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
	$resultFirstData = $conn->query($sqlfirstData);

	$data = array();
	$firstData = array();

	$fixData = array();
	$status = '1';
	$totalCicilanInRupiah = 0;
	$today = date('Y-m-d');

	$dateNextCicilan = "0";
	$nextType = "";

	if ($result != null) {

		while ($row = $result->fetch_assoc()) {
			$data[] = $row;
		}

		while ($row = $resultFirstData->fetch_assoc()) {
			$firstData[] = $row;
		}

		for ($i=0; $i < count($data); $i++) { 
			
			if ($data[$i]['type'] == 'Booking Fee') {
				$nextType = 'Cicilan ke 1';
				$dateNextCicilan = date('Y-m-d', strtotime($data[$i]['trans_date'] . '+14 day'));

				if ($dateNextCicilan >= $today) {
					$status = 'pass';

				} else {
					$status = 'lewat';
				}

			} else if ($data[$i]['type'] != 'Selesai') {
				$takeCicilan = explode(" ", $data[$i]['type']);
				$nextType = 'Cicilan ke ' . ($takeCicilan[2] + 1);
				$dateNextCicilan = date('Y-m', strtotime($data[$i]['trans_date'] . '+1 month')) . '-' . date('d', strtotime($firstData[$i]['trans_date'] . '+14 day'));

				if ($dateNextCicilan >= $today) {
					$status = 'pass';
				} else {
					$status = 'lewat';
				}
			}

			if ($status == 'lewat') {
				array_push($fixData, array(
					'id' => $data[$i]['id'],
					'houseId' => $data[$i]['house_id'],
					'nama' => $data[$i]['nama'],
					'no_telp' => $data[$i]['no_telp'],
					'unit' => $data[$i]['unit'],
					'type' => $nextType
				));	
			}

		}

	}

	echo json_encode($fixData);

?>
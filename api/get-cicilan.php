<?php
	
	require 'connection.php';

	$buyerId = $_POST['buyer_id'];
	$houseId = $_POST['house_id'];
	$unit = $_POST['unit'];

	$sql = "SELECT * FROM tb_cicilan WHERE buyer_id = $buyerId AND house_id = $houseId AND unit = '$unit'";
	$result = $conn->query($sql);

	$data = array();
	$fixData = array();
	$status = '1';
	$totalCicilanInRupiah = 0;
	$totalCicilan =0;
	$nextCicilan = "";

	if ($result != null) {

		while ($row = $result->fetch_assoc()) {
			$data[] = $row;
		}

		for ($i=0; $i < count($data); $i++) {

			$totalCicilan += $data[$i]['cicilan'];

			if ($i == 0) {
				$nextCicilan = date('d-m-Y', strtotime($data[$i]['trans_date'] . "+ 14 day"));
			} else if ($i > 0) {
				$tampNextCicilan = date('d-m-Y', strtotime($data[0]['trans_date'] . "+ 14 day"));
				$tampNextCicilan1 = date('d-m-Y', strtotime($data[$i]['trans_date'] .  "+ 1 month"));

				$nextCicilan = date('d', strtotime($tampNextCicilan)) . '-' . date('m-Y', strtotime($tampNextCicilan1));
			}

			$date_now = new DateTime(date('d-m-Y'));
		    $date2 = new DateTime($nextCicilan);
		    
		    if ($date2 >= $date_now) {
		        $status = "tepat";
		    } else {
		    	$status = "telat";
		    }

			$rupiah = "Rp " . number_format($data[$i]['cicilan'],0,',','.');

			array_push($fixData,
				array(
					'id' => $data[$i]['id'],
					'type' => $data[$i]['type'],
					'cicilan' => $rupiah,
					'date' => date('d-m-Y', strtotime($data[$i]['trans_date']))
				)
			);
		}

		$totalCicilanInRupiah = "Rp " . number_format($totalCicilan,0,',','.');

	}

	$finalData = array();

	array_push($finalData, array(
		'total_cicilan' => $totalCicilanInRupiah,
		 'next_cicilan' => $nextCicilan, 
		 'status' => $status, 
		 'data' => $fixData
	));

	echo json_encode($finalData[0]);

?>
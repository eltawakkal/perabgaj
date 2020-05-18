<?php
	
	require 'connection.php';

	$houseId = $_POST['house_id'];
	$range = $_POST['range'];
	$startDate = $_POST['start_date'];
	$endDate = $_POST['end_date'];

	if ($range == 1) {
		$startDate = date("Y-m-d");
		$endDate = date("Y-m-d");
	}
	if ($range == 7) {
		$startDate = date("Y-m-d", strtotime("-1 week"));
		$endDate = date("Y-m-d");
	} else if ($range == 30) {
		$startDate = date("Y-m-d", strtotime("-1 month"));
		$endDate = date("Y-m-d");
	}

	// echo $sta;

	// $beforeAweek = date("Y-m-d", strtotime("-1 week"));
	// $beforeAmonth = date("Y-m-d", strtotime("-1 month"));

	$sql = "SELECT tb_buyer.nama, tb_cicilan.cicilan, tb_cicilan.type, tb_cicilan.trans_date
			FROM tb_buyer, tb_cicilan
			WHERE tb_buyer.id = tb_cicilan.buyer_id
			AND tb_cicilan.trans_date BETWEEN '$startDate' AND '$endDate'
			AND tb_cicilan.house_id = '$houseId'
            ORDER BY trans_date DESC";

	$result = $conn->query($sql);

	$data = array();
	$fixData = array();
	$finalData = array();
	$totalTrans = 0;

	while ($row = $result->fetch_assoc()) {
		$data[] = $row;
	}

	for ($i=0; $i < count($data); $i++) { 

		$totalTrans+= $data[$i]['cicilan'];

		$rupiah = "Rp " . number_format($data[$i]['cicilan'],0,',','.');
		$newDate = date('d-m-Y', strtotime($data[$i]['trans_date']));

		array_push($fixData, array(
			'nama' => $data[$i]['nama'],
			'cicilan' => $rupiah,
			'type' => $data[$i]['type'],
			'trans_date' => $newDate
		));
	}

	$totalTransInRp = "Rp " . number_format($totalTrans,0,',','.');

	array_push($finalData, array(
		'total_trans' => $totalTransInRp,
		'list_mutation' => $fixData
	));

	// echo json_encode($fixData);
	echo json_encode($finalData[0]);

?>
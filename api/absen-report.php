<?php
	
	require 'connection.php';

	// $userId = $_POST['user_id'];
	// $dateIn = $_POST['date_id'];
	$userId = $_POST['user_id'];
	$dateIn = $_POST['date_in'];

	$dateinExploded = explode("-", $dateIn);
	$startDate = new DateTime($dateinExploded[0] . '-' . ($dateinExploded[1] -1) . '-' . 25);
	$toDate = new DateTime($dateIn . '-' . 25);
	json_encode($startDate);
	json_encode($toDate);
	$startDateTimeToArr = explode(" ", $startDate->date);
	$toDateTimeToArr = explode(" ", $toDate->date);
	// echo 'Start date: ' . $startDateTimeToArr[0] . ', end date: ' . $toDateTimeToArr[0];
	$dateRange = $startDate->diff($toDate);
	// echo $dateRange->days;

	$sqlTepat = "SELECT ta.user_id, COUNT(ta.id) tepat, tg.gaji, tg.makan 
				FROM tb_absen as ta, tb_gaji as tg
				WHERE ta.status = 'tepat waktu'
				AND ta.user_id = '$userId'
                AND tg.user_id = ta.user_id
                AND ta.date_in > '$startDateTimeToArr[0]'
                AND ta.date_in <= '$toDateTimeToArr[0]'";
	$sqlTelatT = "SELECT ta.user_id, COUNT(ta.id) telatT, tg.gaji, tg.makan 
				FROM tb_absen as ta, tb_gaji as tg
				WHERE ta.status = 'terlambat t'
				AND ta.user_id = '$userId'
                AND tg.user_id = ta.user_id
                AND ta.date_in > '$startDateTimeToArr[0]'
                AND ta.date_in <= '$toDateTimeToArr[0]'";
	$sqlTelat = "SELECT ta.user_id, COUNT(ta.id) telat, tg.gaji, tg.makan 
				FROM tb_absen as ta, tb_gaji as tg
				WHERE ta.status = 'terlambat'
				AND ta.user_id = '$userId'
                AND tg.user_id = ta.user_id
                AND ta.date_in > '$startDateTimeToArr[0]'
                AND ta.date_in <= '$toDateTimeToArr[0]'";


	$resultTepat = $conn->query($sqlTepat);
	$dataTepat = mysqli_fetch_assoc($resultTepat);
	$resltTelatT = $conn->query($sqlTelatT);
	$dataTelatT = mysqli_fetch_assoc($resltTelatT);
	$resltTelat = $conn->query($sqlTelat);
	$dataTelat = mysqli_fetch_assoc($resltTelat);

	$userIdGet = $dataTepat['user_id'];
	$userGaji = $dataTepat['gaji'];
	$userMakan = $dataTepat['makan'];

	if ($userIdGet == null) {
		$userIdGet = $dataTelatT['user_id'];
		$userGaji = $dataTelatT['gaji'];
		$userMakan = $dataTelatT['makan'];
	}

	if ($userIdGet == null) {
		$userIdGet = $dataTelat['user_id'];
		$userGaji = $dataTelat['gaji'];
		$userMakan = $dataTelat['makan'];
	}

	$daysInaMonth = $dateRange->days;

	$fixData = array();

	$totalMasuk = $dataTepat['tepat'] + $dataTelatT['telatT'] + $dataTelat['telat'];

	// 3000000 - (((50/100) * 20000) * 4) - (20000 * 3)
	$fixGaji = $userGaji - (((50/100) * $userMakan) * $dataTelatT['telatT']) - ($userMakan * $dataTelat['telat']);
	$gajiInRupiah =  "Rp. " . number_format($dataTepat['gaji'], 0, '.', '.');
	$makanInRupiah = "Rp. " . number_format($dataTepat['makan'], 0, '.', '.');
	$fixGajiInRupiah = "Rp. " . number_format($fixGaji,0,'.','.');

	array_push($fixData, array(
		'user_id' => $userIdGet,
		'masuk' => $totalMasuk,
		'gaji_now' => $fixGajiInRupiah,
		'gaji' => $gajiInRupiah,
		'makan' => $makanInRupiah,
		'absen' => $daysInaMonth - $totalMasuk,
		'tepat' => $dataTepat['tepat'],
		'telatT' => $dataTelatT['telatT'],
		'telat' => $dataTelat['telat'],
	));

	
	echo json_encode($fixData[0]);

?>
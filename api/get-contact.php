<?php
	
	require 'connection.php';

	$nama = $_POST['nama'];
	$homeId = $_POST['home_id'];

	$sql = "SELECT * FROM tb_buyer WHERE (nama LIKE '%$nama%' OR unit LIKE '%$nama%') AND house_id = '$homeId'";
	$result = $conn->query($sql);

	$data = array();

	while ($row = $result->fetch_assoc()) {
		$data[] = $row;
	}

	echo json_encode($data);

?>
<?php
	
	require 'connection.php';

	$nama = $_POST['nama'];
	$ptId = $_POST['pt_id'];

	$sql = "SELECT * FROM tb_house WHERE nama LIKE '%$nama%' AND pt_id = '$ptId'";
	$result = $conn->query($sql);

	$data = array();

	while ($row = $result->fetch_assoc()) {
		$data[] = $row;
	}

	echo json_encode($data);

?>
<?php
	
	require 'connection.php';

	$nama = $_POST['nama'];

	$sql = "SELECT * FROM tb_pt WHERE nama LIKE '%$nama%'";
	$result = $conn->query($sql);

	$data = array();

	while ($row = $result->fetch_assoc()) {
		$data[] = $row;
	}

	echo json_encode($data);

?>
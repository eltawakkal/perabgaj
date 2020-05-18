<?php
	
	require 'connection.php';

	$id = $_POST['id'];

	$sql = "SELECT * FROM tb_buyer WHERE id = '$id'";
	$result = $conn->query($sql);

	$data = array();

	while ($row = $result->fetch_assoc()) {
		$data[] = $row;
	}

	echo json_encode($data);

?>
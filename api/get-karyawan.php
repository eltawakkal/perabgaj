<?php
	
	require 'connection.php';

	$sql = "SELECT tu.*, tg.gaji, tg.makan
			FROM tb_user as tu, tb_gaji as tg
			WHERE tg.user_id = tu.id";
	$result = $conn->query($sql);

	$data = array();

	while ($row = $result->fetch_assoc()) {
		$data[] = $row;
	}

	echo json_encode($data);

?>
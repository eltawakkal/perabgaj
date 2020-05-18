<?php
	
	require 'connection.php';

	$id = $_POST['id'];

	$sql = "SELECT * FROM tb_user WHERE id = '$id'";
	$result = $conn->query($sql);

	$user = array();

	while ($row = $result->fetch_assoc()) {
		$user[] = $row;
	}

	if ($user != null) {
		echo json_encode($user[0]);
	} else {
		echo "{}";
	}

?>
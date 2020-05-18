<?php
	
	require 'connection.php';

	$id = $_POST['id'];
	$name = $_POST['name'];
	$email = $_POST['email'];
	$pass = $_POST['pass'];
	$device = $_POST['device'];
	$photo = $_POST['photo'];

	$today = date("Y-m-d");
	$users = array();

	$sql = "INSERT INTO tb_user(id, created_at, name, email, password, device, photo_url)
			VALUES ('$id', '$today', '$name', '$email', '$pass', '$device', '$photo')";
	$slqGaji = "INSERT INTO tb_gaji(user_id, gaji, makan)
			VALUES ('$id', '0', '0')";
	$sqlCekGaji = "SELECT count(*) gaji FROM tb_gaji WHERE user_id = '$id'";
	
	$resultCekGaji = $conn->query($sqlCekGaji);
	$gajiSudahAda = mysqli_fetch_assoc($resultCekGaji);

	$result = $conn->query($sql);

	if ($result) {
		if ($gajiSudahAda['gaji'] == 0) {
			$resultGaji = $conn->query($slqGaji);	
			echo "{}";
		} else {
			echo "{}";
		}
	} else {
		echo("Gagal");
	}

?>
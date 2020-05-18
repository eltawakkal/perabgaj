<?php
	
	require 'connection.php';

	$harga_jual = $_POST['harga_jual'];
	$cp = $_POST['cp'];
	$unit = $_POST['unit'];
	$houseId = $_POST['house_id'];
	$nama = $_POST['nama'];
	$no_telp = $_POST['no_telp'];
	$no_pasangan = $_POST['no_pasangan'];
	$no_darurat = $_POST['no_darurat'];
	$alamat = $_POST['alamat'];
	$pekerjaan = $_POST['pekerjaan'];
	$status_berkas = $_POST['status_berkas'];
	$tgl_booking = $_POST['tgl_booking'];
	$bank_pel = $_POST['bank_pel'];
	$ket = $_POST['ket'];
	$a = $_POST['a'];
	$b = $_POST['b'];
	$c = $_POST['c'];
	$d = $_POST['d'];
	$e = $_POST['e'];
	$f = $_POST['f'];
	$g = $_POST['g'];
	$verified = $_POST['verified'];

	$sql = "INSERT INTO tb_buyer (harga_jual, cp, unit, house_id, nama, no_telp, no_pasangan, no_darurat, alamat, pekerjaan, status_berkas, tgl_booking, bank_pel, ket, a, b, c, d, e, f, g, verified) VALUES
	('$harga_jual', '$cp', '$unit', '$houseId', '$nama', '$no_telp', '$no_pasangan', '$no_darurat', '$alamat', '$pekerjaan', '$status_berkas', '$tgl_booking', '$bank_pel', '$ket', '$a', '$b', '$c', '$d', '$e', '$f', '$g', '$verified')";

	if ($conn->query($sql)) {
		$data = array('status'=>'berhasil');
		echo json_encode($data);
	} else {
		echo $conn->error;
	}
	
?>
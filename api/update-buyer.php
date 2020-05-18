<?php
	
	require 'connection.php';

	$harga_jual = $_POST['harga_jual'];
	$cp = $_POST['cp'];
	$unit = $_POST['unit'];
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
	// $bayarfee = $_POST['bayarfee'];
	// $bayar1 = $_POST['bayar1'];
	// $bayar2 = $_POST['bayar2'];
	// $bayar3 = $_POST['bayar3'];
	// $bayar4 = $_POST['bayar4'];
	// $total_pembayaran = $_POST['total_pembayaran'];
	$verified = $_POST['verified'];

	$sql = "UPDATE tb_buyer SET harga_jual = '$harga_jual', cp = '$cp', nama = '$nama', no_telp = '$no_telp', no_pasangan = '$no_pasangan', no_darurat = '$no_darurat', alamat = '$alamat', pekerjaan = '$pekerjaan', status_berkas = '$status_berkas', tgl_booking = '$tgl_booking', bank_pel = '$bank_pel', ket = '$ket', a = '$a', b = '$b', c = '$c', d = '$d', e = '$e', f = '$f', g = '$g', verified = '$verified' WHERE unit = '$unit';";
	$result = $conn->query($sql);

	$data = array('status'=>'berhasil');
	echo json_encode($data);

?>
<?php
$db = mysqli_connect("dvwebb.mah.se", "AE3529", "guldfisk1337", "ae3529");

$return = new ArrayObject();

//Används för POST, till för att lägga upp bilder på ddwap och även spara det i en databas.
// REturnar felmeddelande om det gått dåligt annars om det gick bra redirect tillbaka till den sidan som du va på innan.
if(isset($_FILES['image']['tmp_name'])){
	$path = "photo/";
	$fileName = $path.rand().$_FILES['image']['name'];

	if(move_uploaded_file($_FILES['image']['tmp_name'], $fileName)){
		if(mysqli_query($db, "INSERT INTO item (Title, Image, Description) VALUES ('".$_POST['title']."', '".$fileName."', '".$_POST['desc']."')")){
			$return['success'] = true;
			$return['message'] = "Fil uppladdad & sparad i db";
			echo header('Location: ' . $_SERVER['HTTP_REFERER']);
		}else{
			$return['success'] = false;
			$return['message'] = "Fil uppladdad men inte sparad i db";
			echo json_encode($return);
		}

	}else{
		$return['success'] = false;
		$return['message'] = "Kunde inte ladda upp filen";
		echo json_encode($return);
	}
}

/*
	Get för att hämta alla items från vår databas och echo tillbaka det som json.
*/
if(isset($_GET['item']) and $_GET['item'] == "getItem"){
	$res = mysqli_query($db, "SELECT * FROM item ORDER BY id DESC");

	$media = new ArrayObject();
	while($row = mysqli_fetch_assoc($res)){
		$m = new ArrayObject();
		$m['ID'] = $row['ID'];
		$m['Title'] = $row['Title'];
		$m['Image'] = $row['Image'];
		$m['Description'] = $row['Description'];
		$m['Datee'] = $row['Datee'];
		$media['item'][] = $m;
	}
	echo json_encode($media);
}

?>

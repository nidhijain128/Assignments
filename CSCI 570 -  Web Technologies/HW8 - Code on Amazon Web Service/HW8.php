<?php
		function test_input($data)
		{
			$data = trim($data);
			$data = stripslashes($data);
			$data = htmlspecialchars($data);
			return $data;
		}
		
		$location = urlencode(test_input($_GET['location'])); 
		$locationType = test_input($_GET['locationType']);
		$tempUnit = test_input($_GET['tempUnit']);
		
		$url = "";
		if($locationType == 'zip' && strlen($location) == 5 && is_numeric($location))
		{
				$url = "http://where.yahooapis.com/v1/concordance/usps/".$location."?appid=5M3Huy_V34HFgLHJTXK42jTt4ZZiwOVLU5R6wwKmckcyiYV1qdpdWtI2c6V6VOT5t7wyKA--";
				$uwoeidN = @file_get_contents($url);
				$pwoeidN = @simplexml_load_string($uwoeidN);
				if($uwoeidN != "")
				{
				$woeidValue = $pwoeidN->children();
				$urlW = "";
				$urlW = "http://weather.yahooapis.com/forecastrss?w=".$woeidValue."&u=".$tempUnit;
				$uweather = file_get_contents($urlW);
				$pweather = simplexml_load_string($uweather);
				echo $pweather;
			}
				
		}
		else if($locationType == 'city' && $location!="")
		{
			$url = "http://where.yahooapis.com/v1/places\$and(.q('".$location."'),.type(7));start=0;count=1?appid=5M3Huy_V34HFgLHJTXK42jTt4ZZiwOVLU5R6wwKmckcyiYV1qdpdWtI2c6V6VOT5t7wyKA--";
			$uwoeidN = file_get_contents($url);
			$pwoeidN = simplexml_load_string($uwoeidN);
			$character = $pwoeidN->children();
			$woeidValue = $character->children();
			$urlW = "http://weather.yahooapis.com/forecastrss?w=".$woeidValues[$n]."&u=".$tempUnit;
			$uweather = file_get_contents($urlW);
			$pweather = simplexml_load_string($uweather);
			echo $pweather;
		}
?>
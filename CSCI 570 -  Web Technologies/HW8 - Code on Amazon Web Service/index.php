<?php
	function test_input($data)
		{
			$data = trim($data);
			$data = stripslashes($data);
			$data = htmlspecialchars($data);
			return $data;
		}
		
		$location = test_input($_GET['location']); 
		$locationType = test_input($_GET['locationType']);
		$tempUnit = test_input(r$_GET['tempUnit']);
		
		$url = "";
		if($locationType == 'zip')
		{
			$url = "http://where.yahooapis.com/v1/concordance/usps/".$location.="?appid=5M3Huy_V34HFgLHJTXK42jTt4ZZiwOVLU5R6wwKmckcyiYV1qdpdWtI2c6V6VOT5t7wyKA--";
			$uwoeidN = @file_get_contents($url);
			$pwoeidN = @simplexml_load_string($uwoeidN);
		}
		else if($locationType == 'city')
		{
			$url = urlencode("http://where.yahooapis.com/v1/places\$and(.q('".$location."'),.type(7));start=0;count=1?appid=5M3Huy_V34HFgLHJTXK42jTt4ZZiwOVLU5R6wwKmckcyiYV1qdpdWtI2c6V6VOT5t7wyKA--");
			$uwoeidN = file_get_contents($url);
			$pwoeidN = simplexml_load_string($uwoeidN);
		}
		
		$urlW = "";
		if($uwoeidN != "")
		{
			if($locationType == 'zip')
			{
				$woeidValue = $pwoeidN->children();
				$urlW = "http://weather.yahooapis.com/forecastrss?w=".$woeidValue."&u=".$tempUnit;
			}
			else if($locationType == 'city')
			{
				$character = $pwoeidN->children();
				$woeidValue = $character->children();
				$urlW = "http://weather.yahooapis.com/forecastrss?w=".$woeidValue."&u=".$tempUnit;
			}	
		}
			
		$uweather = file_get_contents($urlW);
		$pweather = simplexml_load_string($uweather);
		$link = $pweather->channel->link;
		$feed = urlencode($urlW);
		$namespaces = $pweather->channel->getNameSpaces(true);
		$yweather = $pweather->channel->children($namespaces['yweather']);
		$loc = $yweather->location->attributes();
		$temp = $yweather->units->attributes();
		$namespaces1 = $pweather->channel->item->getNameSpaces(true);
		$yweather1 = $pweather->channel->item->children($namespaces1['yweather']);
		$condition = $yweather1->condition->attributes();
		$a = 0;
		foreach ($yweather1->forecast as $forecast)
		{
			$fc[$a] = $forecast->attributes();
			$a = $a + 1;
		}
		$image = $pweather->channel->item->description;
		preg_match("/http:\/\/(.*?)[^\"']+/", $image, $matches);
		$imgSrc = $matches[0];
		//$urlW = urlencode($urlW);<feed>".$urlW."</feed>
		
		header('Content-Type: text/xml; charset: utf-8');
		$xmlDoc = "<?xml version='1.0' encoding='utf-8'?><weather><link>".$link."</link><feed>".$feed."</feed><location city='".$loc['city']."' region='".$loc['region']."' country='".$loc['country']."'/><units temperature='".$temp['temperature']."'/><condition text='".$condition['text']."' temp='".$condition['temp']."'/><img>".$imgSrc."</img><forecast day='".$fc[0]['day']."' low='".$fc[0]['low']."' high='".$fc[0]['high']."' text='".$fc[0]['text']."'/><forecast day='".$fc[1]['day']."' low='".$fc[1]['low']."' high='".$fc[1]['high']."' text='".$fc[1]['text']."'/><forecast day='".$fc[2]['day']."' low='".$fc[2]['low']."' high='".$fc[2]['high']."' text='".$fc[2]['text']."'/><forecast day='".$fc[3]['day']."' low='".$fc[3]['low']."' high='".$fc[3]['high']."' text='".$fc[3]['text']."'/><forecast day='".$fc[4]['day']."' low='".$fc[4]['low']."' high='".$fc[4]['high']."' text='".$fc[4]['text']."'/></weather>";
		echo $xmlDoc;
?>
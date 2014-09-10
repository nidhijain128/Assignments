<html>
<head>
<title>HW6</title>
<style type="text/css">
	body {
		align: center;
	}
	
	form table tr, form table td {
		border: none;
	}
	
	table {
		border-style: double;
		border-width: 2px;
	}
</style>
<script type="text/javascript">
	function validate()
	{
		if(document.hw6.location.value == "")
			alert("Please enter a location");
		else if(document.hw6.locationType.value == 'zip')
			if(document.hw6.location.value.length != 5 || isNaN(document.hw6.location.value))
				alert("Please enter valid zipcode");
	}
</script>
</head>
<body><center><br><br><br><font size="+2"><b>Weather Search</b></font><br>
	<form name="hw6" id="hw6" method="post">
    <table border="4" cellpadding="8">
    	<tr><td>Location:</td><td><input type="text" name="location" id="location" size="38" value="<?php echo isset($_POST['submit']) ? $_POST['location'] : '' ?>"></td></tr>
        <tr><td>Location Type:</td><td><select name="locationType" id="locationType">
        				<option selected value="city" <?php if(isset($_POST['submit']) && $_POST['locationType'] == 'city') echo ' selected="selected"'; ?>>City</option>
                        <option value="zip" <?php if(isset($_POST['submit']) && $_POST['locationType'] == 'zip') echo ' selected="selected"'; ?>>ZIP Code</option>
                   	 </select> </td></tr>
   		<tr><td>Temparature Unit:</td><td><input type="radio" name="tempUnit" checked value="f" <?php if(isset($_POST['submit']) && $_POST['tempUnit'] == 'f') echo ' checked="checked"'; ?>>Fahrenheit</radio>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        				  <input type="radio" name="tempUnit" value="c" <?php if(isset($_POST['submit']) && $_POST['tempUnit'] == 'c') echo ' checked="checked"'; ?>>Celsius</radio></td></tr><br>
                          
                        <tr><td colspan="2"><center><input type="submit" name="submit" value="Search" onClick="validate()"></input></center></td></tr></table>
                     
</form>
</center>
<?php 
if(isset($_POST['submit'])) 
	{
		function test_input($data)
		{
			$data = trim($data);
			$data = stripslashes($data);
			$data = htmlspecialchars($data);
			return $data;
		}
		
		$location = urlencode(test_input($_POST['location'])); 
		$locationType = test_input($_POST['locationType']);
		$tempUnit = test_input($_POST['tempUnit']);
		
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
			
				echo "<center><br><br><br><table border='1'><caption>1 result for Zip Code&nbsp;".urldecode($location)."</caption><tr><td>Weather</td><td>Temperature</td><td>City</td><td>Region</td><td>Country</td><td>Latitude</td><td>Longitude</td><td>Link to Details</td></tr>";
				$image = $pweather->channel->item->description;
				preg_match("/http:\/\/(.*?)[^\"']+/", $image, $matches);
				$imgSrc = $matches[0];
				if($imgSrc == "")
					$imgSrc = "N/A";
			
				$namespaces = $pweather->channel->item->getNameSpaces(true);
				$yweather = $pweather->channel->item->children($namespaces['yweather']);
				$yweather2 = $pweather->channel->item->children($namespaces['geo']);
				$namespaces1 = $pweather->channel->getNameSpaces(true);
				$yweather1 = $pweather->channel->children($namespaces['yweather']);
				
				$unit = $yweather1->units->attributes();
				$temp = $yweather->condition->attributes();
				$lat = $yweather2->lat;
				if($lat == "")
					$lat = "N/A";
				$long = $yweather2->long;
				if($long == "")
					$long = "N/A";
				$temperature = $temp['text']." ".$temp['temp']."&deg;".$unit['temperature'];
				if($temp['text'] == "")
						$temperature = $temp['temp']."&deg;".strtoupper($tempUnit);
					else if($temp['temp'] == "")
						$temperature = $temp['text'];
						else if($temp['text'] == "" && $temp['temp'] == "")
							$temperature = "N/A";
			
				$loc = $yweather1->location->attributes();
				$city = $loc['city'];
				if($city == "")
					$city = "N/A";
				$region = $loc['region'];
				if($region == "")
					$region = "N/A";
				$country = $loc['country'];
				if($country == "")
					$country = "N/A";
				
				$link = $pweather->channel->item->link;
			
				echo "<tr><td>";
				if($imgSrc != "N/A")
					echo "<a href='".$urlW."'target='_blank'><img src='".$imgSrc."'alt='".$temp['text']."'title='".$temp['text']."'></a></td><td>".$temperature."</td><td>".$city."</td><td>".$region."</td><td>".$country."</td><td>".$lat."</td><td>".$long."</td><td><a href='".$link."'target='_blank'>Details</a></td></tr></table></center>";
				else echo "<a href='".$urlW."'target='_blank'><img src=''&nbsp;alt='".$temp['text']."'></a></td><td>".$temperature."</td><td>".$city."</td><td>".$region."</td><td>".$country."</td><td>".$lat."</td><td>".$long."</td><td><a href='".$link."'target='_blank'>Details</a></td></tr></table></center>";
			}
			else echo "<center><font size='+2'>Zero Results Found!!</font></center>";
				
		}
		else if($locationType == 'city' && $location!="")
		{
			$url = "http://where.yahooapis.com/v1/places\$and(.q('".$location."'),.type(7));start=0;count=5?appid=5M3Huy_V34HFgLHJTXK42jTt4ZZiwOVLU5R6wwKmckcyiYV1qdpdWtI2c6V6VOT5t7wyKA--";
			$uwoeidN = file_get_contents($url);
			$pwoeidN = simplexml_load_string($uwoeidN);
			$woeidValues = array();
			$a = 0;
			foreach ($pwoeidN->children() as $character)
			{
				$woeidValues[$a] = $character->children();
				$a = $a+1;
			}
			if($a != 0)
			{
				$urlW = array();
				$uweather = array();
				$pweather = array();
				$x = 0;
				$j = 0;
				for($n=0;$n<$a;$n++)
				{
					$urlW[$x] = "http://weather.yahooapis.com/forecastrss?w=".$woeidValues[$n]."&u=".$tempUnit;
					$uweather[$x] = file_get_contents($urlW[$x]);
					$pweather[$x] = simplexml_load_string($uweather[$x]);
					$test = $pweather[$x]->channel->title;
					if(strpos($test,'Error'))
					{	
						$x--; 
						$j++;
					}
					$x++;
				}
				echo "<center><br><br><br><table border='1'><caption>".($a-$j)."&nbsp;result(s) for City&nbsp;".urldecode($location)."</caption><tr><td>Weather</td><td>Temperature</td><td>City</td><td>Region</td><td>Country</td><td>Latitude</td><td>Longitude</td><td>Link to Details</td></tr>";
				
				for($i=0;$i<$a-$j;$i++)		
				{
					$image = $pweather[$i]->channel->item->description;
					preg_match("/http:\/\/(.*?)[^\"']+/", $image, $matches);
					if(count($matches)!= 0 && (strpos($matches[0],'gif') || strpos($matches[0],'png') || strpos($matches[0],'jpg') || strpos($matches[0],'jpeg') || strpos($matches[0],'tif')))
						$imgSrc = $matches[0];
					else 
						$imgSrc = "N/A";
					
					$namespaces = $pweather[$i]->channel->item->getNameSpaces(true);
					$yweather = $pweather[$i]->channel->item->children($namespaces['yweather']);
					$yweather2 = $pweather[$i]->channel->item->children($namespaces['geo']);
					$namespaces1 = $pweather[$i]->channel->getNameSpaces(true);
					$yweather1 = $pweather[$i]->channel->children($namespaces['yweather']);
					
					$unit = $yweather1->units->attributes();
					$temp = $yweather->condition->attributes();
					$lat = $yweather2->lat;
					if($lat == "")
						$lat = "N/A";
					$long = $yweather2->long;
					if($long == "")
						$long = "N/A";
					$temperature = $temp['text']." ".$temp['temp']."&deg;".$unit['temperature'];
					if($temp['text'] == "")
						$temperature = $temp['temp']."&deg;".strtoupper($tempUnit);
						else if($temp['temp'] == "")
							$temperature = $temp['text'];
							else if($temp['text'] == "" && $temp['temp'] == "")
								$temperature = "N/A";
										
					$loc = $yweather1->location->attributes();
					$city = $loc['city'];
					if($city == "")
						$city = "N/A";
					$region = $loc['region'];
					if($region == "")
						$region = "N/A";
					$country = $loc['country'];
					if($country == "")
						$country = "N/A";
					
					$link = $pweather[$i]->channel->item->link;
			
					echo "<tr><td>";
					if($imgSrc != "N/A")
						echo "<a href='".$urlW[$i]."'target='_blank'><img src='".$imgSrc."'alt='".$temp['text']."'title='".$temp['text']."'></a></td><td>".$temperature."</td><td>".$city."</td><td>".$region."</td><td>".$country."</td><td>".$lat."</td><td>".$long.	"</td><td><a href='".$link."'target='_blank'>Details</a></td></tr>";
					else echo "<a href='".$urlW[$i]."'target='_blank'><img src=''&nbsp;alt='".$temp['text']."'></a></td><td>".$temperature."</td><td>".$city."</td><td>".$region."</td><td>".$country."</td><td>".$lat."</td><td>".$long."</td><td><a href='".$link."'target='_blank'>Details</a></td></tr>";
				}
			echo "</table></center>";
			}
			else echo "<center><font size='+2'>Zero Results Found!!</font></center>";
		}
	}
?>
<noscript>
<!--</body>-->
</html>
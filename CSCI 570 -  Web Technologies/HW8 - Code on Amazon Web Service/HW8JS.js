function validate()
	{
		var loc = document.hw8.location.value;
		var locationType;
		var tempUnit = "f";
		if(loc == "")
			alert("Please enter a location");
		else if(!isNaN(loc))
			{
				locationType = "zip";
				if(loc.length != 5)
					alert("Invalid zipcode: must be five digits\nExample: 90089");
			}
			else 
			{
				locationType = "city";
				if((loc.indexOf(',') == -1)  || (loc.indexOf(',')) > (loc.length - 6))
					alert("Invalid location: must inlcude state or country separated by comma\nExample: Los Angeles, CA");
			}
			
		if(locationType == "zip" || locationType == "city")
		{
			var url = "http://cs-server.usc.edu:13856/HomeWork8/HomeWork8?location=" + loc + "&locationType=" + locationType + "&tempUnit=" + tempUnit;
			var httpRequest;
			if (window.XMLHttpRequest) {
				httpRequest = new XMLHttpRequest();
			} 
			else if (window.ActiveXObject) {
				httpRequest = new ActiveXObject("Microsoft.XMLHTTP");
			}
			
			var xmlDoc = null;
			if(httpRequest) {
				httpRequest.open("GET", url, false);
				httpRequest.onreadystatechange = function() {
					if(httpRequest.readyState == 4)
						if(httpRequest.status == 200)
							xmlDoc = eval('(' + httpRequest.responseText + ')');
				}
				httpRequest.setRequestHeader("Connection",	"Close");	
				httpRequest.setRequestHeader("Method",	"GET"	+	url	+	"HTTP/1.1");
				httpRequest.send(null);
				document.write(httpRequest.responseText);
			}		 
		}
	}
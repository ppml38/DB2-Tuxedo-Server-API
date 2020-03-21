
function msgaddr(result) {
	//console.log("msg loggin");
	document.getElementById("air_addr").innerHTML = "";
	//document.getElementById("loading").style.visibility = "hidden";
	if(result[0][0]!="#ERROR#" & result[0][0]!="#CERROR#" )
	{
	for(var i=0;i<result.length-3;i++) //-3 is because, last 3 rows is because of additional 2 \n\r at the end of response from server
	{
			document.getElementById("air_addr").innerHTML +=((i==0?"":"<br>")+result[i][2]+result[i][1].trim());	
	}
	}
	else
		document.getElementById("air_addr").innerHTML = "No Address found";
}

function omsgaddr(result) {
	//console.log("Omg loggin");
	document.getElementById("ogs_addr").innerHTML = "";
	//document.getElementById("loading").style.visibility = "hidden";
	if(result[0][0]!="#ERROR#" & result[0][0]!="#CERROR#" )
	{
	for(var i=0;i<result.length-3;i++) //-3 is because, last 3 rows is because of additional 2 \n\r at the end of response from server
	{
			document.getElementById("ogs_addr").innerHTML +=((i==0?"":"<br>")+result[i][2]+result[i][1].trim());	
	}  
	}
	else
		document.getElementById("ogs_addr").innerHTML = "No Address found";
}

function omsgtxt(result)
{
	document.getElementById("ogs_text").innerHTML = "";
	//document.getElementById("loading").style.visibility = "hidden";
	if(result[0][0]!="#ERROR#" & result[0][0]!="#CERROR#" )
	{
		var prevv=0;
	for(var i=0;i<result.length-3;i++) //-3 is because, last 3 rows is because of additional 2 \n\r at the end of response from server
	{
		if(prevv!=result[i][0])
		{
			document.getElementById("ogs_text").innerHTML +="<br>";
		}
			document.getElementById("ogs_text").innerHTML +=((i==0?"":"<br>")+result[i][1].trim());	
			prevv = result[i][0];
	}
	}
	else
		document.getElementById("ogs_text").innerHTML = "No message";
}

function msgtxt(result)
{
	document.getElementById("air_text").innerHTML = "";
	//document.getElementById("loading").style.visibility = "hidden";
	if(result[0][0]!="#ERROR#" & result[0][0]!="#CERROR#" )
	{
		var prevv=0;
	for(var i=0;i<result.length-3;i++) //-3 is because, last 3 rows is because of additional 2 \n\r at the end of response from server
	{
		if(prevv!=result[i][0])
		{
			document.getElementById("air_text").innerHTML +="<br>";
		}
			document.getElementById("air_text").innerHTML +=((i==0?"":"<br>")+result[i][1].trim());	
			prevv = result[i][0];
	}
	}
	else
		document.getElementById("air_text").innerHTML = "No message";
}

function send()
{
	document.getElementById("air_addr").innerHTML = "<marquee direction='right'>o&nbsp&nbspo&nbsp&nbspo&nbsp&nbspo</marquee>";
	document.getElementById("air_text").innerHTML = "<marquee direction='right'>o&nbsp&nbspo&nbsp&nbspo&nbsp&nbspo</marquee>";
	document.getElementById("ogs_addr").innerHTML = "<marquee direction='right'>o&nbsp&nbspo&nbsp&nbspo&nbsp&nbspo</marquee>";
	document.getElementById("ogs_text").innerHTML = "<marquee direction='right'>o&nbsp&nbspo&nbsp&nbspo&nbsp&nbspo</marquee>";
	//document.getElementById("loading").style.visibility = "visible";
	genie("msgaddr.q!__ID~"+document.getElementById("rq").value ,msgaddr);
	genie("omsgaddr.q!__ID~"+document.getElementById("rq").value ,omsgaddr);
	genie("msgtxt.q!__ID~"+document.getElementById("rq").value ,msgtxt);
	genie("omsgtxt.q!__ID~"+document.getElementById("rq").value ,omsgtxt);
}
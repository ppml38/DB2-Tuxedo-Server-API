function genie ( request,callback ) {
    //alert(request);
    var xhttp=new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if ( this.readyState == 4 && this.status == 200 ) {
            //alert(xhttp.responseText);
            var a = xhttp.responseText.split ( "\r\n" ); 
            //alert(a.length);
            var result = [];
            //document.getElementById("result").innerHTML = a;
            a.forEach ( function ( item,index ) {
                result[index] = item.split ( "|" );
            } );

            callback ( result );
        }
    };
    xhttp.open ( "GET", request, true );
    xhttp.send();

}
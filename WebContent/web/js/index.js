function onBodyLoad(){
	let userID = localStorage.getItem("userID");
	
	if (userID){
		document.getElementById("login").style = "display: none;"
		document.getElementById("salesForm").style = "display: block;"
	}else {
		document.getElementById("login").style = "display: block;"
		document.getElementById("salesForm").style = "display: none;" 
	}
}

function login(){
	let xhr = null;
	if (window.XMLHttpRequest) {
	    // code for modern browsers
		xhr = new XMLHttpRequest();
	 } else {
	    // code for old IE browsers
		 xhr = new ActiveXObject("Microsoft.XMLHTTP");
	}
	xhr.onreadystatechange = function() {
		if (this.readyState == 4 ) {
			if (this.status == 200){
				document.getElementById("salesFormData").innerHTML = this.responseText;
			}else {
				 alert('Request failed.  Returned status of ' + xhr.status);
			}
		     
		   }
		
	  
	  };
	xhr.open("POST", "/Login", true);
	xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	xhr.send("userID="+document.getElementById("userName").value+"&pwd="+document.getElementById("pwd").value);
	

}
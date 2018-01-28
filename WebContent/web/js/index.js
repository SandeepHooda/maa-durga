

function loginWithUserIDAndPwd(userID , pwd){
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
				localStorage.setItem("userID",this.responseText);
				localStorage.setItem("pwd",pwd);
				document.getElementById("login").style = "display: none;"
				document.getElementById("salesForm").style = "display: block;"
			}else {
				document.getElementById("login").style = "display: block;"
				document.getElementById("salesForm").style = "display: none;" 
				document.getElementById("loginStatus").innerHTML = "<span>Request failed.  Returned status of " + xhr.status+"</span>";
				
			}
		     
		   }
		
	  
	  };
	xhr.open("POST", "/Login", true);
	xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	xhr.send("userID="+userID+"&pwd="+pwd);
	
}
function login(){
	document.getElementById("loginStatus").innerHTML = "";
	loginWithUserIDAndPwd(document.getElementById("userName").value,document.getElementById("pwd").value);
	

}

function showPassword(){
	console.log(document.getElementById("showPasswordCheckBox").checked)
	if (document.getElementById("showPasswordCheckBox").checked ){
		document.getElementById("showPasswordSpan").innerHTML  = document.getElementById("pwd").value ;
	}else {
		document.getElementById("showPasswordSpan").innerHTML = "";
	}
}
function toggleShowPassword(){
	document.getElementById("showPasswordCheckBox").checked = !document.getElementById("showPasswordCheckBox").checked;
	 showPassword();
}

function onBodyLoad(){
	let userID = localStorage.getItem("userID");
	
	if (userID){
		document.getElementById("login").style = "display: none;"
		document.getElementById("salesForm").style = "display: block;"
		loginWithUserIDAndPwd(userID,localStorage.getItem("pwd"));
		document.getElementById("loginStatus").innerHTML = "";
	}else {
		document.getElementById("login").style = "display: block;"
		document.getElementById("salesForm").style = "display: none;" 
	}
}
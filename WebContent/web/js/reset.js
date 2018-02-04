
function sendResetLink(){
	
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
				document.getElementById("status").innerHTML ="Please check your email to reset password.";
			}else {
				document.getElementById("status").innerHTML ="Please check your email to reset password!";				
			}
		     
		   }
		
	  
	  };
	xhr.open("GET", "/ResetLink?email="+document.getElementById("resetEmailInput").value, true);
	xhr.send();
	
}

function backToLogin(){
	window.open("/", '_self');
	
}

function getParameterByName(name, url) {
    if (!url) url = window.location.href;
    name = name.replace(/[\[\]]/g, "\\$&");
    var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}

function ChangePassword(){
	document.getElementById("status").innerHTML = "";
	let pwd = document.getElementById("pwd").value
	let pwdConfirm = document.getElementById("confirmPwd").value;
	if (pwd==pwdConfirm ){
		if (pwd.length >=4){
			
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
						document.getElementById("status").innerHTML ="Password changed";
					}else {
						document.getElementById("status").innerHTML =this.responseText;				
					}
				     
				   }
				
			  
			  };
			  xhr.open("PUT", "/ChangePassword", true);
			xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
			xhr.send("ref="+ getParameterByName('ref')+"&pwd="+pwd);
		}else {
			document.getElementById("status").innerHTML = "Password must be 4 char long";
		}
		
	}else {
		document.getElementById("status").innerHTML = "Password and confirm password don't match.";
	}
	
	
}
function onBodyLoad(){
	
}



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
				postLogin();
				localStorage.setItem("userID",this.responseText);
				localStorage.setItem("pwd",pwd);
				
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
function fetchStateCodes(){
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
				pupulateStateCodes(JSON.parse(this.responseText));
			}else {
				alert("Couln't get state codes from server");
				
			}
		     
		   }
		
	  
	  };
	xhr.open("GET", "/StatesCodes", true);
	xhr.send();
}
let pleaseSelect = "Please select";
function saveFavouriteStateCode(){
	let shippingState = document.getElementById("shippingState");
	if ( pleaseSelect != shippingState.options[shippingState.selectedIndex].value){
		localStorage.setItem("stateCodeInLastBill",shippingState.options[shippingState.selectedIndex].value);
	}
    

}
function pupulateStateCodes(stateCodesArray){
	let shippingState = document.getElementById("shippingState");
	
	let countOfStates = 0;
	let option = document.createElement('option');
	option.text = option.value = pleaseSelect;
	shippingState.add(option, countOfStates++);
	
	let stateCodeInLastBill = localStorage.getItem("stateCodeInLastBill");
	if (stateCodeInLastBill){
		let option = document.createElement('option');
		option.text = option.value = stateCodeInLastBill;
		shippingState.add(option, countOfStates++);
		
		option = document.createElement('option');
		option.text = option.value = "------------";
		option.disabled = true;
		shippingState.add(option, countOfStates++);
	}
	
	
	for(let i = 0; i <= stateCodesArray.length;i++) {
		let option = document.createElement('option');
	    option.text = option.value = stateCodesArray[i];
	    shippingState.add(option, countOfStates++);
	}
	
	
}
function postLogin(){
	document.getElementById("loginStatus").innerHTML = "";
	document.getElementById("login").style = "display: none;";
	document.getElementById("salesForm").style = "display: block;";
	fetchStateCodes();
}
function onBodyLoad(){
	let userID = localStorage.getItem("userID");
	
	if (userID){
		loginWithUserIDAndPwd(userID,localStorage.getItem("pwd"));
		
	}else {
		document.getElementById("login").style = "display: block;";
		document.getElementById("salesForm").style = "display: none;";
		
	}
}


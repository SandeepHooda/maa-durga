

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
let myGstStateCode = "";
function fetchInventory(){
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
				pupulateStateCodes(JSON.parse(this.responseText).stateCodes);
				pupulateInventoryItems(JSON.parse(this.responseText).inventoryItems);
				myGstStateCode = JSON.parse(this.responseText).myGstStateCode;
			}else {
				alert("Couln't get state codes from server");
				
			}
		     
		   }
		
	  
	  };
	xhr.open("GET", "/Inventory", true);
	xhr.send();
}
let pleaseSelect = "Please select";
let shippingStateCode = "";
let myCart = [];
function saveFavouriteStateCode(){
	
	let shippingState = document.getElementById("shippingState");
	if ( pleaseSelect != shippingState.options[shippingState.selectedIndex].value){
		myCart = [];
		publishCartItems();
		shippingStateCode =  shippingState.options[shippingState.selectedIndex].value.substring(0,2);
		localStorage.setItem("stateCodeInLastBill",shippingState.options[shippingState.selectedIndex].value);
	}

}

function pupulateInventoryItems(inventoryItemsDB){
	let inventoryItems = document.getElementById("inventoryItems");
	let countOfItems = 0;
	
	let option = document.createElement('option');
	option.text = option.value = pleaseSelect;
	inventoryItems.add(option, countOfItems++);
	
	for(let i = 0; i < inventoryItemsDB.length;i++) {
		let option = document.createElement('option');
	     option.value = JSON.stringify(inventoryItemsDB[i]);
	     option.text =  inventoryItemsDB[i].productDesc +" : " +inventoryItemsDB[i].inventoryDesc +" : "+inventoryItemsDB[i].modelNo;
	    inventoryItems.add(option, countOfItems++);
	}
	
	
}
function itemSelected(){
	let inventoryItems = document.getElementById("inventoryItems");
	let selectedProduct = JSON.parse(inventoryItems.options[inventoryItems.selectedIndex].value);
	if ( pleaseSelect != selectedProduct){
		document.getElementById("price").value = selectedProduct.salesPriceRegularWithoutTax;
	}
}

function addToCart(){
	let inventoryItems = document.getElementById("inventoryItems");
	let selectedProduct = JSON.parse(inventoryItems.options[inventoryItems.selectedIndex].value);
	selectedProduct.quantity = document.getElementById("quantity").value ;
	selectedProduct.rate = document.getElementById("price").value;
	selectedProduct.taxableValue = selectedProduct.quantity * selectedProduct.rate;
	

	if (myGstStateCode == shippingStateCode){
		selectedProduct.igst = 0;
		
	}else {
		selectedProduct.cgst = 0;
		selectedProduct.sgst = 0;
	}
	selectedProduct.cgstApplied = selectedProduct.taxableValue * selectedProduct.cgst /100 ;
	selectedProduct.sgstApplied = selectedProduct.taxableValue * selectedProduct.sgst /100 ;
	selectedProduct.igstApplied = selectedProduct.taxableValue * selectedProduct.igst /100 ;
	selectedProduct.cessApplied = selectedProduct.taxableValue * selectedProduct.cess /100 ;
	selectedProduct.totalRowAmount = selectedProduct.taxableValue + selectedProduct.cgstApplied  +selectedProduct.sgstApplied +selectedProduct.igstApplied +selectedProduct.cessApplied;
	
	myCart.push(selectedProduct);
	publishCartItems();
}

function publishCartItems(){
	let cartItemsHtml = "<table class='loginPage' border='1' >";
	cartItemsHtml += "<tr> <th> HSN  </th> <th> Quantity </th> <th> Rate </th> <th> Taxable Value </th>" +
	"<th> CGST </th><th> SGST </th><th> IGST </th><th> CESS </th>" +
			"<th> Total </th></tr>";
	for (let i=0;i<myCart.length;i++){
		cartItemsHtml += "<tr> <td> "+myCart[i].hsn+"	 </td> <td> "+myCart[i].quantity+" </td> <td> "+myCart[i].rate+" </td> <td> "+myCart[i].taxableValue+" </td>" +
				"<td> "+myCart[i].cgstApplied+" @ "+myCart[i].cgst+"% </td><td> "+myCart[i].sgstApplied+" @ "+myCart[i].sgst+"% </td><td> "+myCart[i].igstApplied+" @ "+myCart[i].igst+"% </td><td> "+myCart[i].cessApplied+" @ "+myCart[i].cess+"% </td>" +
						"<td> "+myCart[i].totalRowAmount+" </td></tr>";
	}
	cartItemsHtml += "</table>";
	document.getElementById("cart").innerHTML = cartItemsHtml;
	
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
	
	
	for(let i = 0; i < stateCodesArray.length;i++) {
		let option = document.createElement('option');
	    option.text = option.value = stateCodesArray[i];
	    shippingState.add(option, countOfStates++);
	}
	
	
}
function postLogin(){
	document.getElementById("loginStatus").innerHTML = "";
	document.getElementById("login").style = "display: none;";
	document.getElementById("salesForm").style = "display: block;";
	fetchInventory();
}
function onBodyLoad(){
	let userID = localStorage.getItem("userID");
	
	//Show login form only when user is logged out
	document.getElementById("login").style = "display: none;";
	document.getElementById("salesForm").style = "display: block;";
	if (userID){
		loginWithUserIDAndPwd(userID,localStorage.getItem("pwd"));
		
	}else {
		document.getElementById("login").style = "display: block;";
		document.getElementById("salesForm").style = "display: none;";
		
	}
}


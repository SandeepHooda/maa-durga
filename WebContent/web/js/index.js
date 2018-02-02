
let myGstStateCode = "";
let pleaseSelect = "Please select";
let shippingStateCode = "";
let myCart = [];
let myCartManual = [];
let maxColumnsInInvoiceGrid = 11;//don't change this else in manual grid all mataematic operation slike calc tax and total ect wil disturb
let maxRowsInInvoiceGrid = 10;

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
	selectedProduct.item = selectedProduct.inventoryDesc +" : "+selectedProduct.modelNo; 
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
	selectedProduct.rowTotal = selectedProduct.taxableValue + selectedProduct.cgstApplied  +selectedProduct.sgstApplied +selectedProduct.igstApplied +selectedProduct.cessApplied;
	
	myCart.push(selectedProduct);
	publishCartItems();
}
function deleteFromCart(itemID){
	var response = confirm("Delete item?");
	if (response == true) {
		myCart.splice(itemID, 1);
		publishCartItems();
	} else {
	    
	}
}
function publishCartItems(){
	let cartItemsHtml = "<table class='grid' border='1' >";
	cartItemsHtml += "<tr class='gridLargeCol'> <th> Item  </th><th> HSN  </th> <th> Quantity </th> <th> Rate </th> <th class='gridSmallCol'> Taxable Value </th>" +
	"<th> CGST </th><th> SGST </th><th> IGST </th><th> CESS </th><th> Total </th>" +
			"<th> Remove </th></tr>";
	for (let i=0;i<myCart.length;i++){
		cartItemsHtml += "<tr> <td class='gridLargeCol'> "+myCart[i].inventoryDesc +" : "+myCart[i].modelNo+"	 </td> <td> "+myCart[i].hsn+"	 </td> <td> "+myCart[i].quantity+" </td> <td> "+myCart[i].rate+" </td> <td> "+myCart[i].taxableValue+" </td>" +
				"<td> "+myCart[i].cgstApplied+" @ "+myCart[i].cgst+"% </td><td> "+myCart[i].sgstApplied+" @ "+myCart[i].sgst+"% </td><td> "+myCart[i].igstApplied+" @ "+myCart[i].igst+"% </td><td> "+myCart[i].cessApplied+" @ "+myCart[i].cess+"% </td>" +
						"<td> "+myCart[i].rowTotal+" </td> <td><span onclick=deleteFromCart('"+i+"') class='smallIcon'>&#x2718;</span></td> </tr>";
	}
	cartItemsHtml += "</table>";
	document.getElementById("cart").innerHTML = cartItemsHtml;
	
}

function clearManualCartRow(rowItem){
	var response = confirm("Delete item?");
	if (response == true) {
		for (let j=0;j<maxColumnsInInvoiceGrid-1;j++){
			 document.getElementById("manualCartItem"+rowItem+j).value = "";
		}
	} else {
	    
	}
	
}
function calcManualCartRowTotal(rowItem){
	let rowObject = {};
	rowObject.item =  document.getElementById("manualCartItem"+rowItem+0).value;
	rowObject.hsn =  document.getElementById("manualCartItem"+rowItem+1).value;
	rowObject.quantity =  document.getElementById("manualCartItem"+rowItem+2).value;
	rowObject.rate =  document.getElementById("manualCartItem"+rowItem+3).value;
	
		rowObject.taxableValue = rowObject.quantity * rowObject.rate;
		if (rowObject.item && !isNaN(rowObject.taxableValue) && rowObject.taxableValue >0 ){
			 document.getElementById("manualCartItem"+rowItem+4).innerHTML = rowObject.taxableValue ;
			 rowObject.cgst = document.getElementById("manualCartItem"+rowItem+5).value;
			 if ("" == rowObject.cgst){
				 rowObject.cgst = 0;
			 }
			 rowObject.cgstApplied =  rowObject.cgst * rowObject.taxableValue/100;
			 rowObject.sgst =  document.getElementById("manualCartItem"+rowItem+6).value;
			 if ("" == rowObject.sgst){
				 rowObject.sgst = 0;
			 }
			 rowObject.sgstApplied = rowObject.sgst * rowObject.taxableValue/100;
			 rowObject.igst = document.getElementById("manualCartItem"+rowItem+7).value;
			 if ("" == rowObject.igst){
				 rowObject.igst = 0;
			 }
			 rowObject.igstApplied =   rowObject.igst * rowObject.taxableValue/100;
			 rowObject.cess = document.getElementById("manualCartItem"+rowItem+8).value;
			 if ("" == rowObject.cess){
				 rowObject.cess = 0;
			 }
			 rowObject.cessApplied = rowObject.cess * rowObject.taxableValue/100;
			 rowObject.rowTotal =  rowObject.taxableValue +rowObject.cgstApplied+rowObject.sgstApplied+rowObject.igstApplied+rowObject.cessApplied;
			 document.getElementById("manualCartItem"+rowItem+9).innerHTML =rowObject.rowTotal.toFixed(2);
			 if (!isNaN(rowObject.rowTotal)){
				 return rowObject;
			 }
			 
		}else {
			 document.getElementById("manualCartItem"+rowItem+4).innerHTML = "";
			 document.getElementById("manualCartItem"+rowItem+9).innerHTML = "";
			 return null;
			 
		}
			
		
		
		
	
}
function toggleManualGrid(){
	manualGridVisibility = localStorage.getItem("manualGridVisibility");
	if ("on" == manualGridVisibility){
		document.getElementById("cartManualDiv").style = "display: none;"
		localStorage.setItem("manualGridVisibility", "off");
	}else {
		localStorage.setItem("manualGridVisibility", "on");
		document.getElementById("cartManualDiv").style = "display: block;"
	}
}
function generateManualCart(){
	let cartItemsHtml = "<table class='grid' border='1' >";
	cartItemsHtml += "<tr class='gridLargeCol'> <th> Item  </th><th> HSN  </th> <th> Quantity </th> <th> Rate </th> <th class='gridSmallCol'> Taxable Value </th>" +
	"<th> CGST% </th><th> SGST% </th><th> IGST% </th><th> CESS% </th><th> Total </th>" +
			"<th> Remove </th></tr>";
	for (let i=0;i<maxRowsInInvoiceGrid;i++){
		cartItemsHtml += "<tr>";
		
		for (let j=0; j<maxColumnsInInvoiceGrid; j++){
			cartItemsHtml += " <td";
			if (j==0){
				cartItemsHtml +=" class='gridLargeCol' >"	; 
			}else {
				cartItemsHtml +=" class='gridSmallInputbox' >";	 
			}
			if (j == maxColumnsInInvoiceGrid-1){
				cartItemsHtml += "<span onclick=clearManualCartRow('"+i+"') class='bigIconRed'>&#x2718;</span></td>";
			}else if(j == maxColumnsInInvoiceGrid-2 || j == 4){
				cartItemsHtml += "<span name='manualCartItem"+i+j+"' id='manualCartItem"+i+j+"' ></span></td>";
			}
			else {
				cartItemsHtml += " <input  ";
				if (j==0){
					cartItemsHtml += "  class='gridLargeCol' ";
				}else {
					cartItemsHtml += "  class='gridSmallInputbox' ";
				}
				cartItemsHtml += " onkeyup='calcManualCartRowTotal("+i+")' type='text' name='manualCartItem"+i+j+"' id='manualCartItem"+i+j+"' />	 </td>";
				
			}
			
		}
		
	}
	cartItemsHtml += "</table>";
	manualGridVisibility = localStorage.getItem("manualGridVisibility");
	document.getElementById("cartManualDiv").innerHTML = cartItemsHtml;
	if ("on" == manualGridVisibility){
		document.getElementById("cartManualDiv").style = "display: block;"
	}else {
		document.getElementById("cartManualDiv").style = "display: none;"
		
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
	getRecentInvoices(10);
}
function onBodyLoad(){
	generateManualCart();
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

function extractManualCartItems(){
	myCartManual = [];
	for (let i=0;i<maxRowsInInvoiceGrid;i++){
		let rowObj = calcManualCartRowTotal(i);
		if (null != rowObj){
			myCartManual.push(rowObj);
		}
	}
	
}
function submitCart(){
	document.getElementById("submitInvoiceResult").innerHTML = "";
	extractManualCartItems();
	let invoiceDetails = {};
	invoiceDetails.invoiceTime = new Date().getTime();
	invoiceDetails.myCartManual = myCartManual;
	invoiceDetails.myCart = myCart;
	if (invoiceDetails.myCart.length == 0  && invoiceDetails.myCartManual.length == 0 ){
		document.getElementById("submitInvoiceResult").innerHTML = "<br/></br/></br/><b><span style='color: red;'> Please add details for item sold to invoice before submitting. </span><b>"
	}else {
		invoiceDetails.customerName = document.getElementById("customerName").value;
		invoiceDetails.shippingAddress  = document.getElementById("shippingAddress").value;
		let shippingState = document.getElementById("shippingState");
		if ( pleaseSelect != shippingState.options[shippingState.selectedIndex].value){
			invoiceDetails.shippingState = shippingState.options[shippingState.selectedIndex].value
		}
		if (!invoiceDetails.customerName || !invoiceDetails.shippingAddress || !invoiceDetails.shippingState){
			document.getElementById("submitInvoiceResult").innerHTML = "<br/></br/></br/><b><span style='color: red;'> Customer name, addres and shipping state are mandatory fields. </span><b>";
		}else {
			submitInvoice(invoiceDetails)
		}
	}
	
	
}

function submitInvoice(invoiceDetails){
	
	if ( document.getElementById("submitCartButton").classList.contains('bigIcon') ){
		document.getElementById("submitCartButton").classList.remove('bigIcon');
		document.getElementById("submitCartButton").classList.add('bigIconGrey');
	}else {
		return;//Call in progress
	}
	
	
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
				document.getElementById("submitCartButton").classList.toggle('bigIcon');
				document.getElementById("submitCartButton").classList.toggle('bigIconGrey');
				alert('Your Invoice no is  '+this.responseText);
				getRecentInvoices(10);
			}else {
				document.getElementById("submitCartButton").classList.toggle('bigIcon');
				document.getElementById("submitCartButton").classList.toggle('bigIconGrey');
				
				if (401 == xhr.status){//User migh have logged out duw to inactivity
					let userID = localStorage.getItem("userID");
					if (userID){
						loginWithUserIDAndPwd(userID,localStorage.getItem("pwd"));
						document.getElementById("submitInvoiceResult").innerHTML = "<br/></br/></br/><b><span style='color: red;'> Please resubmit the invoice. </span><b>"
					}
				}else {
					document.getElementById("submitInvoiceResult").innerHTML = "<br/></br/></br/><b><span style='color: red;'> Error while submitting the invoive. Code :"+xhr.status+" </span><b>"
				}
				//document.getElementById("loginStatus").innerHTML = "<span>Request failed.  Returned status of " + xhr.status+"</span>";
				//window.scrollTo(0,document.body.scrollHeight);
			}
		     
		   }
		
	  
	  };
	xhr.open("POST", "/SubmitInvoice", true);
	xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	let invoiceDetailsStr = JSON.stringify(invoiceDetails);
	invoiceDetailsStr = invoiceDetailsStr.replace("\&", "AND");
	xhr.send("invoiceDetails="+invoiceDetailsStr);
	
}
function showRcentInvoices( rcentInvoices){
	
	let html = "<table class='grid' border='1' >";
	html += "<tr> <th> Invoice No  </th><th> Date  </th> <th> Customer Name </th> <th>Print</th> <th>Email</th> </tr>";
	var months = ["Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"];
	for (let i=0;i<rcentInvoices.length;i++){
		var date = new Date(rcentInvoices[i].invoiceTime);
		html += "<tr> <td>"+rcentInvoices[i].invoiceNo+"</td><td>"+date.getDate()+"-"+months[date.getMonth()]+"</td><td>"+rcentInvoices[i].customerName+"</td>" +
				"<td class='gridLargeCol'><a  target='_blank' href='/Print?invoiceNo="+rcentInvoices[i].invoiceNo+"&time="+rcentInvoices[i].invoiceTime+"'>Print</a></td>" +
				"<td class='gridLargeCol'><a target='_blank' href='/Print?invoiceNo="+rcentInvoices[i].invoiceNo+"&time="+rcentInvoices[i].invoiceTime+"'>Email</a></td></tr>";
	}
	html += "</table>";
	document.getElementById("recentInvoices").innerHTML = html;
	location.href = "#";
	location.href = "#submitCartButton";
	
	
}
function getRecentInvoices(count){
	
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
				let rcentInvoices = JSON.parse(this.responseText);
				showRcentInvoices(rcentInvoices);
				//window.scrollTo(0,document.body.scrollHeight);
			}else {
				
				//window.scrollTo(0,document.body.scrollHeight);
			}
		     
		   }
		
	  
	  };
	xhr.open("GET", "/RecentInvoices?count="+count, true);
	xhr.send();
	
	
}


function validateDeliveryAddress(){
	
	var address = $("#address").val();
	var city = $("#city").val();
	var state = $("#state").val();
	var zipcode = $("#zipcode").val();
	var country = $("#country").val();
	
	var returnValue = true;

	if (address === ""){
		$("#addressError").css({"visibility": "visible"});
		returnValue = false;
	}else{
		$("#addressError").css({"visibility": "hidden"});
	};
	
	if (city === ""){
		$("#cityError").css({"visibility": "visible"});
		returnValue = false;
	}else{
		$("#cityError").css({"visibility": "hidden"});
	};
	
	if (state === ""){
		$("#stateError").css({"visibility": "visible"});
		returnValue = false;
	}else{
		$("#stateError").css({"visibility": "hidden"});
	};
	
	if (zipcode === ""){
		$("#zipcodeError").css({"visibility": "visible"});
		returnValue = false;
	}else{
		$("#zipcodeError").css({"visibility": "hidden"});
	};
	
	if (country === ""){
		$("#countryError").css({"visibility": "visible"});
		returnValue = false;
	}else{
		$("#countryError").css({"visibility": "hidden"});
	};
	
	
	return returnValue;
}
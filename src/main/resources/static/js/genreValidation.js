function validateGenre(){
	
	var name = $("#name").val();
	var description = $("#description").val();
	
	var returnValue = true;
	
	if(name === ""){
		$("#nameError").css({"visibility": "visible"});
		returnValue = false;
	}else{
		$("#nameError").css({"visibility": "hidden"});
	}
	
	if(description === ""){
		$("#descriptionError").css({"visibility": "visible"});
		returnValue = false;
	}else{
		$("#descriptionError").css({"visibility": "hidden"});
	}
	
	return returnValue;
}
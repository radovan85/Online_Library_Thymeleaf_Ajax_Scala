function validateReview(){
	
	var text = $("#text").val();
	var rating = $("#rating").val();
	
	var returnValue = true;
	
	if(text === ""){
		$("#textError").css({"visibility": "visible"});
		returnValue = false;
	}else{
		$("#textError").css({"visibility": "hidden"});
	}
	
	if(rating === ""){
		$("#ratingError").css({"visibility": "visible"});
		returnValue = false;
	}else{
		$("#ratingError").css({"visibility": "hidden"});
	}
	
	return returnValue;
}
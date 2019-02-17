$("button.createProduct").click(function() {

	var description = $("form.createProduct input.productDescription").val();
	var name = $("form.createProduct input.productName").val();
	var price = $("form.createProduct input.productPrice").val();

	var product = {
		description: description,
		name: name,
		price: price
	};

	 $.ajax({
		type: 'POST',
		url: '/product',
		data: product,
		success: function(result) {
			alert('Product is created');
		}
	});

//	$.post("product", product, function(data) {
//		if (data == 'Success') {
//			alert('Product is created!');
//		}
//	});

});

$("button.buy-product").click(function() {

	var productId = jQuery(this).attr("product-id");

	$.post("bucket", {
		'productId': productId
	}, function(data) {
		if (data == 'Success') {
			$("[data-dismiss=modal]").trigger({
				type: "click"
			});
			alert('Success');
		}
	});

});
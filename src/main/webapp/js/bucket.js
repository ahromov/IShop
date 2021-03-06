function myFunction() {

	var input, filter, table, tr, td, i, txtValue;

	input = document.getElementById("myInput");
	filter = input.value.toUpperCase();
	table = document.getElementById("myTable");
	tr = table.getElementsByTagName("tr");

	for (i = 0; i < tr.length; i++) {
		td = tr[i].getElementsByTagName("td")[0];
		if (td) {
			txtValue = td.textContent || td.innerText;
			if (txtValue.toUpperCase().indexOf(filter) > -1) {
				tr[i].style.display = "";
			} else {
				tr[i].style.display = "none";
			}
		}
	}

}

function deleteOrderFromBucket(bId) {

	var customUrl = '';
	var urlContent = window.location.href.split('/');

	for (var i = 0; i < urlContent.length - 1; i++) {
		customUrl += urlContent[i] + '/'
	}

	customUrl += 'bucket?pId=' + bId;

	$.ajax({
		url : customUrl,
		type : 'DELETE',
		success : function(data) {
			if (data == 'Success') {
				location.reload();
			}
		}
	});

}

function checkRole(id) {

	$.get("user-role", function(data) {
		if (data === null) {
			alert('You must be loginned for purchase!');
			$(location).attr('href', 'login.jsp');
		} else
			$(location).attr('href', 'product?id=' + id);
	})

}

var buckets = null;

$
		.get("buckets", function(data) {
			if (data !== '') {
				buckets = data;
			}
		})
		.done(
				function() {
					var tableContent = "<tr class='header'>"
							+ "<th style='width: 20%;'>Photo</th>"
							+ "<th style='width: 20%;'>Name</th>"
							+ "<th style='width: 20%;'>Price</th>"
							+ "<th style='width: 20%;'>Quantity</th>"
							+ "<th style='width: 20%;'>Options</th>" + "</tr>";
					jQuery
							.each(
									buckets,
									function(i, value) {
										tableContent += "<tr><td><img style='width: 50px; margin-bottom: 20px' src='data:image/png;base64,"
												+ value.bs
												+ "'><td><a href='#' onclick='checkRole("
												+ value.productId
												+ ")'>"
												+ value.productName
												+ "</a></td><td>"
												+ value.productPrice
												+ "</td><td>"
												+ value.productsCount
												+ "</td>"
												+ "<td><button onclick=\"deleteOrderFromBucket('"
												+ value.productId
												+ "')\">delete</button></td>"
												+ "</tr>"
									});
					
					$('#myTable').html(tableContent);
				});

function goHomePage() {
	$.get("user-role", function(data) {
		if (data === null) {
			alert('You must be loginned for purchase!');
			$(location).attr('href', 'login.jsp');
		} else
			$(location).attr('href', 'cabinet.jsp');
	})
}

$('button.order').click(function() {
	$.get("user-role", function(data) {
		if (data === null) {
			alert('You must be loginned for purchase!');

			$(location).attr('href', 'login.jsp');
		} else {
			$('div.alert.alert-loading').show();

			$.post('order').done(function(data) {
				if (data === 'Success') {
					alert(data);

					$(location).attr('href', 'cabinet.jsp');
				} else
					$(location).attr('href', 'cabinet.jsp');
			})
		}
	})
})
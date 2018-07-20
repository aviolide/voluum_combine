$(document).ready(function(){
// var BASE_URL = "test";
    var BASE_URL = "/voluum_combine/";
	$('.start').click( function(){

		$.ajax({
			url: 'http://localhost:8080/start',
			cache: false,
			type : "POST",
			dataType : 'json',
			data : $(".request-form").serialize(),
			success : function(result) {

				// result
				console.log(result);
			},
			error: function(xhr, resp, text) {
				console.log(xhr, resp, text);
			}
		})
    });

	$('.chkplace').click( function(){

		var cid = $(this).closest("tr").find("cid").text();

		$.ajax({
			url: 'http://localhost:8080/check',
			cache: false,
			type : "POST",
			dataType : 'json',
			data : $(".request-form").serialize() + "&campaign=" + cid,
			success : function(result) {

				// result
				console.log(result);

			},
			error: function(xhr, resp, text) {
				console.log(xhr, resp, text);
			}
		})
    });

	$('.allchkplace').click( function(){

		var lines = $("textarea[name='allchkplace']").val().split('\n');
		campaigns = [];
		var campaignId;
		var zones = [];
		var line = 0
		for(var i = 0;i < lines.length;i++){
			var lgh = lines[i].length;
			if (lgh > 10) {
				if (line == 1) {
					campaigns.push( {"campaignId":campaignId, "zones":zones} );
					line=0;
					zones = [];
					campaignId = lines[i];
				} else {
					campaignId = lines[i];
					line=1;
				}
			} else {
				zones.push(lines[i]);
				if (i == lines.length-1) {
					campaigns.push( {"campaignId":campaignId, "zones":zones} );
				}

			}

		}
		//console.log(campaigns);
		$.ajax({
			url: 'http://localhost:8080/allcheck',
			cache: false,
			type : "POST",
			dataType : 'json',
			data : campaigns,
			success : function(result) {

				// result
				console.log(result);

			},
			error: function(xhr, resp, text) {
				console.log(xhr, resp, text);
			}
		})
    });

	$('#addtoformulaform').submit( function(e){
		e.preventDefault();
		var inp = $("input[name='formula']").val();
		if (inp.length > 0) {
			var currformula = $(".currentformula").text();
			if (currformula.length > 0) {
				$(".currentformula").text(currformula+';'+inp);
			} else {
				$(".currentformula").text(inp);
			}
		}

			var param = $(".currentformula").text();
        var urlpar = BASE_URL+'/add/formula';
		$.ajax({
			url: urlpar,
			type : "POST",
			contentType : "text/plain;charset=UTF-8",
            dataType : 'text',
			data : param,
			success : function(result) {

				// result
				console.log(result);

			},
			error: function(xhr, resp, text) {
				console.log(xhr, resp, text);
			}
		})
    });

	$('.btnclear1').click( function(){

		$(".currentformula").text("");
		$("input[name='formula']").val("");

		$.ajax({
			url: BASE_URL+'/clear_formula',
			cache: false,
			type : "GET",
			//dataType : 'json',
			//data : $(this).serialize(),
			success : function(result) {

				// result
				console.log(result);

			},
			error: function(xhr, resp, text) {
				console.log(xhr, resp, text);
			}
		})
    });

	$('#settimeform').submit( function(e){
		e.preventDefault();
		var settimemonth = $("input[name='settimemonth']").val();
		var settimeday = $("input[name='settimeday']").val();

        var urlpar = BASE_URL+'/set_time?month='+settimemonth+'&days='+settimeday;
		$.ajax({
			url: urlpar,
			type : "GET",
			success : function(result) {
				// result
				console.log(result);

			},
			error: function(xhr, resp, text) {
				console.log(xhr, resp, text);
			}
		})
    });
	$('.btnclear2').click( function(){

		$("input[name='settimemonth']").val("");
		$("input[name='settimeday']").val("");

    });

	$('.btnapply2').click( function(){

		var offeros = ($("input[name='offeros']").prop('checked') ? true : false);
		var offerbrowser = ($("input[name='offerbrowser']").prop('checked') ? true : false);
		var offerosbrowser = ($("input[name='offerosbrowser']").prop('checked') ? true : false);

		if ((offeros != true) && (offerbrowser != true) && (offerosbrowser != true)) {
			alert("Please choose filter variant");
		}

		$.ajax({
			url: "http://localhost:8080/analyzator/set_group?offeros="+offeros+"&offerbrowser="+offerbrowser+"&offerosbrowser="+offerosbrowser,
			cache: false,
			type : "GET",
			success : function(result) {
				console.log(result);
			},
			error: function(xhr, resp, text) {
				console.log(xhr, resp, text);
			}
		})
	});


    $('#addoffersform').submit( function(e){
        e.preventDefault();
        var idcompaign = $("input[name='idcompaign']").val();
        var idland = $("input[name='idland']").val();
        var comment = $("input[name='comment']").val();
        var posfix = $("input[name='postfix']").val();
        var idnew = $("input[name='idnew']").val();
        if ( $("input[name='newoldcampaign']").is(":checked") ) {
            var newoldcampaign = 'true';
        }
        else {
            var newoldcampaign = 'false'
        }
        $('<tr><td>'+idcompaign+'</td><td>'+idland+'</td><td>'+posfix+'</td><td>'+comment+'</td><td>'+idnew+'</td><td>'+newoldcampaign+'</td><td><div class="btn btnofferclear">Clear</div></td></tr>').appendTo( $("#addedoffers tbody") );

    });
    $('body').on('click', '.btnofferclear', function(){
            $(this).closest("tr").remove();

        });

        function printoffertable(input) {
            var table = '<table><tr><th>ID compaign</th><th>ID land</th><th>Comment</th><th>ID for new</th><th>New/Old</th></tr>';
            $.each(input.campaign_list,function(index, value){
                table += '<tr>'
                $.each(value,function(index2, value2){
                    table += '<td>'+value2+'</td>';
                    if (index2 == 'comment') {
                        table += '<td><div class="btn btnofferclear">Clear</div></td>';
                    }
                });
                table += '</tr>';
            });
            table += '</table>';
            return table;
        }

        $('#btnstartoffers').click( function(){

            var out = new Object();
            out.affiliate_network = $('input[name="affnetwork"]:checked').val();
            out.traffic_source = $('input[name="trafsource"]:checked').val();
            var trs = $("#addedoffers table tr");
            var camps_out = [];
            $.each(trs,function(index, value){
                var tds = $(value).find("td");
                var camp = [];
                var v = [];
                if (index > 0) {
                    $.each(tds,function(index2, value2){
                        var val = $(value2).text();
                        v[index2+1] = val;
                    });
                    camps_out.push({offer_id: v[1], land_id: v[2], postfix: v[3], comment: v[4], campaign_edit: v[5], old_campaign: v[6]});
                }
            });
            out.campaigns_list = camps_out;

            $.ajax({
                url: BASE_URL+'/add_offer/start',
                type : "POST",
                data: JSON.stringify( out ),
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                success : function(result) {
                    console.log(result);
                },
                error: function(xhr, resp, text) {
                    console.log(xhr, resp, text);
                }
            })
        });

        if (typeof offer_status !== "undefined") {
            $("#offerstatus").html( printofferstatustable( JSON.parse(offer_status) ) );
        }

        function printofferstatustable(input) {
            var table = '<table><tr><th>offer_id</th><th>land_id</th><th>aff_network</th><th>status</th></tr>';
            $.each(input,function(index2, value2){
                table += '<tr>'
                console.log(value2);
                table += '<td>'+value2.offer_id+'</td>';
                table += '<td>'+value2.land_id+'</td>';
                table += '<td>'+value2.affiliateNetwork.name_voluum+'</td>';
                table += '<td>'+value2.status+'</td>';
                table += '</tr>';
            });
            table += '</table>';
            return table;
        }

        $('#chkpendingoffers').click( function(){

            $.ajax({
                url: BASE_URL+'/add_offer/check_pending',
                type : "GET",
                success : function(result) {
                    console.log(result);
                },
                error: function(xhr, resp, text) {
                    console.log(xhr, resp, text);
                }
            })
        });



});
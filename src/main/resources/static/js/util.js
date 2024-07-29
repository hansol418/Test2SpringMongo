$(document).ready(function(){
		init()
		// FindAllFileName()
	// FindOneFileName("childLook.gif")
		 $('#preview').hide();
		 $('#previewVideo').hide();
	$('#findSearchMemoCount2').hide();

		 
		 
		   $(window).scroll(function() {
      if ($(this).scrollTop() > 100) {
        $('#scroll-to-top').fadeIn();
      } else {
        $('#scroll-to-top').fadeOut();
      }
    });

    
	})

	
	    // 이미지 삭제하는 기능. 
function imageDel(filename2){
	 var shouldDeleteImage = confirm("정말 삭제 할까요?  " + filename2 + "?");
	  if (shouldDeleteImage){
	var token = $("meta[name='_csrf']").attr("content");
            var header = $("meta[name='_csrf_header']").attr("content");
	$.ajax({
		type:"delete",
		beforeSend : function(xhr){
                    /* 데이터를 전송하기 전에 헤더에 csrf값을 설정 */
                    xhr.setRequestHeader(header, token);
                },
		url:"/images/deleteImage/"+filename2,
	})
	.done(function(resp){
		alert(filename2+"번 이미지 삭제 완료");
		location.href='/admin'
	})
	.fail(function(){
		alert("삭제 실패")
	})
	}
}
	
	//스크롤 버튼 부드럽게 동작하기. 
	 $('#scroll-to-top').click(function() {
      $('html, body').animate({scrollTop : 0},100);
      return false;
    });
    
    // 파일 선택시, 선택된 이미지 미리보기 , 로드이미지 함수 출력시 해당 아이디 보여줌.
    // 평소에는 숨김.
	    function loadImage() {
		
            var input = document.getElementById("image");
            /*console.log(input.files[0].name)*/
            var fileStr = input.files[0].name;
              var str2 = fileStr.substring(fileStr.lastIndexOf('.') + 1);
              
            if (input.files && input.files[0] &&str2 !='mp4' &&str2 !='mov' &&str2 !='avi' &&str2 !='wmv' &&str2 !='MOV') {
				$('#preview').show();
				$('#previewVideo').hide();
				$('#imgId4').hide();
                var reader = new FileReader();
                reader.onload = function(e) {
                    var img = document.getElementById("preview");
                    img.src = e.target.result;
                };
                reader.readAsDataURL(input.files[0]);
            } else {
				$('#previewVideo').show();
				$('#preview').hide();
				$('#imgId4').hide();
                var reader = new FileReader();
                reader.onload = function(e) {
                    var img = document.getElementById("previewVideo");
                    img.src = e.target.result;
                };
                reader.readAsDataURL(input.files[0]);
}
        }
	
	// 파일이미지 이름 모두 가져오기. 
	// 가져와서, innerHTML 으로 테이블 만들기. 
function FindAllFileName () {
            $.ajax({
                url: "/images/findFileNameAll",
                type: "GET",
                })
                .done (function(data) {
					var html = '<div class="row">';
					for (var i = 0; i < data.length; i++) {
						html += '<div class="col-md-2 margin"> ';
						html += '<div class="card"> ';
					  var filename = data[i];
					  var str = filename.substring(filename.lastIndexOf('.') + 1);
					  if(str=='mp4' || str=='mov' || str=='MOV' || str=='avi' || str=='wmv'){
						html += '<video controls src='+'/images/'+filename+'></video>';
					}
					else {
					  html += '<img src='+'/images/'+filename+'>';
					  }
					  html +=  '<div class="card-body">';
					  html +=  '<h4 class="card-title">'+filename+'</h4>';
					  html +=  '<p class="card-text">강의설명 준비중</p>';
					  html+="<a href=javascript:imageDel('"+filename+"')>삭제</a>";
					  html += '</div>';
					  html += '</div>';
					  html += '</div>';
}
               html += '</div>';
               $('#ImageTest').html(html);
                })
                };

		//이미지 하나 가져오기.
function FindOneFileName (imageName1) {
	$.ajax({
		url: "/images/"+imageName1,
		type: "GET",
	})
		.done (function(data) {
			var html = '<div class="row">';

				html += '<div class="col-md-2 margin"> ';
				html += '<div class="card"> ';
				console.log("data : " + data)
				var filename = data;
				var str = filename.substring(filename.lastIndexOf('.') + 1);
				if(str=='mp4' || str=='mov' || str=='MOV' || str=='avi' || str=='wmv'){
					html += '<video controls src='+'/images/'+filename+'></video>';
				}
				else {
					html += '<img src='+'/images/'+filename+'>';
				}
				html +=  '<div class="card-body">';
				html +=  '<h4 class="card-title">'+filename+'</h4>';
				html +=  '<p class="card-text">강의설명 준비중</p>';
				html+="<a href=javascript:imageDel('"+filename+"')>삭제</a>";
				html += '</div>';
				html += '</div>';
				html += '</div>';

			html += '</div>';
			$('#ImageOne').html(html);
		})
};
        
        
        // 메인에 유저 게시글 목록 , 돔 준비되면 항상 목록 출력해주는 기능. 	
// 메모 및 등록날짜, 수정 버튼 부분 문자열 부분 큰따옴표안에 작은 따옴표 수정. 
// 자바스크립트 앞에 작은 따옴표 생략 했음.
var init = function(){
		$.ajax({
			type:"get",
			url:"/findAllMemo",
			dataType:"JSON",
			contentType:"application/json;charset=utf-8",
		})
		.done(function(resp){
			//alert("resp"+resp)
			var str2 = resp.length
			// alert("str2"+str2)
			var str = "<table class='table table-hover mt-3  ' border=1>";
			str +="<th>" +"사진"+"</th>"
				str +="<th>" +"제목"+"</th>"
				str +="<th>" +"메세지"+"</th>"
				str +="<th>" +"등록일"+"</th>"
				str +="<th>" +"수정"+"</th>"
				str +="<th>" +"삭제"+"</th>"
			$.each(resp,function(key,val){
		/*		console.log("val.id : "+ val.id)
				console.log("val.title: "+ val.title)
				console.log("val.message: "+ val.message)
				console.log("val.dateField: "+ val.dateField)
				var id2= val.id
				console.log(typeof(id2))*/
				str += "<tr>"
				str += "<td>" +"<img src=/images/"+val.imageFileName+ "></td>"
				str += "<td>" + val.title + "</td>"
				str += "<td>" + val.message + "</td>"
				str += "<td>" + val.dateField + "</td>"
				str+= "<td><a href=javascript:dbUpdateFormMemo('"+val.id+"')>수정</a></td>"
				str+= "<td><a href=javascript:dbDel('"+val.id+"','"+val.imageFileName+"')>삭제</a></td>"
				
				str += "</tr>"

			})
			str += "</table>"
			$("#dbResult").html(str);
			$("#findAllMemoCount").html(str2);

		})
	};
	


function dbUpdateFormMemo(id){
	location.href='/updateFormMemo/'+id;
}


//메모 with 이미지
$("#uploadDBWithImageBtn").click(function(){
	$('#my-form').on('submit', function(e) {
		e.preventDefault();

		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");


		// var formData = new FormData(this);

		var formData = new FormData();

		var data={
			/*"id":$("#dbId").val(),*/
			"title":$("#dbTitle").val(),
			"message":$("#dbMessage").val()

		}

            var input = document.getElementById("image");
            /*console.log(input.files[0].name)*/
            var file = input.files[0];
		
		formData.append('file',file);
		formData.append('key', new Blob([ JSON.stringify(data) ], {type : "application/json"}));

		$.ajax({
			url: '/insertMemoWithImage',
			type: 'POST',
			data: formData,

			processData: false,
			contentType: false,
			beforeSend : function(xhr){
				/* 데이터를 전송하기 전에 헤더에 csrf값을 설정 */
				xhr.setRequestHeader(header, token);
			},
			success  : function(result, status){
				alert('업로드 성공');
				location.href='/admin'
			},
			error : function(jqXHR, status, error){

				if(jqXHR.status == '401'){
					alert('로그인 후 이용해주세요');
					location.href='/members/login';
				} else{
					alert(jqXHR.responseText);
				}

			}
		});
	});
});




//검색 버튼 클릭시 , searchDB : 검색 조건, searchContent : 검색 내용.
$("#dbSearchBtn").click(function(){
	$('#findSearchMemoCount2').show();
	var token = $("meta[name='_csrf']").attr("content");
            var header = $("meta[name='_csrf_header']").attr("content");
            
			var searchData = {
		"searchContent":$("#searchContent").val(),
		"searchDB":$("#searchDB option:selected").val()
	}
	/*console.log(searchData)*/
	$.ajax({
		type:"post",
		url:"/searchDb",
		    beforeSend : function(xhr){
                    /* 데이터를 전송하기 전에 헤더에 csrf값을 설정 */
                    xhr.setRequestHeader(header, token);
                },
		contentType:"application/json;charset=utf-8",
		data:JSON.stringify(searchData)
	})
	.done(function(resp){
		var str4 = resp.length
	 	 var str = "<table class='table table-hover mt-3 ' border=1>";
				str +="<th>" +"사진"+"</th>"
				str +="<th>" +"제목"+"</th>"
				str +="<th>" +"메세지"+"</th>"
				str +="<th>" +"등록일"+"</th>"
				str +="<th>" +"수정"+"</th>"
				str +="<th>" +"삭제"+"</th>"
			$.each(resp,function(key,val){
				str += "<tr>"
				str += "<td>" +"<img src=/images/"+val.imageFileName+ "></td>"
				str += "<td>" + val.title + "</td>"
				str += "<td>" + val.message + "</td>"
				str += "<td>" + val.dateField + "</td>"
				str+= "<td><a href=javascript:dbUpdateFormMemo('"+val.id+"')>수정</a></td>"
				str+= "<td><a href=javascript:dbDel2('"+val.id+"','"+val.imageFileName+"')>삭제</a></td>"
				str += "</tr>"
			})
			str += "</table>"
			$("#searchResult").html(str);
		$("#findSearchMemoCount").html(str4);
			})
	.fail(function(){
		alert("디비 검색 실패")
	});
	});

// 메인 처럼 사용중. 
$("#listBtn").click(function(){
	location.href='/admin'
	});

var init2 = function(){
	location.href='/admin'
}
	
// 메모 수정창에서, 수정시 호출되는 함수.
$("#dbUpdateBtn2").click(function(){
	var shouldUpdate = confirm("정말 수정 할까요?");
	if (shouldUpdate) {
		$('#my-form').on('submit', function(e) {
			e.preventDefault();
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");

		var formData = new FormData();

		var data = {
			"id": $("#dbId").val(),
			"title": $("#dbTitle").val(),
			"message": $("#dbMessage").val()
		}
				var input = document.getElementById("image");
				var file = input.files[0];
				formData.append('file',file);
				formData.append('key', new Blob([ JSON.stringify(data) ], {type : "application/json"}));

		$.ajax({
			type: "post",
			url: "/updateWithMemo",
			data: formData,
			processData: false,
			contentType: false,
			beforeSend: function (xhr) {
				/* 데이터를 전송하기 전에 헤더에 csrf값을 설정 */
				xhr.setRequestHeader(header, token);
			},


		})
			.done(function (resp) {
				alert('디비 수정 완료');
				location.href = '/admin'
			})
			.fail(function () {
				alert("디비 수정 실패")
			});
			}
		)};
});

	
//메모 게시글 하나 삭제 기능. 	
function dbDel(id,imageFileName){
	  var shouldDelete = confirm("정말 삭제 할까요?");
	  if (shouldDelete){
	      var token = $("meta[name='_csrf']").attr("content");
            var header = $("meta[name='_csrf_header']").attr("content");
	
	$.ajax({
		type:"delete",
		url:"/dbDelete/"+id+"/"+imageFileName,
		       beforeSend : function(xhr){
                    /* 데이터를 전송하기 전에 헤더에 csrf값을 설정 */
                    xhr.setRequestHeader(header, token);
                },
	})
	.done(function(resp){
		alert("글 삭제 완료");
		init();
	})
	.fail(function(){
		alert("삭제 실패")
	})
	}
	
}

function dbDel2(id,imageFileName){
	var shouldDelete = confirm("정말 삭제 할까요?");
	if (shouldDelete){
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");

		$.ajax({
			type:"delete",
			url:"/dbDelete/"+id+"/"+imageFileName,
			beforeSend : function(xhr){
				/* 데이터를 전송하기 전에 헤더에 csrf값을 설정 */
				xhr.setRequestHeader(header, token);
			},
		})
			.done(function(resp){
				alert("글 삭제 완료");
				init2();
			})
			.fail(function(){
				alert("삭제 실패")
			})
	}

}

//검색 버튼 클릭시 , searchDB : 검색 조건, searchContent : 검색 내용.
//반찬 먼저 테스트
$("#dbSearchBtn2").on('click','#ban', function(){
/*$("#dbSearchBtn2").click(function(){*/
	$('#findSearchMemoCount2').show();
	var token = $("meta[name='_csrf']").attr("content");
            var header = $("meta[name='_csrf_header']").attr("content");
            
            // alert("테스트 : "+$(this).attr("value"));
            var searchCon =  $(this).attr("value");
            // alert(" searchCon1 " + searchCon);
            
            if (searchCon == "반찬"){
	        // alert(" searchCon 2" + searchCon);
	        		var searchData = {
		"searchContent":"반찬",
		"searchDB":"title"
	}
} else if (searchCon == "음료") {
	  // alert(" searchCon " + searchCon);
	        		var searchData = {
		"searchContent":"음료",
		"searchDB":"title"
	}
} else if (searchCon == "과일") {
				// alert(" searchCon " + searchCon);
				var searchData = {
					"searchContent":"과일",
					"searchDB":"title"
				}
			}
else if (searchCon == "재료") {
	// alert(" searchCon " + searchCon);
	var searchData = {
		"searchContent":"재료",
		"searchDB":"title"
	}
}
else if (searchCon == "소스") {
	// alert(" searchCon " + searchCon);
	var searchData = {
		"searchContent":"소스",
		"searchDB":"title"
	}
}
else if (searchCon == "냉동") {
	// alert(" searchCon " + searchCon);
	var searchData = {
		"searchContent":"냉동",
		"searchDB":"title"
	}
}
else if (searchCon == "간식") {
	// alert(" searchCon " + searchCon);
	var searchData = {
		"searchContent":"간식",
		"searchDB":"title"
	}
}
            
	/*		var searchData = {
		"searchContent":"반찬",
		"searchDB":"title"
	}*/
	/*console.log(searchData)*/
	        // alert(" searchData " + searchData.searchContent + searchData.searchDB);
	$.ajax({
		type:"post",
		url:"/searchDb",
		    beforeSend : function(xhr){
                    /* 데이터를 전송하기 전에 헤더에 csrf값을 설정 */
                    xhr.setRequestHeader(header, token);
                },
		contentType:"application/json;charset=utf-8",
		data:JSON.stringify(searchData)
	})
	.done(function(resp){
		var str3 = resp.length
	 	 var str = "<table class='table table-hover mt-3 ' border=1>";
				str +="<th>" +"사진"+"</th>"
				str +="<th>" +"제목"+"</th>"
				str +="<th>" +"메세지"+"</th>"
				str +="<th>" +"등록일"+"</th>"
				str +="<th>" +"수정"+"</th>"
				str +="<th>" +"삭제"+"</th>"
			$.each(resp,function(key,val){
				str += "<tr>"
				str += "<td>" +"<img src=/images/"+val.imageFileName+ "></td>"
				str += "<td>" + val.title + "</td>"
				str += "<td>" + val.message + "</td>"
				str += "<td>" + val.dateField + "</td>"
				str+= "<td><a href=javascript:dbUpdateFormMemo('"+val.id+"')>수정</a></td>"
				str+= "<td><a href=javascript:dbDel2('"+val.id+"','"+val.imageFileName+"')>삭제</a></td>"
				str += "</tr>"
			})
			str += "</table>"
			$("#searchResult").html(str);
		$("#findSearchMemoCount").html(str3);

			})
	.fail(function(){
		alert("디비 검색 실패")
	});
	});



$(document).ready(function(){
		// init()
		// FindAllFileName()
		 $('#preview').hide();
		 $('#previewVideo').hide();
		 
		 
		   $(window).scroll(function() {
      if ($(this).scrollTop() > 100) {
        $('#scroll-to-top').fadeIn();
      } else {
        $('#scroll-to-top').fadeOut();
      }
    });
    


    
	})
	
	
	//스크롤 버튼 부드럽게 동작하기. 
	 $('#scroll-to-top').click(function() {
      $('html, body').animate({scrollTop : 0},100);
      return false;
    });
    
    
	
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
						html += '<div class="col-md-4 margin"> ';
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
					  html += '</div>';
					  html += '</div>';
					  html += '</div>';
}
               html += '</div>';
               $('#ImageTest').html(html);
                })
                };
        
	


//검색 버튼 클릭시 , searchDB : 검색 조건, searchContent : 검색 내용.
$("#dbSearchBtn").click(function(){
	      var token = $("meta[name='_csrf']").attr("content");
            var header = $("meta[name='_csrf_header']").attr("content");
            
			var searchData = {
		"searchContent":$("#searchContent").val(),
		"searchDB":$("#searchDB option:selected").val()
	}
	console.log(searchData)
	$.ajax({
		type:"post",
		url:"/searchDb",
		contentType:"application/json;charset=utf-8",
		           beforeSend : function(xhr){
                    /* 데이터를 전송하기 전에 헤더에 csrf값을 설정 */
                    xhr.setRequestHeader(header, token);
                },
		data:JSON.stringify(searchData)
	})
	.done(function(resp){
	 	 var str = "<table class='table table-hover mt-3 ' border=1>";
				str +="<th>" +"아이디"+"</th>"
				str +="<th>" +"제목"+"</th>"
				str +="<th>" +"메세지"+"</th>"
			$.each(resp,function(key,val){
				str += "<tr>"
				str += "<td>" + val.id + "</td>"
				str += "<td>" + val.title + "</td>"
				str += "<td>" + val.message + "</td>"
				str += "</tr>"
			})
			str += "</table>"
			$("#searchResult").html(str);
			})
	.fail(function(){
		alert("디비 검색 실패")
	});
	});

// 메인 처럼 사용중. 
$("#listBtn").click(function(){
	location.href='/'
	});

// 유저 게시글 실제 업데이트 처리 부분. 
$("#dbUpdateBtn").click(function(){

      var token = $("meta[name='_csrf']").attr("content");
            var header = $("meta[name='_csrf_header']").attr("content");

	var data={
			"id":$("#dbId").val(),
			"title":$("#dbTitle").val(),
			"message":$("#dbMessage").val()
	}
	
	$.ajax({
		type:"post",
		url:"/updateDb",
		contentType:"application/json;charset=utf-8",
		           beforeSend : function(xhr){
                    /* 데이터를 전송하기 전에 헤더에 csrf값을 설정 */
                    xhr.setRequestHeader(header, token);
                },
		data:JSON.stringify(data)
	})
	.done(function(resp){
			location.href='/'
			})
	.fail(function(){
		alert("디비 수정 실패")
	});
	});
	
//수정 하는 폼에 임시로 테이블 만들어 보여주기.
function dbUpdate(id){
	$.ajax({
		type:"get",
		url:"/updateForm/"+id,
	})
	.done(function(resp){
		  location.href='/updateForm/id';
				var str = "<table class='table table-hover mt-3' border=1>";
				str +="<th>" +"아이디"+"</th>"
				str +="<th>" +"제목"+"</th>"
				str +="<th>" +"메세지"+"</th>"
			$.each(resp,function(key,val){
				str += "<tr>"
				str += "<td>" + val.id + "</td>"
				str += "<td>" + val.title + "</td>"
				str += "<td>" + val.message + "</td>"
				str+= "<td><a href='javascript:dbUpdateGo("+val.id+")'>수정</a></td>"
				
				str += "</tr>"
			})
			str += "</table>"
			$("#dbResult").html(str);
		
	})
	.fail(function(){
		
	})
}




//유저 게시글 하나 삭제 기능. 	
function dbDel(id){
	$.ajax({
		type:"delete",
		url:"/dbDelete/"+id,
	})
	.done(function(resp){
		alert(id+"번 글 삭제 완료");
		init();
	})
	.fail(function(){
		alert("삭제 실패")
	})
}

// 메인에 유저 게시글 목록 , 돔 준비되면 항상 목록 출력해주는 기능. 	
var init = function(){
		$.ajax({
			type:"get",
			url:"/findAll",
			dataType:"JSON",
			contentType:"application/json;charset=utf-8",
		})
		.done(function(resp){
			//alert("resp"+resp)
			var str = "<table class='table table-hover mt-3  ' border=1>";
				str +="<th>" +"아이디"+"</th>"
				str +="<th>" +"제목"+"</th>"
				str +="<th>" +"메세지"+"</th>"
			$.each(resp,function(key,val){
				str += "<tr>"
				str += "<td>" + val.id + "</td>"
				str += "<td>" + val.title + "</td>"
				str += "<td>" + val.message + "</td>"
				str += "</tr>"
			})
			str += "</table>"
			$("#dbResult").html(str);
		})
	};


// 유저 게시글 등록 
$("#dbInsertBtn").click(function(){
	
	      var token = $("meta[name='_csrf']").attr("content");
            var header = $("meta[name='_csrf_header']").attr("content");

	var data={
			"id":$("#dbId").val(),
			"title":$("#dbTitle").val(),
			"message":$("#dbMessage").val()
			
	}
	
	$.ajax({
		type:"post",
		url:"/insertDb",
		contentType:"application/json;charset=utf-8",
		  beforeSend : function(xhr){
                    /* 데이터를 전송하기 전에 헤더에 csrf값을 설정 */
                    xhr.setRequestHeader(header, token);
                },
		data:JSON.stringify(data)
	})
	.done(function(resp){
		alert("디비 추가 성공")
				init();
			})
	.fail(function(){
		alert("디비 추가 실패")
	});
});


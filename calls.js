$(document).ready(function(){
    $.ajax({
          url:"http://localhost:4567/image" ,
          })
          .done(function (data) {
            console.log(data);
            var res = data.substring(10,100);
            console.log(res);
            $('#blurredIMG').attr('src', res);
            $('input[name="image"]').val(res);
       
        });
});


function formSubmit(){
    console.log("FUCKKKKK");
    $.ajax({
       url: "http://localhost:4567/bid",
       data: $("#bid").serialize(),
       success: function(data){
           console.log(data);
       }
    });
}
/*
$("#bid").submit(function(e){
    var url = "http://localhost:4567/bid"
    
    $.ajax({
       type: "POST",
       url: url,
       data: $("#bid").serialize(),
       success: function(data){
           console.log(data);
       }
    });
    e.preventDefault();
});
*/


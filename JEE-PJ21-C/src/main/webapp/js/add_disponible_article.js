
$(function() {

  $(".ajouter").click(function (e) {

    var ref = $(this).data("ref");

    console.log(ref)

    $.ajax({
      method: 'post',
      url: "./panier/"+ref,
      dataType:"json",
      contentType:"application/json",
      data:JSON.stringify({qty:1,id:ref})
    }).done(function(){

       location.reload();
    });

  });


});

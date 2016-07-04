$ ->
  $.get "/files", (files) ->
    $.each files, (index,file) ->
      $('#files').append $("<li>").text 'id='+ file.id+ 'name='+file.name


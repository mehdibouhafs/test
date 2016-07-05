

$(function() {

    console.log("Demarage Script");
    var ul = $('#upload ul');

    $('#drop a').click(function () {
        // Simulate a click on the file input button
        // to show the file browser dialog
        $(this).parent().find('input').click();

    });


// Helper function that formats the file sizes
        function formatFileSize(bytes) {
            if (typeof bytes !== 'number') {
                return '';
            }

            if (bytes >= 1000000000) {
                return (bytes / 1000000000).toFixed(2) + ' GB';
            }

            if (bytes >= 1000000) {
                return (bytes / 1000000).toFixed(2) + ' MB';
            }

            return (bytes / 1000).toFixed(2) + ' KB';
        }

    $('input[type="file"]').on('change', function (event, files, label) {
     var ul = $('#upload ul');
     var file_name = this.value.replace(/\\/g, '/').replace(/.*\//, '')
     if(file_name ==  ''){
            alert('YOU MUST SELECT FILE');
     }
        else{
     $('#drop').hide();
        var tpl = $('<li class=""><input type="text" value="0" data-width="48" data-height="48"' +
                                        ' data-fgColor="#0788a5" data-readOnly="1" data-bgColor="#3e4043" /><p></p><span id="span"></span></li>'+
                                        ' <br/>  <input type="submit" class="btn btn-primary" value="upload"/>');
        var file_name = this.value.replace(/\\/g, '/').replace(/.*\//, '')
        $('.filename').text(file_name);

        tpl.find('p').text(file_name);
        tpl.appendTo(ul);

        tpl.find('span').click(function(){
                tpl.fadeOut(function () {
                                    tpl.remove();
                                    $('#drop').show();
                                });
              });

              }

    });

    });











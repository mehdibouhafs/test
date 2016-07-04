$(function() {

    console.log("Demarage Script");
    var ul = $('#upload ul');

    $('#drop a').click(function () {
        // Simulate a click on the file input button
        // to show the file browser dialog
        $(this).parent().find('input').click();

    });

   /* // Add events
    $('input[type=file]').on('change', prepareUpload);

// Grab the files and set them to our variable
    function prepareUpload(event)
    {
        files = event.target.files;
    }


    // Initialize the jQuery File Upload plugin
    $('#upload').on('submit', uploadFiles);

// Catch the form submit and upload the files
    function uploadFiles(event) {
        event.stopPropagation(); // Stop stuff happening
        event.preventDefault(); // Totally stop stuff happening*/

        // START A LOADING SPINNER HERE

        // Create a formdata object and add the files

        $('#upload').fileupload({
            // This function is called when a file is added to the queue;
            // either via the browse button, or via drag/drop:
            add: function (e, data) {

                var tpl = $('<li class="working"><input type="text" value="0" data-width="48" data-height="48"' +
                    ' data-fgColor="#0788a5" data-readOnly="1" data-bgColor="#3e4043" /><p></p><span></span></li>');

                // Append the file name and file size
                tpl.find('p').text(data.files[0].name)
                    .append('<i>' + formatFileSize(data.files[0].size) + '</i>');

                // Add the HTML to the UL element
                data.context = tpl.appendTo(ul);
                // Initialize the knob plugin
                tpl.find('input').knob();

                // Listen for clicks on the cancel icon
                tpl.find('span').click(function () {

                    if (tpl.hasClass('working')) {
                        jqXHR.abort();
                    }

                    tpl.fadeOut(function () {
                        tpl.remove();
                    });

                });

                // Automatically upload the file once it is added to the queue
                var jqXHR = data.submit();

            },

            progress: function (e, data) {

                // Calculate the completion percentage of the upload
                var progress = parseInt(data.loaded / data.total * 100, 10);

                // Update the hidden input field and trigger a change
                // so that the jQuery knob plugin knows to update the dial
                data.context.find('input').val(progress).change();

                if (progress == 100) {
                    data.context.removeClass('working');


                    var span = $('#upload p1');
                    var tpl1 = $('<br/><br/><a href="/params?id='+parseInt(id)+'"class="btn btn-primary" role="button">GO TO PARAMETRAGE</a>');
                    span.append(tpl1);


                }
            },

            fail: function (e, data) {
                // Something has gone wrong!
                data.context.addClass('error');
            }
        });


        // Prevent the default action when a file is dropped on the window
        $(document).on('drop dragover', function (e) {
            e.preventDefault();
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

        //var msg = $.ajax({type: "GET", url: "my_script.php", async: false}).responseText;

         /*function ok(){
         console.log("CALL ok");
         $.ajax({
                url: '/ajax',
                  success: function(data, textStatus, jqXHR) {
                     //window.alert(data);
                     var id = parseInt(data);
                     console.log("data"+data);
                     console.log("id"+id);

                  },
                  error: function(jqXHR, textStatus, errorThrown) {
                    window.alert(textStatus);
                  }
                 });
                            //var idInt = parseInt(id)
                            }

                */
});


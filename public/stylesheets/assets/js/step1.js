$(document).ready(function() {
    var allTables;
    var i = 0;


    var my_delay = 10000;

    // call your ajax function when the document is ready...
    $(function() {
        callAjax();
    });

// function that processes your ajax calls...
    var nb;
    function callAjax() {
        $.ajax({
            type: "GET",//la method à utiliser soit POST ou GET
            url: "/viewedClasses", //lien de la servlet qui exerce le traitement sur les données
            dataType: 'json',
            success: function (data) {
                console.log("data load "+data);
                nb = data.length;
                if(data.length > 0 ){
                    $("#spanNB").text(""+data.length);
                    $("#spanLi").text("you have "+ data.length+" new notification (s)");
                    $("#spanLi").append('<li class="header"></li> <li> <ul class="menu"> <li><li><a id="nbClasse2" href="#"><i class="fa fa-database text-aqua"></i>'+data[i].className+' was added today by '+data[i].user_email+ '</a> </li></ul> </li> <li class="footer"><a href="#">View all</a></li>');
                    nb = data.length + 1;
                }
                setTimeout(callAjax, my_delay);
            }
        });
    }
    
    $(".notifications-menu").on('click',function(e) {
        e.preventDefault();
        $("#spanNB").text("");
        $.ajax({
            type: "POST",//la method à utiliser soit POST ou GET
            url: "/viewedClasse", //lien de la servlet qui exerce le traitement sur les données
            dataType: 'json',
            success: function (data) {

            }
        });
    });


    function loger() {
        console.log("ok");
    }
    
    
    $("#home").on('click',function () {
        window.location.href = "/home";
    })
    

    $.ajax({
        type: "GET",//la method à utiliser soit POST ou GET
        url: "/classes", //lien de la servlet qui exerce le traitement sur les données
        dataType: 'json',
        success: function (data) {// le cas ou la requete est bien execute en reçoi les données serialiser par JSON dans la variable msg
            //recuperation de la valeur stock dans l'attribut desactive
            for (var i = 0; i < data.length; i++) {
                $("#latestTab").append("<tr id='"+i+"' value='"+data[i].className+"'>" +
                    "<td >" + data[i].className + "</td>" +
                    "<td><button class='attribute' value='" + data[i].className + "'>" +"<span class='fa fa-eye'></span></a></button></div></td>"+
                    "<td><button  class='btn btn-danger' value='" + data[i].className + "'><span class='glyphicon glyphicon-remove-sign'></span></button></td></tr>");
            }
            allTables = $('#latestTabId').DataTable({
                'fnClearTable': true,
                "scrollY": "300px",
                "scrollCollapse": true
            });
        },
        error: function () { //erreur dans le cas les données ne sont pas envoyer on affiche un message qui indique l'erreur
            console.log("error");
        }
    });

    var colSelected;
    $('#latestTabId tbody').on( 'click', 'tr', function () {
        if ( $(this).hasClass('selected') ) {
            $(this).removeClass('selected');
        }
        else {
            allTables.$('tr.selected').removeClass('selected');
            $(this).addClass('selected');
            colSelected= $(this).attr('value');
        }
    });


    $("#validateExisteCsv").on('click',function (e) {
        e.preventDefault();
        var classe = "fragmentRootName="+colSelected+"&";
        var data = classe.concat($('#form00').serialize());
        $.ajax({
            type: "POST",//la method à utiliser soit POST ou GET
            url: "/csvExistHeader", //lien de la servlet qui exerce le traitement sur les données
            data:data,
            dataType: 'json',
            xhr: function () {
                var xhr = new window.XMLHttpRequest();
                //Download progress
                xhr.addEventListener("progress", function (evt) {
                    console.log(evt.lengthComputable);
                    if (evt.lengthComputable) {
                        var percentComplete = evt.loaded / evt.total;
                    }
                }, false);
                return xhr;
            },
            beforeSend: function () {
                var progressbar = $( "#progressbar" ),
                    progressLabel = $( ".progress-label" );
                progressbar.progressbar({
                    value: false,
                });
                $('#pleaseWaitDialog').modal('show');
            },
            complete: function () {
                $('#pleaseWaitDialog').modal('hide');
            },
            success: function (data) {// le cas ou la requete est bien execute en reçoi les données serialiser par JSON dans la variable msg
                $("#contenu").html("");
                var contenu = "";
                console.log("data.length" + data.inputError.length);
                if ((data.inputError.length) == 0) {
                    contenu += "<fieldset><div class='panel panel-success'><div class='panel-heading'><div class='pad margin no-print'>"+
                        "<div class='callout callout-success' style='margin-bottom: 0!important;'>"+
                        "<h4><i class='fa fa-info'></i> SUCCESS:</h4>This JOB HAS NO ERROR INPUT DUE TO EXCEPTIONS... </div>"+
                        "</div></div><div class='panel-body'>"
                } else {
                    contenu += "<fieldset><div class='panel panel-danger'><div class='panel-heading'><div class='pad margin no-print'>"+
                        "<div class='callout callout-danger' style='margin-bottom: 0!important;'>"+
                        "<h4><i class='fa fa-info'></i> ALERT :</h4>This JOB HAS ERROR INPUT... </div>"+
                        "</div></div>"
                    contenu += "<div class='panel-body'><div class='row'><div class='col-xs-12'>" +
                        "<table id='myInputErrors' width='100%' class='table .table-bordered'>" +
                        "<thead><tr><th><b>Line Number</b></th><th ><b>Line</b></th><th><b>Message</b></th></thead><tbody>";
                    for (var i = 0; i < data.inputError.length; i++) {
                        contenu += "<tr><td>" + data.inputError[i].lineNumber + "</td><td>" + data.inputError[i].line + "</td><td>" + data.inputError[i].messages + "</td></tr>";
                    }
                    contenu += "</tbody></table>" +
                        "</div></div>";
                }
                contenu += "<div class='row'><div class='col-xs-12'><table id='resume' width='100%' class='table .table-bordered'>" +
                    "<thead><tr><th><b>Job ID</b></th><th ><b>Start time</b></th><th><b>End time</b></th><th><b>Status </b></th><th><b>commit_count</b></th><th><b>read_count</b></th><th>write_count</th><th>process_skip_count</th><th>exit_code</th></thead><tbody>";
                contenu += "<tr><td>" + data.batchStepExecution.job_execution_id + "</td><td>" + moment(data.batchStepExecution.start_time).format("DD/MM HH:mm:ss") + "</td><td>" + moment(data.batchStepExecution.end_time).format("DD/MM HH:mm:ss") + "</td><td>" + data.batchStepExecution.status + "</td><td>" + data.batchStepExecution.commit_count + "</td><td>" + data.batchStepExecution.read_count + "</td><td>" + data.batchStepExecution.write_count + "</td><td>" + data.batchStepExecution.process_skip_count + "</td><td>" + data.batchStepExecution.exit_code + "</td></tr>";
                contenu += "</tbody></table>" +
                    "</div></div></div></div></fieldset>";

                $('#modal-body').append(contenu);
                $('#modal-success').modal('show');

            },
            error: function () { //erreur dans le cas les données ne sont pas envoyer on affiche un message qui indique l'erreur
                console.log("error");
            }
        });
    });


    $("#existeXml").on('click',function (e) {
        e.preventDefault();
        var classe = "fragmentRootName="+colSelected+"&";
        var data = classe.concat($('#form00').serialize());
        $.ajax({
            type: "POST",//la method à utiliser soit POST ou GET
            url: "/existXml", //lien de la servlet qui exerce le traitement sur les données
            data:data,
            dataType: 'json',
            xhr: function () {
                var xhr = new window.XMLHttpRequest();
                //Download progress
                xhr.addEventListener("progress", function (evt) {
                    console.log(evt.lengthComputable);
                    if (evt.lengthComputable) {
                        var percentComplete = evt.loaded / evt.total;
                    }
                }, false);
                return xhr;
            },
            beforeSend: function () {
                var progressbar = $( "#progressbar" ),
                    progressLabel = $( ".progress-label" );
                progressbar.progressbar({
                    value: false,
                });
                $('#pleaseWaitDialog').modal('show');
            },
            complete: function () {
                $('#pleaseWaitDialog').modal('hide');
            },
            success: function (data) {// le cas ou la requete est bien execute en reçoi les données serialiser par JSON dans la variable msg
                $("#contenu").html("");
                var contenu = "";
                console.log("data.length" + data.inputError.length);
                if ((data.inputError.length) == 0) {
                    contenu += "<fieldset><div class='panel panel-success'><div class='panel-heading'><div class='pad margin no-print'>"+
                        "<div class='callout callout-success' style='margin-bottom: 0!important;'>"+
                        "<h4><i class='fa fa-info'></i> SUCCESS:</h4>This JOB HAS NO ERROR INPUT DUE TO EXCEPTIONS... </div>"+
                        "</div></div><div class='panel-body'>"
                } else {
                    contenu += "<fieldset><div class='panel panel-danger'><div class='panel-heading'><div class='pad margin no-print'>"+
                        "<div class='callout callout-danger' style='margin-bottom: 0!important;'>"+
                        "<h4><i class='fa fa-info'></i> ALERT :</h4>This JOB HAS ERROR INPUT... </div>"+
                        "</div></div>"
                    contenu += "<div class='panel-body'><div class='row'><div class='col-xs-12'>" +
                        "<table id='myInputErrors' width='100%' class='table .table-bordered'>" +
                        "<thead><tr><th><b>Line Number</b></th><th ><b>Line</b></th><th><b>Message</b></th></thead><tbody>";
                    for (var i = 0; i < data.inputError.length; i++) {
                        contenu += "<tr><td>" + data.inputError[i].lineNumber + "</td><td>" + data.inputError[i].line + "</td><td>" + data.inputError[i].messages + "</td></tr>";
                    }
                    contenu += "</tbody></table>" +
                        "</div></div>";
                }
                contenu += "<div class='row'><div class='col-xs-12'><table id='resume' width='100%' class='table .table-bordered'>" +
                    "<thead><tr><th><b>Job ID</b></th><th ><b>Start time</b></th><th><b>End time</b></th><th><b>Status </b></th><th><b>commit_count</b></th><th><b>read_count</b></th><th>write_count</th><th>process_skip_count</th><th>exit_code</th></thead><tbody>";
                contenu += "<tr><td>" + data.batchStepExecution.job_execution_id + "</td><td>" + moment(data.batchStepExecution.start_time).format("DD/MM HH:mm:ss") + "</td><td>" + moment(data.batchStepExecution.end_time).format("DD/MM HH:mm:ss") + "</td><td>" + data.batchStepExecution.status + "</td><td>" + data.batchStepExecution.commit_count + "</td><td>" + data.batchStepExecution.read_count + "</td><td>" + data.batchStepExecution.write_count + "</td><td>" + data.batchStepExecution.process_skip_count + "</td><td>" + data.batchStepExecution.exit_code + "</td></tr>";
                contenu += "</tbody></table>" +
                    "</div></div></div></div></fieldset>";

                $('#modal-body').append(contenu);
                $('#modal-success').modal('show');

            },
            error: function () { //erreur dans le cas les données ne sont pas envoyer on affiche un message qui indique l'erreur
                console.log("error");
            }
        });
    });






    $('#latestTabId').on('click', '.btn-danger', function (e) {
        e.preventDefault();
        var table = $('#latestTabId').DataTable();
        table
            .row( $(this).parents('tr') )
            .remove()
            .draw();
        $.ajax({
            type:'GET',
            url:'/deleteClasse/'+$(this).val(),
            dataType:'json',
            sucess:function (data) {
                console.log("deleted");
            },
            error:function () {
                console.log("not deleted");
            }
        });
    });

    $('#latestTabId').on('click', '.attribute', function (e)
    {
        e.preventDefault();
        $.ajax({
            type: "GET",//la method à utiliser soit POST ou GET
            url: "/attributes/" + $(this).val(), //lien de la servlet qui exerce le traitement sur les données
            dataType: 'json',
            success: function (data) {// le cas ou la requete est bien execute en reçoi les données serialiser par JSON dans la variable msg
                //recuperation de la valeur stock dans l'attribut desactive
                $("#contenus").html("");
                var contenu ="";
                contenu += "<fieldset>" +
                    "<table id='infoClasse' width='100%' class='table .table-bordered'>" +
                    "<thead><tr><th><b> Attribute </b></th><th ><b>Classe Type</b></th></thead><tbody>";
                var type="";
                for (var i = 0; i < data.length; i++) {
                    if(data[i].type == "NUMBER"){
                        type = "INTEGER";
                    }else{
                        if(data[i].type == "VARCHAR2"){
                            type = "STRING";
                        }else{
                            if(data[i].type = "DATE"){
                                type = "DATE";
                            }else {
                                type = "Object";
                            }
                        }
                    }
                    contenu += "<tr><td>" + data[i].nameo + "</td><td>" + type + "</td></tr>";
                }
                contenu += "</tbody></table></fieldset>";
                $("#contenus").append(contenu);
                $("#Modalx").modal("show");

            },
            error: function () { //erreur dans le cas les données ne sont pas envoyer on affiche un message qui indique l'erreur
                console.log("error");
            }
        });
    });

    $("#showAllTables").on('click', function (e) {
        if (i == 0) {
            $('#allTables').dataTable({
                paging: false
            });
            i++;
        } else {
            if (i == 1) {
                $('#allTables').DataTable({
                    'fnClearTable': true,
                    "scrollY": "300px",
                    "scrollCollapse": true
                });
                i--;
            }

        }

    });

    $("#allJobs").on('click', function (e) {
        e.preventDefault();
        $.ajax({
            type: "GET",//la method à utiliser soit POST ou GET
            url: "/allMyJobs", //lien de la servlet qui exerce le traitement sur les données
            success: function (data) {// le cas ou la requete est bien execute en reçoi les données serialiser par JSON dans la variable msg
                //recuperation de la valeur stock dans l'attribut desactive
                $("#contenus").html("");
                var contenu = "<div class='panel panel-primary'><div class='panel-heading'>  ALL JOB Done</div>";
                if (data.length != 0) {
                    contenu += "<div class='panel-body'><fieldset>" +
                        "<table id='myjobs' width='100%' class='table .table-bordered'>" +
                        "<thead><tr><th><b>JOB ID</b></th><th ><b>Create time</b></th><th><b>Start time</b></th><th><b>End time</b></th><th><b>Status</b></th><th><b>Exit code</b></th><th>Exit message</th><th>Last Updated</th></thead><tbody>";
                    for (var i = 0; i < data.length; i++) {
                        contenu += "<tr><td>" + data[i].job_execution_id + "</td><td>" + moment(data[i].create_time).format("DD/MM HH:mm:ss") + "</td><td>" + moment(data[i].start_time).format("DD/MM HH:mm:ss") + "</td><td>" + moment(data[i].end_time).format("DD/MM HH:mm:ss") + "</td><td>" + data[i].status + "</td><td>" + data[i].exit_code + "</td><td>" + data[i].exit_message + "</td><td>" + moment(data[i].last_updated).format("DD/MM HH:mm:ss") + "</td></tr>";
                    }
                    contenu += "</tbody></table>" +
                        "</fieldset></div></div>";
                } else {
                    contenu = "We are waiting your first job :D ";
                }
                $("#contenus").append(contenu);
                $("#Modalx").modal("show");

            },
            error: function () { //erreur dans le cas les données ne sont pas envoyer on affiche un message qui indique l'erreur
                console.log("error");
            }
        });
    });

    $("#allMyJobCompleted").on('click', function (e) {
        e.preventDefault();
        $.ajax({
            type: "GET",//la method à utiliser soit POST ou GET
            url: "/allMyJobCompleted", //lien de la servlet qui exerce le traitement sur les données
            success: function (data) {// le cas ou la requete est bien execute en reçoi les données serialiser par JSON dans la variable msg
                //recuperation de la valeur stock dans l'attribut desactive
                $("#contenus").html("");
                var contenu = "";
                if (data.length != 0) {
                    contenu += "<div class='panel panel-success'><div class='panel-heading'>  ALL JOB Done</div><div class='panel-body'><fieldset>" +
                        "<table id='myjobs' width='100%' class='table .table-bordered'>" +
                        "<thead><tr><th><b>JOB ID</b></th><th ><b>Create time</b></th><th><b>Start time</b></th><th><b>End time</b></th><th><b>Status</b></th><th><b>Exit code</b></th><th>Exit message</th><th>Last Updated</th></thead><tbody>";
                    for (var i = 0; i < data.length; i++) {
                        contenu += "<tr><td>" + data[i].job_execution_id + "</td><td>" + moment(data[i].create_time).format("DD/MM HH:mm:ss") + "</td><td>" + moment(data[i].start_time).format("DD/MM HH:mm:ss") + "</td><td>" + moment(data[i].end_time).format("DD/MM HH:mm:ss") + "</td><td>" + data[i].status + "</td><td>" + data[i].exit_code + "</td><td>" + data[i].exit_message + "</td><td>" + moment(data[i].last_updated).format("DD/MM HH:mm:ss") + "</td></tr>";
                    }
                    contenu += "</tbody></table>" +
                        "</fieldset></div></div>";
                } else {
                    contenu = "No Job Completed for you so we suggest to do your first job :)";
                }
                $("#contenus").append(contenu);
                $("#Modalx").modal("show");

            },
            error: function () { //erreur dans le cas les données ne sont pas envoyer on affiche un message qui indique l'erreur
                console.log("error");
            }
        });

    });

    

    $("#allMyJobFailed").on('click', function (e) {
        e.preventDefault();
        $.ajax({
            type: "GET",//la method à utiliser soit POST ou GET
            url: "/allMyJobFailed", //lien de la servlet qui exerce le traitement sur les données
            success: function (data) {// le cas ou la requete est bien execute en reçoi les données serialiser par JSON dans la variable msg
                //recuperation de la valeur stock dans l'attribut desactive
                $("#contenus").html("");
                var contenu = "";
                if (data.length != 0) {
                    contenu += "<div class='panel panel-danger'><div class='panel-heading'>  ALL JOBS Failed</div><div class='panel-body'><fieldset>" +
                        "<table id='myjobs' width='100%' class='table .table-bordered'>" +
                        "<thead><tr><th><b>JOB ID</b></th><th ><b>Create time</b></th><th><b>Start time</b></th><th><b>End time</b></th><th><b>Status</b></th><th><b>Exit code</b></th><th>Exit message</th><th>Last Updated</th></thead><tbody>";
                    for (var i = 0; i < data.length; i++) {
                        contenu += "<tr><td>" + data[i].job_execution_id + "</td><td>" + moment(data[i].create_time).format("DD/MM HH:mm:ss") + "</td><td>" + moment(data[i].start_time).format("DD/MM HH:mm:ss") + "</td><td>" + moment(data[i].end_time).format("DD/MM HH:mm:ss") + "</td><td>" + data[i].status + "</td><td>" + data[i].exit_code + "</td><td>" + data[i].exit_message + "</td><td>" + moment(data[i].last_updated).format("DD/MM HH:mm:ss") + "</td></tr>";
                    }
                    contenu += "</tbody></table>" +
                        "</fieldset></div></div>";
                } else {
                    var contenu = "No Job with status Failed for you :D ";
                }
                $("#contenus").append(contenu);
                $("#Modalx").modal("show");
            },
            error: function () { //erreur dans le cas les données ne sont pas envoyer on affiche un message qui indique l'erreur
                console.log("error");
            }
        });

    });

    $("#allMyJobAbondonned").on('click', function (e) {
        e.preventDefault();
        $.ajax({
            type: "GET",//la method à utiliser soit POST ou GET
            url: "/allMyJobAbondonned", //lien de la servlet qui exerce le traitement sur les données
            success: function (data) {// le cas ou la requete est bien execute en reçoi les données serialiser par JSON dans la variable msg
                //recuperation de la valeur stock dans l'attribut desactive
                $("#contenus").html("");
                var contenu = "";
                if (data.length != 0) {
                    contenu += "<div class='panel panel-warning'><div class='panel-heading'>  ALL JOBS Failed</div><div class='panel-body'><fieldset>" +
                        "<table id='myjobs' width='100%' class='table .table-bordered'>" +
                        "<thead><tr><th><b>JOB ID</b></th><th ><b>Create time</b></th><th><b>Start time</b></th><th><b>End time</b></th><th><b>Status</b></th><th><b>Exit code</b></th><th>Exit message</th><th>Last Updated</th></thead><tbody>";
                    for (var i = 0; i < data.length; i++) {
                        contenu += "<tr><td>" + data[i].job_execution_id + "</td><td>" + moment(data[i].create_time).format("DD/MM HH:mm:ss") + "</td><td>" + moment(data[i].start_time).format("DD/MM HH:mm:ss") + "</td><td>" + moment(data[i].end_time).format("DD/MM HH:mm:ss") + "</td><td>" + data[i].status + "</td><td>" + data[i].exit_code + "</td><td>" + data[i].exit_message + "</td><td>" + moment(data[i].last_updated).format("DD/MM HH:mm:ss") + "</td></tr>";
                    }
                    contenu += "</tbody></table>" +
                        "</fieldset></div></div>";
                } else {
                    contenu = "NO "
                }
                $("#contenus").append(contenu);
                $("#Modalx").modal("show");
            },

            error: function () { //erreur dans le cas les données ne sont pas envoyer on affiche un message qui indique l'erreur
                console.log("error");
            }
        });

    });


    });



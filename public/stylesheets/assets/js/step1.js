$(document).ready(function() {
    var allTables;
    var i = 0;

    function loger() {
        console.log("ok");
    }

    $.ajax({
        type: "GET",//la method à utiliser soit POST ou GET
        url: "/classes", //lien de la servlet qui exerce le traitement sur les données
        dataType: 'json',
        success: function (data) {// le cas ou la requete est bien execute en reçoi les données serialiser par JSON dans la variable msg
            //recuperation de la valeur stock dans l'attribut desactive
            for (var i = 0; i < data.length; i++) {
                $("#latestTab").append("<tr>" +
                    "<td>" + data[i].className + "</td>" +
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

    $('#latestTabId').on('click', '.attribute', function (e) {
        e.preventDefault();
        $.ajax({
            type: "GET",//la method à utiliser soit POST ou GET
            url: "/attributes/" + $(this).val(), //lien de la servlet qui exerce le traitement sur les données
            success: function (data) {// le cas ou la requete est bien execute en reçoi les données serialiser par JSON dans la variable msg
                //recuperation de la valeur stock dans l'attribut desactive
                $("#contenu").html("");
                var contenu = "<div class='panel panel-primary'> <div class='panel-heading'> Columns  with types of Table = ( '"+$(this).val()+"' )</div>";
                contenu += "<div class='panel-body'><fieldset>" +
                    "<table id='infoClasse' width='100%' class='table .table-bordered'>" +
                    "<thead><tr><th><b>Colonne</b></th><th ><b>Type</b></th><th><b>Size</b></th><th><b>Primary Key</b></th><th><b>Non Null</b></th></thead><tbody>";
                for (var i = 0; i < data.length; i++) {
                    contenu += "<tr><td>" + data[i].nameo + "</td><td>" + data[i].type + "</td><td>" + data[i].sizeo + "</td><td>" + data[i].pko + "</td><td>" + data[i].nonNull + "</td></tr>";
                }
                contenu += "</tbody></table>" +
                    "</fieldset></div></div>";
                $("#contenu").append(contenu);
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
                $("#contenu").html("");
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
                $("#contenu").append(contenu);
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
                $("#contenu").html("");
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
                $("#contenu").append(contenu);
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
                $("#contenu").html("");
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
                $("#contenu").append(contenu);
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
                $("#contenu").html("");
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
                $("#contenu").append(contenu);
                $("#Modalx").modal("show");
            },

            error: function () { //erreur dans le cas les données ne sont pas envoyer on affiche un message qui indique l'erreur
                console.log("error");
            }
        });

    });
});



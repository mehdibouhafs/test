$(document).ready(function() {
    var allTables;
    var i = 0;
    var my_delay = 10000;
    // call your ajax function when the document is ready...
    $(function() {
        callAjax();
    });



    var datetime = $('#datetimepicker1').datetimepicker();

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
        url: "/readers", //lien de la servlet qui exerce le traitement sur les données
        dataType: 'json',
        success: function (data) {// le cas ou la requete est bien execute en reçoi les données serialiser par JSON dans la variable msg
            //recuperation de la valeur stock dans l'attribut desactive
            var executedBy;
            var executed;
            var resultat;
            var lancement;
            var path;
            console.log("data[0] "  +data[0]);
            for (var i = 0; i < data.length; i++) {
                if (data[i].executed_by == null) {
                    executedBy = "<span class='label label-danger'> Not Executed yet </span>";
                } else {
                    executedBy = "<span class='label label-success'>" + data[i].executed_by + "</span>";
                }
                if (data[i].executed == false) {
                    executed = "<span class='label label-danger'> Not Executed yet </span>";
                } else {
                    executed = "<span class='label label-success'> True </span>";
                }
                if (data[i].resultat == false) {
                    resultat = "<span class='label label-danger'> Not disponible </span>";
                } else {
                    resultat = "<span class='label label-success'>Success</span>";
                }
                if (data[i].dateLancement == null) {
                    lancement = "<span class='label label-danger'> Pas encore lancer </span>";
                } else {
                    lancement = "<span class='label label-success'> " + moment(data[i].dateLancement).format("DD/MM/YYYY HH:mm:ss") + " </span>";
                }
                if (data[i].filePath.split("/")[data[i].filePath.split("/").length - 1].length > 20) {
                    path = data[i].filePath.split("\\")[data[i].filePath.split("\\").length - 1];
                } else {
                    path = data[i].filePath.split("/")[data[i].filePath.split("/").length - 1];
                }
                if (data[i].executed == true) {
                    $("#latestTab").append("<tr  class='success' id='" + i + "' value='" + data[i].id + "'>" +
                        "<td align='center'><span class='label label-default'>" + moment(data[i].dateCreation).format("DD/MM/YYYY HH:mm:ss") + "</span></td>" +
                        "<td align='center'><span class='label label-default'>" + path + "</span></td>" +
                        "<td align='center'><span class='label label-default'>" + data[i].classeName + "</span></td>" +
                        "<td align='center'>" + lancement + "</td>" +
                        "<td align='center'>" + executedBy.split("@")[0] + "</td>" +
                        "<td align='center'>" + resultat + "</td>" +
                        "<td align='center'><span class='label label-default'>" + data[i].nbLinesSuccess + "</span></td>" +
                        "<td align='center'> <span class='label label-default'>" + data[i].nbLinesFailed + "</span></td>" +
                        "<td align='center'><button  class='btn btn-success' value='" + data[i].id + "'><span class='glyphicon glyphicon glyphicon-play'></span></button></td>" +
                        "<td align='center'><button  class='time' style='background-color: #00c0ef;' value='" + data[i].classeName + "'><span class='glyphicon glyphicon glyphicon-time'></span></button></td>" +
                        "<td align='center'><button  class='btn btn-danger'  value='" + data[i].id + "'><span class='glyphicon glyphicon-remove-sign'></span></button></td>" +
                        "<td align='center'><button  class='btn btn-primary' value='" + data[i].classeName + "'><span class='glyphicon glyphicon-eye-open'></span></button></td></tr>");
                } else {
                    $("#latestTab").append("<tr  id='" + i + "' value='" + data[i].id + "'>" +
                        "<td align='center'><span class='label label-default'>" + moment(data[i].dateCreation).format("DD/MM/YYYY HH:mm:ss") + "</span></td>" +
                        "<td align='center'><span class='label label-default'>" + path + "</span></td>" +
                        "<td align='center'><span class='label label-default'>" + data[i].classeName + "</span></td>" +
                        "<td align='center'>" + lancement + "</td>" +
                        "<td align='center'>" + executedBy.split("@")[0] + "</td>" +
                        "<td align='center'>" + resultat + "</td>" +
                        "<td align='center'><span class='label label-default'> " + data[i].nbLinesSuccess + "</span></td>" +
                        "<td align='center'><span class='label label-default'>" + data[i].nbLinesFailed + "</span></td>" +
                        "<td align='center'><button  class='btn btn-success' value='" + data[i].id + "'><span class='glyphicon glyphicon glyphicon-play'></span></button></td>" +
                        "<td align='center'><button  class='time' style='background-color: #00c0ef;' value='" + data[i].classeName + "'><span class='glyphicon glyphicon glyphicon-time'></span></button></td>" +
                        "<td align='center'><button  class='btn btn-danger' value='" + data[i].id + "'><span class='glyphicon glyphicon-remove-sign'></span></button></td>" +
                        "<td align='center'><button  class='btn btn-primary' value='" + data[i].classeName + "'><span class='glyphicon glyphicon-eye-open'></span></button></td></tr>");
                }
            }

            allTables = $('#latestTabId').DataTable({
                'fnClearTable': true,
                "order": [[ 0, "desc" ]],
                "scrollY":        "500px",
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


    /*var colSelected1;
    $('#latestTabId1 tbody').on( 'click', 'tr', function () {
        if ( $(this).hasClass('selected') ) {
            $(this).removeClass('selected');
        }
        else {
            allTables.$('tr.selected').removeClass('selected');
            $(this).addClass('selected');
            colSelected= $(this).attr('value');
        }
    });*/


    $('#latestTabId').on('click', '.btn-danger', function (e) {
        e.preventDefault();
        var table = $('#latestTabId').DataTable();
        table
            .row( $(this).parents('tr') )
            .remove()
            .draw();
        $.ajax({
            type:'GET',
            url:'/deleteReader/'+$(this).val(),
            dataType:'json',
            sucess:function (data) {
                console.log("deleted");
            },
            error:function () {
                console.log("not deleted");
            }
        });
    });



    $('#latestTabId').on('click', '.time', function (e) {
        e.preventDefault();
        //var table = $('#latestTabId').DataTable();
        console.log('show mod');
        $("#program").html("");
        $("#program").append('<button class="programation" name="'+$(this).val()+'" type="button"  >Valider</button>');
        $("#myModalxya").modal().show();

    });


    $(document).on("click", ".programation", function(event){
        event.preventDefault();
        $("#myModalxya").modal("hide");
        console.log("lol "+  $(this).attr("name"));
        console.log("date1 "+ $("#datetimepicker1").data("DateTimePicker").date());
        var data2 = "classe="+$(this).attr("name")+"&";
        var data3 = "date="+$("#datetimepicker1").data("DateTimePicker").date();
        var data = data2.concat(data3);
        $.ajax({
            type:'POST',
            data:data,
            url:'/programJob',
            sucess:function (data) {

                console.log("Job Programmed with success");
            },
            error:function () {
                console.log("not deleted");
            }
        });



       // console.log("date2 "+$("#datetimepicker1").find("input").val());



    });





    $(document).on("click", "#btn-danger", function(event){
        event.preventDefault();
        var table = $('#latestTabId').DataTable();
        table
            .row( $(this).parents('tr') )
            .remove()
            .draw();
        $.ajax({
            type:'GET',
            url:'/deleteReader/'+$(this).val(),
            dataType:'json',
            sucess:function (data) {
                console.log("deleted");
            },
            error:function () {
                console.log("not deleted");
            }
        });
    });



    $('#latestTabId').on('click', '.btn-primary', function (e) {
        e.preventDefault();
        var table = $('#latestTabId').DataTable();
        $.ajax({
            type:'GET',
            url:'/resume/'+$(this).val(),
            dataType:'json',
                success: function (data) {// le cas ou la requete est bien execute en reçoi les données serialiser par JSON dans la variable msg
                    //recuperation de la valeur stock dans l'attribut desactive
                    var contenu = '<div class="container-fluid"><div class="row"> <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12"> <div class="panel panel-primary"> <div class="panel-heading"><h2>Configuration Fichier</h2></div>'+
                    '<div class="panel-body"><div class="row">'+
                        '<table class="table table-bordered table-hover" id="table_resume">'+
                        '<thead> <tr> <th width="52%">File Path</th> <th width="1%%">Type</th> <th width="1%">Séparateur</th> <th width="8%">Nombre de ligne skipped</th> <th width="30%"> header</th> <th width="10%">Date de création</th> </tr> </thead><tbody>'+
                    '<tr><td>'+data.reader.filePath+'</td> <td>'+data.reader.filePath.split(".")[1]+'</td> <td>'+data.reader.separator+'</td><td>'+data.reader.nbLineToSkip+'</td> <td>'+data.reader.columns+'</td> <td>'+moment(data.reader.dateCreation).format("DD/MM/YYYY HH:mm:ss")+'</td></tr></tbody> </table></div> </div> </div> </div> </div>';
                    contenu +='<div class="row"><div class="col-xs-12 col-sm-12 col-md-12 col-lg-12"> <div class="panel panel-primary"> <div class="panel-heading"> <h2>Table configuration </h2></div> <div class="panel-body">'+
                                '<table id="table_auto" class="table table-striped" cellspacing="0" width="100%"> <thead> <tr> <th width="2%">Primary key</th> <th width="15%">column</th> <th width="15%">Type</th> <th width="10%">Size</th> <th width="2%"> Not null</th> <th width="20%">Default</th> <th width="36%">Commentaire</th> </tr> </thead><tbody>';
                   console.log("length "+ data.attributes[0].pko);
                    for(var i = 0;i<data.attributes.length;i++) {
                        var qq,ss,object,sizeo,defaut,commentaires;
                        if(data.attributes[i].pko == true){
                            console.log("TRUE");
                            qq= "<input type='checkbox'  checked='checked' disabled>";
                        }else{
                            console.log("FALSEE");
                            qq = "<input type='checkbox'  disabled>";
                        }
                        if(data.attributes[i].nonNull == true){
                            ss= "<input type='checkbox'  checked='checked' disabled>";
                        }else{
                            ss = "<input type='checkbox'  disabled>";
                        }
                        if(data.attributes[i].type == "object"){
                            object = "Non Sélectionné";
                        }else{
                            object = data.attributes[i].type;
                        }
                        if(data.attributes[i].sizeo == null){
                            sizeo="";
                        }else{
                            sizeo = data.attributes[i].sizeo;
                        }
                        if(data.attributes[i].defaut == null){
                            defaut = "";
                        }else{
                            defaut=data.attributes[i].defaut;
                        }
                        if(data.attributes[i].commentaires==null){
                            commentaires = "";
                        }else{
                            commentaires = data.attributes[i].commentaires;
                        }
                        contenu += '<tr> <td>'+qq+'</td><td>'+data.attributes[i].nameo+'</td><td>'+object+'</td><td>'+sizeo+'</td><td>'+ss+'</td><td>'+defaut+'</td><td>'+commentaires+'</td></tr>';
                    }

                    if(data.reader.executed == true) {
                    contenu+='</tbody></table> </div> </div> </div></div><div class="row"> <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12"><div class="panel panel-primary"> <div class="panel-heading"> <h2>Batch Report</h2></div> <div class="panel-body">'+
                        '<table class="table table-bordered table-hover" id="table_auto"><thead> <tr> <th width="2%">Job Id</th> <th width="4%">Type de Job</th> <th width="10%">Start time</th> <th width="10%">End time </th> <th width="10%">status</th> <th width="10%">Read count</th> <th width="10%">Filter count</th> <th width="10%">Write count</th> <th width="10%">Read skip count</th> <th width="10%">Write skip count</th> <th width="10%">Process skip count</th> <th width="4%">Rollback count</th> </tr> </thead>'+
                    '<tbody> <tr> <td align="center">'+data.batchStepExecution.job_execution_id+'</td> <td align="center">'+data.batchStepExecution.step_name+'</td> <td align="center">'+moment(data.batchStepExecution.start_time).format("DD/MM/YYYY HH:mm:ss")+'</td> <td align="center">'+moment(data.batchStepExecution.end_time).format("DD/MM/YYYY HH:mm:ss")+'</td> <td align="center">'+data.batchStepExecution.status+'</td> <td>'+data.batchStepExecution.read_count+'</td> <td align="center">'+data.batchStepExecution.filter_count+'</td> <td align="center">'+data.batchStepExecution.write_count+'</td> <td align="center">'+data.batchStepExecution.read_skip_count+'</td> <td align="center">'+data.batchStepExecution.write_skip_count+'</td> <td align="center">'+data.batchStepExecution.process_skip_count+'</td> <td align="center">'+data.batchStepExecution.rollback_count+'</td> </tr> </tbody> </table> </div> </div> </div> </div>';

                        if (data.inputError.length > 0) {
                            contenu += '<div class="row"> <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12"><div class="panel panel-danger"> <div class="panel-heading"><h2> Error input : </h2></div> <div class="panel-body"> <table class="table table-bordered table-hover" id="table_auto"> <thead> <tr> <th width="5%">Line Number</th> <th width="35%">Line</th> <th width="60%">Messages</th> </tr> </thead><tbody>';

                            for (var jj = 0; jj < data.inputError.length; jj++) {
                                contenu += '<tr> <td>' + data.inputError[jj].lineNumber + '</td><td>' + data.inputError[jj].line + '</td> <td>' + data.inputError[jj].messages + '</td> </tr>';
                            }
                            contenu += '</tbody> </table> </div> </div> </div> </div></div></div>';
                        } else {
                            contenu += '<div class="row"> <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12"><div class="panel panel-success"> <div class="panel-heading"><h2> Success : </h2></div> <div class="panel-body"> Toutes les lines ont été ajoutés</div></div>';
                        }
                    }
                    $("#resume").html("");
                    $('#resume').append(contenu);
                    $("#myModalx").modal().show();
                    
                },
                error: function () { //erreur dans le cas les données ne sont pas envoyer on affiche un message qui indique l'erreur
                    console.log("error");
                }
            });
    });



    $('#latestTabId1').on('click', '.btn-primary', function (e) {
        console.log("lol");
        e.preventDefault();
        $.ajax({
            type:'GET',
            url:'/resume/'+$(this).val(),
            dataType:'json',
            success: function (data) {// le cas ou la requete est bien execute en reçoi les données serialiser par JSON dans la variable msg
                //recuperation de la valeur stock dans l'attribut desactive
                var contenu = '<div class="container-fluid"><div class="row"> <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12"> <div class="panel panel-primary"> <div class="panel-heading"><h2>Configuration Fichier</h2></div>'+
                    '<div class="panel-body"><div class="row">'+
                    '<table class="table table-bordered table-hover" id="table_resume">'+
                    '<thead> <tr> <th width="52%">File Path</th> <th width="1%%">Type</th> <th width="1%">Séparateur</th> <th width="8%">Nombre de ligne skipped</th> <th width="30%"> header</th> <th width="10%">Date de création</th> </tr> </thead><tbody>'+
                    '<tr><td>'+data.reader.filePath+'</td> <td>'+data.reader.filePath.split(".")[1]+'</td> <td>'+data.reader.separator+'</td><td>'+data.reader.nbLineToSkip+'</td> <td>'+data.reader.columns+'</td> <td>'+moment(data.reader.dateCreation).format("DD/MM/YYYY HH:mm:ss")+'</td></tr></tbody> </table></div> </div> </div> </div> </div>';
                contenu +='<div class="row"><div class="col-xs-12 col-sm-12 col-md-12 col-lg-12"> <div class="panel panel-primary"> <div class="panel-heading"> <h2>Table configuration </h2></div> <div class="panel-body">'+
                    '<table  id="table_auto1" class="table table-striped" cellspacing="0" width="100%"> <thead> <tr> <th width="2%">Primary key</th> <th width="15%">column</th> <th width="15%">Type</th> <th width="10%">Size</th> <th width="2%"> Not null</th> <th width="20%">Default</th> <th width="36%">Commentaire</th> </tr> </thead><tbody>';
                console.log("length "+ data.attributes[0].pko);
                for(var i = 0;i<data.attributes.length;i++) {
                    var qq,ss,object,sizeo,defaut,commentaires;
                    if(data.attributes[i].pko == true){
                        console.log("TRUE");
                        qq= "<input type='checkbox'  checked='checked' disabled>";
                    }else{
                        console.log("FALSEE");
                        qq = "<input type='checkbox'  disabled>";
                    }
                    if(data.attributes[i].nonNull == true){
                        ss= "<input type='checkbox'  checked='checked' disabled>";
                    }else{
                        ss = "<input type='checkbox'  disabled>";
                    }
                    if(data.attributes[i].type == "object"){
                        object = "Non Sélectionné";
                    }else{
                        object = data.attributes[i].type;
                    }
                    if(data.attributes[i].sizeo == null){
                        sizeo="";
                    }else{
                        sizeo = data.attributes[i].sizeo;
                    }
                    if(data.attributes[i].defaut == null){
                        defaut = "";
                    }else{
                        defaut=data.attributes[i].defaut;
                    }
                    if(data.attributes[i].commentaires==null){
                        commentaires = "";
                    }else{
                        commentaires = data.attributes[i].commentaires;
                    }
                    contenu += '<tr> <td align="center">'+qq+'</td><td align="center">'+data.attributes[i].nameo+'</td><td align="center">'+object+'</td><td align="center">'+sizeo+'</td><td align="center">'+ss+'</td><td align="center">'+defaut+'</td><td align="center">'+commentaires+'</td></tr>';
                }

                if(data.reader.executed == true) {
                contenu+='</tbody></table> </div> </div> </div></div><div class="row"> <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12"><div class="panel panel-primary"> <div class="panel-heading"> <h2>Batch Report</h2></div> <div class="panel-body">'+
                    '<table class="table table-bordered table-hover" id="table_auto"><thead> <tr> <th width="2%">Job Id</th> <th width="4%">Type de Job</th> <th width="10%">Start time</th> <th width="10%">End time </th> <th width="10%">status</th> <th width="10%">Read count</th> <th width="10%">Filter count</th> <th width="10%">Write count</th> <th width="10%">Read skip count</th> <th width="10%">Write skip count</th> <th width="10%">Process skip count</th> <th width="4%">Rollback count</th> </tr> </thead>'+
                    '<tbody> <tr> <td>'+data.batchStepExecution.job_execution_id+'</td> <td>'+data.batchStepExecution.step_name+'</td> <td>'+moment(data.batchStepExecution.start_time).format("DD/MM/YYYY HH:mm:ss")+'</td> <td>'+moment(data.batchStepExecution.end_time).format("DD/MM/YYYY HH:mm:ss")+'</td> <td>'+data.batchStepExecution.status+'</td> <td>'+data.batchStepExecution.read_count+'</td> <td>'+data.batchStepExecution.filter_count+'</td> <td>'+data.batchStepExecution.write_count+'</td> <td>'+data.batchStepExecution.read_skip_count+'</td> <td>'+data.batchStepExecution.write_skip_count+'</td> <td>'+data.batchStepExecution.process_skip_count+'</td> <td>'+data.batchStepExecution.rollback_count+'</td> </tr> </tbody> </table> </div> </div> </div> </div>';



                    if (data.inputError.length > 0) {
                        contenu += '<div class="row"> <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12"><div class="panel panel-danger"> <div class="panel-heading"><h2> Error input : </h2></div> <div class="panel-body"> <table class="table table-bordered table-hover" id="table_auto"> <thead> <tr> <th width="5%">Line Number</th> <th width="35%">Line</th> <th width="60%">Messages</th> </tr> </thead><tbody>';

                        for (var jj = 0; jj < data.inputError.length; jj++) {
                            contenu += '<tr> <td align="center">' + data.inputError[jj].lineNumber + '</td><td align="center">' + data.inputError[jj].line + '</td> <td>' + data.inputError[jj].messages + '</td> </tr>';
                        }
                        contenu += '</tbody> </table> </div> </div> </div> </div></div></div>';
                    } else {
                        contenu += '<div class="row"> <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12"><div class="panel panel-success"> <div class="panel-heading"><h2> Success : </h2></div> <div class="panel-body"> Toutes les lines ont été ajoutés</div></div>';
                    }
                }
                $("#resume").html("");
                $('#resume').append(contenu);
                $("#myModalx").modal().show();

            },
            error: function () { //erreur dans le cas les données ne sont pas envoyer on affiche un message qui indique l'erreur
                console.log("error");
            }
        });
    });

    $("#allJobs").on('click', function (e) {
        e.preventDefault();
        $.ajax({
            type: "GET",//la method à utiliser soit POST ou GET
            url: "/allMyJobs", //lien de la servlet qui exerce le traitement sur les données
            success: function (data) {// le cas ou la requete est bien execute en reçoi les données serialiser par JSON dans la variable msg
                //recuperation de la valeur stock dans l'attribut desactive
                var contenu = '<div class="container-fluid"><div class="row"> <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12"> <div class="panel panel-primary"> <div class="panel-heading"><h2>Configuration Fichier</h2></div>'+
                    '<div class="panel-body"><table id="table_resume" class="table table-striped" cellspacing="0" width="100%">'+
                '<thead> <tr> <th width="2%">Job Id</th><th width="1%">Date Création</th><th width="1%">Created by</th> <th width="8%">File</th> <th width="30%"> Nb line skiped</th> <th width="10%">Table</th> <th width="10%">Date Lancement</th><th width="10%">Exectued</th><th width="10%">Resultat</th><th width="10%">Nombre de lines ajouter</th><th width="10%">Nombre de lines ajouter</th><th width="10%"> Nombre de lines Non ajouter</th><th width="10%"> Nombre de lines Non ajouter</th> <th width="">More Détail</th></tr></thead><tbody>';
                var executedBy;
                var executed;
                var resultat;
                var lancement;
                var path;
                for (var i = 0; i < data.length; i++) {
                    if(data[i].executed_by == null){
                        executedBy = "<span class='label label-danger'> Not Executed yet </span>";
                    }else{
                        executedBy = "<span class='label label-success'>"+data[i].executed_by+"</span>";
                    }
                    if(data[i].executed  == false){
                        executed = "<span class='label label-danger'> Not Executed yet </span>";
                    }else{
                        executed = "<span class='label label-success'> True </span>";
                    }
                    if(data[i].resultat   == false){
                        resultat = "<span class='label label-danger'> Not disponible </span>";
                    }else{
                        resultat = "<span class='label label-success'>Success</span>";
                    }
                    if(data[i].dateLancement == null){
                        lancement = "<span class='label label-danger'> Pas encore lancer </span>";
                    }else{
                        lancement = "<span class='label label-success'> "+moment(data[i].dateLancement).format("DD/MM/YYYY HH:mm:ss") +" </span>";
                    }
                    if(data[i].filePath.split("/")[data[i].filePath.split("/").length-1].length > 20){
                        path = data[i].filePath.split("\\")[data[i].filePath.split("\\").length-1];
                    }else{
                        path = data[i].filePath.split("/")[data[i].filePath.split("/").length-1];
                    }
                    contenu += "<tr id='"+i+"' value='"+data[i].id+"'>" +
                        "<td align='center'>" + i + "</td>" +
                        "<td align='center'><span class='label label-info'>" + moment(data[i].dateCreation).format("DD/MM/YYYY HH:mm:ss") + "</span></td>" +
                        "<td align='center'>" + data[i].emailUser.split("@")[0] + "</td>" +
                        "<td align='center'>" + path+ "</td>" +
                        "<td align='center'>" + data[i].nbLineToSkip + "</td>" +
                        "<td align='center'>" + data[i].classeName + "</td>" +
                        "<td align='center'>" + lancement+ "</td>" +
                        "<td align='center'>" + executed+ "</td>" +
                        "<td align='center'>" + resultat+ "</td>" +
                        "<td align='center'> "+ data[i].nbLinesSuccess+"</td>"+
                        "<td align='center'> "+ data[i].nbLinesFailed+"</td>"+
                        "<td align='center'><button id='btn-success' class='btn btn-success' value='" + data[i].id + "'><span class='glyphicon glyphicon glyphicon-play'></span></button></td>" +
                        "<td align='center'><button id='btn-danger' class='btn btn-danger' value='" + data[i].id + "'><span class='glyphicon glyphicon-remove-sign'></span></button></td>" +
                        "<td align='center'><button  id='submit_btn' class='btn btn-primary' value='" + data[i].classeName+ "'><span class='glyphicon glyphicon-eye-open'></span></button></td></tr>";
                }
                contenu += "</tbody></<table></div></div>";
                $("#resume").html("");
                $('#resume').append(contenu);
                $("#myModalx").modal().show();
                allTables = $('#table_resume').DataTable({
                    "order": [[ 1, "desc" ]],
                    'fnClearTable': true,
                    "scrollY": "500px",
                    "scrollCollapse": true
                });
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
                var contenu = '<div class="container-fluid"><div class="row"> <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12"> <div class="panel panel-primary"> <div class="panel-heading"><h2>Configuration Fichier</h2></div>'+
                    '<div class="panel-body"><table id="table_resume" class="table table-striped" cellspacing="0" width="100%">'+
                    '<thead> <tr> <th width="2%">Job Id</th><th width="1%%">Date Création</th><th width="1%">Created by</th> <th width="8%">File</th> <th width="30%"> Nb line skiped</th> <th width="10%">Table</th> <th width="10%">Date Lancement</th><th width="10%">Exectued</th><th width="10%">Resultat</th><th width="10%">Nombre de lines ajouter</th><th width="10%">Nombre de lines ajouter</th><th width="10%"> Nombre de lines Non ajouter</th><th width="10%"> Nombre de lines Non ajouter</th> <th width="">More Détail</th></tr></thead><tbody>';
                var executedBy;
                var executed;
                var resultat;
                var lancement;
                var path;
                for (var i = 0; i < data.length; i++) {
                    if(data[i].executed_by == null){
                        executedBy = "<span class='label label-danger'> Not Executed yet </span>";
                    }else{
                        executedBy = "<span class='label label-success'>"+data[i].executed_by+"</span>";
                    }
                    if(data[i].executed  == false){
                        executed = "<span class='label label-danger'> Not Executed yet </span>";
                    }else{
                        executed = "<span class='label label-success'> True </span>";
                    }
                    if(data[i].resultat   == false){
                        resultat = "<span class='label label-danger'> Not disponible </span>";
                    }else{
                        resultat = "<span class='label label-success'>Success</span>";
                    }
                    if(data[i].dateLancement == null){
                        lancement = "<span class='label label-danger'> Pas encore lancer </span>";
                    }else{
                        lancement = "<span class='label label-success'> "+moment(data[i].dateLancement).format("DD/MM/YYYY HH:mm:ss") +" </span>";
                    }
                    if(data[i].filePath.split("/")[data[i].filePath.split("/").length-1].length > 20){
                        path = data[i].filePath.split("\\")[data[i].filePath.split("\\").length-1];
                    }else{
                        path = data[i].filePath.split("/")[data[i].filePath.split("/").length-1];
                    }
                    contenu += "<tr id='"+i+"' value='"+data[i].id+"'>" +
                        "<td align='center'>" + i + "</td>" +
                        "<td align='center'><span class='label label-info'>" + moment(data[i].dateCreation).format("DD/MM/YYYY HH:mm:ss") + "</span></td>" +
                        "<td align='center'>" + data[i].emailUser.split("@")[0] + "</td>" +
                        "<td align='center'>" + path+ "</td>" +
                        "<td align='center'>" + data[i].nbLineToSkip + "</td>" +
                        "<td align='center'>" + data[i].classeName + "</td>" +
                        "<td align='center'>" + lancement+ "</td>" +
                        "<td align='center'>" + executed+ "</td>" +
                        "<td align='center'>" + resultat+ "</td>" +
                        "<td align='center'> "+ data[i].nbLinesSuccess+"</td>"+
                        "<td align='center'> "+ data[i].nbLinesFailed+"</td>"+
                        "<td align='center' ><button id='btn-success' class='btn btn-success' value='" + data[i].id + "'><span class='glyphicon glyphicon glyphicon-play'></span></button></td>" +
                        "<td align='center'><button id='btn-danger' class='btn btn-danger' value='" + data[i].id + "'><span class='glyphicon glyphicon-remove-sign'></span></button></td>" +
                        "<td align='center'><button  id='submit_btn' class='btn btn-primary' value='" + data[i].classeName+ "'><span class='glyphicon glyphicon-eye-open'></span></button></td></tr>";
                }
                contenu += "</tbody></<table></div></div>";
                $("#resume").html("");
                $('#resume').append(contenu);
                $("#myModalx").modal().show();
                allTables = $('#table_resume').DataTable({
                    "order": [[ 1, "desc" ]],
                    'fnClearTable': true,
                    "scrollY": "500px",
                    "scrollCollapse": true
                });
            },
            error: function () { //erreur dans le cas les données ne sont pas envoyer on affiche un message qui indique l'erreur
                console.log("error");
            }
        });
    });

    $(document).on("click", "#submit_btn", function(event){
        event.preventDefault();
        console.log('true');
        var table = $('#table_resume').DataTable();
        $.ajax({
            type:'GET',
            url:'/resume/'+$(this).val(),
            dataType:'json',
            success: function (data) {// le cas ou la requete est bien execute en reçoi les données serialiser par JSON dans la variable msg
                //recuperation de la valeur stock dans l'attribut desactive
                var contenu = '<div class="container-fluid"><div class="row"> <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12"> <div class="panel panel-primary"> <div class="panel-heading"><h2>Configuration Fichier</h2></div>'+
                    '<div class="panel-body"><div class="row">'+
                    '<table id="table_resume" class="table table-striped" cellspacing="0" width="100%">'+
                    '<thead> <tr> <th width="52%">File Path</th> <th width="1%%">Type</th> <th width="1%">Séparateur</th> <th width="8%">Nombre de ligne skipped</th> <th width="30%"> header</th> <th width="10%">Date de création</th> </tr> </thead><tbody>'+
                    '<tr><td>'+data.reader.filePath+'</td> <td>'+data.reader.filePath.split(".")[1]+'</td> <td>'+data.reader.separator+'</td><td>'+data.reader.nbLineToSkip+'</td> <td>'+data.reader.columns+'</td> <td>'+moment(data.reader.dateCreation).format("DD/MM/YYYY HH:mm:ss")+'</td></tr></tbody> </table> </div> </div> </div> </div> </div>';
                contenu +='<div class="row"><div class="col-xs-12 col-sm-12 col-md-12 col-lg-12"> <div class="panel panel-primary"> <div class="panel-heading"> <h2>Table configuration </h2></div> <div class="panel-body">'+
                    '<table class="table table-bordered table-hover" id="table_auto"> <thead> <tr> <th width="2%">Primary key</th> <th width="15%">column</th> <th width="15%">Type</th> <th width="10%">Size</th> <th width="2%"> Not null</th> <th width="20%">Default</th> <th width="36%">Commentaire</th> </tr> </thead><tbody>';
                console.log("length "+ data.attributes[0].pko);
                for(var i = 0;i<data.attributes.length;i++) {
                    var qq,ss;
                    if(data.attributes[i].pko == true){
                        console.log("TRUE");
                        qq= "<input type='checkbox'  checked='checked' disabled>";
                    }else{
                        console.log("FALSEE");
                        qq = "<input type='checkbox'  disabled>";
                    }
                    if(data.attributes[i].nonNull == true){
                        ss= "<input type='checkbox'  checked='checked' disabled>";
                    }else{
                        ss = "<input type='checkbox'  disabled>";
                    }
                    contenu += '<tr> <td align="center">'+qq+'</td><td align="center">'+data.attributes[i].nameo+'</td><td align="center">'+data.attributes[i].type+'</td><td align="center">'+data.attributes[i].sizeo+'</td><td align="center">'+ss+'</td><td align="center">'+data.attributes[i].defaut+'</td><td>'+data.attributes[i].commentaires+'</td></tr>';
                }
                if(data.reader.executed == true) {
                contenu+='</tbody></table> </div> </div> </div></div><div class="row"> <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12"><div class="panel panel-primary"> <div class="panel-heading"> <h2>Batch Report</h2></div> <div class="panel-body">'+
                    '<table class="table table-bordered table-hover" id="table_auto"><thead> <tr> <th width="2%">Job Id</th> <th width="4%">Type de Job</th> <th width="10%">Start time</th> <th width="10%">End time </th> <th width="10%">status</th> <th width="10%">Read count</th> <th width="10%">Filter count</th> <th width="10%">Write count</th> <th width="10%">Read skip count</th> <th width="10%">Write skip count</th> <th width="10%">Process skip count</th> <th width="4%">Rollback count</th> </tr> </thead>'+
                    '<tbody> <tr> <td>'+data.batchStepExecution.job_execution_id+'</td> <td>'+data.batchStepExecution.step_name+'</td> <td>'+moment(data.batchStepExecution.start_time).format("DD/MM/YYYY HH:mm:ss")+'</td> <td>'+moment(data.batchStepExecution.end_time).format("DD/MM/YYYY HH:mm:ss")+'</td> <td>'+data.batchStepExecution.status+'</td> <td>'+data.batchStepExecution.read_count+'</td> <td>'+data.batchStepExecution.filter_count+'</td> <td>'+data.batchStepExecution.write_count+'</td> <td>'+data.batchStepExecution.read_skip_count+'</td> <td>'+data.batchStepExecution.write_skip_count+'</td> <td>'+data.batchStepExecution.process_skip_count+'</td> <td>'+data.batchStepExecution.rollback_count+'</td> </tr> </tbody> </table> </div> </div> </div> </div>';

                    if (data.inputError.length > 0) {
                        contenu += '<div class="row"> <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12"><div class="panel panel-danger"> <div class="panel-heading"><h2> Error input : </h2></div> <div class="panel-body"> <table class="table table-bordered table-hover" id="table_auto"> <thead> <tr> <th width="5%">Line Number</th> <th width="35%">Line</th> <th width="60%">Messages</th> </tr> </thead><tbody>';

                        for (var jj = 0; jj < data.inputError.length; jj++) {
                            contenu += '<tr> <td align="center">' + data.inputError[jj].lineNumber + '</td><td align="center">' + data.inputError[jj].line + '</td> <td>' + data.inputError[jj].messages + '</td> </tr>';
                        }
                        contenu += '</tbody> </table> </div> </div> </div> </div></div></div>';
                    } else {
                        contenu += '<div class="row"> <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12"><div class="panel panel-success"> <div class="panel-heading"><h2> Success : </h2></div> <div class="panel-body"> Toutes les lines ont été ajoutés</div></div>';
                    }
                }
                $("#resume1").html("");
                $('#resume1').append(contenu);
                $("#myModalxy").modal().show();
            },
            error: function () { //erreur dans le cas les données ne sont pas envoyer on affiche un message qui indique l'erreur
                console.log("error");
            }
        });
    });




    


    $('#latestTabId').on('click', '.btn-success', function (e) {
        e.preventDefault();
        var table = $('#latestTabId').DataTable();
        var data = "id="+$(this).val();
        var r = confirm("Voulez vous vraiement lancer ce job ?");
        if(r){
        $.ajax({
            type:'POST',
            data: data,
            url:'/runJob',
            dataType:'json',
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
                        "<table id='myInputErrors' width='100%' class='table table-striped' cellspacing='0' width='100%'>" +
                        "<thead><tr><th><b>Line Number</b></th><th ><b>Line</b></th><th><b>Message</b></th></thead><tbody>";
                    for (var i = 0; i < data.inputError.length; i++) {
                        contenu += "<tr><td align='center'>" + data.inputError[i].lineNumber + "</td><td align='center'>" + data.inputError[i].line + "</td><td>" + data.inputError[i].messages + "</td></tr>";
                    }
                    contenu += "</tbody></table>" +
                        "</div></div>";
                }
                contenu += "<div class='row'><div class='col-xs-12'><table id='resume' width='100%' class='table .table-bordered'>" +
                    "<thead><tr><th><b>Job ID</b></th><th ><b>Start time</b></th><th><b>End time</b></th><th><b>Status </b></th><th><b>commit_count</b></th><th><b>read_count</b></th><th>write_count</th><th>process_skip_count</th><th>exit_code</th></thead><tbody>";
                contenu += "<tr><td align='center'>" + data.batchStepExecution.job_execution_id + "</td><td align='center'>" + moment(data.batchStepExecution.start_time).format("DD/MM HH:mm:ss") + "</td><td align='center'>" + moment(data.batchStepExecution.end_time).format("DD/MM HH:mm:ss") + "</td><td align='center'>" + data.batchStepExecution.status + "</td><td align='center'>" + data.batchStepExecution.commit_count + "</td><td align='center'>" + data.batchStepExecution.read_count + "</td><td align='center'>" + data.batchStepExecution.write_count + "</td><td align='center'>" + data.batchStepExecution.process_skip_count + "</td><td align='center'>" + data.batchStepExecution.exit_code + "</td></tr>";
                contenu += "</tbody></table>" +
                    "</div></div></div></div></fieldset>";
                $('#modal-body').html("");
                $('#modal-body').append(contenu);
                $('#modal-success').modal('show');

            },
            error: function () { //erreur dans le cas les données ne sont pas envoyer on affiche un message qui indique l'erreur
                console.log("error");
            }

        });
        }
    });


    $(document).on("click", "#btn-success", function(event){
        event.preventDefault();
        var table = $('#latestTabId').DataTable();
        var data = "id="+$(this).val();
        var r = confirm("Voulez vous vraiement lancer ce job ?");
        if(r){
            $.ajax({
                type:'POST',
                data: data,
                url:'/runJob',
                dataType:'json',
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
                            "<table id='myInputErrors' class='table table-striped' cellspacing='0' width='100%'>" +
                            "<thead><tr><th><b>Line Number</b></th><th ><b>Line</b></th><th><b>Message</b></th></thead><tbody>";
                        for (var i = 0; i < data.inputError.length; i++) {
                            contenu += "<tr><td align='center'>" + data.inputError[i].lineNumber + "</td><td align='center'>" + data.inputError[i].line + "</td><td>" + data.inputError[i].messages + "</td></tr>";
                        }
                        contenu += "</tbody></table>" +
                            "</div></div>";
                    }
                    contenu += "<div class='row'><div class='col-xs-12'><table id='resume' width='100%' class='table .table-bordered'>" +
                        "<thead><tr><th><b>Job ID</b></th><th ><b>Start time</b></th><th><b>End time</b></th><th><b>Status </b></th><th><b>commit_count</b></th><th><b>read_count</b></th><th>write_count</th><th>process_skip_count</th><th>exit_code</th></thead><tbody>";
                    contenu += "<tr><td align='center'>" + data.batchStepExecution.job_execution_id + "</td><td align='center'>" + moment(data.batchStepExecution.start_time).format("DD/MM HH:mm:ss") + "</td><td align='center'>" + moment(data.batchStepExecution.end_time).format("DD/MM HH:mm:ss") + "</td><td align='center'>" + data.batchStepExecution.status + "</td><td align='center'>" + data.batchStepExecution.commit_count + "</td><td align='center'>" + data.batchStepExecution.read_count + "</td><td align='center'>" + data.batchStepExecution.write_count + "</td><td align='center'>" + data.batchStepExecution.process_skip_count + "</td><td align='center'>" + data.batchStepExecution.exit_code + "</td></tr>";
                    contenu += "</tbody></table>" +
                        "</div></div></div></div></fieldset>";
                    $('#modal-body').html("");
                    $('#modal-body').append(contenu);
                    $('#modal-success').modal('show');
                },
                error: function () { //erreur dans le cas les données ne sont pas envoyer on affiche un message qui indique l'erreur
                    console.log("error");
                }
            });
        }
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
                        "<table id='myInputErrors' class='table table-striped' cellspacing='0' width='100%'>" +
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


    
    $("#allMyJobFailed").on('click', function (e) {
        e.preventDefault();
        $.ajax({
            type: "GET",//la method à utiliser soit POST ou GET
            url: "/allMyJobFailed", //lien de la servlet qui exerce le traitement sur les données
            success: function (data) {// le cas ou la requete est bien execute en reçoi les données serialiser par JSON dans la variable msg
                //recuperation de la valeur stock dans l'attribut desactive
                var contenu = '<div class="container-fluid"><div class="row"> <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12"> <div class="panel panel-primary"> <div class="panel-heading"><h2>Configuration Fichier</h2></div>'+
                    '<div class="panel-body"><table class="table table-bordered table-hover" id="table_resume">'+
                    '<thead> <tr> <th width="2%">Job Id</th><th width="1%%">Date Création</th><th width="1%">Created by</th> <th width="8%">File</th> <th width="30%"> Nb line skiped</th> <th width="10%">Table</th> <th width="10%">Date Lancement</th><th width="10%">Exectued</th><th width="10%">Resultat</th><th width="10%">Nombre de lines ajouter</th><th width="10%">Nombre de lines ajouter</th><th width="10%"> Nombre de lines Non ajouter</th><th width="10%"> Nombre de lines Non ajouter</th> <th width="">More Détail</th></tr></thead><tbody>';
                var executedBy;
                var executed;
                var resultat;
                var lancement;
                var path;
                for (var i = 0; i < data.length; i++) {
                    if(data[i].executed_by == null){
                        executedBy = "<span class='label label-danger'> Not Executed yet </span>";
                    }else{
                        executedBy = "<span class='label label-success'>"+data[i].executed_by+"</span>";
                    }
                    if(data[i].executed  == false){
                        executed = "<span class='label label-danger'> Not Executed yet </span>";
                    }else{
                        executed = "<span class='label label-success'> True </span>";
                    }
                    if(data[i].resultat   == false){
                        resultat = "<span class='label label-danger'> Not disponible </span>";
                    }else{
                        resultat = "<span class='label label-success'>Success</span>";
                    }
                    if(data[i].dateLancement == null){
                        lancement = "<span class='label label-danger'> Pas encore lancer </span>";
                    }else{
                        lancement = "<span class='label label-success'> "+moment(data[i].dateLancement).format("DD/MM/YYYY HH:mm:ss") +" </span>";
                    }
                    if(data[i].filePath.split("/")[data[i].filePath.split("/").length-1].length > 20){
                        path = data[i].filePath.split("\\")[data[i].filePath.split("\\").length-1];
                    }else{
                        path = data[i].filePath.split("/")[data[i].filePath.split("/").length-1];
                    }
                    contenu += "<tr id='"+i+"' value='"+data[i].id+"'>" +
                        "<td >" + i + "</td>" +
                        "<td >" + moment(data[i].dateCreation).format("DD/MM/YYYY HH:mm:ss") + "</td>" +
                        "<td >" + data[i].emailUser.split("@")[0] + "</td>" +
                        "<td >" + path+ "</td>" +
                        "<td >" + data[i].nbLineToSkip + "</td>" +
                        "<td >" + data[i].classeName + "</td>" +
                        "<td >" + lancement+ "</td>" +
                        "<td >" + executed+ "</td>" +
                        "<td >" + resultat+ "</td>" +
                        "<td> "+ data[i].nbLinesSuccess+"</td>"+
                        "<td> "+ data[i].nbLinesFailed+"</td>"+
                        "<td><button id='btn-success' class='btn btn-success' value='" + data[i].id + "'><span class='glyphicon glyphicon glyphicon-play'></span></button></td>" +
                        "<td><button id='btn-danger' class='btn btn-danger' value='" + data[i].id + "'><span class='glyphicon glyphicon-remove-sign'></span></button></td>" +
                        "<td><button  id='submit_btn' class='btn btn-primary' value='" + data[i].classeName+ "'><span class='glyphicon glyphicon-eye-open'></span></button></td></tr>";
                }
                contenu += "</tbody></<table></div></div>";
                $("#resume").html("");
                $('#resume').append(contenu);
                $("#myModalx").modal().show();
                allTables = $('#table_resume').DataTable({
                    "order": [[ 1, "desc" ]],
                    'fnClearTable': true,
                    "scrollY": "500px",
                    "scrollCollapse": true
                });
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
                        contenu += "<tr><td>" + data[i].job_execution_id + "</td><td>" + moment(data[i].create_time).format("DD/MM/YYYY HH:mm:ss") + "</td><td>" + moment(data[i].start_time).format("DD/MM/YYYY HH:mm:ss") + "</td><td>" + moment(data[i].end_time).format("DD/MM/YYYY HH:mm:ss") + "</td><td>" + data[i].status + "</td><td>" + data[i].exit_code + "</td><td>" + data[i].exit_message + "</td><td>" + moment(data[i].last_updated).format("DD/MM/YYYY HH:mm:ss") + "</td></tr>";
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



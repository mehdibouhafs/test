$(document).ready(function() {
    var allTables;
    var i = 0;
    var my_delay = 50000;
    // call your ajax function when the document is ready...
    $(function() {
        callAjax();
    });



    var datetime = $('#datetimepicker1').datetimepicker({
        minDate: moment()
    });


    $('#imprimer').on('click',function (e) {
       // $('#selector').printElement({printMode:'popup'});
        $('#selector').print({
            globalStyles : true,
            mediaPrint : true,
            //append : "Batch report by MIMO and MBS<br/>",
             timeout: 250,
            title: 'Rapport de Batch',
            doctype: '<!doctype html>'
    });
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
                        "<td align='center'><span class='label label-default'>" + data[i].separator + "</span></td>" +
                        "<td align='center'><span class='label label-default'>" + data[i].classeName + "</span></td>" +
                        "<td align='center'>" + lancement + "</td>" +
                        "<td align='center'>" + executedBy.split("@")[0] + "</td>" +
                        "<td align='center'>" + resultat + "</td>" +
                        "<td align='center'><button  class='btn btn-primary' value='" + data[i].classeName + "'><span class='glyphicon glyphicon-eye-open'></span></button></td>"+
                        "<td align='center'><button  class='btn btn-warning'  id='"+data[i].id+"' value='" + data[i].classeName  + "'><span class='glyphicon glyphicon glyphicon-edit'></span></button></td>" +
                        "<td align='center'><button  class='time' style='background-color: #00c0ef;' value='" + data[i].classeName + "'><span class='glyphicon glyphicon glyphicon-time'></span></button></td>" +
                        "<td align='center'><button  class='btn btn-danger'  value='" + data[i].id + "'><span class='glyphicon glyphicon-remove-sign'></span></button></td>" +
                        "<td align='center'><button  class='btn btn-success' value='" + data[i].id + "'><span class='glyphicon glyphicon glyphicon-play'></span></button></td></tr>");
                } else {
                    $("#latestTab").append("<tr  id='" + i + "' value='" + data[i].id + "'>" +
                        "<td align='center'><span class='label label-default'>" + moment(data[i].dateCreation).format("DD/MM/YYYY HH:mm:ss") + "</span></td>" +
                        "<td align='center'><span class='label label-default'>" + path + "</span></td>" +
                        "<td align='center'><span class='label label-default'>" + data[i].separator + "</span></td>" +
                        "<td align='center'><span class='label label-default'>" + data[i].classeName + "</span></td>" +
                        "<td align='center'>" + lancement + "</td>" +
                        "<td align='center'>" + executedBy.split("@")[0] + "</td>" +
                        "<td align='center'>" + resultat + "</td>" +
                        "<td align='center'><button  class='btn btn-primary' value='" + data[i].classeName + "'><span class='glyphicon glyphicon-eye-open'></span></button></td>"+
                        "<td align='center'><button  class='btn btn-warning' id='"+data[i].id+"' value='" + data[i].classeName + "'><span class='glyphicon glyphicon glyphicon-edit'></span></button></td>" +
                        "<td align='center'><button  class='time' style='background-color: #00c0ef;' value='" + data[i].classeName + "'><span class='glyphicon glyphicon glyphicon-time'></span></button></td>" +
                        "<td align='center'><button  class='btn btn-danger' value='" + data[i].id + "'><span class='glyphicon glyphicon-remove-sign'></span></button></td>" +
                        "<td align='center'><button  class='btn btn-success' value='" + data[i].id + "'><span class='glyphicon glyphicon glyphicon-play'></span></button></td></tr>");
                }
            }
            allTables = $('#latestTabId').DataTable({
                "order": [[ 0, "desc" ]],
                'fnClearTable': true,
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

    $('#latestTabId').on('click', '.btn-warning', function (e) {
        e.preventDefault();
        console.log("edit");
        var par = $(this).parent().parent();
        var tdFile = par.children("td:nth-child(2)");
        var tdSep = par.children("td:nth-child(3)");
        var tdEdit = par.children("td:nth-child(9)");
        var value1 = $(this).closest("tr").find("td:eq(2)").text();
        var classe = $(this).attr("value");
        var value;
        var id = $(this).attr("id");
        var data ="id="+id;
        $.ajax({
            type:'POST',
            data:data,
            url:'/getReader',
            success:function (data) {
                console.log("file" + data.filePath);
                value = data.filePath;
                console.log('id ' + id);
                tdEdit.html("");
                tdFile.html("");
                tdSep.html("");
                tdEdit.html("<button  class='btn btn-file' id='"+id+"' value='" + classe + "'><span class='glyphicon glyphicon glyphicon-floppy-save'></span></button>");
                tdFile.html("<input type='text' id='filePath["+id+"]' value='"+value+"' style='width: 160px;'/>");
                tdSep.html('<select class="form-control" id="separator['+id+']" name="separator['+id+']" style="width: 70px;">'+
                    '<option id="separator[0]" ,name="separator[0]" title="point virgule" value="1">;</option>'+
                    '<option id="separator[1]" ,name="separator[1]" title="virgule" value="2">,</option>'+
                    '<option id="separator[2]" ,name="separator[2]" title="deux points" value="3">:</option>'+
                    '<option id="separator[3]" ,name="separator[3]" title="pipe" value="4">|</option>'+
                    '<option id="separator[3]" ,name="separator[3]" title="null" value="">null</option>'+
                    '</select>');
            },
            error:function () {
                console.log("JobParameter  NOT edited");
            }
        });
    });

    $('#latestTabId').on('click', '.btn-file', function (e) {
        e.preventDefault();
        console.log("edit");
        var id = $(this).attr("id");
        var classe = $(this).attr("value");

        var par = $(this).parent().parent();
        var path = $('#filePath\\['+id+'\\]').val();
        var path1;
        console.log(path);


        var separator = $('#separator\\['+id+'\\] option:selected' ).text();
        var separator1 = $('#separator\\['+id+'\\] option:selected' ).val();
        console.log("seâratpr = " + separator);

        var tdFile = par.children("td:nth-child(2)");
        var tdSep = par.children("td:nth-child(3)");
        var tdEdit = par.children("td:nth-child(9)");

        tdEdit.html("");
        tdFile.html("");
        tdSep.html("");

        if (path.split("/")[path.split("/").length - 1].length > 20) {
            path1 = path.split("\\")[path.split("\\").length - 1];
        } else {
            if( 2 < path.split("/")[path.split("/").length - 1].length < 20 ) {
                path1 = path.split("/")[path.split("/").length - 1];
            }else{
                path1 = path;
            }
        }

        tdFile.html("<span class='label label-default'>"+path1+"</span>");
        tdSep.html("<span class='label label-default'>"+separator+"</span>");
        tdEdit.html("<button  class='btn btn-warning' id='"+id+"' value='" +classe + "'><span class='glyphicon glyphicon glyphicon-edit'></span></button>");

        var data2 = "id="+id+"&";
        var data3 = "file="+path;
        var data4 = "separator="+separator1+"&";
        var data5 = data2.concat(data4);
        var data = data5.concat(data3);

        $.ajax({
            type:'POST',
            data:data,
            url:'/editReader',
            success:function (data) {
                //window.location.reload();
            },
            error:function () {
                console.log("JobParameter  NOT edited");
            }
        });



    });


    $('#latestTabId').on('click', '.time', function (e) {
        e.preventDefault();
        //var table = $('#latestTabId').DataTable();
        console.log('show mod');
        $("#program").html("");
        $("#program").append('<button class="programation btn btn-primary" name="'+$(this).val()+'" type="button"  >Valider</button>');
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
            success:function (data) {

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
            success:function (data) {
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
                    var contenu = '<div class="container-fluid"><div class="row"> <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12"> <div class="panel panel-primary"> <div class="panel-heading text-center">Configuration Fichier</div>'+
                    '<div class="panel-body">'+
                        '<table id="table_resume">'+
                        '<thead> <tr> <th >Chemin du Fichier</th> <th>Type</th> <th>Séparateur</th> <th>Nombre de ligne à Sauter</th> <th > header</th> <th >Date de création</th> </tr> </thead><tbody>'+
                    '<tr><td>'+data.reader.filePath+'</td> <td>'+data.reader.filePath.split(".")[1]+'</td> <td>'+data.reader.separator+'</td><td>'+data.reader.nbLineToSkip+'</td> <td>'+data.reader.columns+'</td> <td>'+moment(data.reader.dateCreation).format("DD/MM/YYYY HH:mm:ss")+'</td></tr></tbody> </table></div> </div> </div> </div> </div>';
                    contenu +='<div class="row"><div class="col-xs-12 col-sm-12 col-md-12 col-lg-12"> <div class="panel panel-primary"> <div class="panel-heading text-center"> Configuration des données </div> <div class="panel-body">'+
                                '<table id="table_auto"> <thead> <tr> <th width="2%">Clé Primaire</th> <th width="15%">colonne</th> <th width="15%">Type</th> <th width="10%">Taille</th> <th width="2%"> Non null</th> <th width="20%">Default</th> <th width="36%">Commentaire</th> </tr> </thead><tbody>';
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
                    contenu+='</tbody></table> </div> </div> </div></div><div class="row"> <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12"><div class="panel panel-primary"> <div class="panel-heading text-center">Batch Report</div> <div class="panel-body">'+
                        '<table id="table_auto"><thead> <tr> <th>Job Id</th> <th>Type de Job</th> <th>Start time</th> <th>End time </th> <th>status</th> <th >Read count</th> <th >Filter count</th> <th>Write count</th> <th>Read skip count</th> <th >Write skip count</th> <th>Process skip count</th> <th>Rollback count</th> </tr> </thead>'+
                    '<tbody> <tr> <td align="center">'+data.batchStepExecution.job_execution_id+'</td> <td align="center">'+data.batchStepExecution.step_name+'</td> <td align="center">'+moment(data.batchStepExecution.start_time).format("DD/MM/YYYY HH:mm:ss")+'</td> <td align="center">'+moment(data.batchStepExecution.end_time).format("DD/MM/YYYY HH:mm:ss")+'</td> <td align="center">'+data.batchStepExecution.status+'</td> <td>'+data.batchStepExecution.read_count+'</td> <td align="center">'+data.batchStepExecution.filter_count+'</td> <td align="center">'+data.batchStepExecution.write_count+'</td> <td align="center">'+data.batchStepExecution.read_skip_count+'</td> <td align="center">'+data.batchStepExecution.write_skip_count+'</td> <td align="center">'+data.batchStepExecution.process_skip_count+'</td> <td align="center">'+data.batchStepExecution.rollback_count+'</td> </tr> </tbody> </table> </div> </div> </div> </div>';
                        if (data.inputError.length > 0) {
                            contenu += '<div class="row"> <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12"><div class="panel panel-danger"> <div class="panel-heading text-center"> Error input : </div> <div class="panel-body"> <table id="table_auto"> <thead> <tr> <th>Line Number</th> <th>Line</th> <th>Error in</th><th> Cause</th> </tr> </thead><tbody>';

                            for (var jj = 0; jj < data.inputError.length; jj++) {
                                contenu += '<tr> <td>' + data.inputError[jj].lineNumber + '</td><td>' + data.inputError[jj].line + '</td> <td>' + data.inputError[jj].columne + '</td> <td>' + data.inputError[jj].cause + '</td></tr>';
                            }
                            contenu += '</tbody> </table> </div> </div> </div> </div></div></div>';
                        } else {
                            contenu += '<div class="row"> <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12"><div class="panel panel-success"> <div class="panel-heading text-center">Success : </div> <div class="panel-body"> Toutes les lignes ont été ajoutés</div></div>';
                        }
                    }
                    $("#resumep").html("");
                    $('#resumep').append(contenu);
                    $("#myModalxp").modal().show();
                    
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
                var contenu = '<div class="container-fluid"><div class="row"> <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12"> <div class="panel panel-primary"> <div class="panel-heading">Configuration Fichier</div>'+
                    '<div class="panel-body"><div class="row">'+
                    '<table id="table_resume">'+
                    '<thead> <tr> <th width="52%">Chemin du Fichier</th> <th width="1%%">Type</th> <th width="1%">Séparateur</th> <th width="8%">Nombre de ligne à sauter</th> <th width="30%"> header</th> <th width="10%">Date de création</th> </tr> </thead><tbody>'+
                    '<tr><td>'+data.reader.filePath+'</td> <td>'+data.reader.filePath.split(".")[1]+'</td> <td>'+data.reader.separator+'</td><td>'+data.reader.nbLineToSkip+'</td> <td>'+data.reader.columns+'</td> <td>'+moment(data.reader.dateCreation).format("DD/MM/YYYY HH:mm:ss")+'</td></tr></tbody> </table></div> </div> </div> </div> </div>';
                contenu +='<div class="row"><div class="col-xs-12 col-sm-12 col-md-12 col-lg-12"> <div class="panel panel-primary"> <div class="panel-heading text-center"> Configuration des données </div> <div class="panel-body">'+
                    '<table  id="table_auto1"> <thead> <tr> <th width="2%">Clé primaire</th> <th width="15%">colonne</th> <th width="15%">Type</th> <th width="10%">Taille</th> <th width="2%"> Non null</th> <th width="20%">Default</th> <th width="36%">Commentaire</th> </tr> </thead><tbody>';
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
                contenu+='</tbody></table> </div> </div> </div></div><div class="row"> <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12"><div class="panel panel-primary"> <div class="panel-heading">Batch Report</div> <div class="panel-body">'+
                    '<table id="table_auto"><thead> <tr> <th>Job Id</th> <th>Type de Job</th> <th >Start time</th> <th>End time </th> <th>status</th> <th>Read count</th> <th >Filter count</th> <th >Write count</th> <th >Read skip count</th> <th>Write skip count</th> <th>Process skip count</th> <th>Rollback count</th> </tr> </thead>'+
                    '<tbody> <tr> <td>'+data.batchStepExecution.job_execution_id+'</td> <td>'+data.batchStepExecution.step_name+'</td> <td>'+moment(data.batchStepExecution.start_time).format("DD/MM/YYYY HH:mm:ss")+'</td> <td>'+moment(data.batchStepExecution.end_time).format("DD/MM/YYYY HH:mm:ss")+'</td> <td>'+data.batchStepExecution.status+'</td> <td>'+data.batchStepExecution.read_count+'</td> <td>'+data.batchStepExecution.filter_count+'</td> <td>'+data.batchStepExecution.write_count+'</td> <td>'+data.batchStepExecution.read_skip_count+'</td> <td>'+data.batchStepExecution.write_skip_count+'</td> <td>'+data.batchStepExecution.process_skip_count+'</td> <td>'+data.batchStepExecution.rollback_count+'</td> </tr> </tbody> </table> </div> </div> </div> </div>';

                    if (data.inputError.length > 0) {
                        contenu += '<div class="row"> <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12"><div class="panel panel-danger"> <div class="panel-heading text-center"> Error input : </div> <div class="panel-body"> <table  id="table_auto"> <thead> <tr> <th>Line Number</th> <th >Line</th> <th>Error in</th><th>Cause</th></tr> </thead><tbody>';

                        for (var jj = 0; jj < data.inputError.length; jj++) {
                            contenu += '<tr> <td align="center">' + data.inputError[jj].lineNumber + '</td><td>' + data.inputError[jj].line + '</td> <td>' + data.inputError[jj].columne + '</td> <td>' + data.inputError[jj].cause + '</td> </tr>';
                        }
                        contenu += '</tbody> </table> </div> </div> </div> </div></div></div>';
                    } else {
                        contenu += '<div class="row"> <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12"><div class="panel panel-success"> <div class="panel-heading text-center"> Success : </div> <div class="panel-body"> Toutes les lignes ont été ajoutés avec succès</div></div>';
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
                var contenu = '<div class="container-fluid"><div class="row"> <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12"> <div class="panel panel-primary"> <div class="panel-heading text-center">Configuration Fichier</div>'+
                    '<div class="panel-body"><div style="overflow-x:auto;"><table id="table_resume">'+
                '<thead> <tr><th>Date Création</th><th >Created by</th> <th>File</th> <th> Nb line skiped</th> <th>Table</th> <th>Date Lancement</th><th width="10%">Resultat</th><th>Nombre de lines ajouter</th><th >Nombre de lines ajouter</th><th> Nombre de lines Non ajouter</th><th> Nombre de lines Non ajouter</th> <th>More Détail</th></tr></thead><tbody>';
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
                        "<td align='center'><span class='label label-info'>" + moment(data[i].dateCreation).format("DD/MM/YYYY HH:mm:ss") + "</span></td>" +
                        "<td align='center'>" + data[i].emailUser.split("@")[0] + "</td>" +
                        "<td align='center'>" + path+ "</td>" +
                        "<td align='center'>" + data[i].nbLineToSkip + "</td>" +
                        "<td align='center'>" + data[i].classeName + "</td>" +
                        "<td align='center'>" + lancement+ "</td>" +
                        "<td align='center'>" + resultat+ "</td>" +
                        "<td align='center'> "+ data[i].nbLinesSuccess+"</td>"+
                        "<td align='center'> "+ data[i].nbLinesFailed+"</td>"+
                        "<td align='center'><button id='btn-success' class='btn btn-success' value='" + data[i].id + "'><span class='glyphicon glyphicon glyphicon-play'></span></button></td>" +
                        "<td align='center'><button id='btn-danger' class='btn btn-danger' value='" + data[i].id + "'><span class='glyphicon glyphicon-remove-sign'></span></button></td>" +
                        "<td align='center'><button  id='submit_btn' class='btn btn-primary' value='" + data[i].classeName+ "'><span class='glyphicon glyphicon-eye-open'></span></button></td></tr>";
                }
                contenu += "</tbody></table></div></div></div>";
                $("#resume").html("");
                $('#resume').append(contenu);
                $("#myModalx").modal().show();
                allTables = $('#table_resume').DataTable({
                    "order": [[ 0, "desc" ]],
                    'fnClearTable': true,
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
                var contenu = '<div class="container-fluid"><div class="row"> <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12"> <div class="panel panel-primary"> <div class="panel-heading text-center">Configuration Fichier</div>'+
                    '<div class="panel-body"><div style="overflow-x:auto;"><table id="table_resume">'+
                    '<thead> <tr><th>Date Création</th><th >Crée par</th> <th>Fichier</th> <th> Nb line à sauter</th> <th>Table</th> <th>Date de Lancement</th><th width="10%">Resultat</th><th>Nombre de lines ajouter</th><th >Nombre de lines ajouter</th><th> Nombre de lines Non ajouter</th><th> Nombre de lines Non ajouter</th> <th>More Détail</th></tr></thead><tbody>';
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
                        "<td align='center'><span class='label label-info'>" + moment(data[i].dateCreation).format("DD/MM/YYYY HH:mm:ss") + "</span></td>" +
                        "<td align='center'>" + data[i].emailUser.split("@")[0] + "</td>" +
                        "<td align='center'>" + path+ "</td>" +
                        "<td align='center'>" + data[i].nbLineToSkip + "</td>" +
                        "<td align='center'>" + data[i].classeName + "</td>" +
                        "<td align='center'>" + lancement+ "</td>" +
                        "<td align='center'>" + resultat+ "</td>" +
                        "<td align='center'> "+ data[i].nbLinesSuccess+"</td>"+
                        "<td align='center'> "+ data[i].nbLinesFailed+"</td>"+
                        "<td align='center'><button id='btn-success' class='btn btn-success' value='" + data[i].id + "'><span class='glyphicon glyphicon glyphicon-play'></span></button></td>" +
                        "<td align='center'><button id='btn-danger' class='btn btn-danger' value='" + data[i].id + "'><span class='glyphicon glyphicon-remove-sign'></span></button></td>" +
                        "<td align='center'><button  id='submit_btn' class='btn btn-primary' value='" + data[i].classeName+ "'><span class='glyphicon glyphicon-eye-open'></span></button></td></tr>";
                }
                contenu += "</tbody></table></div></div></div>";
                $("#resume").html("");
                $('#resume').append(contenu);
                $("#myModalx").modal().show();
                allTables = $('#table_resume').DataTable({
                    "order": [[ 0, "desc" ]],
                    'fnClearTable': true,
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
                var contenu = '<div class="container-fluid"><div class="row"> <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12"> <div class="panel panel-primary"> <div class="panel-heading text-center">Configuration Fichier</div>'+
                    '<div class="panel-body"><div class="row">'+
                    '<table id="table_resume">'+
                    '<thead> <tr> <th>Chemin du Fichier</th> <th >Type</th> <th width="1%">Séparateur</th> <th>Nombre de ligne à sauter</th> <th> header</th> <th >Date de création</th> </tr> </thead><tbody>'+
                    '<tr><td>'+data.reader.filePath+'</td> <td>'+data.reader.filePath.split(".")[1]+'</td> <td>'+data.reader.separator+'</td><td>'+data.reader.nbLineToSkip+'</td> <td>'+data.reader.columns+'</td> <td>'+moment(data.reader.dateCreation).format("DD/MM/YYYY HH:mm:ss")+'</td></tr></tbody> </table> </div> </div> </div> </div> </div>';
                contenu +='<div class="row"><div class="col-xs-12 col-sm-12 col-md-12 col-lg-12"> <div class="panel panel-primary"> <div class="panel-heading text-center">Configuration des données </div> <div class="panel-body">'+
                    '<table id="table_auto"> <thead> <tr> <th width="2%">Clé primaire</th> <th>colonne</th> <th>Type</th> <th>Taille</th> <th> Non null</th> <th>Default</th> <th>Commentaire</th> </tr> </thead><tbody>';
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
                contenu+='</tbody></table> </div> </div> </div></div><div class="row"> <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12"><div class="panel panel-primary"> <div class="panel-heading text-center"> Batch Report</div> <div class="panel-body">'+
                    '<table id="table_auto"><thead> <tr> <th>Job Id</th> <th>Type de tache</th> <th>Débute à</th> <th>Termine à </th> <th >Statut</th> <th>Read count</th> <th>Filter count</th> <th>Write count</th> <th>Read skip count</th> <th>Write skip count</th> <th>Process skip count</th> <th>Rollback count</th> </tr> </thead>'+
                    '<tbody> <tr> <td>'+data.batchStepExecution.job_execution_id+'</td> <td>'+data.batchStepExecution.step_name+'</td> <td>'+moment(data.batchStepExecution.start_time).format("DD/MM/YYYY HH:mm:ss")+'</td> <td>'+moment(data.batchStepExecution.end_time).format("DD/MM/YYYY HH:mm:ss")+'</td> <td>'+data.batchStepExecution.status+'</td> <td>'+data.batchStepExecution.read_count+'</td> <td>'+data.batchStepExecution.filter_count+'</td> <td>'+data.batchStepExecution.write_count+'</td> <td>'+data.batchStepExecution.read_skip_count+'</td> <td>'+data.batchStepExecution.write_skip_count+'</td> <td>'+data.batchStepExecution.process_skip_count+'</td> <td>'+data.batchStepExecution.rollback_count+'</td> </tr> </tbody> </table> </div> </div> </div> </div>';

                    if (data.inputError.length > 0) {
                        contenu += '<div class="row"> <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12"><div class="panel panel-danger"> <div class="panel-heading text-center"> Error input :</div> <div class="panel-body"> <table  id="table_auto"> <thead> <tr> <th>Line Number</th> <th >Line</th> <th >Error in</th><th>Cause</th></tr> </thead><tbody>';

                        for (var jj = 0; jj < data.inputError.length; jj++) {
                            contenu += '<tr> <td align="center">' + data.inputError[jj].lineNumber + '</td><td>' + data.inputError[jj].line + '</td> <td>' + data.inputError[jj].columne + '</td> <td>' + data.inputError[jj].cause + '</td> </tr>';
                        }
                        contenu += '</tbody> </table> </div> </div> </div> </div></div></div>';
                    } else {
                        contenu += '<div class="row"> <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12"><div class="panel panel-success"> <div class="panel-heading text-center">Success :</div> <div class="panel-body"> Toutes les lignes ont été ajoutés avec succès</div></div>';
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
                //console.log("data.length" + data.inputError.length);
                if(data.error != null){
                    contenu += "<fieldset><div class='panel panel-danger'><div class='panel-heading text-center' ><div class='pad margin no-print'>"+
                        "<div class='callout callout-danger' style='margin-bottom: 0!important;'>"+
                        "<h4><i class='fa fa-info'></i> ALERT :</h4>Ce batch à un Probléme de parsing du fichier ... </div>"+
                        "</div></div>"
                    contenu += "<div class='panel-body'><div class='row'><div class='col-xs-12'>" +data.error +"</div></div></div></div>";
                }
                else {
                    if ((data.inputError.length) == 0) {
                        contenu += "<fieldset><div class='panel panel-success'><div class='panel-heading text-center'><div class='pad margin no-print'>" +
                            "<div class='callout callout-success' style='margin-bottom: 0!important;'>" +
                            "<h4><i class='fa fa-info'></i> SUCCESS:</h4> Batch terminer avec succès... </div>" +
                            "</div></div><div class='panel-body'>"
                    } else {
                        contenu += "<fieldset><div class='panel panel-danger'><div class='panel-heading text-center' ><div class='pad margin no-print'>" +
                            "<div class='callout callout-danger' style='margin-bottom: 0!important;'>" +
                            "<h4><i class='fa fa-info'></i> ALERT :</h4>Ce Batch à de problèmes de parsing... </div>" +
                            "</div></div>"
                        contenu += "<div class='panel-body'><div class='row'><div class='col-xs-12'>" +
                            "<table id='myInputErrors' >" +
                            "<thead><tr><th><b>Nb de ligne</b></th><th ><b>Ligne</b></th><th><b>Erreur à</b></th><th><b>Cause</b></th></thead><tbody>";
                        for (var i = 0; i < data.inputError.length; i++) {
                            contenu += "<tr><td align='center'>" + data.inputError[i].lineNumber + "</td><td align='center'>" + data.inputError[i].line + "</td><td>" + data.inputError[i].columne + "</td><td>" + data.inputError[i].cause + "</td></tr>";
                        }
                        contenu += "</tbody></table>" +
                            "</div></div>";
                    }
                    contenu += "<div class='row'><div class='col-xs-12'><table id='resume'>" +
                        "<thead><tr><th><b>Batch id</b></th><th ><b>Débute à</b></th><th><b>Termine à</b></th><th><b>Statut </b></th><th><b>Read count</b></th><th>Write count</th><th>Process skip count</th><th>Exit_code</th></thead><tbody>";
                    contenu += "<tr><td align='center'>" + data.batchStepExecution.job_execution_id + "</td><td align='center'>" + moment(data.batchStepExecution.start_time).format("DD/MM HH:mm:ss") + "</td><td align='center'>" + moment(data.batchStepExecution.end_time).format("DD/MM HH:mm:ss") + "</td><td align='center'>" + data.batchStepExecution.status + "</td><td align='center'>" + data.batchStepExecution.read_count + "</td><td align='center'>" + data.batchStepExecution.write_count + "</td><td align='center'>" + data.batchStepExecution.process_skip_count + "</td><td align='center'>" + data.batchStepExecution.exit_code + "</td></tr>";
                    contenu += "</tbody></table>" +
                        "</div></div></div></div></fieldset>";
                }
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
                   // console.log("data.length" + data.inputError.length);
                    if ((data.inputError.length) == 0) {
                        contenu += "<fieldset><div class='panel panel-success'><div class='panel-heading text-center'><div class='pad margin no-print'>"+
                            "<div class='callout callout-success' style='margin-bottom: 0!important;'>"+
                            "<h4><i class='fa fa-info'></i> SUCCESS:</h4>Batch terminé avec succès... </div>"+
                            "</div></div><div class='panel-body'>"
                    } else {
                        contenu += "<fieldset><div class='panel panel-danger'><div class='panel-heading text-center'><div class='pad margin no-print'>"+
                            "<div class='callout callout-danger' style='margin-bottom: 0!important;'>"+
                            "<h4><i class='fa fa-info'></i> ALERT :</h4>Ce Batch à des erreur de parsing du fichier... </div>"+
                            "</div></div>"
                        contenu += "<div class='panel-body'><div class='row'><div class='col-xs-12'>" +
                            "<table id='myInputErrors'>" +
                            "<thead><tr><th><b>Numero de Ligne</b></th><th ><b>Ligne</b></th><th><b>Erreur à</b></th><th>Cause</th></thead><tbody>";
                        for (var i = 0; i < data.inputError.length; i++) {
                            contenu += "<tr><td align='center'>" + data.inputError[i].lineNumber + "</td><td align='center'>" + data.inputError[i].line + "</td><td>" + data.inputError[i].columne + "</td><td>" + data.inputError[i].cause + "</td></tr>";
                        }
                        contenu += "</tbody></table>" +
                            "</div></div>";
                    }
                    contenu += "<div class='row'><div class='col-xs-12'><table id='resume'>" +
                        "<thead><tr><th><b>Batch id</b></th><th ><b>Débute à</b></th><th><b>Termine à</b></th><th><b>Status </b></th><th><b>read_count</b></th><th>write_count</th><th>process_skip_count</th><th>exit_code</th></thead><tbody>";
                    contenu += "<tr><td align='center'>" + data.batchStepExecution.job_execution_id + "</td><td align='center'>" + moment(data.batchStepExecution.start_time).format("DD/MM HH:mm:ss") + "</td><td align='center'>" + moment(data.batchStepExecution.end_time).format("DD/MM HH:mm:ss") + "</td><td align='center'>" + data.batchStepExecution.status + "</td><td align='center'>" + data.batchStepExecution.read_count + "</td><td align='center'>" + data.batchStepExecution.write_count + "</td><td align='center'>" + data.batchStepExecution.process_skip_count + "</td><td align='center'>" + data.batchStepExecution.exit_code + "</td></tr>";
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
                    contenu += "<fieldset><div class='panel panel-success'><div class='panel-heading text-center'><div class='pad margin no-print'>"+
                        "<div class='callout callout-success' style='margin-bottom: 0!important;'>"+
                        "<h4><i class='fa fa-info'></i> SUCCESS:</h4>This JOB HAS NO ERROR INPUT DUE TO EXCEPTIONS... </div>"+
                        "</div></div><div class='panel-body'>"
                } else {
                    contenu += "<fieldset><div class='panel panel-danger'><div class='panel-heading text-center'><div class='pad margin no-print'>"+
                        "<div class='callout callout-danger' style='margin-bottom: 0!important;'>"+
                        "<h4><i class='fa fa-info'></i> ALERT :</h4>This JOB HAS ERROR INPUT... </div>"+
                        "</div></div>"
                    contenu += "<div class='panel-body'><div class='row'><div class='col-xs-12'>" +
                        "<table id='myInputErrors'>" +
                        "<thead><tr><th><b>Line Number</b></th><th ><b>Line</b></th><th><b>Error in</b></th><th><b>Cause</b></th></thead><tbody>";
                    for (var i = 0; i < data.inputError.length; i++) {
                        contenu += "<tr><td>" + data.inputError[i].lineNumber + "</td><td>" + data.inputError[i].line + "</td><td>" + data.inputError[i].columne + "</td><td>" + data.inputError[i].cause + "</td></tr>";
                    }
                    contenu += "</tbody></table>" +
                        "</div></div>";
                }
                contenu += "<div class='row'><div class='col-xs-12'><table id='resume'>" +
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
                var contenu = '<div class="container-fluid"><div class="row"> <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12"> <div class="panel panel-primary"> <div class="panel-heading text-center">Configuration Fichier</div>'+
                    '<div class="panel-body"><div style="overflow-x:auto;"><table id="table_resume">'+
                    '<thead> <tr><th>Date Création</th><th >Created by</th> <th>File</th> <th> Nb line skiped</th> <th>Table</th> <th>Date Lancement</th><th width="10%">Resultat</th><th>Nombre de lines ajouter</th><th >Nombre de lines ajouter</th><th> Nombre de lines Non ajouter</th><th> Nombre de lines Non ajouter</th> <th>More Détail</th></tr></thead><tbody>';
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
                        "<td align='center'><span class='label label-info'>" + moment(data[i].dateCreation).format("DD/MM/YYYY HH:mm:ss") + "</span></td>" +
                        "<td align='center'>" + data[i].emailUser.split("@")[0] + "</td>" +
                        "<td align='center'>" + path+ "</td>" +
                        "<td align='center'>" + data[i].nbLineToSkip + "</td>" +
                        "<td align='center'>" + data[i].classeName + "</td>" +
                        "<td align='center'>" + lancement+ "</td>" +
                        "<td align='center'>" + resultat+ "</td>" +
                        "<td align='center'> "+ data[i].nbLinesSuccess+"</td>"+
                        "<td align='center'> "+ data[i].nbLinesFailed+"</td>"+
                        "<td align='center'><button id='btn-success' class='btn btn-success' value='" + data[i].id + "'><span class='glyphicon glyphicon glyphicon-play'></span></button></td>" +
                        "<td align='center'><button id='btn-danger' class='btn btn-danger' value='" + data[i].id + "'><span class='glyphicon glyphicon-remove-sign'></span></button></td>" +
                        "<td align='center'><button  id='submit_btn' class='btn btn-primary' value='" + data[i].classeName+ "'><span class='glyphicon glyphicon-eye-open'></span></button></td></tr>";
                }
                contenu += "</tbody></table></div></div></div>";
                $("#resume").html("");
                $('#resume').append(contenu);
                $("#myModalx").modal().show();
                allTables = $('#table_resume').DataTable({
                    "order": [[ 0, "desc" ]],
                    'fnClearTable': true,
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
                    contenu += "<div class='panel panel-warning'><div class='panel-heading text-center'>  ALL JOBS Failed</div><div class='panel-body'><fieldset>" +
                        "<table id='myjobs' >" +
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



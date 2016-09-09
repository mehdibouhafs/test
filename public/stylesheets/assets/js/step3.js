$(document).ready(function() {
var allTables2;
$.ajax({
    type: "GET",//la method à utiliser soit POST ou GET
    url: "/readersXml", //lien de la servlet qui exerce le traitement sur les données
    dataType: 'json',
    success: function (data) {// le cas ou la requete est bien execute en reçoi les données serialiser par JSON dans la variable msg
        //recuperation de la valeur stock dans l'attribut desactive
        var executedBy;
        var executed;
        var resultat;
        var lancement;
        var path;
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

            $("#latestTab2").append("<tr id='" + i + "' value='" + data[i].id + "'>" +
                "<td >" + i + "</td>" +
                "<td >" + moment(data[i].dateCreation).format("DD/MM/YYYY HH:mm:ss") + "</td>" +
                "<td >" + data[i].emailUser.split("@")[0] + "</td>" +
                "<td >" + path + "</td>" +
                "<td >" + data[i].nbLineToSkip + "</td>" +
                "<td >" + data[i].classeName + "</td>" +
                "<td >" + lancement + "</td>" +
                "<td >" + executedBy + "</td>" +
                "<td >" + executed + "</td>" +
                "<td >" + resultat + "</td>" +
                "<td> " + data[i].nbLinesSuccess + "</td>" +
                "<td> " + data[i].nbLinesFailed + "</td>" +
                "<td><button  class='btn btn-primary' value='" + data[i].classeName + "'><span class='glyphicon glyphicon-eye-open'></span></button></td></tr>");
        }


        allTables2 = $('#latestTabId2').DataTable({
            'fnClearTable': true,
            "scrollCollapse": true
        });;

    },
    error: function () { //erreur dans le cas les données ne sont pas envoyer on affiche un message qui indique l'erreur
        console.log("error");
    }
});

    $('#latestTabId2').on('click', '.btn-primary', function (e) {
        console.log("lol");
        e.preventDefault();
        $.ajax({
            type:'GET',
            url:'/resume/'+$(this).val(),
            dataType:'json',
            success: function (data) {// le cas ou la requete est bien execute en reçoi les données serialiser par JSON dans la variable msg
                //recuperation de la valeur stock dans l'attribut desactive
                var contenu = '<div class="container-fluid"><div class="row"> <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12"> <div class="panel panel-primary"> <div class="panel-heading text-center">Configuration Fichier</div>'+
                    '<div class="panel-body"><div class="row">'+
                    '<table class="table table-bordered table-hover" id="table_resume">'+
                    '<thead> <tr> <th width="52%">File Path</th> <th width="1%%">Type</th> <th width="1%">Séparateur</th> <th width="8%">Nombre de ligne skipped</th> <th width="30%"> header</th> <th width="10%">Date de création</th> </tr> </thead><tbody>'+
                    '<tr><td>'+data.reader.filePath+'</td> <td>'+data.reader.filePath.split(".")[1]+'</td> <td>'+data.reader.separator+'</td><td>'+data.reader.nbLineToSkip+'</td> <td>'+data.reader.columns+'</td> <td>'+moment(data.reader.dateCreation).format("DD/MM/YYYY HH:mm:ss")+'</td></tr></tbody> </table></div> </div> </div> </div> </div>';
                contenu +='<div class="row"><div class="col-xs-12 col-sm-12 col-md-12 col-lg-12"> <div class="panel panel-primary"> <div class="panel-heading text-center"> Configuration des données </div> <div class="panel-body">'+
                    '<table class="table table-bordered table-hover" id="table_auto"> <thead> <tr> <th width="2%">Primary key</th> <th width="15%">column</th> <th width="15%">Type</th> <th width="10%">Size</th> <th width="2%"> Not null</th> <th width="20%">Default</th> <th width="36%">Commentaire</th> </tr> </thead><tbody>';
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
                    contenu+='</tbody></table> </div> </div> </div></div><div class="row"> <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12"><div class="panel panel-primary"> <div class="panel-heading text-center">Rapport du Batch</div> <div class="panel-body">'+
                        '<table  id="table_auto"><thead> <tr> <th">Batch id</th> <th>Type de batch</th> <th>Débute à</th> <th>Termine à </th> <th>Statut</th> <th>Read count</th> <th>Filter count</th> <th>Write count</th> <th>Read skip count</th> <th>Write skip count</th> <th>Process skip count</th> <th>Rollback count</th> </tr> </thead>'+
                        '<tbody> <tr> <td align="center">'+data.batchStepExecution.job_execution_id+'</td> <td align="center">'+data.batchStepExecution.step_name+'</td> <td align="center">'+moment(data.batchStepExecution.start_time).format("DD/MM/YYYY HH:mm:ss")+'</td> <td align="center">'+moment(data.batchStepExecution.end_time).format("DD/MM/YYYY HH:mm:ss")+'</td> <td align="center">'+data.batchStepExecution.status+'</td> <td align="center">'+data.batchStepExecution.read_count+'</td> <td align="center">'+data.batchStepExecution.filter_count+'</td> <td align="center">'+data.batchStepExecution.write_count+'</td> <td align="center">'+data.batchStepExecution.read_skip_count+'</td> <td align="center">'+data.batchStepExecution.write_skip_count+'</td> <td align="center">'+data.batchStepExecution.process_skip_count+'</td> <td align="center">'+data.batchStepExecution.rollback_count+'</td> </tr> </tbody> </table> </div> </div> </div> </div>';

                    if (data.inputError.length > 0) {
                        contenu += '<div class="row"> <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12"><div class="panel panel-danger"> <div class="panel-heading text-center"> Error input : </div> <div class="panel-body"> <table id="table_auto"> <thead> <tr> <th>Line Number</th> <th>Line</th> <th>Error in</th><th> Cause</th> </tr> </thead><tbody>';

                        for (var jj = 0; jj < data.inputError.length; jj++) {
                            contenu += '<tr> <td>' + data.inputError[jj].lineNumber + '</td><td>' + data.inputError[jj].line + '</td> <td>' + data.inputError[jj].columne + '</td> <td>' + data.inputError[jj].cause + '</td></tr>';
                        }
                        contenu += '</tbody> </table> </div> </div> </div> </div></div></div>';
                    } else {
                        contenu += '<div class="row"> <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12"><div class="panel panel-success"> <div class="panel-heading text-center">Success : </div> <div class="panel-body"> Toutes les lignes ont été ajoutés avec succès</div></div>';
                    }
                }
                $("#resume2").html("");
                $('#resume2').append(contenu);
                $("#myModalxyz").modal().show();

            },
            error: function () { //erreur dans le cas les données ne sont pas envoyer on affiche un message qui indique l'erreur
                console.log("error");
            }
        });
    });


    var colSelected2;
    $('#latestTabId2 tbody').on( 'click', 'tr', function () {
        console.log("click");
        if ( $(this).hasClass('selected') ) {
            $(this).removeClass('selected');
        }
        else {
            allTables2.$('tr.selected').removeClass('selected');
            $(this).addClass('selected');
            colSelected2= $(this).attr('value');
        }
    });


    $("#validateExisteXml").on('click',function (e) {
        e.preventDefault();
        var classe = "classe="+colSelected2+"&";
        var data = classe.concat($('#form00').serialize());
        $.ajax({
            type: "POST",//la method à utiliser soit POST ou GET
            url: "/xmlExist", //lien de la servlet qui exerce le traitement sur les données
            data:data,
            dataType: 'json',
            success: function (data) {// le cas ou la requete est bien execute en reçoi les données serialiser par JSON dans la variable msg
                $("#contenus").html("");
                var currentDate = moment().format("DD-MM-YYYY");
                var contenu = "<div class='panel panel-success'><div class='panel-heading text-center'><div class='pad margin no-print'>"
                    + "<div class='callout callout-success' style='margin-bottom: 0!important;'>"+
                    "<h4><i class='fa fa-info'></i> SUCCESS:</h4> <span class='label label-info'> Table   :  "+$('#tableName').val()+"</span>&nbsp;&nbsp;&nbsp;&nbsp;<span class='label label-info'>  Date        :        "+currentDate+"</span></div></div><div class='panel-body'><fieldset>" +
                    "<table id='myInputErrors' width='100%'>" +
                    "<thead><tr><th><b>Chemin du fichier</b></th><th ><b>Separateur</b></th><th><b>Nb lignes sauter </b></th></thead><tbody>"+
                    "<tr><td><span class='label label-primary'>"+$('#filePath').val()+"</span></td><td><span class='label label-danger'>"+$('#separator').val()+"</span></td><td><span class='label label-danger'>"+$('#nbLineToSkip').val()+"</span></td></tr></tbody></table></fieldset>"+
                    "<fieldset>"+
                    "<table id='table2' name='table2'> <thead style='background: #3c8dbc'><tr> " +
                    "<th style='color: beige; width: 26px;'>Id</th>"+
                    "<th style='color: beige; width: 72px;'> PK</th>"+
                    "<th style='color: beige; width: 72px;'> Name</th>"+
                    "<th style='color: beige; width: 72px;'> Type </th>"+
                    "<th style='color: beige; width: 205px;'> Taille</th>"+
                    "<th style='color: beige; width: 72px;'> !NULL</th>"+
                    "<th style='color: beige; width: 72px;'>  Défault</th>"+
                    "<th style='color: beige; width: 72px;'>Commentaire</th></tr> </thead><tbody id='tableaucontenus2'>";
                var q,s;
                for(var i = 0 ; i< data.length ; i++){
                    if(data[i].pko == true){
                        console.log("TRUE");
                        q= "<td align='center'><input type='checkbox'  checked='checked' disabled></td>";
                    }else{
                        console.log("FALSEE");
                        q = "<td align='center'><input type='checkbox'  disabled></td>";
                    }
                    if(data[i].nonNull == true){
                        s= "<td align='center'><input type='checkbox'  checked='checked' disabled></td>";
                    }else{
                        s = "<td align='center'><input type='checkbox'  disabled></td>";
                    }
                    contenu += "<tr><td align='center'><span class='label label-danger'>"+i+"</span></td>"+q+"<td align='center'><span class='label label-info'>"+data[i].nameo+"</span></td><td><span class='label label-info'>"+data[i].type+"</span></td><td align='center'><span class='label label-success'>"+data[i].sizeo+"</span></td>"+s+"<td align='center'>"+data[i].datac+"</td><td align='center'>"+data[i].commentaires+"</td></td></tr>";
                }
                contenu+="</tbody></table></fieldset>";
                $('#table2').DataTable({
                    "scrollCollapse": true
                });
                $('#contenus').html("");
                $('#contenus').append(contenu);
                $('#Modalx').modal('show');

            },
            error: function () { //erreur dans le cas les données ne sont pas envoyer on affiche un message qui indique l'erreur
                console.log("error");
            }
        });
    });
});

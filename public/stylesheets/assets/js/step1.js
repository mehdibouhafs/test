$(document).ready(function() {
    var allTables;var  i = 0;
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
            $("#latestTab").append("<tr><td><a href='pages/examples/invoice.html'>"+data[i].id+"</a></td>"+
                "<td>"+data[i].className+"</td>"+
           "<td><button class='attribute' value='"+data[i].className+"'>" +
                "<span class='fa fa-eye'></span></a></button></div></td></tr>");
            }
             allTables = $('#latestTabId').DataTable({
                'fnClearTable':true,
                "scrollY":        "300px",
                "scrollCollapse": true
            });
        },
        error: function () { //erreur dans le cas les données ne sont pas envoyer on affiche un message qui indique l'erreur
            console.log("error");
        }
    });


    $('#latestTabId').on( 'click', '.attribute', function (e) {
        e.preventDefault();
        $.ajax({
            type: "GET",//la method à utiliser soit POST ou GET
            url: "/attributes/"+$(this).val(), //lien de la servlet qui exerce le traitement sur les données
            success: function (data) {// le cas ou la requete est bien execute en reçoi les données serialiser par JSON dans la variable msg
                //recuperation de la valeur stock dans l'attribut desactive
                $("#contenu").html("");
                var contenu = "";
                contenu += "<fieldset>" +
                    "<table id='infoClasse' width='100%' class='table .table-bordered'>" +
                    "<thead><tr><th><b>Colonne</b></th><th ><b>Type</b></th><th><b>Size</b></th><th><b>Primary Key</b></th><th><b>Non Null</b></th></thead><tbody>";
                for (var i = 0; i < data.length; i++) {
                    contenu += "<tr><td>" + data[i].nameo + "</td><td>" + data[i].type + "</td><td>" + data[i].sizeo + "</td><td>" + data[i].pko + "</td><td>" + data[i].nonNull + "</td></tr>";
                }
                contenu += "</tbody></table>" +
                    "</fieldset>";
                $("#contenu").append(contenu);
                $("#Modalx").modal("show");

            },
            error: function () { //erreur dans le cas les données ne sont pas envoyer on affiche un message qui indique l'erreur
                console.log("error");
            }
        });

    });

    $("#showAllTables").on('click',function (e) {
        if(i==0) {
            $('#allTables').dataTable({
                paging: false
            });
            i++;
        }else{
            if(i==1){
                $('#allTables').DataTable({
                    'fnClearTable':true,
                    "scrollY":        "300px",
                    "scrollCollapse": true
                });
                i--;
            }

        }

    })

});



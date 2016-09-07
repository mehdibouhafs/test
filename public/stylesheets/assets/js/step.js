$(document).ready(function() {
    var table1;
    $('#type3').hide();
    $("#entete").bootstrapSwitch('state', true);
    $('#columns').hide();
    $('#dropTab').hide();
    $('#csvNoHead').hide();
    $('#xml').hide();
    $('#csv').hide();
    $("#cols").hide();
    $("#all").hide();
    $("#none").hide();
    $('#activate-step-2').hide();

    var tableOpts = {
        "sPaginationType": "full_numbers",
        "bFilter": false,
        "fnCreatedRow": function (nRow, aData, iDataIndex) {
            $(nRow).attr('id', table1.fnSettings().fnRecordsTotal());
            var txtBox = $(nRow).find("input[type=text]");
            var button = $(nRow).find("button");
        }
    }
    table1 = $('#csvTableNoHead').dataTable(tableOpts);


    var navListItems = $('ul.setup-panel li a'),
        allWells = $('.setup-content');
    allWells.hide();
    navListItems.click(function(e)
    {
        e.preventDefault();
        var $target = $($(this).attr('href')),
            $item = $(this).closest('li');
        if (!$item.hasClass('disabled')) {
            navListItems.closest('li').removeClass('active');
            $item.addClass('active');
            allWells.hide();
            if($target.selector == "#step-1") {
                $('#table1').DataTable().destroy();
                $('#table2').DataTable().destroy();
                $("#cols").change(function(){
                    $("#tableaucontenus1").html("");
                    $("#step33").removeClass('active');
                    $("#step33").addClass('disabled');
                    $("#step22").removeClass('active');
                    $("#step22").addClass('disabled');
                    $('#table1').DataTable().destroy();
                    $('#table2').DataTable().destroy();
                    $("#tableaucontenus2").html("");
                });
                $target.show();
            }else{
                $target.show();
            }
        }
    });
    $('ul.setup-panel li.active a').trigger('click');

    $('#form00').validate({ // initialize the plugin
        rules: {
            filePath1: {
                required: true
            }
        },
        messages: {
            filePath1: "Enter your File Path !",
        },
        highlight: function (element) {
            $(element).closest('.form-group').addClass('has-error');
            $('#step111').css('background-color','#ff0000');
        },
        unhighlight: function (element) {
            $(element).closest('.form-group').removeClass('has-error');
            $('#step111').css('background-color','');
        },
        errorElement: 'span',
        errorClass: 'help-block',
        errorPlacement: function (error, element) {
            if (element.parent('.input-group').length) {
                error.insertAfter(element.parent());
            } else {
                error.insertAfter(element);
            }
        }
    });
    $("#validate").on('click',
        function (e) {
            //envoyer les donnees avec ajax
            e.preventDefault();
            if($('#form00').valid()) {
                $('#cols').multiSelect({
                    selectableHeader: "<div class='custom-header'>Columns</div>",
                    selectionHeader: "<div class='custom-header'>Columns Selected</div>",
                    selectableFooter: "<div class='custom-header'>Columns</div>",
                    selectionFooter: "<div class='custom-header'>Columns Selected</div>"
                });
                var array = $('#filePath').val().split(".");
                console.log("array" + array + "lenghth" + array.length);
                var ext = array[array.length - 1];
                $('#columns').hide();
                if (ext == "csv") {
                    $.ajax({
                        type: "POST",//la method à utiliser soit POST ou GET
                        url: "/csvHeader", //lien de la servlet qui exerce le traitement sur les données
                        data: $('#form00').serialize(),// sign_in c'est l'id du form qui contient le bouton submit et toutes les champs à envoyer
                        dataType: 'json',
                        success: function (data) {// le cas ou la requete est bien execute en reçoi les données serialiser par JSON dans la variable msg
                            //recuperation de la valeur stock dans l'attribut desactive
                            for (var i = 0; i < data.length; i++) {
                                $('#cols').multiSelect('addOption', {
                                    value: data[i],
                                    text: data[i],
                                    selected: 'true'
                                });
                                $('#cols').multiSelect('refresh');
                            }
                            $('#all').show();
                            $('#none').show();
                            $('#cols').show();
                            $('#validate').hide();
                            $('#columns').show();
                        },
                        error: function () { //erreur dans le cas les données ne sont pas envoyer on affiche un message qui indique l'erreur
                            console.log("error");
                        }
                    });
                } else {
                    alert("Vous devez donner un fichier de format csv ");

                }
            }else{
                alert("form01 not valid ");
            }

        });

    $("#activate-step-2").click(
        function(e) {
            //envoyer les donnees avec ajax
            e.preventDefault();
            $("#tableaucontenus1").html("");
            var typeXMl;
            /* $('[name="xml[]"]').each( function (){
             if($(this).prop('checked') == true){
             typeXMl = $(this).val();
             }
             });*/
            var data2 = $('#form2').serialize();
            $.ajax({
                type: "POST",//la method à utiliser soit POST ou GET
                url: "/addAttributeReader", //lien de la servlet qui exerce le traitement sur les données
                dataType: 'json',
                data: data2,// sign_in c'est l'id du form qui contient le bouton submit et toutes les champs à envoyer
                success: function (data) {// le cas ou la requete est bien execute en reçoi les données serialiser par JSON dans la variable msg
                    //recuperation de la valeur stock dans l'attribut desactive
                    $('ul.setup-panel li:eq(1)').removeClass('disabled');
                    $('ul.setup-panel li a[href="#step-2"]').trigger('click');
                    $(this).remove();
                    for (var i = 0; i < data.length; i++) {
                        var ids =  'id['+i+']';
                        var cols = 'cols[' + i+ ']';
                        var type = 'type[' + i + ']';
                        var size = 'size[' + i+ ']';
                        var pk = 'pk[' + i+ ']';
                        var nonNull = 'nonNull[' + i + ']';
                        var defaultVal = 'defaultVal[' + i + ']';
                        var commentaire = 'commentaire[' + i+ ']';
                        var s;
                        s = "<td><select class='form-control' id='" + type + "' name='" + type + "'>" +
                            "<option class='blank'  value=''>Please select a value</option>" +
                            "<option title='Un nombre entier de 4 octets. La fourchette des entiers relatifs est de -2 147 483 648 à 2 147 483 647. Pour les entiers positifs, cest de 0 à 4 294 967 295' selected>NUMBER</option>" +
                            "<option title='Une chaîne de longueur variable (0-65,535), la longueur effective réelle dépend de la taille maximum d une ligne'>VARCHAR2</option>" +
                            "<option title='VARCHAR'>VARCHAR</option>" +
                            "<option title='DATE'>DATE</option>" +
                            "<option title='CHAR'>CHAR</option>" +
                            "<option title='Une date, la fourchette est de «1000-01-01» à «9999-12-31»'>DATE</option>" +
                            "<option title='FLOAT(24)'>FLOAT</option>" +
                            "<option title='BLOB'>BLOB</option>" +
                            "<option title='RAW'>RAW</option>" +
                            "<option title='CLOB'>CLOB</option>" +
                            "</optgroup>"+
                            "</select>"+
                            "</td>" +
                            "<td><input type='number' id='data[i].id' name='" + size + "'/> </td>";
                        $("#tableaucontenus1").append("<tr data-id='" + i + "'><td style='color: #63aef9;'>" + i + "<input type='hidden' id='"+ids+"' name='"+ids+"' value='"+i+"'/></td><td><input type='checkbox' id='" + pk + "' name='" + pk + "' value='primaryKey'/></td>"+
                            "<td>"+data[i]+"<input type='hidden' id='"+cols+"' name='"+cols+"' value='"+data[i]+"'/></td>" + s + "<td> <input type='checkbox' id='" + nonNull + "' name='" + nonNull + "' value='notNull'/></td>"+
                            "<td><input type='text' id='"+defaultVal+"' name='"+defaultVal+"' value=''/></td>"+
                            "<td><input type='text' id='"+commentaire+"' name='"+commentaire+"' value=''/></td>"+
                            "<td><button class='btn btn-danger'><span class='glyphicon glyphicon-remove-sign'></span></button>&nbsp;&nbsp;</td></tr>");
                    }
                    $('#table1').DataTable({
                        'fnClearTable':true,
                        "scrollY":        "500px",
                        "scrollCollapse": true
                    });
                    /*$('#table1_info').css("color","#63aef9");
                     $('#table1_next').css("background-color","#18bc9c");
                     $('#table1_previous').css("background-color","#18bc9c");
                     $('#table1_filter').css("background-color","#18bc9c","color","beige");
                     $('#table1_length').css("background-color","#18bc9c","color","beige");*/

                },
                error: function () { //erreur dans le cas les données ne sont pas envoyer on affiche un message qui indique l'erreur
                    console.log("error");
                    alert("Error Mapping ! make sure that you have selecte for all columns attributes or elements");
                }
            });
        });


    // DEMO ONLY //
    /*$('#activate-step-2').on('click', function(e) {
        $.post("/upload");
        $('ul.setup-panel li:eq(1)').removeClass('disabled');
        //$('ul.setup-panel li a[href="#step-2"]').trigger('click');
        $(this).remove();

    })*/
    
    $('#tableName').change(function (e) {
        $('#tableNameValidation').val($(this).val());
        if($('#tableName').val()=="" || $('#tableName').val()==" "){
            $('#consulter').prop("disabled", true);
            $("#dropTable").bootstrapSwitch('state', false);
            $("#dropTable").bootstrapSwitch('toggleDisabled',true,true);
        }else {
            $('#consulter').prop("disabled", false);
            $('#dropTab').show();
            $("#dropTable").bootstrapSwitch('disabled',false);
            $("#dropTable").bootstrapSwitch('state', true);
            var tableName=$('#tableName').val();
            $.ajax({
                type : "post",
                url : "metadata", //process to mail
                data :{
                    tableName:tableName
                },
                success : function(response) {
                    console.log(response[0].col);
                    if(response[0].col == "existe" ){
                        $('#consulter').css("background-color","#ff010f");
                        $('#consulter1').css("background-color","#ff010f");
                        $('#tableSpan').text("Table ( "+$('#tableName').val() +" ) valide");
                        $('#tableSpan').css('color','#18bc9c');
                        $('#tableSpan1').text("Table ( "+$('#tableName').val() +" ) valide");
                        $('#tableSpan1').css('color','#18bc9c');
                        $("#dropTable").bootstrapSwitch('state', false);
                        $("#dropTable").bootstrapSwitch('toggleDisabled',true,true);
                    }else {
                        $('#consulter').css("background-color","#1eff00");
                        $('#consulter1').css("background-color","#1eff00");
                        $('#tableSpan').text("Table ( "+ $('#tableName').val()+" ) existe dejà veuillez consulter sa structure !");
                        $('#tableSpan').css('color','#ff0000');
                        $('#tableSpan1').text("Table ( "+ $('#tableName').val()+" ) existe dejà veuillez consulter sa structure !");
                        $('#tableSpan1').css('color','#ff0000');
                        $("#titreModal").html("Information sur la table " + tableName);
                        $("#dropTable").bootstrapSwitch('disabled',false);
                        $("#dropTable").bootstrapSwitch('state', true);
                        var contenu = "";
                        contenu += "<fieldset>" +
                            "<table id='infoTable' width='100%' class='table table-bordered'>" +
                            "<thead><tr><th style='color: blue;'><b>Colonne</b></th><th style='color: blue';><b>Type</b></th></thead><tbody>";
                        for (var i = 0; i < response.length; i++) {
                            contenu += "<tr><td>" + response[i].col + "</td><td>" + response[i].type + "</td></tr>";
                        }
                        contenu += "</tbody></table>" +
                            "</fieldset>";

                        $("#contenu").html(contenu);
                        $('#infoTable').DataTable({
                            'fnClearTable': true,
                            "scrollY": "500px",
                            "scrollCollapse": true
                        });
                    }
                },
                error : function() {
                    console.log("erreur");
                }
            });
        }
    });
    
    $('#entete').on('switchChange.bootstrapSwitch', function(event, state){
        console.log("yeah");
        if(state == false) {
            $.ajax({
                type: "POST",//la method à utiliser soit POST ou GET
                url: "/nbColCsvNoHead", //lien de la servlet qui exerce le traitement sur les données
                data: $("#form00").serialize(),// sign_in c'est l'id du form qui contient le bouton submit et toutes les champs à envoyer
                success: function (data) {// le cas ou la requete est bien execute en reçoi les données serialiser par JSON dans la variable msg
                    //recuperation de la valeur stock dans l'attribut desactive
                    data.size;

                    for (var i = 0; i < data.size; i++) {
                        var id = '<input type="hidden" class="nami" name="idCol[' + i + ']" value="' + i + '" readonly/>';
                        var textbox = '<input type="text" class="txtBox" id="cols[' + i + ']" name="cols[' + i + ']" placeholder="Column ' + i + '" name="cols[' + i + ']" required  /><input type="hidden" class="nami" name="idCol[' + i + ']" value="' + i+ '" readonly/>';
                        var button = '<button class="btn btn-danger"><span class="glyphicon glyphicon-remove-sign"></span></button>';
                        table1.fnAddData([textbox, button]);
                    }
                    $("#csvNoHead").show();
                },
                error: function () { //erreur dans le cas les données ne sont pas envoyer on affiche un message qui indique l'erreur
                    console.log("error");
                }
            });
        }else{
            table1.fnClearTable();
            $("#csvNoHead").hide();

        }
        
    });
    ///PARAM 1 CSV NO HEAD

    $("#validateNoHead").on('click',
        function (e) {
            //envoyer les donnees avec ajax
            e.preventDefault();
            if($('#form00').valid()) {
                var data2 = table1.$('input').serialize();
                var data = $('#form00').serialize().concat(data2);
                $('#cols').multiSelect({
                    selectableHeader: "<div class='custom-header'>Columns</div>",
                    selectionHeader: "<div class='custom-header'>Columns Selected</div>",
                    selectableFooter: "<div class='custom-header'>Columns</div>",
                    selectionFooter: "<div class='custom-header'>Columns Selected</div>"
                });
                var array = $('#filePath1').val().split(".");
                console.log("array" + array + "lenghth" + array.length);
                var ext = array[array.length - 1];
                $('#columns').hide();
                if (ext == "csv") {
                    $.ajax({
                        type: "POST",//la method à utiliser soit POST ou GET
                        url: "/csvNoHead", //lien de la servlet qui exerce le traitement sur les données
                        data: data,// sign_in c'est l'id du form qui contient le bouton submit et toutes les champs à envoyer
                        dataType: 'json',
                        success: function (data) {// le cas ou la requete est bien execute en reçoi les données serialiser par JSON dans la variable msg
                            //recuperation de la valeur stock dans l'attribut desactive
                            console.log("data " + data);
                            for (var i = 0; i < data.length; i++) {
                                $('#cols').multiSelect('addOption', {
                                    value: data[i],
                                    text: data[i],
                                    selected: 'true'
                                });
                                $('#cols').multiSelect('refresh');
                            }
                            $('#all').show();
                            $('#none').show();
                            $('#cols').show();
                            $('#validateNoHead').hide();
                            $('#columns').show();
                        },
                        error: function () { //erreur dans le cas les données ne sont pas envoyer on affiche un message qui indique l'erreur
                            console.log("error");
                        }
                    });
                } else {
                    alert("Vous devez donner un fichier de type csv");

                }
            }else{
                alert("form not valid veuillez remplir tout les champs ");
            }

        });



    ///PARAM 1 CSV NO HEAD


    //PARAM 2 XML


    $("#validateXml").on('click',
        function (e) {
            //envoyer les donnees avec ajax
            e.preventDefault();
            if($('#form00').valid()) {
                var data2 = table1.$('input').serialize();
                var data = $('#form00').serialize().concat(data2);
                $('#cols').multiSelect({
                    selectableHeader: "<div class='custom-header'>Columns</div>",
                    selectionHeader: "<div class='custom-header'>Columns Selected</div>",
                    selectableFooter: "<div class='custom-header'>Columns</div>",
                    selectionFooter: "<div class='custom-header'>Columns Selected</div>"
                });
                var array = $('#filePath1').val().split(".");
                console.log("array" + array + "lenghth" + array.length);
                var ext = array[array.length - 1];
                $('#columns').hide();
            if (ext == "xml") {
                    $.ajax({
                        type: "POST",//la method à utiliser soit POST ou GET
                        url: "/colsxml", //lien de la servlet qui exerce le traitement sur les données
                        data: data,// sign_in c'est l'id du form qui contient le bouton submit et toutes les champs à envoyer
                        dataType: 'json',
                        success: function (data) {// le cas ou la requete est bien execute en reçoi les données serialiser par JSON dans la variable msg
                            //recuperation de la valeur stock dans l'attribut desactive
                            console.log("data " + data);
                            for (var i = 0; i < data.length; i++) {
                                $('#cols').multiSelect('addOption', {
                                    value: data[i],
                                    text: data[i],
                                    selected: 'true'
                                });
                                $('#cols').multiSelect('refresh');
                            }
                            $('#all').show();
                            $('#none').show();
                            $('#cols').show();
                            $('#validateNoHead').hide();
                            $('#columns').show();
                        },
                        error: function () { //erreur dans le cas les données ne sont pas envoyer on affiche un message qui indique l'erreur
                            console.log("error");
                        }
                    });
                } else {
                    alert("form01 not valid ");
                }
            }else{
                alert('Le fichier doit etre de format xml');
            }

        });
    
    //PARAM 2 XML

    var entete;

        $('#form01').validate({ // initialize the plugin
            rules: {
                separator: {
                    required: true
                },  	        	
                numberLine: {
                    required: true
                }
            },
            messages: {
                separator: "Enter your separtor !",
                numberLine: "Enter the number of Lines that you will be skipped in your file"
            },
            highlight: function (element) {
                $(element).closest('.form-group').addClass('has-error');
                $('#step111').css('background-color','#ff0000');
            },
            unhighlight: function (element) {
                $(element).closest('.form-group').removeClass('has-error');
                $('#step111').css('background-color','');
            },
            errorElement: 'span',
            errorClass: 'help-block',
            errorPlacement: function (error, element) {
                if (element.parent('.input-group').length) {
                    error.insertAfter(element.parent());
                } else {
                    error.insertAfter(element);
                }
            }
        });


    $('#form001').validate({ // initialize the plugin
        rules: {
            separator: {
                required: true
            }
        },
        highlight: function (element) {
            $(element).closest('.form-group').addClass('has-error');
            $('#step222').css('background-color','#ff0000');
        },
        unhighlight: function (element) {
            $(element).closest('.form-group').removeClass('has-error');
            $('#step222').css('background-color','');
        },
        errorElement: 'span',
        errorClass: 'help-block',
        errorPlacement: function (error, element) {
            if (element.parent('.input-group').length) {
                error.insertAfter(element.parent());
            } else {
                error.insertAfter(element);
            }
        }
    });





    $(function() {
        $("#form001").submit(
            function (e) {
                //envoyer les donnees avec ajax
                e.preventDefault();
                if($('#form001').valid() && $('#form01').valid() && table1.fnGetData().length>0) {
                    var data1="separator="+$('#separator').find(":selected").text()+"&";
                    var data2 = table1.$('input').serialize();
                    var data= data1.concat(data2);
                    $.ajax({
                        type: "POST",//la method à utiliser soit POST ou GET
                        url: "/cols001", //lien de la servlet qui exerce le traitement sur les données
                        data: data,// sign_in c'est l'id du form qui contient le bouton submit et toutes les champs à envoyer
                        dataType: 'json',
                        success: function (data) {// le cas ou la requete est bien execute en reçoi les données serialiser par JSON dans la variable msg
                            //recuperation de la valeur stock dans l'attribut desactive
                            var obj = JSON.parse(JSON.stringify(data));
                            var s = parseInt(data.length);
                            var sal;
                            for (var i = 0; i < s; i++) {
                                sal = obj[i].name;
                                //$("#cols").append($('<option>', {value:"ok"}).text("ok"));
                                //$("#cols").append("<option value='" + data[i].name + "'>" + data[i].name + "</option>");
                                $('#all').show();
                                $('#none').show();
                                $('#cols').multiSelect({
                                    selectableHeader: "<div class='custom-header'>Columns</div>",
                                    selectionHeader: "<div class='custom-header'>Columns Selected</div>",
                                    selectableFooter: "<div class='custom-header'>Columns</div>",
                                    selectionFooter: "<div class='custom-header'>Columns Selected </div>"
                                });
                                $('#cols').multiSelect('addOption', {
                                    value: sal,
                                    text: sal,
                                    selected: 'true'
                                });
                                $('#cols').multiSelect('refresh');
                            }

                            $('#cols').show();
                            $('#columns').show();
                        },
                        error: function () { //erreur dans le cas les données ne sont pas envoyer on affiche un message qui indique l'erreur
                            console.log("error");
                        }
                    });
                }else{
                    alert("No Column was inserted in this table");
                }

            });
    });
    
    $("#activate-step-3").click(function(e) {
        console.log("cclik");
           e.preventDefault();
                if ($("#tableaucontenus1").children().length > 0) {
                    var table = $('#table1').DataTable();
                    var data0 = table.$('input, select').serialize();
                    var data1 = $('#tableName').serialize()+"&";
                    var data2 = data1.concat(data0);
                    var data3=  "dropeTable="+$("#dropTable").bootstrapSwitch('state')+"&";
                    var data4 = data3.concat(data2);
                    $.ajax({
                        type: "POST",//la method à utiliser soit POST ou GET
                        url: "/getTypes", //lien de la servlet qui exerce le traitement sur les données
                        data: data4,// sign_in c'est l'id du form qui contient le bouton submit et toutes les champs à envoyer
                        success: function (data) {// le cas ou la requete est bien execute en reçoi les données serialiser par JSON dans la variable msg
                                $("#contenus").html("");
                            var currentDate = moment().format("DD-MM-YYYY");
                                var contenu = "<div class='panel panel-success'><div class='panel-heading'><div class='pad margin no-print'>"
                                    + "<div class='callout callout-success' style='margin-bottom: 0!important;'>"+
                            "<h4><i class='fa fa-info'></i> SUCCESS:</h4> <span class='label label-info'> Table   :  "+$('#tableName').val()+"</span>&nbsp;&nbsp;&nbsp;&nbsp;<span class='label label-info'>  Date        :        "+currentDate+"</span></div></div><div class='panel-body'><fieldset>" +
                                    "<table id='myInputErrors' width='100%' class='table .table-bordered'>" +
                                    "<thead><tr><th><b>Path File</b></th><th ><b>Separator</b></th><th><b>lineSkipped </b></th></thead><tbody>"+
                                    "<tr><td><span class='label label-primary'>"+$('#filePath').val()+"</span></td><td><span class='label label-danger'>"+$('#separator').val()+"</span></td><td><span class='label label-danger'>"+$('#nbLineToSkip').val()+"</span></td></tr></tbody></table></fieldset>"+
                                        "<fieldset>"+
                                    "<table class='table table-bordered' id='table2' name='table2'> <thead style='background: #3c8dbc'><tr> " +
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
                                    q= "<td><span class='label label-primary'><input type='checkbox'  checked='checked' disabled></span></td>";
                                }else{
                                    console.log("FALSEE");
                                    q = "<td><input type='checkbox'  disabled></td>";
                                }
                                if(data[i].nonNull == true){
                                    s= "<td><input type='checkbox'  checked='checked' disabled></td>";
                                }else{
                                    s = "<td><input type='checkbox'  disabled></td>";
                                }
                                contenu += "<tr><td><span class='label label-danger'>"+i+"</span></td>"+q+"<td><span class='label label-info'>"+data[i].nameo+"</span></td><td><span class='label label-info'>"+data[i].type+"</span></td><td><span class='label label-success'>"+data[i].sizeo+"</span></td>"+s+"<td>"+data[i].defaut+"</td><td>"+data[i].commentaires+"</td></td></tr>";
                            }
                            contenu+="</tbody></table></fieldset>";
                            $('#table2').DataTable({
                                "scrollY":        "500px",
                                "scrollCollapse": true
                            });
                                $('#contenus').append(contenu);
                                $('#Modalx').modal('show');
                        },
                        error: function () { //erreur dans le cas les données ne sont pas envoyer on affiche un message qui indique l'erreur
                            console.log("error");
                        }
                    });
                } else {
                    var r = confirm("Vous devez séléctionner au moins une colonne ! voulez vous vous diriger vers Config Columns ?");
                    if (r == true) {
                        $('ul.setup-panel li a[href="#step-1"]').trigger('click');
                    }
                }

        });


    $(".close").on('click',function () {
        window.location.replace("/home");
    });






    $("#cols").change(function(e){
        e.preventDefault();
        if($("#cols").val()!=null) {
            $('#activate-step-2').show();
            /*if(typeXMl=="type3") {
                var cols = $('#cols').val()+'';
                var tabCols = cols.split(",");
                $('#xmlTableContenu1').html("");
                for(var i = 0;i<$('#cols :selected').length;i++){
                    var attributes = 'attributes[' + i+ ']';
                    var elements = 'elements[' + i+ ']';
                    $('#xmlTableContenu1').append("<tr style='background-color: white'  data-id='"+i+"'><td>" + tabCols[i] + "</td><td><input type='checkbox' id='"+attributes+"' name='"+attributes+"' value='"+attributes+"'></td><td><input type='checkbox' id='"+elements+"' name='"+elements+"' value='"+elements+"' ></td></tr>");
                }
                $('#type3').show();
                console.log("val cols "+ $('#cols').val());
                console.log("length cols"+$('#cols :selected').length);
            }*/
            $('#validate').hide();
        }else {
            $('#type3').hide();
            $('#activate-step-2').hide();
        }
    });

    $('#dropTable1').bootstrapSwitch('disabled',true);

    $('#dropTable').on('switchChange.bootstrapSwitch', function(event, state){
        //event.preventDefault();

        if(state == true){
            $('#dropTable1').bootstrapSwitch('disabled',false);
            $("#dropTable1").bootstrapSwitch('state',true);
            $('#dropTable1').bootstrapSwitch('disabled',true);
        }else{
            $('#dropTable1').bootstrapSwitch('disabled',false);
            $("#dropTable1").bootstrapSwitch('state',false);
            $('#dropTable1').bootstrapSwitch('disabled',true);
        }
    });

    $('#precedent').click(function (e) {
        e.preventDefault();
        $('ul.setup-panel li a[href="#step-1"]').trigger('click');
    });
    $('#precedent1').click(function (e) {
        e.preventDefault();
        $('ul.setup-panel li a[href="#step-2"]').trigger('click');
    });

    $("#thanks").click(function (e) {
        location.reload();
    });


    $("#all").click(function (e) {
        e.preventDefault();
        $("#cols").multiSelect('select_all');

    });

    $("#none").click(function(e){
        e.preventDefault();
        $('#xmlTableContenu1').html("");
        $('#type3').hide();
        $("#cols").multiSelect('deselect_all');
    });


   /* $('#table1').bind("DOMSubtreeModified",function (e) {
        e.preventDefault();
        if($('#tableaucontenus1').children().length==0){
            $('ul.setup-panel li a[href="#step-1"]').trigger('click');
            $("#cols").multiSelect('deselect_all');
            $("#step22").removeClass('active');
            $("#step22").addClass('disabled');
        }

    })*/

    /*$('#table1').on('click', '.glyphicon-remove-sign', function(e){
        var r = confirm("Voulez vous vraiement supprimer ?");
        if (r == true) {
            $(this).closest('tr').remove();
            var id =  $(this).closest('tr').data('id');
            document.getElementById("table1").deleteRow(id);
            console.log("id = "+id);
            $.ajax({
                type : "GET",//la method à utiliser soit POST ou GET
                url : "/delete?id="+id, //lien de la servlet qui exerce le traitement sur les données// sign_in c'est l'id du form qui contient le bouton submit et toutes les champs à envoyer
                success : function(data) {// le cas ou la requete est bien execute en reçoi les données serialiser par JSON dans la variable msg
                    //recuperation de la valeur stock dans l'attribut desactive
                    $("#step33").removeClass('active');
                    $("#step33").addClass('disabled');
                },
                error : function() { //erreur dans le cas les données ne sont pas envoyer on affiche un message qui indique l'erreur
                    console.log("error");
                }
            });
        } else {
            console.log("ss");
        }
    });*/

    $('#table1').on( 'click', '.glyphicon-remove-sign', function () {
        var table = $('#table1').DataTable();
        table
            .row( $(this).parents('tr') )
            .remove()
            .draw();
    } );


   /* $('#table1').on('click','input[type="checkbox"]', function(e) {
        var id =  $(this).closest('tr').data('id');
        var rowCount = $('#table1 tr').length;
        console.log("type= "+ $('#type\\['+id+'\\] option:selected').text());
        if($('#type\\['+id+'\\] option:selected').text() != 'INT'){
            alert("Impossible de choisir un "+$('#type\\['+id+'\\]').val()+"Auto Increment");
            $('#autoIncrement\\[' + id + '\\]').prop('checked',false);
        }else {
            for (var i = 0; i < rowCount; i++) {
                if (i != id) {
                    $('#autoIncrement\\[' + i + '\\]').prop('checked', false);
                }
            }
        }
    });*/

    $(function () {
        $('.btn-radio').click(function(e) {
            $('.btn-radio').not(this).removeClass('active')
                .siblings('input').prop('checked',false)
                .siblings('.img-radio').css('opacity','0.5');
            $(this).addClass('active')
                .siblings('input').prop('checked',true)
                .siblings('.img-radio').css('opacity','1');
        });
    });


    $('#xmlTable').on('click','input[type="checkbox"]', function(e) {
        var id =  $(this).closest('tr').data('id');
        var rowCount = $('#xmlTable tr').length;

        var c = "attributes["+id+"]";
        //console.log("type= "+ $('#type\\['+id+'\\] option:selected').text());
        //if($('#type\\['+id+'\\] option:selected').text() != 'INT'){
        if($(this).val() == c){
            $('#elements\\['+id+'\\]').prop('checked', false);
        }else{
            $('#attributes\\['+id+'\\]').prop('checked', false);
        }
        //

        //$('#autoIncrement\\[' + i + '\\]').prop('checked', false);

    });


    $('.consulter').on('click',function(e){
        e.preventDefault();
        var tableName=$('#tableName').val();
        $.ajax({
            type : "post",
            url : "metadata", //process to mail
            data :{
                tableName:tableName
            },
            success : function(data) {
                $("#contenus").html("");
                var contenu ="";
                contenu += "<fieldset>" +
                    "<table id='infoClasse' width='100%' class='table .table-bordered'>" +
                    "<thead><tr><th><b> Column </b></th><th ><b>Type</b></th></thead><tbody>";
                var type="";
                for (var i = 0; i < data.length; i++) {
                    contenu += "<tr><td>" + data[i].col + "</td><td>" + data[i].type  + "</td></tr>";
                }
                contenu += "</tbody></table></fieldset>";
                $("#contenus").append(contenu);
                $("#Modalx").modal("show");
            },
            error : function() {
                console.log("erreur");
            }
        });

    });



    function removeRow(removeNum) {
        $('#rowCount'+removeNum).remove();
    }


    $('#csvTableNoHead').on( 'click', '.glyphicon-remove-sign', function (e) {
        e.preventDefault();
        var table = $('#csvTableNoHead').DataTable();
        table.row( $(this).parents('tr') ).remove().draw();
        $('.nami').each(function (i){
            $(this).val(i);
            $(this).attr('name','idCol['+i+']');
        });
        $('.txtBox').each(function (i){
            $(this).attr('name','col['+i+']');
        });

    });
    
    /*$('#table2').on('click', '.glyphicon-pencil', function(e){
        //var id = $(this).closest('tr').data('id');
        //$('#md\\['+id+'\\]').click(function(e) {
        var r = confirm("Voulez vous vraiement Modifier ?");
            $('ul.setup-panel li a[href="#step-2"]').trigger('click');
        e.preventDefault();
           /* if (($(this).val()) == "modifier") {
                console.log("case modifie" + id);
                console.log("modifier");
                console.log("idPencil" + id);
                $(this).val("enregistre");
                $('#size\\['+id+'\\]').prop("disabled", false);
                $('#type\\['+id+'\\]').prop("disabled", false);
                $(this).removeClass('btn btn-warning');
                $(this).addClass("btn btn-success");
            } else {
                console.log("case Enregistre" + id);
                if(($(this).val()) == "enregistre"){
                    $(this).val("modifier");
                    $(this).removeClass('btn btn-success');
                    $(this).addClass("btn btn-warning");
                    $('#size\\['+id+'\\]').prop("disabled", true);
                    $('#type\\['+id+'\\]').prop("disabled", true);
                }
                console.log("none");
            }
        //});
        });*/

        /*var r = confirm("Voulez vous vraiement modifier ?");
       / if (r == true) {
            console.log($(this));
            $(this).closest('tr').remove();

        } else {
            console.log("ss");
        }*/

   
});


$(document).ready(function() {
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
                $("#cols").change(function(){
                    $("#tableaucontenus1").html("");
                    $("#step33").removeClass('active');
                    $("#step33").addClass('disabled');
                    $("#step22").removeClass('active');
                    $("#step22").addClass('disabled');
                    $("#tableaucontenus2").html("");
                });
                $target.show();
            }else{
                    $target.show();
            }
        }
    });
    $('ul.setup-panel li.active a').trigger('click');
    // DEMO ONLY //
    /*$('#activate-step-2').on('click', function(e) {
        $.post("/upload");
        $('ul.setup-panel li:eq(1)').removeClass('disabled');
        //$('ul.setup-panel li a[href="#step-2"]').trigger('click');
        $(this).remove();

    })*/
    $(function() {
        $("form").submit(
            function (e) {
                //envoyer les donnees avec ajax
                e.preventDefault();
                $.ajax({
                    type: "POST",//la method à utiliser soit POST ou GET
                    url: "/cols", //lien de la servlet qui exerce le traitement sur les données
                    data: $('#form').serialize(),// sign_in c'est l'id du form qui contient le bouton submit et toutes les champs à envoyer
                    dataType: 'json',
                    success: function (data) {// le cas ou la requete est bien execute en reçoi les données serialiser par JSON dans la variable msg
                        //recuperation de la valeur stock dans l'attribut desactive
                        var obj = JSON.parse(JSON.stringify(data));
                        var s = parseInt(data.length);
                        var sal;
                        for (var i = 0;i<s; i++) {
                             sal = obj[i].name;
                            //$("#cols").append($('<option>', {value:"ok"}).text("ok"));
                            //$("#cols").append("<option value='" + data[i].name + "'>" + data[i].name + "</option>");
                            $('#cols').show();
                            $('#all').show();
                            $('#none').show();
                            $('#cols').multiSelect({
                                selectableHeader: "<div class='custom-header'>Columns</div>",
                                selectionHeader: "<div class='custom-header'>Columns Selected</div>",
                                selectableFooter: "<div class='custom-header'>Columns</div>",
                                selectionFooter: "<div class='custom-header'>Columns Selected footer</div>"
                            });

                            $('#cols').multiSelect('addOption', {
                                value: sal,
                                text: sal,
                                selected: 'true'
                            });
                            $('#cols').multiSelect('refresh');
                        }
                    },
                    error: function () { //erreur dans le cas les données ne sont pas envoyer on affiche un message qui indique l'erreur
                        console.log("error");
                    }
                });

            });
    });

    $("#activate-step-2").click(
        function(e) {
            //envoyer les donnees avec ajax
            e.preventDefault();
            $.ajax({
                type : "POST",//la method à utiliser soit POST ou GET
                url : "/upload", //lien de la servlet qui exerce le traitement sur les données
                dataType: 'json',
                data : $('#form2').serialize(),// sign_in c'est l'id du form qui contient le bouton submit et toutes les champs à envoyer
                success : function(data) {// le cas ou la requete est bien execute en reçoi les données serialiser par JSON dans la variable msg
                    //recuperation de la valeur stock dans l'attribut desactive
                    $('ul.setup-panel li:eq(1)').removeClass('disabled');
                    $('ul.setup-panel li a[href="#step-2"]').trigger('click');
                    $(this).remove();
                    var obj = JSON.parse(JSON.stringify(data));
                    var le = parseInt(data.length);
                    for(var i=0;i<le;i++) {
                        var type = 'type['+data[i].id+']';
                        var size = 'size['+data[i].id+']';
                        var s;
                        if(i==0) {
                            s ="<td><select class='form-control' id='type' name='" + type + "'>" +
                                "<option class='blank'  value=''>Please select a value</option>" +
                                "<option title='Un nombre entier de 4 octets. La fourchette des entiers relatifs est de -2 147 483 648 à 2 147 483 647. Pour les entiers positifs, cest de 0 à 4 294 967 295' selected>INT</option>" +
                                "<option title='Une chaîne de longueur variable (0-65,535), la longueur effective réelle dépend de la taille maximum d'une ligne'>VARCHAR</option>" +
                                "<option title='Une colonne TEXT d une longueur maximum de 65 535 (2^16 - 1) caractères, stockée avec un préfixe de deux octets indiquant la longueur de la valeur en octets'>TEXT</option>" +
                                "<option title='Une date, la fourchette est de «1000-01-01» à «9999-12-31»'>DATETIME</option>" +
                                "<optgroup label='Numérique'>" +
                                "<option title='Un nombre entier de 1 octet. La fourchette des nombres avec signe est de -128 à 127. Pour les nombres sans signe, c est de 0 à 255'>TINYINT</option>" +
                                "<option title='Un nombre entier de 2 octets. La fourchette des nombres avec signe est de -32 768 à 32 767. Pour les nombres sans signe, c est de 0 à 65 535'>SMALLINT</option>" +
                                "<option title='Un nombre entier de 3 octets. La fourchette des nombres avec signe est de -8 388 608 à 8 388 607. Pour les nombres sans signe, c est de 0 à 16 777 215'>MEDIUMINT</option>" +
                                "<option title='Un nombre entier de 4 octets. La fourchette des entiers relatifs est de -2 147 483 648 à 2 147 483 647. Pour les entiers positifs, c est de 0 à 4 294 967 295'>INT</option>" +
                                "<option title='Un nombre entier de 8 octets. La fourchette des nombres avec signe est de -9 223 372 036 854 775 808 à 9 223 372 036 854 775 807. Pour les nombres sans signe, c est de 0 à 18 446 744 073 709 551 615'>BIGINT</option>" +
                                "</optgroup></select>" +
                                "</td>" +
                                "<td><input type='number' id='data[i].id' name='" + size + "' size='4' value='10'/> </td>";
                        }
                        if (i == 1) {
                            s=  "<td><select class='form-control' id='type' name='" + type + "'>" +
                                "<option class='blank'  value=''>Please select a value</option>" +
                                "<option title='Un nombre entier de 4 octets. La fourchette des entiers relatifs est de -2 147 483 648 à 2 147 483 647. Pour les entiers positifs, cest de 0 à 4 294 967 295' >INT</option>" +
                                "<option title='Une chaîne de longueur variable (0-65,535), la longueur effective réelle dépend de la taille maximum d une ligne' selected >VARCHAR</option>" +
                                "<option title='Une colonne TEXT dune longueur maximum de 65 535 (2^16 - 1) caractères, stockée avec un préfixe de deux octets indiquant la longueur de la valeur en octets'>TEXT</option>" +
                                "<option title='Une date, la fourchette est de «1000-01-01» à «9999-12-31»'>DATETIME</option>" +
                                "<optgroup label='Numérique'>" +
                                "<option title='Un nombre entier de 1 octet. La fourchette des nombres avec signe est de -128 à 127. Pour les nombres sans signe, c est de 0 à 255'>TINYINT</option>" +
                                "<option title='Un nombre entier de 2 octets. La fourchette des nombres avec signe est de -32 768 à 32 767. Pour les nombres sans signe, c est de 0 à 65 535'>SMALLINT</option>" +
                                "<option title='Un nombre entier de 3 octets. La fourchette des nombres avec signe est de -8 388 608 à 8 388 607. Pour les nombres sans signe, c est de 0 à 16 777 215'>MEDIUMINT</option>" +
                                "<option title='Un nombre entier de 4 octets. La fourchette des entiers relatifs est de -2 147 483 648 à 2 147 483 647. Pour les entiers positifs, c est de 0 à 4 294 967 295'>INT</option>" +
                                "<option title='Un nombre entier de 8 octets. La fourchette des nombres avec signe est de -9 223 372 036 854 775 808 à 9 223 372 036 854 775 807. Pour les nombres sans signe, c est de 0 à 18 446 744 073 709 551 615'>BIGINT</option>" +
                                "</optgroup></select>" +
                                "</td>" +
                                "<td><input type='number' id='data[i].id' name='" + size + "' size='4' value='255'/> </td>";
                        }
                        if (i == 2) {
                            s= "<td><select class='form-control' id='type' name='" + type + "'>" +
                                "<option class='blank'  value=''>Please select a value</option>" +
                                "<option title='Un nombre entier de 4 octets. La fourchette des entiers relatifs est de -2 147 483 648 à 2 147 483 647. Pour les entiers positifs, cest de 0 à 4 294 967 295' >INT</option>" +
                                "<option title='Une chaîne de longueur variable (0-65,535), la longueur effective réelle dépend de la taille maximum d une ligne' selected>VARCHAR</option>" +
                                "<option title='Une colonne TEXT d une longueur maximum de 65 535 (2^16 - 1) caractères, stockée avec un préfixe de deux octets indiquant la longueur de la valeur en octets'>TEXT</option>" +
                                "<option title='Une date, la fourchette est de «1000-01-01» à «9999-12-31»'>DATETIME</option>" +
                                "<optgroup label='Numérique'>" +
                                "<option title='Un nombre entier de 1 octet. La fourchette des nombres avec signe est de -128 à 127. Pour les nombres sans signe, c est de 0 à 255'>TINYINT</option>" +
                                "<option title='Un nombre entier de 2 octets. La fourchette des nombres avec signe est de -32 768 à 32 767. Pour les nombres sans signe, c est de 0 à 65 535'>SMALLINT</option>" +
                                "<option title='Un nombre entier de 3 octets. La fourchette des nombres avec signe est de -8 388 608 à 8 388 607. Pour les nombres sans signe, c est de 0 à 16 777 215'>MEDIUMINT</option>" +
                                "<option title='Un nombre entier de 4 octets. La fourchette des entiers relatifs est de -2 147 483 648 à 2 147 483 647. Pour les entiers positifs, c est de 0 à 4 294 967 295'>INT</option>" +
                                "<option title='Un nombre entier de 8 octets. La fourchette des nombres avec signe est de -9 223 372 036 854 775 808 à 9 223 372 036 854 775 807. Pour les nombres sans signe, c est de 0 à 18 446 744 073 709 551 615'>BIGINT</option>" +
                                "</optgroup></select>" +
                                "</td>" +
                                "<td><input type='number' id='data[i].id' name='" + size + "' size='4' value='255'/> </td>";
                        }
                        if (i == 3) {
                            s = "<td><select class='form-control' id='type' name='" + type + "'>" +
                                "<option class='blank'  value=''>Please select a value</option>" +
                                "<option title='Un nombre entier de 4 octets. La fourchette des entiers relatifs est de -2 147 483 648 à 2 147 483 647. Pour les entiers positifs, cest de 0 à 4 294 967 295'>INT</option>" +
                                "<option title='Une chaîne de longueur variable (0-65,535), la longueur effective réelle dépend de la taille maximum d une ligne'>VARCHAR</option>" +
                                "<option title='Une colonne TEXT d une longueur maximum de 65 535 (2^16 - 1) caractères, stockée avec un préfixe de deux octets indiquant la longueur de la valeur en octets'>TEXT</option>" +
                                "<option title='Une date, la fourchette est de «1000-01-01» à «9999-12-31»' selected>DATETIME</option>" +
                                "<optgroup label='Numérique'>" +
                                "<option title='Un nombre entier de 1 octet. La fourchette des nombres avec signe est de -128 à 127. Pour les nombres sans signe, c est de 0 à 255'>TINYINT</option>" +
                                "<option title='Un nombre entier de 2 octets. La fourchette des nombres avec signe est de -32 768 à 32 767. Pour les nombres sans signe, c est de 0 à 65 535'>SMALLINT</option>" +
                                "<option title='Un nombre entier de 3 octets. La fourchette des nombres avec signe est de -8 388 608 à 8 388 607. Pour les nombres sans signe, c est de 0 à 16 777 215'>MEDIUMINT</option>" +
                                "<option title='Un nombre entier de 4 octets. La fourchette des entiers relatifs est de -2 147 483 648 à 2 147 483 647. Pour les entiers positifs, c est de 0 à 4 294 967 295'>INT</option>" +
                                "<option title='Un nombre entier de 8 octets. La fourchette des nombres avec signe est de -9 223 372 036 854 775 808 à 9 223 372 036 854 775 807. Pour les nombres sans signe, c est de 0 à 18 446 744 073 709 551 615'>BIGINT</option>" +
                                "</optgroup></select>" +
                                "</td>" +
                                "<td><input type='number' id='data[i].id' name='" + size + "' size='4' value='5'/> </td>";
                        }
                        $("#tableaucontenus1").append("<tr data-id='"+data[i].id+"'><td>" + data[i].id + "</td><td>" + data[i].name + "</td>" + s +"<td><button class='btn btn-danger'><span class='glyphicon glyphicon-remove-sign'></span></button>&nbsp;&nbsp;</tr>");
                    }

                },
                error : function() { //erreur dans le cas les données ne sont pas envoyer on affiche un message qui indique l'erreur
                    console.log("error");
                }
            });

        });


    $("#activate-step-3").click(
        function(e) {
            $("#tableaucontenus2").html("");
            if($("#tableaucontenus1").children().length>0) {
                //envoyer les donnees avec ajax
                e.preventDefault();
                $.ajax({
                    type: "POST",//la method à utiliser soit POST ou GET
                    url: "/getTypes", //lien de la servlet qui exerce le traitement sur les données
                    dataType: 'json',
                    data: $('#form1').serialize(),// sign_in c'est l'id du form qui contient le bouton submit et toutes les champs à envoyer
                    success: function (data) {// le cas ou la requete est bien execute en reçoi les données serialiser par JSON dans la variable msg
                        //recuperation de la valeur stock dans l'attribut desactive
                        $('ul.setup-panel li:eq(2)').removeClass('disabled');
                        $('ul.setup-panel li a[href="#step-3"]').trigger('click');
                        $(this).remove();
                        for (var i = 0; i < data.length; i++) {
                            var type = 'type[' + data[i].id + ']';
                            var size = 'size[' + data[i].id + ']';
                            var md = 'md[' + data[i].id + ']';
                            $("#tableaucontenus2").append("<tr data-id='" + data[i].id + "'><td>" + data[i].id + "</td><td>" + data[i].name + "</td><td>" + " <select class='form-control' id='" + type + "' name='" + type + "' disabled>" +
                                "<option class='blank'  value='" + data[i].type + "' selected>" + data[i].type + "</option>" +
                                "<option title='Un nombre entier de 4 octets. La fourchette des entiers relatifs est de -2 147 483 648 à 2 147 483 647. Pour les entiers positifs, cest de 0 à 4 294 967 295' >INT</option>" +
                                "<option title='Une chaîne de longueur variable (0-65,535), la longueur effective réelle dépend de la taille maximum d une ligne'>VARCHAR</option>" +
                                "<option title='Une colonne TEXT dune longueur maximum de 65 535 (2^16 - 1) caractères, stockée avec un préfixe de deux octets indiquant la longueur de la valeur en octets'>TEXT</option>" +
                                "<option title='Une date, la fourchette est de «1000-01-01» à «9999-12-31»'>DATETIME</option>" +
                                "<optgroup label='Numérique'>" +
                                "<option title='Un nombre entier de 1 octet. La fourchette des nombres avec signe est de -128 à 127. Pour les nombres sans signe, c est de 0 à 255'>TINYINT</option>" +
                                "<option title='Un nombre entier de 2 octets. La fourchette des nombres avec signe est de -32 768 à 32 767. Pour les nombres sans signe, c est de 0 à 65 535'>SMALLINT</option>" +
                                "<option title='Un nombre entier de 3 octets. La fourchette des nombres avec signe est de -8 388 608 à 8 388 607. Pour les nombres sans signe, c est de 0 à 16 777 215'>MEDIUMINT</option>" +
                                "<option title='Un nombre entier de 4 octets. La fourchette des entiers relatifs est de -2 147 483 648 à 2 147 483 647. Pour les entiers positifs, c est de 0 à 4 294 967 295'>INT</option>" +
                                "<option title='Un nombre entier de 8 octets. La fourchette des nombres avec signe est de -9 223 372 036 854 775 808 à 9 223 372 036 854 775 807. Pour les nombres sans signe, c est de 0 à 18 446 744 073 709 551 615'>BIGINT</option>" +
                                "</optgroup></select>" + "</td><td><input type='number' id='" + size + "' name='" + size + "' value='" + data[i].size + "' disabled/></td><td><button id='" + md + "' value='modifier' class='btn btn-warning'><span class='glyphicon glyphicon-pencil'></span></button></td></tr>")
                        }
                    },
                    error: function () { //erreur dans le cas les données ne sont pas envoyer on affiche un message qui indique l'erreur
                        console.log("error");
                    }
                });
            }else{
                alert("Veuillez ajouter une column");
                $('ul.setup-panel li a[href="#step-1"]').trigger('click');
            }
        });

    $("#activate-step-4").click(
        function(e) {
            //envoyer les donnees avec ajax
            e.preventDefault();
            if($("#tableaucontenus2").children().length>0) {
                $.ajax({
                    type: "POST",//la method à utiliser soit POST ou GET
                    url: "/validate", //lien de la servlet qui exerce le traitement sur les données
                    data: $('#form2').serialize(),// sign_in c'est l'id du form qui contient le bouton submit et toutes les champs à envoyer
                    success: function (data) {// le cas ou la requete est bien execute en reçoi les données serialiser par JSON dans la variable msg
                        //recuperation de la valeur stock dans l'attribut desactive
                        $('#modal-success').modal('show');
                    },
                    error: function () { //erreur dans le cas les données ne sont pas envoyer on affiche un message qui indique l'erreur
                        console.log("error");
                    }
                });
            }else{
                alert("Veuillez ajouter une column");
                $('ul.setup-panel li a[href="#step-1"]').trigger('click');
            }
        });


    $("#cols").change(function(){
        if($("#cols").val()!=null) {
            $('#activate-step-2').show();
            $('#validate').hide();
        }else {
            $('#activate-step-2').hide();
        }
    });

    $('#precedent').click(function () {
        $('ul.setup-panel li a[href="#step-1"]').trigger('click');
    });
    $('#precedent1').click(function () {
        $('ul.setup-panel li a[href="#step-2"]').trigger('click');
    });



    $("#cols").hide();
    $("#all").hide();
    $("#none").hide();
    $('#activate-step-2').hide();

    $("#all").click(function () {
        $("#cols").multiSelect('select_all');
    });

    $("#none").click(function(){
        $("#cols").multiSelect('deselect_all');
    });

    $('table').on('click', '.glyphicon-remove-sign', function(e){
        var r = confirm("Voulez vous vraiement supprimer ?");
        if (r == true) {
            $(this).closest('tr').remove();
            e.preventDefault();
            var id =  $(this).closest('tr').data('id');
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

            $.ajax()
        } else {
            console.log("ss");
        }
    });

    $('table').on('click', '.glyphicon-pencil', function(e){
        var id = $(this).closest('tr').data('id');
        $('#md\\['+id+'\\]').click(function() {
            e.preventDefault();
            console.log("id"+id);
            if ($(this).val() == "modifier") {
                console.log("modifier");
                console.log("idPencil" + id);
                console.log($(this).closest('tr').find("input"));
                console.log($(this).closest('tr').find("select"));
                $(this).val("enregistrer");
                $('#size\\['+id+'\\]').prop("disabled", false);
                $('#type\\['+id+'\\]').prop("disabled", false);
                $(this).removeClass('btn btn-warning');
                $(this).addClass("btn btn-success");
            } else {
                console.log("enregistrer");
                /*$.ajax({
                 type : "post",
                 url : "/save", //process to mail
                 data : $("#form3").serialize(),
                 success : function(response) {
                 alert("mdofication effectuer avec succes");
                 },
                 error : function() {
                 console.log("erreur");
                 }
                 });*/
                $(this).val("modifier ");
                $(this).removeClass('btn btn-success');
                $(this).addClass("btn btn-warning");
                $('#size\\['+id+'\\]').prop("disabled", true);
                $('#type\\['+id+'\\]').prop("disabled", true);
            }
        });
        });

        /*var r = confirm("Voulez vous vraiement modifier ?");
       / if (r == true) {
            console.log($(this));
            $(this).closest('tr').remove();

        } else {
            console.log("ss");
        }*/
});


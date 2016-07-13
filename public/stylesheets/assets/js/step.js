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
            $target.show();
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

    $("#activate-step-2").click(
        function(e) {
            //envoyer les donnees avec ajax
            e.preventDefault();
            $.ajax({
                type : "POST",//la method à utiliser soit POST ou GET
                url : "/upload", //lien de la servlet qui exerce le traitement sur les données
                data : $('#form').serialize(),// sign_in c'est l'id du form qui contient le bouton submit et toutes les champs à envoyer
                success : function(data) {// le cas ou la requete est bien execute en reçoi les données serialiser par JSON dans la variable msg
                    //recuperation de la valeur stock dans l'attribut desactive
                    $('ul.setup-panel li:eq(1)').removeClass('disabled');
                    $('ul.setup-panel li a[href="#step-2"]').trigger('click');
                    $(this).remove();
                    for(i=0;data.length;i++) {
                        var type = 'type['+data[i].id+']';
                        var selected = 'selected['+data[i].id+']';
                        var size = 'size['+data[i].id+']';
                        $("#tableaucontenus1").append("<tr><td>" + data[i].id + "</td><td>" + data[i].name + "</td>" +
                            "<td><select class='form-control' id='type' name='"+type+"'>" +
                            "<option class='blank'  value=''>Please select a value</option>" +
                            "<option title='Un nombre entier de 4 octets. La fourchette des entiers relatifs est de -2 147 483 648 à 2 147 483 647. Pour les entiers positifs, cest de 0 à 4 294 967 295'>INT</option>"+
                            "<option title='Une chaîne de longueur variable (0-65,535), la longueur effective réelle dépend de la taille maximum d'une ligne'>VARCHAR</option>"+
                            "<option title='Une colonne TEXT d'une longueur maximum de 65 535 (2^16 - 1) caractères, stockée avec un préfixe de deux octets indiquant la longueur de la valeur en octets'>TEXT</option>"+
                            "<option title='Une date, la fourchette est de «1000-01-01» à «9999-12-31»'>DATE</option>"+
                            "<optgroup label='Numérique'>"+
                        "<option title='Un nombre entier de 1 octet. La fourchette des nombres avec signe est de -128 à 127. Pour les nombres sans signe, c est de 0 à 255'>TINYINT</option>"+
                        "<option title='Un nombre entier de 2 octets. La fourchette des nombres avec signe est de -32 768 à 32 767. Pour les nombres sans signe, c est de 0 à 65 535'>SMALLINT</option>"+
                        "<option title='Un nombre entier de 3 octets. La fourchette des nombres avec signe est de -8 388 608 à 8 388 607. Pour les nombres sans signe, c est de 0 à 16 777 215'>MEDIUMINT</option>"+
                        "<option title='Un nombre entier de 4 octets. La fourchette des entiers relatifs est de -2 147 483 648 à 2 147 483 647. Pour les entiers positifs, c est de 0 à 4 294 967 295'>INT</option>"+
                        "<option title='Un nombre entier de 8 octets. La fourchette des nombres avec signe est de -9 223 372 036 854 775 808 à 9 223 372 036 854 775 807. Pour les nombres sans signe, c est de 0 à 18 446 744 073 709 551 615'>BIGINT</option>"+
                        "</optgroup></select>"+
                        "</td>"+
                            "<td><input type='text' name='"+size+"' size='4'/> </td>"+
                            "<td>"+
                        "<input type='checkbox'"+
                        "name='"+selected+"'"+
                        "id='selected'></td></tr>"
                        )
                    }
                },
                error : function() { //erreur dans le cas les données ne sont pas envoyer on affiche un message qui indique l'erreur
                    console.log("error");
                }
            });

        });


    $("#activate-step-3").click(
        function(e) {
            //envoyer les donnees avec ajax
            e.preventDefault();
            $.ajax({
                type : "POST",//la method à utiliser soit POST ou GET
                url : "/getCols", //lien de la servlet qui exerce le traitement sur les données
                data : $('#form1').serialize(),// sign_in c'est l'id du form qui contient le bouton submit et toutes les champs à envoyer
                success : function(data) {// le cas ou la requete est bien execute en reçoi les données serialiser par JSON dans la variable msg
                    //recuperation de la valeur stock dans l'attribut desactive
                    $('ul.setup-panel li:eq(2)').removeClass('disabled');
                    $('ul.setup-panel li a[href="#step-3"]').trigger('click');
                    $(this).remove();
                    for(i=0;data.length;i++) {
                        $("#tableaucontenus2").append("<tr><td>" + data[i].id + "</td><td>" + data[i].name + "</td><td>" + data[i].type + "</td></tr>")
                    }

                },
                error : function() { //erreur dans le cas les données ne sont pas envoyer on affiche un message qui indique l'erreur
                    console.log("error");
                }
            });

        });

    $("#activate-step-4").click(
        function(e) {
            //envoyer les donnees avec ajax
            e.preventDefault();
            $.ajax({
                type : "POST",//la method à utiliser soit POST ou GET
                url : "/validate", //lien de la servlet qui exerce le traitement sur les données
                data : $('#form2').serialize(),// sign_in c'est l'id du form qui contient le bouton submit et toutes les champs à envoyer
                success : function(data) {// le cas ou la requete est bien execute en reçoi les données serialiser par JSON dans la variable msg
                    //recuperation de la valeur stock dans l'attribut desactive
                   
                },
                error : function() { //erreur dans le cas les données ne sont pas envoyer on affiche un message qui indique l'erreur
                    console.log("error");
                }
            });
        });


});


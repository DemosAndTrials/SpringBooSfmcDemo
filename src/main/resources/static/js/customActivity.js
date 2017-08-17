requirejs.config({
    paths: {
        postmonger: 'js/postmonger'
    }
});

// Postmonger Events
// https://developer.salesforce.com/docs/atlas.en-us.noversion.mc-app-development.meta/mc-app-development/using-postmonger.htm
define(['postmonger'], function(Postmonger) {
    'use strict';

    console.log('*** ' + window.location.href  + ' ***');

    var connection = new Postmonger.Session();

    var tokens;
    var endpoints;
    var inArgPayload = {};
    var step = 1;

    // get the # of steps
	// get it from hidden field
    var numSteps = $('#numSteps').val();
    //var numSteps = getUrlParameter('numSteps');
    // do some error checking on the inbound num steps
    console.log("numSteps: " + numSteps);


    $(window).ready(function() {
        console.log("ready + request Endpoints");
        connection.trigger('ready');
        connection.trigger('requestEndpoints');

        // TODO for testing only remove this
       // post({ name: "test" });
        $("#driver").click(function(event){
            post({ name: "test" });
        });
    });

    // Broadcast when the next button has been clicked on the configuration modal.
    // The activity should respond by calling nextStep (or ready, if validation failed,
    // and the custom activity wants to prevent navigation to the next step).
    connection.on('clickedNext', function() {
        step++;
        console.log("clicked next step: " + step);
        connection.trigger('nextStep');
    });

    // Broadcast when the back button has been clicked on the configuration modal.
    // The activity should respond by calling prevStep (or ready, if validation failed,
    // and the custom activity wants to prevent navigation to the previous step).
    connection.on('clickedBack', function() {
        step--;
        console.log("clicked back: " + step);
        connection.trigger('prevStep');
    });

    // Broadcast when a new step has been loaded (either via button navigation,
    // or the user clicking on a step via the wizard). Returns a step payload.
    // Response: { key: 'step1', label: 'Step 1' }
    connection.on('gotoStep', function (stepPayload) {
        console.log("go to step: " + step);
        console.log("go to step payload: " + stepPayload);
        gotoStep(step);
        connection.trigger('ready');
    });

    // This listens for Journey Builder to send tokens
    // Parameter is either the tokens data or an object with an
    // "error" property containing the error message
    connection.on('getTokens', function( data ) {
        if( data.error ) {
            console.error( data.error );
        } else {
            tokens = data;
        }
        console.log('*** getTokens ***', data);
    });

    // - Broadcast in response to the first ready event called by the custom application.
    //   This is typically done on $(window).ready()
    // - Response (payload): { name: 'MyActivity', metaData: {}, arguments: {}, configurationArguments: {}, outcomes: {} }
    // - When the activity is dragged from the activity list initially (meaning that it has no existing data),
    //   the default activity structure is pulled from the custom application's config.json.
    //   If the activity is a configured activity, the existing saved JSON structure of the activity is passed.
    connection.on('initActivity', function(payload) {
        console.log('initActivity');
        if (payload) {
            inArgPayload = payload;

            console.log('payload',JSON.stringify(payload));

            var jsonPayload = payload['arguments'].execute.inArguments;

            if (typeof jsonPayload != "undefined" && jsonPayload.length > 0) {

                // get the keys from the arguments array
                for (var i = 0; i < jsonPayload.length; i++) {

                    var obj = jsonPayload[i];
                    var formKey = Object.keys(obj);
                    var selector = '#' + formKey;
                    var value = obj[formKey];

                    $(selector).val(value);
                }
            }
        }

        gotoStep(step);

    });

    function gotoStep(step) {
        $('.step').hide();
        var stepStr = '#step' + step;

        var event = new CustomEvent('isVisible',
            {
                detail: {
                    step: step
                },
                bubbles: true,
                cancelable: false
            });

        // console.log('Current step:'  + step);
        // console.log('Step String: ' + stepStr);
        // remove the case statement ... better handled by if statement
        // special cases ... first step and last step ..
        // if step 1, remove the back button
        // else, we have moved past step 1 and less than num steps, add a back button
        // if step == numSteps (add the done button)
        // if step < numSteps (add the next button)
        // if step > numSteps - we done
        if (step == 1) {
            console.log('Do not show back button');
            $(stepStr).show();
            connection.trigger('updateButton', { button: 'back', visible: false });
        }
        else if (step > 1 && step < numSteps) {
            console.log('Show back button');
            $(stepStr).show();
            connection.trigger('updateButton', { button: 'back', visible: true, enabled: true });
        }

        if (step == numSteps) {
            if(step != 1) {
                $(stepStr).show();
            }
            connection.trigger('updateButton', { button: 'next', text: 'done', visible: true });
        } else {
            console.log('Show next button');
            connection.trigger('updateButton', { button: 'next', text: 'next', enabled: true });
        }

        if (step > numSteps) {
            console.log('Saving');
            save();
        }

        document.dispatchEvent(event);
    }

    // ??
    connection.on('updateStep', function( data ) {
        // Called if the configuration flow needs to change
        console.log('*** updateStep ***', data);
    });

    // This listens for Journey Builder to send endpoints
    // Parameter is either the endpoints data or an object with an
    // "error" property containing the error message
    connection.on('getEndpoints', function( data ) {
        if( data.error ) {
            console.error( data.error );
        } else {
            endpoints = data;
        }
        console.log('*** getEndpoints ***', data);
    });

    // ??
    connection.on('requestPayload', function() {
        var payload = {};

        payload.options = {

        };

        //TODO: Shouldn't this come from the data?
        payload.flowDisplayName = 'Custom Activity';

        connection.trigger('getPayload', payload);
        console.log('*** requestPayload ***', payload);
    });

    // Journey Builder broadcasts this event to us after this module
    // sends the "ready" method. JB parses the serialized object which
    // consists of the Event Data and passes it to the
    // "config.js.save.uri" as a POST
    connection.on('populateFields', function(payload) {
        console.log('*** populateFields ***', payload);
    });

    function getUrlParameter(name) {
        name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
        var regex = new RegExp('[\\?&]' + name + '=([^&#]*)');
        var results = regex.exec(location.search);
        return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
    }

    function preparePayload() {

        var value = getMessage();


        // inArgPayload['arguments'].execute.inArguments.push({"displayMessage": value});

        console.log('Message: ' + value);
    }

    //
    // Save
    //
    function save() {
        console.log('*** save ***', inArgPayload['arguments']);

        inArgPayload['arguments'].execute.inArguments = []; // remove all the args, only save the last one

        // push all of the form names / values onto the args stack
        $('#genericActivity *').filter(':input').each(function(){
            console.log("ID: " + this.id + " Name: " + this.name + " Value: " + this.value); //your code here
            var key;

            this.id ? key = this.id : key = this.name;

            var formArg = {};
            formArg[key] = this.value;

            inArgPayload['arguments'].execute.inArguments.push(formArg);
        });

        connection.trigger('updateActivity',inArgPayload);
        inArgPayload.metaData.isConfigured = true;

        //$('form#genericActivity').submit(); // post form
        post(inArgPayload['arguments'].execute.inArguments);
    }

    //
    // Post
    //
    function post(args) {
        console.log('*** post ***');
        $.post(
            "ca/save",
            args,
            function(data) {
                console.log("data: ", data);
                alert(data);
                $('#stage').html(data);
            }
        );
    }

});

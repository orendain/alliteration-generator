 (function(mainApp) {
  mainApp(window.jQuery, window, document);
}(function($, window, document) {
  // The $ is now locally scoped
  $(function() {
    // The DOM is ready!

    // Select between alliterative and regular sentence types
    $('#generate-sentence-type input').change(function() {
      var selected = $(this).val();
      if (selected == 'regular') {
        $('#generate-letter').hide(500);
      }
      else {
        $('#generate-letter').show(500);
      }
    });

    // Open share windows in popup
    $('.popup').click(function(event) {
      var width  = 575,
          height = 400,
          left   = ($(window).width()  - width)  / 2,
          top    = ($(window).height() - height) / 2,
          url    = this.href,
          opts   = 'status=1' +
                   ',width='  + width  +
                   ',height=' + height +
                   ',top='    + top    +
                   ',left='   + left;

      window.open(url, 'twitter', opts);
      return false;
    });

    // Handle generate form's "Generate" button
    $('#generate-submit').click(function(event) {
      event.preventDefault();
      generate_processClick(event);
    });

    //$(".alert").alert();

  });


  // Code to load immediately



  function generate_processClick() {
    // Clear any previous InfoBox
    closeAlerts();

    var sentenceType = $('#generate-sentence-type input:checked').val();

    // Regular sentences
    if (sentenceType == 'regular') {
      generateRegularSentence();
    } // Alliterative sentences
    else {
      var letter = $('#generate-letter input').val();
      if (isLetter(letter)) {
        generateAlliterativeSentence(letter);
      }
      else {
        displayAlert("Incorrect Letter!", "Hey, that's not a valid letter.  Try again.", "alert alert-danger alert-dismissable fade in");
      }
    }
  }

  function addToSentenceList(sentence) {
    $('#generate-sentences textarea').prepend(sentence + "\n");
  }

  function generateRegularSentence() {
    var r = jsRoutes.controllers.Generator.getRegular();
    return $.ajax({
      url: r.url,
      type: r.type,
      success: function(tpl) {
        addToSentenceList(tpl);
      },
      error: function(err) {
        console.log(err);
      }
    });
  }

  function generateAlliterativeSentence(letter) {
    if (letter.length === 0) {
      letter = "?";
    }
    var r = jsRoutes.controllers.Generator.getAlliterative(letter);
    return $.ajax({
      url: r.url,
      type: r.type,
      /*context: this,
      data: {
        letter: "S"
      },*/
      success: function(tpl) {
        addToSentenceList(tpl);
      },
      error: function(err) {
        console.log(err);
      }
    });
  }

  function isLetter(str) {
    return str.length === 0 || (str.length === 1 && (str.match(/[a-z]/i) || str == "?"));
  }

  var alertsClosed = true;

  function closeAlerts() {
    if ($('.alert').length) {
      alertsClosed = false;
      $('.alert').bind('closed.bs.alert', function () {
        alertsClosed = true;
        if (queuedAlert) {
          renderAlert(queuedAlert);
          queuedAlert = null;
        }
      });
      $(".alert").alert('close');
    }
  }

  function displayAlert(title, text, classes) {
    var alertDiv = document.createElement("div");
    alertDiv.setAttribute("class", classes);

    var closeButton = document.createElement("button");
    closeButton.setAttribute("type", "button");
    closeButton.setAttribute("class", "close");
    closeButton.setAttribute("data-dismiss", "alert");
    closeButton.setAttribute("aria-hidden", "true");

    var closeContent = document.createTextNode('×');
    closeButton.appendChild(closeContent);

    var infoTitle = document.createElement("strong");
    var infoTitleContent = document.createTextNode(title);
    infoTitle.appendChild(infoTitleContent);

    var br = document.createElement("br");
    var infoMainContent = document.createTextNode(text);

    alertDiv.appendChild(closeButton);
    alertDiv.appendChild(infoTitle);
    alertDiv.appendChild(br);
    alertDiv.appendChild(infoMainContent);

    renderAlert(alertDiv);
  }

  var queuedAlert;
  function renderAlert(element) {
    if (alertsClosed) {
      var infoDiv = document.getElementById("status");
      infoDiv.appendChild(element);
    }
    else {
      queuedAlert = element;
    }
  }

  // TODO: JS-alternative to submitting forms
  // Handle add form's "Submit" button
  // Handle contact form's "Send" button

}));

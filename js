<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<style>
/* Popup container - can be anything you want */
.popup {
    position: relative;
    display: inline-block;
    cursor: pointer;
    -webkit-user-select: none;
    -moz-user-select: none;
    -ms-user-select: none;
    user-select: none;
}

/* The actual popup */
.popup .popuptext {
    opacity: 0;
    width: 250px;
    background-color: gray;
    color: #000;
    text-align: center;
    border-radius: 6px;
    padding: 8px 0;
    position: absolute;
    z-index: 1;
    bottom: 125%;
    left: 50%;
    margin-left: -80px;
}

/* Popup arrow */
.popup .popuptext::after {
    content: "";
    position: absolute;
    top: 100%;
    left: 50%;
    margin-left: -5px;
    border-width: 5px;
    border-style: solid;
    border-color: #555 transparent transparent transparent;
}

/* Toggle this class - hide and show the popup */
.popup .show {
    opacity: 1;
    -webkit-animation: fadeIn .5s;
    animation: fadeIn .5s;
}

.popup .fade {
	opacity: 0;
    -webkit-animation: fadeOut .5s;
    animation: fadeOut .5s;
    }


/* Add animation (fade in the popup) */
@-webkit-keyframes fadeIn {
	from {transform: scale(.5);}
    to{transform:scale(1);}
    from {opacity: 0;}
    to {opacity: 1;}
}

@keyframes fadeIn {
    from {transform: scale(.5);}
    to{transform:scale(1);}
    from {opacity: 0;}
    to {opacity: 1;}
}
@-webkit-keyframes fadeOut {
	from {transform: scale(1);}
    to{transform:scale(.5);}
    from {opacity: 1;}
    to {opacity: .5;}
}

@keyframes fadeOut {

  	from {transform: scale(1);}
    to{transform:scale(.5);}
    from {opacity: 1;}
    to {opacity: 0;}
}
</style>
</head>
<body style="text-align:center">

<h2>Popup</h2>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>


<div class="popup" onclick="myFunction('Moffit', 'https://americanlibrariesmagazine.org/wp-content/uploads/2017/08/design-moffitt-1.jpg', 'open 24 hours', '27', '85%', 'Wednesday', false, true)">Click me to toggle the popup!
  <span class="popuptext" id="myPopup">A Simple Popup!</span>
</div>

<script>

// When the user clicks on div, open the popup
function myFunction(library, image, hours, seats, capacity, day, sound, food) {
    var popup = document.getElementById("myPopup");
    var soundimage = '';
    var foodimg = '';
    if (sound) {
    soundimage = 'https://i.imgur.com/d4fQUV5.png';
    } else {
    soundimage = 'https://i.imgur.com/QuEuwB4.png';
    }
    if (food) {
    foodimg = 'https://i.imgur.com/J896HT9.png';
    }
    var text = '<img src =' + image + ' width = "100%" height = "auto"><p style = "text-align: left; line-height: 5px;"><span style="font-size: 13pt; font-family: "book antiqua", palatino;"><strong>&nbsp;&nbsp;&nbsp;' + library + '&nbsp;</strong></span></p><p style = "text-align: left;line-height: 5px;"><span style="font-size: 10pt; font-family: "book antiqua", palatino;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src =' + soundimage + ' width = "7%" height = "auto">&nbsp;<img src = ' + foodimg + ' width = "7%" height = "auto">&nbsp;'+ day + '&nbsp;Hours: ' + hours + '</p><p style = "text-align: left;line-height: 10px;"><span style="font-family: "book antiqua", palatino;"><strong>&nbsp;&nbsp;&nbsp;Open Seats:&nbsp;' + seats + '&nbsp;</strong></span></p><p style = "text-align: left;line-height: 5px;"><span style="font-family: "book antiqua", palatino;"><strong>&nbsp;&nbsp;&nbsp;Capacity:&nbsp;' + capacity + '&nbsp;full</strong></span></p><p style = "text-align: right; line-height: 5px;><span style="font-size: 13pt; font-family: "book antiqua", palatino;"><strong> See More &nbsp;&nbsp;&nbsp;&nbsp; </p>';
    popup.innerHTML = text;
    if (popup.style.opacity == 0) {
    popup.classList.remove("fade");
    popup.classList.add("show");
    popup.style.opacity = 1;
    } else {
    popup.classList.remove("show");
    popup.classList.add("fade");
    popup.style.opacity = 0;
    }

}
</script>

</body>
</html>

//THIS IS A SMALL FUNCTIONALITY TO SHOW USER LOGIN AND LOG OUT STUFF

function getUser(userData) {

    if (userData.length == 0) {
        $("#login").show();
        $("#upload").hide();
    }  else {
        $("#logout").show();
        $("#upload").show();
    }
}

$.get("/user", getUser);

//THIS IS GETTING A LIST OF PHOTOS

function getPhotos(photosData) {
    for (var i in photosData) {
        var elem = $("<a>");
        elem.attr("href", "photosdisplay/" + photosData[i].filename);
        elem.text(photosData[i].filename);
        $("#photos").append(elem);
        var elem2 = $("<br>");
        $("#photos").append(elem2);
    }
}

$.get("/photos", getPhotos);

//THIS IS FOR DISPLAYING THE ACTUAL PHOTOS

function showPhoto(photoData) {
        var photo = photoData;
        var elem = $("<img>");
        elem.attr("src", photo.filename,"height=&#822042&#8221 width=&#822042&#8221");
        $("#photosdisplay").append(elem);
}

$.get("/sessionPhoto",showPhoto);

$.get("/delete");


//THIS WAS SET TO RELOAD PAGE AUTOMATICLY BUT IT RELOADS TO FAST
// $.get("/reload",location.reload(false));
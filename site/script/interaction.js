let leftBox = document.getElementById("left-box");
let rightBox = document.getElementById("right-box");

let boxExpanded = false;

// let canvas = document.getElementById("bg-canvas"); already defined in gallifreyan.js

function resizeBoxes() {

    // TODO clean this up it looks stupid
    if (screenIsVertical()) {
        leftBox.style.width = "80vw";
        rightBox.style.width = "80vw";
        leftBox.style.height = "20vh";
        rightBox.style.height = "20vh";
        leftBox.style.left = "10vw";
        rightBox.style.left = "10vw";
        leftBox.style.top = "2.5vh";
        rightBox.style.top = "77.5vh";
    } else {
        leftBox.style.width = "20vw";
        rightBox.style.width = "20vw";
        leftBox.style.height = "80vh";
        rightBox.style.height = "80vh";
        leftBox.style.left = "2.5vw";
        rightBox.style.left = "77.5vw";
        leftBox.style.top = "10vh";
        rightBox.style.top = "10vh";
    }
}

window.addEventListener('resize', resizeBoxes, false);
resizeBoxes();


function bruh() {
    console.log("bruh");
    canvas.style.animation = "fade-out 0.25s";
    canvas.style.animationFillMode = "forwards";
    rightBox.style.animation = "fade-out 0.5s";
    rightBox.style.animationFillMode = "forwards";
    leftBox.style.animation = `box-full-${screenIsVertical() ? "vert" : "horiz"} 1s`;
    leftBox.style.animationFillMode = "forwards";
}

function expandRightBox(){
    console.log("bruh");
    canvas.style.animation = "fade-out 0.25s";
    canvas.style.animationFillMode = "forwards";
    leftBox.style.animation = "fade-out 0.5s";
    leftBox.style.animationFillMode = "forwards";
    rightBox.style.animation = `box-full-${screenIsVertical() ? "vert" : "horiz"} 1s`;
    rightBox.style.animationFillMode = "forwards";
    let projectBox = document.getElementById("project-box");
    projectBox.style.borderBottomRightRadius = "0";

    let detailsBox = document.getElementById("details-box");
    detailsBox.hidden = false;
    detailsBox.style.borderBottomLeftRadius = "0";
    detailsBox.style.borderLeft = "2px solid white";
}

function selectProject(element) {
    if (!boxExpanded) {
        expandRightBox();
    }
    document.querySelectorAll(".project").forEach(function(project){
        project.style.background = "black";
    });
    element.style.background = "#202020";
}

function languageToggle(checkbox) {
    setLanguage(checkbox.checked ? "ja-JP" : "en-US");
}
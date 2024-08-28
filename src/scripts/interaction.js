import {screenIsVertical} from "./util.js";

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

function expandRightBox() {
    // hide canvas
    canvas.style.animation = "fade-out 0.25s";
    canvas.style.animationFillMode = "forwards";

    // hide left box
    leftBox.style.animation = "fade-out 0.5s";
    leftBox.style.animationFillMode = "forwards";

    // expand right box
    rightBox.style.animation = `box-full-${screenIsVertical() ? "vert" : "horiz"} 1s`;
    rightBox.style.animationFillMode = "forwards";

    // remove rounded edges of bottom right of project box
    let projectBox = document.getElementById("project-box");
    projectBox.style.borderBottomRightRadius = "0";

    // show details box, remove rounded edge from bottom left, and give border (could do last 2 at page load)
    let detailsBox = document.getElementById("details-box");
    detailsBox.hidden = false;
    detailsBox.style.borderBottomLeftRadius = "0";
    detailsBox.style.borderLeft = "2px solid white";
    detailsBox.style.animation = "details-box-widen 1s";
    detailsBox.style.animationFillMode = "forwards";
}

function selectProject(element, updateURL = true) {
    if (!boxExpanded) {
        expandRightBox();
    }
    document.querySelectorAll(".project").forEach(function (project) {
        project.style.background = "black";
    });
    element.style.background = "#202020";

    let detailsIframe = document.getElementById("details-iframe");

    // fade out to prevent flashing when reloading
    detailsIframe.style.animation = "fade-out 0.25s";
    detailsIframe.style.animationFillMode = "forwards";

    // set the frame to change source only after completely faded out, to make sure flashing isn't seen
    // TODO could prob make this only set once
    detailsIframe.onanimationend = function (event) {
        // make sure this only happens for the fade out animation
        if (event.animationName === "fade-out") {
            detailsIframe.src = `./site/iframes/${element.dataset.name}.html`;
        }
    }

    // fade in once the page is loaded
    detailsIframe.onload = function () {
        detailsIframe.style.animation = "fade-in 0.25s";
        detailsIframe.style.animationFillMode = "forwards";
        updateText(detailsIframe.contentWindow.document);
    };

    if (updateURL) {
        window.location.hash = element.dataset.name;
    }
}

export function checkURLHash() {
    const hashLocation = window.location.hash.substring(1);
    let project;
    if (hashLocation !== "" && (project = document.querySelector(`.project[data-name=${hashLocation}]`)) !== null) {
        selectProject(project, false);
    }
}
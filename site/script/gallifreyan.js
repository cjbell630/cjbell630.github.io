let tick = 0;
let canvas = document.getElementById("bg-canvas");
let ctx = canvas.getContext("2d");

//get DPI
let dpi = window.devicePixelRatio || 1;
ctx.scale(dpi, dpi);
console.log(dpi);

fix_dpi();
let centerX = canvas.width/2;
let centerY = canvas.height/2;
let outerRad = centerY*0.9;
let innerRad = centerY*0.7;
let chunkDist = Math.sin(19*Math.PI/36)*innerRad/Math.sin(13*Math.PI/36);
let chunkRad = centerY*0.7*Math.sin(Math.PI/9)/Math.sin(13*Math.PI/36);


function fix_dpi() {
//get CSS height
//the + prefix casts it to an integer
//the slice method gets rid of "px"
    let style_height = +getComputedStyle(canvas).getPropertyValue("height").slice(0, -2);
    let style_width = +getComputedStyle(canvas).getPropertyValue("width").slice(0, -2);

//scale the canvas
    canvas.setAttribute('height', style_height * dpi);
    canvas.setAttribute('width', style_width * dpi);
}



function drawGallifreyan() {
    //console.log("frame");
    fix_dpi();
    ctx.clearRect(0, 0, canvas.width, canvas.height);

    ctx.strokeStyle = "#ffffff"
    ctx.beginPath();

    // draw outer ring
    ctx.arc(centerX, centerY, outerRad, 0, Math.PI * 2);
    ctx.stroke();

    ctx.beginPath();

    // draw inner ring (with missing 40 deg / 2pi/9 rad chunk)
    ctx.arc(centerX, centerY, innerRad, (tick+200)*Math.PI/180, (tick+160)*Math.PI/180);

    ctx.stroke();

    ctx.beginPath();
    // draw inner ring chunk (46pi/36 chunk
    ctx.arc(centerX-(chunkDist*Math.cos(tick*Math.PI/180)), centerY-chunkDist*Math.sin(tick*Math.PI/180), chunkRad, (tick+295)*Math.PI/180, (tick+65)*Math.PI/180);

    ctx.stroke();
    tick = (tick + 1) % 360;
    window.requestAnimationFrame(drawGallifreyan);
}


window.requestAnimationFrame(drawGallifreyan);
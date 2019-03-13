window.onload = function () {
    // selcted
   
    colorContainer = document.getElementById("color")
    alert(colorContainer)
    colors = ["red","blue","green","gold","aqua"]
    for (let index = 0; index < colors.length; index++) {
        elm = document.createElement("span")
        elm.class = " colorType " 
        elm.style.background = colors[index]
        elm.addEventListener("click",chnageColor);
        colorContainer.appendChild(elm)
    }
    function chnageColor()
    {
        alert(this.style.background);
    }
}
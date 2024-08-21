let description_box = document.querySelectorAll('.description_box');

description_box.forEach(element => {
    let button = element.querySelector(".read_more_button");
    button.onclick = function () {
        if (element.style.height === `${element.scrollHeight}px`) {
            element.style.height = '200px';
            button.innerHTML = "Read More...";
        } else {
            element.style.height = `${element.scrollHeight}px`;
            button.innerHTML = "Read Less";
        }
        button.classList.toggle("active_button");
    }
});

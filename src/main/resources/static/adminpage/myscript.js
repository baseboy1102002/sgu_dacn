function getParentElement(element, parent) {
    while (element.parentElement) {
        if (element.parentElement.matches(parent)) {
            return element.parentElement;
        }
        element = element.parentElement;
    }
}

$(document).ready(function () {
    let c = $(".btn_seemore").length;
    let btn_seemore = $(".btn_seemore")
    for (let index = c-1; index > 0; index--) {
        btn_seemore[index].click()
    }
});

$(".btn_seemore").each(function (index, element) {
    $(element).click(function (e) {
        e.preventDefault();
        if($(this).hasClass("hide"))
            $(this).html("Ẩn bớt <i class='fa-solid fa-chevron-up'></i>").removeClass("hide");
        else
            $(this).html("Xem <i class='fa-solid fa-chevron-down'></i>").addClass("hide");

        const list_items = getParentElement(element, ".list-group-item").nextElementSibling

        if($(list_items).hasClass("group-item_dp-none"))
            $(list_items).animate({maxHeight:220},200).removeClass("group-item_dp-none");
        else
            $(list_items).animate({maxHeight:0},200).addClass("group-item_dp-none");
    });
});
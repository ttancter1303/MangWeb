
$(document).ready(function () {
    $("#back-to-top").click(() => {
        $("html").scrollTop(0);
    })

    $('.box-img img').each(function () {
        $(this).on('load', function () {
            // $(this).siblings('.box-skeleton').remove();
            $(this).siblings(".box-skeleton").remove();
        });
    });
});
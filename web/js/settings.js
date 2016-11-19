//live search filter games
$('.live-search-box').on('keyup', function(){
    var searchTerm = $(this).val().toLowerCase();

    $('.live-search-list li').each(function(){
        if ($(this).filter('[data-search-term *= ' + searchTerm + ']').length > 0 || searchTerm.length < 1) {
            $(this).show();
        } else {
            $(this).hide();
        }
    });
});








//geen idee waarom togglen images niet meer werkt
$('.addGamesImg').click(function(){
    $(this).toggleClass('active');
});








/*<![CDATA[*/
$(document).ready(function() {

    // CDATA tags above are used to inject Spring thymeleaf dynamic variables into Javascript
    $('.third.circle-bar').circleProgress({
        value: [[${AdaptiveLearningChartInverseScore}]],
        fill: {gradient: [['#06bfd3', .5], ['#06bfd3', .5]], gradientAngle: Math.PI / 4}
    }).on('circle-animation-progress', function(event, progress, stepValue) {
        $(this).find('strong').html(parseInt([[${AdaptiveLearningChartScore}]] * progress) + '<i>%</i>');
    });


    $('.curriculum-proficiency.circle-bar').circleProgress({
      value: 1.00,
      fill: {gradient: [['#06bfd3', .5], ['#06bfd3', .5]], gradientAngle: Math.PI / 4}
    }).on('circle-animation-progress', function(event, progress, stepValue) {
      $(this).find('strong').html(parseInt(10 * progress));
    });

});
/*]]>*/
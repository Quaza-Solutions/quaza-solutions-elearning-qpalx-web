function initializeChart() {
        google.charts.load('current', {'packages':['corechart']});
        google.charts.setOnLoadCallback(drawChart);
}

function showTable() {
    console.log("showtable clicked");
    $("#table_div").show();
    $("#chart_div").hide();
}

function showChart() {
    console.log("show chart clicked");
    $("#table_div").hide();
    $("#chart_div").show();
}

// this function will get called immediately when a bootstrap modal is shown.
// piggy backing off this behavior to immediately initialize and load google charts.
$(window).on('shown.bs.modal', function() {
    console.log("Modal is now visible invoking initializeChart();");
    initializeChart();
});


function drawChart() {

     var quizID = $("#QuizID").val();

     $.ajax({ type: 'GET',
                 url: 'http://localhost:8080/StudentQuizPerformance?QuizID=' + quizID,
                 datatype: 'json',
                 success : function(studentQuizResults)
                 {
                    var data = new google.visualization.DataTable(studentQuizResults);
                    data.addColumn('date', 'Time of Day');
                    data.addColumn('number', 'Quiz Score');

                    var resultsHTMLTable = "<table width='100%' border='0' class='table-striped'>";
                    resultsHTMLTable += "<tr><td><b>Date</b></td><td><b>Score</b></td></tr>";

                    $.each(studentQuizResults, function( index, studentQuizResult) {
                        var quizScorePercent = studentQuizResult.quizScore;
                        var quizScoreDtTime = studentQuizResult.javaScriptSafeDateTime;

                        // Convert date time to array in order to create a JavaScript date object
                        var dateArray = quizScoreDtTime.split(',');
                        var date = new Date(dateArray[0], dateArray[1], dateArray[2], dateArray[3], dateArray[4]);

                        resultsHTMLTable += "<tr><td>" + studentQuizResult.userFriendlyDateTimeDisplay + "</td><td>" + quizScorePercent + "</td></tr>";

                        // Add date with score to Google chart data
                        data.addRow([date, quizScorePercent]);
                    });

                    resultsHTMLTable += "</table>";

                    // Set all options and draw the actual chart
                    var options = {
                      title: 'Your Performance:  Relations Quiz I',
                      width: 900,
                      height: 500,
                      lineWidth: 5,
                      hAxis: {
                          title: 'Quiz Date',
                        format: 'MM/dd/yyyy hh:mm'
                      },
                      vAxis: {
                          title: 'Quiz Score(%)',
                        maxValue: 100,
                        gridlines: {color: 'none'},
                        minValue: 0
                      },
                      series: {
                       0: { color: '#06bfd3' }
                      }
                    };

                    var chart = new google.visualization.AreaChart(document.getElementById('chart_div'));
                    console.log(resultsHTMLTable);
                    document.getElementById('table_div').innerHTML = resultsHTMLTable;

                    chart.draw(data, options);
                 }
     });

     // In Javascript months start with index 0
     // new Date(Year, Month, Day, Hours, Minutes, Seconds, Milliseconds)
        <!--data.addRows([-->
          <!--[new Date(2017, 0, 1, 09, 00), 50],  [new Date(2017, 0, 1, 12, 30), 70],  [new Date(2017, 0, 3, 9, 30), 30],-->
          <!--[new Date(2017, 0, 4, 13, 03), 90],  [new Date(2017, 0, 5, 14, 04), 80],  [new Date(2017, 0, 6, 17, 30), 40]-->
        <!--]);-->
}

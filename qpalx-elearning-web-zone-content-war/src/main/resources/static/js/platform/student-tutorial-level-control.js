$(document).ready(function () {
    // Listen to QPalx Country Region Select for any changes.  Changes will trigger loading of Academic levels in that Region
    $('#municipalityID').change(function() {
        var municipalityID = $(this).val();
        console.log("Municipality Currently Selected: "+ municipalityID);
        loadMunicipalityAcademicLevels(municipalityID);
    });

});

$(document).ready(function () {
    // Listen to QPalx AcademicLevel Select for changes.  Changes will trigger loading or Tutorial Grades for AcademicLevel
    $('#studentTutorialLevelID').change(function() {
        var studentTutorialLevelID = $(this).val();
        console.log("studentTutorialLevelID Currently Selected: "+ studentTutorialLevelID);
        loadStudentTutorialGrades(studentTutorialLevelID);
    });
});

$(document).ready(function () {
    // Listen to QPalx TutorialGrade Select for any changes.  Changes will trigger loading of all Schools configured in QPalX that match TutorialGrade
    $('#tutorialGradeID').change(function() {
        var municipalityID = $('#municipalityID').val();
        var studentTutorialLevelID = $('#studentTutorialLevelID').val();
        console.log("Currently selected municipalityID: "+ municipalityID + " studentTutorialLevelID: " + studentTutorialLevelID);
        loadAllSchoolsByMunicipalityAndAcademicLevel(municipalityID, studentTutorialLevelID);
    });
});

function loadMunicipalityAcademicLevels(municipalityID) {
    console.log("Executing remote call for all QPalx AcademicLevels in MunicipalityID: "+municipalityID)

    $.ajax({ type: 'GET',
                     url: 'http://localhost:8080/find-academic-levels-by-municipality?municipalityID=' + municipalityID,
                     datatype: 'json',
                     success : function(academicLevels)
                     {
                        // clear out all old options in other select options
                        clearAllStudentLevelSelection(true, false, false);

                        $.each(academicLevels, function( index, academicLevel) {
                            var id = academicLevel.id;
                            var tutorialLevel = academicLevel.tutorialLevel;

                            console.log("Appending tutorialLevel: " +tutorialLevel+" to AcademicLevels Select");

                            // Append new options received from the Server to drop down
                            $('#studentTutorialLevelID').append($("<option />").val(id).text(tutorialLevel));
                        });
                     }
         });
}


function loadStudentTutorialGrades(studentTutorialLevelID) {
    console.log("Executing remote call for all QPalx TutorailGrades in studentTutorialLevelID: "+studentTutorialLevelID)

    $.ajax({ type: 'GET',
                     url: 'http://localhost:8080/find-student-tutorial-grades-by-academic-level?studentTutorialLevelID=' + studentTutorialLevelID,
                     datatype: 'json',
                     success : function(tutorialGrades)
                     {
                        // clear out old data in the AcademicLevels Select dropdown and build from new results
                        clearAllStudentLevelSelection(false, true, false);

                        $.each(tutorialGrades, function( index, tutorialGrade) {
                            var id = tutorialGrade.id;
                            var tutorialGrade = tutorialGrade.tutorialGrade;

                            console.log("Appending tutorialGrade: " +tutorialGrade+" to TutorialGrade Select")

                            // Append new options received from the Server to drop down
                            $('#tutorialGradeID').append("<option value='" + id + "'>" + tutorialGrade + "</option>")
                        });
                     }
         });
}

function loadAllSchoolsByMunicipalityAndAcademicLevel(municipalityID, studentTutorialLevelID) {
    console.log("Executing remote call to load all Schools in municipalityID:" + municipalityID + " studentTutorialLevelID: " + studentTutorialLevelID)

    $.ajax({ type: 'GET',
                     url: 'http://localhost:8080/FindEducationalInstitutionsMatching?municipalityID=' + municipalityID + '&studentTutorialLevelID=' + studentTutorialLevelID,
                     datatype: 'json',
                     success : function(schools)
                     {
                        // clear out old data in the AcademicLevels Select dropdown and build from new results
                        clearAllStudentLevelSelection(false, false, true);

                        $.each(schools, function( index, school) {
                            var id = school.id;
                            var name = school.name;

                            console.log("Appending School: " +name+" to Schools select")

                            // Append new options received from the Server to drop down
                            $('#educationalInstitutionID').append("<option value='" + id + "'>" + name + "</option>")
                        });
                     }
        });
}

function clearAllStudentLevelSelection(refreshAcademicLevel, refreshTutorialGrade, refreshQSchools) {
    if(refreshAcademicLevel) {
        $('#studentTutorialLevelID').empty();
        $('#studentTutorialLevelID').append("<option value=''>Choose Your Academic Level</option>");

        // empty out all the other select as well
        $('#tutorialGradeID').empty();
        $('#educationalInstitutionID').empty();
    }

    if(refreshTutorialGrade) {
        $('#tutorialGradeID').empty();
        $('#tutorialGradeID').append("<option value=''>Choose Your Grade</option>");
    }

    if(refreshQSchools) {
        $('#educationalInstitutionID').empty();
        $('#educationalInstitutionID').append("<option value=''>Choose Your School</option>");
    }
}

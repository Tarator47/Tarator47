package com.tarator.gui

class PipelineNames {

    static final String devPrefix = ""
    static final String rootFolder = devPrefix == "" ? "Games" : "Games_DEV"
    static final String folderSudoku = rootFolder + "/Sudoku"
    static final String jobFolderSudoku = devPrefix == "" ? "${folderSudoku}" : "${folderSudoku}/${devPrefix}"

    //Sudoku
    static final String BUILD_TEST_PIPELINE = "${jobFolderSudoku}/${devPrefix}${devPrefix != "" ? "-" : ""}Build-Test"
    static final String INTEGRATION_TEST_PIPELINE = "${jobFolderSudoku}/${devPrefix}${devPrefix != "" ? "-" : ""}Integration-Test"
}
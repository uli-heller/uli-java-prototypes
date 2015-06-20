https://code.google.com/p/projectlombok/issues/detail?id=826&colspec=ID%20Type%20Status%20Priority%20Target%20Component%20Owner%20Summary

What steps will reproduce the problem?

1. Install eclipse-jee-mars-RC3
2. Copy the installation and install lombok-1.16.4 within the copy
3. Create a new eclipse project and within this, create the java class mentioned below (... it contains an error)
4. Try to do a quickfix

What is the expected output? What do you see instead?

* The list of potential fixes shows up in any case
* Without lombok, you see a small preview window of the quickfix when hovering over the list
* Without lombok, the quickfix is applied when clicking on the list item
* With lombok, the preview is missing
* With lombok, the quickfix isn't applied when clicking on the list item
* Please note: There are some quickfixes which seem to work, for example "import class"

What version of the product are you using? On what operating system?

* Linux, Ubuntu1404, 64 bit
* Lombok-1.16.4 (latest version)
* Eclipse-Jee-Mars-RC3, 64 bit

Please provide any additional information below.

* Java class: QuickFix.java added as attachment

# Document-Archival-System
This is an Android App we are making for safekeeping of documents of IIT PATNA.

What we are doing:-

1. Click image of any doucument or import from phone's gallery.
2. Extract text from this document through OCR. We have used ML kit Vision provided by Google for this.
3. Connect app with IIT PATNA Server. We have used JSCH library for this.
4. Classify document on the basis of folders present on server, by the text we extracted and upload it to
   the folder to which it belongs.
5. There is a file searching system also through which you will get list of all images present on server which
   contains the word user searched.

How to use this app:-
1. Click any photo using camera option in menu present or import any photo.
2. Go to OCR option in menu for extracting text .(Result will appear automatically.)
3. Choose Upload option present in homesreen for uploading.
4. Go to SEARCH option in menu for searching files.
/*
 Uploading and Searching features will not work if you are not connected with IIT PATNA Server.
*/ 
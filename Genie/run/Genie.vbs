Set oShell = CreateObject ("Wscript.Shell") 
Dim strArgs
strArgs = "cmd /k del c:\users\886599\Desktop\Genie.lnk & echo f|xcopy ""C:\Users\886599\Documents\My Workshop\BizzTools\Genie\shortcuts\GenieX.lnk"" c:\users\886599\Desktop\GenieX.lnk & cd ""C:\Users\886599\Documents\My Workshop\BizzTools\Genie"" & java Genie & exit"
oShell.Run strArgs, 0, false
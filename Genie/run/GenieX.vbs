Set oShell = CreateObject ("Wscript.Shell") 
Dim strArgs
strArgs = "cmd /k del c:\users\886599\Desktop\GenieX.lnk & echo f|xcopy ""C:\Users\886599\Documents\My Workshop\BizzTools\Genie\shortcuts\Genie.lnk"" c:\users\886599\Desktop\Genie.lnk & taskkill /f /im ""java.exe"" & exit"
oShell.Run strArgs, 0, false
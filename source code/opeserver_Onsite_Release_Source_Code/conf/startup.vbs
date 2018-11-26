Dim oShell
Dim responseVar
	Set oShell = WScript.CreateObject ("WSCript.shell")
responseVar = MsgBox("Please press Ok to start the FTP Server" , 1 , "OPE")

If responseVar = 1 Then

	oShell.run "java -jar ./lib/opeserver.jar",0
	Set oShell = Nothing
	responseVare = MsgBox ("OPE Server started successfully" , 0 , "Server response" )

End If





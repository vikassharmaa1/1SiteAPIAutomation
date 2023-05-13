set projectLocation=C:\HCL\1SITEAPIAUTOMATION\1Site_API
cd %projectLocation%
cmd /k mvn clean test -DsuiteXmlFile=TestRunner.xml
pause
test_ps:
ifdef groups
	mvn test -Dgroups=$(groups) -DsuiteXmlFile="PS_Suite.xml"
else
	mvn test -DsuiteXmlFile="PS_Suite.xml"
endif

test_wcs:
ifdef groups
	mvn test -Dgroups=$(groups) -DsuiteXmlFile="WCS_Suite.xml"
else
	mvn test -DsuiteXmlFile="WCS_Suite.xml"
endif

help:
	@echo "Usage examples:"
	@echo "make test_ps"
	@echo "make test_wcs"
	@echo "make groups=new test_ps"
